package com.fii.practic.mes.admin.process.step;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = ProcessStepMaterialFailEntity.ENTITY_NAME)
@DiscriminatorValue(value = ProcessStepMaterialFailEntity.DISCRIMINATOR)
@Audited
@Getter @Setter
public class ProcessStepMaterialFailEntity extends ProcessStepMaterialEntity {
    public static final String ENTITY_NAME = "ProcessStepMaterialFail";
    public static final String DISCRIMINATOR = "F";
}
