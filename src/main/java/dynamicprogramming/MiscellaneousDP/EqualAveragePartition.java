package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Equal Average Partition
 *
 * Given an integer array, decide whether it can be split into two non-empty parts
 * with the same average. The implementation returns only whether such a partition
 * exists, not the partition itself.
 *
 * Source: InterviewBit - Equal Average Partition
 * Pattern:  Dynamic programming | Subset size and sum | Memoized recursion
 *
 * Example:
 *   Input:  nums = [1,7,15,29,11,9]
 *   Output: true
 *   Why:    [9,15] and [1,7,11,29] both average to 12, so the array can be split.
 *
 * Follow-ups:
 *   1. Return the actual two subsets?
 *      Store parent choices while searching for the target subset size and sum.
 *   2. Handle negative numbers?
 *      Offset sums or use a set/map DP instead of indexing by non-negative sums.
 *   3. Partition into k equal-average subsets?
 *      Combine subset-sum DP with backtracking over groups; the problem becomes much harder.
 *
 * Related: Split Array With Same Average (805), Partition Equal Subset Sum (416).
 */
public class EqualAveragePartition {

    public static void main(String[] args) {
        EqualAveragePartition solver = new EqualAveragePartition();
        int[][] inputs = {{1, 3}, {1, 7, 15, 29, 11, 9}, {1, 2, 3}};
        boolean[] expected = {false, true, true};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.canPartitionWithEqualAverage(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: if one subset has size k and the same average as the whole array,
     * then its sum must be totalSum * k / n. So the problem becomes: is there any
     * subset of exactly k numbers with that exact integer sum? The DP state is the
     * current index, how many numbers still need to be chosen, and how much sum is
     * still needed. Each state either takes the current number or skips it, and
     * memoization makes those overlapping choices manageable.
     *
     * Algorithm:
     *   1. Compute totalSum and try every non-empty subsetSize smaller than totalLength.
     *   2. Only sizes with totalSum * subsetSize divisible by totalLength can work.
     *   3. Recursively choose or skip elements to find that exact targetSum and subsetSize.
     *   4. Return true on the first successful subset.
     *
     * Time:  O(n^2*sum) - there are states for index, chosen count, and reachable remaining sums.
     * Space: O(n^2*sum) - memoization stores those states, plus O(n) recursion depth.
     *
     * @param nums input array
     * @return true if the array can be split into two non-empty equal-average subsets
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
