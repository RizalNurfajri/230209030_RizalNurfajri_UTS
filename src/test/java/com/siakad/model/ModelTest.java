package com.siakad.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Satu file test untuk seluruh kelas model:
 * - Course
 * - Student
 * - Enrollment
 * - CourseGrade*
 * Fokus:
 * - ctor, getter/setter
 * - perilaku addPrerequisite() termasuk cabang saat prerequisites == null
 */
class ModelTest {

    // ===================== Student =====================
    // Memastikan semua setter dan getter pada kelas Student berfungsi dengan benar
    @Test
    @DisplayName("Student: ctor kosong + setter/getter semua field")
    void student_settersAndGetters_work() {
        Student s = new Student();
        s.setStudentId("S001");
        s.setName("Rizal");
        s.setEmail("rizal@example.com");
        s.setMajor("RKS");
        s.setSemester(3);
        s.setGpa(3.45);
        s.setAcademicStatus("ACTIVE");

        assertAll(
                () -> assertEquals("S001", s.getStudentId()),
                () -> assertEquals("Rizal", s.getName()),
                () -> assertEquals("rizal@example.com", s.getEmail()),
                () -> assertEquals("RKS", s.getMajor()),
                () -> assertEquals(3, s.getSemester()),
                () -> assertEquals(3.45, s.getGpa(), 1e-9),
                () -> assertEquals("ACTIVE", s.getAcademicStatus())
        );
    }
    // Memastikan constructor penuh dengan semua parameter mengisi semua field Student dengan benar.
    @Test
    @DisplayName("Student: ctor penuh mengisi semua field dengan benar")
    void student_fullConstructor_populatesFields() {
        Student s = new Student(
                "S002", "Nurfajri", "nurfajri@example.com",
                "Informatika", 5, 3.90, "PROBATION"
        );
        assertAll(
                () -> assertEquals("S002", s.getStudentId()),
                () -> assertEquals("Nurfajri", s.getName()),
                () -> assertEquals("nurfajri@example.com", s.getEmail()),
                () -> assertEquals("Informatika", s.getMajor()),
                () -> assertEquals(5, s.getSemester()),
                () -> assertEquals(3.90, s.getGpa(), 1e-9),
                () -> assertEquals("PROBATION", s.getAcademicStatus())
        );
    }

    // ===================== Enrollment =====================
    // Menguji semua setter dan getter kelas Enrollment
    @Test
    @DisplayName("Enrollment: ctor kosong + setter/getter semua field")
    void enrollment_settersAndGetters_work() {
        Enrollment e = new Enrollment();
        LocalDateTime now = LocalDateTime.of(2025, 10, 28, 12, 34, 56);

        e.setEnrollmentId("E001");
        e.setStudentId("S001");
        e.setCourseCode("IF101");
        e.setEnrollmentDate(now);
        e.setStatus("APPROVED");

        assertAll(
                () -> assertEquals("E001", e.getEnrollmentId()),
                () -> assertEquals("S001", e.getStudentId()),
                () -> assertEquals("IF101", e.getCourseCode()),
                () -> assertEquals(now, e.getEnrollmentDate()),
                () -> assertEquals("APPROVED", e.getStatus())
        );
    }
    // Memastikan constructor Enrollment yang lengkap bekerja dengan benar.
    @Test
    @DisplayName("Enrollment: ctor penuh mengisi field dengan benar")
    void enrollment_fullConstructor_populatesFields() {
        LocalDateTime t = LocalDateTime.of(2024, 9, 1, 8, 0, 0);
        Enrollment e = new Enrollment("E777", "S777", "IF202", t, "PENDING");

        assertAll(
                () -> assertEquals("E777", e.getEnrollmentId()),
                () -> assertEquals("S777", e.getStudentId()),
                () -> assertEquals("IF202", e.getCourseCode()),
                () -> assertEquals(t, e.getEnrollmentDate()),
                () -> assertEquals("PENDING", e.getStatus())
        );
    }

