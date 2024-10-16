package g56583.esi.atl.model.validator;

import g56583.esi.atl.model.Code;

/**
 * The ValidatorFactory class provides a factory method for creating
 * different types of validators based on a specified validator number.
 */
public class ValidatorFactory {
    /**
     * Returns a Validator instance based on the specified validator number.
     *
     * @param validatorNo The number identifying the type of validator to create.
     * @return A Validator instance corresponding to the specified number.
     * @throws IllegalArgumentException if the validator number is invalid.
     */
    public static Validator getValidator(int validatorNo) {
        return switch (validatorNo) {
            case 1 -> new GenericValidator(1, "Compare the first digit with 1",
                    (userInputCode, secretCode) -> compareValue(userInputCode, secretCode, 0, 1));
            case 2 -> new GenericValidator(2, "Compare the first digit with 3",
                    (userInputCode, secretCode) -> compareValue(userInputCode, secretCode, 0, 3));
            case 3 -> new GenericValidator(3, "Compare the second digit with 3",
                    (userInputCode, secretCode) -> compareValue(userInputCode, secretCode, 1, 3));
            case 4 -> new GenericValidator(4, "Compare the second digit with 4",
                    (userInputCode, secretCode) -> compareValue(userInputCode, secretCode, 1, 4));
            case 5 -> new GenericValidator(5, "Check the parity of the first digit",
                    (userInputCode, secretCode) -> checkParity(userInputCode, secretCode, 0));
            case 6 -> new GenericValidator(6, "Check the parity of the second digit",
                    (userInputCode, secretCode) -> checkParity(userInputCode, secretCode, 1));
            case 7 -> new GenericValidator(7, "Check the parity of the third digit",
                    (userInputCode, secretCode) -> checkParity(userInputCode, secretCode, 2));
            case 8 -> new GenericValidator(8, "Count how many times the value 1 appears",
                    (userInputCode, secretCode) -> countValueOccurrences(userInputCode, secretCode, 1));
            case 9 -> new GenericValidator(9, "Count how many times the value 3 appears",
                    (userInputCode, secretCode) -> countValueOccurrences(userInputCode, secretCode, 3));
            case 10 -> new GenericValidator(10, "Count how many times the value 4 appears",
                    (userInputCode, secretCode) -> countValueOccurrences(userInputCode, secretCode, 4));
            case 11 -> new GenericValidator(11, "Compare the digits in positions 1 and 2",
                    (userInputCode, secretCode) -> compareTwoPositions(userInputCode, secretCode, 0, 1));
            case 12 -> new GenericValidator(12, "Compare the digits in positions 1 and 3",
                    (userInputCode, secretCode) -> compareTwoPositions(userInputCode, secretCode, 0, 2));
            case 13 -> new GenericValidator(13, "Compare the digits in positions 2 and 3",
                    (userInputCode, secretCode) -> compareTwoPositions(userInputCode, secretCode, 1, 2));
            case 14 -> new GenericValidator(14, "Determine which digit is the smallest",
                    (userInputCode, secretCode) -> findExtremumDigit(userInputCode, secretCode, true));
            case 15 -> new GenericValidator(15, "Determine which digit is the largest",
                    (userInputCode, secretCode) -> findExtremumDigit(userInputCode, secretCode, false));
            case 16 -> new GenericValidator(16, "Determine the most common parity",
                    ValidatorFactory::findMostCommonParity);
            case 17 -> new GenericValidator(17, "Count how many digits in the code are even",
                    ValidatorFactory::countEvenDigits);
            case 18 -> new GenericValidator(18, "Determine if the sum of the digits is even or odd",
                    ValidatorFactory::sumDigitsParity);
            case 19 -> new GenericValidator(19, "Compare the sum of the first two digits with 6",
                    ValidatorFactory::compareSumOfFirstTwoDigits);
            case 20 -> new GenericValidator(20, "Determine if a digit in the code repeats, and if so, how many times",
                    ValidatorFactory::getMaxRepeatCount);
            case 21 -> new GenericValidator(21, "Determine if a digit appears exactly twice in the code",
                    ValidatorFactory::hasTwinDigit);
            case 22 -> new GenericValidator(22, "Determine if the three digits are in ascending or descending order",
                    ValidatorFactory::getOrderType);
            default -> throw new IllegalArgumentException("Validator not found");
        };
    }

