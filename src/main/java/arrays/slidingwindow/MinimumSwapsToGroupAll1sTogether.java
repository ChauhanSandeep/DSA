package arrays.slidingwindow;

import java.util.Arrays;

/**
 * Problem: Minimum Swaps to Group All 1s Together
 *
 * Given a binary array, group all ones into one contiguous block using the fewest
 * swaps. Because any position can be swapped, the cost of a target window is the
 * number of zeroes inside it.
 *
 * Leetcode: https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together/ (Medium)
 * Rating:   acceptance 60.5% (Medium) - contest rating 1508
 * Pattern:  Sliding window | Fixed-size window | Count zeroes
 *
 * Example:
 *   Input:  data = [1,0,1,0,1]
 *   Output: 1
 *   Why:    any length-3 window with two ones has one zero that must be swapped out.
 *
 * Follow-ups:
 *   1. What if the array is circular?
 *      Run the same fixed-size window over two copies or use modulo indices.
 *   2. What if grouping zeroes instead?
 *      Count total zeroes and minimize ones inside that window.
 *   3. What if only adjacent swaps are allowed?
 *      Use median positions of ones and prefix sums of distances.
 *
 * Related: Minimum Swaps to Group All 1s Together II (2134), Max Consecutive Ones III (1004).
 */
public class MinimumSwapsToGroupAll1sTogether {

    public static void main(String[] args) {
        MinimumSwapsToGroupAll1sTogether solver = new MinimumSwapsToGroupAll1sTogether();
        int[][] inputs = {{1, 0, 1, 0, 1}, {0, 0, 0, 1, 0}, {1, 1, 1}};
        int[] expected = {1, 0, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minSwaps(inputs[i]);
            System.out.printf("data=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: if there are onesCount ones, the final grouped block must have
     * length onesCount. Any zero inside that target block must be swapped with a
     * one outside it, so choose the length-onesCount window with the fewest zeroes.
     *
     * Algorithm:
     *   1. Count total ones to determine the fixed window size.
     *   2. Slide a window of that size while tracking zeroes inside it.
     *   3. Return the minimum zero count seen among full-size windows.
     *
     * Time:  O(n) - one pass counts ones and one pass slides the window.
     * Space: O(1) - only counters and pointers are used.
     *
     * @param data binary array
     * @return minimum swaps needed to group all ones together
     */
    public int minSwaps(int[] data) {
        int onesCount = 0;

        // Count total 1s in array
        for (int num : data) {
            if (num == 1) onesCount++;
        }

        // Edge case: no 1s or all are 1s
        if (onesCount <= 1) return 0;

        int windowSize = onesCount;
        int zerosInWindow = 0;
        int minSwaps = Integer.MAX_VALUE;
        int left = 0;

        // Sliding window
        for (int right = 0; right < data.length; right++) {
            // Expand: add right element to window
            if (data[right] == 0) {
                zerosInWindow++;
            }

            // Shrink: when window size exceeds target, remove left element
            if (right - left + 1 > windowSize) {
                if (data[left] == 0) {
                    zerosInWindow--;
                }
                left++;
            }

            // Calculate result: when window size equals target
            if (right - left + 1 == windowSize) {
                minSwaps = Math.min(minSwaps, zerosInWindow);
            }
        }

        return minSwaps;
    }

}
