package dailybytes.string;

import java.util.Arrays;

/**
 * Problem: Valid Palindrome II
 *
 * Given a string, return true if it is already a palindrome or can become one by
 * deleting at most one character. Only one mismatch can be repaired.
 *
 * Leetcode: https://leetcode.com/problems/valid-palindrome-ii/ (Easy)
 * Rating:   acceptance 44.5% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Two pointers | One allowed deletion
 *
 * Example:
 *   Input:  str = "foobof"
 *   Output: true
 *   Why:    deleting 'b' leaves "fooof", which is a palindrome.
 *
 * Follow-ups:
 *   1. Allow deleting up to k characters?
 *      Use dynamic programming on substring endpoints and remaining deletions.
 *   2. Return which character to delete?
 *      At the first mismatch, test both branches and return the successful index.
 *   3. Minimize deletions to form a palindrome?
 *      Compute n minus the longest palindromic subsequence length.
 *   4. Support case-insensitive alphanumeric filtering too?
 *      Combine this branch-on-mismatch logic with the Valid Palindrome filters.
 *
 * Related: Valid Palindrome (125), Palindrome Linked List (234).
 */
public class ValidPalindrome2 {

    public static void main(String[] args) {
        String[] inputs = { "foobof", "abcba", "abccab", "a" };
        boolean[] expected = { true, true, false, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = canFormPalindromeByDeletingAtMostOneChar(inputs[i]);
            System.out.printf("str=%s -> %b  expected=%b%n", inputs[i], output, expected[i]);
        }
    }

    /**
     * Intuition: matching characters on the outside are harmless, so keep moving
     * inward. At the first mismatch, the single allowed deletion must remove
     * either the left character or the right character; if neither remaining
     * substring is a palindrome, no one-deletion fix exists.
     *
     * Algorithm:
     *   1. Return true for null, empty, or one-character strings.
     *   2. Move left and right inward while characters match.
     *   3. On the first mismatch, test the two substrings formed by skipping either side.
     *   4. Return true if the full scan finishes with no mismatch.
     *
     * Time:  O(n) - the main scan plus at most one helper scan are linear.
     * Space: O(1) - only indices are used.
     *
     * @param str input string to test
     * @return true if the string is a palindrome after at most one deletion
     */
    public static boolean canFormPalindromeByDeletingAtMostOneChar(String str) {
        if (str == null || str.length() < 2) return true;

        int left = 0, right = str.length() - 1;

        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                // Try skipping either the left or the right character
                return isPalindrome(str, left + 1, right) || isPalindrome(str, left, right - 1);
            }
            left++;
            right--;
        }

        return true; // No mismatches
    }

    /** Returns true when str[left..right] is a palindrome. */
    private static boolean isPalindrome(String str, int left, int right) {
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) return false;
        }
        return true;
    }
}