package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Given an array and number, find if there is any subset which sums up to the number
 * Not needed to be subarray
 */
public class SubsetSum {
    public static void main(String[] args) {
        int[] arr = {1, 4, 5};
        int sum = 5;
        System.out.println(findSubsetSumItr(arr, sum, arr.length));
    }

    /**
    Recursive
     */
    public static boolean findSubsetSum(int[] arr, int sum, int size) {
        if(sum == 0) return true;
        if(size ==0) return false;

        if (arr[size - 1] <= sum) {
            return findSubsetSum(arr, sum - arr[size - 1], size - 1) ||
                    findSubsetSum(arr, sum, size - 1);
        }else{
            return findSubsetSum(arr, sum, size-1);
        }
    }

    /**
    Recursive + Memoization
     */
    public static boolean findSubsetHelper(int[] arr, int sum, int size) {
        Boolean[][] dp = new Boolean[size+1] [sum+1];
        Arrays.fill(dp[0], false);
        for(int i=0; i<size+1; i++) {
            dp[i][0] = true;
        }
        findSubsetSumDp(arr, sum, size, dp);
        System.out.println(Arrays.deepToString(dp));
        return dp[size][sum];
    }

    private static boolean findSubsetSumDp(int[] arr, int sum, int size, Boolean[][] dp) {
        if(sum == 0) return true;
        if(size ==0) return false;
        if(dp[size][sum] != null) {
            return dp[size][sum];
        }

        boolean result = false;
        if(arr[size-1] <= sum) {
            result = findSubsetSumDp(arr, sum-arr[size-1], size-1, dp) ||
                    findSubsetSumDp(arr, sum, size-1, dp);
        }else{
            result = findSubsetSumDp(arr, sum, size-1, dp);
        }
        dp[size][sum] = result;
        return result;
    }

    /**
     * Iterative
     */
    public static boolean findSubsetSumItr(int[] arr, int sum, int size) {
        boolean[][] dp = new boolean[size+1][sum+1];

        for(int i=0; i<size+1; i++) {
            dp[i][0] = true;
        }

        for(int i=1; i<size+1; i++) {
            for(int j=1; j<sum+1; j++) {            // j is current sum
                if(arr[i-1] <= j) {                 // this is j, remember
                    dp[i][j] = dp[i-1][j]           // same sum with previous index
                            || dp[i-1][j-arr[i-1]]; // sum-current in previous index
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[size][sum];
    }


}
