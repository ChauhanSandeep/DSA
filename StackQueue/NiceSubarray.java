package StackQueue;

import java.util.LinkedList;

/**
 * Problem: Count Number of Nice Subarrays
 * 
 * Given an array of integers `nums` and an integer `k`, return the number 
 * of subarrays that contain exactly `k` odd numbers.
 * 
 * Example:
 * Input: nums = [1, 1, 2, 1, 1], k = 3
 * Output: 2
 * Explanation: The two valid subarrays are [1,1,2,1,1] and [1,2,1,1].
 * 
 * Approach:
 * - Use a **deque (LinkedList) to track indices of odd numbers**.
 * - Maintain a **window** that ensures exactly `k` odd numbers.
 * - Count subarrays that start between two consecutive odd indices.
 * 
 * Time Complexity: **O(N)** (each element is processed at most twice)
 * Space Complexity: **O(K)** (for storing odd indices in deque)
 * 
 * LeetCode Link: https://leetcode.com/problems/count-number-of-nice-subarrays/
 */
public class NiceSubarray {

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 1, 1};
        int k = 3;
        System.out.println(countNiceSubarrays(nums, k)); // Expected output: 2
    }

    /**
     * Counts the number of subarrays containing exactly k odd numbers.
     *
     * @param nums Input array of integers
     * @param k    The required count of odd numbers in a subarray
     * @return Number of valid subarrays
     */
    public static int countNiceSubarrays(int[] nums, int k) {
        LinkedList<Integer> oddIndices = new LinkedList<>();
        oddIndices.offer(-1); // Initialize with -1 for easier calculations

        int count = 0;

        for (int i = 0; i < nums.length; i++) {
            // If the number is odd, store its index
            if (nums[i] % 2 != 0) {
                oddIndices.offer(i);
            }

            // Maintain only (k+1) odd numbers in the window
            while (oddIndices.size() > k + 1) {
                oddIndices.poll();
            }

            // If we have exactly k odd numbers, count valid subarrays
            if (oddIndices.size() == k + 1) {
                int startIndex = oddIndices.get(1); // First odd number in the window
                int previousOddIndex = oddIndices.get(0); // Odd number before `startIndex`
                count += startIndex - previousOddIndex;
            }
        }

        return count;
    }
}
