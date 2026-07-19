package arrays.slidingwindow;

import java.util.*;

/**
 * Problem: Max Consecutive Ones III
 *
 * Given a binary array and k, flip at most k zeroes to ones. Return the longest
 * contiguous window that can be made all ones after those flips.
 *
 * Leetcode: https://leetcode.com/problems/max-consecutive-ones-iii/ (Medium)
 * Rating:   acceptance 65.0% (Medium) - contest rating 1656
 * Pattern:  Sliding window | At most k zeroes
 *
 * Example:
 *   Input:  nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 *   Output: 6
 *   Why:    the best window contains exactly two zeroes, which can both be flipped.
 *
 * Follow-ups:
 *   1. Return the window boundaries too?
 *      Store left and right whenever the best length improves.
 *   2. What if k is larger than the zero count?
 *      The whole array is valid, so return nums.length.
 *   3. Find the shortest window needing exactly k flips?
 *      Track windows with exactly k zeroes and minimize length instead.
 *
 * Related: Max Consecutive Ones (485), Max Consecutive Ones II (487).
 */
public class MaxConsecutiveOnesIII {

    public static void main(String[] args) {
        MaxConsecutiveOnesIII solver = new MaxConsecutiveOnesIII();
        int[][] nums = {{1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 0, 0}, {1, 1, 1}};
        int[] kValues = {2, 0, 1};
        int[] expected = {6, 3, 3};

        for (int i = 0; i < nums.length; i++) {
            int got = solver.longestOnes(nums[i], kValues[i]);
            System.out.printf("nums=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(nums[i]), kValues[i], got, expected[i]);
        }
    }


    /**
     * Intuition: a window is valid exactly when it contains at most
     * maxAllowedZeroes zeroes. Expand right to try longer windows, and whenever
     * the zero budget is exceeded, move left until the window is valid again.
     *
     * Algorithm:
     *   1. Move right across nums, counting zeroes added to the window.
     *   2. While usedZeroes exceeds maxAllowedZeroes, move left and refund zeroes.
     *   3. Record the largest valid window length.
     *
     * Time:  O(n) - each pointer moves across the array once.
     * Space: O(1) - only counters and pointers are used.
     *
     * @param nums binary array
     * @param maxAllowedZeroes maximum zeroes that may be flipped
     * @return longest window that can be made all ones
     */
    public int longestOnes(int[] nums, int maxAllowedZeroes) {
        int left = 0, usedZeroes = 0, maxLength = 0;

        for (int right = 0; right < nums.length; right++) {
            // Expand window
            if (nums[right] == 0) {
                usedZeroes++;
            }

            // Shrink window if too many zeros
            while (usedZeroes > maxAllowedZeroes) {
                if (nums[left] == 0) {
                    usedZeroes--;
                }
                left++;
            }

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    /**
     * Queue-based approach tracking positions of zeros.
     * Maintains positions of zeros for efficient window management.
     */
    public int longestOnesQueue(int[] nums, int k) {
        Queue<Integer> zeroPositions = new LinkedList<>();
        int left = 0, maxLength = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeroPositions.offer(right);

                // Remove oldest zero position if we exceed k zeros
                if (zeroPositions.size() > k) {
                    left = zeroPositions.poll() + 1;
                }
            }

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
