package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.TagsRepository;
import com.prospectra.bp.service.TagsService;
import com.prospectra.bp.service.dto.TagsDTO;
import com.prospectra.bp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.prospectra.bp.domain.Tags}.
 */
@RestController
@RequestMapping("/api/tags")
public class TagsResource {

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);

    private static final String ENTITY_NAME = "prospectraTags";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagsService tagsService;

    private final TagsRepository tagsRepository;

    public TagsResource(TagsService tagsService, TagsRepository tagsRepository) {
        this.tagsService = tagsService;
        this.tagsRepository = tagsRepository;
    }

    /**
     * {@code POST  /tags} : Create a new tags.
     *
     * @param tagsDTO the tagsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagsDTO, or with status {@code 400 (Bad Request)} if the tags has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TagsDTO> createTags(@Valid @RequestBody TagsDTO tagsDTO) throws URISyntaxException {
        log.debug("REST request to save Tags : {}", tagsDTO);
        if (tagsDTO.getId() != null) {
            throw new BadRequestAlertException("A new tags cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagsDTO result = tagsService.save(tagsDTO);
        return ResponseEntity
            .created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tags/:id} : Updates an existing tags.
     *
     * @param id the id of the tagsDTO to save.
     * @param tagsDTO the tagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagsDTO,
     * or with status {@code 400 (Bad Request)} if the tagsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagsDTO> updateTags(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TagsDTO tagsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tags : {}, {}", id, tagsDTO);
        if (tagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TagsDTO result = tagsService.update(tagsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tagsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tags/:id} : Partial updates given fields of an existing tags, field will ignore if it is null
     *
     * @param id the id of the tagsDTO to save.
     * @param tagsDTO the tagsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagsDTO,
     * or with status {@code 400 (Bad Request)} if the tagsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagsDTO> partialUpdateTags(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TagsDTO tagsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tags partially : {}, {}", id, tagsDTO);
        if (tagsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagsDTO> result = tagsService.partialUpdate(tagsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tagsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TagsDTO>> getAllTags(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tags");
        Page<TagsDTO> page = tagsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tags.
     *
     * @param id the id of the tagsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagsDTO> getTags(@PathVariable Long id) {
        log.debug("REST request to get Tags : {}", id);
        Optional<TagsDTO> tagsDTO = tagsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagsDTO);
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tags.
     *
     * @param id the id of the tagsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTags(@PathVariable Long id) {
        log.debug("REST request to delete Tags : {}", id);
        tagsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
