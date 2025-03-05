package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Equal Average Partition
 * Link: https://www.interviewbit.com/problems/equal-average-partition/
 *
 * Approach:
 * - The problem is a variation of the subset sum problem where we need to check if we can split
 *   an array into two non-empty subsets such that their averages are equal.
 * - If the total sum is `S` and the size is `N`, the target subset should have an average `S/N`.
 * - We use recursion with memoization to check if a subset exists with a sum of `target * subsetSize`.
 *
 * Time Complexity: O(N * Sum) -> Exponential in worst case due to recursion, but optimized using memoization.
 * Space Complexity: O(N * Sum) -> Due to recursion stack and memoization storage.
 */
public class EqualAveragePartition {

    public static void main(String[] args) {
        int[] input = {1, 3};
        System.out.println(new EqualAveragePartition().canPartitionWithEqualAverage(input));
    }

    /**
     * Determines if the array can be split into two non-empty subsets with the same average.
     */
    public boolean canPartitionWithEqualAverage(int[] nums) {
        if (nums == null || nums.length < 2) return false;

        int totalSum = 0;
        int n = nums.length;

        for (int num : nums) {
            totalSum += num;
        }

        // Memoization map to store already computed results
        Map<String, Boolean> memo = new HashMap<>();

        // Try different subset sizes
        for (int subsetSize = 1; subsetSize < n; subsetSize++) {
            if ((totalSum * subsetSize) % n == 0) { // Ensures integer subset sum
                int targetSum = (totalSum * subsetSize) / n;
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
    private boolean canFindSubset(int[] nums, int index, int subsetSize, int targetSum, Map<String, Boolean> memo) {
        // Base case: if subset size becomes zero, check if we reached the target sum
        if (subsetSize == 0) return targetSum == 0;

        // If we've exhausted all elements or if target sum is negative, return false
        if (index >= nums.length || targetSum < 0) return false;

        // Memoization key
        String key = index + "-" + subsetSize + "-" + targetSum;
        if (memo.containsKey(key)) return memo.get(key);

        // Choice 1: Include the current element
        boolean include = canFindSubset(nums, index + 1, subsetSize - 1, targetSum - nums[index], memo);

        // Choice 2: Exclude the current element
        boolean exclude = canFindSubset(nums, index + 1, subsetSize, targetSum, memo);

        // Store and return result
        memo.put(key, include || exclude);
        return memo.get(key);
    }
}
