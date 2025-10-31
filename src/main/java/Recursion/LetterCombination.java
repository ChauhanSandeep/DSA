package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Letter Combinations of a Phone Number
 *
 * Problem:
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations
 * that the number could represent. A mapping of digits to letters (just like on telephone buttons).
 *
 * Example:
 * Input: digits = "23"
 * Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 *
 * Example:
 * Input: digits = "2"
 * Output: ["a","b","c"]
 *
 * Constraints:
 * - 0 <= digits.length <= 4
 * - digits[i] is a digit in the range ['2', '9']
 *
 * LeetCode: https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 *
 * Follow-up Questions:
 * Q1: How would you handle digit '0' and '1' which don't have letters on phone keypads?
 * A1: Map them to empty strings or skip them during iteration, adjusting the mapping array.
 *
 * Q2: What if we want to generate combinations with a specific prefix or suffix?
 * A2: Initialize the StringBuilder with the prefix before starting backtracking.
 *
 * Q3: Can we optimize memory if we only need to count combinations, not list them?
 * A3: Yes - just multiply the number of letters for each digit (e.g., "23" has 3*3=9 combinations).
 *
 * Q4: How would you implement this iteratively using a queue (BFS approach)?
 * A4: Start with empty string in queue, for each digit expand all strings by appending each possible letter.
 *
 * Q5: What if digits can repeat and we want unique combinations only?
 * A5: Use a Set to store results instead of List, or sort and deduplicate afterwards.
 */
public class LetterCombination {

    public static void main(String[] args) {
        System.out.println("All letter combinations for '23': " + letterCombinations("23"));
        System.out.println("All letter combinations for '222': " + letterCombinations("222"));
    }

    /**
     * Returns all possible letter combinations for a given digit string.
     *
     * Time Complexity: O(4^N * N) - 4^N combinations, N characters per combination
     * Space Complexity: O(N) - recursion depth
     *
     * @param digits String containing digits from 2-9
     * @return List of all possible letter combinations
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

    /**
     * Recursive backtracking helper to generate all letter combinations.
     *
     * Algorithm:
     * 1. Base case: If processed all digits, add current combination to result
     * 2. Get letters corresponding to current digit
     * 3. For each letter, append it and recurse to next digit
     * 4. Backtrack by removing last appended letter
     *
     * Key Insight: Each digit adds one layer to the recursion tree, with branching
     * factor equal to the number of letters for that digit (3 or 4).
     *
     * Time Complexity: O(4^N * N) where N is length of digits (4^N combinations, N to build each)
     * Space Complexity: O(N) for recursion depth
     *
     * @param digits Input string of digits (2-9)
     * @param digitToLetters Mapping array from digit to corresponding letters
     * @param currentIndex Current position being processed in digits string
     * @param currentCombination StringBuilder holding current combination being built
     * @param result List storing all valid letter combinations
     */
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
