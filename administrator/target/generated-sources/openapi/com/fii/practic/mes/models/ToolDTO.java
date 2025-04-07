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



@JsonTypeName("ToolDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class ToolDTO  implements Serializable {
  private String name;
  private IdentityDTO equipmentTypeIdentity;
  private Boolean active;
  private String uuid;
  private String description;
  private Integer version;
  private OffsetDateTime updated;
  private String updatedBy;
  private String alias;

  /**
   **/
  public ToolDTO name(String name) {
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
  public ToolDTO equipmentTypeIdentity(IdentityDTO equipmentTypeIdentity) {
    this.equipmentTypeIdentity = equipmentTypeIdentity;
    return this;
  }

  
  @JsonProperty("equipmentTypeIdentity")
  @NotNull @Valid public IdentityDTO getEquipmentTypeIdentity() {
    return equipmentTypeIdentity;
  }

  @JsonProperty("equipmentTypeIdentity")
  public void setEquipmentTypeIdentity(IdentityDTO equipmentTypeIdentity) {
    this.equipmentTypeIdentity = equipmentTypeIdentity;
  }

  /**
   **/
  public ToolDTO active(Boolean active) {
    this.active = active;
    return this;
  }

  
  @JsonProperty("active")
  @NotNull public Boolean getActive() {
    return active;
  }

  @JsonProperty("active")
  public void setActive(Boolean active) {
    this.active = active;
  }

  /**
   **/
  public ToolDTO uuid(String uuid) {
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
  public ToolDTO description(String description) {
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
  public ToolDTO version(Integer version) {
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
  public ToolDTO updated(OffsetDateTime updated) {
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
  public ToolDTO updatedBy(String updatedBy) {
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
   **/
  public ToolDTO alias(String alias) {
    this.alias = alias;
    return this;
  }

  
  @JsonProperty("alias")
   @Size(min=1)public String getAlias() {
    return alias;
  }

  @JsonProperty("alias")
  public void setAlias(String alias) {
    this.alias = alias;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToolDTO toolDTO = (ToolDTO) o;
    return Objects.equals(this.name, toolDTO.name) &&
        Objects.equals(this.equipmentTypeIdentity, toolDTO.equipmentTypeIdentity) &&
        Objects.equals(this.active, toolDTO.active) &&
        Objects.equals(this.uuid, toolDTO.uuid) &&
        Objects.equals(this.description, toolDTO.description) &&
        Objects.equals(this.version, toolDTO.version) &&
        Objects.equals(this.updated, toolDTO.updated) &&
        Objects.equals(this.updatedBy, toolDTO.updatedBy) &&
        Objects.equals(this.alias, toolDTO.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, equipmentTypeIdentity, active, uuid, description, version, updated, updatedBy, alias);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ToolDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    equipmentTypeIdentity: ").append(toIndentedString(equipmentTypeIdentity)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
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

