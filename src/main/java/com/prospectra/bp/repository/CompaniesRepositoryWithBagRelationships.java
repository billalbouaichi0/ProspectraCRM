package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Companies;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CompaniesRepositoryWithBagRelationships {
    Optional<Companies> fetchBagRelationships(Optional<Companies> companies);

    List<Companies> fetchBagRelationships(List<Companies> companies);

    Page<Companies> fetchBagRelationships(Page<Companies> companies);
}
