package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.domain.Phones;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.dto.PhonesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Phones} and its DTO {@link PhonesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhonesMapper extends EntityMapper<PhonesDTO, Phones> {
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsId")
    PhonesDTO toDto(Phones s);

    @Named("contactsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactsDTO toDtoContactsId(Contacts contacts);
}
