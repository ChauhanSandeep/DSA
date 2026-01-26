package strings.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a Roman numeral string to its corresponding integer value.
 * Problem: Roman to Integer
 * Example: "MCMIV" -> 1904
 * Explaination:
 * - Roman numerals are represented by combinations of letters from the Latin alphabet.
 * - Each letter has a fixed integer value.
 * - The numeral is read from left to right, and the values are added together.
 *
 * LeetCode Equivalent: https://leetcode.com/problems/roman-to-integer/
 * LeetCode Contest Rating: Not available (not a contest problem)
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
     * Approach:
     * - Use a HashMap to store the integer values of Roman numerals.
     * - Traverse the string from right to left, keeping track of the last processed numeral.
     * - If the current numeral is smaller than the last processed one, subtract it (e.g., IV = 4).
     * - Otherwise, add it to the result.
     *
     * Time Complexity: O(N) - Single traversal of the string.
     * Space Complexity: O(1) - Constant extra space for the HashMap.
     */
    public static int romanToInteger(String roman) {
        if (roman == null || roman.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid Roman numeral input.");
        }

        // Map storing Roman numeral values
        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

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
