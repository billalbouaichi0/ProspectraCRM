package com.prospectra.bp.service.dto;

import com.prospectra.bp.domain.enumeration.ProdServ;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.prospectra.bp.domain.Products} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductsDTO implements Serializable {

    private Long id;

    private ProdServ type;

    @NotNull
    private String designation;

    @NotNull
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdServ getType() {
        return type;
    }

    public void setType(ProdServ type) {
        this.type = type;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductsDTO)) {
            return false;
        }

        ProductsDTO productsDTO = (ProductsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
