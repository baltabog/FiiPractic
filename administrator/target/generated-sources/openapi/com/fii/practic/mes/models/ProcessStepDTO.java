package com.fii.practic.mes.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fii.practic.mes.models.IdentityDTO;
import com.fii.practic.mes.models.ProcessStepMaterialDTO;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ProcessStepDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class ProcessStepDTO  implements Serializable {
  private String name;
  private @Valid Set<ProcessStepMaterialDTO> inputMaterials = new LinkedHashSet<>();
  private @Valid Set<@Valid IdentityDTO> equipments = new LinkedHashSet<>();
  private @Valid Set<ProcessStepMaterialDTO> successOutputMaterials = new LinkedHashSet<>();
  private String uuid;
  private String description;
  private Integer version;
  private OffsetDateTime updated;
  private String updatedBy;
  private @Valid Set<ProcessStepMaterialDTO> failOutputMaterials = new LinkedHashSet<>();

  /**
   **/
  public ProcessStepDTO name(String name) {
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
  public ProcessStepDTO inputMaterials(Set<ProcessStepMaterialDTO> inputMaterials) {
    this.inputMaterials = inputMaterials;
    return this;
  }

  
  @JsonProperty("inputMaterials")
  @NotNull @Valid  @Size(min=1)public Set<@Valid ProcessStepMaterialDTO> getInputMaterials() {
    return inputMaterials;
  }

  @JsonProperty("inputMaterials")
  @JsonDeserialize(as = LinkedHashSet.class)
  public void setInputMaterials(Set<ProcessStepMaterialDTO> inputMaterials) {
    this.inputMaterials = inputMaterials;
  }

  public ProcessStepDTO addInputMaterialsItem(ProcessStepMaterialDTO inputMaterialsItem) {
    if (this.inputMaterials == null) {
      this.inputMaterials = new LinkedHashSet<>();
    }

    this.inputMaterials.add(inputMaterialsItem);
    return this;
  }

  public ProcessStepDTO removeInputMaterialsItem(ProcessStepMaterialDTO inputMaterialsItem) {
    if (inputMaterialsItem != null && this.inputMaterials != null) {
      this.inputMaterials.remove(inputMaterialsItem);
    }

    return this;
  }
  /**
   **/
  public ProcessStepDTO equipments(Set<@Valid IdentityDTO> equipments) {
    this.equipments = equipments;
    return this;
  }

  
  @JsonProperty("equipments")
  @NotNull @Valid  @Size(min=1)public Set<@Valid IdentityDTO> getEquipments() {
    return equipments;
  }

  @JsonProperty("equipments")
  @JsonDeserialize(as = LinkedHashSet.class)
  public void setEquipments(Set<@Valid IdentityDTO> equipments) {
    this.equipments = equipments;
  }

  public ProcessStepDTO addEquipmentsItem(IdentityDTO equipmentsItem) {
    if (this.equipments == null) {
      this.equipments = new LinkedHashSet<>();
    }

    this.equipments.add(equipmentsItem);
    return this;
  }

  public ProcessStepDTO removeEquipmentsItem(IdentityDTO equipmentsItem) {
    if (equipmentsItem != null && this.equipments != null) {
      this.equipments.remove(equipmentsItem);
    }

    return this;
  }
  /**
   **/
  public ProcessStepDTO successOutputMaterials(Set<ProcessStepMaterialDTO> successOutputMaterials) {
    this.successOutputMaterials = successOutputMaterials;
    return this;
  }

  
  @JsonProperty("successOutputMaterials")
  @NotNull @Valid  @Size(min=1)public Set<@Valid ProcessStepMaterialDTO> getSuccessOutputMaterials() {
    return successOutputMaterials;
  }

  @JsonProperty("successOutputMaterials")
  @JsonDeserialize(as = LinkedHashSet.class)
  public void setSuccessOutputMaterials(Set<ProcessStepMaterialDTO> successOutputMaterials) {
    this.successOutputMaterials = successOutputMaterials;
  }

  public ProcessStepDTO addSuccessOutputMaterialsItem(ProcessStepMaterialDTO successOutputMaterialsItem) {
    if (this.successOutputMaterials == null) {
      this.successOutputMaterials = new LinkedHashSet<>();
    }

    this.successOutputMaterials.add(successOutputMaterialsItem);
    return this;
  }

  public ProcessStepDTO removeSuccessOutputMaterialsItem(ProcessStepMaterialDTO successOutputMaterialsItem) {
    if (successOutputMaterialsItem != null && this.successOutputMaterials != null) {
      this.successOutputMaterials.remove(successOutputMaterialsItem);
    }

    return this;
  }
  /**
   **/
  public ProcessStepDTO uuid(String uuid) {
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
  public ProcessStepDTO description(String description) {
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
  public ProcessStepDTO version(Integer version) {
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
  public ProcessStepDTO updated(OffsetDateTime updated) {
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
  public ProcessStepDTO updatedBy(String updatedBy) {
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
  public ProcessStepDTO failOutputMaterials(Set<ProcessStepMaterialDTO> failOutputMaterials) {
    this.failOutputMaterials = failOutputMaterials;
    return this;
  }

  
  @JsonProperty("failOutputMaterials")
  @Valid  @Size(min=0)public Set<@Valid ProcessStepMaterialDTO> getFailOutputMaterials() {
    return failOutputMaterials;
  }

  @JsonProperty("failOutputMaterials")
  @JsonDeserialize(as = LinkedHashSet.class)
  public void setFailOutputMaterials(Set<ProcessStepMaterialDTO> failOutputMaterials) {
    this.failOutputMaterials = failOutputMaterials;
  }

  public ProcessStepDTO addFailOutputMaterialsItem(ProcessStepMaterialDTO failOutputMaterialsItem) {
    if (this.failOutputMaterials == null) {
      this.failOutputMaterials = new LinkedHashSet<>();
    }

    this.failOutputMaterials.add(failOutputMaterialsItem);
    return this;
  }

  public ProcessStepDTO removeFailOutputMaterialsItem(ProcessStepMaterialDTO failOutputMaterialsItem) {
    if (failOutputMaterialsItem != null && this.failOutputMaterials != null) {
      this.failOutputMaterials.remove(failOutputMaterialsItem);
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
    ProcessStepDTO processStepDTO = (ProcessStepDTO) o;
    return Objects.equals(this.name, processStepDTO.name) &&
        Objects.equals(this.inputMaterials, processStepDTO.inputMaterials) &&
        Objects.equals(this.equipments, processStepDTO.equipments) &&
        Objects.equals(this.successOutputMaterials, processStepDTO.successOutputMaterials) &&
        Objects.equals(this.uuid, processStepDTO.uuid) &&
        Objects.equals(this.description, processStepDTO.description) &&
        Objects.equals(this.version, processStepDTO.version) &&
        Objects.equals(this.updated, processStepDTO.updated) &&
        Objects.equals(this.updatedBy, processStepDTO.updatedBy) &&
        Objects.equals(this.failOutputMaterials, processStepDTO.failOutputMaterials);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, inputMaterials, equipments, successOutputMaterials, uuid, description, version, updated, updatedBy, failOutputMaterials);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessStepDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    inputMaterials: ").append(toIndentedString(inputMaterials)).append("\n");
    sb.append("    equipments: ").append(toIndentedString(equipments)).append("\n");
    sb.append("    successOutputMaterials: ").append(toIndentedString(successOutputMaterials)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
    sb.append("    failOutputMaterials: ").append(toIndentedString(failOutputMaterials)).append("\n");
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

