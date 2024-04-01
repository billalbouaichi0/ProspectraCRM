package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Companies;
import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.domain.Opportunities;
import com.prospectra.bp.domain.Products;
import com.prospectra.bp.domain.Stages;
import com.prospectra.bp.service.dto.CompaniesDTO;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
import com.prospectra.bp.service.dto.ProductsDTO;
import com.prospectra.bp.service.dto.StagesDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Opportunities} and its DTO {@link OpportunitiesDTO}.
 */
@Mapper(componentModel = "spring")
public interface OpportunitiesMapper extends EntityMapper<OpportunitiesDTO, Opportunities> {
    @Mapping(target = "compagnies", source = "compagnies", qualifiedByName = "companiesId")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productsIdSet")
    @Mapping(target = "stages", source = "stages", qualifiedByName = "stagesId")
    OpportunitiesDTO toDto(Opportunities s);

    @Mapping(target = "removeProducts", ignore = true)
    Opportunities toEntity(OpportunitiesDTO opportunitiesDTO);

    @Named("companiesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompaniesDTO toDtoCompaniesId(Companies companies);

    @Named("contactsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactsDTO toDtoContactsId(Contacts contacts);

    @Named("productsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductsDTO toDtoProductsId(Products products);

    @Named("productsIdSet")
    default Set<ProductsDTO> toDtoProductsIdSet(Set<Products> products) {
        return products.stream().map(this::toDtoProductsId).collect(Collectors.toSet());
    }

    @Named("stagesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StagesDTO toDtoStagesId(Stages stages);
}
