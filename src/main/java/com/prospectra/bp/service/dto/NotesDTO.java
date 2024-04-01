package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Notes} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotesDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Instant creationDate;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
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
        if (!(o instanceof NotesDTO)) {
            return false;
        }

        NotesDTO notesDTO = (NotesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotesDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", opportunities=" + getOpportunities() +
            "}";
    }
}
