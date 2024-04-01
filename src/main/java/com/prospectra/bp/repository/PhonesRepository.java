package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Phones;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Phones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhonesRepository extends JpaRepository<Phones, Long> {}
