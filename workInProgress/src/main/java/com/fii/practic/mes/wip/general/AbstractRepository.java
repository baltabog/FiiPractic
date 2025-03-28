package com.fii.practic.mes.wip.general;

import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.StaleObjectStateException;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractRepository<E extends AbstractEntity> implements PanacheRepository<E> {
	private static final Logger log = Logger.getLogger(AbstractRepository.class);
	@Getter(AccessLevel.PROTECTED)
	private final Class<E> entityClazz;
	@Getter
	private final String entityName;

	protected AbstractRepository(final Class<E> entityClazz, String entityName) {
		super();
		this.entityClazz = entityClazz;
		this.entityName = entityName;
	}

	public E findMandatoryByUuid(final String uuid) {
		try {
			return findOneMandatory("uuid = ?1", uuid);
		} catch (ApplicationRuntimeException are) {
			logErrorQueryInfo("uuid = ?1", uuid);
			throw new ApplicationRuntimeException(ServerErrorEnum.FIND_ERROR_BY_ID, entityName, uuid);
		}
	}

	public E findMandatoryVersionedByUuid(final String uuid, final int version) {
		final E entity = findMandatoryByUuid(uuid);
		if (!Objects.equals(entity.getVersion(), version)) {
			logErrorQueryInfo("uuid = ?1 and version = ?2", uuid, version);
			throw new StaleObjectStateException(entityName, uuid);
		}

		return entity;
	}

	public Optional<E> findOneIfExist(final String query, Object... param) {
		final List<E> entityList = list(query, param);
		if (CollectionUtils.size(entityList) > 1) {
			logErrorQueryInfo(query, param);
			throw new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
					"Find one return multiple fields");
		}
		if (CollectionUtils.isEmpty(entityList)) {
			return Optional.empty();
		}

		return Optional.of(entityList.get(0));
	}

	public E findOneMandatory(final String query, Object... param) {
		final List<E> entityList = list(query, param);
		if (CollectionUtils.size(entityList) > 1) {
			logErrorQueryInfo(query, param);
			throw new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
					"Find one return multiple fields.");
		}
		if (CollectionUtils.isEmpty(entityList)) {
			logErrorQueryInfo(query, param);
			throw new ApplicationRuntimeException(ServerErrorEnum.UNEXPECTED_EXCEPTION,
					"Entity not found.");
		}

		return entityList.get(0);
	}

	private void logErrorQueryInfo(String query, Object... param) {
		log.error("""
				EntityName: %s
				Query: %s
				Param: %s
				""".formatted(entityName, query, Arrays.toString(param)));
	}
}