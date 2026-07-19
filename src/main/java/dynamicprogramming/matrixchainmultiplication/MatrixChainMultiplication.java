package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Problem: Matrix Chain Multiplication
 *
 * Given matrix dimensions in an array where matrix i has size arr[i-1] x arr[i],
 * choose the parenthesization that minimizes scalar multiplications. The matrix
 * order cannot change, only where we put parentheses.
 *
 * Source: GeeksForGeeks Matrix Chain Multiplication
 * Pattern:  Dynamic Programming | Interval DP | Split on every partition point
 *
 * Example:
 *   Input:  arr = [2,1,3,4]
 *   Output: 20
 *   Why:    M1 x (M2 x M3) costs 1*3*4 + 2*1*4 = 20, which beats doing
 *           (M1 x M2) first for total cost 30.
 *
 * Follow-ups:
 *   1. Can you print the optimal parenthesization?
 *      Store the best split point for each interval and recursively rebuild the string.
 *   2. What if multiplication costs vary by hardware or matrix sparsity?
 *      Replace the merge-cost formula with the real cost model; the interval DP shape stays the same.
 *   3. Can independent subchains be evaluated in parallel?
 *      Yes after the optimal split tree is known, but DP table dependencies still require increasing lengths.
 *
 * Related: Burst Balloons (312), Minimum Score Triangulation of Polygon (1039).
 */
public class MatrixChainMultiplication {

    /**
   * Intuition: the final multiplication in any matrix-chain interval splits the
   * chain into a left product and a right product. Once those products are formed,
   * the final cost is determined by the boundary dimensions around the split.
   *
   * Algorithm:
   *   1. Treat leftIndex and rightIndex as the matrix interval boundaries.
   *   2. Try every split point as the final multiplication boundary.
   *   3. Add left cost, right cost, and final multiplication cost.
   *   4. Memoize the minimum for each interval.
   *
   * Time:  O(n^3) - O(n^2) intervals each try O(n) splits.
   * Space: O(n^2) - memo table plus recursion depth.
   *
   * @param arr matrix dimensions where matrix i is arr[i-1] x arr[i]
   * @return minimum scalar multiplications
   */
  public int matrixMultiplication(int[] arr) {
    int length = arr.length;
    if (length <= 2)
      return 0; // Single matrix or empty chain

    int[][] dp = new int[length][length];
    for (int[] row : dp) {
      Arrays.fill(row, Integer.MAX_VALUE / 2); // Avoid overflow
    }

    return matrixMultiplicationHelper(arr, 1, length - 1, dp);
  }

  // Helper: Recursively computes minimum multiplications for matrix chain from
  // index i to j.
  /** Returns the minimum cost to multiply the matrix interval. */
  private int matrixMultiplicationHelper(int[] arr, int leftIndex, int rightIndex, int[][] dp) {
    // Base case: Single matrix, no multiplication needed
    if (leftIndex == rightIndex)
      return 0;

    // Return memoized result if already computed
    if (dp[leftIndex][rightIndex] != Integer.MAX_VALUE / 2)
      return dp[leftIndex][rightIndex];

    int minCost = Integer.MAX_VALUE;

    // Try every possible split point k between i and j
    for (int breakPoint = leftIndex; breakPoint < rightIndex; breakPoint++) {
      // Cost to multiply left partition (i to k)
      int leftCost = matrixMultiplicationHelper(arr, leftIndex, breakPoint, dp);
      // Cost to multiply right partition (k+1 to j)
      int rightCost = matrixMultiplicationHelper(arr, breakPoint + 1, rightIndex, dp);
      // Cost to multiply the two resulting matrices
      // here matrix dimensions are arr[leftIndex - 1] x arr[breakPoint] x
      // arr[rightIndex]
      int mergeCost = arr[leftIndex - 1] * arr[breakPoint] * arr[rightIndex];

      int totalCost = leftCost + rightCost + mergeCost;
      minCost = Math.min(minCost, totalCost);
    }

    // Memoize and return the result
    dp[leftIndex][rightIndex] = minCost;
    return minCost;
  }

    /**
   * Intuition: interval solutions depend only on smaller intervals created by a
   * split. Filling by increasing chain length guarantees each side of a split is
   * ready when the larger interval is evaluated.
   *
   * Algorithm:
   *   1. Initialize zero cost for single matrices.
   *   2. Iterate chain lengths from two matrices up to the full chain.
   *   3. Try every possible split for each interval.
   *   4. Store the smallest multiplication cost in dp[start][end].
   *
   * Time:  O(n^3) - chain length, start index, and split loops.
   * Space: O(n^2) - interval DP table.
   *
   * @param arr matrix dimensions where matrix i is arr[i-1] x arr[i]
   * @return minimum scalar multiplications
   */
  public int matrixMultiplicationTabulation(int[] arr) {
    int length = arr.length;
    if (length <= 2)
      return 0;

    int[][] dp = new int[length][length]; // dp[i][j] = min cost to multiply matrices from i to j

    // Initialize: Cost of multiplying single matrix is 0
    for (int i = 1; i < length; i++) {
      dp[i][i] = 0;
    }

    // len is the chain length, starting from 2
    for (int gap = 2; gap < length; gap++) {
      for (int left = 1; left < length - gap + 1; left++) {
        int right = left + gap - 1;
        dp[left][right] = Integer.MAX_VALUE;

        // Try all possible split points
        for (int splitPoint = left; splitPoint < right; splitPoint++) {
          int mergeCost = arr[left - 1] * arr[splitPoint] * arr[right];
          int cost = dp[left][splitPoint] + dp[splitPoint + 1][right] + mergeCost;
          dp[left][right] = Math.min(dp[left][right], cost);
        }

      }
    }

    return dp[1][length - 1];
  }


    public static void main(String[] args) {
        MatrixChainMultiplication solver = new MatrixChainMultiplication();
        int[][] inputs = { {10, 20}, {2, 1, 3, 4}, {1, 2, 3, 4, 3} };
        int[] expected = {0, 20, 30};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.matrixMultiplicationTabulation(inputs[i]);
            System.out.printf("arr=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

}
