package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.CompaniesTestSamples.*;
import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.NotesTestSamples.*;
import static com.prospectra.bp.domain.OpportunitiesTestSamples.*;
import static com.prospectra.bp.domain.ProductsTestSamples.*;
import static com.prospectra.bp.domain.StagesTestSamples.*;
import static com.prospectra.bp.domain.TasksTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OpportunitiesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opportunities.class);
        Opportunities opportunities1 = getOpportunitiesSample1();
        Opportunities opportunities2 = new Opportunities();
        assertThat(opportunities1).isNotEqualTo(opportunities2);

        opportunities2.setId(opportunities1.getId());
        assertThat(opportunities1).isEqualTo(opportunities2);

        opportunities2 = getOpportunitiesSample2();
        assertThat(opportunities1).isNotEqualTo(opportunities2);
    }

    @Test
    void tasksTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Tasks tasksBack = getTasksRandomSampleGenerator();

        opportunities.addTasks(tasksBack);
        assertThat(opportunities.getTasks()).containsOnly(tasksBack);
        assertThat(tasksBack.getOpportunities()).isEqualTo(opportunities);

        opportunities.removeTasks(tasksBack);
        assertThat(opportunities.getTasks()).doesNotContain(tasksBack);
        assertThat(tasksBack.getOpportunities()).isNull();

        opportunities.tasks(new HashSet<>(Set.of(tasksBack)));
        assertThat(opportunities.getTasks()).containsOnly(tasksBack);
        assertThat(tasksBack.getOpportunities()).isEqualTo(opportunities);

        opportunities.setTasks(new HashSet<>());
        assertThat(opportunities.getTasks()).doesNotContain(tasksBack);
        assertThat(tasksBack.getOpportunities()).isNull();
    }

    @Test
    void notesTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Notes notesBack = getNotesRandomSampleGenerator();

        opportunities.addNotes(notesBack);
        assertThat(opportunities.getNotes()).containsOnly(notesBack);
        assertThat(notesBack.getOpportunities()).isEqualTo(opportunities);

        opportunities.removeNotes(notesBack);
        assertThat(opportunities.getNotes()).doesNotContain(notesBack);
        assertThat(notesBack.getOpportunities()).isNull();

        opportunities.notes(new HashSet<>(Set.of(notesBack)));
        assertThat(opportunities.getNotes()).containsOnly(notesBack);
        assertThat(notesBack.getOpportunities()).isEqualTo(opportunities);

        opportunities.setNotes(new HashSet<>());
        assertThat(opportunities.getNotes()).doesNotContain(notesBack);
        assertThat(notesBack.getOpportunities()).isNull();
    }

    @Test
    void compagniesTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Companies companiesBack = getCompaniesRandomSampleGenerator();

        opportunities.setCompagnies(companiesBack);
        assertThat(opportunities.getCompagnies()).isEqualTo(companiesBack);

        opportunities.compagnies(null);
        assertThat(opportunities.getCompagnies()).isNull();
    }

    @Test
    void contactsTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Contacts contactsBack = getContactsRandomSampleGenerator();

        opportunities.setContacts(contactsBack);
        assertThat(opportunities.getContacts()).isEqualTo(contactsBack);

        opportunities.contacts(null);
        assertThat(opportunities.getContacts()).isNull();
    }

    @Test
    void productsTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Products productsBack = getProductsRandomSampleGenerator();

        opportunities.addProducts(productsBack);
        assertThat(opportunities.getProducts()).containsOnly(productsBack);

        opportunities.removeProducts(productsBack);
        assertThat(opportunities.getProducts()).doesNotContain(productsBack);

        opportunities.products(new HashSet<>(Set.of(productsBack)));
        assertThat(opportunities.getProducts()).containsOnly(productsBack);

        opportunities.setProducts(new HashSet<>());
        assertThat(opportunities.getProducts()).doesNotContain(productsBack);
    }

    @Test
    void stagesTest() throws Exception {
        Opportunities opportunities = getOpportunitiesRandomSampleGenerator();
        Stages stagesBack = getStagesRandomSampleGenerator();

        opportunities.setStages(stagesBack);
        assertThat(opportunities.getStages()).isEqualTo(stagesBack);

        opportunities.stages(null);
        assertThat(opportunities.getStages()).isNull();
    }
}
