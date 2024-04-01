package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpportunitiesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OpportunitiesDTO.class);
        OpportunitiesDTO opportunitiesDTO1 = new OpportunitiesDTO();
        opportunitiesDTO1.setId(1L);
        OpportunitiesDTO opportunitiesDTO2 = new OpportunitiesDTO();
        assertThat(opportunitiesDTO1).isNotEqualTo(opportunitiesDTO2);
        opportunitiesDTO2.setId(opportunitiesDTO1.getId());
        assertThat(opportunitiesDTO1).isEqualTo(opportunitiesDTO2);
        opportunitiesDTO2.setId(2L);
        assertThat(opportunitiesDTO1).isNotEqualTo(opportunitiesDTO2);
        opportunitiesDTO1.setId(null);
        assertThat(opportunitiesDTO1).isNotEqualTo(opportunitiesDTO2);
    }
}
