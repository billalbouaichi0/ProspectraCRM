package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Emails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Emails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailsRepository extends JpaRepository<Emails, Long> {}
