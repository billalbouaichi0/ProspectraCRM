package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.CompaniesTestSamples.*;
import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.EmailsTestSamples.*;
import static com.prospectra.bp.domain.TagsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompaniesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Companies.class);
        Companies companies1 = getCompaniesSample1();
        Companies companies2 = new Companies();
        assertThat(companies1).isNotEqualTo(companies2);

        companies2.setId(companies1.getId());
        assertThat(companies1).isEqualTo(companies2);

        companies2 = getCompaniesSample2();
        assertThat(companies1).isNotEqualTo(companies2);
    }

    @Test
    void emailsTest() throws Exception {
        Companies companies = getCompaniesRandomSampleGenerator();
        Emails emailsBack = getEmailsRandomSampleGenerator();

        companies.addEmails(emailsBack);
        assertThat(companies.getEmails()).containsOnly(emailsBack);
        assertThat(emailsBack.getCompanies()).isEqualTo(companies);

        companies.removeEmails(emailsBack);
        assertThat(companies.getEmails()).doesNotContain(emailsBack);
        assertThat(emailsBack.getCompanies()).isNull();

        companies.emails(new HashSet<>(Set.of(emailsBack)));
        assertThat(companies.getEmails()).containsOnly(emailsBack);
        assertThat(emailsBack.getCompanies()).isEqualTo(companies);

        companies.setEmails(new HashSet<>());
        assertThat(companies.getEmails()).doesNotContain(emailsBack);
        assertThat(emailsBack.getCompanies()).isNull();
    }

    @Test
    void tagsTest() throws Exception {
        Companies companies = getCompaniesRandomSampleGenerator();
        Tags tagsBack = getTagsRandomSampleGenerator();

        companies.addTags(tagsBack);
        assertThat(companies.getTags()).containsOnly(tagsBack);

        companies.removeTags(tagsBack);
        assertThat(companies.getTags()).doesNotContain(tagsBack);

        companies.tags(new HashSet<>(Set.of(tagsBack)));
        assertThat(companies.getTags()).containsOnly(tagsBack);

        companies.setTags(new HashSet<>());
        assertThat(companies.getTags()).doesNotContain(tagsBack);
    }

    @Test
    void contactsTest() throws Exception {
        Companies companies = getCompaniesRandomSampleGenerator();
        Contacts contactsBack = getContactsRandomSampleGenerator();

        companies.addContacts(contactsBack);
        assertThat(companies.getContacts()).containsOnly(contactsBack);

        companies.removeContacts(contactsBack);
        assertThat(companies.getContacts()).doesNotContain(contactsBack);

        companies.contacts(new HashSet<>(Set.of(contactsBack)));
        assertThat(companies.getContacts()).containsOnly(contactsBack);

        companies.setContacts(new HashSet<>());
        assertThat(companies.getContacts()).doesNotContain(contactsBack);
    }
}
