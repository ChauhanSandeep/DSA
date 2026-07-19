package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Letter Combinations of a Phone Number
 *
 * Given a string of digits from 2 through 9, return every letter string those
 * digits could represent on a phone keypad. The output may be in any order, but
 * the usual keypad order is easiest to read and verify.
 *
 * Leetcode: https://leetcode.com/problems/letter-combinations-of-a-phone-number/ (Medium)
 * Rating:   acceptance 66.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Recursion | Backtracking | One character choice per digit
 *
 * Example:
 *   Input:  digits = "23"
 *   Output: [ad, ae, af, bd, be, bf, cd, ce, cf]
 *   Why:    digit 2 contributes a, b, or c, and digit 3 contributes d, e, or f,
 *           so every first-letter choice is paired with every second-letter choice.
 *
 * Follow-ups:
 *   1. How would you support digits 0 and 1?
 *      Define whether they map to empty choices, themselves, or invalid input before recursing.
 *   2. How would you count combinations without building them?
 *      Multiply the number of mapped letters for each digit.
 *   3. How can this be generated iteratively?
 *      Use a queue and expand all current strings for one digit at a time.
 *   4. How would you stream combinations lazily?
 *      Treat each digit as a wheel in a mixed-radix counter and emit one string per advance.
 *
 * Related: Generate Parentheses (22), Combinations (77), Subsets (78).
 */
public class LetterCombination {

    public static void main(String[] args) {
        String[] inputs = { "", "2", "23" };
        String[] expected = {
            "[]",
            "[a, b, c]",
            "[ad, ae, af, bd, be, bf, cd, ce, cf]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = letterCombinations(inputs[i]);
            System.out.printf("digits=\"%s\" -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: this is the Cartesian product of the keypad choices. Each
     * recursion level fills one character position for the digit at currentIndex.
     * When currentIndex reaches the end, currentCombination contains one letter
     * for every digit and can be copied into result. Removing the last character
     * after each recursive call restores the prefix before trying the next letter.
     *
     * Algorithm:
     *   1. Return an empty list for null or empty input.
     *   2. Build the digitToLetters keypad array.
     *   3. Backtrack from currentIndex 0 with an empty currentCombination.
     *   4. For each mapped letter, choose it, recurse to the next digit, then un-choose it.
     *
     * Time:  O(4^n * n) - there can be up to 4 choices per digit, and copying
     *        each finished string costs n characters.
     * Space: O(n) - the recursion stack and currentCombination grow with the number of digits.
     *
     * @param digits string containing digits from 2 through 9
     * @return all letter combinations represented by the digits
     */
    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;

        // Phone keypad mapping: index represents digit, value represents letters
        String[] digitToLetters = {
                "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        backtrack(digits, digitToLetters, 0, new StringBuilder(), result);
        return result;
    }

    /** Fills one keypad letter per digit until the buffer is a complete combination. */
    private static void backtrack(String digits, String[] digitToLetters, int currentIndex,
        StringBuilder currentCombination, List<String> result) {
        // Base case: processed all digits
        if (currentIndex == digits.length()) {
            result.add(currentCombination.toString());
            return;
        }

        String possibleLetters = digitToLetters[digits.charAt(currentIndex) - '0'];
        for (char letter : possibleLetters.toCharArray()) {
            currentCombination.append(letter); // Choose letter
            backtrack(digits, digitToLetters, currentIndex + 1, currentCombination, result);
            currentCombination.deleteCharAt(currentCombination.length() - 1); // Backtrack
        }
    }
}
