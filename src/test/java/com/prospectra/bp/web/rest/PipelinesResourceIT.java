package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Pipelines;
import com.prospectra.bp.repository.PipelinesRepository;
import com.prospectra.bp.service.dto.PipelinesDTO;
import com.prospectra.bp.service.mapper.PipelinesMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PipelinesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PipelinesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OWNING_USER = "AAAAAAAAAA";
    private static final String UPDATED_OWNING_USER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pipelines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PipelinesRepository pipelinesRepository;

    @Autowired
    private PipelinesMapper pipelinesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPipelinesMockMvc;

    private Pipelines pipelines;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipelines createEntity(EntityManager em) {
        Pipelines pipelines = new Pipelines()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .owningUser(DEFAULT_OWNING_USER);
        return pipelines;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pipelines createUpdatedEntity(EntityManager em) {
        Pipelines pipelines = new Pipelines()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .owningUser(UPDATED_OWNING_USER);
        return pipelines;
    }

    @BeforeEach
    public void initTest() {
        pipelines = createEntity(em);
    }

    @Test
    @Transactional
    void createPipelines() throws Exception {
        int databaseSizeBeforeCreate = pipelinesRepository.findAll().size();
        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);
        restPipelinesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeCreate + 1);
        Pipelines testPipelines = pipelinesList.get(pipelinesList.size() - 1);
        assertThat(testPipelines.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPipelines.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPipelines.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPipelines.getOwningUser()).isEqualTo(DEFAULT_OWNING_USER);
    }

    @Test
    @Transactional
    void createPipelinesWithExistingId() throws Exception {
        // Create the Pipelines with an existing ID
        pipelines.setId(1L);
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        int databaseSizeBeforeCreate = pipelinesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPipelinesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelinesRepository.findAll().size();
        // set the field null
        pipelines.setName(null);

        // Create the Pipelines, which fails.
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        restPipelinesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pipelinesRepository.findAll().size();
        // set the field null
        pipelines.setDescription(null);

        // Create the Pipelines, which fails.
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        restPipelinesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPipelines() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        // Get all the pipelinesList
        restPipelinesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pipelines.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].owningUser").value(hasItem(DEFAULT_OWNING_USER)));
    }

    @Test
    @Transactional
    void getPipelines() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        // Get the pipelines
        restPipelinesMockMvc
            .perform(get(ENTITY_API_URL_ID, pipelines.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pipelines.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.owningUser").value(DEFAULT_OWNING_USER));
    }

    @Test
    @Transactional
    void getNonExistingPipelines() throws Exception {
        // Get the pipelines
        restPipelinesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPipelines() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();

        // Update the pipelines
        Pipelines updatedPipelines = pipelinesRepository.findById(pipelines.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPipelines are not directly saved in db
        em.detach(updatedPipelines);
        updatedPipelines
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .owningUser(UPDATED_OWNING_USER);
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(updatedPipelines);

        restPipelinesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelinesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
        Pipelines testPipelines = pipelinesList.get(pipelinesList.size() - 1);
        assertThat(testPipelines.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPipelines.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPipelines.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPipelines.getOwningUser()).isEqualTo(UPDATED_OWNING_USER);
    }

    @Test
    @Transactional
    void putNonExistingPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pipelinesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePipelinesWithPatch() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();

        // Update the pipelines using partial update
        Pipelines partialUpdatedPipelines = new Pipelines();
        partialUpdatedPipelines.setId(pipelines.getId());

        partialUpdatedPipelines.description(UPDATED_DESCRIPTION).owningUser(UPDATED_OWNING_USER);

        restPipelinesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelines.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelines))
            )
            .andExpect(status().isOk());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
        Pipelines testPipelines = pipelinesList.get(pipelinesList.size() - 1);
        assertThat(testPipelines.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPipelines.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPipelines.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPipelines.getOwningUser()).isEqualTo(UPDATED_OWNING_USER);
    }

    @Test
    @Transactional
    void fullUpdatePipelinesWithPatch() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();

        // Update the pipelines using partial update
        Pipelines partialUpdatedPipelines = new Pipelines();
        partialUpdatedPipelines.setId(pipelines.getId());

        partialUpdatedPipelines
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .owningUser(UPDATED_OWNING_USER);

        restPipelinesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPipelines.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPipelines))
            )
            .andExpect(status().isOk());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
        Pipelines testPipelines = pipelinesList.get(pipelinesList.size() - 1);
        assertThat(testPipelines.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPipelines.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPipelines.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPipelines.getOwningUser()).isEqualTo(UPDATED_OWNING_USER);
    }

    @Test
    @Transactional
    void patchNonExistingPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pipelinesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPipelines() throws Exception {
        int databaseSizeBeforeUpdate = pipelinesRepository.findAll().size();
        pipelines.setId(longCount.incrementAndGet());

        // Create the Pipelines
        PipelinesDTO pipelinesDTO = pipelinesMapper.toDto(pipelines);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPipelinesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pipelinesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pipelines in the database
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePipelines() throws Exception {
        // Initialize the database
        pipelinesRepository.saveAndFlush(pipelines);

        int databaseSizeBeforeDelete = pipelinesRepository.findAll().size();

        // Delete the pipelines
        restPipelinesMockMvc
            .perform(delete(ENTITY_API_URL_ID, pipelines.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pipelines> pipelinesList = pipelinesRepository.findAll();
        assertThat(pipelinesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
