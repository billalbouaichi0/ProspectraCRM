package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StagesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StagesDTO.class);
        StagesDTO stagesDTO1 = new StagesDTO();
        stagesDTO1.setId(1L);
        StagesDTO stagesDTO2 = new StagesDTO();
        assertThat(stagesDTO1).isNotEqualTo(stagesDTO2);
        stagesDTO2.setId(stagesDTO1.getId());
        assertThat(stagesDTO1).isEqualTo(stagesDTO2);
        stagesDTO2.setId(2L);
        assertThat(stagesDTO1).isNotEqualTo(stagesDTO2);
        stagesDTO1.setId(null);
        assertThat(stagesDTO1).isNotEqualTo(stagesDTO2);
    }
}
