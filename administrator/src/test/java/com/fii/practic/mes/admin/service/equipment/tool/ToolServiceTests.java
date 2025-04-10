package com.fii.practic.mes.admin.service.equipment.tool;

import com.fii.practic.mes.admin.domain.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.domain.equipment.tool.ToolRepository;
import com.fii.practic.mes.admin.domain.equipment.tool.ToolService;
import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeRepository;
import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeService;
import com.fii.practic.mes.models.EquipmentTypeDTO;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.SearchType;
import com.fii.practic.mes.models.ToolDTO;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.http.HttpServerResponse;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
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
public class ToolServiceTests {
    private static final String UPDATED_BY = "system";
    public static final String TEST_TOOL = "TEST_TOOL";
    public static final String TEST_TYPE_1 = "testType1";
    @Inject
    ToolService service;
    @Inject
    ToolRepository repository;
    @Inject
    EquipmentTypeService typeService;
    @Inject
    EquipmentTypeRepository typeRepository;

    private ToolDTO getMinimalValidCreateDto() {
        return new ToolDTO()
                .name(TEST_TOOL)
                .active(true)
                .equipmentTypeIdentity(new IdentityDTO()
                        .uuid(UUID.randomUUID().toString())
                        .name(TEST_TYPE_1)
                );
    }

    private ToolDTO getMinimalValidUpdateDto() {
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
        ToolDTO toolDTO = getMinimalValidCreateDto()
                .name(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(toolDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateTypeEmptyName() {
        ToolDTO toolDTO = getMinimalValidCreateDto()
                .name(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(toolDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateSucceed() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();

        ToolDTO responseDto = service.create(toolDTO);

        assertToolsEquality(toolDTO, responseDto);
    }

    private EquipmentTypeDTO createEquipmentType(String typeName) {
        return typeService.create(
                new EquipmentTypeDTO()
                        .name(typeName)
        );
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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdToolDto = service.create(toolDTO);
        createdToolDto.setName(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdToolDto));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("update.dto.name"));
    }

    @Test
    public void testUpdateNullVersion() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdToolDto = service.create(toolDTO);
        createdToolDto.setVersion(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdToolDto));

        assertTrue(runtimeException.getMessage()
                .contains("version must not be null"));
    }

    @Test
    public void testUpdateNullUuid() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdToolDto = service.create(toolDTO);
        createdToolDto.setUuid(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdToolDto));

        assertTrue(runtimeException.getMessage()
                .contains("uuid must not be null"));
    }

    @Test
    public void testUpdateSuccess() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdToolDto = service.create(toolDTO);
        createdToolDto.setDescription("description");

        ToolDTO updatedDto = service.update(createdToolDto);
        assertToolsEquality(createdToolDto, updatedDto);
    }

    @Test
    public void testGetByNullIdentity() {
        IdentityDTO identityDTO =  null;
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO responseDto = service.create(toolDTO);

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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO responseDto = service.create(toolDTO);
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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO responseDto = service.create(toolDTO);
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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO responseDto = service.create(toolDTO);
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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO responseDto = service.create(toolDTO);
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
        ToolDTO toolDTO = getCreateToolDtoWithValidType();

        ToolDTO createdDto = service.create(toolDTO);
        identityDTO.setUuid(createdDto.getUuid());
        identityDTO.setName(createdDto.getName());

        ToolEntity getByIdentityDto = service.getByIdentity(identityDTO);
        assertToolsEquality(createdDto, getByIdentityDto);
    }

    private ToolDTO getCreateToolDtoWithValidType() {
        EquipmentTypeDTO equipmentType = createEquipmentType(TEST_TYPE_1);
        ToolDTO toolDTO = getMinimalValidCreateDto()
                .equipmentTypeIdentity(new IdentityDTO()
                        .name(equipmentType.getName())
                        .uuid(equipmentType.getUuid()));
        return toolDTO;
    }

    @Test
    public void testReadNegativeSearchOffset() {
        HttpServerResponse httpServerResponse = Mockito.mock(HttpServerResponse.class);

        EquipmentTypeDTO equipmentType = createEquipmentType(TEST_TYPE_1);
        for (int index = 1; index <= 5; index ++) {
            ToolDTO toolDTO = getMinimalValidCreateDto()
                    .equipmentTypeIdentity(new IdentityDTO()
                            .uuid(equipmentType.getUuid())
                            .name(equipmentType.getName()))
                    .name(TEST_TOOL + index);
            ToolDTO createdDto = service.create(toolDTO);
        }

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.read(new SearchType().offset(-1), httpServerResponse));

        assertTrue(runtimeException.getMessage().contains("message: must be greater than or equal to 0"));
        assertTrue(runtimeException.getMessage().contains("property path: read.searchType.offset"));
    }

    @Test
    public void testReadNegativeSearchLimit() {
        HttpServerResponse httpServerResponse = Mockito.mock(HttpServerResponse.class);

        EquipmentTypeDTO equipmentType = createEquipmentType(TEST_TYPE_1);
        for (int index = 1; index <= 5; index ++) {
            ToolDTO toolDTO = getMinimalValidCreateDto()
                    .equipmentTypeIdentity(new IdentityDTO()
                            .uuid(equipmentType.getUuid())
                            .name(equipmentType.getName()))
                    .name(TEST_TOOL + index);
            ToolDTO createdDto = service.create(toolDTO);
        }

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.read(new SearchType().limit(-1), httpServerResponse));

        assertTrue(runtimeException.getMessage().contains("message: must be greater than or equal to 0"));
        assertTrue(runtimeException.getMessage().contains("property path: read.searchType.limit"));
    }


    @Test
    public void testDeleteNullUuid() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdDto = service.create(toolDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(null, createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.uuid"));
    }

    @Test
    public void testDeleteWrongUuid() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdDto = service.create(toolDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(UUID.randomUUID().toString(), createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("failed"));
    }

    @Test
    public void testDeleteSuccess() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdDto = service.create(toolDTO);

        service.delete(createdDto.getUuid(), createdDto.getVersion());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.getByIdentity(new IdentityDTO()
                .uuid(createdDto.getUuid())
                .name(createdDto.getName())));
        assertTrue(runtimeException.getMessage().contains("Error finding Equipment with id"));
    }

    @Test
    public void testDeleteNullVersion() {
        ToolDTO toolDTO = getCreateToolDtoWithValidType();
        ToolDTO createdDto = service.create(toolDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(createdDto.getUuid(), null));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.version"));
    }

    private void assertToolsEquality(ToolDTO expected, ToolDTO actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong tool.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong tool.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong tool.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong tool.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong tool.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated())) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong tool.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong tool.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong tool.updatedBy");
        assertEquals(expected.getActive(), actual.getActive(), "Wrong tool.active");
        assertEquals(expected.getEquipmentTypeIdentity(), actual.getEquipmentTypeIdentity(), "Wrong tool.equipmentTypeIdentity");
        assertNotNull(actual.getUpdated(), "Wrong tool.updated");
    }

    private void assertToolsEquality(ToolDTO expected, ToolEntity actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong tool.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong tool.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong tool.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong tool.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong tool.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated().atOffset(OffsetDateTime.now().getOffset()))) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong tool.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong tool.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong tool.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong tool.updated");
    }

    @BeforeEach
    public void beforeEach() {
        cleanDb();
    }

    @AfterAll
    @ActivateRequestContext
    public void afterAll() {
        cleanDb();
    }

    private void cleanDb() {
        QuarkusTransaction.begin();
        repository.deleteAll();
        typeRepository.deleteAll();
        QuarkusTransaction.commit();
    }

}
