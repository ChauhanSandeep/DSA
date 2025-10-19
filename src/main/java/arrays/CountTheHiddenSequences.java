package com.sandeep.frazsheet.twopointers;

import java.util.*;


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
     * Generates all possible hidden sequences (for small inputs only).
     * Returns list of all valid sequences.
     *
     * Algorithm:
     * 1. Find valid starting range using prefix sum analysis
     * 2. Generate sequences for each valid starting value
     * 3. Return list of all valid sequences
     *
     * Time Complexity: O(k * n) where k is number of valid sequences
     * Space Complexity: O(k * n)
     */
    public List<List<Long>> generateAllSequences(int[] differences, int lower, int upper) {
        List<List<Long>> result = new ArrayList<>();

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
            List<Long> sequence = new ArrayList<>();
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
}