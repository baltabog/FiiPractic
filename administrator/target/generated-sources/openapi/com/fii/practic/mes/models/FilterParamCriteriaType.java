package com.fii.practic.mes.models;

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



@JsonTypeName("FilterParamCriteriaType")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class FilterParamCriteriaType  implements Serializable {
  private String propertyName;
  public enum OperatorEnum {

    EQ(String.valueOf("eq")), NE(String.valueOf("ne")), LIKE(String.valueOf("like")), NOT_LIKE(String.valueOf("not_like")), LT(String.valueOf("lt")), LE(String.valueOf("le")), GT(String.valueOf("gt")), GE(String.valueOf("ge")), IS_NULL(String.valueOf("is_null")), IS_NOT_NULL(String.valueOf("is_not_null")), IN(String.valueOf("in")), NOT_IN(String.valueOf("not_in"));


    private String value;

    OperatorEnum (String v) {
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
    public static OperatorEnum fromString(String s) {
        for (OperatorEnum b : OperatorEnum.values()) {
            // using Objects.toString() to be safe if value type non-object type
            // because types like 'int' etc. will be auto-boxed
            if (java.util.Objects.toString(b.value).equals(s)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static OperatorEnum fromValue(String value) {
        for (OperatorEnum b : OperatorEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

  private OperatorEnum operator;
  private @Valid List<String> values = new ArrayList<>();

  /**
   **/
  public FilterParamCriteriaType propertyName(String propertyName) {
    this.propertyName = propertyName;
    return this;
  }

  
  @JsonProperty("propertyName")
  @NotNull public String getPropertyName() {
    return propertyName;
  }

  @JsonProperty("propertyName")
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  /**
   **/
  public FilterParamCriteriaType operator(OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

  
  @JsonProperty("operator")
  @NotNull public OperatorEnum getOperator() {
    return operator;
  }

  @JsonProperty("operator")
  public void setOperator(OperatorEnum operator) {
    this.operator = operator;
  }

  /**
   **/
  public FilterParamCriteriaType values(List<String> values) {
    this.values = values;
    return this;
  }

  
  @JsonProperty("values")
  public List<String> getValues() {
    return values;
  }

  @JsonProperty("values")
  public void setValues(List<String> values) {
    this.values = values;
  }

  public FilterParamCriteriaType addValuesItem(String valuesItem) {
    if (this.values == null) {
      this.values = new ArrayList<>();
    }

    this.values.add(valuesItem);
    return this;
  }

  public FilterParamCriteriaType removeValuesItem(String valuesItem) {
    if (valuesItem != null && this.values != null) {
      this.values.remove(valuesItem);
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
    FilterParamCriteriaType filterParamCriteriaType = (FilterParamCriteriaType) o;
    return Objects.equals(this.propertyName, filterParamCriteriaType.propertyName) &&
        Objects.equals(this.operator, filterParamCriteriaType.operator) &&
        Objects.equals(this.values, filterParamCriteriaType.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(propertyName, operator, values);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FilterParamCriteriaType {\n");
    
    sb.append("    propertyName: ").append(toIndentedString(propertyName)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    values: ").append(toIndentedString(values)).append("\n");
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

