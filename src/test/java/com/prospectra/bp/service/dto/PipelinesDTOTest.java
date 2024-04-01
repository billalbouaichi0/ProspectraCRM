package com.prospectra.bp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PipelinesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PipelinesDTO.class);
        PipelinesDTO pipelinesDTO1 = new PipelinesDTO();
        pipelinesDTO1.setId(1L);
        PipelinesDTO pipelinesDTO2 = new PipelinesDTO();
        assertThat(pipelinesDTO1).isNotEqualTo(pipelinesDTO2);
        pipelinesDTO2.setId(pipelinesDTO1.getId());
        assertThat(pipelinesDTO1).isEqualTo(pipelinesDTO2);
        pipelinesDTO2.setId(2L);
        assertThat(pipelinesDTO1).isNotEqualTo(pipelinesDTO2);
        pipelinesDTO1.setId(null);
        assertThat(pipelinesDTO1).isNotEqualTo(pipelinesDTO2);
    }
}
