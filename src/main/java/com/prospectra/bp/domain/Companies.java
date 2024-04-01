package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prospectra.bp.domain.enumeration.FormeJuridique;
import com.prospectra.bp.domain.enumeration.SecteurActivitie;
import com.prospectra.bp.domain.enumeration.TypeCompany;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Companies.
 */
@Entity
@Table(name = "companies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Companies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Column(name = "country", nullable = false)
    private Long country;

    @Column(name = "region")
    private Long region;

    @Column(name = "sub_regions")
    private Long subRegions;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeCompany type;

    @Enumerated(EnumType.STRING)
    @Column(name = "secteur_activite")
    private SecteurActivitie secteurActivite;

    @Enumerated(EnumType.STRING)
    @Column(name = "juridique_form")
    private FormeJuridique juridiqueForm;

    @Column(name = "employee_number")
    private Long employeeNumber;

    @Column(name = "caa")
    private Double caa;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "companies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "companies" }, allowSetters = true)
    private Set<Emails> emails = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_companies__tags",
        joinColumns = @JoinColumn(name = "companies_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "companies" }, allowSetters = true)
    private Set<Tags> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_companies__contacts",
        joinColumns = @JoinColumn(name = "companies_id"),
        inverseJoinColumns = @JoinColumn(name = "contacts_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emails", "phones", "tags", "companies" }, allowSetters = true)
    private Set<Contacts> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Companies id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Companies name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountry() {
        return this.country;
    }

    public Companies country(Long country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getRegion() {
        return this.region;
    }

    public Companies region(Long region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getSubRegions() {
        return this.subRegions;
    }

    public Companies subRegions(Long subRegions) {
        this.setSubRegions(subRegions);
        return this;
    }

    public void setSubRegions(Long subRegions) {
        this.subRegions = subRegions;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Companies codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getAddress() {
        return this.address;
    }

    public Companies address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TypeCompany getType() {
        return this.type;
    }

    public Companies type(TypeCompany type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeCompany type) {
        this.type = type;
    }

    public SecteurActivitie getSecteurActivite() {
        return this.secteurActivite;
    }

    public Companies secteurActivite(SecteurActivitie secteurActivite) {
        this.setSecteurActivite(secteurActivite);
        return this;
    }

    public void setSecteurActivite(SecteurActivitie secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public FormeJuridique getJuridiqueForm() {
        return this.juridiqueForm;
    }

    public Companies juridiqueForm(FormeJuridique juridiqueForm) {
        this.setJuridiqueForm(juridiqueForm);
        return this;
    }

    public void setJuridiqueForm(FormeJuridique juridiqueForm) {
        this.juridiqueForm = juridiqueForm;
    }

    public Long getEmployeeNumber() {
        return this.employeeNumber;
    }

    public Companies employeeNumber(Long employeeNumber) {
        this.setEmployeeNumber(employeeNumber);
        return this;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Double getCaa() {
        return this.caa;
    }

    public Companies caa(Double caa) {
        this.setCaa(caa);
        return this;
    }

    public void setCaa(Double caa) {
        this.caa = caa;
    }

    public Set<Emails> getEmails() {
        return this.emails;
    }

    public void setEmails(Set<Emails> emails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setCompanies(null));
        }
        if (emails != null) {
            emails.forEach(i -> i.setCompanies(this));
        }
        this.emails = emails;
    }

    public Companies emails(Set<Emails> emails) {
        this.setEmails(emails);
        return this;
    }

    public Companies addEmails(Emails emails) {
        this.emails.add(emails);
        emails.setCompanies(this);
        return this;
    }

    public Companies removeEmails(Emails emails) {
        this.emails.remove(emails);
        emails.setCompanies(null);
        return this;
    }

    public Set<Tags> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public Companies tags(Set<Tags> tags) {
        this.setTags(tags);
        return this;
    }

    public Companies addTags(Tags tags) {
        this.tags.add(tags);
        return this;
    }

    public Companies removeTags(Tags tags) {
        this.tags.remove(tags);
        return this;
    }

    public Set<Contacts> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contacts> contacts) {
        this.contacts = contacts;
    }

    public Companies contacts(Set<Contacts> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Companies addContacts(Contacts contacts) {
        this.contacts.add(contacts);
        return this;
    }

    public Companies removeContacts(Contacts contacts) {
        this.contacts.remove(contacts);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Companies)) {
            return false;
        }
        return getId() != null && getId().equals(((Companies) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Companies{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", country=" + getCountry() +
            ", region=" + getRegion() +
            ", subRegions=" + getSubRegions() +
            ", codePostal='" + getCodePostal() + "'" +
            ", address='" + getAddress() + "'" +
            ", type='" + getType() + "'" +
            ", secteurActivite='" + getSecteurActivite() + "'" +
            ", juridiqueForm='" + getJuridiqueForm() + "'" +
            ", employeeNumber=" + getEmployeeNumber() +
            ", caa=" + getCaa() +
            "}";
    }
}
