package g56583.esi.atl.model.validator;

import g56583.esi.atl.model.Code;

/**
 * The Validator interface defines the methods that all validators must implement
 * to validate a code against a secret code. It also provides a default method
 * to return a description of the validator.
 */
public interface Validator {
    /**
     * Validates the user's code against the secret code.
     *
     * @param userInputCode The code entered by the user.
     * @param secretCode    The secret code to validate against.
     * @return true if the validation passes, false otherwise.
     */
    boolean validate(Code userInputCode, Code secretCode);

    /**
     * Returns a description of the validator. By default, it returns "Validateur générique".
     *
     * @return A string describing the validator.
     */
    default String getDescription() {
        return "Validateur générique";
    }
}
