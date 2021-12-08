package DynamicProgramming;

import java.util.Arrays;

/**
 * Given an array of  positive elements, you have to flip the sign of some of its elements
 * such that the resultant sum of the elements of array should be minimum
 * non-negative(as close to zero as possible).
 * Return the minimum no. of elements whose sign needs to be flipped such
 * that the resultant sum is minimum non-negative.
 */
public class FlipArray {
    public static void main(String[] args) {
        int[] arr = {8, 4, 5, 7, 6, 2};
        int result = new FlipArray().solve(arr);
        System.out.println(result);
    }

    public int solve(final int[] arr) {
        int sum = 0;
        int size = arr.length;
        for (int i = 0; i < size; i++) {
            sum += arr[i];
        }
        int[] dp = new int[2 * sum + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);

        dp[2 * sum] = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= 2 * sum; j++) {
                if (dp[j] != Integer.MAX_VALUE) {
                    dp[j - 2 * arr[i]] = Math.min(dp[j] + 1, dp[j - 2 * arr[i]]);
                }
            }
        }
        for (int i = sum; i < 2 * sum + 1; i++) {
            if (dp[i] != Integer.MAX_VALUE) {
                return dp[i];
            }
        }
        return 0;
    }
}
