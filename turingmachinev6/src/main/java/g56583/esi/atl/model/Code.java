package g56583.esi.atl.model;

/**
 * A record representing a code with specific validation rules.
 * The code must consist of exactly three digits, each between 1 and 5.
 */
public record Code(String code) {
    /**
     * Constructs a new Code instance.
     *
     * @throws IllegalArgumentException if the provided code does not meet the required format.
     */
    public Code {
        if (code==null || !isValidCode(code)) {
            throw new IllegalArgumentException("Invalid code: must be 3 digits, each between 1 and 5");
        }
    }
    /**
     * Checks if the provided code string meets the specific format requirements.
     *
     * @param code The string representation of the code to be validated.
     * @return true if the code matches the required format; false otherwise.
     */
    private boolean isValidCode(String code) {
        return code.matches("[1-5]{3}");
    }
    /**
     * Retrieves the code string.
     *
     * @return The string representation of the code.
     */
    public String getCode() {
        return code;
    }
    @Override
    public String toString(){
        return code;
    }

}