    // ===================== CourseGrade =====================
    // Memastikan setter/getter untuk courseCode, credits, dan gradePoint bekerja.
    @Test
    @DisplayName("CourseGrade: ctor kosong + setter/getter semua field")
    void courseGrade_settersAndGetters_work() {
        CourseGrade g = new CourseGrade();
        g.setCourseCode("IF303");
        g.setCredits(3);
        g.setGradePoint(4.0);

        assertAll(
                () -> assertEquals("IF303", g.getCourseCode()),
                () -> assertEquals(3, g.getCredits()),
                () -> assertEquals(4.0, g.getGradePoint(), 1e-9)
        );
    }
    // Memverifikasi constructor CourseGrade langsung mengisi nilai yang sesuai tanpa setter.
    @Test
    @DisplayName("CourseGrade: ctor penuh mengisi field dengan benar")
    void courseGrade_fullConstructor_populatesFields() {
        CourseGrade g = new CourseGrade("IF404", 2, 3.5);

        assertAll(
                () -> assertEquals("IF404", g.getCourseCode()),
                () -> assertEquals(2, g.getCredits()),
                () -> assertEquals(3.5, g.getGradePoint(), 1e-9)
        );
    }

    // ===================== Course =====================
    // memastikan constructor penuh Course menginisialisasi semua atribut dengan benar
    @Test
    @DisplayName("Course: ctor penuh mengisi semua field dengan benar")
    void course_fullConstructor_populatesFields() {
        Course c = new Course("IF123", "Sistem Operasi", 3, 40, 12, "Dr. Andi");
        assertAll(
                () -> assertEquals("IF123", c.getCourseCode()),
                () -> assertEquals("Sistem Operasi", c.getCourseName()),
                () -> assertEquals(3, c.getCredits()),
                () -> assertEquals(40, c.getCapacity()),
                () -> assertEquals(12, c.getEnrolledCount()),
                () -> assertEquals("Dr. Andi", c.getLecturer()),
                () -> assertNotNull(c.getPrerequisites()) // ctor penuh juga inisialisasi list
        );
    }
    // uji lengkap semua setter/getter di Course
    @Test
    @DisplayName("Course: semua getter/setter bekerja (credits, lecturer, dsb.)")
    void course_allSettersAndGetters_work() {
        Course c = new Course();
        c.setCourseCode("IF101");
        c.setCourseName("Pemrograman Dasar");
        c.setCredits(4);
        c.setCapacity(50);
        c.setEnrolledCount(25);
        c.setLecturer("Bu Sari");

        assertAll(
                () -> assertEquals("IF101", c.getCourseCode()),
                () -> assertEquals("Pemrograman Dasar", c.getCourseName()),
                () -> assertEquals(4, c.getCredits()),
                () -> assertEquals(50, c.getCapacity()),
                () -> assertEquals(25, c.getEnrolledCount()),
                () -> assertEquals("Bu Sari", c.getLecturer())
        );
    }

    /**
     * Menguji cabang if (prerequisites == null) di addPrerequisite()
     * supaya branch coverage 100%.
     */
    // menguji cabang kondisi saat prerequisites == null di method addPrerequisite()
    @Test
    @DisplayName("Course: addPrerequisite() menangani prerequisites==null (trigger cabang if)")
    void course_addPrerequisite_handlesNullBranch() {
        Course c = new Course();
        // bikin null dulu agar melewati cabang if di addPrerequisite()
        c.setPrerequisites(null);
        c.addPrerequisite("IF000");

        assertNotNull(c.getPrerequisites(), "List harus diinisialisasi saat null");
        assertEquals(1, c.getPrerequisites().size());
        assertEquals("IF000", c.getPrerequisites().get(0));
    }
    // menguji jalur normal addPrerequisite() saat list sudah ada
    @Test
    @DisplayName("Course: addPrerequisite() menambah item saat list sudah ada")
    void course_addPrerequisite_initializesListAndAdds() {
        Course c = new Course(); // ctor default sudah inisialisasi list kosong
        c.addPrerequisite("IF001");
        c.addPrerequisite("IF002");

        assertNotNull(c.getPrerequisites(), "prerequisites harus terinisialisasi");
        assertEquals(2, c.getPrerequisites().size());
        assertTrue(c.getPrerequisites().containsAll(Arrays.asList("IF001", "IF002")));
    }
    // memastikan setPrerequisites() dan getPrerequisites() berfungsi simetris
    @Test
    @DisplayName("Course: setPrerequisites() menyetel list; getPrerequisites() mengembalikan referensi yang sama")
    void course_setAndGetPrerequisites_roundTrip() {
        Course c = new Course();
        var list = new ArrayList<String>();
        list.add("IF101");
        list.add("IF102");

        c.setPrerequisites(list);

        assertSame(list, c.getPrerequisites(), "Harus mengembalikan referensi list yang diset");
        assertEquals(2, c.getPrerequisites().size());
        assertEquals("IF101", c.getPrerequisites().get(0));
        assertEquals("IF102", c.getPrerequisites().get(1));
    }
}
