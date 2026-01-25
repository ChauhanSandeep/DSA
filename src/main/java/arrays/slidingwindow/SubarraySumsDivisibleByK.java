package arrays.slidingwindow;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Subarray Sums Divisible by K
 *
 * Given an integer array nums and an integer k, return the number of non-empty subarrays that have a sum divisible by k.
 *
 * A subarray is a contiguous part of an array.
 *
 * Example:
 * Input: nums = [4,5,0,-2,-3,1], k = 5
 * Output: 7
 * Explanation: There are 7 subarrays with a sum divisible by k = 5:
 * [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
 *
 * LeetCode: https://leetcode.com/problems/subarray-sums-divisible-by-k
 *
 * Time Complexity: O(n) where n is the length of nums
 * Space Complexity: O(k) for the frequency map
 * LeetCode Contest Rating: 1676
 */
public class SubarraySumsDivisibleByK {
    public int subarraysDivByK(int[] nums, int k) {
        // Map to store frequency of remainders
        Map<Integer, Integer> remainderFreq = new HashMap<>();
        // Initialize with remainder 0 having one occurrence
        remainderFreq.put(0, 1);

        int prefixSum = 0;
        int count = 0;

        for (int num : nums) {
            // Update the prefix sum
            prefixSum += num;

            // Calculate the remainder when divided by k
            // Adding k before taking modulo to handle negative numbers
            int remainder = (prefixSum % k + k) % k;

            // If we've seen this remainder before, add the count to our result
            count += remainderFreq.getOrDefault(remainder, 0);

            // Update the frequency map
            remainderFreq.put(remainder, remainderFreq.getOrDefault(remainder, 0) + 1);
        }

        return count;
    }
}
