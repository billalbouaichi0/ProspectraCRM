package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Companies;
import com.prospectra.bp.domain.enumeration.FormeJuridique;
import com.prospectra.bp.domain.enumeration.SecteurActivitie;
import com.prospectra.bp.domain.enumeration.TypeCompany;
import com.prospectra.bp.repository.CompaniesRepository;
import com.prospectra.bp.service.CompaniesService;
import com.prospectra.bp.service.dto.CompaniesDTO;
import com.prospectra.bp.service.mapper.CompaniesMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CompaniesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CompaniesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COUNTRY = 1L;
    private static final Long UPDATED_COUNTRY = 2L;

    private static final Long DEFAULT_REGION = 1L;
    private static final Long UPDATED_REGION = 2L;

    private static final Long DEFAULT_SUB_REGIONS = 1L;
    private static final Long UPDATED_SUB_REGIONS = 2L;

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final TypeCompany DEFAULT_TYPE = TypeCompany.Professionnel;
    private static final TypeCompany UPDATED_TYPE = TypeCompany.Particulier;

    private static final SecteurActivitie DEFAULT_SECTEUR_ACTIVITE = SecteurActivitie.Prive;
    private static final SecteurActivitie UPDATED_SECTEUR_ACTIVITE = SecteurActivitie.Public;

    private static final FormeJuridique DEFAULT_JURIDIQUE_FORM = FormeJuridique.EURL;
    private static final FormeJuridique UPDATED_JURIDIQUE_FORM = FormeJuridique.SARL;

    private static final Long DEFAULT_EMPLOYEE_NUMBER = 1L;
    private static final Long UPDATED_EMPLOYEE_NUMBER = 2L;

    private static final Double DEFAULT_CAA = 1D;
    private static final Double UPDATED_CAA = 2D;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompaniesRepository companiesRepository;

    @Mock
    private CompaniesRepository companiesRepositoryMock;

    @Autowired
    private CompaniesMapper companiesMapper;

    @Mock
    private CompaniesService companiesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompaniesMockMvc;

    private Companies companies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companies createEntity(EntityManager em) {
        Companies companies = new Companies()
            .name(DEFAULT_NAME)
            .country(DEFAULT_COUNTRY)
            .region(DEFAULT_REGION)
            .subRegions(DEFAULT_SUB_REGIONS)
            .codePostal(DEFAULT_CODE_POSTAL)
            .address(DEFAULT_ADDRESS)
            .type(DEFAULT_TYPE)
            .secteurActivite(DEFAULT_SECTEUR_ACTIVITE)
            .juridiqueForm(DEFAULT_JURIDIQUE_FORM)
            .employeeNumber(DEFAULT_EMPLOYEE_NUMBER)
            .caa(DEFAULT_CAA);
        return companies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companies createUpdatedEntity(EntityManager em) {
        Companies companies = new Companies()
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .juridiqueForm(UPDATED_JURIDIQUE_FORM)
            .employeeNumber(UPDATED_EMPLOYEE_NUMBER)
            .caa(UPDATED_CAA);
        return companies;
    }

    @BeforeEach
    public void initTest() {
        companies = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanies() throws Exception {
        int databaseSizeBeforeCreate = companiesRepository.findAll().size();
        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);
        restCompaniesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeCreate + 1);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanies.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompanies.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCompanies.getSubRegions()).isEqualTo(DEFAULT_SUB_REGIONS);
        assertThat(testCompanies.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testCompanies.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompanies.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCompanies.getSecteurActivite()).isEqualTo(DEFAULT_SECTEUR_ACTIVITE);
        assertThat(testCompanies.getJuridiqueForm()).isEqualTo(DEFAULT_JURIDIQUE_FORM);
        assertThat(testCompanies.getEmployeeNumber()).isEqualTo(DEFAULT_EMPLOYEE_NUMBER);
        assertThat(testCompanies.getCaa()).isEqualTo(DEFAULT_CAA);
    }

    @Test
    @Transactional
    void createCompaniesWithExistingId() throws Exception {
        // Create the Companies with an existing ID
        companies.setId(1L);
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        int databaseSizeBeforeCreate = companiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompaniesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companiesRepository.findAll().size();
        // set the field null
        companies.setName(null);

        // Create the Companies, which fails.
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        restCompaniesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = companiesRepository.findAll().size();
        // set the field null
        companies.setCountry(null);

        // Create the Companies, which fails.
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        restCompaniesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get all the companiesList
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companies.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.intValue())))
            .andExpect(jsonPath("$.[*].subRegions").value(hasItem(DEFAULT_SUB_REGIONS.intValue())))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].secteurActivite").value(hasItem(DEFAULT_SECTEUR_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].juridiqueForm").value(hasItem(DEFAULT_JURIDIQUE_FORM.toString())))
            .andExpect(jsonPath("$.[*].employeeNumber").value(hasItem(DEFAULT_EMPLOYEE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].caa").value(hasItem(DEFAULT_CAA.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompaniesWithEagerRelationshipsIsEnabled() throws Exception {
        when(companiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompaniesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(companiesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCompaniesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(companiesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCompaniesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(companiesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        // Get the companies
        restCompaniesMockMvc
            .perform(get(ENTITY_API_URL_ID, companies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companies.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.intValue()))
            .andExpect(jsonPath("$.subRegions").value(DEFAULT_SUB_REGIONS.intValue()))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.secteurActivite").value(DEFAULT_SECTEUR_ACTIVITE.toString()))
            .andExpect(jsonPath("$.juridiqueForm").value(DEFAULT_JURIDIQUE_FORM.toString()))
            .andExpect(jsonPath("$.employeeNumber").value(DEFAULT_EMPLOYEE_NUMBER.intValue()))
            .andExpect(jsonPath("$.caa").value(DEFAULT_CAA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCompanies() throws Exception {
        // Get the companies
        restCompaniesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies
        Companies updatedCompanies = companiesRepository.findById(companies.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompanies are not directly saved in db
        em.detach(updatedCompanies);
        updatedCompanies
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .juridiqueForm(UPDATED_JURIDIQUE_FORM)
            .employeeNumber(UPDATED_EMPLOYEE_NUMBER)
            .caa(UPDATED_CAA);
        CompaniesDTO companiesDTO = companiesMapper.toDto(updatedCompanies);

        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanies.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompanies.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCompanies.getSubRegions()).isEqualTo(UPDATED_SUB_REGIONS);
        assertThat(testCompanies.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testCompanies.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompanies.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompanies.getSecteurActivite()).isEqualTo(UPDATED_SECTEUR_ACTIVITE);
        assertThat(testCompanies.getJuridiqueForm()).isEqualTo(UPDATED_JURIDIQUE_FORM);
        assertThat(testCompanies.getEmployeeNumber()).isEqualTo(UPDATED_EMPLOYEE_NUMBER);
        assertThat(testCompanies.getCaa()).isEqualTo(UPDATED_CAA);
    }

    @Test
    @Transactional
    void putNonExistingCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companiesDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompaniesWithPatch() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies using partial update
        Companies partialUpdatedCompanies = new Companies();
        partialUpdatedCompanies.setId(companies.getId());

        partialUpdatedCompanies.address(UPDATED_ADDRESS).type(UPDATED_TYPE).employeeNumber(UPDATED_EMPLOYEE_NUMBER).caa(UPDATED_CAA);

        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanies.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanies))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompanies.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testCompanies.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testCompanies.getSubRegions()).isEqualTo(DEFAULT_SUB_REGIONS);
        assertThat(testCompanies.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testCompanies.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompanies.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompanies.getSecteurActivite()).isEqualTo(DEFAULT_SECTEUR_ACTIVITE);
        assertThat(testCompanies.getJuridiqueForm()).isEqualTo(DEFAULT_JURIDIQUE_FORM);
        assertThat(testCompanies.getEmployeeNumber()).isEqualTo(UPDATED_EMPLOYEE_NUMBER);
        assertThat(testCompanies.getCaa()).isEqualTo(UPDATED_CAA);
    }

    @Test
    @Transactional
    void fullUpdateCompaniesWithPatch() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();

        // Update the companies using partial update
        Companies partialUpdatedCompanies = new Companies();
        partialUpdatedCompanies.setId(companies.getId());

        partialUpdatedCompanies
            .name(UPDATED_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .type(UPDATED_TYPE)
            .secteurActivite(UPDATED_SECTEUR_ACTIVITE)
            .juridiqueForm(UPDATED_JURIDIQUE_FORM)
            .employeeNumber(UPDATED_EMPLOYEE_NUMBER)
            .caa(UPDATED_CAA);

        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanies.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanies))
            )
            .andExpect(status().isOk());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
        Companies testCompanies = companiesList.get(companiesList.size() - 1);
        assertThat(testCompanies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompanies.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testCompanies.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testCompanies.getSubRegions()).isEqualTo(UPDATED_SUB_REGIONS);
        assertThat(testCompanies.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testCompanies.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompanies.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCompanies.getSecteurActivite()).isEqualTo(UPDATED_SECTEUR_ACTIVITE);
        assertThat(testCompanies.getJuridiqueForm()).isEqualTo(UPDATED_JURIDIQUE_FORM);
        assertThat(testCompanies.getEmployeeNumber()).isEqualTo(UPDATED_EMPLOYEE_NUMBER);
        assertThat(testCompanies.getCaa()).isEqualTo(UPDATED_CAA);
    }

    @Test
    @Transactional
    void patchNonExistingCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companiesDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanies() throws Exception {
        int databaseSizeBeforeUpdate = companiesRepository.findAll().size();
        companies.setId(longCount.incrementAndGet());

        // Create the Companies
        CompaniesDTO companiesDTO = companiesMapper.toDto(companies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompaniesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companies in the database
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanies() throws Exception {
        // Initialize the database
        companiesRepository.saveAndFlush(companies);

        int databaseSizeBeforeDelete = companiesRepository.findAll().size();

        // Delete the companies
        restCompaniesMockMvc
            .perform(delete(ENTITY_API_URL_ID, companies.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Companies> companiesList = companiesRepository.findAll();
        assertThat(companiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
