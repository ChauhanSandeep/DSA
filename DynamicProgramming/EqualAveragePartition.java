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
     * Time Complexity: O(N * Sum * N) = O(N^2 * Sum) where N is array length
     * Space Complexity: O(N * Sum * N) for memoization + O(N) for recursion stack
     *
     * @param nums input array of integers
     * @return true if equal average partition exists, false otherwise
     */
    public boolean canPartitionWithEqualAverage(int[] nums) {
        if (nums == null || nums.length < 2) return false;

        int totalSum = 0;
        int totalLength = nums.length;

        for (int num : nums) {
            totalSum += num;
        }

        // Memoization map to store already computed results
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
