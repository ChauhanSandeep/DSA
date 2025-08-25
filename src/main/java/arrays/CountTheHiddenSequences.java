package com.sandeep.frazsheet.twopointers;

/**
 * Problem: Count The Hidden Sequences
 * 
 * You are given a 0-indexed array of n integers differences, which describes the differences
 * between the adjacent elements of a hidden sequence of length n + 1. More formally, call the
 * hidden sequence hidden, then we have:
 * hidden[i + 1] - hidden[i] == differences[i] for 0 <= i < n
 * 
 * You are further given two integers lower and upper that describe the inclusive range of values
 * [lower, upper] that the hidden sequence can contain.
 * Return the number of possible hidden sequences that exist. If no such sequence exists, return 0.
 * 
 * Example:
 * Input: differences = [1, -3, 4], lower = 1, upper = 6
 * Output: 2
 * Explanation: The hidden sequence is [hidden[0], hidden[0] + 1, hidden[0] - 2, hidden[0] + 2].
 * The two possible sequences are:
 * - [3, 4, 1, 5] if hidden[0] = 3
 * - [4, 5, 2, 6] if hidden[0] = 4
 * 
 * LeetCode: https://leetcode.com/problems/count-the-hidden-sequences
 * 
 * Follow-up Questions:
 * 1. What if we need to return all possible hidden sequences instead of just the count?
 *    Answer: Generate sequences by iterating through valid starting values and constructing each sequence.
 * 
 * 2. How would you handle very large ranges efficiently?
 *    Answer: Current approach is already O(1) for counting, just calculate valid range mathematically.
 * 
 * 3. What if differences array can be very large?
 *    Answer: Use prefix sums and optimize memory usage, but time complexity remains O(n).
 *    Related: https://leetcode.com/problems/subarray-sum-equals-k/
 * 
 * @author Sandeep
 */
public class CountTheHiddenSequences {
    
    /**
     * Counts possible hidden sequences using prefix sum and range analysis.
     * 
     * Algorithm:
     * 1. Calculate prefix sums to determine relative positions
     * 2. Find minimum and maximum values in the constructed sequence
     * 3. Determine valid range for the starting value
     * 4. Count integers in the valid range
     * 
     * Time Complexity: O(n) where n is length of differences array
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param differences Array of adjacent differences
     * @param lower Lower bound of allowed values
     * @param upper Upper bound of allowed values
     * @return Number of possible hidden sequences
     */
    public int numberOfArrays(int[] differences, int lower, int upper) {
        if (differences == null || differences.length == 0) {
            // Single element sequence, check if it can be in range
            return upper >= lower ? 1 : 0;
        }
        
        // Calculate prefix sums to find relative positions
        long prefixSum = 0;
        long minPrefix = 0;
        long maxPrefix = 0;
        
        for (int diff : differences) {
            prefixSum += diff;
            minPrefix = Math.min(minPrefix, prefixSum);
            maxPrefix = Math.max(maxPrefix, prefixSum);
        }
        
        // For a starting value x, the sequence values will be:
        // x, x + prefix[0], x + prefix[1], ..., x + prefix[n-1]
        // where prefix[i] is the sum of differences[0..i]
        
        // The minimum value in sequence will be x + minPrefix
        // The maximum value in sequence will be x + maxPrefix
        
        // For sequence to be valid:
        // lower <= x + minPrefix  =>  x >= lower - minPrefix
        // x + maxPrefix <= upper  =>  x <= upper - maxPrefix
        
        long minStartValue = lower - minPrefix;
        long maxStartValue = upper - maxPrefix;
        
        if (minStartValue > maxStartValue) {
            return 0; // No valid starting values
        }
        
        // Count integers in range [minStartValue, maxStartValue]
        return (int)(maxStartValue - minStartValue + 1);
    }
    
    /**
     * Alternative implementation with explicit sequence construction for verification.
     * More intuitive but same time complexity.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n) for storing prefix sums
     */
    public int numberOfArraysAlternative(int[] differences, int lower, int upper) {
        int n = differences.length;
        
        // Calculate all prefix sums
        long[] prefixSums = new long[n + 1];
        prefixSums[0] = 0;
        
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + differences[i];
        }
        
