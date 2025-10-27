package com.siakad.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testDefaultConstructor_initializesEmptyList() {
        Course c = new Course();
        assertNotNull(c.getPrerequisites());
        assertTrue(c.getPrerequisites().isEmpty());
    }

    @Test
    void testParameterizedConstructor_initializesAllFieldsAndList() {
        Course c = new Course("IF101", "Pemrograman", 3, 40, 25, "Dr. Andi");

        assertEquals("IF101", c.getCourseCode());
        assertEquals("Pemrograman", c.getCourseName());
        assertEquals(3, c.getCredits());
        assertEquals(40, c.getCapacity());
        assertEquals(25, c.getEnrolledCount());
        assertEquals("Dr. Andi", c.getLecturer());
        assertNotNull(c.getPrerequisites());
        assertTrue(c.getPrerequisites().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        Course c = new Course();
        c.setCourseCode("IF102");
        c.setCourseName("Algoritma");
        c.setCredits(4);
        c.setCapacity(50);
        c.setEnrolledCount(30);
        c.setLecturer("Budi");
        c.setPrerequisites(List.of("IF001", "IF002"));

        assertEquals("IF102", c.getCourseCode());
        assertEquals("Algoritma", c.getCourseName());
        assertEquals(4, c.getCredits());
        assertEquals(50, c.getCapacity());
        assertEquals(30, c.getEnrolledCount());
        assertEquals("Budi", c.getLecturer());
        assertEquals(List.of("IF001", "IF002"), c.getPrerequisites());
    }

    // Cabang TRUE: this.prerequisites == null → inisialisasi baru
    @Test
    void testAddPrerequisite_initializesListIfNull() {
        Course c = new Course();
        c.setPrerequisites(null); // paksa null agar cabang if(true)
        c.addPrerequisite("IF001");

        assertNotNull(c.getPrerequisites());
        assertEquals(List.of("IF001"), c.getPrerequisites());
    }

    // Cabang FALSE: this.prerequisites != null → langsung menambah ke list lama
    @Test
    void testAddPrerequisite_whenListAlreadyExists() {
        Course c = new Course();
        List<String> existing = new ArrayList<>();
        existing.add("IF001");
        c.setPrerequisites(existing);

        c.addPrerequisite("IF002");

        assertSame(existing, c.getPrerequisites());
        assertEquals(List.of("IF001", "IF002"), c.getPrerequisites());
    }
}
