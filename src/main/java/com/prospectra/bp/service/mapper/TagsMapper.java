package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Tags;
import com.prospectra.bp.service.dto.TagsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tags} and its DTO {@link TagsDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagsMapper extends EntityMapper<TagsDTO, Tags> {}
