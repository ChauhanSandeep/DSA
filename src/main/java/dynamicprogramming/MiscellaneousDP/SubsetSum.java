package dynamicprogramming.MiscellaneousDP;

import java.util.Arrays;

/**
 * Problem: Subset Sum
 *
 * Given positive integers and a target sum, decide whether some subset adds up
 * exactly to the target. Each number can be used at most once.
 *
 * Pattern:  Dynamic programming | 0/1 knapsack | Boolean reachability
 *
 * Example:
 *   Input:  nums = [3, 34, 4, 12, 5, 2], target = 9
 *   Output: true
 *   Why:    the subset [4,5] reaches 9, so at least one valid subset exists.
 *
 * Follow-ups:
 *   1. Return the subset itself?
 *      Store parent choices or walk backward through the DP table after finding true.
 *   2. Count how many subsets reach the target?
 *      Change boolean states into integer counts and add include/exclude counts.
 *   3. Optimize space?
 *      Use a 1-D boolean array and scan sums backward for each number.
 *
 * Related: Partition Equal Subset Sum (416), Target Sum (494), Coin Change (322).
 */
public class SubsetSum {

    public static void main(String[] args) {
        int[][] inputs = {{}, {1, 4, 5}, {3, 34, 4, 12, 5, 2}};
        int[] targets = {0, 7, 9};
        boolean[] expected = {true, false, true};

        for (int i = 0; i < inputs.length; i++) {
            boolean got = findSubsetSumItr(inputs[i], targets[i], inputs[i].length);
            System.out.printf("nums=%s target=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), targets[i], got, expected[i]);
        }
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
     * Intuition: dp[itemCount][currentSum] answers whether the first itemCount
     * numbers can make currentSum. For each number, every target sum has two
     * possibilities: ignore the number, or take it and ask whether the remaining
     * sum was possible before this number existed. Building the table row by row
     * enforces the 0/1 rule because each row only reads from the previous row.
     *
     * Algorithm:
     *   1. Create dp[i][j] for whether the first i elements can make sum j.
     *   2. Set every dp[i][0] to true because sum 0 needs no elements.
     *   3. For each element and currentSum, either skip the element or take it if it fits.
     *   4. Return dp[size][sum].
     *
     * Time:  O(n*target) - every table cell is filled once with O(1) work.
     * Space: O(n*target) - the table stores reachability for every prefix and sum.
     *
     * @param arr positive input numbers
     * @param sum target sum to form
     * @param size number of prefix elements to consider
     * @return true if some subset sums to target
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
