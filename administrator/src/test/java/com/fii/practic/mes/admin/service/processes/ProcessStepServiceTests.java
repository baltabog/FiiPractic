package com.fii.practic.mes.admin.service.processes;

import com.fii.practic.mes.admin.domain.equipment.tool.ToolRepository;
import com.fii.practic.mes.admin.domain.equipment.tool.ToolService;
import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeRepository;
import com.fii.practic.mes.admin.domain.equipment.type.EquipmentTypeService;
import com.fii.practic.mes.admin.domain.material.MaterialRepository;
import com.fii.practic.mes.admin.domain.material.MaterialService;
import com.fii.practic.mes.admin.domain.process.step.ProcessStepEntity;
import com.fii.practic.mes.admin.domain.process.step.ProcessStepRepository;
import com.fii.practic.mes.admin.domain.process.step.ProcessStepService;
import com.fii.practic.mes.models.EquipmentTypeDTO;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.MaterialDTO;
import com.fii.practic.mes.models.ProcessStepDTO;
import com.fii.practic.mes.models.ProcessStepMaterialDTO;
import com.fii.practic.mes.models.ToolDTO;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProcessStepServiceTests {
    private static final String UPDATED_BY = "system";
    public static final String TEST_PROCESS_STEP = "testProcessStep";
    public static final String TEST_MATERIAL_1 = "testMaterial1";
    public static final String TEST_MATERIAL_2 = "testMaterial2";
    public static final String TEST_MATERIAL_3 = "testMaterial3";
    public static final String TEST_TOOL = "testTool";
    public static final String TEST_TYPE = "testType";
    @Inject
    ProcessStepService service;
    @Inject
    ProcessStepRepository repository;
    @Inject
    MaterialService materialService;
    @Inject
    MaterialRepository materialRepository;
    @Inject
    ToolService toolService;
    @Inject
    ToolRepository toolRepository;
    @Inject
    EquipmentTypeService typeService;
    @Inject
    EquipmentTypeRepository typeRepository;

    private ProcessStepDTO getMinimalValidCreateDto() {
        return new ProcessStepDTO()
                .name(TEST_PROCESS_STEP)
                .inputMaterials(Set.of(
                        new ProcessStepMaterialDTO()
                                .uuid(UUID.randomUUID().toString())
                                .name(TEST_MATERIAL_1)
                                .quantity(2)
                ))
                .successOutputMaterials(Set.of(
                        new ProcessStepMaterialDTO()
                                .uuid(UUID.randomUUID().toString())
                                .name(TEST_MATERIAL_2)
                                .quantity(1)
                ))
                .failOutputMaterials(Set.of(
                        new ProcessStepMaterialDTO()
                                .uuid(UUID.randomUUID().toString())
                                .name(TEST_MATERIAL_3)
                                .quantity(1)
                ))
                .equipments(Set.of(
                        new IdentityDTO()
                                .uuid(UUID.randomUUID().toString())
                                .name(TEST_TOOL)
                ));
    }

    private ProcessStepDTO getMinimalValidUpdateDto() {
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
        ProcessStepDTO processStepDTO = getMinimalValidCreateDto()
                .name(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(processStepDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateStepWithEmptyName() {
        ProcessStepDTO processStepDTO = getMinimalValidCreateDto()
                .name(StringUtils.EMPTY);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(processStepDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: size must be between"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.name"));
    }

    @Test
    public void testCreateStepWithNullInputMaterials() {
        ProcessStepDTO processStepDTO = getMinimalValidCreateDto()
                .inputMaterials(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(processStepDTO));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("create.dto.inputMaterials"));
    }

    @Test
    public void testCreateStepWithInputMaterialNotFound() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        ProcessStepDTO createDto = getMinimalValidCreateDto()
                .successOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                                .uuid(material1.getUuid())
                                .name(material1.getName())
                                .quantity(1)))
                .failOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)));

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(createDto));
        assertTrue(runtimeException.getMessage().contains("Error finding Material with id"));
    }
    @Test
    public void testCreateStepWithSuccessOutputMaterialNotFound() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        ProcessStepDTO createDto = getMinimalValidCreateDto()
                .inputMaterials(Set.of(new ProcessStepMaterialDTO()
                                .uuid(material1.getUuid())
                                .name(material1.getName())
                                .quantity(1)))
                .failOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)));

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(createDto));
        assertTrue(runtimeException.getMessage().contains("Error finding Material with id"));
    }
    @Test
    public void testCreateStepWithSuccessFailMaterialNotFound() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        ProcessStepDTO createDto = getMinimalValidCreateDto()
                .successOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                                .uuid(material1.getUuid())
                                .name(material1.getName())
                                .quantity(1)))
                .inputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)));

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(createDto));
        assertTrue(runtimeException.getMessage().contains("Error finding Material with id"));
    }

    @Test
    public void testCreateStepWithEquipmentNotFound() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        MaterialDTO material3 = createMaterial(TEST_MATERIAL_3);
        ProcessStepDTO createDto = getMinimalValidCreateDto()
                .failOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material3.getUuid())
                        .name(material3.getName())
                        .quantity(1)))
                .successOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material1.getUuid())
                        .name(material1.getName())
                        .quantity(1)))
                .inputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)));

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.create(createDto));
        assertTrue(runtimeException.getMessage().contains("Error finding Equipment with id"));
    }

    private MaterialDTO createMaterial(String materialTest) {
        return materialService.create(
                new MaterialDTO()
                        .name(materialTest)
                        .quantityUnit("g")
                        .availableQuantity(10)
        );
    }

    @Test
    public void testCreateSucceed() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        MaterialDTO material3 = createMaterial(TEST_MATERIAL_3);
        EquipmentTypeDTO type = createType(TEST_TYPE);
        ToolDTO tool = createTool(TEST_TOOL, type);
        ProcessStepDTO processStepDTO = getMinimalValidCreateDto()
                .failOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material3.getUuid())
                        .name(material3.getName())
                        .quantity(1)))
                .successOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material1.getUuid())
                        .name(material1.getName())
                        .quantity(1)))
                .inputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)))
                .equipments(Set.of(new IdentityDTO()
                        .uuid(tool.getUuid())
                        .name(tool.getName())));


        ProcessStepDTO responseDto = service.create(processStepDTO);

        assertProcessStepsEquality(processStepDTO, responseDto);
    }

    private EquipmentTypeDTO createType(String typeName) {
        return typeService.create(new EquipmentTypeDTO().name(typeName));
    }

    private ToolDTO createTool(String toolName, EquipmentTypeDTO typeDTO) {
        return toolService.create(
                new ToolDTO()
                        .name("toolName")
                        .active(true)
                        .equipmentTypeIdentity(new IdentityDTO()
                                .uuid(typeDTO.getUuid())
                                .name(typeDTO.getName()))
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
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdProcessStepDto = service.create(processStepDTO);
        createdProcessStepDto.setName(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdProcessStepDto));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("update.dto.name"));
    }

    @Test
    public void testUpdateNullVersion() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdProcessStepDto = service.create(processStepDTO);
        createdProcessStepDto.setVersion(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdProcessStepDto));

        assertTrue(runtimeException.getMessage()
                .contains("version must not be null"));
    }

    @Test
    public void testUpdateNullUuid() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdProcessStepDto = service.create(processStepDTO);
        createdProcessStepDto.setUuid(null);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.update(createdProcessStepDto));

        assertTrue(runtimeException.getMessage()
                .contains("uuid must not be null"));
    }

    @Test
    public void testUpdateSuccess() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdProcessStepDto = service.create(processStepDTO);
        createdProcessStepDto.setDescription("description");

        ProcessStepDTO updatedDto = service.update(createdProcessStepDto);
        assertProcessStepsEquality(createdProcessStepDto, updatedDto);
    }

    @Test
    public void testGetByNullIdentity() {
        IdentityDTO identityDTO =  null;
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO responseDto = service.create(processStepDTO);

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
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO responseDto = service.create(processStepDTO);
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
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();

        ProcessStepDTO responseDto = service.create(processStepDTO);
        identityDTO.setUuid(responseDto.getUuid());

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.getByIdentity(identityDTO));

        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("getByIdentity.identityDTO.name"));
    }

    private ProcessStepDTO getProcessStepCreateDtoWithValidDependencies() {
        MaterialDTO material1 = createMaterial(TEST_MATERIAL_1);
        MaterialDTO material2 = createMaterial(TEST_MATERIAL_2);
        MaterialDTO material3 = createMaterial(TEST_MATERIAL_3);
        EquipmentTypeDTO type = createType(TEST_TYPE);
        ToolDTO tool = createTool(TEST_TOOL, type);
        ProcessStepDTO processStepDTO = getMinimalValidCreateDto()
                .failOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material3.getUuid())
                        .name(material3.getName())
                        .quantity(1)))
                .successOutputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material1.getUuid())
                        .name(material1.getName())
                        .quantity(1)))
                .inputMaterials(Set.of(new ProcessStepMaterialDTO()
                        .uuid(material2.getUuid())
                        .name(material2.getName())
                        .quantity(1)))
                .equipments(Set.of(new IdentityDTO()
                        .uuid(tool.getUuid())
                        .name(tool.getName())));
        return processStepDTO;
    }

    @Test
    public void testGetByNullIdentityWithEmptyName() {
        IdentityDTO identityDTO =  new IdentityDTO();
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO responseDto = service.create(processStepDTO);
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
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO responseDto = service.create(processStepDTO);
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
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdDto = service.create(processStepDTO);
        identityDTO.setUuid(createdDto.getUuid());
        identityDTO.setName(createdDto.getName());

        ProcessStepEntity getByIdentityDto = service.getByIdentity(identityDTO);
        assertProcessStepsEquality(createdDto, getByIdentityDto);
    }

    @Test
    public void testDeleteNullUuid() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdDto = service.create(processStepDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(null, createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.uuid"));
    }

    @Test
    public void testDeleteWrongUuid() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdDto = service.create(processStepDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(UUID.randomUUID().toString(), createdDto.getVersion()));
        assertTrue(runtimeException.getMessage()
                .contains("failed"));
    }

    @Test
    public void testDeleteSuccess() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdDto = service.create(processStepDTO);

        service.delete(createdDto.getUuid(), createdDto.getVersion());

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> service.getByIdentity(new IdentityDTO()
                .uuid(createdDto.getUuid())
                .name(createdDto.getName())));
        assertTrue(runtimeException.getMessage().contains("Error finding ProcessStep with id"));

    }

    @Test
    public void testDeleteNullVersion() {
        ProcessStepDTO processStepDTO = getProcessStepCreateDtoWithValidDependencies();
        ProcessStepDTO createdDto = service.create(processStepDTO);

        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> service.delete(createdDto.getUuid(), null));
        assertTrue(runtimeException.getMessage()
                .contains("message: must not be null"));
        assertTrue(runtimeException.getMessage()
                .contains("property path: delete.version"));
    }

    private void assertProcessStepsEquality(ProcessStepDTO expected, ProcessStepDTO actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong processStep.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong processStep.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong processStep.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong processStep.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong processStep.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated())) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong processStep.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong processStep.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong processStep.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong processStep.updated");
    }

    private void assertProcessStepsEquality(ProcessStepDTO expected, ProcessStepEntity actual) {
        if (expected.getUuid() != null) {
            assertEquals(expected.getUuid(), actual.getUuid(), "Wrong processStep.uuid");
        } else {
            assertNotNull(actual.getUuid(), "Wrong processStep.uuid");
        }

        assertEquals(expected.getName(), actual.getName(), "Wrong processStep.name");
        assertEquals(expected.getDescription(), actual.getDescription(), "Wrong processStep.description");

        if (expected.getVersion() == null) {
            assertEquals(1, actual.getVersion(), "Wrong processStep.version");
        } else if (expected.getUpdated() == null || expected.getUpdated().isBefore(actual.getUpdated().atOffset(OffsetDateTime.now().getOffset()))) {
            assertEquals(expected.getVersion() + 1, actual.getVersion(), "Wrong processStep.version");
        } else {
            assertEquals(expected.getVersion(), actual.getVersion(), "Wrong processStep.version");
        }
        assertEquals(UPDATED_BY, actual.getUpdatedBy(), "Wrong processStep.updatedBy");
        assertNotNull(actual.getUpdated(), "Wrong processStep.updated");
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
        materialRepository.deleteAll();
        toolRepository.deleteAll();
        typeRepository.deleteAll();

        QuarkusTransaction.commit();
    }

}
