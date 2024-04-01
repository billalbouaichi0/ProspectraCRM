package com.prospectra.bp.service;

import com.prospectra.bp.domain.Stages;
import com.prospectra.bp.repository.StagesRepository;
import com.prospectra.bp.service.dto.StagesDTO;
import com.prospectra.bp.service.mapper.StagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Stages}.
 */
@Service
@Transactional
public class StagesService {

    private final Logger log = LoggerFactory.getLogger(StagesService.class);

    private final StagesRepository stagesRepository;

    private final StagesMapper stagesMapper;

    public StagesService(StagesRepository stagesRepository, StagesMapper stagesMapper) {
        this.stagesRepository = stagesRepository;
        this.stagesMapper = stagesMapper;
    }

    /**
     * Save a stages.
     *
     * @param stagesDTO the entity to save.
     * @return the persisted entity.
     */
    public StagesDTO save(StagesDTO stagesDTO) {
        log.debug("Request to save Stages : {}", stagesDTO);
        Stages stages = stagesMapper.toEntity(stagesDTO);
        stages = stagesRepository.save(stages);
        return stagesMapper.toDto(stages);
    }

    /**
     * Update a stages.
     *
     * @param stagesDTO the entity to save.
     * @return the persisted entity.
     */
    public StagesDTO update(StagesDTO stagesDTO) {
        log.debug("Request to update Stages : {}", stagesDTO);
        Stages stages = stagesMapper.toEntity(stagesDTO);
        stages = stagesRepository.save(stages);
        return stagesMapper.toDto(stages);
    }

    /**
     * Partially update a stages.
     *
     * @param stagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StagesDTO> partialUpdate(StagesDTO stagesDTO) {
        log.debug("Request to partially update Stages : {}", stagesDTO);

        return stagesRepository
            .findById(stagesDTO.getId())
            .map(existingStages -> {
                stagesMapper.partialUpdate(existingStages, stagesDTO);

                return existingStages;
            })
            .map(stagesRepository::save)
            .map(stagesMapper::toDto);
    }

    /**
     * Get all the stages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stages");
        return stagesRepository.findAll(pageable).map(stagesMapper::toDto);
    }

    /**
     * Get one stages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StagesDTO> findOne(Long id) {
        log.debug("Request to get Stages : {}", id);
        return stagesRepository.findById(id).map(stagesMapper::toDto);
    }

    /**
     * Delete the stages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Stages : {}", id);
        stagesRepository.deleteById(id);
    }
}
