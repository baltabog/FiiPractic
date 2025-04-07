package com.fii.practic.mes.models;

import com.fii.practic.mes.models.FilterParamCriteriaGroupType;
import com.fii.practic.mes.models.FilterParamCriteriaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("FilterParamType")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class FilterParamType  implements Serializable {
  public enum OuterOperatorEnum {

    AND(String.valueOf("and")), OR(String.valueOf("or"));


    private String value;

    OuterOperatorEnum (String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the
     * <a href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0 Specification, section 3.2, p. 12</a>
     */
    public static OuterOperatorEnum fromString(String s) {
        for (OuterOperatorEnum b : OuterOperatorEnum.values()) {
            // using Objects.toString() to be safe if value type non-object type
            // because types like 'int' etc. will be auto-boxed
            if (java.util.Objects.toString(b.value).equals(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static OuterOperatorEnum fromValue(String value) {
        for (OuterOperatorEnum b : OuterOperatorEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private OuterOperatorEnum outerOperator = OuterOperatorEnum.AND;
  private @Valid List<@Valid FilterParamCriteriaGroupType> criteriaGroups = new ArrayList<>();
  private @Valid List<@Valid FilterParamCriteriaType> criteria = new ArrayList<>();

  /**
   **/
  public FilterParamType outerOperator(OuterOperatorEnum outerOperator) {
    this.outerOperator = outerOperator;
    return this;
  }

  
  @JsonProperty("outerOperator")
  public OuterOperatorEnum getOuterOperator() {
    return outerOperator;
  }

  @JsonProperty("outerOperator")
  public void setOuterOperator(OuterOperatorEnum outerOperator) {
    this.outerOperator = outerOperator;
  }

  /**
   **/
  public FilterParamType criteriaGroups(List<@Valid FilterParamCriteriaGroupType> criteriaGroups) {
    this.criteriaGroups = criteriaGroups;
    return this;
  }

  
  @JsonProperty("criteriaGroups")
  @Valid public List<@Valid FilterParamCriteriaGroupType> getCriteriaGroups() {
    return criteriaGroups;
  }

  @JsonProperty("criteriaGroups")
  public void setCriteriaGroups(List<@Valid FilterParamCriteriaGroupType> criteriaGroups) {
    this.criteriaGroups = criteriaGroups;
  }

  public FilterParamType addCriteriaGroupsItem(FilterParamCriteriaGroupType criteriaGroupsItem) {
    if (this.criteriaGroups == null) {
      this.criteriaGroups = new ArrayList<>();
    }

    this.criteriaGroups.add(criteriaGroupsItem);
    return this;
  }

  public FilterParamType removeCriteriaGroupsItem(FilterParamCriteriaGroupType criteriaGroupsItem) {
    if (criteriaGroupsItem != null && this.criteriaGroups != null) {
      this.criteriaGroups.remove(criteriaGroupsItem);
    }

    return this;
  }
  /**
   **/
  public FilterParamType criteria(List<@Valid FilterParamCriteriaType> criteria) {
    this.criteria = criteria;
    return this;
  }

  
  @JsonProperty("criteria")
  @Valid public List<@Valid FilterParamCriteriaType> getCriteria() {
    return criteria;
  }

  @JsonProperty("criteria")
  public void setCriteria(List<@Valid FilterParamCriteriaType> criteria) {
    this.criteria = criteria;
  }

  public FilterParamType addCriteriaItem(FilterParamCriteriaType criteriaItem) {
    if (this.criteria == null) {
      this.criteria = new ArrayList<>();
    }

    this.criteria.add(criteriaItem);
    return this;
  }

  public FilterParamType removeCriteriaItem(FilterParamCriteriaType criteriaItem) {
    if (criteriaItem != null && this.criteria != null) {
      this.criteria.remove(criteriaItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FilterParamType filterParamType = (FilterParamType) o;
    return Objects.equals(this.outerOperator, filterParamType.outerOperator) &&
        Objects.equals(this.criteriaGroups, filterParamType.criteriaGroups) &&
        Objects.equals(this.criteria, filterParamType.criteria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(outerOperator, criteriaGroups, criteria);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterParamType {\n");
    
    sb.append("    outerOperator: ").append(toIndentedString(outerOperator)).append("\n");
    sb.append("    criteriaGroups: ").append(toIndentedString(criteriaGroups)).append("\n");
    sb.append("    criteria: ").append(toIndentedString(criteria)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

