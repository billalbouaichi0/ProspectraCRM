package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Phones;
import com.prospectra.bp.repository.PhonesRepository;
import com.prospectra.bp.service.dto.PhonesDTO;
import com.prospectra.bp.service.mapper.PhonesMapper;
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
 * Integration tests for the {@link PhonesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhonesResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "4";
    private static final String UPDATED_PHONE = "432200";

    private static final String ENTITY_API_URL = "/api/phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhonesRepository phonesRepository;

    @Autowired
    private PhonesMapper phonesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhonesMockMvc;

    private Phones phones;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phones createEntity(EntityManager em) {
        Phones phones = new Phones().label(DEFAULT_LABEL).phone(DEFAULT_PHONE);
        return phones;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phones createUpdatedEntity(EntityManager em) {
        Phones phones = new Phones().label(UPDATED_LABEL).phone(UPDATED_PHONE);
        return phones;
    }

    @BeforeEach
    public void initTest() {
        phones = createEntity(em);
    }

    @Test
    @Transactional
    void createPhones() throws Exception {
        int databaseSizeBeforeCreate = phonesRepository.findAll().size();
        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);
        restPhonesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeCreate + 1);
        Phones testPhones = phonesList.get(phonesList.size() - 1);
        assertThat(testPhones.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testPhones.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createPhonesWithExistingId() throws Exception {
        // Create the Phones with an existing ID
        phones.setId(1L);
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        int databaseSizeBeforeCreate = phonesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhonesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = phonesRepository.findAll().size();
        // set the field null
        phones.setLabel(null);

        // Create the Phones, which fails.
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        restPhonesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhones() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        // Get all the phonesList
        restPhonesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phones.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getPhones() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        // Get the phones
        restPhonesMockMvc
            .perform(get(ENTITY_API_URL_ID, phones.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phones.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingPhones() throws Exception {
        // Get the phones
        restPhonesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhones() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();

        // Update the phones
        Phones updatedPhones = phonesRepository.findById(phones.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhones are not directly saved in db
        em.detach(updatedPhones);
        updatedPhones.label(UPDATED_LABEL).phone(UPDATED_PHONE);
        PhonesDTO phonesDTO = phonesMapper.toDto(updatedPhones);

        restPhonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phonesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
        Phones testPhones = phonesList.get(phonesList.size() - 1);
        assertThat(testPhones.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPhones.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phonesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhonesWithPatch() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();

        // Update the phones using partial update
        Phones partialUpdatedPhones = new Phones();
        partialUpdatedPhones.setId(phones.getId());

        partialUpdatedPhones.phone(UPDATED_PHONE);

        restPhonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhones.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhones))
            )
            .andExpect(status().isOk());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
        Phones testPhones = phonesList.get(phonesList.size() - 1);
        assertThat(testPhones.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testPhones.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdatePhonesWithPatch() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();

        // Update the phones using partial update
        Phones partialUpdatedPhones = new Phones();
        partialUpdatedPhones.setId(phones.getId());

        partialUpdatedPhones.label(UPDATED_LABEL).phone(UPDATED_PHONE);

        restPhonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhones.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhones))
            )
            .andExpect(status().isOk());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
        Phones testPhones = phonesList.get(phonesList.size() - 1);
        assertThat(testPhones.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testPhones.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phonesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhones() throws Exception {
        int databaseSizeBeforeUpdate = phonesRepository.findAll().size();
        phones.setId(longCount.incrementAndGet());

        // Create the Phones
        PhonesDTO phonesDTO = phonesMapper.toDto(phones);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phonesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phones in the database
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhones() throws Exception {
        // Initialize the database
        phonesRepository.saveAndFlush(phones);

        int databaseSizeBeforeDelete = phonesRepository.findAll().size();

        // Delete the phones
        restPhonesMockMvc
            .perform(delete(ENTITY_API_URL_ID, phones.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Phones> phonesList = phonesRepository.findAll();
        assertThat(phonesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
