package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Pipelines;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pipelines entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PipelinesRepository extends JpaRepository<Pipelines, Long> {}
