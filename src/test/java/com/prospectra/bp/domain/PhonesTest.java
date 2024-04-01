package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.ContactsTestSamples.*;
import static com.prospectra.bp.domain.PhonesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhonesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phones.class);
        Phones phones1 = getPhonesSample1();
        Phones phones2 = new Phones();
        assertThat(phones1).isNotEqualTo(phones2);

        phones2.setId(phones1.getId());
        assertThat(phones1).isEqualTo(phones2);

        phones2 = getPhonesSample2();
        assertThat(phones1).isNotEqualTo(phones2);
    }

    @Test
    void contactsTest() throws Exception {
        Phones phones = getPhonesRandomSampleGenerator();
        Contacts contactsBack = getContactsRandomSampleGenerator();

        phones.setContacts(contactsBack);
        assertThat(phones.getContacts()).isEqualTo(contactsBack);

        phones.contacts(null);
        assertThat(phones.getContacts()).isNull();
    }
}
