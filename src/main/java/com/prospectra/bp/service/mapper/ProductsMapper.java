package com.prospectra.bp.service.mapper;

import com.prospectra.bp.domain.Products;
import com.prospectra.bp.service.dto.ProductsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Products} and its DTO {@link ProductsDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductsMapper extends EntityMapper<ProductsDTO, Products> {}
