package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.PhonesRepository;
import com.prospectra.bp.service.PhonesService;
import com.prospectra.bp.service.dto.PhonesDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Phones}.
 */
@RestController
@RequestMapping("/api/phones")
public class PhonesResource {

    private final Logger log = LoggerFactory.getLogger(PhonesResource.class);

    private static final String ENTITY_NAME = "prospectraPhones";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhonesService phonesService;

    private final PhonesRepository phonesRepository;

    public PhonesResource(PhonesService phonesService, PhonesRepository phonesRepository) {
        this.phonesService = phonesService;
        this.phonesRepository = phonesRepository;
    }

    /**
     * {@code POST  /phones} : Create a new phones.
     *
     * @param phonesDTO the phonesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phonesDTO, or with status {@code 400 (Bad Request)} if the phones has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhonesDTO> createPhones(@Valid @RequestBody PhonesDTO phonesDTO) throws URISyntaxException {
        log.debug("REST request to save Phones : {}", phonesDTO);
        if (phonesDTO.getId() != null) {
            throw new BadRequestAlertException("A new phones cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhonesDTO result = phonesService.save(phonesDTO);
        return ResponseEntity
            .created(new URI("/api/phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phones/:id} : Updates an existing phones.
     *
     * @param id the id of the phonesDTO to save.
     * @param phonesDTO the phonesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonesDTO,
     * or with status {@code 400 (Bad Request)} if the phonesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phonesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhonesDTO> updatePhones(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhonesDTO phonesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Phones : {}, {}", id, phonesDTO);
        if (phonesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhonesDTO result = phonesService.update(phonesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phonesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phones/:id} : Partial updates given fields of an existing phones, field will ignore if it is null
     *
     * @param id the id of the phonesDTO to save.
     * @param phonesDTO the phonesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonesDTO,
     * or with status {@code 400 (Bad Request)} if the phonesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the phonesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the phonesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhonesDTO> partialUpdatePhones(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhonesDTO phonesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phones partially : {}, {}", id, phonesDTO);
        if (phonesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phonesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhonesDTO> result = phonesService.partialUpdate(phonesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phonesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /phones} : get all the phones.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phones in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PhonesDTO>> getAllPhones(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Phones");
        Page<PhonesDTO> page = phonesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /phones/:id} : get the "id" phones.
     *
     * @param id the id of the phonesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phonesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhonesDTO> getPhones(@PathVariable Long id) {
        log.debug("REST request to get Phones : {}", id);
        Optional<PhonesDTO> phonesDTO = phonesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phonesDTO);
    }

    /**
     * {@code DELETE  /phones/:id} : delete the "id" phones.
     *
     * @param id the id of the phonesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhones(@PathVariable Long id) {
        log.debug("REST request to delete Phones : {}", id);
        phonesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
