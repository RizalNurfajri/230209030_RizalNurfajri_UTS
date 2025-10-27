package com.siakad.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void allArgsConstructorSetsFields() {
        Student s = new Student(
                "S-1001",
                "Rizal",
                "rizal@example.com",
                "Rekayasa Keamanan Siber",
                5,
                3.67,
                "ACTIVE"
        );

        assertEquals("S-1001", s.getStudentId());
        assertEquals("Rizal", s.getName());
        assertEquals("rizal@example.com", s.getEmail());
        assertEquals("Rekayasa Keamanan Siber", s.getMajor());
        assertEquals(5, s.getSemester());
        assertEquals(3.67, s.getGpa());
        assertEquals("ACTIVE", s.getAcademicStatus());
    }

    @Test
    void gettersAndSettersWork() {
        Student s = new Student();

        s.setStudentId("S-2002");
        s.setName("Ayu");
        s.setEmail("ayu@example.com");
        s.setMajor("Informatika");
        s.setSemester(3);
        s.setGpa(3.2);
        s.setAcademicStatus("PROBATION");

        assertEquals("S-2002", s.getStudentId());
        assertEquals("Ayu", s.getName());
        assertEquals("ayu@example.com", s.getEmail());
        assertEquals("Informatika", s.getMajor());
        assertEquals(3, s.getSemester());
        assertEquals(3.2, s.getGpa());
        assertEquals("PROBATION", s.getAcademicStatus());
    }
}
