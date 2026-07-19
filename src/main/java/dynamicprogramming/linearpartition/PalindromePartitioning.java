package dynamicprogramming.linearpartition;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Palindrome Partitioning
 *
 * Given a string, return every way to split it into substrings such that every
 * substring is a palindrome. Each partition must preserve the original character
 * order and cover the entire string.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-partitioning/ (Medium)
 * Rating:   acceptance 74.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Backtracking | Palindrome cuts
 *
 * Example:
 *   Input:  input = "aab"
 *   Output: [[a, a, b], [aa, b]]
 *   Why:    those are the only cuts where every produced substring is a palindrome.
 *
 * Follow-ups:
 *   1. Return the minimum number of cuts?
 *      Use DP over prefix endpoints after precomputing palindromic substrings.
 *   2. Count partitions without listing them?
 *      Use a counting DP so exponential output storage is avoided.
 *   3. How do you speed up palindrome checks?
 *      Precompute a boolean palindrome table for all substrings.
 *
 * Related: Palindrome Partitioning II (132), Palindrome Partitioning III (1278).
 */
public class PalindromePartitioning {

    public static void main(String[] args) {
        PalindromePartitioning solver = new PalindromePartitioning();
        String[] inputs = { "aab", "" };
        String[] expected = { "[[a, a, b], [aa, b]]", "[[]]" };

        for (int i = 0; i < inputs.length; i++) {
            List<List<String>> got = solver.partition(inputs[i]);
            System.out.printf("input=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: a partition is built by choosing the first cut, then solving
     * the same problem for the remaining suffix. If input[startIndex..breakIndex]
     * is a palindrome, it is safe to place that substring next in the current
     * partition and recurse from breakIndex + 1. Backtracking removes that choice
     * so the next possible cut can be tried.
     *
     * Algorithm:
     *   1. Start with an empty currentPartition and explore cuts from index 0.
     *   2. For every end index, add the substring only if it is a palindrome.
     *   3. When startIndex reaches the end, copy the completed partition into result.
     *
     * Time:  O(n * 2^n) - there can be exponentially many partitions and palindrome checks cost up to n.
     * Space: O(n) - the recursion path holds at most n substrings, excluding output.
     *
     * @param input string to partition
     * @return all palindromic partitions of input
     */
    public List<List<String>> partition(String input) {
        List<List<String>> result = new ArrayList<>();
        List<String> currentPartition = new ArrayList<>();
        backtrack(0, input, currentPartition, result);
        return result;
    }

    /**
     * Backtracking helper to generate all possible palindromic partitions.
     */
    private void backtrack(int startIndex, String input, List<String> currentPartition, List<List<String>> result) {
        // If we've reached the end, add the current partition to result
        if (startIndex == input.length()) {
            result.add(new ArrayList<>(currentPartition));
            return;
        }

        for (int breakIndex = startIndex; breakIndex < input.length(); breakIndex++) {
            // Check if substring input[startIndex ... endIndex] is palindrome
            if (isPalindrome(input, startIndex, breakIndex)) {
                // Select
                currentPartition.add(input.substring(startIndex, breakIndex + 1));
                // Explore
                backtrack(breakIndex + 1, input, currentPartition, result);
                // Unselect (Backtrack)
                currentPartition.remove(currentPartition.size() - 1);
            }
        }
    }

    /**
     * Helper to check if input[start..end] is a palindrome.
     */
    private boolean isPalindrome(String input, int start, int end) {
        return input.substring(start, end+1).equals(new StringBuilder(input.substring(start, end+1)).reverse().toString());
    }

    /**
     * Bottom-Up DP Approach:
     * - Build solutions from smaller substrings to larger ones.
     * - First, precompute which substrings are palindromes using a 2D DP table.
     * - Then, use another DP table where dp[i] stores all possible palindromic partitions for substring [0...i-1].
     *
     * Approach:
     * 1. Create a 2D boolean array isPalin[i][j] to check if substring from i to j is a palindrome.
     * 2. Fill the palindrome table using bottom-up DP:
     *    - All single characters are palindromes.
     *    - For length >= 2, substring is palindrome if first and last chars match and inner substring is palindrome.
     * 3. Create dp array where dp[i] contains all partitions for substring [0...i-1].
     * 4. For each position i, try all possible last palindromic substrings ending at i-1.
     * 5. Combine previous partitions with the current palindrome substring.
     *
     * Time Complexity: O(n^2) for palindrome precomputation + O(n * 2^n) for generating partitions = O(n * 2^n)
     * Space Complexity: O(n^2) for palindrome table + O(n * 2^n) for storing all partitions
     *
     * @param input the input string to be partitioned
     * @return a list of all possible palindromic partitions of the input string
     */
    public List<List<String>> partitionBottomUp(String input) {
        int length = input.length();
        
        // Step 1: Precompute palindrome information using DP
        boolean[][] isPalindrome = new boolean[length][length];
        
        // Every single character is a palindrome
        for (int i = 0; i < length; i++) {
            isPalindrome[i][i] = true;
        }
        
        // Check for palindromes of length 2 and more
        for (int gap = 2; gap <= length; gap++) {
            for (int left = 0; left <= length - gap; left++) {
                int right = left + gap - 1;
                if (input.charAt(left) == input.charAt(right)) {
                    isPalindrome[left][right] = (gap == 2) || isPalindrome[left + 1][right - 1];
                }
            }
        }
        
        // Step 2: Build partitions using DP
        // partitionedPalindromes[i] contains all palindromic partitions for substring [0...i-1]
        List<List<List<String>>> partitionedPalindromes = new ArrayList<>();
        
        // Initialize: empty string has one empty partition
        partitionedPalindromes.add(new ArrayList<>());
        partitionedPalindromes.get(0).add(new ArrayList<>());
        
        // Build partitions for each position
        for (int right = 1; right <= length; right++) {
            List<List<String>> currentPartitions = new ArrayList<>();
            
            // Try all possible last palindromic substrings ending at position right-1
            for (int left = 0; left < right; left++) {
                // If substring [left...right-1] is a palindrome
                if (isPalindrome[left][right - 1]) {
                    String palindrome = input.substring(left, right);
                    
                    // Get all partitions up to position left
                    List<List<String>> previousPartitions = partitionedPalindromes.get(left);
                    
                    // Add current palindrome to each previous partition
                    for (List<String> prevPartition : previousPartitions) {
                        List<String> newPartition = new ArrayList<>(prevPartition);
                        newPartition.add(palindrome);
                        currentPartitions.add(newPartition);
                    }
                }
            }
            
            partitionedPalindromes.add(currentPartitions);
        }
        
        return partitionedPalindromes.get(length);
    }
}
