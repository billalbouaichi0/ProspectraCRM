package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.EmailsRepository;
import com.prospectra.bp.service.EmailsService;
import com.prospectra.bp.service.dto.EmailsDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Emails}.
 */
@RestController
@RequestMapping("/api/emails")
public class EmailsResource {

    private final Logger log = LoggerFactory.getLogger(EmailsResource.class);

    private static final String ENTITY_NAME = "prospectraEmails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailsService emailsService;

    private final EmailsRepository emailsRepository;

    public EmailsResource(EmailsService emailsService, EmailsRepository emailsRepository) {
        this.emailsService = emailsService;
        this.emailsRepository = emailsRepository;
    }

    /**
     * {@code POST  /emails} : Create a new emails.
     *
     * @param emailsDTO the emailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailsDTO, or with status {@code 400 (Bad Request)} if the emails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmailsDTO> createEmails(@Valid @RequestBody EmailsDTO emailsDTO) throws URISyntaxException {
        log.debug("REST request to save Emails : {}", emailsDTO);
        if (emailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new emails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmailsDTO result = emailsService.save(emailsDTO);
        return ResponseEntity
            .created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emails/:id} : Updates an existing emails.
     *
     * @param id the id of the emailsDTO to save.
     * @param emailsDTO the emailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailsDTO,
     * or with status {@code 400 (Bad Request)} if the emailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmailsDTO> updateEmails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmailsDTO emailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Emails : {}, {}", id, emailsDTO);
        if (emailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmailsDTO result = emailsService.update(emailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emails/:id} : Partial updates given fields of an existing emails, field will ignore if it is null
     *
     * @param id the id of the emailsDTO to save.
     * @param emailsDTO the emailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailsDTO,
     * or with status {@code 400 (Bad Request)} if the emailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmailsDTO> partialUpdateEmails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmailsDTO emailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Emails partially : {}, {}", id, emailsDTO);
        if (emailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmailsDTO> result = emailsService.partialUpdate(emailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, emailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /emails} : get all the emails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emails in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmailsDTO>> getAllEmails(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Emails");
        Page<EmailsDTO> page = emailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emails/:id} : get the "id" emails.
     *
     * @param id the id of the emailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmailsDTO> getEmails(@PathVariable Long id) {
        log.debug("REST request to get Emails : {}", id);
        Optional<EmailsDTO> emailsDTO = emailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emailsDTO);
    }

    /**
     * {@code DELETE  /emails/:id} : delete the "id" emails.
     *
     * @param id the id of the emailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmails(@PathVariable Long id) {
        log.debug("REST request to delete Emails : {}", id);
        emailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
