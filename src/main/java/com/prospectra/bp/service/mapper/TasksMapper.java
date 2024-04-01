package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Opportunities;
import com.prospectra.bp.domain.Tasks;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
import com.prospectra.bp.service.dto.TasksDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tasks} and its DTO {@link TasksDTO}.
 */
@Mapper(componentModel = "spring")
public interface TasksMapper extends EntityMapper<TasksDTO, Tasks> {
    @Mapping(target = "opportunities", source = "opportunities", qualifiedByName = "opportunitiesId")
    TasksDTO toDto(Tasks s);

    @Named("opportunitiesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OpportunitiesDTO toDtoOpportunitiesId(Opportunities opportunities);
}
