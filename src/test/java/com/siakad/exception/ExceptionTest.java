package com.siakad.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    // ===== CourseFullException =====
    @Test
    @DisplayName("CourseFullException - ctor & thrown")
    void testCourseFullException() {
        CourseFullException ex1 = new CourseFullException("kelas penuh");
        assertEquals("kelas penuh", ex1.getMessage());

        Throwable cause = new IllegalStateException("kapasitas habis");
        CourseFullException ex2 = new CourseFullException("gagal daftar", cause);
        assertEquals("gagal daftar", ex2.getMessage());
        assertSame(cause, ex2.getCause());

        CourseFullException thrown = assertThrows(
                CourseFullException.class,
                () -> { throw new CourseFullException("penuh"); }
        );
        assertEquals("penuh", thrown.getMessage());
    }

    // ===== CourseNotFoundException =====
    @Test
    @DisplayName("CourseNotFoundException - ctor & thrown")
    void testCourseNotFoundException() {
        CourseNotFoundException ex1 = new CourseNotFoundException("MK tidak ditemukan");
        assertEquals("MK tidak ditemukan", ex1.getMessage());

        Throwable cause = new NullPointerException("null id");
        CourseNotFoundException ex2 = new CourseNotFoundException("not found", cause);
        assertEquals("not found", ex2.getMessage());
        assertSame(cause, ex2.getCause());

        CourseNotFoundException thrown = assertThrows(
                CourseNotFoundException.class,
                () -> { throw new CourseNotFoundException("x"); }
        );
        assertEquals("x", thrown.getMessage());
    }

    // ===== EnrollmentException =====
    @Test
    @DisplayName("EnrollmentException - ctor & thrown")
    void testEnrollmentException() {
        EnrollmentException ex1 = new EnrollmentException("enroll gagal");
        assertEquals("enroll gagal", ex1.getMessage());

        Throwable cause = new IllegalArgumentException("arg");
        EnrollmentException ex2 = new EnrollmentException("error", cause);
        assertEquals("error", ex2.getMessage());
        assertSame(cause, ex2.getCause());

        EnrollmentException thrown = assertThrows(
                EnrollmentException.class,
                () -> { throw new EnrollmentException("boom"); }
        );
        assertEquals("boom", thrown.getMessage());
    }

    // ===== PrerequisiteNotMetException =====
    @Test
    @DisplayName("PrerequisiteNotMetException - ctor & thrown")
    void testPrerequisiteNotMetException() {
        PrerequisiteNotMetException ex1 = new PrerequisiteNotMetException("prasyarat belum terpenuhi");
        assertEquals("prasyarat belum terpenuhi", ex1.getMessage());

        Throwable cause = new UnsupportedOperationException("no prereq");
        PrerequisiteNotMetException ex2 = new PrerequisiteNotMetException("gagal", cause);
        assertEquals("gagal", ex2.getMessage());
        assertSame(cause, ex2.getCause());

        PrerequisiteNotMetException thrown = assertThrows(
                PrerequisiteNotMetException.class,
                () -> { throw new PrerequisiteNotMetException("e"); }
        );
        assertEquals("e", thrown.getMessage());
    }

    // ===== StudentNotFoundException =====
    @Test
    @DisplayName("StudentNotFoundException - ctor & thrown")
    void testStudentNotFoundException() {
        StudentNotFoundException ex1 = new StudentNotFoundException("mhs tidak ditemukan");
        assertEquals("mhs tidak ditemukan", ex1.getMessage());

        Throwable cause = new IndexOutOfBoundsException("idx");
        StudentNotFoundException ex2 = new StudentNotFoundException("not found", cause);
        assertEquals("not found", ex2.getMessage());
        assertSame(cause, ex2.getCause());

        StudentNotFoundException thrown = assertThrows(
                StudentNotFoundException.class,
                () -> { throw new StudentNotFoundException("z"); }
        );
        assertEquals("z", thrown.getMessage());
    }
}
