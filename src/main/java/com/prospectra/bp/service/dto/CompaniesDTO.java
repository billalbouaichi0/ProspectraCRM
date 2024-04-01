package com.prospectra.bp.service.dto;

import com.prospectra.bp.domain.enumeration.FormeJuridique;
import com.prospectra.bp.domain.enumeration.SecteurActivitie;
import com.prospectra.bp.domain.enumeration.TypeCompany;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Companies} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompaniesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    private Long country;

    private Long region;

    private Long subRegions;

    private String codePostal;

    private String address;

    private TypeCompany type;

    private SecteurActivitie secteurActivite;

    private FormeJuridique juridiqueForm;

    private Long employeeNumber;

    private Double caa;

    private Set<TagsDTO> tags = new HashSet<>();

    private Set<ContactsDTO> contacts = new HashSet<>();

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

    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getSubRegions() {
        return subRegions;
    }

    public void setSubRegions(Long subRegions) {
        this.subRegions = subRegions;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TypeCompany getType() {
        return type;
    }

    public void setType(TypeCompany type) {
        this.type = type;
    }

    public SecteurActivitie getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(SecteurActivitie secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public FormeJuridique getJuridiqueForm() {
        return juridiqueForm;
    }

    public void setJuridiqueForm(FormeJuridique juridiqueForm) {
        this.juridiqueForm = juridiqueForm;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public Double getCaa() {
        return caa;
    }

    public void setCaa(Double caa) {
        this.caa = caa;
    }

    public Set<TagsDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagsDTO> tags) {
        this.tags = tags;
    }

    public Set<ContactsDTO> getContacts() {
        return contacts;
    }

    public void setContacts(Set<ContactsDTO> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompaniesDTO)) {
            return false;
        }

        CompaniesDTO companiesDTO = (CompaniesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, companiesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompaniesDTO{" +
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
            ", tags=" + getTags() +
            ", contacts=" + getContacts() +
            "}";
    }
}
