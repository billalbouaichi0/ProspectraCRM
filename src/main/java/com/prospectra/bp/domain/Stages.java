package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stages.
 */
@Entity
@Table(name = "stages")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "jhi_order")
    private Integer order;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stages")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "notes", "compagnies", "contacts", "products", "stages" }, allowSetters = true)
    private Set<Opportunities> opportunities = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stages" }, allowSetters = true)
    private Pipelines pipelines;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stages id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Stages name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Stages description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Stages order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Set<Opportunities> getOpportunities() {
        return this.opportunities;
    }

    public void setOpportunities(Set<Opportunities> opportunities) {
        if (this.opportunities != null) {
            this.opportunities.forEach(i -> i.setStages(null));
        }
        if (opportunities != null) {
            opportunities.forEach(i -> i.setStages(this));
        }
        this.opportunities = opportunities;
    }

    public Stages opportunities(Set<Opportunities> opportunities) {
        this.setOpportunities(opportunities);
        return this;
    }

    public Stages addOpportunities(Opportunities opportunities) {
        this.opportunities.add(opportunities);
        opportunities.setStages(this);
        return this;
    }

    public Stages removeOpportunities(Opportunities opportunities) {
        this.opportunities.remove(opportunities);
        opportunities.setStages(null);
        return this;
    }

    public Pipelines getPipelines() {
        return this.pipelines;
    }

    public void setPipelines(Pipelines pipelines) {
        this.pipelines = pipelines;
    }

    public Stages pipelines(Pipelines pipelines) {
        this.setPipelines(pipelines);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stages)) {
            return false;
        }
        return getId() != null && getId().equals(((Stages) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stages{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
