package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhonesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhonesDTO.class);
        PhonesDTO phonesDTO1 = new PhonesDTO();
        phonesDTO1.setId(1L);
        PhonesDTO phonesDTO2 = new PhonesDTO();
        assertThat(phonesDTO1).isNotEqualTo(phonesDTO2);
        phonesDTO2.setId(phonesDTO1.getId());
        assertThat(phonesDTO1).isEqualTo(phonesDTO2);
        phonesDTO2.setId(2L);
        assertThat(phonesDTO1).isNotEqualTo(phonesDTO2);
        phonesDTO1.setId(null);
        assertThat(phonesDTO1).isNotEqualTo(phonesDTO2);
    }
}