    // Helper methods for the validators

    private static boolean compareValue(Code userInputCode, Code secretCode, int position, int comparisonValue) {
        int userInputDigit = Character.getNumericValue(userInputCode.getCode().charAt(position));
        int secretDigit = Character.getNumericValue(secretCode.getCode().charAt(position));
        return (userInputDigit < comparisonValue && secretDigit < comparisonValue) ||
                (userInputDigit == comparisonValue && secretDigit == comparisonValue) ||
                (userInputDigit > comparisonValue && secretDigit > comparisonValue);
    }

    private static boolean checkParity(Code userInputCode, Code secretCode, int position) {
        int userInputDigit = Character.getNumericValue(userInputCode.getCode().charAt(position));
        int secretDigit = Character.getNumericValue(secretCode.getCode().charAt(position));
        return (userInputDigit % 2 == 0) == (secretDigit % 2 == 0);
    }

    private static boolean countValueOccurrences(Code userInputCode, Code secretCode, int valueToCount) {
        int userInputCount = countOccurrences(userInputCode, valueToCount);
        int secretCount = countOccurrences(secretCode, valueToCount);
        return userInputCount == secretCount;
    }

    private static int countOccurrences(Code code, int value) {
        int count = 0;
        for (char ch : code.getCode().toCharArray()) {
            if (Character.getNumericValue(ch) == value) {
                count++;
            }
        }
        return count;
    }

    private static boolean compareTwoPositions(Code userInputCode, Code secretCode, int pos1, int pos2) {
        String userInputCategory = getCategory(userInputCode, pos1, pos2);
        String secretCodeCategory = getCategory(secretCode, pos1, pos2);
        return userInputCategory.equals(secretCodeCategory);
    }

    private static String getCategory(Code code, int pos1, int pos2) {
        int digit1 = Character.getNumericValue(code.getCode().charAt(pos1));
        int digit2 = Character.getNumericValue(code.getCode().charAt(pos2));
        if (digit1 < digit2) {
            return "Less than";
        } else if (digit1 > digit2) {
            return "Greater than";
        } else {
            return "Equal";
        }
    }

    private static boolean findExtremumDigit(Code userInputCode, Code secretCode, boolean findSmallest) {
        int userInputExtremum = findExtremumPosition(userInputCode, findSmallest);
        int secretCodeExtremum = findExtremumPosition(secretCode, findSmallest);
        return userInputExtremum == secretCodeExtremum;
    }

    private static int findExtremumPosition(Code code, boolean findSmallest) {
        int extremumDigit = Character.getNumericValue(code.getCode().charAt(0));
        int extremumPosition = 0;

        for (int i = 1; i < code.getCode().length(); i++) {
            int currentDigit = Character.getNumericValue(code.getCode().charAt(i));
            if ((findSmallest && currentDigit < extremumDigit) || (!findSmallest && currentDigit > extremumDigit)) {
                extremumDigit = currentDigit;
                extremumPosition = i;
            }
        }

        return extremumPosition;
    }

    private static boolean findMostCommonParity(Code userInputCode, Code secretCode) {
        boolean userInputParity = findMostCommonParity(userInputCode);
        boolean secretCodeParity = findMostCommonParity(secretCode);
        return userInputParity == secretCodeParity;
    }

    private static boolean findMostCommonParity(Code code) {
        int countEven = 0;
        int countOdd = 0;
        for (char ch : code.getCode().toCharArray()) {
            int digit = Character.getNumericValue(ch);
            if (digit % 2 == 0) {
                countEven++;
            } else {
                countOdd++;
            }
        }
        return countEven >= countOdd;
    }

