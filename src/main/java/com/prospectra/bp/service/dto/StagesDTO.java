package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Stages} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StagesDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private Integer order;

    private PipelinesDTO pipelines;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public PipelinesDTO getPipelines() {
        return pipelines;
    }

    public void setPipelines(PipelinesDTO pipelines) {
        this.pipelines = pipelines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StagesDTO)) {
            return false;
        }

        StagesDTO stagesDTO = (StagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StagesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", pipelines=" + getPipelines() +
            "}";
    }
}
