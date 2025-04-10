package com.fii.practic.mes.admin.domain.process.step;

import com.fii.practic.mes.admin.domain.equipment.tool.ToolEntity;
import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "processStep")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProcessStepInputMaterialEntity> processStepInputMaterial = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "processStep")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProcessStepMaterialSuccessEntity> successOutputMaterials = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "processStep")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProcessStepMaterialFailEntity> failOutputMaterials = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "T_PROCESS_STEP_EQUIPMENT",
            joinColumns = { @JoinColumn(name = "PROCESS_STEP_ID") },
            inverseJoinColumns = { @JoinColumn(name = "EQUIPMENT_ID") }
    )
    private Set<ToolEntity> equipments = new HashSet<>();
}
