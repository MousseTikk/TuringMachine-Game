package g56583.esi.atl.model;

import g56583.esi.atl.model.validator.GenericValidator;
import g56583.esi.atl.model.validator.Validator;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProblemTest {

    @Test
    void problemShouldReturnCorrectCode() {
        Code code = new Code("123");
        Problem problem = new Problem(code, List.of(), 1, 1);
        assertEquals("123", problem.getCode().getCode());
    }

    @Test
    void problemShouldCorrectlyValidateWithValidator() {
        Code secretCode = new Code("123");
        Code inputCode = new Code("123");
        Validator validator = new GenericValidator(1, "Test Validator", (userInputCode, secretCode1) -> true);
        Problem problem = new Problem(secretCode, List.of(validator), 1, 1);
        assertTrue(problem.validateWithValidator(1, inputCode));
    }

    @Test
    void problemShouldThrowExceptionForInvalidValidator() {
        Code secretCode = new Code("123");
        Code inputCode = new Code("123");
        Problem problem = new Problem(secretCode, List.of(), 1, 1);
        assertThrows(IllegalArgumentException.class, () -> problem.validateWithValidator(1, inputCode));
    }

    @Test
    void problemToStringShouldReturnCorrectFormat() {
        Code code = new Code("123");
        Validator validator = new GenericValidator(1, "Test Validator", (userInputCode, secretCode) -> true);
        Problem problem = new Problem(code, List.of(validator), 3, 2);
        String expected = "Problem { difficulté=3, chance=2, validateurs=[Test Validator] }";
        assertEquals(expected, problem.toString());
    }
}
