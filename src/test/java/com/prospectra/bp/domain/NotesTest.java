package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.NotesTestSamples.*;
import static com.prospectra.bp.domain.OpportunitiesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notes.class);
        Notes notes1 = getNotesSample1();
        Notes notes2 = new Notes();
        assertThat(notes1).isNotEqualTo(notes2);

        notes2.setId(notes1.getId());
        assertThat(notes1).isEqualTo(notes2);

        notes2 = getNotesSample2();
        assertThat(notes1).isNotEqualTo(notes2);
    }

    @Test
    void opportunitiesTest() throws Exception {
        Notes notes = getNotesRandomSampleGenerator();
        Opportunities opportunitiesBack = getOpportunitiesRandomSampleGenerator();

        notes.setOpportunities(opportunitiesBack);
        assertThat(notes.getOpportunities()).isEqualTo(opportunitiesBack);

        notes.opportunities(null);
        assertThat(notes.getOpportunities()).isNull();
    }
}
