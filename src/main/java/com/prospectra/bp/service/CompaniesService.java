package com.prospectra.bp.service;

import com.prospectra.bp.domain.Companies;
import com.prospectra.bp.repository.CompaniesRepository;
import com.prospectra.bp.service.dto.CompaniesDTO;
import com.prospectra.bp.service.mapper.CompaniesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Companies}.
 */
@Service
@Transactional
public class CompaniesService {

    private final Logger log = LoggerFactory.getLogger(CompaniesService.class);

    private final CompaniesRepository companiesRepository;

    private final CompaniesMapper companiesMapper;

    public CompaniesService(CompaniesRepository companiesRepository, CompaniesMapper companiesMapper) {
        this.companiesRepository = companiesRepository;
        this.companiesMapper = companiesMapper;
    }

    /**
     * Save a companies.
     *
     * @param companiesDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniesDTO save(CompaniesDTO companiesDTO) {
        log.debug("Request to save Companies : {}", companiesDTO);
        Companies companies = companiesMapper.toEntity(companiesDTO);
        companies = companiesRepository.save(companies);
        return companiesMapper.toDto(companies);
    }

    /**
     * Update a companies.
     *
     * @param companiesDTO the entity to save.
     * @return the persisted entity.
     */
    public CompaniesDTO update(CompaniesDTO companiesDTO) {
        log.debug("Request to update Companies : {}", companiesDTO);
        Companies companies = companiesMapper.toEntity(companiesDTO);
        companies = companiesRepository.save(companies);
        return companiesMapper.toDto(companies);
    }

    /**
     * Partially update a companies.
     *
     * @param companiesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompaniesDTO> partialUpdate(CompaniesDTO companiesDTO) {
        log.debug("Request to partially update Companies : {}", companiesDTO);

        return companiesRepository
            .findById(companiesDTO.getId())
            .map(existingCompanies -> {
                companiesMapper.partialUpdate(existingCompanies, companiesDTO);

                return existingCompanies;
            })
            .map(companiesRepository::save)
            .map(companiesMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompaniesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companiesRepository.findAll(pageable).map(companiesMapper::toDto);
    }

    /**
     * Get all the companies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CompaniesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return companiesRepository.findAllWithEagerRelationships(pageable).map(companiesMapper::toDto);
    }

    /**
     * Get one companies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompaniesDTO> findOne(Long id) {
        log.debug("Request to get Companies : {}", id);
        return companiesRepository.findOneWithEagerRelationships(id).map(companiesMapper::toDto);
    }

    /**
     * Delete the companies by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Companies : {}", id);
        companiesRepository.deleteById(id);
    }
}
