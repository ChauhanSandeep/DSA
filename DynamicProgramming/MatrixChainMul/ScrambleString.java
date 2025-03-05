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

    public int isScramble(final String s1, final String s2) {
        if (s1.equals(s2)) return 1;
        if (!areAnagrams(s1, s2)) return 0;

        String key = s1 + "_" + s2;
        if (memo.containsKey(key)) return memo.get(key);

        int len = s1.length();
        for (int i = 1; i < len; i++) {
            // Case 1: No swapping
            if (isScramble(s1.substring(0, i), s2.substring(0, i)) == 1 &&
                isScramble(s1.substring(i), s2.substring(i)) == 1) {
                memo.put(key, 1);
                return 1;
            }

            // Case 2: With swapping
            if (isScramble(s1.substring(0, i), s2.substring(len - i)) == 1 &&
                isScramble(s1.substring(i), s2.substring(0, len - i)) == 1) {
                memo.put(key, 1);
                return 1;
            }
        }
        memo.put(key, 0);
        return 0;
    }

    private boolean areAnagrams(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int[] count = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            count[s1.charAt(i) - 'a']++;
            count[s2.charAt(i) - 'a']--;
        }
        for (int freq : count) {
            if (freq != 0) return false;
        }
        return true;
    }
}
