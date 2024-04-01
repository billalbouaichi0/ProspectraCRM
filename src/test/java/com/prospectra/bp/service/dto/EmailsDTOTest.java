package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailsDTO.class);
        EmailsDTO emailsDTO1 = new EmailsDTO();
        emailsDTO1.setId(1L);
        EmailsDTO emailsDTO2 = new EmailsDTO();
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
        emailsDTO2.setId(emailsDTO1.getId());
        assertThat(emailsDTO1).isEqualTo(emailsDTO2);
        emailsDTO2.setId(2L);
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
        emailsDTO1.setId(null);
        assertThat(emailsDTO1).isNotEqualTo(emailsDTO2);
    }
}
