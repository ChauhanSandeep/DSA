package frazsheet;

import java.util.*;

/**
 * 77. Combinations
 * 
 * Problem: Given two integers n and k, return all possible combinations of k numbers
 * chosen from the range [1, n].
 * 
 * Example:
 * Input: n = 4, k = 2
 * Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * 
 * LeetCode: https://leetcode.com/problems/combinations
 * 
 * Follow-up questions:
 * Q: Can we generate combinations in lexicographic order?
 * A: Yes, current backtracking approach naturally generates them in lexicographic order.
 * 
 * Q: How to generate the next combination given a current one?
 * A: Implement next permutation algorithm adapted for combinations.
 * 
 * Q: Can we optimize space if we only need to count combinations?
 * A: Yes, use mathematical formula C(n,k) = n!/(k!(n-k)!) or dynamic programming.
 */
public class Combinations {
    
    /**
     * Generates all combinations of k numbers from range [1, n].
     * 
     * Algorithm: Backtracking
     * - Start from number 1, try each number as potential candidate
     * - For each choice, recursively generate remaining combinations
     * - Backtrack by removing current choice and trying next number
     * - Use start parameter to avoid duplicates and ensure ascending order
     * 
     * Time Complexity: O(C(n,k) * k) where C(n,k) is binomial coefficient
     * Space Complexity: O(k) for recursion stack and current combination
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        
        backtrack(result, current, n, k, 1);
        return result;
    }
    
    // Backtracking helper
    private void backtrack(List<List<Integer>> result, List<Integer> current, int n, int k, int start) {
        // Base case: found a complete combination
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        // Try each number from start to n
        for (int i = start; i <= n; i++) {
            // Early termination: not enough numbers left to complete combination
            if (current.size() + (n - i + 1) < k) {
                break;
            }
            
            current.add(i);
            backtrack(result, current, n, k, i + 1);
            current.remove(current.size() - 1);
        }
    }
    
    /**
     * Optimized version with better pruning.
     * Calculates exact remaining slots needed for early termination.
     */
    public List<List<Integer>> combineOptimized(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        
        if (k > n || k <= 0) {
            return result;
        }
        
        backtrackOptimized(result, new ArrayList<>(), n, k, 1);
        return result;
    }
    
    // Optimized backtracking with tighter bounds
    private void backtrackOptimized(List<List<Integer>> result, List<Integer> current, int n, int k, int start) {
        int remaining = k - current.size();
        
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        // Only iterate through numbers that can lead to valid combinations
        for (int i = start; i <= n - remaining + 1; i++) {
            current.add(i);
            backtrackOptimized(result, current, n, k, i + 1);
            current.remove(current.size() - 1);
        }
    }
    
    /**
     * Iterative approach using lexicographic generation.
     * Generates combinations without recursion.
     */
    public List<List<Integer>> combineIterative(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        
        if (k > n || k <= 0) {
            return result;
        }
        
        // Start with the first combination [1, 2, ..., k]
        List<Integer> current = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            current.add(i);
        }
        
        while (true) {
            result.add(new ArrayList<>(current));
            
            // Find the rightmost element that can be incremented
            int i = k - 1;
            while (i >= 0 && current.get(i) == n - k + i + 1) {
                i--;
            }
            
            if (i < 0) {
                break; // No more combinations
            }
            
            // Increment current[i] and adjust subsequent elements
            current.set(i, current.get(i) + 1);
            for (int j = i + 1; j < k; j++) {
                current.set(j, current.get(j - 1) + 1);
            }
        }
        
        return result;
    }
    
    /**
     * Binary representation approach.
     * Uses bit manipulation to generate all combinations.
     */
    public List<List<Integer>> combineBinary(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        
        // Generate all possible bitmasks with exactly k bits set
        generateCombinations(result, n, k, 0, 0, new ArrayList<>());
        
        return result;
    }
    
    // Generate combinations using binary representation
    private void generateCombinations(List<List<Integer>> result, int n, int k, int pos, int count, List<Integer> current) {
        if (count == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        if (pos > n || count + (n - pos) < k) {
            return;
        }
        
        // Include current position
        current.add(pos + 1);
        generateCombinations(result, n, k, pos + 1, count + 1, current);
        current.remove(current.size() - 1);
        
        // Exclude current position
        generateCombinations(result, n, k, pos + 1, count, current);
    }
    
    /**
     * Mathematical approach for counting combinations only.
     * Useful when we only need the count, not the actual combinations.
     */
    public long countCombinations(int n, int k) {
        if (k > n || k < 0) {
            return 0;
        }
        
        if (k == 0 || k == n) {
            return 1;
        }
        
        // Optimize by using C(n,k) = C(n,n-k)
        k = Math.min(k, n - k);
        
        long result = 1;
        for (int i = 0; i < k; i++) {
            result = result * (n - i) / (i + 1);
        }
        
        return result;
    }
    
    /**
     * Dynamic programming approach for computing binomial coefficients.
     * Useful for multiple queries or when building Pascal's triangle.
     */
    public List<List<Integer>> combineDP(int n, int k) {
        // Build Pascal's triangle up to row n
        int[][] dp = new int[n + 1][k + 1];
        
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
            for (int j = 1; j <= Math.min(i, k); j++) {
                dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
            }
        }
        
        // Generate actual combinations using the computed values
        List<List<Integer>> result = new ArrayList<>();
        generateFromDP(result, new ArrayList<>(), n, k, 1, dp);
        
        return result;
    }
    
    // Generate combinations using precomputed DP table
    private void generateFromDP(List<List<Integer>> result, List<Integer> current, int n, int k, int start, int[][] dp) {
        if (k == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = start; i <= n - k + 1; i++) {
            current.add(i);
            generateFromDP(result, current, n, k - 1, i + 1, dp);
            current.remove(current.size() - 1);
        }
    }
}