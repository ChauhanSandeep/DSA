package dailybytes.string;

import java.util.Arrays;

/**
 * Problem: Valid Palindrome
 *
 * Given a string, determine whether it reads the same forward and backward after
 * ignoring non-alphanumeric characters and case. Punctuation and spaces do not
 * affect the comparison.
 *
 * Leetcode: https://leetcode.com/problems/valid-palindrome/ (Easy)
 * Rating:   acceptance 53.8% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Two pointers | Filtered comparison
 *
 * Example:
 *   Input:  str = "A man, a plan, a canal: Panama."
 *   Output: true
 *   Why:    after ignoring punctuation and case, the text becomes
 *           "amanaplanacanalpanama", which reads the same both ways.
 *
 * Follow-ups:
 *   1. Support full Unicode normalization?
 *      Normalize code points before comparing with Character helpers.
 *   2. Return the first mismatching pair?
 *      Return the left and right indices when the lowercase characters differ.
 *   3. Validate a stream with limited memory?
 *      Use rolling hashes or store only one side when exact comparison is required.
 *   4. Allow deleting one bad character?
 *      Branch once at the first mismatch, as in Valid Palindrome II.
 *
 * Related: Valid Palindrome II (680), Palindrome Linked List (234).
 */
public class ValidPalindrome {

    public static void main(String[] args) {
        String[] inputs = { "level", "algorithm", "A man, a plan, a canal: Panama.", " " };
        boolean[] expected = { true, false, true, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = isValidPalindrome(inputs[i]);
            System.out.printf("str=%s -> %b  expected=%b%n", inputs[i], output, expected[i]);
        }
    }

    /**
     * Intuition: only alphanumeric characters matter, so compare the next useful
     * character from the left with the next useful character from the right.
     * If every such pair matches after lowercasing, the filtered string is a
     * palindrome without ever building that filtered string.
     *
     * Algorithm:
     *   1. Return false for null input.
     *   2. Move left and right inward, skipping non-alphanumeric characters.
     *   3. Return false if the lowercase characters at left and right differ.
     *   4. Return true after all valid pairs have matched.
     *
     * Time:  O(n) - each character is skipped or compared at most once.
     * Space: O(1) - only two pointers are used.
     *
     * @param str input string to validate
     * @return true when the alphanumeric characters form a palindrome
     */
    public static boolean isValidPalindrome(String str) {
        if (str == null) return false;

        int left = 0, right = str.length() - 1;

        while (left < right) {
            // Skip non-alphanumeric characters
            while (left < right && !Character.isLetterOrDigit(str.charAt(left))) left++;
            while (left < right && !Character.isLetterOrDigit(str.charAt(right))) right--;

            if (Character.toLowerCase(str.charAt(left)) != Character.toLowerCase(str.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }

        return true;
    }
}