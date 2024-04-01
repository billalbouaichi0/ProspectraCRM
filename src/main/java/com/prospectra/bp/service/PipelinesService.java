package com.prospectra.bp.service;

import com.prospectra.bp.domain.Pipelines;
import com.prospectra.bp.repository.PipelinesRepository;
import com.prospectra.bp.service.dto.PipelinesDTO;
import com.prospectra.bp.service.mapper.PipelinesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Pipelines}.
 */
@Service
@Transactional
public class PipelinesService {

    private final Logger log = LoggerFactory.getLogger(PipelinesService.class);

    private final PipelinesRepository pipelinesRepository;

    private final PipelinesMapper pipelinesMapper;

    public PipelinesService(PipelinesRepository pipelinesRepository, PipelinesMapper pipelinesMapper) {
        this.pipelinesRepository = pipelinesRepository;
        this.pipelinesMapper = pipelinesMapper;
    }

    /**
     * Save a pipelines.
     *
     * @param pipelinesDTO the entity to save.
     * @return the persisted entity.
     */
    public PipelinesDTO save(PipelinesDTO pipelinesDTO) {
        log.debug("Request to save Pipelines : {}", pipelinesDTO);
        Pipelines pipelines = pipelinesMapper.toEntity(pipelinesDTO);
        pipelines = pipelinesRepository.save(pipelines);
        return pipelinesMapper.toDto(pipelines);
    }

    /**
     * Update a pipelines.
     *
     * @param pipelinesDTO the entity to save.
     * @return the persisted entity.
     */
    public PipelinesDTO update(PipelinesDTO pipelinesDTO) {
        log.debug("Request to update Pipelines : {}", pipelinesDTO);
        Pipelines pipelines = pipelinesMapper.toEntity(pipelinesDTO);
        pipelines = pipelinesRepository.save(pipelines);
        return pipelinesMapper.toDto(pipelines);
    }

    /**
     * Partially update a pipelines.
     *
     * @param pipelinesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PipelinesDTO> partialUpdate(PipelinesDTO pipelinesDTO) {
        log.debug("Request to partially update Pipelines : {}", pipelinesDTO);

        return pipelinesRepository
            .findById(pipelinesDTO.getId())
            .map(existingPipelines -> {
                pipelinesMapper.partialUpdate(existingPipelines, pipelinesDTO);

                return existingPipelines;
            })
            .map(pipelinesRepository::save)
            .map(pipelinesMapper::toDto);
    }

    /**
     * Get all the pipelines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PipelinesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pipelines");
        return pipelinesRepository.findAll(pageable).map(pipelinesMapper::toDto);
    }

    /**
     * Get one pipelines by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PipelinesDTO> findOne(Long id) {
        log.debug("Request to get Pipelines : {}", id);
        return pipelinesRepository.findById(id).map(pipelinesMapper::toDto);
    }

    /**
     * Delete the pipelines by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pipelines : {}", id);
        pipelinesRepository.deleteById(id);
    }
}
