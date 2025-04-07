package com.fii.practic.mes.admin.process.step;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Entity(name = ProcessStepInputMaterialEntity.ENTITY_NAME)
@DiscriminatorValue(value = ProcessStepInputMaterialEntity.DISCRIMINATOR)
@Audited
@AuditTable(value = "T_PROCESS_STEP_MATERIAL_INPUT_AUD")
@Getter @Setter
public class ProcessStepInputMaterialEntity extends ProcessStepMaterialEntity {
    public static final String ENTITY_NAME = "ProcessStepInputMaterial";
    public static final String DISCRIMINATOR = "IN";
}
