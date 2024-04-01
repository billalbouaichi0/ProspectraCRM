package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.OpportunitiesTestSamples.*;
import static com.prospectra.bp.domain.TasksTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TasksTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tasks.class);
        Tasks tasks1 = getTasksSample1();
        Tasks tasks2 = new Tasks();
        assertThat(tasks1).isNotEqualTo(tasks2);

        tasks2.setId(tasks1.getId());
        assertThat(tasks1).isEqualTo(tasks2);

        tasks2 = getTasksSample2();
        assertThat(tasks1).isNotEqualTo(tasks2);
    }

    @Test
    void opportunitiesTest() throws Exception {
        Tasks tasks = getTasksRandomSampleGenerator();
        Opportunities opportunitiesBack = getOpportunitiesRandomSampleGenerator();

        tasks.setOpportunities(opportunitiesBack);
        assertThat(tasks.getOpportunities()).isEqualTo(opportunitiesBack);

        tasks.opportunities(null);
        assertThat(tasks.getOpportunities()).isNull();
    }
}
