package dynamicprogramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Target Sum
 *
 * Given an integer array and a target value, assign either a plus or minus sign
 * before every number so the signed expression evaluates to the target. Return
 * how many sign assignments are possible.
 *
 * Leetcode: https://leetcode.com/problems/target-sum/ (Medium)
 * Rating:   acceptance 52.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | 0/1 knapsack counting | Signed subset sum
 *
 * Example:
 *   Input:  nums = [1,1,1,1,1], target = 3
 *   Output: 5
 *   Why:    exactly one of the five 1s must be negative, and there are five
 *           choices for that negative sign.
 *
 * Follow-ups:
 *   1. How do you reduce this to subset-sum counting?
 *      Let positive sum be P and negative sum be N; P - N = target and P + N = total.
 *   2. What if nums contains zeros?
 *      Each zero doubles the count because +0 and -0 are distinct assignments.
 *   3. How would you list the actual expressions?
 *      Backtrack over signs and collect only paths whose final sum equals target.
 *
 * Related: Partition Equal Subset Sum (416), Ones and Zeroes (474), Coin Change II (518).
 */
public class TargetSum {

    /**
     * Intuition: choosing plus signs for subset P and minus signs for the rest N
     * gives sum(P) - sum(N) = target and sum(P) + sum(N) = total. Therefore P
     * must sum to (total + target) / 2, so the problem becomes counting subsets
     * with that transformed sum.
     *
     * Algorithm:
     *   1. Sum nums and reject impossible target or parity combinations.
     *   2. Seed one way to make subset sum 0: choose nothing.
     *   3. For each number, update subset counts backward so each item is used once.
     *   4. Return the count for the transformed subset sum.
     *
     * Time:  O(n * subsetSum) - each item updates every reachable subset sum once.
     * Space: O(subsetSum) - one rolling count array stores all sums.
     *
     * @param nums input values
     * @param target desired signed sum
     * @return number of plus/minus assignments that reach target
     */
    public static int findTargetSumWays(int[] nums, int target) {
        if (nums == null) return 0;

        int total = 0;
        for (int num : nums) {
            total += num;
        }

        int transformedSum = total + target;
        if (total < Math.abs(target) || (transformedSum & 1) == 1) {
            return 0;
        }

        int subsetSum = transformedSum / 2;
        int[] subsetCounts = new int[subsetSum + 1];
        subsetCounts[0] = 1;

        for (int num : nums) {
            for (int currentSum = subsetSum; currentSum >= num; currentSum--) {
                subsetCounts[currentSum] += subsetCounts[currentSum - num];
            }
        }

        return subsetCounts[subsetSum];
    }

    public static void main(String[] args) {
        int[][] numsCases = { {1, 1, 1, 1, 1}, {}, {0, 0, 1} };
        int[] targets = { 3, 0, 1 };
        int[] expected = { 5, 1, 4 };

        for (int i = 0; i < numsCases.length; i++) {
            int got = findTargetSumWays(numsCases[i], targets[i]);
            System.out.printf("nums=%s target=%d -> %d  expected=%d%n",
                Arrays.toString(numsCases[i]), targets[i], got, expected[i]);
        }
    }
    
}
