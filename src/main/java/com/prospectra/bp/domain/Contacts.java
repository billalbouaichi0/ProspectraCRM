package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contacts.
 */
@Entity
@Table(name = "contacts")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contacts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @NotNull
    @Size(min = 2, max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

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

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contacts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "companies" }, allowSetters = true)
    private Set<Emails> emails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contacts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts" }, allowSetters = true)
    private Set<Phones> phones = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_contacts__tags",
        joinColumns = @JoinColumn(name = "contacts_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contacts", "companies" }, allowSetters = true)
    private Set<Tags> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "contacts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "emails", "tags", "contacts" }, allowSetters = true)
    private Set<Companies> companies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contacts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Contacts lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Contacts firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getCountry() {
        return this.country;
    }

    public Contacts country(Long country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getRegion() {
        return this.region;
    }

    public Contacts region(Long region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getSubRegions() {
        return this.subRegions;
    }

    public Contacts subRegions(Long subRegions) {
        this.setSubRegions(subRegions);
        return this;
    }

    public void setSubRegions(Long subRegions) {
        this.subRegions = subRegions;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Contacts codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getAddress() {
        return this.address;
    }

    public Contacts address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDay() {
        return this.birthDay;
    }

    public Contacts birthDay(LocalDate birthDay) {
        this.setBirthDay(birthDay);
        return this;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Set<Emails> getEmails() {
        return this.emails;
    }

    public void setEmails(Set<Emails> emails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setContacts(null));
        }
        if (emails != null) {
            emails.forEach(i -> i.setContacts(this));
        }
        this.emails = emails;
    }

    public Contacts emails(Set<Emails> emails) {
        this.setEmails(emails);
        return this;
    }

    public Contacts addEmails(Emails emails) {
        this.emails.add(emails);
        emails.setContacts(this);
        return this;
    }

    public Contacts removeEmails(Emails emails) {
        this.emails.remove(emails);
        emails.setContacts(null);
        return this;
    }

    public Set<Phones> getPhones() {
        return this.phones;
    }

    public void setPhones(Set<Phones> phones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setContacts(null));
        }
        if (phones != null) {
            phones.forEach(i -> i.setContacts(this));
        }
        this.phones = phones;
    }

    public Contacts phones(Set<Phones> phones) {
        this.setPhones(phones);
        return this;
    }

    public Contacts addPhones(Phones phones) {
        this.phones.add(phones);
        phones.setContacts(this);
        return this;
    }

    public Contacts removePhones(Phones phones) {
        this.phones.remove(phones);
        phones.setContacts(null);
        return this;
    }

    public Set<Tags> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tags> tags) {
        this.tags = tags;
    }

    public Contacts tags(Set<Tags> tags) {
        this.setTags(tags);
        return this;
    }

    public Contacts addTags(Tags tags) {
        this.tags.add(tags);
        return this;
    }

    public Contacts removeTags(Tags tags) {
        this.tags.remove(tags);
        return this;
    }

    public Set<Companies> getCompanies() {
        return this.companies;
    }

    public void setCompanies(Set<Companies> companies) {
        if (this.companies != null) {
            this.companies.forEach(i -> i.removeContacts(this));
        }
        if (companies != null) {
            companies.forEach(i -> i.addContacts(this));
        }
        this.companies = companies;
    }

    public Contacts companies(Set<Companies> companies) {
        this.setCompanies(companies);
        return this;
    }

    public Contacts addCompanies(Companies companies) {
        this.companies.add(companies);
        companies.getContacts().add(this);
        return this;
    }

    public Contacts removeCompanies(Companies companies) {
        this.companies.remove(companies);
        companies.getContacts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contacts)) {
            return false;
        }
        return getId() != null && getId().equals(((Contacts) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contacts{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", country=" + getCountry() +
            ", region=" + getRegion() +
            ", subRegions=" + getSubRegions() +
            ", codePostal='" + getCodePostal() + "'" +
            ", address='" + getAddress() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            "}";
    }
}
