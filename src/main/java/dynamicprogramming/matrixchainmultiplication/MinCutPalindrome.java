package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Palindrome Partitioning II
 *
 * Given a string, cut it into substrings so every piece is a palindrome. Return
 * the minimum number of cuts needed. A string that is already a palindrome needs
 * zero cuts.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-partitioning-ii/
 * Rating:   acceptance 37.5% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Palindrome interval DP | Prefix partition DP
 *
 * Example:
 *   Input:  s = "aab"
 *   Output: 1
 *   Why:    "aa" and "b" are both palindromes, so one cut after the second a is enough.
 *
 * Follow-ups:
 *   1. Can you return an actual minimum-cut partition?
 *      Store the previous cut position when each prefix answer improves, then backtrack.
 *   2. Can space be reduced below O(n^2)?
 *      Use expand-around-center updates or Manacher-style palindrome information with a 1D cut array.
 *   3. What if each cut has a different cost?
 *      Replace the +1 transition with the cost of cutting at that boundary.
 *
 * Related: Palindrome Partitioning (131), Palindrome Partitioning III (1278).
 */
public class MinCutPalindrome {

        /**
     * Intuition: a valid partition chooses a first palindromic piece and then solves
     * the suffix. The recursion tries every ending point for that first piece and
     * keeps the fewest cuts among palindromic choices.
     *
     * Algorithm:
     *   1. Return 0 when the current interval is already a palindrome.
     *   2. Try each split after start for the first piece.
     *   3. Recurse only when str[start..split] is a palindrome.
     *   4. Memoize the best cut count for each start/end interval.
     *
     * Time:  O(n^3) - intervals and splits can each check palindromes.
     * Space: O(n^2) - memo table plus recursion depth.
     *
     * @param str input string
     * @return minimum cuts needed so every piece is a palindrome
     */
    public int minCut(String str) {
        int length = str.length();
        int[][] dp = new int[length][length]; // dp[start][end] = min cuts needed for str[start:end]
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        return minCutsRecursive(str, 0, length - 1, dp);
    }

    /** Returns the minimum palindrome cuts for str[start..end]. */
    private int minCutsRecursive(String str, int start, int end, int[][] dp) {
        if (dp[start][end] != -1) return dp[start][end];

        // If the substring is already a palindrome, no cuts are needed
        if (isPalindrome(str, start, end)) {
            dp[start][end] = 0;
            return 0;
        }

        int minCuts = end - start; // Initially assume max cuts (cut at every character)
        for (int i = start + 1; i <= end; i++) {
            if (isPalindrome(str, start, i - 1)) {
                minCuts = Math.min(minCuts, 1 + minCutsRecursive(str, i, end, dp));
            }
        }

        dp[start][end] = minCuts;
        return minCuts;
    }


        /**
     * Intuition: first precompute which substrings are palindromes, then a prefix DP
     * only needs to ask where the last palindromic block starts. If s[start..end] is
     * a palindrome, it can finish a partition ending at end.
     *
     * Algorithm:
     *   1. Build a palindrome table by expanding substring lengths.
     *   2. Let cuts[end] be the minimum cuts for s[0..end].
     *   3. Set zero cuts when the whole prefix is a palindrome.
     *   4. Otherwise try each palindromic suffix start and minimize cuts.
     *
     * Time:  O(n^2) - palindrome table and cut transitions scan substring pairs.
     * Space: O(n^2) - palindrome table plus cuts array.
     *
     * @param str input string
     * @return minimum cuts needed so every piece is a palindrome
     */
    public int minCutDP(String str) {
        int length = str.length();
        boolean[][] isPalindrome = new boolean[length][length]; // isPalindrome[i][j] = true if str[i:j] is a palindrome
        

        // Precompute palindrome substrings
        for (int end = 0; end < length; end++) {
            for (int start = 0; start <= end; start++) {
                if (str.charAt(start) == str.charAt(end) && (end - start <= 2 || isPalindrome[start + 1][end - 1])) {
                    isPalindrome[start][end] = true;
                }
            }
        }

        int[] minCutsRequired = new int[length]; // minCutsRequired[i] = min cuts needed for str[0..i]
        // Fill DP table
        for (int right = 0; right < length; right++) {
            if (isPalindrome[0][right]) {
                minCutsRequired[right] = 0; // No cut needed if whole substring is a palindrome
            } else {
                minCutsRequired[right] = right; // Max possible cuts (cut at every character)
                for (int left = 1; left <= right; left++) {
                    if (isPalindrome[left][right]) {
                        int cutsBeforeLeft = minCutsRequired[left - 1];
                        minCutsRequired[right] = Math.min(
                            minCutsRequired[right], // if we don't cut at left
                            1 + cutsBeforeLeft // if we cut at left, adding 1 cut
                        );
                    }
                }
            }
        }

        return minCutsRequired[length - 1];
    }

        /** Returns true when s[left..right] is a palindrome. */
    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }


    public static void main(String[] args) {
        MinCutPalindrome solver = new MinCutPalindrome();
        String[] inputs = {"a", "aab", "abcde", "aba"};
        int[] expected = {0, 1, 4, 0};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.minCutDP(inputs[i]);
            System.out.printf("s=%s  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}
