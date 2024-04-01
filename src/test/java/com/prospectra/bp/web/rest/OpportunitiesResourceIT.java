package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Opportunities;
import com.prospectra.bp.domain.enumeration.MarketingModel;
import com.prospectra.bp.domain.enumeration.ProsOp;
import com.prospectra.bp.repository.OpportunitiesRepository;
import com.prospectra.bp.service.OpportunitiesService;
import com.prospectra.bp.service.dto.OpportunitiesDTO;
import com.prospectra.bp.service.mapper.OpportunitiesMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OpportunitiesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OpportunitiesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ProsOp DEFAULT_PROSPECT_LEVEL = ProsOp.PROSPECT;
    private static final ProsOp UPDATED_PROSPECT_LEVEL = ProsOp.OPPORTUNITIES;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final MarketingModel DEFAULT_MODEL = MarketingModel.B2C;
    private static final MarketingModel UPDATED_MODEL = MarketingModel.B2B;

    private static final String ENTITY_API_URL = "/api/opportunities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpportunitiesRepository opportunitiesRepository;

    @Mock
    private OpportunitiesRepository opportunitiesRepositoryMock;

    @Autowired
    private OpportunitiesMapper opportunitiesMapper;

    @Mock
    private OpportunitiesService opportunitiesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpportunitiesMockMvc;

    private Opportunities opportunities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunities createEntity(EntityManager em) {
        Opportunities opportunities = new Opportunities()
            .name(DEFAULT_NAME)
            .prospectLevel(DEFAULT_PROSPECT_LEVEL)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .creationDate(DEFAULT_CREATION_DATE)
            .model(DEFAULT_MODEL);
        return opportunities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opportunities createUpdatedEntity(EntityManager em) {
        Opportunities opportunities = new Opportunities()
            .name(UPDATED_NAME)
            .prospectLevel(UPDATED_PROSPECT_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .creationDate(UPDATED_CREATION_DATE)
            .model(UPDATED_MODEL);
        return opportunities;
    }

    @BeforeEach
    public void initTest() {
        opportunities = createEntity(em);
    }

    @Test
    @Transactional
    void createOpportunities() throws Exception {
        int databaseSizeBeforeCreate = opportunitiesRepository.findAll().size();
        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);
        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeCreate + 1);
        Opportunities testOpportunities = opportunitiesList.get(opportunitiesList.size() - 1);
        assertThat(testOpportunities.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOpportunities.getProspectLevel()).isEqualTo(DEFAULT_PROSPECT_LEVEL);
        assertThat(testOpportunities.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOpportunities.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testOpportunities.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testOpportunities.getModel()).isEqualTo(DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void createOpportunitiesWithExistingId() throws Exception {
        // Create the Opportunities with an existing ID
        opportunities.setId(1L);
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        int databaseSizeBeforeCreate = opportunitiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunitiesRepository.findAll().size();
        // set the field null
        opportunities.setName(null);

        // Create the Opportunities, which fails.
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProspectLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunitiesRepository.findAll().size();
        // set the field null
        opportunities.setProspectLevel(null);

        // Create the Opportunities, which fails.
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunitiesRepository.findAll().size();
        // set the field null
        opportunities.setDescription(null);

        // Create the Opportunities, which fails.
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = opportunitiesRepository.findAll().size();
        // set the field null
        opportunities.setAmount(null);

        // Create the Opportunities, which fails.
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        restOpportunitiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpportunities() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        // Get all the opportunitiesList
        restOpportunitiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opportunities.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].prospectLevel").value(hasItem(DEFAULT_PROSPECT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOpportunitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(opportunitiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOpportunitiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(opportunitiesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOpportunitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(opportunitiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOpportunitiesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(opportunitiesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOpportunities() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        // Get the opportunities
        restOpportunitiesMockMvc
            .perform(get(ENTITY_API_URL_ID, opportunities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opportunities.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.prospectLevel").value(DEFAULT_PROSPECT_LEVEL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOpportunities() throws Exception {
        // Get the opportunities
        restOpportunitiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOpportunities() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();

        // Update the opportunities
        Opportunities updatedOpportunities = opportunitiesRepository.findById(opportunities.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOpportunities are not directly saved in db
        em.detach(updatedOpportunities);
        updatedOpportunities
            .name(UPDATED_NAME)
            .prospectLevel(UPDATED_PROSPECT_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .creationDate(UPDATED_CREATION_DATE)
            .model(UPDATED_MODEL);
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(updatedOpportunities);

        restOpportunitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opportunitiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
        Opportunities testOpportunities = opportunitiesList.get(opportunitiesList.size() - 1);
        assertThat(testOpportunities.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpportunities.getProspectLevel()).isEqualTo(UPDATED_PROSPECT_LEVEL);
        assertThat(testOpportunities.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpportunities.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOpportunities.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testOpportunities.getModel()).isEqualTo(UPDATED_MODEL);
    }

    @Test
    @Transactional
    void putNonExistingOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opportunitiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpportunitiesWithPatch() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();

        // Update the opportunities using partial update
        Opportunities partialUpdatedOpportunities = new Opportunities();
        partialUpdatedOpportunities.setId(opportunities.getId());

        partialUpdatedOpportunities.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).model(UPDATED_MODEL);

        restOpportunitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpportunities.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpportunities))
            )
            .andExpect(status().isOk());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
        Opportunities testOpportunities = opportunitiesList.get(opportunitiesList.size() - 1);
        assertThat(testOpportunities.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpportunities.getProspectLevel()).isEqualTo(DEFAULT_PROSPECT_LEVEL);
        assertThat(testOpportunities.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpportunities.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testOpportunities.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testOpportunities.getModel()).isEqualTo(UPDATED_MODEL);
    }

    @Test
    @Transactional
    void fullUpdateOpportunitiesWithPatch() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();

        // Update the opportunities using partial update
        Opportunities partialUpdatedOpportunities = new Opportunities();
        partialUpdatedOpportunities.setId(opportunities.getId());

        partialUpdatedOpportunities
            .name(UPDATED_NAME)
            .prospectLevel(UPDATED_PROSPECT_LEVEL)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .creationDate(UPDATED_CREATION_DATE)
            .model(UPDATED_MODEL);

        restOpportunitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpportunities.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpportunities))
            )
            .andExpect(status().isOk());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
        Opportunities testOpportunities = opportunitiesList.get(opportunitiesList.size() - 1);
        assertThat(testOpportunities.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpportunities.getProspectLevel()).isEqualTo(UPDATED_PROSPECT_LEVEL);
        assertThat(testOpportunities.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpportunities.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testOpportunities.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testOpportunities.getModel()).isEqualTo(UPDATED_MODEL);
    }

    @Test
    @Transactional
    void patchNonExistingOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opportunitiesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpportunities() throws Exception {
        int databaseSizeBeforeUpdate = opportunitiesRepository.findAll().size();
        opportunities.setId(longCount.incrementAndGet());

        // Create the Opportunities
        OpportunitiesDTO opportunitiesDTO = opportunitiesMapper.toDto(opportunities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpportunitiesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opportunitiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opportunities in the database
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpportunities() throws Exception {
        // Initialize the database
        opportunitiesRepository.saveAndFlush(opportunities);

        int databaseSizeBeforeDelete = opportunitiesRepository.findAll().size();

        // Delete the opportunities
        restOpportunitiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, opportunities.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opportunities> opportunitiesList = opportunitiesRepository.findAll();
        assertThat(opportunitiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
