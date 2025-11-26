package dynamicprogramming.MatrixChainMul;

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
 */
public class PalindromePartitioning {

    public static void main(String[] args) {
        PalindromePartitioning solution = new PalindromePartitioning();
        
        // Test backtracking approach
        List<List<String>> result1 = solution.partition("aab");
        System.out.println("Backtracking approach: " + result1);
        
        // Test bottom-up DP approach
        List<List<String>> result2 = solution.partitionBottomUp("aab");
        System.out.println("Bottom-Up DP approach: " + result2);
    }

    /**
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
     * - Copying the current partition to the result takes O(n) as well.
     *
     * Space Complexity: O(n)
     * - The recursion stack and the path list can take up to O(n) space.
     *
     * @param input the input string to be partitioned
     * @return a list of all possible palindromic partitions of the input string
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

        for (int endIndex = startIndex; endIndex < input.length(); endIndex++) {
            // Check if substring input[startIndex ... endIndex] is palindrome
            if (isPalindrome(input, startIndex, endIndex)) {
                // Select
                currentPartition.add(input.substring(startIndex, endIndex + 1));
                // Explore
                backtrack(endIndex + 1, input, currentPartition, result);
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
        for (int len = 2; len <= length; len++) {
            for (int i = 0; i <= length - len; i++) {
                int j = i + len - 1;
                if (input.charAt(i) == input.charAt(j)) {
                    isPalindrome[i][j] = (len == 2) || isPalindrome[i + 1][j - 1];
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
        for (int i = 1; i <= length; i++) {
            List<List<String>> currentPartitions = new ArrayList<>();
            
            // Try all possible last palindromic substrings ending at position i-1
            for (int j = 0; j < i; j++) {
                // If substring [j...i-1] is a palindrome
                if (isPalindrome[j][i - 1]) {
                    String palindrome = input.substring(j, i);
                    
                    // Get all partitions up to position j
                    List<List<String>> previousPartitions = partitionedPalindromes.get(j);
                    
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
