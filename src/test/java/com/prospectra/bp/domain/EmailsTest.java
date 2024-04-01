package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.CompaniesTestSamples.*;
import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.EmailsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Emails.class);
        Emails emails1 = getEmailsSample1();
        Emails emails2 = new Emails();
        assertThat(emails1).isNotEqualTo(emails2);

        emails2.setId(emails1.getId());
        assertThat(emails1).isEqualTo(emails2);

        emails2 = getEmailsSample2();
        assertThat(emails1).isNotEqualTo(emails2);
    }

    @Test
    void contactsTest() throws Exception {
        Emails emails = getEmailsRandomSampleGenerator();
        Contacts contactsBack = getContactsRandomSampleGenerator();

        emails.setContacts(contactsBack);
        assertThat(emails.getContacts()).isEqualTo(contactsBack);

        emails.contacts(null);
        assertThat(emails.getContacts()).isNull();
    }

    @Test
    void companiesTest() throws Exception {
        Emails emails = getEmailsRandomSampleGenerator();
        Companies companiesBack = getCompaniesRandomSampleGenerator();

        emails.setCompanies(companiesBack);
        assertThat(emails.getCompanies()).isEqualTo(companiesBack);

        emails.companies(null);
        assertThat(emails.getCompanies()).isNull();
    }
}
