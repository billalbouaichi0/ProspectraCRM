package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Pipelines} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PipelinesDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private Instant creationDate;

    private String owningUser;

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

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getOwningUser() {
        return owningUser;
    }

    public void setOwningUser(String owningUser) {
        this.owningUser = owningUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PipelinesDTO)) {
            return false;
        }

        PipelinesDTO pipelinesDTO = (PipelinesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pipelinesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PipelinesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", owningUser='" + getOwningUser() + "'" +
            "}";
    }
}
