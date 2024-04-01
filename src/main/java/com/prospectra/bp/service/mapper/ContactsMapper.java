package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.domain.Tags;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.dto.TagsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contacts} and its DTO {@link ContactsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactsMapper extends EntityMapper<ContactsDTO, Contacts> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagsIdSet")
    ContactsDTO toDto(Contacts s);

    @Mapping(target = "removeTags", ignore = true)
    Contacts toEntity(ContactsDTO contactsDTO);

    @Named("tagsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagsDTO toDtoTagsId(Tags tags);

    @Named("tagsIdSet")
    default Set<TagsDTO> toDtoTagsIdSet(Set<Tags> tags) {
        return tags.stream().map(this::toDtoTagsId).collect(Collectors.toSet());
    }
}
