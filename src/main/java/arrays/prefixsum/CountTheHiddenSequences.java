package arrays.prefixsum;

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
 * LeetCode Contest Rating: 1614
 */
public class CountTheHiddenSequences {

    /**
     * Counts how many valid original arrays can produce the given differences array
     * while keeping every element within [lower, upper].
     *
     * Idea:
     * 1. Build relative offsets using prefix sums of differences.
     * 2. Track the smallest and largest offset we ever reach.
     * 3. Let firstElement be the unknown starting value.
     *    - Every element = firstElement + someOffset.
     * 4. Constrain firstElement so that all elements stay within [lower, upper].
     * 5. Count how many integer values of firstElement satisfy all constraints.
     */
    public int numberOfArrays(int[] differences, int lower, int upper) {
        if (differences == null || differences.length == 0) {
            // Only one element in the array; it just needs to be within [lower, upper].
            return upper >= lower ? 1 : 0;
        }

        // currentOffset: value difference from the first element as we walk forward.
        // minOffset / maxOffset: smallest and largest offset seen so far.
        long currentOffset = 0;
        long minOffset = 0;
        long maxOffset = 0;

        for (int delta : differences) {
            currentOffset += delta;
            minOffset = Math.min(minOffset, currentOffset);
            maxOffset = Math.max(maxOffset, currentOffset);
        }

        // If firstElement = x, then all elements lie in:
        // [x + minOffset, x + maxOffset].
        //
        // To keep the whole array within [lower, upper], we need:
        //   x + minOffset >= lower   ->  x >= lower - minOffset
        //   x + maxOffset <= upper  ->  x <= upper - maxOffset
        long minFirstElement = lower - minOffset;
        long maxFirstElement = upper - maxOffset;

        if (minFirstElement > maxFirstElement) {
            // No starting value x satisfies both inequalities.
            return 0;
        }

        // Number of integer x in [minFirstElement, maxFirstElement].
        return (int) (maxFirstElement - minFirstElement + 1);
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