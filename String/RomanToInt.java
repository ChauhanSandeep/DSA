package String;

import java.util.Map;

/**
 * Converts a Roman numeral string to its corresponding integer value.
 *
 * Approach:
 * - Use a HashMap to store the integer values of Roman numerals.
 * - Traverse the string from right to left, keeping track of the last processed numeral.
 * - If the current numeral is smaller than the last processed one, subtract it (e.g., IV = 4).
 * - Otherwise, add it to the result.
 *
 * Time Complexity: O(N) - Single traversal of the string.
 * Space Complexity: O(1) - Constant extra space for the HashMap.
 *
 * LeetCode Equivalent: https://leetcode.com/problems/roman-to-integer/
 */
public class RomanToInt {
    public static void main(String[] args) {
        String romanNumeral = "MCMIV";
        int integerValue = romanToInteger(romanNumeral);
        System.out.println("Integer value for " + romanNumeral + " is " + integerValue);
    }

    /**
     * Converts a Roman numeral string to an integer.
     *
     * @param roman The Roman numeral string (e.g., "MCMIV").
     * @return The corresponding integer value.
     * @throws IllegalArgumentException if the input is null, empty, or contains invalid characters.
     */
    public static int romanToInteger(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Roman numeral input.");
        }

        // Immutable map storing Roman numeral values (Java 9+)
        Map<Character, Integer> romanValues = Map.of(
                'I', 1, 'V', 5, 'X', 10, 'L', 50,
                'C', 100, 'D', 500, 'M', 1000
        );

        int result = 0;
        int lastSeenValue = 0;

        // Traverse the string from right to left
        for (int i = roman.length() - 1; i >= 0; i--) {
            char currentChar = roman.charAt(i);

            // Validate character
            if (!romanValues.containsKey(currentChar)) {
                throw new IllegalArgumentException("Invalid character in Roman numeral: " + currentChar);
            }

            int currValue = romanValues.get(currentChar);

            // If current value is smaller than the last seen value, subtract it
            if (currValue < lastSeenValue) {
                result -= currValue;
            } else {
                result += currValue;
            }

            lastSeenValue = currValue;
        }

        return result;
    }
}
