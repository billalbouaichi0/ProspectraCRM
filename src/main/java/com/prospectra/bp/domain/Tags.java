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
 * A Tags.
 */
@Entity
@Table(name = "tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emails", "phones", "tags", "companies" }, allowSetters = true)
    private Set<Contacts> contacts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emails", "tags", "contacts" }, allowSetters = true)
    private Set<Companies> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tags id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Tags description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Contacts> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contacts> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.removeTags(this));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.addTags(this));
        }
        this.contacts = contacts;
    }

    public Tags contacts(Set<Contacts> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Tags addContacts(Contacts contacts) {
        this.contacts.add(contacts);
        contacts.getTags().add(this);
        return this;
    }

    public Tags removeContacts(Contacts contacts) {
        this.contacts.remove(contacts);
        contacts.getTags().remove(this);
        return this;
    }

    public Set<Companies> getCompanies() {
        return this.companies;
    }

    public void setCompanies(Set<Companies> companies) {
        if (this.companies != null) {
            this.companies.forEach(i -> i.removeTags(this));
        }
        if (companies != null) {
            companies.forEach(i -> i.addTags(this));
        }
        this.companies = companies;
    }

    public Tags companies(Set<Companies> companies) {
        this.setCompanies(companies);
        return this;
    }

    public Tags addCompanies(Companies companies) {
        this.companies.add(companies);
        companies.getTags().add(this);
        return this;
    }

    public Tags removeCompanies(Companies companies) {
        this.companies.remove(companies);
        companies.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tags)) {
            return false;
        }
        return getId() != null && getId().equals(((Tags) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tags{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
