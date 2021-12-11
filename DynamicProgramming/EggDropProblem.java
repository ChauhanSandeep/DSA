package DynamicProgramming;

import java.util.Arrays;

public class EggDropProblem {

    public static void main(String[] args) {
        System.out.println(new EggDropProblem().superEggDrop(2, 10));
    }

    /**
     * Recursive approach
     * Throwing stackoverflow error in Interviewbit
     * @param e eggs provided
     * @param f number of floors
     * @return min trials required to find threshold floor
     */
    public int minTrials(int e, int f) {
        int[][] dp = new int[e+1][f+1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }
        return minTrialsRec(e, f, dp);
    }

    public int minTrialsRec(int e, int f, int[][] dp) {
        if(f == 1 || f == 0) {
            return f;
        }
        if(e == 1) {
            return f;
        }
        if(dp[e][f] != -1) return dp[e][f];
        int min = Integer.MAX_VALUE;
        for(int x=1; x<=f; x++) {
            int temp = 1 + Math.max(minTrialsRec(e-1, x-1, dp), minTrialsRec(e, f-x, dp));
            min = Math.min(min, temp);
        }
        dp[e][f] = min;
        return dp[e][f];
    }

    /**
     * Iterative solution.
     * Throws time complexity error in Interviewbit
     * @param e eggs provided
     * @param f number of floors
     * @return min trials required to find threshold floor
     */
    static int minTrialsIter(int e, int f) {
        int dp[][] = new int[e + 1][f + 1];

        for (int i = 1; i <= e; i++) {
            dp[i][1] = 1; // 1 trial required for 1 floors
            dp[i][0] = 0; // 0 trial required for 0 floors
        }
        for (int i = 1; i <= f; i++) {
            dp[1][i] = i; // if only 1 egg is given then it would take `no. of floors` trials
        }

        for (int e1 = 2; e1 <= e; e1++) {
            for (int f1 = 2; f1 <= f; f1++) {
                dp[e1][f1] = Integer.MAX_VALUE;
                for (int x = 1; x <= f1; x++) {
                    int temp = 1 + Math.max(dp[e1 - 1][x - 1], dp[e1][f1 - x]);
                    if (temp < dp[e1][f1]) {
                        dp[e1][f1] = temp;
                    }
                }
            }
        }
        return dp[e][f];
    }

    /**
     * Iterative solution.
     * This is most optimized
     * @param e eggs provided
     * @param f number of floors
     * @return min trials required to find threshold floor
     */
    public int superEggDrop(int e, int f) {
        int[][] dp = new int[e + 1][f + 1];

        for (int i = 1; i <= e; i++) {
            dp[i][1] = 1; // 1 trial required for 1 floors
            dp[i][0] = 0; // 0 trial required for 0 floors
        }

        for (int i = 1; i <= f; i++) {
            dp[1][i] = i;
        }
        for (int e1 = 2; e1 <= e; e1++) {
            for (int f1 = 1; f1 <= f; f1++) {
                int min = Integer.MAX_VALUE;
                int left = 1;
                int right = f1;
                while (left <= right) {
                    int mid = left + (right - left) / 2;
                    int a = dp[e1 - 1][mid - 1]; // break
                    int b = dp[e1][f1 - mid]; // did not break
                    min = Math.min(min, Math.max(a, b) + 1);
                    if (a == b) break;
                    if (a < b) left = mid + 1;
                    else right = mid - 1;
                }
                dp[e1][f1] = min;
            }
        }
        return dp[e][f];
    }
}
