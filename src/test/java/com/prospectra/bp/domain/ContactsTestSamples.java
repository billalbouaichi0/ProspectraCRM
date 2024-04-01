package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Contacts getContactsSample1() {
        return new Contacts()
            .id(1L)
            .lastName("lastName1")
            .firstName("firstName1")
            .country(1L)
            .region(1L)
            .subRegions(1L)
            .codePostal("codePostal1")
            .address("address1");
    }

    public static Contacts getContactsSample2() {
        return new Contacts()
            .id(2L)
            .lastName("lastName2")
            .firstName("firstName2")
            .country(2L)
            .region(2L)
            .subRegions(2L)
            .codePostal("codePostal2")
            .address("address2");
    }

    public static Contacts getContactsRandomSampleGenerator() {
        return new Contacts()
            .id(longCount.incrementAndGet())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .country(longCount.incrementAndGet())
            .region(longCount.incrementAndGet())
            .subRegions(longCount.incrementAndGet())
            .codePostal(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
