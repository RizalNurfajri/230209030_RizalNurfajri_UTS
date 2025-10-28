package com.siakad.service;

import com.siakad.model.CourseGrade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {
    // Mengetes perhitungan IPK
    @Test
    @DisplayName("calculateGPA: hitung IPK benar + empty case + input invalid")
    void calculateGPA_cases() {
        GradeCalculator calc = new GradeCalculator();

        // (4×3 + 3×2 + 2×1) / (3+2+1) = (12+6+2)/6 = 20/6 = 3.333... ≈ 3.33
        var grades = List.of(
                new CourseGrade("IF101", 3, 4.0),
                new CourseGrade("IF102", 2, 3.0),
                new CourseGrade("IF103", 1, 2.0)
        );
        assertEquals(3.33, calc.calculateGPA(grades), 1e-9);

        // daftar kosong → 0.0
        assertEquals(0.0, calc.calculateGPA(List.of()), 1e-9);

        // gradePoint di luar rentang (mis. 5.0) → IllegalArgumentException
        var invalid = List.of(new CourseGrade("XX", 2, 5.0));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateGPA(invalid));
    }
    // Mengetes status akademik berdasarkan semester & IPK
    @Test
    @DisplayName("determineAcademicStatus: aturan semester 1-2 / 3-4 / 5+ & input invalid")
    void determineAcademicStatus_rules() {
        GradeCalculator calc = new GradeCalculator();

        // Semester 1-2
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.0, 1));
        assertEquals("PROBATION", calc.determineAcademicStatus(1.99, 2));

        // Semester 3-4
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.25, 3));
        assertEquals("PROBATION", calc.determineAcademicStatus(2.10, 4));
        assertEquals("SUSPENDED", calc.determineAcademicStatus(1.90, 3));

        // Semester 5+
        assertEquals("ACTIVE", calc.determineAcademicStatus(2.5, 6));
        assertEquals("PROBATION", calc.determineAcademicStatus(2.2, 7));
        assertEquals("SUSPENDED", calc.determineAcademicStatus(1.5, 5));

        // input invalid
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(-0.1, 1));
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(4.1, 1));
        assertThrows(IllegalArgumentException.class, () -> calc.determineAcademicStatus(3.0, 0));
    }
    // Mengetes aturan batas maksimum SKS per IPK
    @Test
    @DisplayName("calculateMaxCredits: mapping batas SKS dari IPK & input invalid")
    void calculateMaxCredits_rules() {
        GradeCalculator calc = new GradeCalculator();

        assertEquals(24, calc.calculateMaxCredits(3.0)); // IPK ≥ 3.0
        assertEquals(21, calc.calculateMaxCredits(2.5)); // 2.5–2.99
        assertEquals(18, calc.calculateMaxCredits(2.1)); // 2.0–2.49
        assertEquals(15, calc.calculateMaxCredits(1.99)); // < 2.0

        assertThrows(IllegalArgumentException.class, () -> calc.calculateMaxCredits(-0.1));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateMaxCredits(4.1));
    }
    // Memastikan method tidak error ketika parameter grades = null.
    @Test
    @DisplayName("calculateGPA: daftar NULL → 0.0")
    void calculateGPA_nullList_returnsZero() {
        GradeCalculator calc = new GradeCalculator();
        assertEquals(0.0, calc.calculateGPA(null), 1e-9);
    }
    // Memastikan input nilai (gradePoint) negatif ditolak oleh sistem
    @Test
    @DisplayName("calculateGPA: gradePoint negatif → IllegalArgumentException")
    void calculateGPA_negativeGradePoint_throws() {
        GradeCalculator calc = new GradeCalculator();
        var invalidNeg = List.of(new CourseGrade("NEG", 3, -0.1));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateGPA(invalidNeg));
    }
    // Menguji kasus di mana total SKS = 0, jadi hasil IPK seharusnya 0.0.
    @Test
    @DisplayName("calculateGPA: semua credits = 0 → total SKS 0 → 0.0")
    void calculateGPA_allZeroCredits_returnsZero() {
        GradeCalculator calc = new GradeCalculator();
        var grades = List.of(
                new CourseGrade("Z1", 0, 4.0),
                new CourseGrade("Z2", 0, 3.0)
        );
        assertEquals(0.0, calc.calculateGPA(grades), 1e-9);
    }

}
