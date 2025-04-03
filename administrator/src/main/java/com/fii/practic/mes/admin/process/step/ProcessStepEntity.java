package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity(name = ProcessStepEntity.ENTITY_NAME)
@Table(
    name = ProcessStepEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = ProcessStepEntity.TABLE_NAME + "__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = ProcessStepEntity.TABLE_NAME + "__NAME")
        }
)
@Audited
@Getter @Setter
public class ProcessStepEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "ProcessStep";
    public static final String TABLE_NAME = "T_PROCESS_STEP";
    public static final String PROCESS_STEP_MATERIAL_JOIN_COLUMN = "PROCESS_STEP_ID";

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = PROCESS_STEP_MATERIAL_JOIN_COLUMN)
    private List<ProcessStepMaterialSuccessEntity> successOutputMaterials = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = PROCESS_STEP_MATERIAL_JOIN_COLUMN)
    private List<ProcessStepMaterialEntity> failOutputMaterials = new ArrayList<>();
}
