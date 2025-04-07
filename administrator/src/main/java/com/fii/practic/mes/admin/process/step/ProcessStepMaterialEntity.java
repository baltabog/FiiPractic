package com.fii.practic.mes.admin.process.step;

import com.fii.practic.mes.admin.material.MaterialEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = ProcessStepMaterialEntity.ENTITY_NAME)
@Table(
   name = ProcessStepMaterialEntity.TABLE_NAME,
   uniqueConstraints = {
           @UniqueConstraint(columnNames = {"UUID"}, name = ProcessStepMaterialEntity.TABLE_NAME + "__UUID")
   }
)
@Audited
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MAT_TYPE")
@Getter @Setter
public class ProcessStepMaterialEntity {
    public static final String ENTITY_NAME = "ProcessStepMaterial";
    public static final String TABLE_NAME = "T_PROCESS_STEP_MATERIAL";

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "entity_id_generator")
    @SequenceGenerator(name = "entity_id_generator", sequenceName = "T_SEQUENCE__ID", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PROCESS_STEP_ID", referencedColumnName = "ID", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_T_PROCESS_STEP_MATERIAL__PROCESS_STEP_ID"))
    @NotNull
    private ProcessStepEntity processStep;

    @ManyToOne
    @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_T_PROCESS_STEP_MATERIAL__MATERIAL_ID"))
    @NotNull
    private MaterialEntity material;

    @Column(name = "QUANTITY")
    private Integer quantity;

}
