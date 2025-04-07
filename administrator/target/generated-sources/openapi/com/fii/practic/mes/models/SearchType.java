package com.fii.practic.mes.models;

import com.fii.practic.mes.models.FilterParamType;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SearchType")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class SearchType  implements Serializable {
  private String sortBy;
  private Integer offset = 0;
  private Integer limit = 1000000;
  private FilterParamType filter;

  /**
   * Comma separated list with property names to sort, e.g. \&quot;sort-by&#x3D;asc:name,desc:updated\&quot;.
   **/
  public SearchType sortBy(String sortBy) {
    this.sortBy = sortBy;
    return this;
  }

  
  @JsonProperty("sortBy")
  public String getSortBy() {
    return sortBy;
  }

  @JsonProperty("sortBy")
  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }

  /**
   * Number of items to skip before returning the results.
   * minimum: 0
   **/
  public SearchType offset(Integer offset) {
    this.offset = offset;
    return this;
  }

  
  @JsonProperty("offset")
   @Min(0)public Integer getOffset() {
    return offset;
  }

  @JsonProperty("offset")
  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  /**
   * Maximum number of items to return. Limit value 0 will disable paging and all items are returned.
   * minimum: 0
   **/
  public SearchType limit(Integer limit) {
    this.limit = limit;
    return this;
  }

  
  @JsonProperty("limit")
   @Min(0)public Integer getLimit() {
    return limit;
  }

  @JsonProperty("limit")
  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  /**
   **/
  public SearchType filter(FilterParamType filter) {
    this.filter = filter;
    return this;
  }

  
  @JsonProperty("filter")
  @Valid public FilterParamType getFilter() {
    return filter;
  }

  @JsonProperty("filter")
  public void setFilter(FilterParamType filter) {
    this.filter = filter;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchType searchType = (SearchType) o;
    return Objects.equals(this.sortBy, searchType.sortBy) &&
        Objects.equals(this.offset, searchType.offset) &&
        Objects.equals(this.limit, searchType.limit) &&
        Objects.equals(this.filter, searchType.filter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sortBy, offset, limit, filter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchType {\n");
    
    sb.append("    sortBy: ").append(toIndentedString(sortBy)).append("\n");
    sb.append("    offset: ").append(toIndentedString(offset)).append("\n");
    sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
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

