package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Stages;
import com.prospectra.bp.repository.StagesRepository;
import com.prospectra.bp.service.dto.StagesDTO;
import com.prospectra.bp.service.mapper.StagesMapper;
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
 * Integration tests for the {@link StagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StagesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StagesRepository stagesRepository;

    @Autowired
    private StagesMapper stagesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStagesMockMvc;

    private Stages stages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stages createEntity(EntityManager em) {
        Stages stages = new Stages().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).order(DEFAULT_ORDER);
        return stages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stages createUpdatedEntity(EntityManager em) {
        Stages stages = new Stages().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).order(UPDATED_ORDER);
        return stages;
    }

    @BeforeEach
    public void initTest() {
        stages = createEntity(em);
    }

    @Test
    @Transactional
    void createStages() throws Exception {
        int databaseSizeBeforeCreate = stagesRepository.findAll().size();
        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);
        restStagesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeCreate + 1);
        Stages testStages = stagesList.get(stagesList.size() - 1);
        assertThat(testStages.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStages.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStages.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createStagesWithExistingId() throws Exception {
        // Create the Stages with an existing ID
        stages.setId(1L);
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        int databaseSizeBeforeCreate = stagesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStagesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stagesRepository.findAll().size();
        // set the field null
        stages.setName(null);

        // Create the Stages, which fails.
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        restStagesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = stagesRepository.findAll().size();
        // set the field null
        stages.setDescription(null);

        // Create the Stages, which fails.
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        restStagesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        // Get all the stagesList
        restStagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stages.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    void getStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        // Get the stages
        restStagesMockMvc
            .perform(get(ENTITY_API_URL_ID, stages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stages.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingStages() throws Exception {
        // Get the stages
        restStagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();

        // Update the stages
        Stages updatedStages = stagesRepository.findById(stages.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStages are not directly saved in db
        em.detach(updatedStages);
        updatedStages.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).order(UPDATED_ORDER);
        StagesDTO stagesDTO = stagesMapper.toDto(updatedStages);

        restStagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stagesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
        Stages testStages = stagesList.get(stagesList.size() - 1);
        assertThat(testStages.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStages.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stagesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStagesWithPatch() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();

        // Update the stages using partial update
        Stages partialUpdatedStages = new Stages();
        partialUpdatedStages.setId(stages.getId());

        partialUpdatedStages.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restStagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStages))
            )
            .andExpect(status().isOk());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
        Stages testStages = stagesList.get(stagesList.size() - 1);
        assertThat(testStages.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStages.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateStagesWithPatch() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();

        // Update the stages using partial update
        Stages partialUpdatedStages = new Stages();
        partialUpdatedStages.setId(stages.getId());

        partialUpdatedStages.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).order(UPDATED_ORDER);

        restStagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStages.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStages))
            )
            .andExpect(status().isOk());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
        Stages testStages = stagesList.get(stagesList.size() - 1);
        assertThat(testStages.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStages.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStages.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stagesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStages() throws Exception {
        int databaseSizeBeforeUpdate = stagesRepository.findAll().size();
        stages.setId(longCount.incrementAndGet());

        // Create the Stages
        StagesDTO stagesDTO = stagesMapper.toDto(stages);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStagesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stagesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stages in the database
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStages() throws Exception {
        // Initialize the database
        stagesRepository.saveAndFlush(stages);

        int databaseSizeBeforeDelete = stagesRepository.findAll().size();

        // Delete the stages
        restStagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, stages.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stages> stagesList = stagesRepository.findAll();
        assertThat(stagesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
