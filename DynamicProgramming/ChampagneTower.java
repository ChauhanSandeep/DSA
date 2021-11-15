package DynamicProgramming;

/**
 * https://leetcode.com/problems/champagne-tower/
 */
public class ChampagneTower {

    public static void main(String[] args) {
        double value = new ChampagneTower().champagneTower(3, 3, 1);
        System.out.println(value);
    }

    public double champagneTower(int poured, int glassRow, int glassCol) {
        double[][] dp = new double[4][4];
        dp[0][0] = poured;
        for (int row = 0; row <= glassRow; ++row) {
            for (int col = 0; col <= row; ++col) {
                double extra = dp[row][col] - 1.0;
                if (extra > 0) { // extra to next level
                    dp[row + 1][col] += extra/2.0;
                    dp[row + 1][col + 1] += extra/2.0;
                }
            }
        }
        return Math.min(1, dp[glassRow][glassCol]);
    }
}
