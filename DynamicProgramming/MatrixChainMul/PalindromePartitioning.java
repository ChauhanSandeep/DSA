package DynamicProgramming.MatrixChainMul;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Palindrome Partitioning
 * Leetcode Equivalent: https://leetcode.com/problems/palindrome-partitioning/
 *
 * Problem Statement:
 * Given a string `str`, partition the string such that every substring in the partition is a palindrome.
 * Return all possible palindrome partitionings of the string.
 *
 * Example:
 * Input: "aab"
 * Output: [["a", "a", "b"], ["aa", "b"]]
 * Explanation: The string "aab" can be partitioned into palindromic substrings in two ways:
 * - ["a", "a", "b"]: Each character is a palindrome.
 * - ["aa", "b"]: "aa" is a palindrome, and "b" is a palindrome.
 *
 * Intuition:
 * - A palindrome reads the same forward and backward. To solve this problem, we need to explore all possible
 *   partitions of the string and check if each substring in the partition is a palindrome.
 * - Use backtracking to generate all possible partitions and validate each substring for being a palindrome.
 *
 * Approach:
 * 1. Use a helper function to recursively explore all partitions starting from the current index.
 * 2. For each substring starting from the current index, check if it is a palindrome.
 * 3. If it is a palindrome, add it to the current path and recursively explore further partitions.
 * 4. Backtrack by removing the last added substring and continue exploring other possibilities.
 * 5. Add the valid partition to the result when the end of the string is reached.
 *
 * Time Complexity: O(n * 2^n)
 * - There are 2^n possible partitions of the string.
 * - Checking if a substring is a palindrome takes O(n) in the worst case.
 *
 * Space Complexity: O(n)
 * - The recursion stack and the path list can take up to O(n) space.
 */
public class PalindromePartitioning {

    public static void main(String[] args) {
        List<List<String>> result = new PalindromePartitioning().partition("aab");
        System.out.println(result);
    }

    public List<List<String>> partition(String input) {
        List<List<String>> result = new ArrayList<>();
        List<String> currentPartition = new ArrayList<>();
        Boolean[][] dp = new Boolean[input.length()][input.length()]; // memoization table
        backtrack(0, input, currentPartition, result, dp);
        return result;
    }

    /**
     * Backtracking helper to generate all possible palindromic partitions.
     */
    private void backtrack(int startIndex, String input, List<String> currentPartition, List<List<String>> result, Boolean[][] dp) {
        // If we've reached the end, add the current partition to result
        if (startIndex == input.length()) {
            result.add(new ArrayList<>(currentPartition));
            return;
        }

        for (int endIndex = startIndex; endIndex < input.length(); endIndex++) {
            // Check if substring input[startIndex ... endIndex] is palindrome
            if (isPalindrome(input, startIndex, endIndex, dp)) {
                // Choose
                currentPartition.add(input.substring(startIndex, endIndex + 1));
                // Explore
                backtrack(endIndex + 1, input, currentPartition, result, dp);
                // Unchoose (Backtrack)
                currentPartition.remove(currentPartition.size() - 1);
            }
        }
    }

    /**
     * Helper to check if input[start..end] is a palindrome using memoization.
     */
    private boolean isPalindrome(String input, int start, int end, Boolean[][] dp) {
        if (dp[start][end] != null) {
            return dp[start][end];
        }

        while (start <= end) {
            if (input.charAt(start) != input.charAt(end)) {
                dp[start][end] = false;
                return false;
            }
            start++;
            end--;
        }
        dp[start][end] = true;
        return true;
    }
}
