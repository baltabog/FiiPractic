package com.fii.practic.mes.wip.general.search;

import com.fii.practic.mes.wip.general.error.ApplicationRuntimeException;
import com.fii.practic.mes.wip.general.error.ServerErrorEnum;
import com.fii.practic.mes.models.FilterParamCriteriaGroupType;
import com.fii.practic.mes.models.FilterParamCriteriaType;
import com.fii.practic.mes.models.FilterParamType;
import com.fii.practic.mes.models.SearchType;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public final class SearchTransformer {

    public static final int DEFAULT_LIMIT = 1000000;

    private SearchTransformer() {
        // implementation not requested
    }

    /**
     *
     * @param filter - which will be transformed to query string
     * @param params - holder for bind values
     * @param outputDtoName - used to get dto map descriptors
     *
     * @return hql query string to be used in repository
     */
    public static String getQueryString(FilterParamType filter,
                                        Map<String, Object> params,
                                        String outputDtoName) {

        List<PropertyDescription> dtoPropertyDescriptors = PropertyDescriptionHolder.getPropertyDescriptions(outputDtoName);
        Boolean conditionAdded = Boolean.FALSE;

        if (filter == null) {
            return StringUtils.EMPTY;
        }

        StringBuilder searchStrBuilder = new StringBuilder( "(");
        String outerOperator = filter.getOuterOperator().value();
        String criteriaStr = getCriteriaString(filter.getCriteria(), params, dtoPropertyDescriptors, outerOperator);
        if (StringUtils.isNotBlank(criteriaStr)) {
            conditionAdded = Boolean.TRUE;
            searchStrBuilder.append(criteriaStr);
        }
        String criteriaGroupStr = getCriteriaGroupString(filter.getCriteriaGroups(), params, dtoPropertyDescriptors, outerOperator);
        if (StringUtils.isNotBlank(criteriaGroupStr)) {
            searchStrBuilder
                    .append(" ")
                    .append(Boolean.TRUE.equals(conditionAdded) ? outerOperator : " ")
                    .append(criteriaGroupStr);
        }

        return searchStrBuilder.append(")").toString();
    }

    /**
     *
     * @param searchSortByString - search string from SearchType
     * @param outputDtoClass - used to get dto map descriptors
     *
     * @return sorting to be used in repository
     */
    public static Sort getSorting(String searchSortByString, String outputDtoClass) {
        List<PropertyDescription> dtoPropertyDescriptors = PropertyDescriptionHolder.getPropertyDescriptions(outputDtoClass);
        if (searchSortByString == null) {
            return null;
        }

        Sort sort = null;
        for (final String sortedBy : searchSortByString.trim().split(",")) {
            sort = getSort(dtoPropertyDescriptors, sortedBy, sort);
        }

        return sort;
    }

    /**
     *
     * @param search - holder of page start no and page size
     *
     * @return page to be used in repository
     */
    public static Page getPage(SearchType search) {
        Page page = Page.ofSize(search.getLimit() != null && search.getLimit() > 0
                ? search.getLimit()
                : DEFAULT_LIMIT);
        if (search.getOffset() != null) {
            page = page.index(search.getOffset() / page.size);
        }

        return page;
    }

    private static String getCriteriaString(List<FilterParamCriteriaType> criterias,
                                     Map<String, Object> param,
                                     List<PropertyDescription>  propertyDescriptions,
                                     String outerOperator) {

        Boolean conditionAdded = Boolean.FALSE;
        StringBuilder criteriaString = new StringBuilder();

        if (CollectionUtils.isNotEmpty(criterias)) {
            for (FilterParamCriteriaType filterParamCriteria : criterias) {
                PropertyDescription criteriaPropertyDescription = null;
                try {
                    criteriaPropertyDescription = getPropertyDescriptor(propertyDescriptions, filterParamCriteria.getPropertyName());
                } catch (ApplicationRuntimeException e) {
                    throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER, filterParamCriteria.getPropertyName());
                }
                criteriaString.append(Boolean.TRUE.equals(conditionAdded) ? " " + outerOperator + " " : StringUtils.EMPTY)
                        .append(CriteriaTransformer.transform(filterParamCriteria, param, criteriaPropertyDescription));
                conditionAdded = Boolean.TRUE;
            }
        }

        return criteriaString.toString();
    }

    private static String getCriteriaGroupString(List<FilterParamCriteriaGroupType> criteriaGroups,
                                          Map<String, Object> param,
                                          List<PropertyDescription>  propertyDescriptions,
                                          String outerOperator) {

        Boolean conditionAdded = Boolean.FALSE;
        StringBuilder criteriaString = new StringBuilder();

        if (CollectionUtils.isNotEmpty(criteriaGroups)) {
            for (FilterParamCriteriaGroupType filterParamCriteria : criteriaGroups) {
                criteriaString.append(Boolean.TRUE.equals(conditionAdded) ? " " + outerOperator + " ( " : " ( ")
                        .append(getCriteriaGroupString(filterParamCriteria, param, propertyDescriptions))
                        .append(" ) ");
                conditionAdded = Boolean.TRUE;
            }
        }

        return criteriaString.toString();
    }

    private static String getCriteriaGroupString(FilterParamCriteriaGroupType criteriaGroup,
                                                 Map<String, Object> param,
                                                 List<PropertyDescription> propertyDescriptions) {

        Boolean conditionAdded = Boolean.FALSE;
        String outerOperator = criteriaGroup.getOuterOperator().value();
        StringBuilder criteriaGroupString = new StringBuilder();

        if (CollectionUtils.isNotEmpty(criteriaGroup.getCriteria())) {
            criteriaGroupString.append(" ( ");
            String criteriaStr = getCriteriaString(criteriaGroup.getCriteria(), param, propertyDescriptions, outerOperator);
            if (StringUtils.isNotBlank(criteriaStr)) {
                conditionAdded = Boolean.TRUE;
                criteriaGroupString.append(criteriaStr);
            }
        }
        if (CollectionUtils.isNotEmpty(criteriaGroup.getCriteriaGroups())) {
            String criteriaGroupStr = getCriteriaGroupString(criteriaGroup.getCriteriaGroups(), param, propertyDescriptions, outerOperator);
            if (StringUtils.isNotBlank(criteriaGroupStr)) {
                criteriaGroupString
                        .append( " " )
                        .append(Boolean.FALSE.equals(conditionAdded) ? "( " : outerOperator);
                conditionAdded = Boolean.TRUE;
                criteriaGroupString.append(criteriaGroupStr);
            }
        }

        return criteriaGroupString.append(Boolean.TRUE.equals(conditionAdded) ? " ) " : StringUtils.EMPTY)
                .toString();
    }

    private static PropertyDescription getPropertyDescriptor(List<PropertyDescription> propertyDescriptions, String criteriaName)
            throws ApplicationRuntimeException{

        return propertyDescriptions.stream()
                .filter(qp -> qp.getDtoName().equals(criteriaName))
                .findFirst()
                .orElseThrow(() ->  new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_FILTER));
    }

    private static Sort getSort(List<PropertyDescription> propertyDescriptions, String sortedBy, Sort sort) {
        final boolean ascSortBy = sortedBy.startsWith("asc:");
        final boolean descSortBy = sortedBy.startsWith("desc:");
        if (ascSortBy || descSortBy) {
            String orderByColumnDtoName = sortedBy.substring(ascSortBy ? 4 : 5);
            PropertyDescription orderByColumn = null;
            try {
                orderByColumn = getPropertyDescriptor(propertyDescriptions, orderByColumnDtoName);
            } catch (ApplicationRuntimeException e) {
                throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_SORT_FIELD, orderByColumnDtoName);
            }
            if (!orderByColumn.isSortable()) {
                throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_SORT_FIELD, orderByColumnDtoName);
            }
            if (sort != null) {
                sort = sort.and(orderByColumn.getHqlName(), ascSortBy ? Sort.Direction.Ascending : Sort.Direction.Descending);
            } else {
                sort = Sort.by(orderByColumn.getHqlName(), ascSortBy ? Sort.Direction.Ascending : Sort.Direction.Descending);
            }
        } else {
            throw new ApplicationRuntimeException(ServerErrorEnum.QUERY_INVALID_SORT_MISSING_ASC_DESC, sortedBy);
        }
        return sort;
    }
}