        // Find range of prefix sums
        long minPrefix = 0;
        long maxPrefix = 0;
        
        for (long sum : prefixSums) {
            minPrefix = Math.min(minPrefix, sum);
            maxPrefix = Math.max(maxPrefix, sum);
        }
        
        // Calculate valid starting range
        long minStart = lower - minPrefix;
        long maxStart = upper - maxPrefix;
        
        return Math.max(0, (int)(maxStart - minStart + 1));
    }
    
    /**
     * Optimized single-pass approach without storing prefix sums.
     * Most memory efficient implementation.
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public int numberOfArraysOptimized(int[] differences, int lower, int upper) {
        long currentSum = 0;
        long minSum = 0;
        long maxSum = 0;
        
        // Single pass to find min and max prefix sums
        for (int diff : differences) {
            currentSum += diff;
            minSum = Math.min(minSum, currentSum);
            maxSum = Math.max(maxSum, currentSum);
        }
        
        // Calculate valid range for starting value
        long validRangeStart = lower - minSum;
        long validRangeEnd = upper - maxSum;
        
        if (validRangeStart > validRangeEnd) {
            return 0;
        }
        
        // Handle potential overflow in subtraction
        if (validRangeEnd - validRangeStart >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        
        return (int)(validRangeEnd - validRangeStart + 1);
    }
    
    /**
     * Validates if a specific starting value produces a valid hidden sequence.
     * Useful for testing and verification.
     * 
     * @param differences Array of differences
     * @param startValue Starting value for the sequence
     * @param lower Lower bound
     * @param upper Upper bound
     * @return true if starting value produces valid sequence
     */
    public boolean isValidSequence(int[] differences, long startValue, int lower, int upper) {
        long current = startValue;
        
        // Check if starting value is in range
        if (current < lower || current > upper) {
            return false;
        }
        
        // Check each subsequent value
        for (int diff : differences) {
            current += diff;
            if (current < lower || current > upper) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Generates all possible hidden sequences (for small inputs only).
     * Returns list of all valid sequences.
     * 
     * Time Complexity: O(k * n) where k is number of valid sequences
     * Space Complexity: O(k * n)
     */
    public java.util.List<java.util.List<Long>> generateAllSequences(int[] differences, int lower, int upper) {
        java.util.List<java.util.List<Long>> result = new java.util.ArrayList<>();
        
        // Find valid starting range (same logic as main algorithm)
        long prefixSum = 0;
        long minPrefix = 0;
        long maxPrefix = 0;
        
        for (int diff : differences) {
            prefixSum += diff;
            minPrefix = Math.min(minPrefix, prefixSum);
            maxPrefix = Math.max(maxPrefix, prefixSum);
        }
        
        long minStartValue = lower - minPrefix;
        long maxStartValue = upper - maxPrefix;
        
        // Generate all sequences for valid starting values
        for (long start = minStartValue; start <= maxStartValue; start++) {
            java.util.List<Long> sequence = new java.util.ArrayList<>();
            long current = start;
            sequence.add(current);
            
            for (int diff : differences) {
                current += diff;
                sequence.add(current);
            }
            
            result.add(sequence);
        }
        
        return result;
    }
    
    /**
     * Edge case handler for various input scenarios.
     * 
     * @param differences Input differences array
     * @param lower Lower bound
     * @param upper Upper bound
     * @return Number of valid sequences, handling edge cases
     */
    public int numberOfArraysRobust(int[] differences, int lower, int upper) {
        // Validate inputs
        if (lower > upper) return 0;
        if (differences == null || differences.length == 0) {
            return upper >= lower ? 1 : 0;
        }
        
        // Handle potential overflow in differences
        try {
            return numberOfArraysOptimized(differences, lower, upper);
        } catch (Exception e) {
            // Fallback for overflow cases
            return 0;
        }
    }
}