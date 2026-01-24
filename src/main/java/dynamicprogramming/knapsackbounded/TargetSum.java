package dynamicprogramming.knapsackbounded;

import java.util.Arrays;

/**
 * Problem: Target Sum
 *
 * Problem Statement:
 * You are given an array of integers and a target sum.
 * You can assign either a '+' or '-' sign to each element.
 * Find the number of ways to assign symbols to make the sum equal to the target.
 *
 * Leetcode Equivalent: https://leetcode.com/problems/target-sum/
 *
 * Relation to 0/1 Knapsack:
 * - This problem is **almost identical** to "Count of Subsets with Given Difference".
 * - Think of '+' assigned elements as S1 and '-' assigned elements as S2.
 * - The problem reduces to:
 *      -> Find the number of subsets whose sum is (target + total_sum) / 2
 *      -> Same as Count of Subsets with Given Difference.
 *
 * Important Concept:
 * - Assume two groups S1 and S2, then:
 *     S1 - S2 = target
 *     S1 + S2 = total_sum
 *
 * Adding the equations:
 *     2S1 = target + total_sum
 *     S1 = (target + total_sum)/2
 *
 * - Hence, the problem reduces to **counting the number of subsets with sum = (target + total_sum)/2**.
 */
public class TargetSum {
    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3};
        int target = 1;
        System.out.println(findTargetSumWays(arr, target));
    }

    /**
     * Main method to find number of ways to assign '+' and '-' to achieve target sum.
     *
     * Intuition:
     * Reduce the Target Sum problem into a Subset Sum counting problem.
     * Find subsets with sum = (target + total_sum)/2.
     *
     * Approach:
     * - Calculate total sum of array.
     * - Check if (target + total_sum) is odd or negative; if yes, return 0.
     * - Otherwise, count subsets with sum = (target + total_sum)/2.
     *
     * Time Complexity: O(n * subsetSum)
     * Space Complexity: O(n * subsetSum)
     */
    public static int findTargetSumWays(int[] arr, int target) {
        int totalSum = Arrays.stream(arr).sum();

        // Check if (target + totalSum) is even and non-negative
        if (totalSum < Math.abs(target) || (target + totalSum) % 2 != 0) {
            return 0;
        }

        int subsetSum = (target + totalSum) / 2;
        return CountSubsetsWithGivenSum.countSubsetsIterative(arr, arr.length, subsetSum); // already solved
    }
}