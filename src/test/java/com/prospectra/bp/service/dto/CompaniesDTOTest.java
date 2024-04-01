package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompaniesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompaniesDTO.class);
        CompaniesDTO companiesDTO1 = new CompaniesDTO();
        companiesDTO1.setId(1L);
        CompaniesDTO companiesDTO2 = new CompaniesDTO();
        assertThat(companiesDTO1).isNotEqualTo(companiesDTO2);
        companiesDTO2.setId(companiesDTO1.getId());
        assertThat(companiesDTO1).isEqualTo(companiesDTO2);
        companiesDTO2.setId(2L);
        assertThat(companiesDTO1).isNotEqualTo(companiesDTO2);
        companiesDTO1.setId(null);
        assertThat(companiesDTO1).isNotEqualTo(companiesDTO2);
    }
}
