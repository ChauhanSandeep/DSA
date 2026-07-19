package arrays.slidingwindow;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Problem: Subarray Sums Divisible by K
 *
 * Count non-empty contiguous subarrays whose sum is divisible by k. If two
 * prefix sums have the same remainder modulo k, the subarray between them has a
 * sum divisible by k.
 *
 * Leetcode: https://leetcode.com/problems/subarray-sums-divisible-by-k/ (Medium)
 * Rating:   acceptance 55.6% (Medium) - contest rating 1676
 * Pattern:  Prefix sum | Remainder frequency map
 *
 * Example:
 *   Input:  nums = [4,5,0,-2,-3,1], k = 5
 *   Output: 7
 *   Why:    seven subarrays have prefix remainders that match across their ends.
 *
 * Follow-ups:
 *   1. Return the actual subarrays?
 *      Store indices per remainder and emit each matching pair, which can be O(n^2).
 *   2. What if nums contains negative values?
 *      Normalize remainders with (sum % k + k) % k.
 *   3. What if k changes across many queries?
 *      Preprocessing is hard; answer each k with its own remainder count pass.
 *
 * Related: Continuous Subarray Sum (523), Subarray Sum Equals K (560).
 */
public class SubarraySumsDivisibleByK {

    public static void main(String[] args) {
        SubarraySumsDivisibleByK solver = new SubarraySumsDivisibleByK();
        int[][] nums = {{4, 5, 0, -2, -3, 1}, {5}, {1, 2, 3}};
        int[] divisors = {5, 9, 3};
        int[] expected = {7, 0, 3};

        for (int i = 0; i < nums.length; i++) {
            int got = solver.subarraysDivByK(nums[i], divisors[i]);
            System.out.printf("nums=%s k=%d -> %d  expected=%d%n",
                Arrays.toString(nums[i]), divisors[i], got, expected[i]);
        }
    }

    /**
     * Intuition: a subarray sum is divisible by k when the prefix sums before and
     * after it have the same remainder. For each new prefix remainder, every
     * previous occurrence of that remainder forms one valid subarray ending here.
     *
     * Algorithm:
     *   1. Seed remainder 0 with frequency 1 for subarrays starting at index 0.
     *   2. Scan nums, update prefixSum, and normalize its remainder modulo k.
     *   3. Add the previous frequency of that remainder, then record the new prefix.
     *
     * Time:  O(n) - one pass over nums.
     * Space: O(k) - at most k different remainders are stored.
     *
     * @param nums input array
     * @param k divisor for subarray sums
     * @return number of non-empty subarrays whose sum is divisible by k
     */
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
