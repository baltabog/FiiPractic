package com.fii.practic.mes.admin.service.material;

import com.fii.practic.mes.admin.domain.material.MaterialEntity;
import com.fii.practic.mes.admin.domain.material.MaterialRepository;
import com.fii.practic.mes.admin.domain.material.MaterialService;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.MaterialDTO;
import com.fii.practic.mes.models.SearchType;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.http.HttpServerResponse;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MaterialServiceTests {
    private static final String UPDATED_BY = "system";
    public static final String TEST_MATERIAL = "TEST_MATERIAL";
    @Inject
    MaterialService service;
    @Inject
    MaterialRepository repository;

    private MaterialDTO getMinimalValidCreateDto() {
        return new MaterialDTO()
                .name(TEST_MATERIAL)
                .availableQuantity(10)
                .quantityUnit("g");
    }

    private MaterialDTO getMinimalValidUpdateDto() {
        return getMinimalValidCreateDto()
                .uuid(UUID.randomUUID().toString())
                .updatedBy(UPDATED_BY)
                .version(1);
    }

    @Test
    public void testCreateNoBody() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(null));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: create.dto"));
    }

    @Test
    public void testCreateWithNullName() {
        MaterialDTO materialDTO = getMinimalValidCreateDto()
                .name(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(materialDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateTypeEmptyName() {
        MaterialDTO materialDTO = getMinimalValidCreateDto()
                .name(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(materialDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateSucceed() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();

        MaterialDTO responseDto = service.create(materialDTO);

        assertMaterialsEquality(materialDTO, responseDto);
    }

    @Test
    public void testUpdateNullBody() {
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(null));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("update.dto"));
    }
    @Test
    public void testUpdateNullName() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdMaterialDto = service.create(materialDTO);
        createdMaterialDto.setName(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdMaterialDto));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("update.dto.name"));
    }

    @Test
    public void testUpdateNullVersion() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdMaterialDto = service.create(materialDTO);
        createdMaterialDto.setVersion(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdMaterialDto));

        assertTrue(runtimeException.getMessage()
                .contains("version must not be null"));
    }

    @Test
    public void testUpdateNullUuid() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdMaterialDto = service.create(materialDTO);
        createdMaterialDto.setUuid(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdMaterialDto));

        assertTrue(runtimeException.getMessage()
                .contains("uuid must not be null"));
    }

    @Test
    public void testUpdateSuccess() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdMaterialDto = service.create(materialDTO);
        createdMaterialDto.setDescription("description");

        MaterialDTO updatedDto = service.update(createdMaterialDto);
        assertMaterialsEquality(createdMaterialDto, updatedDto);
    }

    @Test
    public void testGetByNullIdentity() {
        IdentityDTO identityDTO =  null;
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO responseDto = service.create(materialDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO"));
    }

    @Test
    public void testGetByNullIdentityWithNullUuid() {
        IdentityDTO identityDTO =  new IdentityDTO();
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO responseDto = service.create(materialDTO);
        identityDTO.setName(responseDto.getName());

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO.uuid"));
    }

    @Test
    public void testGetByNullIdentityWithNullName() {
        IdentityDTO identityDTO =  new IdentityDTO();
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO responseDto = service.create(materialDTO);
        identityDTO.setUuid(responseDto.getUuid());

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO.name"));
    }

    @Test
    public void testGetByNullIdentityWithEmptyName() {
        IdentityDTO identityDTO =  new IdentityDTO();
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO responseDto = service.create(materialDTO);
        identityDTO.setName(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO.name"));
    }

    @Test
    public void testGetByNullIdentityWithEmptyUuid() {
        IdentityDTO identityDTO =  new IdentityDTO();
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO responseDto = service.create(materialDTO);
        identityDTO.setName(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO.name"));
    }

    @Test
    public void testGetByIdentitySuccess() {
        IdentityDTO identityDTO =  new IdentityDTO();
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdDto = service.create(materialDTO);
        identityDTO.setUuid(createdDto.getUuid());
        identityDTO.setName(createdDto.getName());

        MaterialEntity getByIdentityDto = service.getByIdentity(identityDTO);
        assertMaterialsEquality(createdDto, getByIdentityDto);
    }

    @Test
    public void testReadNegativeSearchOffset() {
        HttpServerResponse httpServerResponse = Mockito.mock(HttpServerResponse.class);

        for (int index = 1; index <= 5; index ++) {
            MaterialDTO materialDTO = getMinimalValidCreateDto()
                    .name(TEST_MATERIAL + index);
            MaterialDTO createdDto = service.create(materialDTO);
        }

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.read(new SearchType().offset(-1), httpServerResponse));

        assertTrue(runtimeException.getMessage().contains("message: must be greater than or equal to 0"));
        assertTrue(runtimeException.getMessage().contains("property path: read.searchType.offset"));
    }

    @Test
    public void testReadNegativeSearchLimit() {
        HttpServerResponse httpServerResponse = Mockito.mock(HttpServerResponse.class);

        for (int index = 1; index <= 5; index ++) {
            MaterialDTO materialDTO = getMinimalValidCreateDto()
                    .name(TEST_MATERIAL + index);
            MaterialDTO createdDto = service.create(materialDTO);
        }

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.read(new SearchType().limit(-1), httpServerResponse));

        assertTrue(runtimeException.getMessage().contains("message: must be greater than or equal to 0"));
        assertTrue(runtimeException.getMessage().contains("property path: read.searchType.limit"));
    }


    @Test
    public void testDeleteNullUuid() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdDto = service.create(materialDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(null, createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.uuid"));
    }

    @Test
    public void testDeleteWrongUuid() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdDto = service.create(materialDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(UUID.randomUUID().toString(), createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("failed"));
    }

    @Test
    public void testDeleteSuccess() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdDto = service.create(materialDTO);

        service.delete(createdDto.getUuid(), createdDto.getVersion());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.getByIdentity(new IdentityDTO()
                .uuid(createdDto.getUuid())
                .name(createdDto.getName())));
        assertTrue(runtimeException.getMessage().contains("Error finding Material with id"));

    }

    @Test
    public void testDeleteNullVersion() {
        MaterialDTO materialDTO = getMinimalValidCreateDto();
        MaterialDTO createdDto = service.create(materialDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(createdDto.getUuid(), null));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.version"));
    }

    private void assertMaterialsEquality(MaterialDTO expected, MaterialDTO actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong material.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong material.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong material.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong material.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong material.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated())) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong material.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong material.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong material.updatedBy");
        assertEquals(expected.getAvailableQuantity(), actual.getAvailableQuantity(), "Wrong material.availableQuantity");
        assertEquals(expected.getQuantityUnit(), actual.getQuantityUnit(), "Wrong material.quantityUnit");
        assertNotNull(actual.getUpdated(), "Wrong material.updated");
    }

    private void assertMaterialsEquality(MaterialDTO expected, MaterialEntity actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong material.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong material.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong material.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong material.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong material.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated().atOffset(OffsetDateTime.now().getOffset()))) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong material.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong material.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong material.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong material.updated");
    }

    @BeforeEach
    public void beforeEach() {
        QuarkusTransaction.begin();
        repository.deleteAll();
        QuarkusTransaction.commit();
    }

}
