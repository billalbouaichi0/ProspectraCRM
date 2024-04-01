package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Tasks} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TasksDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String type;

    private String description;

    private Instant creationDate;

    private Instant dueDate;

    private String status;

    private OpportunitiesDTO opportunities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OpportunitiesDTO getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(OpportunitiesDTO opportunities) {
        this.opportunities = opportunities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TasksDTO)) {
            return false;
        }

        TasksDTO tasksDTO = (TasksDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tasksDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TasksDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", opportunities=" + getOpportunities() +
            "}";
    }
}
