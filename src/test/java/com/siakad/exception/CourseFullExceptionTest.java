package com.siakad.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseFullExceptionTest {

    @Test
    void shouldExtendRuntimeException() {
        CourseFullException ex = new CourseFullException("full");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void messageOnlyConstructorStoresMessage() {
        String msg = "Kelas sudah penuh";
        CourseFullException ex = new CourseFullException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void messageAndCauseConstructorStoresBoth() {
        String msg = "Kelas sudah penuh";
        Throwable cause = new IllegalStateException("state");
        CourseFullException ex = new CourseFullException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
