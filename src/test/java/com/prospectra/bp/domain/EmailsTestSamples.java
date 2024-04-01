package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Emails getEmailsSample1() {
        return new Emails().id(1L).label("label1").mail("mail1");
    }

    public static Emails getEmailsSample2() {
        return new Emails().id(2L).label("label2").mail("mail2");
    }

    public static Emails getEmailsRandomSampleGenerator() {
        return new Emails().id(longCount.incrementAndGet()).label(UUID.randomUUID().toString()).mail(UUID.randomUUID().toString());
    }
}
