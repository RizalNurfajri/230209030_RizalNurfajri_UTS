package com.siakad.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    void gettersAndSettersWork() {
        Enrollment e = new Enrollment();

        LocalDateTime now = LocalDateTime.now();

        e.setEnrollmentId("ENR-001");
        e.setStudentId("S-1001");
        e.setCourseCode("IF101");
        e.setEnrollmentDate(now);
        e.setStatus("APPROVED");

        assertEquals("ENR-001", e.getEnrollmentId());
        assertEquals("S-1001", e.getStudentId());
        assertEquals("IF101", e.getCourseCode());
        assertEquals(now, e.getEnrollmentDate());
        assertEquals("APPROVED", e.getStatus());
    }

    @Test
    void parameterizedConstructor_setsAllFieldsCorrectly() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 2, 3, 4, 5);

        Enrollment e = new Enrollment(
                "ENR-777",
                "S-2002",
                "IF202",
                time,
                "PENDING"
        );

        assertEquals("ENR-777", e.getEnrollmentId());
        assertEquals("S-2002", e.getStudentId());
        assertEquals("IF202", e.getCourseCode());
        assertEquals(time, e.getEnrollmentDate());
        assertEquals("PENDING", e.getStatus());
    }
}
