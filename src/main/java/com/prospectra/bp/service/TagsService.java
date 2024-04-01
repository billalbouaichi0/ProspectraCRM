package com.prospectra.bp.service;

import com.prospectra.bp.domain.Tags;
import com.prospectra.bp.repository.TagsRepository;
import com.prospectra.bp.service.dto.TagsDTO;
import com.prospectra.bp.service.mapper.TagsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Tags}.
 */
@Service
@Transactional
public class TagsService {

    private final Logger log = LoggerFactory.getLogger(TagsService.class);

    private final TagsRepository tagsRepository;

    private final TagsMapper tagsMapper;

    public TagsService(TagsRepository tagsRepository, TagsMapper tagsMapper) {
        this.tagsRepository = tagsRepository;
        this.tagsMapper = tagsMapper;
    }

    /**
     * Save a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO save(TagsDTO tagsDTO) {
        log.debug("Request to save Tags : {}", tagsDTO);
        Tags tags = tagsMapper.toEntity(tagsDTO);
        tags = tagsRepository.save(tags);
        return tagsMapper.toDto(tags);
    }

    /**
     * Update a tags.
     *
     * @param tagsDTO the entity to save.
     * @return the persisted entity.
     */
    public TagsDTO update(TagsDTO tagsDTO) {
        log.debug("Request to update Tags : {}", tagsDTO);
        Tags tags = tagsMapper.toEntity(tagsDTO);
        tags = tagsRepository.save(tags);
        return tagsMapper.toDto(tags);
    }

    /**
     * Partially update a tags.
     *
     * @param tagsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TagsDTO> partialUpdate(TagsDTO tagsDTO) {
        log.debug("Request to partially update Tags : {}", tagsDTO);

        return tagsRepository
            .findById(tagsDTO.getId())
            .map(existingTags -> {
                tagsMapper.partialUpdate(existingTags, tagsDTO);

                return existingTags;
            })
            .map(tagsRepository::save)
            .map(tagsMapper::toDto);
    }

    /**
     * Get all the tags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TagsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagsRepository.findAll(pageable).map(tagsMapper::toDto);
    }

    /**
     * Get one tags by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TagsDTO> findOne(Long id) {
        log.debug("Request to get Tags : {}", id);
        return tagsRepository.findById(id).map(tagsMapper::toDto);
    }

    /**
     * Delete the tags by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tags : {}", id);
        tagsRepository.deleteById(id);
    }
}
