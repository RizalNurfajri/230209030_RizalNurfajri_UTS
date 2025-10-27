package com.siakad.service;

import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private NotificationService notificationService;
    private GradeCalculator gradeCalculator;
    private EnrollmentService service;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        courseRepository = mock(CourseRepository.class);
        notificationService = mock(NotificationService.class);
        gradeCalculator = mock(GradeCalculator.class);

        service = new EnrollmentService(studentRepository, courseRepository, notificationService, gradeCalculator);
    }

    private Student activeStudent() {
        Student s = new Student();
        s.setStudentId("S-1");
        s.setEmail("s1@example.com");
        s.setAcademicStatus("ACTIVE");
        s.setGpa(3.2);
        return s;
    }

    private Course openCourse() {
        Course c = new Course();
        c.setCourseCode("IF101");
        c.setCourseName("Pemrograman Dasar");
        c.setCapacity(30);
        c.setEnrolledCount(10);
        return c;
    }

    @Test
    void enrollCourse_success_updatesCount_sendsEmail_returnsEnrollment() {
        // given
        Student stu = activeStudent();
        Course course = openCourse();

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(course);
        when(courseRepository.isPrerequisiteMet("S-1", "IF101")).thenReturn(true);

        // when
        Enrollment enr = service.enrollCourse("S-1", "IF101");

        // then
        assertNotNull(enr.getEnrollmentId());
        assertTrue(enr.getEnrollmentId().startsWith("ENR-"));
        assertEquals("S-1", enr.getStudentId());
        assertEquals("IF101", enr.getCourseCode());
        assertEquals("APPROVED", enr.getStatus());
        assertNotNull(enr.getEnrollmentDate());

        // enrolledCount bertambah & repo di-update
        assertEquals(11, course.getEnrolledCount());
        verify(courseRepository).update(course);

        // email terkirim dengan subject yang benar
        verify(notificationService).sendEmail(eq("s1@example.com"),
                eq("Enrollment Confirmation"),
                contains("Pemrograman Dasar"));
    }

    @Test
    void enrollCourse_studentNotFound_throws() {
        when(studentRepository.findById("S-404")).thenReturn(null);
        assertThrows(StudentNotFoundException.class,
                () -> service.enrollCourse("S-404", "IF101"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void enrollCourse_studentSuspended_throwsEnrollmentException() {
        Student s = activeStudent();
        s.setAcademicStatus("SUSPENDED");
        when(studentRepository.findById("S-1")).thenReturn(s);
        assertThrows(EnrollmentException.class,
                () -> service.enrollCourse("S-1", "IF101"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void enrollCourse_courseNotFound_throws() {
        when(studentRepository.findById("S-1")).thenReturn(activeStudent());
        when(courseRepository.findByCourseCode("XX")).thenReturn(null);
        assertThrows(CourseNotFoundException.class,
                () -> service.enrollCourse("S-1", "XX"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void enrollCourse_courseFull_throws() {
        Student stu = activeStudent();
        Course full = openCourse();
        full.setCapacity(10);
        full.setEnrolledCount(10);

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(full);

        assertThrows(CourseFullException.class,
                () -> service.enrollCourse("S-1", "IF101"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void enrollCourse_prereqNotMet_throws() {
        Student stu = activeStudent();
        Course course = openCourse();

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(course);
        when(courseRepository.isPrerequisiteMet("S-1", "IF101")).thenReturn(false);

        assertThrows(PrerequisiteNotMetException.class,
                () -> service.enrollCourse("S-1", "IF101"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void validateCreditLimit_true_whenWithinLimit() {
        Student stu = activeStudent();
        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(gradeCalculator.calculateMaxCredits(3.2)).thenReturn(24);

        assertTrue(service.validateCreditLimit("S-1", 20));
    }

    @Test
    void validateCreditLimit_false_whenExceedsLimit() {
        Student stu = activeStudent();
        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(gradeCalculator.calculateMaxCredits(3.2)).thenReturn(18);

        assertFalse(service.validateCreditLimit("S-1", 19));
    }

    @Test
    void validateCreditLimit_studentNotFound_throws() {
        when(studentRepository.findById("S-404")).thenReturn(null);
        assertThrows(StudentNotFoundException.class,
                () -> service.validateCreditLimit("S-404", 18));
    }

    @Test
    void dropCourse_success_decrementsCount_updatesRepo_sendsEmail() {
        Student stu = activeStudent();
        Course course = openCourse(); // enrolledCount=10

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(course);

        service.dropCourse("S-1", "IF101");

        assertEquals(9, course.getEnrolledCount());
        verify(courseRepository).update(course);

        verify(notificationService).sendEmail(eq("s1@example.com"),
                eq("Course Drop Confirmation"),
                contains("Pemrograman Dasar"));
    }

    @Test
    void dropCourse_studentNotFound_throws() {
        when(studentRepository.findById("S-404")).thenReturn(null);
        assertThrows(StudentNotFoundException.class,
                () -> service.dropCourse("S-404", "IF101"));
        verifyNoInteractions(notificationService);
    }

    @Test
    void dropCourse_courseNotFound_throws() {
        when(studentRepository.findById("S-1")).thenReturn(activeStudent());
        when(courseRepository.findByCourseCode("ZZ")).thenReturn(null);
        assertThrows(CourseNotFoundException.class,
                () -> service.dropCourse("S-1", "ZZ"));
        verifyNoInteractions(notificationService);
    }
}
