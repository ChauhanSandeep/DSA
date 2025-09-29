package arrays;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Range Addition
 *
 * Given an array of length n initialized with all 0's and k update operations,
 * apply all updates and return the modified array. Each operation is represented
 * as a triplet [startIndex, endIndex, inc] which increments each element of
 * subarray from startIndex to endIndex (both inclusive) with inc.
 *
 * Key challenge: Efficiently handle potentially many range updates without
 * iterating through each range for every update, which would be O(n*k) complexity.
 *
 * Core insight: Use difference array technique to mark range boundaries instead
 * of updating individual elements. This reduces complexity from O(n*k) to O(n+k).
 *
 * Example:
 * Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
 * Output: [-2,0,3,5,3]
 *
 * Process:
 * - Initial: [0,0,0,0,0]
 * - After [1,3,2]: [0,2,2,2,0]
 * - After [2,4,3]: [0,2,5,5,3]
 * - After [0,2,-2]: [-2,0,3,5,3]
 *
 * LeetCode: https://leetcode.com/problems/range-addition
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How to handle 2D range updates on a matrix?
 *    Answer: Extend to 2D difference array with boundary marking at corners of update rectangles.
 * 2. What if updates come in real-time and we need query results immediately?
 *    Answer: Use segment tree with lazy propagation for O(log n) updates and queries.
 * 3. How to optimize when many updates affect overlapping ranges?
 *    Answer: Consider coordinate compression or merge overlapping updates before processing.
 * 4. What if we need to support both range updates and range queries?
 *    Answer: Implement using Fenwick tree or segment tree with lazy propagation.
 *
 * Related Problems:
 * - LeetCode 598: Range Addition II
 * - LeetCode 307: Range Sum Query - Mutable
 * - LeetCode 1109: Corporate Flight Bookings (Same pattern)
 */
public class RangeAddition {

    /**
     * Applies range updates using difference array technique for optimal efficiency.
     *
     * Algorithm: Difference Array with Boundary Marking
     * Core principle: Instead of updating each element in a range, mark only the
     * boundaries where changes begin and end. Use prefix sum to reconstruct final array.
     *
     * Key steps:
     * 1. For each update [start, end, inc], mark diff[start] += inc (range begins)
     * 2. Mark diff[end+1] -= inc (range ends, if within bounds)
     * 3. Compute prefix sum to propagate boundary changes across entire array
     *
     * Why this works: When computing prefix sum, the increment at diff[start]
     * propagates through all subsequent positions until cancelled by decrement
     * at diff[end+1]. Multiple overlapping ranges automatically combine through addition.
     *
     * Time Complexity: O(n + k) where n = array length, k = number of updates
     * Space Complexity: O(n) for difference array storage
     *
     * @param length initial array length (all zeros)
     * @param updates array of [startIndex, endIndex, increment] operations
     * @return modified array after applying all range updates
     */
    public int[] getModifiedArray(int length, int[][] updates) {
        // Initialize difference array to track boundary changes
        int[] differenceArray = new int[length];

        // Process each range update by marking boundaries
        for (int[] update : updates) {
            int startIndex = update[0];
            int endIndex = update[1];
            int increment = update[2];

            // Mark start of range: increment begins affecting elements from here
            differenceArray[startIndex] += increment;

            // Mark end of range: increment stops affecting elements after endIndex
            // Boundary check prevents array index out of bounds
            if (endIndex + 1 < length) {
                differenceArray[endIndex + 1] -= increment;
            }
        }

        // Apply prefix sum to convert boundary markers into final values
        // Each position accumulates all increments that affect it
        for (int i = 1; i < length; i++) {
            differenceArray[i] += differenceArray[i - 1];
        }

        return differenceArray;
    }

    /**
     * Brute force implementation for verification and educational purposes.
     * Directly applies each range update without optimization.
     *
     * Time Complexity: O(n * k) - inefficient for large inputs
     * Space Complexity: O(n) - for result array only
     *
     * @param length initial array length
     * @param updates array of range update operations
     * @return modified array after all updates
     */
    public int[] getModifiedArrayBruteForce(int length, int[][] updates) {
        int[] result = new int[length];

        // Apply each update by directly modifying range elements
        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int increment = update[2];

            // Update each element in range individually
            for (int i = start; i <= end; i++) {
                result[i] += increment;
            }
        }

        return result;
    }


}
