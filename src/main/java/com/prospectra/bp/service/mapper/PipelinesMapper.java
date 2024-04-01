package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Pipelines;
import com.prospectra.bp.service.dto.PipelinesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pipelines} and its DTO {@link PipelinesDTO}.
 */
@Mapper(componentModel = "spring")
public interface PipelinesMapper extends EntityMapper<PipelinesDTO, Pipelines> {}
