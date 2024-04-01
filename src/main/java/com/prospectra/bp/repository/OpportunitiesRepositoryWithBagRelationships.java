package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Opportunities;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OpportunitiesRepositoryWithBagRelationships {
    Optional<Opportunities> fetchBagRelationships(Optional<Opportunities> opportunities);

    List<Opportunities> fetchBagRelationships(List<Opportunities> opportunities);

    Page<Opportunities> fetchBagRelationships(Page<Opportunities> opportunities);
}
