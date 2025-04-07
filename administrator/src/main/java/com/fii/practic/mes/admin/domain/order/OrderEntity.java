package com.fii.practic.mes.admin.domain.order;

import com.fii.practic.mes.admin.general.AbstractEntity;
import com.fii.practic.mes.admin.domain.process.plan.ProcessPlanEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = OrderEntity.ENTITY_NAME)
@Table(
        name = OrderEntity.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"UUID"}, name = OrderEntity.TABLE_NAME + "__UUID"),
                @UniqueConstraint(columnNames = {"NAME"}, name = OrderEntity.TABLE_NAME + "__NAME")
        }
)
@Audited
@Getter @Setter
public class OrderEntity extends AbstractEntity {
    public static final String ENTITY_NAME = "Order";
    public static final String TABLE_NAME = "T_ORDER";

    @ManyToOne
    @JoinColumn(name = "PROCESS_PLAN_ID", nullable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_T_ORDER__PROCEES_PLAN_ID"))
    @NotNull
    private ProcessPlanEntity processPlan;

    @Column(name = "QUANTITY", nullable = false)
    @NotNull
    private Integer quantity;

    @Column(name = "COMPLETE_QTY", nullable = false)
    @NotNull
    private Integer completeQty;


}
