package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PhonesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Phones getPhonesSample1() {
        return new Phones().id(1L).label("label1").phone("phone1");
    }

    public static Phones getPhonesSample2() {
        return new Phones().id(2L).label("label2").phone("phone2");
    }

    public static Phones getPhonesRandomSampleGenerator() {
        return new Phones().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).phone(UUID.randomUUID().toString());
    }
}
