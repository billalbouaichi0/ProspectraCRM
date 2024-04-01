package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Companies;
import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.domain.Tags;
import com.prospectra.bp.service.dto.CompaniesDTO;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.dto.TagsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Companies} and its DTO {@link CompaniesDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompaniesMapper extends EntityMapper<CompaniesDTO, Companies> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagsIdSet")
    @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsIdSet")
    CompaniesDTO toDto(Companies s);

    @Mapping(target = "removeTags", ignore = true)
    @Mapping(target = "removeContacts", ignore = true)
    Companies toEntity(CompaniesDTO companiesDTO);

    @Named("tagsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagsDTO toDtoTagsId(Tags tags);

    @Named("tagsIdSet")
    default Set<TagsDTO> toDtoTagsIdSet(Set<Tags> tags) {
        return tags.stream().map(this::toDtoTagsId).collect(Collectors.toSet());
    }

    @Named("contactsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactsDTO toDtoContactsId(Contacts contacts);

    @Named("contactsIdSet")
    default Set<ContactsDTO> toDtoContactsIdSet(Set<Contacts> contacts) {
        return contacts.stream().map(this::toDtoContactsId).collect(Collectors.toSet());
    }
}
