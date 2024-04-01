package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.CompaniesTestSamples.*;
import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.EmailsTestSamples.*;
import static com.prospectra.bp.domain.PhonesTestSamples.*;
import static com.prospectra.bp.domain.TagsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContactsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contacts.class);
        Contacts contacts1 = getContactsSample1();
        Contacts contacts2 = new Contacts();
        assertThat(contacts1).isNotEqualTo(contacts2);

        contacts2.setId(contacts1.getId());
        assertThat(contacts1).isEqualTo(contacts2);

        contacts2 = getContactsSample2();
        assertThat(contacts1).isNotEqualTo(contacts2);
    }

    @Test
    void emailsTest() throws Exception {
        Contacts contacts = getContactsRandomSampleGenerator();
        Emails emailsBack = getEmailsRandomSampleGenerator();

        contacts.addEmails(emailsBack);
        assertThat(contacts.getEmails()).containsOnly(emailsBack);
        assertThat(emailsBack.getContacts()).isEqualTo(contacts);

        contacts.removeEmails(emailsBack);
        assertThat(contacts.getEmails()).doesNotContain(emailsBack);
        assertThat(emailsBack.getContacts()).isNull();

        contacts.emails(new HashSet<>(Set.of(emailsBack)));
        assertThat(contacts.getEmails()).containsOnly(emailsBack);
        assertThat(emailsBack.getContacts()).isEqualTo(contacts);

        contacts.setEmails(new HashSet<>());
        assertThat(contacts.getEmails()).doesNotContain(emailsBack);
        assertThat(emailsBack.getContacts()).isNull();
    }

    @Test
    void phonesTest() throws Exception {
        Contacts contacts = getContactsRandomSampleGenerator();
        Phones phonesBack = getPhonesRandomSampleGenerator();

        contacts.addPhones(phonesBack);
        assertThat(contacts.getPhones()).containsOnly(phonesBack);
        assertThat(phonesBack.getContacts()).isEqualTo(contacts);

        contacts.removePhones(phonesBack);
        assertThat(contacts.getPhones()).doesNotContain(phonesBack);
        assertThat(phonesBack.getContacts()).isNull();

        contacts.phones(new HashSet<>(Set.of(phonesBack)));
        assertThat(contacts.getPhones()).containsOnly(phonesBack);
        assertThat(phonesBack.getContacts()).isEqualTo(contacts);

        contacts.setPhones(new HashSet<>());
        assertThat(contacts.getPhones()).doesNotContain(phonesBack);
        assertThat(phonesBack.getContacts()).isNull();
    }

    @Test
    void tagsTest() throws Exception {
        Contacts contacts = getContactsRandomSampleGenerator();
        Tags tagsBack = getTagsRandomSampleGenerator();

        contacts.addTags(tagsBack);
        assertThat(contacts.getTags()).containsOnly(tagsBack);

        contacts.removeTags(tagsBack);
        assertThat(contacts.getTags()).doesNotContain(tagsBack);

        contacts.tags(new HashSet<>(Set.of(tagsBack)));
        assertThat(contacts.getTags()).containsOnly(tagsBack);

        contacts.setTags(new HashSet<>());
        assertThat(contacts.getTags()).doesNotContain(tagsBack);
    }

    @Test
    void companiesTest() throws Exception {
        Contacts contacts = getContactsRandomSampleGenerator();
        Companies companiesBack = getCompaniesRandomSampleGenerator();

        contacts.addCompanies(companiesBack);
        assertThat(contacts.getCompanies()).containsOnly(companiesBack);
        assertThat(companiesBack.getContacts()).containsOnly(contacts);

        contacts.removeCompanies(companiesBack);
        assertThat(contacts.getCompanies()).doesNotContain(companiesBack);
        assertThat(companiesBack.getContacts()).doesNotContain(contacts);

        contacts.companies(new HashSet<>(Set.of(companiesBack)));
        assertThat(contacts.getCompanies()).containsOnly(companiesBack);
        assertThat(companiesBack.getContacts()).containsOnly(contacts);

        contacts.setCompanies(new HashSet<>());
        assertThat(contacts.getCompanies()).doesNotContain(companiesBack);
        assertThat(companiesBack.getContacts()).doesNotContain(contacts);
    }
}
