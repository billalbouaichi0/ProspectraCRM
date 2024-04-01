package com.prospectra.bp.service;

import com.prospectra.bp.domain.Emails;
import com.prospectra.bp.repository.EmailsRepository;
import com.prospectra.bp.service.dto.EmailsDTO;
import com.prospectra.bp.service.mapper.EmailsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.prospectra.bp.domain.Emails}.
 */
@Service
@Transactional
public class EmailsService {

    private final Logger log = LoggerFactory.getLogger(EmailsService.class);

    private final EmailsRepository emailsRepository;

    private final EmailsMapper emailsMapper;

    public EmailsService(EmailsRepository emailsRepository, EmailsMapper emailsMapper) {
        this.emailsRepository = emailsRepository;
        this.emailsMapper = emailsMapper;
    }

    /**
     * Save a emails.
     *
     * @param emailsDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailsDTO save(EmailsDTO emailsDTO) {
        log.debug("Request to save Emails : {}", emailsDTO);
        Emails emails = emailsMapper.toEntity(emailsDTO);
        emails = emailsRepository.save(emails);
        return emailsMapper.toDto(emails);
    }

    /**
     * Update a emails.
     *
     * @param emailsDTO the entity to save.
     * @return the persisted entity.
     */
    public EmailsDTO update(EmailsDTO emailsDTO) {
        log.debug("Request to update Emails : {}", emailsDTO);
        Emails emails = emailsMapper.toEntity(emailsDTO);
        emails = emailsRepository.save(emails);
        return emailsMapper.toDto(emails);
    }

    /**
     * Partially update a emails.
     *
     * @param emailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmailsDTO> partialUpdate(EmailsDTO emailsDTO) {
        log.debug("Request to partially update Emails : {}", emailsDTO);

        return emailsRepository
            .findById(emailsDTO.getId())
            .map(existingEmails -> {
                emailsMapper.partialUpdate(existingEmails, emailsDTO);

                return existingEmails;
            })
            .map(emailsRepository::save)
            .map(emailsMapper::toDto);
    }

    /**
     * Get all the emails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EmailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Emails");
        return emailsRepository.findAll(pageable).map(emailsMapper::toDto);
    }

    /**
     * Get one emails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmailsDTO> findOne(Long id) {
        log.debug("Request to get Emails : {}", id);
        return emailsRepository.findById(id).map(emailsMapper::toDto);
    }

    /**
     * Delete the emails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Emails : {}", id);
        emailsRepository.deleteById(id);
    }
}
