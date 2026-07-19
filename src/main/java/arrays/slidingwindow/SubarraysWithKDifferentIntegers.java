package arrays.slidingwindow;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Problem: Subarrays with K Different Integers
 *
 * Count contiguous subarrays containing exactly k distinct integers. The usual
 * trick is to count subarrays with at most k distinct values and subtract those
 * with at most k - 1.
 *
 * Leetcode: https://leetcode.com/problems/subarrays-with-k-different-integers/ (Hard)
 * Rating:   acceptance 64.1% (Hard) - contest rating 2210
 * Pattern:  Sliding window | At most K transform | Frequency map
 *
 * Example:
 *   Input:  nums = [1,2,1,2,3], k = 2
 *   Output: 7
 *   Why:    exactly-k equals atMost(2) - atMost(1), leaving only windows with two distinct values.
 *
 * Follow-ups:
 *   1. Return all matching subarrays?
 *      Enumerate windows from the two at-most boundaries, but output can be quadratic.
 *   2. What if k is large relative to unique values?
 *      Return 0 when k exceeds the number of distinct values in the array.
 *   3. What if the stream is online?
 *      Maintain two sliding windows for at most k and at most k - 1 simultaneously.
 *
 * Related: Fruit Into Baskets (904), Longest Substring with At Most K Distinct Characters (340).
 */
public class SubarraysWithKDifferentIntegers {

    public static void main(String[] args) {
        SubarraysWithKDifferentIntegers solver = new SubarraysWithKDifferentIntegers();
        int[][] nums = {{1, 2, 1, 2, 3}, {1, 2, 1, 3, 4}, {1}};
        int[] kValues = {2, 3, 1};
        int[] expected = {7, 3, 1};

        for (int i = 0; i < nums.length; i++) {
            int got = solver.subarraysWithKDistinct(nums[i], kValues[i]);
            System.out.printf("nums=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(nums[i]), kValues[i], got, expected[i]);
        }
    }

    /**
     * Intuition: counting exactly k distinct values is easier as a subtraction.
     * Every subarray with at most k distinct values includes those with fewer, so
     * removing atMost(k - 1) leaves exactly k.
     *
     * Algorithm:
     *   1. Count subarrays with at most k distinct values.
     *   2. Count subarrays with at most k - 1 distinct values.
     *   3. Return the difference.
     *
     * Time:  O(n) - each atMostKDistinct pass moves both pointers linearly.
     * Space: O(k) - the frequency map stores values in the current window.
     *
     * @param nums input array
     * @param k required number of distinct integers
     * @return number of subarrays containing exactly k distinct integers
     */
    public int subarraysWithKDistinct(int[] nums, int k) {
        // The problem can be transformed to find the difference between
        // subarrays with at most K distinct integers and subarrays with at most K-1 distinct integers
        return atMostKDistinct(nums, k) - atMostKDistinct(nums, k - 1);
    }

    // Helper method to find the number of subarrays with at most K distinct integers
    private int atMostKDistinct(int[] nums, int k) {
        if (k < 0) return 0;

        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {
            // Add current number to frequency map
            int num = nums[right];
            freq.put(num, freq.getOrDefault(num, 0) + 1);

            // If we have more than k distinct elements, move left pointer
            while (freq.size() > k) {
                int leftNum = nums[left];
                freq.put(leftNum, freq.get(leftNum) - 1);
                if (freq.get(leftNum) == 0) {
                    freq.remove(leftNum);
                }
                left++;
            }

            // Add the number of subarrays ending at right with at most k distinct elements
            count += right - left + 1;
        }

        return count;
    }
}
