package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * Find two subsets with minimum sum
 */
public class MinimumSubsetSum {
    public static void main(String[] args) {
        int[] arr = {1, 6, 11, 5};
        int minSum = findMinSubsetSum(arr, arr.length);
        System.out.println(minSum);
    }

    public static int findMinSubsetSum(int[] arr, int size) {
        int sum = Arrays.stream(arr).sum();

        boolean[][] dp = new boolean[size+1][sum+1];
        for(int i=0; i<size+1; i++) {
            dp[i][0] = true;
        }

        for(int i=1; i<size+1; i++) {
            for(int j=1; j<sum+1; j++) {
                if(arr[i-1] <= j) {
                    dp[i][j] = dp[i-1][j] || dp[i-1][j-arr[i-1]];
                }else{
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
        int s1 = sum/2;
        while(s1 >=0 && !dp[size][s1]) {
            s1--;
        }
        return sum - 2*s1;
    }
}
