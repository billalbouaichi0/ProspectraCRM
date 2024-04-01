package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Notes;
import com.prospectra.bp.domain.Opportunities;
import com.prospectra.bp.service.dto.NotesDTO;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notes} and its DTO {@link NotesDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotesMapper extends EntityMapper<NotesDTO, Notes> {
    @Mapping(target = "opportunities", source = "opportunities", qualifiedByName = "opportunitiesId")
    NotesDTO toDto(Notes s);

    @Named("opportunitiesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OpportunitiesDTO toDtoOpportunitiesId(Opportunities opportunities);
}
