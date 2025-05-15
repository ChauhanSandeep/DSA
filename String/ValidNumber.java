package String;

/**
 * Valid Number Checker
 * https://leetcode.com/problems/valid-number/
 * 
 * Given a string, determine if it represents a valid number.
 * 
 * **Valid cases include:**
 * - Integers (e.g., "2", "0089", "-90", "+6")
 * - Decimals (e.g., "-0.1", "3.14", "4.", "-.9")
 * - Scientific notation (e.g., "2e10", "-90E3", "3e+7", "53.5e93")
 * 
 * **Invalid cases include:**
 * - Alphabetic characters mixed with numbers ("abc", "1a", "95a54e53")
 * - Incorrect exponent usage ("1e", "e3", "99e2.5")
 * - Multiple signs or misplaced signs ("--6", "-+3")
 * 
 * **Time Complexity:** O(N) - One pass through the string.
 * **Space Complexity:** O(1) - Constant space used.
 */
public class ValidNumber {

    public static void main(String[] args) {
        String[] validNumbers = {
            "2", "0089", "-0.1", "+3.14", "4.", "-.9", 
            "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"
        };

        String[] invalidNumbers = {
            "abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"
        };

        ValidNumber validator = new ValidNumber();

        System.out.println("Valid Numbers:");
        for (String number : validNumbers) {
            System.out.println(number + " -> " + validator.isValidNumber(number));
        }

        System.out.println("\nInvalid Numbers:");
        for (String number : invalidNumbers) {
            System.out.println(number + " -> " + validator.isValidNumber(number));
        }
    }

    /**
     * Determines if a given string is a valid number.
     *
     * @param numStr The string to validate.
     * @return True if the string is a valid number, otherwise false.
     */
    public boolean isValidNumber(String numStr) {
        boolean hasDigit = false;
        boolean hasDecimal = false;
        boolean hasExponent = false;
        boolean hasSign = false;

        for (int i = 0; i < numStr.length(); i++) {
            char ch = numStr.charAt(i);

            // Check if the character is a digit
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
            // Check if it's a decimal point
            else if (ch == '.') {
                // A decimal cannot appear after an exponent or if already seen
                if (hasDecimal || hasExponent) return false;
                hasDecimal = true;
            }
            // Check if it's an exponent ('e' or 'E')
            else if (ch == 'e' || ch == 'E') {
                // An exponent must follow at least one digit and must not appear twice
                if (!hasDigit || hasExponent) return false;
                hasExponent = true;
                hasSign = false; // Reset sign for exponent part
                hasDigit = false; // Reset digit flag, expecting a valid number after exponent
            }
            // Check if it's a sign ('+' or '-')
            else if (ch == '+' || ch == '-') {
                // Sign is only allowed at the start or immediately after an exponent
                if (hasSign || (i > 0 && numStr.charAt(i - 1) != 'e' && numStr.charAt(i - 1) != 'E')) 
                    return false;
                hasSign = true;
            }
            // Any other character is invalid
            else {
                return false;
            }
        }

        // A valid number must end with at least one digit
        return hasDigit;
    }
}
