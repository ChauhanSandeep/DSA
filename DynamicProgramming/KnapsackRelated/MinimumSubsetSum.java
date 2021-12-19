package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Find two subsets with minimum sum.
 * Return the difference between two subsets
 * Input: {1, 6, 11, 5}
 * output: 1
 * Exp: {1, 5, 6} - {11} = 1
 */
public class MinimumSubsetSum {
    public static void main(String[] args) {
        int[] arr = {1, 6, 11, 5};
        int minSum = findMinSubsetSum(arr, arr.length);
        System.out.println(minSum);
    }

    public static int findMinSubsetSum(int[] arr, int size) {
        int sum = Arrays.stream(arr).sum();

        int halfSum = sum/2;
        boolean[][] dp = new boolean[size+1][halfSum+1];
        for(int i=0; i<=size; i++) {
            dp[i][0] = true;
        }

        for(int i=1; i<=size; i++) {
            for(int j=1; j<=halfSum; j++) {
                int currSum = j;
                int currVal = arr[i-1];
                if(currSum >= currVal) {
                    dp[i][j] = dp[i-1][j] || dp[i-1][currSum - currVal];
                }else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }

         /*
            s2 - s1 = min
            (sum - s1) - s1 = min
            sum - 2s1 = min
            s1 is smaller subset so it would be found on the lower half side of the array
            first subset sum on the lower side is ans
         */
        int s1 = halfSum;
        while(s1 >=0 && dp[size][s1] == false) {
            s1--;
        }
        return sum - 2 * s1;
    }
}
