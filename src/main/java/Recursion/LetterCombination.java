package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Given a string containing digits (2-9), return all possible letter 
 * combinations that the number could represent (like a telephone keypad).
 *
 * Approach:
 * - Use **backtracking** to generate all possible combinations.
 * - Use a **mapping array** to store corresponding characters for each digit.
 * - Iterate through the characters mapped to the current digit and recursively explore possibilities.
 *
 * Time Complexity: O(4^N) (Exponential, where N = number of digits)
 * Space Complexity: O(N) (Recursion stack depth)
 *
 * LeetCode Link: https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 */
public class LetterCombination {

    public static void main(String[] args) {
        System.out.println("All letter combinations for '23': " + letterCombinations("23"));
        System.out.println("All letter combinations for '222': " + letterCombinations("222"));
    }

    /**
     * Returns all possible letter combinations for a given digit string.
     *
     * @param digits A string consisting of digits from 2-9.
     * @return A list of possible letter combinations.
     */
    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.isEmpty()) return result;

        // Mapping of digits to corresponding letters on a telephone keypad
        String[] digitToLetters = {
                "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
        };

        backtrack(digits, digitToLetters, 0, new StringBuilder(), result);
        return result;
    }

    /**
     * Recursive backtracking function to generate letter combinations.
     *
     * @param digits        Input digit string.
     * @param digitToLetters Array mapping each digit (2-9) to corresponding letters.
     * @param index         Current position in the digit string.
     * @param current       Current combination being formed.
     * @param result        List storing all valid combinations.
     */
    private static void backtrack(String digits, String[] digitToLetters, int index, StringBuilder current, List<String> result) {
        if (index == digits.length()) {
            result.add(current.toString()); // Add formed combination to result
            return;
        }

        String letters = digitToLetters[digits.charAt(index) - '0'];
        for (char letter : letters.toCharArray()) {
            current.append(letter); // Choose a letter
            backtrack(digits, digitToLetters, index + 1, current, result);
            current.deleteCharAt(current.length() - 1); // Undo choice (Backtrack)
        }
    }
}
