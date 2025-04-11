package com.fii.practic.mes.admin.general.search;

import com.fii.practic.mes.admin.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.admin.general.error.ServerErrorEnum;
import com.fii.practic.mes.models.FilterParamCriteriaType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public final class CriteriaTransformer {

    private CriteriaTransformer() {
        // implementation not requested
    }

    /**
     *
     * @param criteria - transformed criteria
     * @param params - holder for bind values
     * @param dtoPropertyDescriptor - property description
     *
     * @return hql criteria string
     */
    public static String transform(FilterParamCriteriaType criteria,
                                   Map<String, Object> params,
                                   PropertyDescription dtoPropertyDescriptor) {

        validateCriteria(criteria, dtoPropertyDescriptor);
        String entityQueriedProperty = dtoPropertyDescriptor.getHqlName();
        String operator = getOperatorString(criteria);
        String subquery = List.of(PropertyType.SINGLE_VALUE_SUBQUERY, PropertyType.MULTI_VALUE_SUBQUERY).contains(dtoPropertyDescriptor.getHqlType())
                ? (String) dtoPropertyDescriptor.getValueTransformer().apply("subqueryValue")
                : StringUtils.EMPTY;

        if (CollectionUtils.isNotEmpty(criteria.getValues())) {
            params.put(criteria.getPropertyName(), CollectionUtils.size(criteria.getValues()) == 1
                    ? criteria.getValues().get(0)
                    : criteria.getValues());
        }

        return "%s %s %s".formatted(
                entityQueriedProperty,
                operator,
                subquery);
    }

    private static void validateCriteria(FilterParamCriteriaType criteria, PropertyDescription dtoPropertyDescriptor) {
        if (!dtoPropertyDescriptor.isFilterable()) {
            throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER, criteria.getPropertyName());
        }

        if (isNoValueOperator(criteria.getOperator())) {
            if (CollectionUtils.isNotEmpty(criteria.getValues())) {
                throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER_LOGICAL_OPERATOR_WITH_VALUES, criteria.getPropertyName());
            }
            return;
        }

        if (isMultiValueOperator(criteria.getOperator())) {
            if (CollectionUtils.isEmpty(criteria.getValues())) {
                throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER_MULTI_OPERATOR_NO_VALUES, criteria.getPropertyName());
            }
            return;
        }

        if (isSingleValueOperator(criteria.getOperator()) && CollectionUtils.size(criteria.getValues()) != 1) {
            throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER_UNARY_OPERATOR_MULTI_VALUES, criteria.getPropertyName());
        }
    }

    private static String getOperatorString(FilterParamCriteriaType criteria) {        
        return switch (criteria.getOperator()) {
            case EQ -> "=";
            case NE -> "!=";
            case LT -> "<";
            case LE -> "<=";
            case GT -> ">";
            case GE -> ">=";
            case LIKE -> "like";
            case NOT_LIKE -> "not like";
            case IS_NULL -> "is null";
            case IS_NOT_NULL -> "is not null";
            case IN -> "in";
            case NOT_IN -> "not in";
        };
    }

    private static String getBindVariableObject(FilterParamCriteriaType criteria,
                                                Map<String, Object> params,
                                                PropertyDescription dtoPropertyDescriptor) {

        if (isNoValueOperator(criteria.getOperator())) {
            return StringUtils.EMPTY;
        }
        String criteriaBindVariableName = getCriteriaParamName(criteria);
        if (isSingleValueOperator(criteria.getOperator())) {
            Object value = dtoPropertyDescriptor.getValue(criteria.getValues().get(0));
            if (FilterParamCriteriaType.OperatorEnum.LIKE.equals(criteria.getOperator()) && value instanceof String stringValue) {
                value =  stringValue.replace("*", "%");
            }
            params.put(criteriaBindVariableName, value);
        }
        if (isMultiValueOperator(criteria.getOperator())) {
            params.put(criteriaBindVariableName, dtoPropertyDescriptor.getValues(criteria.getValues()));
        }

        return criteriaBindVariableName;
    }

    private static String getCriteriaParamName(FilterParamCriteriaType criteria) {
        return criteria.getPropertyName() + System.nanoTime();
    }

    private static boolean isNoValueOperator(FilterParamCriteriaType.OperatorEnum operator) {
        return List.of( FilterParamCriteriaType.OperatorEnum.IS_NULL,
                        FilterParamCriteriaType.OperatorEnum.IS_NOT_NULL).contains(operator);
    }

    private static boolean isSingleValueOperator(FilterParamCriteriaType.OperatorEnum operator) {
        return List.of( FilterParamCriteriaType.OperatorEnum.EQ,
                        FilterParamCriteriaType.OperatorEnum.NE,
                        FilterParamCriteriaType.OperatorEnum.LT,
                        FilterParamCriteriaType.OperatorEnum.LE,
                        FilterParamCriteriaType.OperatorEnum.GT,
                        FilterParamCriteriaType.OperatorEnum.GE,
                        FilterParamCriteriaType.OperatorEnum.LIKE,
                        FilterParamCriteriaType.OperatorEnum.NOT_LIKE
                ).contains(operator);
    }

    private static boolean isMultiValueOperator(FilterParamCriteriaType.OperatorEnum operator) {
        return List.of( FilterParamCriteriaType.OperatorEnum.IN,
                FilterParamCriteriaType.OperatorEnum.NOT_IN).contains(operator);
    }
}
