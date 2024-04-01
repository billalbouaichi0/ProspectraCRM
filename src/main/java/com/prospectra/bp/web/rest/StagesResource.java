package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.StagesRepository;
import com.prospectra.bp.service.StagesService;
import com.prospectra.bp.service.dto.StagesDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Stages}.
 */
@RestController
@RequestMapping("/api/stages")
public class StagesResource {

    private final Logger log = LoggerFactory.getLogger(StagesResource.class);

    private static final String ENTITY_NAME = "prospectraStages";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StagesService stagesService;

    private final StagesRepository stagesRepository;

    public StagesResource(StagesService stagesService, StagesRepository stagesRepository) {
        this.stagesService = stagesService;
        this.stagesRepository = stagesRepository;
    }

    /**
     * {@code POST  /stages} : Create a new stages.
     *
     * @param stagesDTO the stagesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stagesDTO, or with status {@code 400 (Bad Request)} if the stages has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StagesDTO> createStages(@Valid @RequestBody StagesDTO stagesDTO) throws URISyntaxException {
        log.debug("REST request to save Stages : {}", stagesDTO);
        if (stagesDTO.getId() != null) {
            throw new BadRequestAlertException("A new stages cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StagesDTO result = stagesService.save(stagesDTO);
        return ResponseEntity
            .created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stages/:id} : Updates an existing stages.
     *
     * @param id the id of the stagesDTO to save.
     * @param stagesDTO the stagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stagesDTO,
     * or with status {@code 400 (Bad Request)} if the stagesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StagesDTO> updateStages(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StagesDTO stagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Stages : {}, {}", id, stagesDTO);
        if (stagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StagesDTO result = stagesService.update(stagesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stagesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stages/:id} : Partial updates given fields of an existing stages, field will ignore if it is null
     *
     * @param id the id of the stagesDTO to save.
     * @param stagesDTO the stagesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stagesDTO,
     * or with status {@code 400 (Bad Request)} if the stagesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stagesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stagesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StagesDTO> partialUpdateStages(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StagesDTO stagesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stages partially : {}, {}", id, stagesDTO);
        if (stagesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stagesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stagesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StagesDTO> result = stagesService.partialUpdate(stagesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stagesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /stages} : get all the stages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StagesDTO>> getAllStages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Stages");
        Page<StagesDTO> page = stagesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stages/:id} : get the "id" stages.
     *
     * @param id the id of the stagesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stagesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StagesDTO> getStages(@PathVariable Long id) {
        log.debug("REST request to get Stages : {}", id);
        Optional<StagesDTO> stagesDTO = stagesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stagesDTO);
    }

    /**
     * {@code DELETE  /stages/:id} : delete the "id" stages.
     *
     * @param id the id of the stagesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStages(@PathVariable Long id) {
        log.debug("REST request to delete Stages : {}", id);
        stagesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
