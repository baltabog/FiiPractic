package com.fii.practic.mes.models;

import java.time.OffsetDateTime;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("MaterialDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class MaterialDTO  implements Serializable {
  private String name;
  private Integer availableQuantity;
  private String quantityUnit;
  private String uuid;
  private String description;
  private Integer version;
  private OffsetDateTime updated;
  private String updatedBy;

  /**
   **/
  public MaterialDTO name(String name) {
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
   **/
  public MaterialDTO availableQuantity(Integer availableQuantity) {
    this.availableQuantity = availableQuantity;
    return this;
  }

  
  @JsonProperty("availableQuantity")
  @NotNull  @Min(0)public Integer getAvailableQuantity() {
    return availableQuantity;
  }

  @JsonProperty("availableQuantity")
  public void setAvailableQuantity(Integer availableQuantity) {
    this.availableQuantity = availableQuantity;
  }

  /**
   **/
  public MaterialDTO quantityUnit(String quantityUnit) {
    this.quantityUnit = quantityUnit;
    return this;
  }

  
  @JsonProperty("quantityUnit")
  @NotNull  @Size(min=1)public String getQuantityUnit() {
    return quantityUnit;
  }

  @JsonProperty("quantityUnit")
  public void setQuantityUnit(String quantityUnit) {
    this.quantityUnit = quantityUnit;
  }

  /**
   **/
  public MaterialDTO uuid(String uuid) {
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
  public MaterialDTO description(String description) {
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
  public MaterialDTO version(Integer version) {
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
  public MaterialDTO updated(OffsetDateTime updated) {
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
  public MaterialDTO updatedBy(String updatedBy) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MaterialDTO materialDTO = (MaterialDTO) o;
    return Objects.equals(this.name, materialDTO.name) &&
        Objects.equals(this.availableQuantity, materialDTO.availableQuantity) &&
        Objects.equals(this.quantityUnit, materialDTO.quantityUnit) &&
        Objects.equals(this.uuid, materialDTO.uuid) &&
        Objects.equals(this.description, materialDTO.description) &&
        Objects.equals(this.version, materialDTO.version) &&
        Objects.equals(this.updated, materialDTO.updated) &&
        Objects.equals(this.updatedBy, materialDTO.updatedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, availableQuantity, quantityUnit, uuid, description, version, updated, updatedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MaterialDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    availableQuantity: ").append(toIndentedString(availableQuantity)).append("\n");
    sb.append("    quantityUnit: ").append(toIndentedString(quantityUnit)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
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

