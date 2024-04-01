package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pipelines.
 */
@Entity
@Table(name = "pipelines")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pipelines implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "owning_user")
    private String owningUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pipelines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opportunities", "pipelines" }, allowSetters = true)
    private Set<Stages> stages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pipelines id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Pipelines name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Pipelines description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Pipelines creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getOwningUser() {
        return this.owningUser;
    }

    public Pipelines owningUser(String owningUser) {
        this.setOwningUser(owningUser);
        return this;
    }

    public void setOwningUser(String owningUser) {
        this.owningUser = owningUser;
    }

    public Set<Stages> getStages() {
        return this.stages;
    }

    public void setStages(Set<Stages> stages) {
        if (this.stages != null) {
            this.stages.forEach(i -> i.setPipelines(null));
        }
        if (stages != null) {
            stages.forEach(i -> i.setPipelines(this));
        }
        this.stages = stages;
    }

    public Pipelines stages(Set<Stages> stages) {
        this.setStages(stages);
        return this;
    }

    public Pipelines addStages(Stages stages) {
        this.stages.add(stages);
        stages.setPipelines(this);
        return this;
    }

    public Pipelines removeStages(Stages stages) {
        this.stages.remove(stages);
        stages.setPipelines(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pipelines)) {
            return false;
        }
        return getId() != null && getId().equals(((Pipelines) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pipelines{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", owningUser='" + getOwningUser() + "'" +
            "}";
    }
}
