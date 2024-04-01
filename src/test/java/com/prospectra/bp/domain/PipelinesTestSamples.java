package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PipelinesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Pipelines getPipelinesSample1() {
        return new Pipelines().id(1L).name("name1").description("description1").owningUser("owningUser1");
    }

    public static Pipelines getPipelinesSample2() {
        return new Pipelines().id(2L).name("name2").description("description2").owningUser("owningUser2");
    }

    public static Pipelines getPipelinesRandomSampleGenerator() {
        return new Pipelines()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .owningUser(UUID.randomUUID().toString());
    }
}
