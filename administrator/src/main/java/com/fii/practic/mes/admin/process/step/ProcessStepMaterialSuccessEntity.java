package com.fii.practic.mes.admin.process.step;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;


@Entity(name = ProcessStepMaterialSuccessEntity.ENTITY_NAME)
@DiscriminatorValue(value = ProcessStepMaterialSuccessEntity.DISCRIMINATOR)
@Getter @Setter
@Audited
@AuditTable(value = "T_PROCESS_STEP_MATERIAL_SUCCESS_AUD")
public class ProcessStepMaterialSuccessEntity extends ProcessStepMaterialEntity {
    public static final String ENTITY_NAME = "ProcessStepMaterialSuccess";
    public static final String DISCRIMINATOR = "S";
}
