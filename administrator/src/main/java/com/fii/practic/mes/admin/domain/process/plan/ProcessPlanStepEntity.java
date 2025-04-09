package com.fii.practic.mes.admin.domain.process.plan;

import com.fii.practic.mes.admin.domain.process.step.ProcessStepEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;

@Entity(name = ProcessPlanStepEntity.ENTITY_NAME)
@Table(name = ProcessPlanStepEntity.TABLE_NAME)
@Audited
@Getter
@Setter
public class ProcessPlanStepEntity {
        public static final String ENTITY_NAME = "ProcessPlanStep";
        public static final String TABLE_NAME = "T_PROCESS_PLAN_STEP";

        @Id
        @Column(name = "ID", nullable = false, unique = true)
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "entity_id_generator")
        @SequenceGenerator(name = "entity_id_generator", sequenceName = "T_SEQUENCE__ID", allocationSize = 1)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "PROCESS_PLAN_ID", nullable = false, updatable = false,
                foreignKey = @ForeignKey(name = "FK_T_PROCESS_PLAN_STEP__PROCESS_PLAN_ID"))
        @NotNull
        @NaturalId
        private ProcessPlanEntity processPlan;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "PROCESS_STEP_ID", nullable = false, updatable = false,
                foreignKey = @ForeignKey(name = "FK_T_PROCESS_PLAN_STEP__PROCESS_STEP_ID"))
        @NotNull
        @NaturalId
        private ProcessStepEntity processStep;

        @Column(name = "ORDER_IN_PLAN", nullable = false)
        @NotNull
        private Integer orderInProcess;
}
