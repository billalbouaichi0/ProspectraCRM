package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prospectra.bp.domain.enumeration.MarketingModel;
import com.prospectra.bp.domain.enumeration.ProsOp;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Opportunities.
 */
@Entity
@Table(name = "opportunities")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Opportunities implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "prospect_level", nullable = false)
    private ProsOp prospectLevel;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "model")
    private MarketingModel model;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "opportunities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opportunities" }, allowSetters = true)
    private Set<Tasks> tasks = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "opportunities")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opportunities" }, allowSetters = true)
    private Set<Notes> notes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emails", "tags", "contacts" }, allowSetters = true)
    private Companies compagnies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emails", "phones", "tags", "companies" }, allowSetters = true)
    private Contacts contacts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_opportunities__products",
        joinColumns = @JoinColumn(name = "opportunities_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "opportunities" }, allowSetters = true)
    private Set<Products> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "opportunities", "pipelines" }, allowSetters = true)
    private Stages stages;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Opportunities id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Opportunities name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProsOp getProspectLevel() {
        return this.prospectLevel;
    }

    public Opportunities prospectLevel(ProsOp prospectLevel) {
        this.setProspectLevel(prospectLevel);
        return this;
    }

    public void setProspectLevel(ProsOp prospectLevel) {
        this.prospectLevel = prospectLevel;
    }

    public String getDescription() {
        return this.description;
    }

    public Opportunities description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Opportunities amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Opportunities creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public MarketingModel getModel() {
        return this.model;
    }

    public Opportunities model(MarketingModel model) {
        this.setModel(model);
        return this;
    }

    public void setModel(MarketingModel model) {
        this.model = model;
    }

    public Set<Tasks> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Tasks> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setOpportunities(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setOpportunities(this));
        }
        this.tasks = tasks;
    }

    public Opportunities tasks(Set<Tasks> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Opportunities addTasks(Tasks tasks) {
        this.tasks.add(tasks);
        tasks.setOpportunities(this);
        return this;
    }

    public Opportunities removeTasks(Tasks tasks) {
        this.tasks.remove(tasks);
        tasks.setOpportunities(null);
        return this;
    }

    public Set<Notes> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Notes> notes) {
        if (this.notes != null) {
            this.notes.forEach(i -> i.setOpportunities(null));
        }
        if (notes != null) {
            notes.forEach(i -> i.setOpportunities(this));
        }
        this.notes = notes;
    }

    public Opportunities notes(Set<Notes> notes) {
        this.setNotes(notes);
        return this;
    }

    public Opportunities addNotes(Notes notes) {
        this.notes.add(notes);
        notes.setOpportunities(this);
        return this;
    }

    public Opportunities removeNotes(Notes notes) {
        this.notes.remove(notes);
        notes.setOpportunities(null);
        return this;
    }

    public Companies getCompagnies() {
        return this.compagnies;
    }

    public void setCompagnies(Companies companies) {
        this.compagnies = companies;
    }

    public Opportunities compagnies(Companies companies) {
        this.setCompagnies(companies);
        return this;
    }

    public Contacts getContacts() {
        return this.contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public Opportunities contacts(Contacts contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Set<Products> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Products> products) {
        this.products = products;
    }

    public Opportunities products(Set<Products> products) {
        this.setProducts(products);
        return this;
    }

    public Opportunities addProducts(Products products) {
        this.products.add(products);
        return this;
    }

    public Opportunities removeProducts(Products products) {
        this.products.remove(products);
        return this;
    }

    public Stages getStages() {
        return this.stages;
    }

    public void setStages(Stages stages) {
        this.stages = stages;
    }

    public Opportunities stages(Stages stages) {
        this.setStages(stages);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opportunities)) {
            return false;
        }
        return getId() != null && getId().equals(((Opportunities) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Opportunities{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prospectLevel='" + getProspectLevel() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", creationDate='" + getCreationDate() + "'" +
            ", model='" + getModel() + "'" +
            "}";
    }
}
