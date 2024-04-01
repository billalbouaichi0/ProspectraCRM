package com.prospectra.bp.service;

import com.prospectra.bp.domain.Phones;
import com.prospectra.bp.repository.PhonesRepository;
import com.prospectra.bp.service.dto.PhonesDTO;
import com.prospectra.bp.service.mapper.PhonesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Phones}.
 */
@Service
@Transactional
public class PhonesService {

    private final Logger log = LoggerFactory.getLogger(PhonesService.class);

    private final PhonesRepository phonesRepository;

    private final PhonesMapper phonesMapper;

    public PhonesService(PhonesRepository phonesRepository, PhonesMapper phonesMapper) {
        this.phonesRepository = phonesRepository;
        this.phonesMapper = phonesMapper;
    }

    /**
     * Save a phones.
     *
     * @param phonesDTO the entity to save.
     * @return the persisted entity.
     */
    public PhonesDTO save(PhonesDTO phonesDTO) {
        log.debug("Request to save Phones : {}", phonesDTO);
        Phones phones = phonesMapper.toEntity(phonesDTO);
        phones = phonesRepository.save(phones);
        return phonesMapper.toDto(phones);
    }

    /**
     * Update a phones.
     *
     * @param phonesDTO the entity to save.
     * @return the persisted entity.
     */
    public PhonesDTO update(PhonesDTO phonesDTO) {
        log.debug("Request to update Phones : {}", phonesDTO);
        Phones phones = phonesMapper.toEntity(phonesDTO);
        phones = phonesRepository.save(phones);
        return phonesMapper.toDto(phones);
    }

    /**
     * Partially update a phones.
     *
     * @param phonesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhonesDTO> partialUpdate(PhonesDTO phonesDTO) {
        log.debug("Request to partially update Phones : {}", phonesDTO);

        return phonesRepository
            .findById(phonesDTO.getId())
            .map(existingPhones -> {
                phonesMapper.partialUpdate(existingPhones, phonesDTO);

                return existingPhones;
            })
            .map(phonesRepository::save)
            .map(phonesMapper::toDto);
    }

    /**
     * Get all the phones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PhonesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Phones");
        return phonesRepository.findAll(pageable).map(phonesMapper::toDto);
    }

    /**
     * Get one phones by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhonesDTO> findOne(Long id) {
        log.debug("Request to get Phones : {}", id);
        return phonesRepository.findById(id).map(phonesMapper::toDto);
    }

    /**
     * Delete the phones by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Phones : {}", id);
        phonesRepository.deleteById(id);
    }
}
