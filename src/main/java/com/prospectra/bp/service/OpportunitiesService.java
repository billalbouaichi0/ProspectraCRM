package com.prospectra.bp.service;

import com.prospectra.bp.domain.Opportunities;
import com.prospectra.bp.repository.OpportunitiesRepository;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
import com.prospectra.bp.service.mapper.OpportunitiesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Opportunities}.
 */
@Service
@Transactional
public class OpportunitiesService {

    private final Logger log = LoggerFactory.getLogger(OpportunitiesService.class);

    private final OpportunitiesRepository opportunitiesRepository;

    private final OpportunitiesMapper opportunitiesMapper;

    public OpportunitiesService(OpportunitiesRepository opportunitiesRepository, OpportunitiesMapper opportunitiesMapper) {
        this.opportunitiesRepository = opportunitiesRepository;
        this.opportunitiesMapper = opportunitiesMapper;
    }

    /**
     * Save a opportunities.
     *
     * @param opportunitiesDTO the entity to save.
     * @return the persisted entity.
     */
    public OpportunitiesDTO save(OpportunitiesDTO opportunitiesDTO) {
        log.debug("Request to save Opportunities : {}", opportunitiesDTO);
        Opportunities opportunities = opportunitiesMapper.toEntity(opportunitiesDTO);
        opportunities = opportunitiesRepository.save(opportunities);
        return opportunitiesMapper.toDto(opportunities);
    }

    /**
     * Update a opportunities.
     *
     * @param opportunitiesDTO the entity to save.
     * @return the persisted entity.
     */
    public OpportunitiesDTO update(OpportunitiesDTO opportunitiesDTO) {
        log.debug("Request to update Opportunities : {}", opportunitiesDTO);
        Opportunities opportunities = opportunitiesMapper.toEntity(opportunitiesDTO);
        opportunities = opportunitiesRepository.save(opportunities);
        return opportunitiesMapper.toDto(opportunities);
    }

    /**
     * Partially update a opportunities.
     *
     * @param opportunitiesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OpportunitiesDTO> partialUpdate(OpportunitiesDTO opportunitiesDTO) {
        log.debug("Request to partially update Opportunities : {}", opportunitiesDTO);

        return opportunitiesRepository
            .findById(opportunitiesDTO.getId())
            .map(existingOpportunities -> {
                opportunitiesMapper.partialUpdate(existingOpportunities, opportunitiesDTO);

                return existingOpportunities;
            })
            .map(opportunitiesRepository::save)
            .map(opportunitiesMapper::toDto);
    }

    /**
     * Get all the opportunities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OpportunitiesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Opportunities");
        return opportunitiesRepository.findAll(pageable).map(opportunitiesMapper::toDto);
    }

    /**
     * Get all the opportunities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<OpportunitiesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return opportunitiesRepository.findAllWithEagerRelationships(pageable).map(opportunitiesMapper::toDto);
    }

    /**
     * Get one opportunities by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OpportunitiesDTO> findOne(Long id) {
        log.debug("Request to get Opportunities : {}", id);
        return opportunitiesRepository.findOneWithEagerRelationships(id).map(opportunitiesMapper::toDto);
    }

    /**
     * Delete the opportunities by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Opportunities : {}", id);
        opportunitiesRepository.deleteById(id);
    }
}
