package com.prospectra.bp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Emails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    @NotNull
    private String mail;

    private ContactsDTO contacts;

    private CompaniesDTO companies;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ContactsDTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsDTO contacts) {
        this.contacts = contacts;
    }

    public CompaniesDTO getCompanies() {
        return companies;
    }

    public void setCompanies(CompaniesDTO companies) {
        this.companies = companies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailsDTO)) {
            return false;
        }

        EmailsDTO emailsDTO = (EmailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailsDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", mail='" + getMail() + "'" +
            ", contacts=" + getContacts() +
            ", companies=" + getCompanies() +
            "}";
    }
}
