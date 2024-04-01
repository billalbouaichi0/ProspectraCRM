package com.prospectra.bp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.prospectra.bp.IntegrationTest;
import com.prospectra.bp.domain.Contacts;
import com.prospectra.bp.repository.ContactsRepository;
import com.prospectra.bp.service.ContactsService;
import com.prospectra.bp.service.dto.ContactsDTO;
import com.prospectra.bp.service.mapper.ContactsMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ContactsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContactsResourceIT {

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

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

    private static final LocalDate DEFAULT_BIRTH_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactsRepository contactsRepository;

    @Mock
    private ContactsRepository contactsRepositoryMock;

    @Autowired
    private ContactsMapper contactsMapper;

    @Mock
    private ContactsService contactsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactsMockMvc;

    private Contacts contacts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contacts createEntity(EntityManager em) {
        Contacts contacts = new Contacts()
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .country(DEFAULT_COUNTRY)
            .region(DEFAULT_REGION)
            .subRegions(DEFAULT_SUB_REGIONS)
            .codePostal(DEFAULT_CODE_POSTAL)
            .address(DEFAULT_ADDRESS)
            .birthDay(DEFAULT_BIRTH_DAY);
        return contacts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contacts createUpdatedEntity(EntityManager em) {
        Contacts contacts = new Contacts()
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .birthDay(UPDATED_BIRTH_DAY);
        return contacts;
    }

    @BeforeEach
    public void initTest() {
        contacts = createEntity(em);
    }

    @Test
    @Transactional
    void createContacts() throws Exception {
        int databaseSizeBeforeCreate = contactsRepository.findAll().size();
        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);
        restContactsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeCreate + 1);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContacts.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContacts.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContacts.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testContacts.getSubRegions()).isEqualTo(DEFAULT_SUB_REGIONS);
        assertThat(testContacts.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testContacts.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testContacts.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);
    }

    @Test
    @Transactional
    void createContactsWithExistingId() throws Exception {
        // Create the Contacts with an existing ID
        contacts.setId(1L);
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        int databaseSizeBeforeCreate = contactsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsRepository.findAll().size();
        // set the field null
        contacts.setLastName(null);

        // Create the Contacts, which fails.
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        restContactsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsRepository.findAll().size();
        // set the field null
        contacts.setFirstName(null);

        // Create the Contacts, which fails.
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        restContactsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsRepository.findAll().size();
        // set the field null
        contacts.setCountry(null);

        // Create the Contacts, which fails.
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        restContactsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get all the contactsList
        restContactsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contacts.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.intValue())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.intValue())))
            .andExpect(jsonPath("$.[*].subRegions").value(hasItem(DEFAULT_SUB_REGIONS.intValue())))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].birthDay").value(hasItem(DEFAULT_BIRTH_DAY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contactsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contactsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContactsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contactsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContactsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contactsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get the contacts
        restContactsMockMvc
            .perform(get(ENTITY_API_URL_ID, contacts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contacts.getId().intValue()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.intValue()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.intValue()))
            .andExpect(jsonPath("$.subRegions").value(DEFAULT_SUB_REGIONS.intValue()))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.birthDay").value(DEFAULT_BIRTH_DAY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContacts() throws Exception {
        // Get the contacts
        restContactsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Update the contacts
        Contacts updatedContacts = contactsRepository.findById(contacts.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContacts are not directly saved in db
        em.detach(updatedContacts);
        updatedContacts
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .birthDay(UPDATED_BIRTH_DAY);
        ContactsDTO contactsDTO = contactsMapper.toDto(updatedContacts);

        restContactsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContacts.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContacts.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContacts.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testContacts.getSubRegions()).isEqualTo(UPDATED_SUB_REGIONS);
        assertThat(testContacts.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testContacts.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContacts.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    void putNonExistingContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactsWithPatch() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Update the contacts using partial update
        Contacts partialUpdatedContacts = new Contacts();
        partialUpdatedContacts.setId(contacts.getId());

        partialUpdatedContacts
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .country(UPDATED_COUNTRY)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS);

        restContactsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContacts.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContacts))
            )
            .andExpect(status().isOk());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContacts.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContacts.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContacts.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testContacts.getSubRegions()).isEqualTo(DEFAULT_SUB_REGIONS);
        assertThat(testContacts.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testContacts.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContacts.getBirthDay()).isEqualTo(DEFAULT_BIRTH_DAY);
    }

    @Test
    @Transactional
    void fullUpdateContactsWithPatch() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Update the contacts using partial update
        Contacts partialUpdatedContacts = new Contacts();
        partialUpdatedContacts.setId(contacts.getId());

        partialUpdatedContacts
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .country(UPDATED_COUNTRY)
            .region(UPDATED_REGION)
            .subRegions(UPDATED_SUB_REGIONS)
            .codePostal(UPDATED_CODE_POSTAL)
            .address(UPDATED_ADDRESS)
            .birthDay(UPDATED_BIRTH_DAY);

        restContactsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContacts.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContacts))
            )
            .andExpect(status().isOk());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContacts.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContacts.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContacts.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testContacts.getSubRegions()).isEqualTo(UPDATED_SUB_REGIONS);
        assertThat(testContacts.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testContacts.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContacts.getBirthDay()).isEqualTo(UPDATED_BIRTH_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();
        contacts.setId(longCount.incrementAndGet());

        // Create the Contacts
        ContactsDTO contactsDTO = contactsMapper.toDto(contacts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        int databaseSizeBeforeDelete = contactsRepository.findAll().size();

        // Delete the contacts
        restContactsMockMvc
            .perform(delete(ENTITY_API_URL_ID, contacts.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
