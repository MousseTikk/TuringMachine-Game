package g56583.esi.atl.model.validator;

import g56583.esi.atl.model.Code;

/**
 * The GenericValidator class is a concrete implementation of the Validator interface.
 * It represents a validator that can validate a code against specific criteria.
 * The validator is identified by a unique number and has a description.
 */
public class GenericValidator implements Validator {
    private final int validatorNo;
    private final String description;
    private final Validator validator;

    /**
     * Constructs a GenericValidator with a specific number, description, and validation logic.
     *
     * @param validatorNo  The unique number identifying the validator.
     * @param description  A description of the validation logic.
     * @param validator    The validation logic implemented by another Validator instance.
     */
    public GenericValidator(int validatorNo, String description, Validator validator) {
        this.validatorNo = validatorNo;
        this.description = description;
        this.validator = validator;
    }

    /**
     * Validates the user's code against the secret code using the validation logic.
     *
     * @param userInputCode The code entered by the user.
     * @param secretCode    The secret code to validate against.
     * @return true if the validation passes, false otherwise.
     */
    @Override
    public boolean validate(Code userInputCode, Code secretCode) {
        return validator.validate(userInputCode, secretCode);
    }

    /**
     * Returns the description of the validator.
     *
     * @return A string describing the validation logic.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns the unique number identifying the validator.
     *
     * @return The validator number.
     */
    public int getValidatorNo() {
        return validatorNo;
    }
}
