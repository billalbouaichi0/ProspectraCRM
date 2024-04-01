package com.prospectra.bp.domain;

import static com.prospectra.bp.domain.OpportunitiesTestSamples.*;
import static com.prospectra.bp.domain.ProductsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prospectra.bp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProductsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Products.class);
        Products products1 = getProductsSample1();
        Products products2 = new Products();
        assertThat(products1).isNotEqualTo(products2);

        products2.setId(products1.getId());
        assertThat(products1).isEqualTo(products2);

        products2 = getProductsSample2();
        assertThat(products1).isNotEqualTo(products2);
    }

    @Test
    void opportunitiesTest() throws Exception {
        Products products = getProductsRandomSampleGenerator();
        Opportunities opportunitiesBack = getOpportunitiesRandomSampleGenerator();

        products.addOpportunities(opportunitiesBack);
        assertThat(products.getOpportunities()).containsOnly(opportunitiesBack);
        assertThat(opportunitiesBack.getProducts()).containsOnly(products);

        products.removeOpportunities(opportunitiesBack);
        assertThat(products.getOpportunities()).doesNotContain(opportunitiesBack);
        assertThat(opportunitiesBack.getProducts()).doesNotContain(products);

        products.opportunities(new HashSet<>(Set.of(opportunitiesBack)));
        assertThat(products.getOpportunities()).containsOnly(opportunitiesBack);
        assertThat(opportunitiesBack.getProducts()).containsOnly(products);

        products.setOpportunities(new HashSet<>());
        assertThat(products.getOpportunities()).doesNotContain(opportunitiesBack);
        assertThat(opportunitiesBack.getProducts()).doesNotContain(products);
    }
}
