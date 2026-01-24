package arrays.slidingwindow;

import java.util.*;

/**
 * 1004. Max Consecutive Ones III
 *
 * Problem: Given a binary array nums and integer k, return the maximum number
 * of consecutive 1s in the array if you can flip at most k 0s.
 *
 * Example:
 * Input: nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
 * Output: 6
 * Explanation: [1,1,1,0,0,1,1,1,1,1,1] - flip the two 0s at indices 5 and 10
 *
 * LeetCode: https://leetcode.com/problems/max-consecutive-ones-iii
 *
 * Follow-up questions:
 * Q: What if we want to minimize the number of flips for a target length?
 * A: Use binary search on the answer with sliding window validation.
 *
 * Q: Can we handle the case where k is very large?
 * A: If k >= number of zeros, answer is the entire array length.
 *
 * Q: How to find all maximum-length windows?
 * A: Track all windows that achieve the maximum length during traversal.
 * LeetCode Contest Rating: 1656
 **/
public class MaxConsecutiveOnesIII {

    /**
     * Sliding window approach counting zeros in current window.
     *
     * Algorithm:
     * - Use sliding window [left, right] tracking number of zeros
     * - Expand right pointer, count zeros encountered
     * - If zeros > k, shrink window from left until zeros <= k
     * - Track maximum window size achieved
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) constant extra space
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