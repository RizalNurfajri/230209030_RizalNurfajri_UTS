package com.siakad.service;

import com.siakad.exception.*;
import com.siakad.model.Course;
import com.siakad.model.Enrollment;
import com.siakad.model.Student;
import com.siakad.repository.CourseRepository;
import com.siakad.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Satu kelas test untuk EnrollmentService:
 * - MOCK  : enrollCourse()
 * - STUB  : validateCreditLimit(), dropCourse()
 */
class EnrollmentServiceTest {

    // =====================  MOCK SECTION  =====================
    // (untuk menguji enrollCourse)
    private StudentRepository studentRepository;     // mock
    private CourseRepository courseRepository;       // mock
    private NotificationService notificationService; // mock
    private GradeCalculator gradeCalculator;         // mock
    private EnrollmentService serviceWithMocks;

    @BeforeEach
    void setUpMocks() {
        studentRepository = mock(StudentRepository.class);
        courseRepository = mock(CourseRepository.class);
        notificationService = mock(NotificationService.class);
        gradeCalculator = mock(GradeCalculator.class);
        serviceWithMocks = new EnrollmentService(studentRepository, courseRepository, notificationService, gradeCalculator);
    }

    // ---------- data dumy ----------
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

    // ---------------- MOCK tests: enrollCourse() ----------------
    //
    @Test
    void enroll_success_updatesCount_and_sendsEmail() {
        Student stu = activeStudent();
        Course course = openCourse();

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(course);
        when(courseRepository.isPrerequisiteMet("S-1", "IF101")).thenReturn(true);

        Enrollment enr = serviceWithMocks.enrollCourse("S-1", "IF101");

        assertNotNull(enr.getEnrollmentId());
        assertEquals("S-1", enr.getStudentId());
        assertEquals("IF101", enr.getCourseCode());
        assertEquals("APPROVED", enr.getStatus());
        assertEquals(11, course.getEnrolledCount());

        verify(courseRepository).update(course);
        verify(notificationService).sendEmail(eq("s1@example.com"),
                eq("Enrollment Confirmation"),
                contains("Pemrograman Dasar"));
    }
    // error jika mahasiswa tidak di temukan
    @Test
    void enroll_studentNotFound_throws_and_noEmail() {
        when(studentRepository.findById("S-404")).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> serviceWithMocks.enrollCourse("S-404", "IF101"));

