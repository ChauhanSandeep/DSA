package Graph;

import java.util.Arrays;

/**
 * Find number of nodes from where water can flow to top-left and bottom-right boundary
 * water can only flow from higher number to lower number.
 *
 * https://www.interviewbit.com/problems/water-flow/
 */
public class WaterFlow {

    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 2, 3, 5},
                {3, 2, 3, 4, 4},
                {2, 4, 5, 3, 1},
                {6, 7, 1, 4, 5},
                {5, 1, 1, 2, 4},
        };
        System.out.println(new WaterFlow().solve(grid));
    }

    /**
     * [[false, false, false, false, true],
     * [false, false, false, true, true],
     * [false, false, false, true, true],
     * [true, true, false, true, true],
     * [true, true, true, true, true]]
     *
     * [[true, true, true, true, true],
     * [true, false, false, false, false],
     * [true, false, false, false, false],
     * [true, false, false, false, false],
     * [true, false, false, false, false]]
     */

    public int solve(int[][] arr) {
        int first = 0;
        Boolean[][] visited1 = new Boolean[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                touchBottomRight(arr, i, j, visited1);
            }
        }
        System.out.println(Arrays.deepToString(visited1));

        Boolean[][] visited2 = new Boolean[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                touchTopLeft(arr, i, j, visited2);
            }
        }
        System.out.println(Arrays.deepToString(visited2));

        int diff = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (visited1[i][j] && visited2[i][j]) {
                    System.out.println("i->" +i + "  j->" + j);
                    diff++;
                }
            }
        }
        return diff;
    }

    private int touchBottomRight(int[][] arr, int i, int j, Boolean[][] visited) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[i].length || visited[i][j] != null) return 0;

        if (i == arr.length - 1 || j == arr[i].length - 1) {
            visited[i][j] = true;
            return 1;
        }

        visited[i][j] = true;
        int down = isValid(arr, i, j, i + 1, j) ? touchBottomRight(arr, i + 1, j, visited) : 0;
        int up = isValid(arr, i, j, i - 1, j) ? touchBottomRight(arr, i - 1, j, visited) : 0;
        int left = isValid(arr, i, j, i, j - 1) ? touchBottomRight(arr, i, j - 1, visited) : 0;
        int right = isValid(arr, i, j, i, j + 1) ? touchBottomRight(arr, i, j + 1, visited) : 0;

        if (up > 0 || down > 0 || left > 0 || right > 0) return 1 + up + down + left + right;
        visited[i][j] = false;
        return 0;
    }

    private int touchTopLeft(int[][] arr, int i, int j, Boolean[][] visited) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[i].length || visited[i][j] != null) return 0;

        if (i == 0 || j == 0) {
            visited[i][j] = true;
            return 1;
        }
        visited[i][j] = true;
        int down = isValid(arr, i, j, i + 1, j) ? touchTopLeft(arr, i + 1, j, visited) : 0;
        int up = isValid(arr, i, j, i - 1, j) ? touchTopLeft(arr, i - 1, j, visited) : 0;
        int left = isValid(arr, i, j, i, j - 1) ? touchTopLeft(arr, i, j - 1, visited) : 0;
        int right = isValid(arr, i, j, i, j + 1) ? touchTopLeft(arr, i, j + 1, visited) : 0;

        if (up > 0 || down > 0 || left > 0 || right > 0) return 1 + up + down + left + right;
        visited[i][j] = false;
        return 0;
    }

    private boolean isValid(int[][] arr, int i1, int j1, int i, int j) {
        if (i < 0 || i >= arr.length || j < 0 || j >= arr[i].length) return false;
        return arr[i1][j1] > arr[i][j];
    }
}
