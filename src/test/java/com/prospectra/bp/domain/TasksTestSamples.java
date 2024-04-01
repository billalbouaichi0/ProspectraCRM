package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TasksTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tasks getTasksSample1() {
        return new Tasks().id(1L).title("title1").type("type1").description("description1").status("status1");
    }

    public static Tasks getTasksSample2() {
        return new Tasks().id(2L).title("title2").type("type2").description("description2").status("status2");
    }

    public static Tasks getTasksRandomSampleGenerator() {
        return new Tasks()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
