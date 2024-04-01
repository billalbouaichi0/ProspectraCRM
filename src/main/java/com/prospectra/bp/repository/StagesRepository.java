package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Stages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StagesRepository extends JpaRepository<Stages, Long> {}
