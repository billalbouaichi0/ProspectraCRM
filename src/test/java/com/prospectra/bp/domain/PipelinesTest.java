package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.PipelinesTestSamples.*;
import static com.prospectra.bp.domain.StagesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PipelinesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pipelines.class);
        Pipelines pipelines1 = getPipelinesSample1();
        Pipelines pipelines2 = new Pipelines();
        assertThat(pipelines1).isNotEqualTo(pipelines2);

        pipelines2.setId(pipelines1.getId());
        assertThat(pipelines1).isEqualTo(pipelines2);

        pipelines2 = getPipelinesSample2();
        assertThat(pipelines1).isNotEqualTo(pipelines2);
    }

    @Test
    void stagesTest() throws Exception {
        Pipelines pipelines = getPipelinesRandomSampleGenerator();
        Stages stagesBack = getStagesRandomSampleGenerator();

        pipelines.addStages(stagesBack);
        assertThat(pipelines.getStages()).containsOnly(stagesBack);
        assertThat(stagesBack.getPipelines()).isEqualTo(pipelines);

        pipelines.removeStages(stagesBack);
        assertThat(pipelines.getStages()).doesNotContain(stagesBack);
        assertThat(stagesBack.getPipelines()).isNull();

        pipelines.stages(new HashSet<>(Set.of(stagesBack)));
        assertThat(pipelines.getStages()).containsOnly(stagesBack);
        assertThat(stagesBack.getPipelines()).isEqualTo(pipelines);

        pipelines.setStages(new HashSet<>());
        assertThat(pipelines.getStages()).doesNotContain(stagesBack);
        assertThat(stagesBack.getPipelines()).isNull();
    }
}
