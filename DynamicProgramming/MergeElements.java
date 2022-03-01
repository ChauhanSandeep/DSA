package DynamicProgramming;

/**
 * Given an integer array A of size N. You have to merge all the elements of the array into one with the minimum possible cost.
 *
 * The rule for merging is as follows:
 * Choose any two adjacent elements of the array with values say X and Y and merge them into a single element with value (X + Y) paying a total cost of (X + Y).
 * Return the minimum possible cost of merging all elements.
 */
public class MergeElements {

    public static void main(String[] args) {
        int[] input = {1,2,3,4};

        System.out.println(new MergeElements().solve(input));
    }

    /**
     *  dp[i][j] = min(sum[i][j] + dp[i][k] + dp[k + 1][j]) (i <= k < j)
     *  dp[i][k] gives min cost of merging elements from i to k
     *  dp[k+1][j] gives min cost of merging elements from k+1 to j
     *  sum[i][j] sum of elements from i to j. This is required as this
     *  cost will be incurred again to merge two stones [i][k] and [k+1][j]
     */
    public int solve(int[] stones) {
        if (stones == null || stones.length == 0) {
            return 0;
        }
        int len = stones.length;
        int max = Integer.MAX_VALUE;

        int[][] dp = new int[len + 1][len + 1];
        int[] prefixSum = new int[len + 1];
        int i, j, k, currLen;

        for (i = 1; i <= len; i++) {
            prefixSum[i] = prefixSum[i - 1] + stones[i - 1];
        }

        for (i = 1; i <= len; i++) {
            dp[i][i] = 0;
        }

        for (currLen = 2; currLen <= len; currLen++) {
            for (i = 1; i <= len - currLen + 1; i++) {
                j = i + currLen - 1;
                System.out.println("i: " + i + " j:"+j);
                dp[i][j] = max;
                int sum = prefixSum[j] - prefixSum[i - 1];
                for (k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + sum);
                }
            }
        }

        return dp[1][len];
    }
}
