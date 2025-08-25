package arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Two Sum
 *
 * Given an array of integers and a target sum, find the indices of two numbers
 * such that they add up to the target.
 *
 * Approach:
 * - Use a **HashMap (numToIndexMap)** to store previously seen numbers and their indices.
 * - Iterate through the array, checking if **(target - currentNumber)** exists in the map.
 * - If found, return the indices; otherwise, store the current number in the map.
 *
 * Time Complexity: **O(N)** (Single pass through array)
 * Space Complexity: **O(N)** (Stores at most N elements in the map)
 *
 * Edge Cases:
 * - No valid pair → Returns `{-1, -1}`
 * - Multiple valid pairs → Returns the first found
 * - Negative numbers & duplicates are handled correctly
 *
 * LeetCode Problem Link:
 * https://leetcode.com/problems/two-sum/
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] arr = {12, 3, 4, 1, 6, 9};
        int target = 10;

        int[] result = getTwoSumIndices(arr, target);

        if (result[0] != -1) {
            System.out.println("Indices: " + result[0] + ", " + result[1]);
            System.out.println("Numbers: " + arr[result[0]] + ", " + arr[result[1]]);
        } else {
            System.out.println("No valid pair found.");
        }
    }

    /**
     * Finds two numbers in the array that add up to the given target.
     *
     * @param arr    Input array of integers
     * @param target Target sum
     * @return Indices of the two numbers that sum up to the target, or {-1, -1} if no pair exists
     */
    public static int[] getTwoSumIndices(int[] arr, int target) {
        Map<Integer, Integer> numToIndexMap = new HashMap<>();

        for (int i = 0; i < arr.length; i++) {
            int complement = target - arr[i];

            if (numToIndexMap.containsKey(complement)) {
                return new int[]{numToIndexMap.get(complement), i}; // Return indices of valid pair
            }

            numToIndexMap.put(arr[i], i); // Store current number with its index
        }

        return new int[]{-1, -1}; // No valid pair found
    }
}
