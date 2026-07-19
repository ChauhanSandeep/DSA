package arrays;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Problem: Height Checker
 *
 * Students are standing in a line, and the expected line is the same heights in
 * non-decreasing order. Count how many positions currently have a different
 * height than they would have in that sorted line.
 *
 * Leetcode: https://leetcode.com/problems/height-checker/ (Easy)
 * Rating:   1303 (zerotrac Elo)
 * Pattern:  Array | Counting sort | Compare with expected order
 *
 * Example:
 *   Input:  heights = [1,1,4,2,1,3]
 *   Output: 3
 *   Why:    the expected order is [1,1,1,2,3,4], so positions 2, 4, and 5 differ.
 *
 * Follow-ups:
 *   1. Return the misplaced indices instead of the count?
 *      Compare against the expected order and collect each index that differs.
 *   2. Sort in decreasing height order instead?
 *      Build the expected sequence from high to low and compare the same way.
 *   3. Heights are not bounded by 100?
 *      Fall back to cloning and sorting, or coordinate-compress the values first.
 *
 * Related: Sort Array By Parity (905), Relative Sort Array (1122).
 *
 */
public class HeightChecker {

    public static void main(String[] args) {
        HeightChecker solver = new HeightChecker();

        int[][] inputs = { {1, 1, 4, 2, 1, 3}, {5, 1, 2, 3, 4}, {1, 1, 1} };
        int[] expected = { 3, 5, 0 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.heightChecker(inputs[i]);
            System.out.printf("heights=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: the expected line is simply the current heights sorted in
     * non-decreasing order. Clone before sorting so the original order remains
     * available, then compare both arrays position by position; every mismatch is a
     * student standing at a height different from the expected line.
     *
     * Algorithm:
     *   1. Clone heights into expected.
     *   2. Sort expected into non-decreasing order.
     *   3. Compare heights[i] with expected[i] for every position.
     *   4. Return the mismatch count.
     *
     * Time:  O(n log n) - sorting the cloned array dominates the comparison.
     * Space: O(n) - expected stores a full sorted copy.
     *
     * @param heights current student heights
     * @return number of positions that differ from sorted order
     */
    public int heightChecker(int[] heights) {
        int[] expected = heights.clone();
        Arrays.sort(expected);

        int count = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expected[i]) {
                count++;
            }
        }

        return count;
    }

    /**
     * Optimized counting sort approach (heights bounded by 100)
     * Time Complexity: O(n), Space Complexity: O(1) - fixed size array
     */
    public int heightCheckerCountingSort(int[] heights) {
        // Count frequency of each height (1 to 100)
        int[] freq = new int[101];
        for (int height : heights) {
            freq[height]++;
        }

        int count = 0;
        int currentHeight = 1;

        for (int i = 0; i < heights.length; i++) {
            // Find next height that should be at position i
            while (freq[currentHeight] == 0) {
                currentHeight++;
            }

            // Compare with actual height at position i
            if (heights[i] != currentHeight) {
                count++;
            }

            // Decrease frequency of current expected height
            freq[currentHeight]--;
        }

        return count;
    }

    /**
     * Stream-based approach for functional style
     * Time Complexity: O(n log n), Space Complexity: O(n)
     */
    public int heightCheckerStream(int[] heights) {
        int[] expected = Arrays.stream(heights).sorted().toArray();

        return (int) IntStream.range(0, heights.length)
                .filter(i -> heights[i] != expected[i])
                .count();
    }

    /**
     * Helper method to get actual misplaced indices (for debugging)
     */
    public List<Integer> getMisplacedIndices(int[] heights) {
        int[] expected = heights.clone();
        Arrays.sort(expected);

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expected[i]) {
                indices.add(i);
            }
        }

        return indices;
    }
}
