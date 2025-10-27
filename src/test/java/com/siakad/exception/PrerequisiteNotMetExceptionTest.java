package com.siakad.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrerequisiteNotMetExceptionTest {

    @Test
    void shouldExtendRuntimeException() {
        PrerequisiteNotMetException ex = new PrerequisiteNotMetException("prasyarat");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    void messageOnlyConstructorStoresMessage() {
        String msg = "Prasyarat tidak terpenuhi";
        PrerequisiteNotMetException ex = new PrerequisiteNotMetException(msg);
        assertEquals(msg, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    void messageAndCauseConstructorStoresBoth() {
        String msg = "Prasyarat tidak terpenuhi";
        Throwable cause = new UnsupportedOperationException("op");
        PrerequisiteNotMetException ex = new PrerequisiteNotMetException(msg, cause);
        assertEquals(msg, ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}
