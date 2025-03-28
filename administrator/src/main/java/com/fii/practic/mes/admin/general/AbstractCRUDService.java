package com.fii.practic.mes.admin.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fii.practic.mes.admin.general.dto.CreateArtificialDto;
import com.fii.practic.mes.admin.general.dto.UpdateArtificialDto;
import com.fii.practic.mes.admin.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.admin.general.error.ServerErrorEnum;
import com.fii.practic.mes.admin.general.search.HQLSearchExecutor;
import com.fii.practic.mes.admin.general.search.SearchTransformer;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.SearchType;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractCRUDService<D extends Serializable, E extends AbstractEntity> {
    protected static final RuntimeException NOT_IMPLEMENTED_EXCEPTION = new ApplicationRuntimeException(ServerErrorEnum.NOT_IMPLEMENTED);

    @Inject
    protected Validator validator;
    @Inject
    protected ObjectMapper objectMapper;

    protected abstract D mapToDto(E entity);
    protected abstract E mapToEntity(D dto);
    protected abstract E mapToEntity(E entity, D dto);

    protected abstract String getDtoName();
    protected abstract String getEntityName();

    protected abstract AbstractRepository<E> getRepository();

    public D create(@Valid @NotNull D dto) {
        if (!isCreateAllowed()) {
            throw NOT_IMPLEMENTED_EXCEPTION;
        }

        CreateArtificialDto createArtificialDto = objectMapper.convertValue(dto, CreateArtificialDto.class);
        validate(createArtificialDto);

        QuarkusTransaction.begin();
        E entity = createEntityFromDto(dto, createArtificialDto);
        getRepository().persist(entity);
        QuarkusTransaction.commit();

        return mapToDto(entity);
    }
    protected E createEntityFromDto(D dto, CreateArtificialDto createArtificialDto) {
        getRepository().findOneIfExist("name = ?1", createArtificialDto.getName())
                .ifPresent((e) -> { throw new ApplicationRuntimeException(ServerErrorEnum.FK_NAME_ALREADY_IN_USE); });

        E entity = mapToEntity(dto);
        if (StringUtils.isEmpty(entity.getUuid())) {
            entity.setUuid(UUID.randomUUID().toString());
        }

        entity.setUpdatedBy(getUpdatedBy());
        return entity;
    }
    protected abstract boolean isCreateAllowed();

    public E getByIdentity(@NotNull @Valid IdentityDTO identityDTO) {
        final E entity = getRepository().findMandatoryByUuid(identityDTO.getUuid());
        if (!Objects.equals(entity.getName(), identityDTO.getName())) {
            throw new ApplicationRuntimeException(ServerErrorEnum.UUID_NAME_NOT_MATCH, getEntityName(), identityDTO.getUuid());
        }

        return entity;
    }

    public List<D> read(@Valid SearchType searchType,
                        @NotNull final HttpServerResponse response) {
        if (!isReadAllowed()) {
            throw NOT_IMPLEMENTED_EXCEPTION;
        }

        if (Objects.isNull(searchType)) {
            searchType = new SearchType().sortBy("asc:name");
        }

        final Map<String, Object> params = new HashMap<>();
        String queryStr = createSearchQuery(params, searchType);
        Sort sorting = SearchTransformer.getSorting(searchType.getSortBy(), getDtoName());
        Page page = SearchTransformer.getPage(searchType);

        QuarkusTransaction.begin();
        List<D> dtoList = HQLSearchExecutor.executeSearch(queryStr, params, sorting, response, getRepository(), page)
                .map(this::mapToDto)
                .toList();
        QuarkusTransaction.commit();

        return dtoList;
    }
    protected String createSearchQuery(Map<String, Object> params, SearchType search) {
        String searchQueryString = SearchTransformer.getQueryString(search.getFilter(), params, getDtoName());
        String queryStr = String.format("select distinct e from %s e",
                getEntityName());
        queryStr += StringUtils.isNoneBlank(searchQueryString) ? " where " + searchQueryString: "";

        return queryStr;
    }
    protected abstract boolean isReadAllowed();

    public D update(@Valid @NotNull D dto) {
        if (!isUpdateAllowed()) {
            throw NOT_IMPLEMENTED_EXCEPTION;
        }

        UpdateArtificialDto updateArtificialDto = objectMapper.convertValue(dto, UpdateArtificialDto.class);
        validate(updateArtificialDto);

        QuarkusTransaction.begin();
        E entity = updateEntityWithDto(dto, updateArtificialDto);
        getRepository().persist(entity);
        QuarkusTransaction.commit();

        return mapToDto(entity);
    }
    protected E updateEntityWithDto(D dto, UpdateArtificialDto updateArtificialDto) {
        E entity = getRepository().findMandatoryVersionedByUuid(updateArtificialDto.getUuid(), updateArtificialDto.getVersion());
        if (!Objects.equals(entity.getName(), updateArtificialDto.getName())) {
            if (!isNameUpdateAllowed()) {
                throw new ApplicationRuntimeException(ServerErrorEnum.UPDATE_NAME_NOT_ALLOWED, getDtoName());
            }
            getRepository().findOneIfExist("name = ?1", updateArtificialDto.getName())
                    .ifPresent((e) -> { throw new ApplicationRuntimeException(ServerErrorEnum.FK_NAME_ALREADY_IN_USE); });
        }
        entity = mapToEntity(entity, dto);
        entity.setUpdatedBy(getUpdatedBy());
        return entity;
    }
    protected abstract boolean isUpdateAllowed();
    protected abstract boolean isNameUpdateAllowed();

    public void delete(@NotNull @Size(min=1,max=36) String uuid,
                       @NotNull Integer version) {
        if (!isDeleteAllowed()) {
            throw NOT_IMPLEMENTED_EXCEPTION;
        }

        QuarkusTransaction.begin();
        deleteEntityBasedOnUuidAndVersion(uuid, version);
        QuarkusTransaction.commit();
    }

    private void deleteEntityBasedOnUuidAndVersion(String uuid, Integer version) {
        long deletedEntries = getRepository().delete("uuid = ?1 and version = ?2", uuid, version);
        if (deletedEntries != 1) {
            throw new ApplicationRuntimeException(ServerErrorEnum.DELETE_ENTITY_FAIL, getEntityName(),
                    uuid, String.valueOf(version));
        }
    }

    protected abstract boolean isDeleteAllowed();

    /**
     * Unimplemented because APIs are not authorized.
     *
     * @return User name of the caller.
     */
    protected String getUpdatedBy() {
        return "system";
    }

    /**
     * Validate objects
     *
     * @throws ApplicationRuntimeException ServerErrorEnum.VALIDATOR_CONSTRAINT_VIOLATION
     */
    protected void validate(final Object... objects) {
        for(final Object obj : objects) {
            final Set<ConstraintViolation<Object>> violations = validator.validate(obj);
            if (!violations.isEmpty()) {
                final ConstraintViolation<Object> vio = violations.iterator().next();
                throw new ApplicationRuntimeException(ServerErrorEnum.VALIDATOR_CONSTRAINT_VIOLATION,
                        vio.getPropertyPath() + " " + vio.getMessage());
            }
        }
    }
}
