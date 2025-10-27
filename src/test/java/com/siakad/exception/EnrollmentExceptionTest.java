package com.siakad.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnrollmentExceptionTest {

    @Test
    void shouldExtendRuntimeException() {
        EnrollmentException ex = new EnrollmentException("enroll err");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void messageOnlyConstructorStoresMessage() {
        String msg = "Kesalahan saat enrollment";
        EnrollmentException ex = new EnrollmentException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void messageAndCauseConstructorStoresBoth() {
        String msg = "Kesalahan saat enrollment";
        Throwable cause = new RuntimeException("root");
        EnrollmentException ex = new EnrollmentException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
