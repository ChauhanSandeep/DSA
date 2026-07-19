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

    public static void main(String[] args) {
        int[][] numsCases = { {1, 1, 1, 1, 1}, {} };
        int[] targets = { 3, 0 };
        String[] expected = { "original file has no implementation", "original file has no implementation" };

        for (int i = 0; i < numsCases.length; i++) {
            String got = "original file has no implementation";
            System.out.printf("nums=%s target=%d -> %s  expected=%s%n",
                Arrays.toString(numsCases[i]), targets[i], got, expected[i]);
        }
    }
    
}
