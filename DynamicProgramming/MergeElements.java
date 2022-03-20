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
        if(stones == null || stones.length == 0) return 0;
        int len = stones.length;

        int[] prefixSum = new int[len+1];
        int[][] dp = new int[len+1][len+1];

        for(int i=1; i<len+1; i++) {
            prefixSum[i] = stones[i-1] + prefixSum[i-1];
        }
        for(int i=0; i<len+1; i++) {
            dp[i][i] = 0;
        }

        for(int currLen = 2; currLen<=len; currLen++) {
            for(int i=1; i<len+1; i++) {
                int j = i+currLen-1;
                if(j>=len+1) continue;
                dp[i][j] = Integer.MAX_VALUE;
                int sum = prefixSum[j] - prefixSum[i-1];
                for(int k=i; k<j; k++) {
                    /* Example
                    dp[2][4] = min(
                        dp[2][2] + dp[3][4] + sum,
                        dp[2][3] + dp[4][4] + sum
                    )
                     */
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k+1][j] + sum);
                }
            }
        }
        return dp[1][len];
    }
}
