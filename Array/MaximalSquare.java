package Array;

/**
 * Find maximum dimension square of 1s
 *
 * https://leetcode.com/problems/maximal-square/
 */
public class MaximalSquare {
    public static void main(String[] args) {
        int[][] arr = {
                {0, 1, 1, 0, 1},
                {1, 1, 0, 1, 0},
                {0, 1, 1, 1, 0},
                {1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0}
        };
        System.out.println(new MaximalSquare().solve(arr));
    }

    public int solve(int[][] arr) {
        int result = 0;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (i != 0 && j != 0 && arr[i][j] == 1) {
                    int tentative = Math.min(arr[i - 1][j - 1], Math.min(arr[i - 1][j], arr[i][j - 1])) + 1;
                    arr[i][j] = tentative;
                }
                result = Math.max(result, arr[i][j]);
            }
        }
        return result * result;
    }
}
