package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Companies;
import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.domain.Emails;
import com.prospectra.bp.service.dto.CompaniesDTO;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.dto.EmailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emails} and its DTO {@link EmailsDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailsMapper extends EntityMapper<EmailsDTO, Emails> {
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsId")
    @Mapping(target = "companies", source = "companies", qualifiedByName = "companiesId")
    EmailsDTO toDto(Emails s);

    @Named("contactsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactsDTO toDtoContactsId(Contacts contacts);

    @Named("companiesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompaniesDTO toDtoCompaniesId(Companies companies);
}
