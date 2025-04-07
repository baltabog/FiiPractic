package com.fii.practic.mes.models;

import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("OrderedProcessStepDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class OrderedProcessStepDTO  implements Serializable {
  private String uuid;
  private String name;
  private Integer orderInProcess;

  /**
   **/
  public OrderedProcessStepDTO uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  
  @JsonProperty("uuid")
  @NotNull  @Size(min=1,max=36)public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   **/
  public OrderedProcessStepDTO name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull  @Size(min=1)public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   * minimum: 0
   * maximum: 128
   **/
  public OrderedProcessStepDTO orderInProcess(Integer orderInProcess) {
    this.orderInProcess = orderInProcess;
    return this;
  }

  
  @JsonProperty("orderInProcess")
  @NotNull  @Min(0) @Max(128)public Integer getOrderInProcess() {
    return orderInProcess;
  }

  @JsonProperty("orderInProcess")
  public void setOrderInProcess(Integer orderInProcess) {
    this.orderInProcess = orderInProcess;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderedProcessStepDTO orderedProcessStepDTO = (OrderedProcessStepDTO) o;
    return Objects.equals(this.uuid, orderedProcessStepDTO.uuid) &&
        Objects.equals(this.name, orderedProcessStepDTO.name) &&
        Objects.equals(this.orderInProcess, orderedProcessStepDTO.orderInProcess);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, name, orderInProcess);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderedProcessStepDTO {\n");
    
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    orderInProcess: ").append(toIndentedString(orderInProcess)).append("\n");
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

