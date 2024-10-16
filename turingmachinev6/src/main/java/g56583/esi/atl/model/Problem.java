package g56583.esi.atl.model;


import g56583.esi.atl.model.validator.GenericValidator;
import g56583.esi.atl.model.validator.Validator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a problem in the game, consisting of a secret code, a set of validators, and difficulty and luck parameters.
 */
public class Problem {
    private final Code secretCode;
    private final List<Validator> validators;
    private final int difficulty;
    private final int luck;

    /**
     * Constructs a new Problem instance with a secret code, a list of validators, and difficulty and luck parameters.
     *
     * @param secretCode The secret code for this problem.
     * @param validators A list of validators to validate guesses against the secret code.
     * @param difficulty The difficulty level of the problem.
     * @param luck The luck factor associated with the problem.
     */
    public Problem(Code secretCode, List<Validator> validators, int difficulty, int luck) {
        this.secretCode = secretCode;
        this.validators = validators;
        this.difficulty = difficulty;
        this.luck = luck;
    }

    /**
     * Gets the secret code of this problem.
     *
     * @return The secret code.
     */
    public Code getCode() {
        return secretCode;
    }

    public boolean validateWithValidator(int validatorNo, Code inputCode) {
        GenericValidator validator = (GenericValidator) validators.stream()
                .filter(v -> v instanceof GenericValidator && ((GenericValidator) v).getValidatorNo() == validatorNo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Validator number " + validatorNo + " is not available."));
        return validator.validate(inputCode, secretCode);
    }
    /**
     * Gets the list of validators associated with this problem.
     *
     * @return A list of validators.
     */
    public List<Validator> getValidators() {
        return validators;
    }

    /**
     * Returns a string representation of this problem, including its difficulty, luck, and validators.
     *
     * @return A string representation of the problem.
     */
    @Override
    public String toString() {
        String validatorDescriptions = validators.stream()
                .map(Validator::getDescription)
                .collect(Collectors.joining(", "));

        return String.format("Problem { difficulté=%d, chance=%d, validateurs=[%s] }", difficulty, luck, validatorDescriptions);
    }

    public boolean isCorrectGuess(Code guessedCode) {
        return guessedCode.equals(secretCode);
    }
}
