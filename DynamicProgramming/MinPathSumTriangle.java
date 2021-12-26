package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a triangle, find the minimum path sum from top to bottom.
 * Each step you may move to adjacent numbers on the row below.
 */
public class MinPathSumTriangle {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> lists = new ArrayList<>();
        lists.add((ArrayList<Integer>) Stream.of(2).collect(Collectors.toList()));
        lists.add((ArrayList<Integer>) Stream.of(3 ,4).collect(Collectors.toList()));
        lists.add((ArrayList<Integer>) Stream.of(6, 5, 7).collect(Collectors.toList()));
        lists.add((ArrayList<Integer>) Stream.of(4, 1, 8, 3).collect(Collectors.toList()));
        System.out.println("Min total to reach bottom row of triangle : "
                + new MinPathSumTriangle().minimumTotalItr(lists));
    }

    int rows;
    /**
     * Recursive Approach
     */
    public int minimumTotal(ArrayList<ArrayList<Integer>> lists) {
        this.rows = lists.size();
        int[][] dp = new int[rows+1][rows+1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }
        return findMin(lists, 0, 0, dp);
    }

    public int findMin(ArrayList<ArrayList<Integer>> lists, int i, int j, int[][] dp) {
        if(i >= rows || j>i) return Integer.MAX_VALUE;
        if(i == rows-1) return lists.get(i).get(j);
        if(dp[i][j] != -1) return dp[i][j];

        int down = findMin(lists, i+1, j, dp);
        int diagonal = findMin(lists, i+1, j+1, dp);// diagonal

        if(down == Integer.MAX_VALUE && diagonal == Integer.MAX_VALUE) {
            dp[i][j] = Integer.MAX_VALUE;
        } else{
            dp[i][j] = Math.min(down, diagonal) + lists.get(i).get(j);
        }
        return dp[i][j];
    }

    /**
     * Iterative approach
     */
    public int minimumTotalItr(ArrayList<ArrayList<Integer>> lists) {
        int size = lists.size();
        int dp[][] = new int[size][size];

        dp[0][0] = lists.get(0).get(0);
        for (int i = 1; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                dp[i][j] = lists.get(i).get(j);
                if (j == 0) {
                    dp[i][j] += dp[i - 1][j];
                } else if (j == i) {
                    dp[i][j] += dp[i - 1][j - 1];
                } else {
                    dp[i][j] += Math.min(dp[i - 1][j], dp[i - 1][j - 1]);
                }
            }
        }
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            result = Math.min(result, dp[size - 1][i]);
        }
        return result;
    }
}
