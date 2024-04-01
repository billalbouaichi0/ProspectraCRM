package com.prospectra.bp.web.rest;

import com.prospectra.bp.repository.ContactsRepository;
import com.prospectra.bp.service.ContactsService;
import com.prospectra.bp.service.dto.ContactsDTO;
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
 * REST controller for managing {@link com.prospectra.bp.domain.Contacts}.
 */
@RestController
@RequestMapping("/api/contacts")
public class ContactsResource {

    private final Logger log = LoggerFactory.getLogger(ContactsResource.class);

    private static final String ENTITY_NAME = "prospectraContacts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactsService contactsService;

    private final ContactsRepository contactsRepository;

    public ContactsResource(ContactsService contactsService, ContactsRepository contactsRepository) {
        this.contactsService = contactsService;
        this.contactsRepository = contactsRepository;
    }

    /**
     * {@code POST  /contacts} : Create a new contacts.
     *
     * @param contactsDTO the contactsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactsDTO, or with status {@code 400 (Bad Request)} if the contacts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContactsDTO> createContacts(@Valid @RequestBody ContactsDTO contactsDTO) throws URISyntaxException {
        log.debug("REST request to save Contacts : {}", contactsDTO);
        if (contactsDTO.getId() != null) {
            throw new BadRequestAlertException("A new contacts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactsDTO result = contactsService.save(contactsDTO);
        return ResponseEntity
            .created(new URI("/api/contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contacts/:id} : Updates an existing contacts.
     *
     * @param id the id of the contactsDTO to save.
     * @param contactsDTO the contactsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactsDTO,
     * or with status {@code 400 (Bad Request)} if the contactsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactsDTO> updateContacts(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContactsDTO contactsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contacts : {}, {}", id, contactsDTO);
        if (contactsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactsDTO result = contactsService.update(contactsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contacts/:id} : Partial updates given fields of an existing contacts, field will ignore if it is null
     *
     * @param id the id of the contactsDTO to save.
     * @param contactsDTO the contactsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactsDTO,
     * or with status {@code 400 (Bad Request)} if the contactsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactsDTO> partialUpdateContacts(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContactsDTO contactsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contacts partially : {}, {}", id, contactsDTO);
        if (contactsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactsDTO> result = contactsService.partialUpdate(contactsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contacts} : get all the contacts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contacts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContactsDTO>> getAllContacts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Contacts");
        Page<ContactsDTO> page;
        if (eagerload) {
            page = contactsService.findAllWithEagerRelationships(pageable);
        } else {
            page = contactsService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contacts/:id} : get the "id" contacts.
     *
     * @param id the id of the contactsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactsDTO> getContacts(@PathVariable Long id) {
        log.debug("REST request to get Contacts : {}", id);
        Optional<ContactsDTO> contactsDTO = contactsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactsDTO);
    }

    /**
     * {@code DELETE  /contacts/:id} : delete the "id" contacts.
     *
     * @param id the id of the contactsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContacts(@PathVariable Long id) {
        log.debug("REST request to delete Contacts : {}", id);
        contactsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
