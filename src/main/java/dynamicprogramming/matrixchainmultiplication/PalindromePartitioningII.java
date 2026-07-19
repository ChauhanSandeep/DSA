package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Palindrome Partitioning II
 *
 * Given a string, split it into palindromic substrings using as few cuts as
 * possible. Return only the minimum cut count, not the actual partition.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-partitioning-ii/
 * Rating:   acceptance 37.5% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Palindrome interval DP | Prefix partition DP
 *
 * Example:
 *   Input:  s = "aab"
 *   Output: 1
 *   Why:    the prefix "aa" is a palindrome and "b" is a palindrome, so one cut is enough.
 *
 * Follow-ups:
 *   1. Can you return all minimum-cut partitions?
 *      Store every previous index that attains the best cut count and backtrack through those choices.
 *   2. Can you answer many substring queries?
 *      Keep the palindrome table and a separate range-DP or query structure for requested intervals.
 *   3. What if each cut has a custom cost?
 *      Replace the fixed +1 transition with that boundary cost and minimize total cost.
 *
 * Related: Palindrome Partitioning (131), Palindrome Partitioning III (1278).
 */
public class PalindromePartitioningII {

        /**
     * Intuition: once palindrome substrings are known, the last cut before position
     * end can be tried directly. If input[start..end] is a palindrome, then it can
     * be the final block after the best partition ending at start - 1.
     *
     * Algorithm:
     *   1. Precompute palindrome[start][end] for all substrings.
     *   2. Let cuts[end] be the minimum cuts for the prefix ending at end.
     *   3. Use zero cuts when the whole prefix is a palindrome.
     *   4. Otherwise minimize cuts[start - 1] + 1 over palindromic suffixes.
     *
     * Time:  O(n^2) - palindrome and cut tables scan substring pairs.
     * Space: O(n^2) - palindrome table plus cuts array.
     *
     * @param input string to partition
     * @return minimum cuts so every piece is a palindrome
     */
    public int minCut(String input) {
        if (input == null || input.length() <= 1) {
            return 0;
        }

        int length = input.length();

        // STEP 1: Precompute palindrome information using Interval DP (The "Gap" Pattern)
        boolean[][] isPalindrome = new boolean[length][length];

        // Every single character is a palindrome (gap 1)
        for (int i = 0; i < length; i++) {
            isPalindrome[i][i] = true;
        }

        // Fill table for gaps of 2 and more
        for (int gap = 2; gap <= length; gap++) {
            for (int left = 0; left <= length - gap; left++) {
                int right = left + gap - 1;
                if (input.charAt(left) == input.charAt(right)) {
                    // A substring is a palindrome if outer chars match AND inner part is a palindrome
                    isPalindrome[left][right] = (gap == 2) || isPalindrome[left + 1][right - 1];
                }
            }
        }

        // STEP 2: Build minimum cuts using Linear Partition DP
        // minCuts[i] stores the min cuts for input.substring(0, i)
        int[] minCuts = new int[length + 1];

        // Initialize with worst case: a cut between every single character
        // A string of length i can be cut at most i-1 times.
        for (int i = 0; i <= length; i++) {
            minCuts[i] = i - 1;
        }

        // Fill minCuts table linearly from left to right
        for (int right = 1; right <= length; right++) {
            // Try every possible index 'j' to see if input[j...i-1] is the last palindromic piece
            for (int left = 0; left < right; left++) {
                if (isPalindrome[left][right - 1]) {
                    // If the whole prefix [0...right-1] is a palindrome, 0 cuts are needed
                    if (left == 0) {
                        minCuts[right] = 0;
                    } else {
                        // Otherwise, take the best solution for the prefix ending at left,
                        // and add 1 more cut for the current palindrome [left...right-1]
                        minCuts[right] = Math.min(minCuts[right], minCuts[left] + 1);
                    }
                }
            }
        }

        return minCuts[length];
    }


    public static void main(String[] args) {
        PalindromePartitioningII solver = new PalindromePartitioningII();
        String[] inputs = {"a", "aab", "aba", "abcde"};
        int[] expected = {0, 1, 0, 4};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.minCut(inputs[i]);
            System.out.printf("s=%s  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}