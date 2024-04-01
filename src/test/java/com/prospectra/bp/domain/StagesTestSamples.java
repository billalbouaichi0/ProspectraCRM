package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StagesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Stages getStagesSample1() {
        return new Stages().id(1L).name("name1").description("description1").order(1);
    }

    public static Stages getStagesSample2() {
        return new Stages().id(2L).name("name2").description("description2").order(2);
    }

    public static Stages getStagesRandomSampleGenerator() {
        return new Stages()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .order(intCount.incrementAndGet());
    }
}
