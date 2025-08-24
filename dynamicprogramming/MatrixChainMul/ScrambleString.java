package dynamicprogramming.MatrixChainMul;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Scramble String
 *
 * Determines if one string is a scrambled version of another. In a scramble, a string can be transformed by recursively swapping any non-empty substrings.
 *
 * Example:
 * Input: s1 = "great", s2 = "rgeat"
 * Output: true
 *
 * LeetCode: https://leetcode.com/problems/scramble-string/
 *
 * Follow-up Questions (FAANG-style):
 * 1. Can you optimize further for very long strings?
 *    - Use iterative DP table (O(N^4) time, O(N^3) space); see LeetCode editorial.
 * 2. How do you adapt for multiple queries on (s1, s2) pairs?
 *    - Use a global memoization map for caching subresults efficiently.
 * 3. Can you check actual transformation sequence?
 *    - Trace recursion and record each split/swap used in the transformation.
 * 4. What if swaps must be balanced or minimal?
 *    - Add constraints in recursion and minimize swap operations in DP.
 */
public class ScrambleString {
    private final Map<String, Integer> memo = new HashMap<>();

    /**
     * Recursive + Memoization Solution
     *
     * Steps of Solution:
     * - Base Case: If source equals target, return true.
     * - Anagram Check: If source and target are not anagrams, return false.
     * - Recursive Splitting: For each possible split index, check both no-swap and
     * swap cases recursively.
     * - Memoization: Cache results for (source, target) pairs to avoid recomputation.
     *
     * Algorithm: Top-down recursion with memoization
     * Time Complexity: O(N^4) in worst case due to substring operations and splits
     * Space Complexity: O(N^3) for memoization storage
     *
     * @param source Source string
     * @param target Target string
     * @return
     */
    public int isScramble(final String source, final String target) {
        if (source.equals(target)) return 1;
        if (!areAnagrams(source, target)) return 0;

        String key = source + "_" + target;
        if (memo.containsKey(key)) return memo.get(key);

        int len = source.length();
        for (int i = 1; i < len; i++) {
            /**
             * There are 2 cases to consider:
             * 1. No swapping: Check if the first i characters of source can be scrambled to match the first i characters of target,
             *    and the remaining characters of source can be scrambled to match the remaining characters of target.
             *
             * 2. With swapping: Check if the first i characters of source can be scrambled to match the last i characters of target,
             *    and the remaining characters of source can be scrambled to match the first (len - i) characters of target.
             *
             * If either case returns true, then source is a scrambled version of target.
              */


            // Case 1: No swapping
            if (isScramble(source.substring(0, i), target.substring(0, i)) == 1 &&
                isScramble(source.substring(i), target.substring(i)) == 1) {
                memo.put(key, 1);
                return 1;
            }

            // Case 2: With swapping
            if (isScramble(source.substring(0, i), target.substring(len - i)) == 1 &&
                isScramble(source.substring(i), target.substring(0, len - i)) == 1) {
                memo.put(key, 1);
                return 1;
            }
        }
        memo.put(key, 0);
        return 0;
    }

    /**
     * Check if two strings are anagrams.
     * Anagrams are strings that can be rearranged to form each other.
     * @param source first string
     * @param target second string
     * @return true if they are anagrams, false otherwise
     */
    private boolean areAnagrams(String source, String target) {
        if (source.length() != target.length()) return false;
        int[] count = new int[26];
        for (int i = 0; i < source.length(); i++) {
            count[source.charAt(i) - 'a']++;
            count[target.charAt(i) - 'a']--;
        }

        for (int freq : count) {
            if (freq != 0) return false;
        }
        return true;
    }
}
