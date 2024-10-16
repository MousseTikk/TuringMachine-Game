package g56583.esi.atl.model.validator;

import g56583.esi.atl.model.Code;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import g56583.esi.atl.model.validators.ValidatorFactory;
class ValidatorFactoryTest {

    @Test
    void validatorFactoryShouldReturnCorrectValidator() {
        Validator validator = ValidatorFactory.getValidator(1);
        assertEquals(1, ((GenericValidator) validator).getValidatorNo());
        assertEquals("Compare le premier chiffre avec 1", validator.getDescription());
    }

    @Test
    void validatorFactoryShouldThrowExceptionForInvalidNumber() {
        assertThrows(IllegalArgumentException.class, () -> ValidatorFactory.getValidator(100));
    }

    @Test
    void validatorShouldWorkCorrectly() {
        Validator validator = ValidatorFactory.getValidator(1);
        Code inputCode = new Code("123");
        Code secretCode = new Code("123");
        assertTrue(validator.validate(inputCode, secretCode));
    }
}
