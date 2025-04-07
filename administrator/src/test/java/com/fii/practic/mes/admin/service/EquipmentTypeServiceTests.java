package com.fii.practic.mes.admin.service;

import com.fii.practic.mes.admin.domanin.equipment.type.EquipmentTypeEntity;
import com.fii.practic.mes.admin.domanin.equipment.type.EquipmentTypeRepository;
import com.fii.practic.mes.admin.domanin.equipment.type.EquipmentTypeService;
import com.fii.practic.mes.models.EquipmentTypeDTO;
import com.fii.practic.mes.models.IdentityDTO;
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
public class EquipmentTypeServiceTests {
    private static final String UPDATED_BY = "system";
    public static final String TEST_TYPE = "testType";
    @Inject
    EquipmentTypeService service;
    @Inject
    EquipmentTypeRepository repository;

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
    public void testCreateTypeWithNullName() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO();

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(equipmentTypeDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateTypeWithEmptyName() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(equipmentTypeDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateSucceed() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);

        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);

        assertEquipmentTypesEquality(equipmentTypeDTO, responseDto);
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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdEquipmentTypeDto = service.create(equipmentTypeDTO);
        createdEquipmentTypeDto.setName(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdEquipmentTypeDto));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("update.dto.name"));
    }

    @Test
    public void testUpdateNullVersion() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdEquipmentTypeDto = service.create(equipmentTypeDTO);
        createdEquipmentTypeDto.setVersion(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdEquipmentTypeDto));

        assertTrue(runtimeException.getMessage()
                .contains("version must not be null"));
    }

    @Test
    public void testUpdateNullUuid() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdEquipmentTypeDto = service.create(equipmentTypeDTO);
        createdEquipmentTypeDto.setUuid(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdEquipmentTypeDto));

        assertTrue(runtimeException.getMessage()
                .contains("uuid must not be null"));
    }

    @Test
    public void testUpdateName() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdEquipmentTypeDto = service.create(equipmentTypeDTO);
        createdEquipmentTypeDto.setName("new name");

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdEquipmentTypeDto));

        assertTrue(runtimeException.getMessage()
                .contains("The server denied the request to update <EquipmentTypeDTO> name."));
    }

    @Test
    public void testUpdateSuccess() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdEquipmentTypeDto = service.create(equipmentTypeDTO);
        createdEquipmentTypeDto.setDescription("description");

        EquipmentTypeDTO updatedDto = service.update(createdEquipmentTypeDto);
        assertEquipmentTypesEquality(createdEquipmentTypeDto, updatedDto);
    }

    @Test
    public void testGetByNullIdentity() {
        IdentityDTO identityDTO =  null;
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);

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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);
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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);
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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);
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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO responseDto = service.create(equipmentTypeDTO);
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
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);
        identityDTO.setUuid(createdDto.getUuid());
        identityDTO.setName(createdDto.getName());

        EquipmentTypeEntity getByIdentityDto = service.getByIdentity(identityDTO);
        assertEquipmentTypesEquality(createdDto, getByIdentityDto);
    }

    @Test
    public void testReadNegativeSearchOffset() {
        HttpServerResponse httpServerResponse = Mockito.mock(HttpServerResponse.class);

        for (int index = 1; index <= 5; index ++) {
            EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                    .name(TEST_TYPE + index);
            EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);
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
            EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                    .name(TEST_TYPE + index);
            EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);
        }

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.read(new SearchType().limit(-1), httpServerResponse));

        assertTrue(runtimeException.getMessage().contains("message: must be greater than or equal to 0"));
        assertTrue(runtimeException.getMessage().contains("property path: read.searchType.limit"));
    }


    @Test
    public void testDeleteNullUuid() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(null, createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.uuid"));
    }

    @Test
    public void testDeleteWrongUuid() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(UUID.randomUUID().toString(), createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("failed"));
    }

    @Test
    public void testDeleteSuccess() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);

        service.delete(createdDto.getUuid(), createdDto.getVersion());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.getByIdentity(new IdentityDTO()
                .uuid(createdDto.getUuid())
                .name(createdDto.getName())));
        assertTrue(runtimeException.getMessage().contains("Error finding EquipmentType with id"));

    }

    @Test
    public void testDeleteNullVersion() {
        EquipmentTypeDTO equipmentTypeDTO = new EquipmentTypeDTO()
                .name(TEST_TYPE);
        EquipmentTypeDTO createdDto = service.create(equipmentTypeDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(createdDto.getUuid(), null));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.version"));
    }

    private void assertEquipmentTypesEquality(EquipmentTypeDTO expected, EquipmentTypeDTO actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong equipmentType.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong equipmentType.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong equipmentType.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong equipmentType.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong equipmentType.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated())) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong equipmentType.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong equipmentType.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong equipmentType.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong equipmentType.updated");
    }

    private void assertEquipmentTypesEquality(EquipmentTypeDTO expected, EquipmentTypeEntity actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong equipmentType.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong equipmentType.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong equipmentType.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong equipmentType.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong equipmentType.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated().atOffset(OffsetDateTime.now().getOffset()))) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong equipmentType.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong equipmentType.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong equipmentType.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong equipmentType.updated");
    }

    @BeforeEach
    public void beforeEach() {
        QuarkusTransaction.begin();
        repository.deleteAll();
        QuarkusTransaction.commit();
    }

}
