package g56583.esi.atl.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeTest {

    @Test
    void validCodeShouldPass() {
        Code code = new Code("123");
        assertEquals("123", code.getCode());
    }

    @Test
    void codeWithInvalidCharactersShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Code("128"));
        assertThrows(IllegalArgumentException.class, () -> new Code("abc"));
        assertThrows(IllegalArgumentException.class, () -> new Code("12"));
    }

    @Test
    void nullCodeShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Code(null));
    }

    @Test
    void codeWithTooFewOrTooManyDigitsShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Code("12"));
        assertThrows(IllegalArgumentException.class, () -> new Code("1234"));
    }
}