package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.OpportunitiesTestSamples.*;
import static com.prospectra.bp.domain.PipelinesTestSamples.*;
import static com.prospectra.bp.domain.StagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stages.class);
        Stages stages1 = getStagesSample1();
        Stages stages2 = new Stages();
        assertThat(stages1).isNotEqualTo(stages2);

        stages2.setId(stages1.getId());
        assertThat(stages1).isEqualTo(stages2);

        stages2 = getStagesSample2();
        assertThat(stages1).isNotEqualTo(stages2);
    }

    @Test
    void opportunitiesTest() throws Exception {
        Stages stages = getStagesRandomSampleGenerator();
        Opportunities opportunitiesBack = getOpportunitiesRandomSampleGenerator();

        stages.addOpportunities(opportunitiesBack);
        assertThat(stages.getOpportunities()).containsOnly(opportunitiesBack);
        assertThat(opportunitiesBack.getStages()).isEqualTo(stages);

        stages.removeOpportunities(opportunitiesBack);
        assertThat(stages.getOpportunities()).doesNotContain(opportunitiesBack);
        assertThat(opportunitiesBack.getStages()).isNull();

        stages.opportunities(new HashSet<>(Set.of(opportunitiesBack)));
        assertThat(stages.getOpportunities()).containsOnly(opportunitiesBack);
        assertThat(opportunitiesBack.getStages()).isEqualTo(stages);

        stages.setOpportunities(new HashSet<>());
        assertThat(stages.getOpportunities()).doesNotContain(opportunitiesBack);
        assertThat(opportunitiesBack.getStages()).isNull();
    }

    @Test
    void pipelinesTest() throws Exception {
        Stages stages = getStagesRandomSampleGenerator();
        Pipelines pipelinesBack = getPipelinesRandomSampleGenerator();

        stages.setPipelines(pipelinesBack);
        assertThat(stages.getPipelines()).isEqualTo(pipelinesBack);

        stages.pipelines(null);
        assertThat(stages.getPipelines()).isNull();
    }
}
