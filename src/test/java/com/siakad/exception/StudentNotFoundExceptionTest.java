package com.siakad.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentNotFoundExceptionTest {

    @Test
    void shouldExtendRuntimeException() {
        StudentNotFoundException ex = new StudentNotFoundException("not found");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void messageOnlyConstructorStoresMessage() {
        String msg = "Mahasiswa tidak ditemukan";
        StudentNotFoundException ex = new StudentNotFoundException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void messageAndCauseConstructorStoresBoth() {
        String msg = "Mahasiswa tidak ditemukan";
        Throwable cause = new NullPointerException("npe");
        StudentNotFoundException ex = new StudentNotFoundException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
