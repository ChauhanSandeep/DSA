package DynamicProgramming;

/**
 * LeetCode Problem: https://leetcode.com/problems/interleaving-string/
 * 
 * Problem Statement:
 * Given strings s1, s2, and s3, determine if s3 is formed by an interleaving of s1 and s2.
 * Interleaving means that characters from s1 and s2 are used in order without rearranging.
 * 
 * Approach:
 * - We use **Top-Down Dynamic Programming (Memoization)**.
 * - A **2D DP table (memoization array) is used** to store results of subproblems.
 * - At each step, we try using characters from either `s1` or `s2` to match `s3`.
 * - If a character from `s1` matches `s3`, move `index1` forward.
 * - If a character from `s2` matches `s3`, move `index2` forward.
 * - If both match, explore both possibilities using recursion.
 * 
 * Complexity Analysis:
 * - **Time Complexity: O(M * N)** (where M = length of s1, N = length of s2)
 * - **Space Complexity: O(M * N)** (due to memoization table)
 */
public class InterleavingString {

    public boolean isInterleave(String s1, String s2, String s3) {
        // Base case: Length check
        if (s1.length() + s2.length() != s3.length()) return false;

        // Memoization table to store results of subproblems
        Boolean[][] memo = new Boolean[s1.length() + 1][s2.length() + 1];

        return isInterleaveHelper(s1, s2, s3, 0, 0, memo);
    }

    private boolean isInterleaveHelper(String s1, String s2, String s3, int index1, int index2, Boolean[][] memo) {
        // If both s1 and s2 are exhausted, we've successfully interleaved
        if (index1 == s1.length() && index2 == s2.length()) return true;

        // If this subproblem was already solved, return stored result
        if (memo[index1][index2] != null) return memo[index1][index2];

        // Calculate current index in s3
        int index3 = index1 + index2;

        boolean canInterleave = false;

        // Try taking a character from s1 if it matches the corresponding character in s3
        if (index1 < s1.length() && s1.charAt(index1) == s3.charAt(index3)) {
            canInterleave |= isInterleaveHelper(s1, s2, s3, index1 + 1, index2, memo);
        }

        // Try taking a character from s2 if it matches the corresponding character in s3
        if (!canInterleave && index2 < s2.length() && s2.charAt(index2) == s3.charAt(index3)) {
            canInterleave |= isInterleaveHelper(s1, s2, s3, index1, index2 + 1, memo);
        }

        // Store result in memo table
        memo[index1][index2] = canInterleave;
        return canInterleave;
    }
}
