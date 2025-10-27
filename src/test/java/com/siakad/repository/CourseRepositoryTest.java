package com.siakad.repository;

import com.siakad.model.Course;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CourseRepositoryTest {

    @Test
    void typeIsInterface() {
        assertTrue(CourseRepository.class.isInterface(), "CourseRepository harus interface");
    }

    @Test
    void hasUpdateWithCorrectSignature() throws Exception {
        Method m = CourseRepository.class.getMethod("update", Course.class);
        assertEquals(void.class, m.getReturnType(), "update harus void");
    }

    @Test
    void hasIsPrerequisiteMetWithCorrectSignature() throws Exception {
        Method m = CourseRepository.class.getMethod("isPrerequisiteMet", String.class, String.class);
        assertEquals(boolean.class, m.getReturnType(), "isPrerequisiteMet harus boolean");
    }

    @Test
    void hasSomeFindMethodByCodeReturningCourse() {
        // Di file kamu ada Javadoc "Mencari mata kuliah berdasarkan course code"
        // tapi nama metodenya tidak terlihat (baris terpotong).
        // Tes ini fleksibel: menerima metode apa pun yang:
        // - bernama diawali "find"
        // - return type Course
        // - punya 1 parameter String (courseCode)
        boolean exists = Arrays.stream(CourseRepository.class.getMethods())
                .anyMatch(m ->
                        m.getName().startsWith("find")
                                && m.getReturnType() == Course.class
                                && m.getParameterCount() == 1
                                && m.getParameterTypes()[0] == String.class
                );
        assertTrue(exists, "Harus ada metode pencarian (mis. findByCourseCode(String)) yang mengembalikan Course");
    }
}
