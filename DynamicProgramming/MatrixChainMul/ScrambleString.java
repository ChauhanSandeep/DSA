package DynamicProgramming.MatrixChainMul;

import java.util.HashMap;
import java.util.Map;

/**
 * Determines if one string is a scrambled version of another.
 * LeetCode Link: https://leetcode.com/problems/scramble-string/
 *
 * Approach:
 * - Recursively check if `s1` can be transformed into `s2` by swapping substrings.
 * - Use memoization to optimize repeated subproblems.
 * - Base case: If `s1.equals(s2)`, return true.
 * - Additional check: If `s1` and `s2` are not anagrams, return false early.
 *
 * Time Complexity: O(N^4) (due to substring operations and recursive calls)
 * Space Complexity: O(N^3) (memoization table)
 */
public class ScrambleString {
    private final Map<String, Integer> memo = new HashMap<>();

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
