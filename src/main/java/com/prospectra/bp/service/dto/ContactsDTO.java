package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Contacts} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContactsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    private Long country;

    private Long region;

    private Long subRegions;

    private String codePostal;

    private String address;

    private LocalDate birthDay;

    private Set<TagsDTO> tags = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Set<TagsDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagsDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactsDTO)) {
            return false;
        }

        ContactsDTO contactsDTO = (ContactsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactsDTO{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", country=" + getCountry() +
            ", region=" + getRegion() +
            ", subRegions=" + getSubRegions() +
            ", codePostal='" + getCodePostal() + "'" +
            ", address='" + getAddress() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", tags=" + getTags() +
            "}";
    }
}
