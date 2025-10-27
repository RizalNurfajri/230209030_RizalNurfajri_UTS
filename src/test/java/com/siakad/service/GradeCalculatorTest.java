package com.siakad.service;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    private final GradeCalculator calc = new GradeCalculator();

    @Test
    void calculateGPA_returnsZero_onEmptyOrNull() {
        assertEquals(0.0, calc.calculateGPA(null));
        assertEquals(0.0, calc.calculateGPA(List.of()));
    }

    @Test
    void calculateGPA_weightedAverage_rounded2Decimals() {
        // (3 sks * 4.0) + (2 sks * 3.0) + (1 sks * 3.7) = 12 + 6 + 3.7 = 21.7
        // total sks = 6 ⇒ 21.7/6 = 3.6166.. ⇒ 3.62
        List<CourseGrade> grades = List.of(
                new CourseGrade("IF101", 3, 4.0),
                new CourseGrade("IF102", 2, 3.0),
                new CourseGrade("IF103", 1, 3.7)
        );
        assertEquals(3.62, calc.calculateGPA(grades));
    }

    @Test
    void calculateGPA_throws_onInvalidGradePoint() {
        List<CourseGrade> grades = List.of(new CourseGrade("IF999", 3, 4.5));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateGPA(grades));
    }

    @Test
    void determineAcademicStatus_validRanges() {
        // Semester 1-2
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.0, 1));
        assertEquals("PROBATION", calc.determineAcademicStatus(1.99, 2));

        // Semester 3-4
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.25, 3));
        assertEquals("PROBATION", calc.determineAcademicStatus(2.10, 4));
        assertEquals("SUSPENDED", calc.determineAcademicStatus(1.99, 3));

        // Semester 5+
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.5, 5));
        assertEquals("PROBATION", calc.determineAcademicStatus(2.25, 6));
        assertEquals("SUSPENDED", calc.determineAcademicStatus(1.99, 7));
    }

    @Test
    void determineAcademicStatus_throws_onInvalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(-0.1, 1));
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(4.1, 1));
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(3.0, 0));
    }

    @Test
    void calculateMaxCredits_mappingAndValidation() {
        assertEquals(24, calc.calculateMaxCredits(3.0));
        assertEquals(21, calc.calculateMaxCredits(2.5));
        assertEquals(18, calc.calculateMaxCredits(2.0));
        assertEquals(15, calc.calculateMaxCredits(1.99));

        assertThrows(IllegalArgumentException.class, () -> calc.calculateMaxCredits(-0.1));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateMaxCredits(4.1));
    }

    @Test
    void calculateGPA_returnsZero_whenAllCoursesHaveZeroCredits() {
        // Semua credits = 0 → totalCredits = 0 → harus return 0.0
        List<CourseGrade> grades = List.of(
                new CourseGrade("IF001", 0, 4.0),
                new CourseGrade("IF002", 0, 3.5)
        );
        double result = calc.calculateGPA(grades);
        assertEquals(0.0, result);
    }

    @Test
    void calculateGPA_throws_onNegativeGradePoint_tooLow() {
        List<CourseGrade> grades = List.of(
                new CourseGrade("IF001", 3, -0.1)  // sisi kiri dari (gp < 0 || gp > 4.0)
        );
        assertThrows(IllegalArgumentException.class, () -> calc.calculateGPA(grades));
    }
}