        verifyNoInteractions(notificationService);
    }
    // Mengetes kondisi status studen aktif atau tidak
    @Test
    void enroll_studentSuspended_throws_and_noEmail() {
        Student s = activeStudent();
        s.setAcademicStatus("SUSPENDED");
        when(studentRepository.findById("S-1")).thenReturn(s);

        assertThrows(EnrollmentException.class,
                () -> serviceWithMocks.enrollCourse("S-1", "IF101"));

        verifyNoInteractions(notificationService);
    }
    // Mengecek kode mata kuliah tidak di temukan
    @Test
    void enroll_courseNotFound_throws_and_noEmail() {
        when(studentRepository.findById("S-1")).thenReturn(activeStudent());
        when(courseRepository.findByCourseCode("XX")).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> serviceWithMocks.enrollCourse("S-1", "XX"));

        verifyNoInteractions(notificationService);
    }
    // Mengetes jika kelas sudah penuh
    @Test
    void enroll_courseFull_throws_and_noEmail() {
        Student stu = activeStudent();
        Course full = openCourse();
        full.setCapacity(10);
        full.setEnrolledCount(10);

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(full);

        assertThrows(CourseFullException.class,
                () -> serviceWithMocks.enrollCourse("S-1", "IF101"));

        verifyNoInteractions(notificationService);
    }
    // Mengetes kalau mahasiswa belum memenuhi syarat
    @Test
    void enroll_prereqNotMet_throws_and_noEmail() {
        Student stu = activeStudent();
        Course course = openCourse();

        when(studentRepository.findById("S-1")).thenReturn(stu);
        when(courseRepository.findByCourseCode("IF101")).thenReturn(course);
        when(courseRepository.isPrerequisiteMet("S-1", "IF101")).thenReturn(false);

        assertThrows(PrerequisiteNotMetException.class,
                () -> serviceWithMocks.enrollCourse("S-1", "IF101"));

        verifyNoInteractions(notificationService);
    }

    // =====================  STUB SECTION  =====================
    // (untuk menguji validateCreditLimit() & dropCourse())

    static class StubStudentRepo implements StudentRepository {
        Student stored;
        StubStudentRepo(Student s) { this.stored = s; }

        @Override
        public Student findById(String studentId) {
            return stored != null && studentId.equals(stored.getStudentId()) ? stored : null;
        }

        @Override
        public void update(Student student) { /* no-op */ }

        @Override
        public List<com.siakad.model.Course> getCompletedCourses(String studentId) { return List.of(); }
    }

    static class StubCourseRepo implements CourseRepository {
        Course stored;
        boolean prereqMet;
        boolean updatedFlag = false;

        StubCourseRepo(Course c, boolean prereqMet) {
            this.stored = c;
            this.prereqMet = prereqMet;
        }

        @Override
        public Course findByCourseCode(String courseCode) {
            return stored != null && courseCode.equals(stored.getCourseCode()) ? stored : null;
        }

        @Override
        public boolean isPrerequisiteMet(String studentId, String courseCode) { return prereqMet; }

        @Override
        public void update(Course course) { updatedFlag = true; }
    }

    static class StubNotificationService implements NotificationService {
        boolean sent;
        String lastSubject;

        @Override
        public void sendEmail(String email, String subject, String message) {
            sent = true;
            lastSubject = subject;
        }

        @Override
        public void sendSMS(String phone, String message) {
        }
    }

    // GradeCalculator menggunakan CLASS, jadi pakai extends
    static class StubGradeCalculator extends GradeCalculator {
        private final int max;
        StubGradeCalculator(int max) { this.max = max; }

        @Override
        public int calculateMaxCredits(double gpa) {
            return max;
        }
    }

    // -------- validateCreditLimit(): STUB --------
    // Mengetes batas SKS masih sesuai
    @Test
    void validateCreditLimit_true_withStub() {
        Student s = activeStudent();
        var service = new EnrollmentService(
                new StubStudentRepo(s),
                new StubCourseRepo(null, true),
                new StubNotificationService(),
                new StubGradeCalculator(24)
        );

        assertTrue(service.validateCreditLimit("S-1", 20));
    }
    // Mengetes jumlah SKS melebihi batas
    @Test
    void validateCreditLimit_false_withStub() {
        Student s = activeStudent();
        var service = new EnrollmentService(
                new StubStudentRepo(s),
                new StubCourseRepo(null, true),
                new StubNotificationService(),
                new StubGradeCalculator(18)
        );

        assertFalse(service.validateCreditLimit("S-1", 19));
    }
    // Mengetes saat mahasiswa tidak ditemukan
    @Test
    void validateCreditLimit_studentNotFound_throws_withStub() {
        var service = new EnrollmentService(
                new StubStudentRepo(null),
                new StubCourseRepo(null, true),
                new StubNotificationService(),
                new StubGradeCalculator(24)
        );

        assertThrows(StudentNotFoundException.class,
                () -> service.validateCreditLimit("S-404", 18));
    }

    // -------- dropCourse(): STUB --------
    // Mengetes dropCourse() jalur sukses
    @Test
    void dropCourse_success_withStub() {
        Student s = activeStudent();
        Course c = openCourse();

        var sRepo = new StubStudentRepo(s);
        var cRepo = new StubCourseRepo(c, true);
        var notif = new StubNotificationService();

        var service = new EnrollmentService(sRepo, cRepo, notif, new StubGradeCalculator(24));
        service.dropCourse("S-1", "IF101");

        assertEquals(9, c.getEnrolledCount(), "enrolledCount harus berkurang 1");
        assertTrue(cRepo.updatedFlag, "repository.update() harus dipanggil");
        assertTrue(notif.sent, "email harus terkirim");
        assertEquals("Course Drop Confirmation", notif.lastSubject);
    }
    // Mengetes ketika mahasiswa tidak ada di repo
    @Test
    void dropCourse_studentNotFound_throws_withStub() {
        Course c = openCourse();

        var service = new EnrollmentService(
                new StubStudentRepo(null),
                new StubCourseRepo(c, true),
                new StubNotificationService(),
                new StubGradeCalculator(24)
        );

        assertThrows(StudentNotFoundException.class,
                () -> service.dropCourse("S-404", "IF101"));
    }
    // Mengetes ketika course tidak ditemukan
    @Test
    void dropCourse_courseNotFound_throws_withStub() {
        Student s = activeStudent();

        var service = new EnrollmentService(
                new StubStudentRepo(s),
                new StubCourseRepo(null, true), // course tidak ada
                new StubNotificationService(),
                new StubGradeCalculator(24)
        );

        assertThrows(CourseNotFoundException.class,
                () -> service.dropCourse("S-1", "ZZ"));
    }
}
