package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.OpportunitiesRepository;
import com.prospectra.bp.service.OpportunitiesService;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Opportunities}.
 */
@RestController
@RequestMapping("/api/opportunities")
public class OpportunitiesResource {

    private final Logger log = LoggerFactory.getLogger(OpportunitiesResource.class);

    private static final String ENTITY_NAME = "prospectraOpportunities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpportunitiesService opportunitiesService;

    private final OpportunitiesRepository opportunitiesRepository;

    public OpportunitiesResource(OpportunitiesService opportunitiesService, OpportunitiesRepository opportunitiesRepository) {
        this.opportunitiesService = opportunitiesService;
        this.opportunitiesRepository = opportunitiesRepository;
    }

    /**
     * {@code POST  /opportunities} : Create a new opportunities.
     *
     * @param opportunitiesDTO the opportunitiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opportunitiesDTO, or with status {@code 400 (Bad Request)} if the opportunities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OpportunitiesDTO> createOpportunities(@Valid @RequestBody OpportunitiesDTO opportunitiesDTO)
        throws URISyntaxException {
        log.debug("REST request to save Opportunities : {}", opportunitiesDTO);
        if (opportunitiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new opportunities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OpportunitiesDTO result = opportunitiesService.save(opportunitiesDTO);
        return ResponseEntity
            .created(new URI("/api/opportunities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /opportunities/:id} : Updates an existing opportunities.
     *
     * @param id the id of the opportunitiesDTO to save.
     * @param opportunitiesDTO the opportunitiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportunitiesDTO,
     * or with status {@code 400 (Bad Request)} if the opportunitiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opportunitiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OpportunitiesDTO> updateOpportunities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OpportunitiesDTO opportunitiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Opportunities : {}, {}", id, opportunitiesDTO);
        if (opportunitiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportunitiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opportunitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OpportunitiesDTO result = opportunitiesService.update(opportunitiesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, opportunitiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /opportunities/:id} : Partial updates given fields of an existing opportunities, field will ignore if it is null
     *
     * @param id the id of the opportunitiesDTO to save.
     * @param opportunitiesDTO the opportunitiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opportunitiesDTO,
     * or with status {@code 400 (Bad Request)} if the opportunitiesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the opportunitiesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the opportunitiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OpportunitiesDTO> partialUpdateOpportunities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OpportunitiesDTO opportunitiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Opportunities partially : {}, {}", id, opportunitiesDTO);
        if (opportunitiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opportunitiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opportunitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OpportunitiesDTO> result = opportunitiesService.partialUpdate(opportunitiesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, opportunitiesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /opportunities} : get all the opportunities.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opportunities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OpportunitiesDTO>> getAllOpportunities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Opportunities");
        Page<OpportunitiesDTO> page;
        if (eagerload) {
            page = opportunitiesService.findAllWithEagerRelationships(pageable);
        } else {
            page = opportunitiesService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /opportunities/:id} : get the "id" opportunities.
     *
     * @param id the id of the opportunitiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opportunitiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OpportunitiesDTO> getOpportunities(@PathVariable Long id) {
        log.debug("REST request to get Opportunities : {}", id);
        Optional<OpportunitiesDTO> opportunitiesDTO = opportunitiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opportunitiesDTO);
    }

    /**
     * {@code DELETE  /opportunities/:id} : delete the "id" opportunities.
     *
     * @param id the id of the opportunitiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpportunities(@PathVariable Long id) {
        log.debug("REST request to delete Opportunities : {}", id);
        opportunitiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
