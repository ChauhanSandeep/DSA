package arrays;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Range Addition
 *
 * Problem: Apply range updates efficiently. Given length n and list of updates [start, end, inc],
 * return final array after applying all updates.
 *
 * Example: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]] -> Output: [-2,0,3,5,3]
 * Start with [0,0,0,0,0], apply updates to get final array.
 *
 * LeetCode: https://leetcode.com/problems/range-addition
 *
 * Follow-up Questions:
 * - How to handle updates dynamically? (Use segment tree with lazy propagation)
 * - What if we need range queries too? (Combine with range query data structure)
 * - Can we handle 2D range updates? (Extend difference array to 2D)
 */
public class RangeAddition {

    /**
     * Applies range updates efficiently using difference array technique.
     *
     * Algorithm:
     * 1. Use difference array to mark range updates
     * 2. For update [start, end, inc]: diff[start] += inc, diff[end+1] -= inc
     * 3. Compute prefix sum of difference array to get final result
     * 4. This converts O(k*n) naive approach to O(k+n)
     *
     * Time Complexity: O(k + n) where k is number of updates, n is array length
     * Space Complexity: O(n) for difference array
     *
     * @param length size of the array
     * @param updates list of [startIndex, endIndex, increment] updates
     * @return final array after all updates
     */
    public int[] getModifiedArray(int length, int[][] updates) {
        int[] diff = new int[length + 1]; // Extra space to handle end+1 safely

        // Apply all updates to difference array
        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            diff[start] += inc;
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
        }

        // Convert difference array to actual values using prefix sum
        int[] result = new int[length];
        result[0] = diff[0];

        for (int i = 1; i < length; i++) {
            result[i] = result[i - 1] + diff[i];
        }

        return result;
    }

    /**
     * Alternative implementation with bounds checking
     * Time Complexity: O(k + n), Space Complexity: O(n)
     */
    public int[] getModifiedArraySafe(int length, int[][] updates) {
        int[] diff = new int[length + 1];

        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            // Add increment at start position
            if (start < length) {
                diff[start] += inc;
            }

            // Subtract increment at position after end
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
        }

        // Build result using prefix sum
        int[] result = new int[length];
        int prefixSum = 0;

        for (int i = 0; i < length; i++) {
            prefixSum += diff[i];
            result[i] = prefixSum;
        }

        return result;
    }

    /**
     * Naive approach for comparison (less efficient)
     * Time Complexity: O(k * n), Space Complexity: O(n)
     */
    public int[] getModifiedArrayNaive(int length, int[][] updates) {
        int[] result = new int[length];

        // Apply each update by iterating through the range
        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            for (int i = start; i <= end; i++) {
                result[i] += inc;
            }
        }

        return result;
    }

    /**
     * In-place version using the result array as difference array
     * Time Complexity: O(k + n), Space Complexity: O(1) extra
     */
    public int[] getModifiedArrayInPlace(int length, int[][] updates) {
        int[] result = new int[length];

        // Use result array as difference array first
        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            result[start] += inc;
            if (end + 1 < length) {
                result[end + 1] -= inc;
            }
        }

        // Convert to prefix sum in-place
        for (int i = 1; i < length; i++) {
            result[i] += result[i - 1];
        }

        return result;
    }

    /**
     * Stream-based approach using Java 8
     * Time Complexity: O(k + n), Space Complexity: O(n)
     */
    public int[] getModifiedArrayStream(int length, int[][] updates) {
        int[] diff = new int[length + 1];

        // Apply updates using streams
        Arrays.stream(updates).forEach(update -> {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            diff[start] += inc;
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
        });

        // Convert to result using stream
        return IntStream.range(0, length)
                .map(i -> IntStream.rangeClosed(0, i).map(j -> diff[j]).sum())
                .toArray();
    }

    /**
     * Helper method to demonstrate the difference array concept
     */
    public void explainDifferenceArray(int length, int[][] updates) {
        int[] diff = new int[length + 1];

        System.out.println("Applying updates to difference array:");
        for (int[] update : updates) {
            int start = update[0];
            int end = update[1];
            int inc = update[2];

            System.out.printf("Update [%d,%d,%d]: ", start, end, inc);
            diff[start] += inc;
            if (end + 1 < length) {
                diff[end + 1] -= inc;
            }
            System.out.println(Arrays.toString(Arrays.copyOf(diff, length)));
        }

        System.out.println("Converting to final array using prefix sum:");
        int[] result = new int[length];
        result[0] = diff[0];
        System.out.println("Step 0: " + Arrays.toString(result));

        for (int i = 1; i < length; i++) {
            result[i] = result[i - 1] + diff[i];
            System.out.printf("Step %d: %s\n", i, Arrays.toString(result));
        }
    }
}
