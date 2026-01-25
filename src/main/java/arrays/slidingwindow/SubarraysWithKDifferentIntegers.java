package arrays.slidingwindow;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Subarrays with K Different Integers
 *
 * Given an integer array nums and an integer k, return the number of good subarrays of nums.
 * A good array is an array where the number of different integers in that array is exactly k.
 *
 * A subarray is a contiguous part of an array.
 *
 * Example:
 * Input: nums = [1,2,1,2,3], k = 2
 * Output: 7
 * Explanation: Subarrays formed with exactly 2 different integers:
 * [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
 *
 * LeetCode: https://leetcode.com/problems/subarrays-with-k-different-integers
 *
 * Time Complexity: O(n) where n is the length of nums
 * Space Complexity: O(k) for the frequency map
 * LeetCode Contest Rating: 2210
 */
public class SubarraysWithKDifferentIntegers {
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
