package DynamicProgramming;

import java.util.Arrays;

/**
 * There are n coins (Assume n is even) in a line.
 * Two players take turns to take a coin from one of the ends of the line until there are no more coins left.
 * The player with the larger amount of money wins, Assume that you go first.
 * Return the maximum amount of money you can win.
 * https://www.interviewbit.com/problems/coins-in-a-line/
 */
public class CoinsInLine {
    public static void main(String[] args) {
        int[] coins = {5, 4, 8, 10};
        System.out.println(new CoinsInLine().maxCoin(coins));
    }

    public int maxCoin(int[] nums) {
        int size = nums.length - 1;
        int[][][] dp = new int[size+1][size+1][2];
        for(int[][] matrix: dp) {
            for(int[] row: matrix) {
                Arrays.fill(row, -1);
            }
        }
        return maxCoinRec(nums, 0, size, true, dp);
    }

    public int maxCoinRec(int[] nums, int start, int end, boolean playerOne, int[][][] dp) {
        if(start > end) return 0;
        if(start == end) {
            if(playerOne) return nums[start];
            else return 0;
        }
        int p1Int = playerOne == true ? 1 : 0;
        if(dp[start][end][p1Int] != -1) {
            return dp[start][end][p1Int];
        }
        if(playerOne) {
            dp[start][end][p1Int] = Math.max(nums[start] + maxCoinRec(nums, start+1, end, false, dp), nums[end] + maxCoinRec(nums, start, end-1, false, dp));
        }else{
            dp[start][end][p1Int] = Math.min(maxCoinRec(nums, start+1, end, true, dp), maxCoinRec(nums, start, end-1, true, dp));
        }
        return dp[start][end][p1Int];
    }

    /**
     * This is much optimum approach as we are not storing opponent moves
     * @param nums
     * @return
     */
    public int maxcoinOpt(int[] nums) {
        int[][] dp = new int[nums.length][nums.length];
        return maxCoinOptRec(nums, 0, nums.length - 1, dp);
    }

    public int maxCoinOptRec(int[] nums, int start, int end, int[][] dp) {
        if (start > end) {
            return 0;
        }
        if (dp[start][end] != 0) {
            return dp[start][end];
        }
        // playerOne takes start
        int left = nums[start] + Math.min(maxCoinOptRec(nums, start + 2, end, dp), maxCoinOptRec(nums, start + 1, end - 1, dp));
        // playerOne takes end
        int right = nums[end] + Math.min(maxCoinOptRec(nums, start + 1, end - 1, dp), maxCoinOptRec(nums, start, end - 2, dp));
        dp[start][end] = Math.max(left, right);
        return dp[start][end];
    }
}
