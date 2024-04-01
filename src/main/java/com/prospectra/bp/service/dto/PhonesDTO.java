package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Phones} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhonesDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    @Pattern(regexp = "^[0-9]+$")
    private String phone;

    private ContactsDTO contacts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactsDTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsDTO contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhonesDTO)) {
            return false;
        }

        PhonesDTO phonesDTO = (PhonesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phonesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhonesDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", phone='" + getPhone() + "'" +
            ", contacts=" + getContacts() +
            "}";
    }
}
