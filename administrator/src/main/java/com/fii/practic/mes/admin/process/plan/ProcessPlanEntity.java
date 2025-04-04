package com.fii.practic.mes.admin.process.plan;

import com.fii.practic.mes.admin.general.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity(name = ProcessPlanEntity.ENTITY_NAME)
@Table(
        name = ProcessPlanEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = ProcessPlanEntity.TABLE_NAME + "__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = ProcessPlanEntity.TABLE_NAME + "__NAME")
        }
)
@Audited
@Getter
@Setter
public class ProcessPlanEntity extends AbstractEntity {
        public static final String ENTITY_NAME = "ProcessPlan";
        public static final String TABLE_NAME = "T_PROCESS_PLAN";

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "processPlan")
        private List<ProcessPlanStepEntity> orderedProcessSteps = new ArrayList<>();

}
