package dailybytes.array;

import java.util.Arrays;

/**
 * Problem: Valid Anagram
 *
 * Given two lowercase strings, decide whether one can be rearranged to form the
 * other. Both strings must have exactly the same character counts.
 *
 * Leetcode: https://leetcode.com/problems/valid-anagram/ (Easy)
 * Rating:   acceptance 68.4% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Frequency counting | Character inventory
 *
 * Example:
 *   Input:  str1 = "cat", str2 = "tac"
 *   Output: true
 *   Why:    both strings contain one 'a', one 'c', and one 't', just in a
 *           different order.
 *
 * Follow-ups:
 *   1. Support Unicode or mixed-case text?
 *      Normalize the input, then count with a hash map instead of a 26-slot array.
 *   2. Check many words against one fixed word?
 *      Precompute the fixed word frequency signature and compare each candidate.
 *   3. Group a full list of anagrams?
 *      Use the frequency signature or sorted string as a hash-map key.
 *   4. Handle streaming characters from both strings?
 *      Keep a running difference map and a nonzero-count total.
 *
 * Related: Group Anagrams (49), Find All Anagrams in a String (438).
 */
public class ValidAnagram {

    public static void main(String[] args) {
        String[][] inputs = { { "cat", "tac" }, { "program", "function" }, { "", "" } };
        boolean[] expected = { true, false, true };

        for (int i = 0; i < inputs.length; i++) {
            boolean output = validAnagrams(inputs[i][0], inputs[i][1]);
            System.out.printf("strings=%s -> %b  expected=%b%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

    /**
     * Intuition: anagrams have no leftover letters after one string pays for the
     * other. Count every letter in str1 as a credit, subtract every letter in
     * str2 as a debit, and the strings are anagrams only if all balances end at
     * zero.
     *
     * Algorithm:
     *   1. Return false immediately when the lengths differ.
     *   2. Increment charFrequency for every character in str1.
     *   3. Decrement charFrequency for every character in str2.
     *   4. Return false on any nonzero count; otherwise return true.
     *
     * Time:  O(n) - each string and the fixed alphabet are scanned once.
     * Space: O(1) - the 26 lowercase counts do not grow with input length.
     *
     * @param str1 first lowercase string
     * @param str2 second lowercase string
     * @return true when the two strings are anagrams, otherwise false
     */
    public static boolean validAnagrams(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }

        int[] charFrequency = new int[26];

        // Increment counts for characters in the first string
        for (char c : str1.toCharArray()) {
            charFrequency[c - 'a']++;
        }

        // Decrement counts for characters in the second string
        for (char c : str2.toCharArray()) {
            charFrequency[c - 'a']--;
        }

        // Check if all counts are zero
        for (int count : charFrequency) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }
}