    private static boolean countEvenDigits(Code userInputCode, Code secretCode) {
        int userInputCount = countEvenDigits(userInputCode);
        int secretCodeCount = countEvenDigits(secretCode);
        return userInputCount == secretCodeCount;
    }

    private static int countEvenDigits(Code code) {
        int countEven = 0;
        for (char ch : code.getCode().toCharArray()) {
            int digit = Character.getNumericValue(ch);
            if (digit % 2 == 0) {
                countEven++;
            }
        }
        return countEven;
    }

    private static boolean sumDigitsParity(Code userInputCode, Code secretCode) {
        boolean userInputParity = sumDigitsParity(userInputCode);
        boolean secretCodeParity = sumDigitsParity(secretCode);
        return userInputParity == secretCodeParity;
    }

    private static boolean sumDigitsParity(Code code) {
        int sum = code.getCode().chars()
                .map(Character::getNumericValue)
                .sum();
        return sum % 2 == 0;
    }

    private static boolean compareSumOfFirstTwoDigits(Code userInputCode, Code secretCode) {
        String userInputComparison = compareSumToValue(userInputCode);
        String secretCodeComparison = compareSumToValue(secretCode);
        return userInputComparison.equals(secretCodeComparison);
    }

    private static String compareSumToValue(Code code) {
        int sumOfFirstTwo = Character.getNumericValue(code.getCode().charAt(0))
                + Character.getNumericValue(code.getCode().charAt(1));
        if (sumOfFirstTwo < 6) {
            return "Less than";
        } else if (sumOfFirstTwo > 6) {
            return "Greater than";
        } else {
            return "Equal";
        }
    }

    private static boolean getMaxRepeatCount(Code userInputCode, Code secretCode) {
        int userInputMaxCount = getMaxRepeatCount(userInputCode);
        int secretCodeMaxCount = getMaxRepeatCount(secretCode);
        return userInputMaxCount == secretCodeMaxCount;
    }

    private static int getMaxRepeatCount(Code code) {
        int[] digitCounts = new int[10];
        for (char ch : code.getCode().toCharArray()) {
            digitCounts[Character.getNumericValue(ch)]++;
        }
        int maxCount = 1;
        for (int count : digitCounts) {
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    private static boolean hasTwinDigit(Code userInputCode, Code secretCode) {
        boolean userInputTwin = hasTwinDigit(userInputCode);
        boolean secretCodeTwin = hasTwinDigit(secretCode);
        return userInputTwin == secretCodeTwin;
    }

    private static boolean hasTwinDigit(Code code) {
        int[] digitCounts = new int[10];
        for (char ch : code.getCode().toCharArray()) {
            digitCounts[Character.getNumericValue(ch)]++;
        }
        boolean twinFound = false;
        for (int count : digitCounts) {
            if (count == 2) {
                twinFound = true;
            } else if (count > 2) {
                return false;
            }
        }
        return twinFound;
    }

    private static boolean getOrderType(Code userInputCode, Code secretCode) {
        return getOrderType(userInputCode) == getOrderType(secretCode);
    }

    private static OrderType getOrderType(Code code) {
        int firstDigit = Character.getNumericValue(code.getCode().charAt(0));
        int secondDigit = Character.getNumericValue(code.getCode().charAt(1));
        int thirdDigit = Character.getNumericValue(code.getCode().charAt(2));

        if (firstDigit < secondDigit && secondDigit < thirdDigit) {
            return OrderType.ASCENDING;
        } else if (firstDigit > secondDigit && secondDigit > thirdDigit) {
            return OrderType.DESCENDING;
        } else {
            return OrderType.NONE;
        }
    }

    /**
     * The OrderType enum defines the possible orderings for the digits
     * in the code: ascending, descending, or none.
     */
    private enum OrderType {
        ASCENDING, DESCENDING, NONE
    }
}
