package arrays.prefixsum;

import java.util.*;
import java.util.stream.IntStream;
/**
 * Problem: Range Addition
 *
 * Start with an all-zero array of a given length, apply inclusive range increment
 * updates, and return the final array. The efficient solution records only where
 * each increment starts and stops.
 *
 * Leetcode: https://leetcode.com/problems/range-addition/ (Medium)
 * Rating:   no contest rating (premium problem)
 * Pattern:  Difference array | Prefix sum | Range updates
 *
 * Example:
 *   Input:  length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
 *   Output: [-2,0,3,5,3]
 *   Why:    prefixing the boundary deltas accumulates exactly the active updates at each index.
 *
 * Follow-ups:
 *   1. Support online range updates and point queries?
 *      Use a Fenwick tree over the difference array.
 *   2. Support range updates and range sum queries?
 *      Use lazy propagation or two Fenwick trees.
 *   3. Extend to 2D rectangles?
 *      Mark four corners in a 2D difference array and take 2D prefix sums.
 *
 * Related: Range Addition II (598), Corporate Flight Bookings (1109).
 */
public class RangeAddition {

    public static void main(String[] args) {
        RangeAddition solver = new RangeAddition();

        int[] lengths = { 5, 3 };
        int[][][] updates = {
            { {1, 3, 2}, {2, 4, 3}, {0, 2, -2} },
            { }
        };
        int[][] expected = { {-2, 0, 3, 5, 3}, {0, 0, 0} };

        for (int i = 0; i < lengths.length; i++) {
            int[] got = solver.getModifiedArray(lengths[i], updates[i]);
            System.out.printf("length=%d updates=%s -> output=%s  expected=%s%n",
                lengths[i], Arrays.deepToString(updates[i]), Arrays.toString(got), Arrays.toString(expected[i]));
        }
    }

/**
 * Intuition: adding inc to every element in [start, end] means the running value
 * increases at start and decreases just after end. A final prefix sum spreads
 * each boundary marker across exactly its intended range.
 *
 * Algorithm:
 *   1. Create a differenceArray of the requested length.
 *   2. For each update, add increment at startIndex.
 *   3. Subtract increment at endIndex + 1 when that index exists.
 *   4. Prefix-sum differenceArray to recover the final values.
 *
 * Time:  O(length + updates) - each update and each array position is processed once.
 * Space: O(length) - the returned difference array holds the final values.
 *
 * @param length length of the initially zero array
 * @param updates range updates as [startIndex, endIndex, increment]
 * @return final array after all updates
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
