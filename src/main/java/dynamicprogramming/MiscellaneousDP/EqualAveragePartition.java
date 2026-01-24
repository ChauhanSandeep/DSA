package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Equal Average Partition
 * Link: https://www.interviewbit.com/problems/equal-average-partition/
 * 
 * Given an array of integers, partition the array into two non-empty subsets such that both subsets 
 * have the same average. Return any valid partition as a list of two lists.
 * If no such partition exists, return an empty list.
 *
 * The average of a subset is defined as the sum of elements divided by the number of elements.
 *
 * Example 1:
 * Input: A = [1, 7, 15, 29, 11, 9]
 * Output: [[9, 15], [1, 7, 11, 29]]
 * Explanation:
 * Subset 1: [9, 15] → average = (9+15)/2 = 24/2 = 12
 * Subset 2: [1, 7, 11, 29] → average = (1+7+11+29)/4 = 48/4 = 12
 * Both averages are equal.
 * 
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - How would you handle floating point precision issues?
 *    → Use integer arithmetic: avg1 = sum1/size1 equals avg2 = sum2/size2 becomes sum1*size2 = sum2*size1.
 *  - Can you extend this to partition into k subsets with equal average?
 *    → Much more complex; would need to try all combinations of subset sizes and use backtracking.
 *  - What if elements can be negative?
 *    → Algorithm still works but need to handle negative sums carefully in DP.
 *  - How would you find the lexicographically smallest partition?
 *    → Sort array first and process elements in order, preferring smaller elements in first subset.
 */
public class EqualAveragePartition {

    public static void main(String[] args) {
        int[] input = {1, 3};
        System.out.println(new EqualAveragePartition().canPartitionWithEqualAverage(input));
    }

    /**
     * Determines if array can be partitioned into two subsets with equal averages
     *
     * Steps:
     * - For equal averages: sum1/size1 = sum2/size2, which gives us
     *          sum1*size2 = sum2*size1
     * - Since sum1 + sum2 = totalSum and
     *          size1 + size2 = totalLength
     * - Therefore sum1 × (totalLength - size1) = (totalSum - sum1) × size1
     *      sum1 × totalLength - sum1 × size1 = totalSum × size1 - sum1 × size1
     *      sum1 × totalLength = totalSum × size1
     *      sum1 = (totalSum × size1) / totalLength
     * - For a valid partition to exist, sum1 must be an integer (since it's a sum of integers).
     * Therefore: (totalSum × size1) % totalLength == 0
     * - If we had to find the subarray, we could have simply used 2 pointers or sliding window,
     * but here we need to check for subsets of different sizes so we use recursion with memoization.
     *
     * Algorithm: Recursion with Memoization
     * Time Complexity: O(N * Sum * N) - visiting each element for each (size, sum) state
     * Space Complexity: O(Sum * N) for memoization + O(N) for recursion stack
     *
     * @param nums input array of integers
     * @return true if equal average partition exists, false otherwise
     */
    public boolean canPartitionWithEqualAverage(int[] nums) {
        if (nums == null || nums.length < 2) return false;

        int totalSum = Arrays.stream(nums).sum();
        int totalLength = nums.length;

        // Memoization map to store already computed results
        // Key: "remainingSize-remainingSum" (index not needed as we only move forward)
        Map<String, Boolean> memo = new HashMap<>();

        // Try different subset sizes
        for (int subsetSize = 1; subsetSize < totalLength; subsetSize++) {
            int mod = (totalSum * subsetSize) % totalLength; // as per the derived condition
            if (mod == 0) { // Ensures integer subset sum
                int targetSum = (totalSum * subsetSize) / totalLength;
                if (canFindSubset(nums, 0, subsetSize, targetSum, memo)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Recursive function to check if a subset of given size exists with the target sum.
     */
    private boolean canFindSubset(int[] nums, int index, int remainingSize, int remainingSum, Map<String, Boolean> memo) {
        // Base case: if subset size becomes zero, check if we reached the target sum
        if (remainingSize == 0) return remainingSum == 0;

        // If we've exhausted all elements or if target sum is negative, return false
        if (index >= nums.length || remainingSum < 0) return false;

        // Memoization key: only remainingSize and remainingSum matter
        // (index excluded because we only move forward, never revisit)
        String key = remainingSize + "-" + remainingSum;
        if (memo.containsKey(key)) return memo.get(key);

        // Choice 1: Include the current element
        boolean include = canFindSubset(nums, index + 1, remainingSize - 1, remainingSum - nums[index], memo);

        // Choice 2: Exclude the current element
        boolean exclude = canFindSubset(nums, index + 1, remainingSize, remainingSum, memo);

        // Store and return result
        memo.put(key, include || exclude);
        return memo.get(key);
    }
}
