package com.fii.practic.mes.models;

import com.fii.practic.mes.models.IdentityDTO;
import java.time.OffsetDateTime;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("OrderDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class OrderDTO  implements Serializable {
  private String name;
  private IdentityDTO process;
  private Integer quantity;
  private String uuid;
  private String description;
  private Integer version;
  private OffsetDateTime updated;
  private String updatedBy;
  private Integer completeQty;

  /**
   **/
  public OrderDTO name(String name) {
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
   **/
  public OrderDTO process(IdentityDTO process) {
    this.process = process;
    return this;
  }

  
  @JsonProperty("process")
  @NotNull @Valid public IdentityDTO getProcess() {
    return process;
  }

  @JsonProperty("process")
  public void setProcess(IdentityDTO process) {
    this.process = process;
  }

  /**
   * minimum: 0
   **/
  public OrderDTO quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  
  @JsonProperty("quantity")
  @NotNull  @Min(0)public Integer getQuantity() {
    return quantity;
  }

  @JsonProperty("quantity")
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  /**
   **/
  public OrderDTO uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  
  @JsonProperty("uuid")
   @Size(min=1,max=36)public String getUuid() {
    return uuid;
  }

  @JsonProperty("uuid")
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   **/
  public OrderDTO description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty("description")
   @Size(min=0,max=255)public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public OrderDTO version(Integer version) {
    this.version = version;
    return this;
  }

  
  @JsonProperty("version")
  public Integer getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(Integer version) {
    this.version = version;
  }

  /**
   * e.g. 2017-07-21T17:32:28Z
   **/
  public OrderDTO updated(OffsetDateTime updated) {
    this.updated = updated;
    return this;
  }

  
  @JsonProperty("updated")
  public OffsetDateTime getUpdated() {
    return updated;
  }

  @JsonProperty("updated")
  public void setUpdated(OffsetDateTime updated) {
    this.updated = updated;
  }

  /**
   **/
  public OrderDTO updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  
  @JsonProperty("updatedBy")
   @Size(max=255)public String getUpdatedBy() {
    return updatedBy;
  }

  @JsonProperty("updatedBy")
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * minimum: 0
   **/
  public OrderDTO completeQty(Integer completeQty) {
    this.completeQty = completeQty;
    return this;
  }

  
  @JsonProperty("completeQty")
   @Min(0)public Integer getCompleteQty() {
    return completeQty;
  }

  @JsonProperty("completeQty")
  public void setCompleteQty(Integer completeQty) {
    this.completeQty = completeQty;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderDTO orderDTO = (OrderDTO) o;
    return Objects.equals(this.name, orderDTO.name) &&
        Objects.equals(this.process, orderDTO.process) &&
        Objects.equals(this.quantity, orderDTO.quantity) &&
        Objects.equals(this.uuid, orderDTO.uuid) &&
        Objects.equals(this.description, orderDTO.description) &&
        Objects.equals(this.version, orderDTO.version) &&
        Objects.equals(this.updated, orderDTO.updated) &&
        Objects.equals(this.updatedBy, orderDTO.updatedBy) &&
        Objects.equals(this.completeQty, orderDTO.completeQty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, process, quantity, uuid, description, version, updated, updatedBy, completeQty);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    process: ").append(toIndentedString(process)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    completeQty: ").append(toIndentedString(completeQty)).append("\n");
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

