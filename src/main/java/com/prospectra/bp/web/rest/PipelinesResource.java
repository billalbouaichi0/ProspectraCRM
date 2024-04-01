package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.PipelinesRepository;
import com.prospectra.bp.service.PipelinesService;
import com.prospectra.bp.service.dto.PipelinesDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Pipelines}.
 */
@RestController
@RequestMapping("/api/pipelines")
public class PipelinesResource {

    private final Logger log = LoggerFactory.getLogger(PipelinesResource.class);

    private static final String ENTITY_NAME = "prospectraPipelines";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PipelinesService pipelinesService;

    private final PipelinesRepository pipelinesRepository;

    public PipelinesResource(PipelinesService pipelinesService, PipelinesRepository pipelinesRepository) {
        this.pipelinesService = pipelinesService;
        this.pipelinesRepository = pipelinesRepository;
    }

    /**
     * {@code POST  /pipelines} : Create a new pipelines.
     *
     * @param pipelinesDTO the pipelinesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pipelinesDTO, or with status {@code 400 (Bad Request)} if the pipelines has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PipelinesDTO> createPipelines(@Valid @RequestBody PipelinesDTO pipelinesDTO) throws URISyntaxException {
        log.debug("REST request to save Pipelines : {}", pipelinesDTO);
        if (pipelinesDTO.getId() != null) {
            throw new BadRequestAlertException("A new pipelines cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PipelinesDTO result = pipelinesService.save(pipelinesDTO);
        return ResponseEntity
            .created(new URI("/api/pipelines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pipelines/:id} : Updates an existing pipelines.
     *
     * @param id the id of the pipelinesDTO to save.
     * @param pipelinesDTO the pipelinesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelinesDTO,
     * or with status {@code 400 (Bad Request)} if the pipelinesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pipelinesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PipelinesDTO> updatePipelines(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PipelinesDTO pipelinesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pipelines : {}, {}", id, pipelinesDTO);
        if (pipelinesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelinesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelinesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PipelinesDTO result = pipelinesService.update(pipelinesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pipelinesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pipelines/:id} : Partial updates given fields of an existing pipelines, field will ignore if it is null
     *
     * @param id the id of the pipelinesDTO to save.
     * @param pipelinesDTO the pipelinesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pipelinesDTO,
     * or with status {@code 400 (Bad Request)} if the pipelinesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pipelinesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pipelinesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PipelinesDTO> partialUpdatePipelines(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PipelinesDTO pipelinesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pipelines partially : {}, {}", id, pipelinesDTO);
        if (pipelinesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pipelinesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pipelinesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PipelinesDTO> result = pipelinesService.partialUpdate(pipelinesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pipelinesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pipelines} : get all the pipelines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pipelines in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PipelinesDTO>> getAllPipelines(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pipelines");
        Page<PipelinesDTO> page = pipelinesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pipelines/:id} : get the "id" pipelines.
     *
     * @param id the id of the pipelinesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pipelinesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PipelinesDTO> getPipelines(@PathVariable Long id) {
        log.debug("REST request to get Pipelines : {}", id);
        Optional<PipelinesDTO> pipelinesDTO = pipelinesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pipelinesDTO);
    }

    /**
     * {@code DELETE  /pipelines/:id} : delete the "id" pipelines.
     *
     * @param id the id of the pipelinesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePipelines(@PathVariable Long id) {
        log.debug("REST request to delete Pipelines : {}", id);
        pipelinesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
