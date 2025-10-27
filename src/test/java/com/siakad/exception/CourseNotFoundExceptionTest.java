package com.siakad.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseNotFoundExceptionTest {

    @Test
    void shouldExtendRuntimeException() {
        CourseNotFoundException ex = new CourseNotFoundException("not found");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void messageOnlyConstructorStoresMessage() {
        String msg = "Mata kuliah tidak ditemukan";
        CourseNotFoundException ex = new CourseNotFoundException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void messageAndCauseConstructorStoresBoth() {
        String msg = "Mata kuliah tidak ditemukan";
        Throwable cause = new IllegalArgumentException("invalid");
        CourseNotFoundException ex = new CourseNotFoundException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
