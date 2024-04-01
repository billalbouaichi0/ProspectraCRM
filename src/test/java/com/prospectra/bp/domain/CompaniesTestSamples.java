package com.prospectra.bp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompaniesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Companies getCompaniesSample1() {
        return new Companies()
            .id(1L)
            .name("name1")
            .country(1L)
            .region(1L)
            .subRegions(1L)
            .codePostal("codePostal1")
            .address("address1")
            .employeeNumber(1L);
    }

    public static Companies getCompaniesSample2() {
        return new Companies()
            .id(2L)
            .name("name2")
            .country(2L)
            .region(2L)
            .subRegions(2L)
            .codePostal("codePostal2")
            .address("address2")
            .employeeNumber(2L);
    }

    public static Companies getCompaniesRandomSampleGenerator() {
        return new Companies()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .country(longCount.incrementAndGet())
            .region(longCount.incrementAndGet())
            .subRegions(longCount.incrementAndGet())
            .codePostal(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .employeeNumber(longCount.incrementAndGet());
    }
}
