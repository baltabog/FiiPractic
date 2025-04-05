package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.material.MaterialEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = ProcessStepInputMaterialEntity.ENTITY_NAME)
@Table(name = ProcessStepInputMaterialEntity.TABLE_NAME)
@Audited
@Getter @Setter
public class ProcessStepInputMaterialEntity {
    public static final String ENTITY_NAME = "ProcessStepInputMaterial";
    public static final String TABLE_NAME = "T_PROCESS_STEP_INPUT_MATERIAL";

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "entity_id_generator")
    @SequenceGenerator(name = "entity_id_generator", sequenceName = "T_SEQUENCE__ID", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MATERIAL", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_T_PROCESS_STEP_INPUT_MATERIAL__MATERIAL_ID"))
    @NotNull
    private MaterialEntity material;

    @ManyToOne
    @JoinColumn(name = "PROCESS_STEP_ID", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_T_PROCESS_STEP_INPUT_MATERIAL__PROCESS_STEP_ID"))
    @NotNull
    private ProcessStepEntity processStep;

    @Column(name = "QUANTITY", nullable = false)
    @NotNull
    private Integer quantity;

}
