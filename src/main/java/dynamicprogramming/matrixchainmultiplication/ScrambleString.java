package dynamicprogramming.matrixchainmultiplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Scramble String
 *
 * A string can be scrambled by recursively splitting it into two non-empty parts
 * and optionally swapping the two parts at each split. Given source and target,
 * decide whether target can be produced from source by this process.
 *
 * Leetcode: https://leetcode.com/problems/scramble-string/
 * Rating:   acceptance 45.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Interval recursion | Split with swap/no-swap states
 *
 * Example:
 *   Input:  source = "great", target = "rgeat"
 *   Output: true
 *   Why:    split "great" as "gr" + "eat", scramble "gr" to "rg", and keep "eat" unchanged.
 *
 * Follow-ups:
 *   1. Can this be written bottom-up?
 *      Yes; dp[length][i][j] tells whether source substring i of that length scrambles to target substring j.
 *   2. How do you avoid exploring impossible splits?
 *      Reject states whose character frequencies differ before recursing.
 *   3. Can you return the actual sequence of swaps?
 *      Store the winning split and whether it swapped, then recursively rebuild the transformation tree.
 *
 * Related: Interleaving String (97), Different Ways to Add Parentheses (241).
 */
public class ScrambleString {
    private final Map<String, Boolean> memo = new HashMap<>();

        /**
     * Intuition: a scramble split either keeps the two child substrings aligned or
     * swaps them. Before trying splits, matching character counts prove the two
     * intervals could contain the same multiset of leaves.
     *
     * Algorithm:
     *   1. Return quickly for equal strings, length mismatch, or anagram mismatch.
     *   2. Memoize each source#target pair.
     *   3. Try every split position.
     *   4. Accept if either the no-swap or swapped recursive pairing works.
     *
     * Time:  O(n^4) - substring states try O(n) splits with substring work.
     * Space: O(n^3) - memoized substring pairs plus recursion depth.
     *
     * @param source first string
     * @param target second string
     * @return true if target is a scramble of source
     */
    public boolean isScramble(final String source, final String target) {
        if (source.equals(target))
            return true;
        if (!areAnagrams(source, target))
            return false;

        String key = source + "_" + target;
        if (memo.containsKey(key))
            return memo.get(key);

        int len = source.length();
        for (int breakPoint = 1; breakPoint < len; breakPoint++) {
            /**
             * There are 2 cases to consider:
             * 1. No swapping: Check if the first i characters of source can be scrambled to
             * match the first i characters of target,
             * and the remaining characters of source can be scrambled to match the
             * remaining characters of target.
             *
             * 2. With swapping: Check if the first i characters of source can be scrambled
             * to match the last i characters of target,
             * and the remaining characters of source can be scrambled to match the first
             * (len - i) characters of target.
             *
             * If either case returns true, then source is a scrambled version of target.
             */

            // Case 1: No swapping
            if (isScramble(source.substring(0, breakPoint), target.substring(0, breakPoint)) 
                    && isScramble(source.substring(breakPoint), target.substring(breakPoint))) {
                memo.put(key, true);
                return true;
            }

            // Case 2: With swapping
            if (isScramble(source.substring(0, breakPoint), target.substring(len - breakPoint)) 
                    && isScramble(source.substring(breakPoint), target.substring(0, len - breakPoint))) {
                memo.put(key, true);
                return true;
            }
        }
        memo.put(key, false);
        return false;
    }

        /** Returns whether two strings have identical character counts. */
    private boolean areAnagrams(String source, String target) {
        if (source.length() != target.length())
            return false;
        int[] count = new int[26];
        for (int i = 0; i < source.length(); i++) {
            count[source.charAt(i) - 'a']++;
            count[target.charAt(i) - 'a']--;
        }

        for (int freq : count) {
            if (freq != 0)
                return false;
        }
        return true;
    }


    public static void main(String[] args) {
        String[][] cases = { {"a", "a"}, {"great", "rgeat"}, {"abcde", "caebd"} };
        boolean[] expected = {true, true, false};

        for (int i = 0; i < cases.length; i++) {
            ScrambleString solver = new ScrambleString();
            boolean output = solver.isScramble(cases[i][0], cases[i][1]);
            System.out.printf("source=%s target=%s  ->  %b  expected=%b%n",
                cases[i][0], cases[i][1], output, expected[i]);
        }
    }

}
