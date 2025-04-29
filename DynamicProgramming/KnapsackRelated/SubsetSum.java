package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Problem: Subset Sum
 *
 * Problem Statement:
 * Given an array of positive integers and a target sum, determine whether there exists a subset
 * whose sum equals the target.
 *
 * Leetcode Equivalent: https://leetcode.com/problems/partition-equal-subset-sum/ (related)
 *
 * Relation to 0/1 Knapsack:
 * - This is a special case of 0/1 Knapsack where:
 *     -> Each item’s "value" is same as its "weight".
 *     -> We just need to check if it's **possible** to achieve a sum (not maximize it).
 */
public class SubsetSum {
    public static void main(String[] args) {
        int[] arr = {1, 4, 5};
        int sum = 5;
        System.out.println(findSubsetSumItr(arr, sum, arr.length));
    }

    /**
     * Recursive Approach (with memoization):
     *
     * Intuition:
     * Try including or excluding each number. If we can make the target from any path, return true.
     *
     * Approach:
     * - Base Case: If target is 0, subset is always possible (empty set).
     * - If index is 0, check if nums[0] == target.
     * - Use memoization to store results of subproblems.
     *
     * Time Complexity: O(n * sum)
     * Space Complexity: O(n * sum) for memo + O(n) recursion stack
     */
    public static boolean findSubsetHelper(int[] arr, int sum, int size) {
        Boolean[][] dp = new Boolean[size+1] [sum+1]; // dp[i][j] signifies that we can make sum j using first i elements
        Arrays.fill(dp[0], false); // Base case: no elements means no sum
        for(int i=0; i<size+1; i++) {
            dp[i][0] = true; // Base case: sum 0 is always possible (empty subset)
        }
        findSubsetSumDp(arr, sum, size, dp);
        System.out.println(Arrays.deepToString(dp));
        return dp[size][sum];
    }

    private static boolean findSubsetSumDp(int[] arr, int sum, int index, Boolean[][] dp) {
        if(sum == 0) return true;
        if(index ==0) return false;
        if(dp[index][sum] != null) {
            return dp[index][sum];
        }

        boolean result = false;
        if(arr[index-1] <= sum) {
            result = findSubsetSumDp(arr, sum-arr[index-1], index-1, dp) ||
                    findSubsetSumDp(arr, sum, index-1, dp);
        }else{
            result = findSubsetSumDp(arr, sum, index-1, dp);
        }
        dp[index][sum] = result;
        return result;
    }

    /**
     * Iterative Tabulation Approach:
     *
     * Intuition:
     * dp[elementIndex][currentSum] means: Is it possible to make sum 'currentSum' using the first 'elementIndex' elements?
     * Build the solution from smaller subproblems.
     *
     * Approach:
     * - dp[i][0] = true (sum 0 is always possible)
     * - Fill dp[elementIndex][currentSum] = dp[elementIndex-1][currentSum] || dp[elementIndex-1][currentSum - arr[elementIndex-1]] if arr[elementIndex-1] <= currentSum
     *
     * Time Complexity: O(size * sum)
     * Space Complexity: O(size * sum)
     */
    public static boolean findSubsetSumItr(int[] arr, int sum, int size) {
        boolean[][] dp = new boolean[size+1][sum+1]; // dp[i][j] signifies that we can make sum j using first i elements

        for(int i=0; i<size+1; i++) {
            dp[i][0] = true;
        }

        for (int elementIndex = 1; elementIndex < size + 1; elementIndex++) {
            for (int currentSum = 1; currentSum < sum + 1; currentSum++) {
                if(arr[elementIndex-1] <= currentSum) { // ensuring that we are not going out of bounds
                    dp[elementIndex][currentSum] = dp[elementIndex-1][currentSum]  // skip the current element and the current target is possible in previous index
                            || dp[elementIndex-1][currentSum-arr[elementIndex-1]]; // take the current element and currentSum-currentValue is possible in previous index
                } else {
                    dp[elementIndex][currentSum] = dp[elementIndex - 1][currentSum];
                }
            }
        }
        return dp[size][sum];
    }


}
