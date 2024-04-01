package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.CompaniesTestSamples.*;
import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.TagsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tags.class);
        Tags tags1 = getTagsSample1();
        Tags tags2 = new Tags();
        assertThat(tags1).isNotEqualTo(tags2);

        tags2.setId(tags1.getId());
        assertThat(tags1).isEqualTo(tags2);

        tags2 = getTagsSample2();
        assertThat(tags1).isNotEqualTo(tags2);
    }

    @Test
    void contactsTest() throws Exception {
        Tags tags = getTagsRandomSampleGenerator();
        Contacts contactsBack = getContactsRandomSampleGenerator();

        tags.addContacts(contactsBack);
        assertThat(tags.getContacts()).containsOnly(contactsBack);
        assertThat(contactsBack.getTags()).containsOnly(tags);

        tags.removeContacts(contactsBack);
        assertThat(tags.getContacts()).doesNotContain(contactsBack);
        assertThat(contactsBack.getTags()).doesNotContain(tags);

        tags.contacts(new HashSet<>(Set.of(contactsBack)));
        assertThat(tags.getContacts()).containsOnly(contactsBack);
        assertThat(contactsBack.getTags()).containsOnly(tags);

        tags.setContacts(new HashSet<>());
        assertThat(tags.getContacts()).doesNotContain(contactsBack);
        assertThat(contactsBack.getTags()).doesNotContain(tags);
    }

    @Test
    void companiesTest() throws Exception {
        Tags tags = getTagsRandomSampleGenerator();
        Companies companiesBack = getCompaniesRandomSampleGenerator();

        tags.addCompanies(companiesBack);
        assertThat(tags.getCompanies()).containsOnly(companiesBack);
        assertThat(companiesBack.getTags()).containsOnly(tags);

        tags.removeCompanies(companiesBack);
        assertThat(tags.getCompanies()).doesNotContain(companiesBack);
        assertThat(companiesBack.getTags()).doesNotContain(tags);

        tags.companies(new HashSet<>(Set.of(companiesBack)));
        assertThat(tags.getCompanies()).containsOnly(companiesBack);
        assertThat(companiesBack.getTags()).containsOnly(tags);

        tags.setCompanies(new HashSet<>());
        assertThat(tags.getCompanies()).doesNotContain(companiesBack);
        assertThat(companiesBack.getTags()).doesNotContain(tags);
    }
}
