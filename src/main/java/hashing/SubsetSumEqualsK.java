package hashing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Subarray Sum Equals K
 *
 * Given an integer array, count the continuous subarrays whose sum is exactly k.
 * Values may be positive, negative, or zero, so a simple sliding window is not
 * enough for the general case.
 *
 * Leetcode: https://leetcode.com/problems/subarray-sum-equals-k/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | Prefix sum | Frequency map
 *
 * Example:
 *   Input:  nums = [1,1,1], k = 2
 *   Output: 2
 *   Why:    the subarrays nums[0..1] and nums[1..2] each sum to 2.
 *
 * Follow-ups:
 *   1. How would the solution change if all numbers were positive?
 *      A sliding window can count or find target-sum windows because sums only grow as the right pointer moves.
 *   2. How would you return the subarray ranges instead of the count?
 *      Store all indices for each prefix sum and emit ranges when currentSum - k appears.
 *   3. How would you handle very large sums?
 *      Use long for prefix sums to avoid integer overflow.
 *   4. How would you count subarrays divisible by k?
 *      Store frequencies of prefix-sum remainders instead of exact prefix sums.
 *
 * Related: Subarray Sums Divisible by K (974), Continuous Subarray Sum (523).
 */
public class SubsetSumEqualsK {
    public static void main(String[] args) {
        int[][] nums = { {1, 1, 1}, {1, -1, 0} };
        int[] targets = { 2, 0 };
        int[] expected = { 2, 3 };

        for (int i = 0; i < nums.length; i++) {
            int got = countSubarraysWithSum(nums[i], targets[i]);
            System.out.printf("nums=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(nums[i]), targets[i], got, expected[i]);
        }
    }

        /**
     * Intuition: if two prefix sums differ by k, the values between them sum to
     * k. While scanning, count how many earlier prefix sums equal currentSum - k;
     * each one gives a subarray ending at the current index.
     *
     * Algorithm:
     *   1. Seed prefixSumMap with sum 0 so subarrays starting at index 0 are counted.
     *   2. Add each number to currentSum and add prior occurrences of currentSum - k to the answer.
     *   3. Record the current prefix sum for future subarrays.
     *
     * Time:  O(n) - each number performs constant-time map work once.
     * Space: O(n) - the map may store one prefix sum per index.
     *
     * @param nums input array of integers
     * @param k target subarray sum
     * @return number of continuous subarrays whose sum equals k
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
