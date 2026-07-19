package arrays.prefixsum;

import java.util.*;
/**
 * Problem: Count The Hidden Sequences
 *
 * Given adjacent differences and an allowed inclusive range [lower, upper], count
 * how many starting values can produce a hidden sequence whose every value stays
 * in range.
 *
 * Leetcode: https://leetcode.com/problems/count-the-hidden-sequences/ (Medium)
 * Rating:   1614 (Weekly Contest 277)
 * Pattern:  Prefix sum | Range intersection | Difference array
 *
 * Example:
 *   Input:  differences = [1,-3,4], lower = 1, upper = 6
 *   Output: 2
 *   Why:    only starting values 3 and 4 keep all generated values within [1,6].
 *
 * Follow-ups:
 *   1. Return all valid sequences?
 *      Enumerate every valid start and rebuild the sequence; output can be large.
 *   2. Differences arrive as a stream?
 *      Maintain running offset, minimum offset, and maximum offset online.
 *   3. Range bounds are huge?
 *      Use long arithmetic for offset bounds before converting the final count.
 *
 * Related: Corporate Flight Bookings (1109), Range Addition (370).
 */
public class CountTheHiddenSequences {

    public static void main(String[] args) {
        CountTheHiddenSequences solver = new CountTheHiddenSequences();

        int[][] differences = { {1, -3, 4}, {3, -4, 5, 1, -2}, {} };
        int[] lowers = { 1, -4, 2 };
        int[] uppers = { 6, 5, 2 };
        int[] expected = { 2, 4, 1 };

        for (int i = 0; i < differences.length; i++) {
            int got = solver.numberOfArrays(differences[i], lowers[i], uppers[i]);
            System.out.printf("differences=%s lower=%d upper=%d -> output=%d  expected=%d%n",
                Arrays.toString(differences[i]), lowers[i], uppers[i], got, expected[i]);
        }
    }

/**
 * Intuition: after choosing the first hidden value x, every later value is x plus
 * a prefix sum of differences. So all constraints collapse to one interval for x:
 * x + minOffset must be at least lower, and x + maxOffset must be at most upper.
 *
 * Algorithm:
 *   1. Handle the empty differences case as a one-element sequence.
 *   2. Scan differences to find the minimum and maximum relative offset from the start.
 *   3. Convert those offsets into the valid interval for the first element.
 *   4. Return the number of integers in that interval, or 0 if it is empty.
 *
 * Time:  O(n) - each difference is scanned once.
 * Space: O(1) - only offset bounds are stored.
 *
 * @param differences adjacent differences of the hidden sequence
 * @param lower minimum allowed sequence value
 * @param upper maximum allowed sequence value
 * @return count of valid hidden sequences
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
