package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Emails;
import com.prospectra.bp.repository.EmailsRepository;
import com.prospectra.bp.service.dto.EmailsDTO;
import com.prospectra.bp.service.mapper.EmailsMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailsResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmailsRepository emailsRepository;

    @Autowired
    private EmailsMapper emailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailsMockMvc;

    private Emails emails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emails createEntity(EntityManager em) {
        Emails emails = new Emails().label(DEFAULT_LABEL).mail(DEFAULT_MAIL);
        return emails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emails createUpdatedEntity(EntityManager em) {
        Emails emails = new Emails().label(UPDATED_LABEL).mail(UPDATED_MAIL);
        return emails;
    }

    @BeforeEach
    public void initTest() {
        emails = createEntity(em);
    }

    @Test
    @Transactional
    void createEmails() throws Exception {
        int databaseSizeBeforeCreate = emailsRepository.findAll().size();
        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);
        restEmailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeCreate + 1);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testEmails.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    void createEmailsWithExistingId() throws Exception {
        // Create the Emails with an existing ID
        emails.setId(1L);
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        int databaseSizeBeforeCreate = emailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailsRepository.findAll().size();
        // set the field null
        emails.setLabel(null);

        // Create the Emails, which fails.
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        restEmailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailsRepository.findAll().size();
        // set the field null
        emails.setMail(null);

        // Create the Emails, which fails.
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        restEmailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        // Get all the emailsList
        restEmailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emails.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)));
    }

    @Test
    @Transactional
    void getEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        // Get the emails
        restEmailsMockMvc
            .perform(get(ENTITY_API_URL_ID, emails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emails.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL));
    }

    @Test
    @Transactional
    void getNonExistingEmails() throws Exception {
        // Get the emails
        restEmailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();

        // Update the emails
        Emails updatedEmails = emailsRepository.findById(emails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmails are not directly saved in db
        em.detach(updatedEmails);
        updatedEmails.label(UPDATED_LABEL).mail(UPDATED_MAIL);
        EmailsDTO emailsDTO = emailsMapper.toDto(updatedEmails);

        restEmailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testEmails.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    void putNonExistingEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailsWithPatch() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();

        // Update the emails using partial update
        Emails partialUpdatedEmails = new Emails();
        partialUpdatedEmails.setId(emails.getId());

        partialUpdatedEmails.label(UPDATED_LABEL);

        restEmailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmails))
            )
            .andExpect(status().isOk());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testEmails.getMail()).isEqualTo(DEFAULT_MAIL);
    }

    @Test
    @Transactional
    void fullUpdateEmailsWithPatch() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();

        // Update the emails using partial update
        Emails partialUpdatedEmails = new Emails();
        partialUpdatedEmails.setId(emails.getId());

        partialUpdatedEmails.label(UPDATED_LABEL).mail(UPDATED_MAIL);

        restEmailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmails))
            )
            .andExpect(status().isOk());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
        Emails testEmails = emailsList.get(emailsList.size() - 1);
        assertThat(testEmails.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testEmails.getMail()).isEqualTo(UPDATED_MAIL);
    }

    @Test
    @Transactional
    void patchNonExistingEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmails() throws Exception {
        int databaseSizeBeforeUpdate = emailsRepository.findAll().size();
        emails.setId(longCount.incrementAndGet());

        // Create the Emails
        EmailsDTO emailsDTO = emailsMapper.toDto(emails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emails in the database
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmails() throws Exception {
        // Initialize the database
        emailsRepository.saveAndFlush(emails);

        int databaseSizeBeforeDelete = emailsRepository.findAll().size();

        // Delete the emails
        restEmailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, emails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emails> emailsList = emailsRepository.findAll();
        assertThat(emailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
