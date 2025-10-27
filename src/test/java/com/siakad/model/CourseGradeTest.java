package com.siakad.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseGradeTest {

    @Test
    void allArgsConstructorSetsFields() {
        CourseGrade g = new CourseGrade("IF101", 3, 4.0);

        assertEquals("IF101", g.getCourseCode());
        assertEquals(3, g.getCredits());
        assertEquals(4.0, g.getGradePoint());
    }

    @Test
    void gettersAndSettersWork() {
        CourseGrade g = new CourseGrade();
        g.setCourseCode("IF202");
        g.setCredits(2);
        g.setGradePoint(3.5);

        assertEquals("IF202", g.getCourseCode());
        assertEquals(2, g.getCredits());
        assertEquals(3.5, g.getGradePoint());
    }
}
