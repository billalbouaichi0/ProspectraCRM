package com.prospectra.bp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.prospectra.bp.domain.enumeration.ProdServ;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProdServ type;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tasks", "notes", "compagnies", "contacts", "products", "stages" }, allowSetters = true)
    private Set<Opportunities> opportunities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Products id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdServ getType() {
        return this.type;
    }

    public Products type(ProdServ type) {
        this.setType(type);
        return this;
    }

    public void setType(ProdServ type) {
        this.type = type;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Products designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrice() {
        return this.price;
    }

    public Products price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Opportunities> getOpportunities() {
        return this.opportunities;
    }

    public void setOpportunities(Set<Opportunities> opportunities) {
        if (this.opportunities != null) {
            this.opportunities.forEach(i -> i.removeProducts(this));
        }
        if (opportunities != null) {
            opportunities.forEach(i -> i.addProducts(this));
        }
        this.opportunities = opportunities;
    }

    public Products opportunities(Set<Opportunities> opportunities) {
        this.setOpportunities(opportunities);
        return this;
    }

    public Products addOpportunities(Opportunities opportunities) {
        this.opportunities.add(opportunities);
        opportunities.getProducts().add(this);
        return this;
    }

    public Products removeOpportunities(Opportunities opportunities) {
        this.opportunities.remove(opportunities);
        opportunities.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return getId() != null && getId().equals(((Products) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
