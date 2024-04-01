package com.prospectra.bp.service.dto;

import com.prospectra.bp.domain.enumeration.MarketingModel;
import com.prospectra.bp.domain.enumeration.ProsOp;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Opportunities} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OpportunitiesDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private ProsOp prospectLevel;

    @NotNull
    private String description;

    @NotNull
    private Double amount;

    private Instant creationDate;

    private MarketingModel model;

    private CompaniesDTO compagnies;

    private ContactsDTO contacts;

    private Set<ProductsDTO> products = new HashSet<>();

    private StagesDTO stages;

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

    public ProsOp getProspectLevel() {
        return prospectLevel;
    }

    public void setProspectLevel(ProsOp prospectLevel) {
        this.prospectLevel = prospectLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public MarketingModel getModel() {
        return model;
    }

    public void setModel(MarketingModel model) {
        this.model = model;
    }

    public CompaniesDTO getCompagnies() {
        return compagnies;
    }

    public void setCompagnies(CompaniesDTO compagnies) {
        this.compagnies = compagnies;
    }

    public ContactsDTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsDTO contacts) {
        this.contacts = contacts;
    }

    public Set<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductsDTO> products) {
        this.products = products;
    }

    public StagesDTO getStages() {
        return stages;
    }

    public void setStages(StagesDTO stages) {
        this.stages = stages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OpportunitiesDTO)) {
            return false;
        }

        OpportunitiesDTO opportunitiesDTO = (OpportunitiesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, opportunitiesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OpportunitiesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prospectLevel='" + getProspectLevel() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            ", creationDate='" + getCreationDate() + "'" +
            ", model='" + getModel() + "'" +
            ", compagnies=" + getCompagnies() +
            ", contacts=" + getContacts() +
            ", products=" + getProducts() +
            ", stages=" + getStages() +
            "}";
    }
}
