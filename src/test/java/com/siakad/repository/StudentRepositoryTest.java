package com.siakad.repository;

import com.siakad.model.Course;
import com.siakad.model.Student;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    @Test
    void typeIsInterface() {
        assertTrue(StudentRepository.class.isInterface(), "StudentRepository harus interface");
    }

    @Test
    void hasFindByIdWithCorrectSignature() throws Exception {
        Method m = StudentRepository.class.getMethod("findById", String.class);
        assertEquals(Student.class, m.getReturnType(), "findById harus mengembalikan Student");
    }

    @Test
    void hasUpdateWithCorrectSignature() throws Exception {
        Method m = StudentRepository.class.getMethod("update", Student.class);
        assertEquals(void.class, m.getReturnType(), "update harus void");
    }

    @Test
    void hasGetCompletedCoursesWithCorrectSignature() throws Exception {
        Method m = StudentRepository.class.getMethod("getCompletedCourses", String.class);
        assertEquals(List.class, m.getReturnType(), "getCompletedCourses harus List<Course>");
        // Tidak bisa cek generic <Course> via refleksi runtime, tapi setidaknya tipe List terverifikasi.
    }
}
