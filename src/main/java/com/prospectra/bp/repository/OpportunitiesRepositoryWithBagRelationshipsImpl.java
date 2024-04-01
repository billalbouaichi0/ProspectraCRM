package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Opportunities;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OpportunitiesRepositoryWithBagRelationshipsImpl implements OpportunitiesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Opportunities> fetchBagRelationships(Optional<Opportunities> opportunities) {
        return opportunities.map(this::fetchProducts);
    }

    @Override
    public Page<Opportunities> fetchBagRelationships(Page<Opportunities> opportunities) {
        return new PageImpl<>(
            fetchBagRelationships(opportunities.getContent()),
            opportunities.getPageable(),
            opportunities.getTotalElements()
        );
    }

    @Override
    public List<Opportunities> fetchBagRelationships(List<Opportunities> opportunities) {
        return Optional.of(opportunities).map(this::fetchProducts).orElse(Collections.emptyList());
    }

    Opportunities fetchProducts(Opportunities result) {
        return entityManager
            .createQuery(
                "select opportunities from Opportunities opportunities left join fetch opportunities.products where opportunities.id = :id",
                Opportunities.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Opportunities> fetchProducts(List<Opportunities> opportunities) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, opportunities.size()).forEach(index -> order.put(opportunities.get(index).getId(), index));
        List<Opportunities> result = entityManager
            .createQuery(
                "select opportunities from Opportunities opportunities left join fetch opportunities.products where opportunities in :opportunities",
                Opportunities.class
            )
            .setParameter("opportunities", opportunities)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
