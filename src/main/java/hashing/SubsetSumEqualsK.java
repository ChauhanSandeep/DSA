package hashing;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Count the number of subarrays with a given sum `k`.
 *
 * Given an integer array `nums`, return the total number of continuous subarrays
 * whose sum equals `k`. The array can contain both positive and negative integers.
 *
 * **LeetCode Problem:** https://leetcode.com/problems/subarray-sum-equals-k/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class SubsetSumEqualsK {
    public static void main(String[] args) {
        int[] nums = {1, 1, 1};
        int targetSum = 2;
        System.out.println("Number of subarrays with sum " + targetSum + ": " + countSubarraysWithSum(nums, targetSum));
    }

    /**
     * **Approach: Prefix Sum + HashMap**
     * 1. Maintain a running prefix sum (`currentSum`).
     * 2. Use a **HashMap (prefixSumMap)** to store the count of prefix sums encountered so far.
     * 3. If `currentSum - k` exists in the map, it means there exists a subarray summing to `k`.
     * 4. Update the map to keep track of occurrences of each prefix sum.
     *
     * Note: If all the numbers are positive then we can simply use a two-pointer technique,
     *
     * **Time Complexity:** O(N) → Single pass through the array.
     * **Space Complexity:** O(N) → HashMap stores at most `N` prefix sums.
     *
     * @param nums Input array of integers.
     * @param k Target sum for the subarrays.
     * @return Number of continuous subarrays whose sum equals `k`.
     */
    public static int countSubarraysWithSum(int[] nums, int k) {
        int currentSum = 0;
        int subarrayCount = 0;
        Map<Integer, Integer> prefixSumMap = new HashMap<>();
        prefixSumMap.put(0, 1); // Handle case where subarray starts from index 0

        for (int num : nums) {
            currentSum += num; // Update prefix sum

            // If (currentSum - k) exists, add its count to the result
            if (prefixSumMap.containsKey(currentSum - k)) {
                subarrayCount += prefixSumMap.get(currentSum - k);
            }

            // Store the occurrence of the current prefix sum
            prefixSumMap.put(currentSum, prefixSumMap.getOrDefault(currentSum, 0) + 1);
        }

        return subarrayCount;
    }
}
