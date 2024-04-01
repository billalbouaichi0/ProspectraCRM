package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class OpportunitiesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Opportunities getOpportunitiesSample1() {
        return new Opportunities().id(1L).name("name1").description("description1");
    }

    public static Opportunities getOpportunitiesSample2() {
        return new Opportunities().id(2L).name("name2").description("description2");
    }

    public static Opportunities getOpportunitiesRandomSampleGenerator() {
        return new Opportunities()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
