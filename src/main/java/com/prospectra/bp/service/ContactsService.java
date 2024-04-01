package com.prospectra.bp.service;

import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.repository.ContactsRepository;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.mapper.ContactsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Contacts}.
 */
@Service
@Transactional
public class ContactsService {

    private final Logger log = LoggerFactory.getLogger(ContactsService.class);

    private final ContactsRepository contactsRepository;

    private final ContactsMapper contactsMapper;

    public ContactsService(ContactsRepository contactsRepository, ContactsMapper contactsMapper) {
        this.contactsRepository = contactsRepository;
        this.contactsMapper = contactsMapper;
    }

    /**
     * Save a contacts.
     *
     * @param contactsDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactsDTO save(ContactsDTO contactsDTO) {
        log.debug("Request to save Contacts : {}", contactsDTO);
        Contacts contacts = contactsMapper.toEntity(contactsDTO);
        contacts = contactsRepository.save(contacts);
        return contactsMapper.toDto(contacts);
    }

    /**
     * Update a contacts.
     *
     * @param contactsDTO the entity to save.
     * @return the persisted entity.
     */
    public ContactsDTO update(ContactsDTO contactsDTO) {
        log.debug("Request to update Contacts : {}", contactsDTO);
        Contacts contacts = contactsMapper.toEntity(contactsDTO);
        contacts = contactsRepository.save(contacts);
        return contactsMapper.toDto(contacts);
    }

    /**
     * Partially update a contacts.
     *
     * @param contactsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContactsDTO> partialUpdate(ContactsDTO contactsDTO) {
        log.debug("Request to partially update Contacts : {}", contactsDTO);

        return contactsRepository
            .findById(contactsDTO.getId())
            .map(existingContacts -> {
                contactsMapper.partialUpdate(existingContacts, contactsDTO);

                return existingContacts;
            })
            .map(contactsRepository::save)
            .map(contactsMapper::toDto);
    }

    /**
     * Get all the contacts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        return contactsRepository.findAll(pageable).map(contactsMapper::toDto);
    }

    /**
     * Get all the contacts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContactsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contactsRepository.findAllWithEagerRelationships(pageable).map(contactsMapper::toDto);
    }

    /**
     * Get one contacts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContactsDTO> findOne(Long id) {
        log.debug("Request to get Contacts : {}", id);
        return contactsRepository.findOneWithEagerRelationships(id).map(contactsMapper::toDto);
    }

    /**
     * Delete the contacts by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contacts : {}", id);
        contactsRepository.deleteById(id);
    }
}
