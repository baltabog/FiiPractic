package com.fii.practic.mes.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fii.practic.mes.models.OrderedProcessStepDTO;
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



@JsonTypeName("ProcessPlanDTO")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-04-07T11:57:29.761686900+03:00[Europe/Bucharest]", comments = "Generator version: 7.9.0")
public class ProcessPlanDTO  implements Serializable {
  private String name;
  private @Valid Set<OrderedProcessStepDTO> orderedProcessSteps = new LinkedHashSet<>();
  private String uuid;
  private String description;
  private Integer version;
  private OffsetDateTime updated;
  private String updatedBy;

  /**
   **/
  public ProcessPlanDTO name(String name) {
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
  public ProcessPlanDTO orderedProcessSteps(Set<OrderedProcessStepDTO> orderedProcessSteps) {
    this.orderedProcessSteps = orderedProcessSteps;
    return this;
  }

  
  @JsonProperty("orderedProcessSteps")
  @NotNull @Valid  @Size(min=1)public Set<@Valid OrderedProcessStepDTO> getOrderedProcessSteps() {
    return orderedProcessSteps;
  }

  @JsonProperty("orderedProcessSteps")
  @JsonDeserialize(as = LinkedHashSet.class)
  public void setOrderedProcessSteps(Set<OrderedProcessStepDTO> orderedProcessSteps) {
    this.orderedProcessSteps = orderedProcessSteps;
  }

  public ProcessPlanDTO addOrderedProcessStepsItem(OrderedProcessStepDTO orderedProcessStepsItem) {
    if (this.orderedProcessSteps == null) {
      this.orderedProcessSteps = new LinkedHashSet<>();
    }

    this.orderedProcessSteps.add(orderedProcessStepsItem);
    return this;
  }

  public ProcessPlanDTO removeOrderedProcessStepsItem(OrderedProcessStepDTO orderedProcessStepsItem) {
    if (orderedProcessStepsItem != null && this.orderedProcessSteps != null) {
      this.orderedProcessSteps.remove(orderedProcessStepsItem);
    }

    return this;
  }
  /**
   **/
  public ProcessPlanDTO uuid(String uuid) {
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
  public ProcessPlanDTO description(String description) {
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
  public ProcessPlanDTO version(Integer version) {
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
  public ProcessPlanDTO updated(OffsetDateTime updated) {
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
  public ProcessPlanDTO updatedBy(String updatedBy) {
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
    ProcessPlanDTO processPlanDTO = (ProcessPlanDTO) o;
    return Objects.equals(this.name, processPlanDTO.name) &&
        Objects.equals(this.orderedProcessSteps, processPlanDTO.orderedProcessSteps) &&
        Objects.equals(this.uuid, processPlanDTO.uuid) &&
        Objects.equals(this.description, processPlanDTO.description) &&
        Objects.equals(this.version, processPlanDTO.version) &&
        Objects.equals(this.updated, processPlanDTO.updated) &&
        Objects.equals(this.updatedBy, processPlanDTO.updatedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, orderedProcessSteps, uuid, description, version, updated, updatedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProcessPlanDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    orderedProcessSteps: ").append(toIndentedString(orderedProcessSteps)).append("\n");
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

