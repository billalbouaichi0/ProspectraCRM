package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Pipelines;
import com.prospectra.bp.domain.Stages;
import com.prospectra.bp.service.dto.PipelinesDTO;
import com.prospectra.bp.service.dto.StagesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stages} and its DTO {@link StagesDTO}.
 */
@Mapper(componentModel = "spring")
public interface StagesMapper extends EntityMapper<StagesDTO, Stages> {
    @Mapping(target = "pipelines", source = "pipelines", qualifiedByName = "pipelinesId")
    StagesDTO toDto(Stages s);

    @Named("pipelinesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelinesDTO toDtoPipelinesId(Pipelines pipelines);
}
