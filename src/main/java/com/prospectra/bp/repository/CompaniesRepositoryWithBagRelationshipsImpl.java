package com.prospectra.bp.repository;

import com.prospectra.bp.domain.Companies;
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
public class CompaniesRepositoryWithBagRelationshipsImpl implements CompaniesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Companies> fetchBagRelationships(Optional<Companies> companies) {
        return companies.map(this::fetchTags).map(this::fetchContacts);
    }

    @Override
    public Page<Companies> fetchBagRelationships(Page<Companies> companies) {
        return new PageImpl<>(fetchBagRelationships(companies.getContent()), companies.getPageable(), companies.getTotalElements());
    }

    @Override
    public List<Companies> fetchBagRelationships(List<Companies> companies) {
        return Optional.of(companies).map(this::fetchTags).map(this::fetchContacts).orElse(Collections.emptyList());
    }

    Companies fetchTags(Companies result) {
        return entityManager
            .createQuery(
                "select companies from Companies companies left join fetch companies.tags where companies.id = :id",
                Companies.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Companies> fetchTags(List<Companies> companies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, companies.size()).forEach(index -> order.put(companies.get(index).getId(), index));
        List<Companies> result = entityManager
            .createQuery(
                "select companies from Companies companies left join fetch companies.tags where companies in :companies",
                Companies.class
            )
            .setParameter("companies", companies)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Companies fetchContacts(Companies result) {
        return entityManager
            .createQuery(
                "select companies from Companies companies left join fetch companies.contacts where companies.id = :id",
                Companies.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Companies> fetchContacts(List<Companies> companies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, companies.size()).forEach(index -> order.put(companies.get(index).getId(), index));
        List<Companies> result = entityManager
            .createQuery(
                "select companies from Companies companies left join fetch companies.contacts where companies in :companies",
                Companies.class
            )
            .setParameter("companies", companies)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
