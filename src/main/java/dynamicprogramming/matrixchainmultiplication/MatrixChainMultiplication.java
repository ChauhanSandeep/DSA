package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * MatrixChainMultiplication.java
 *
 * Problem Statement:
 * Given a sequence of matrices, the goal is to find the most efficient way to
 * multiply these matrices together.
 * The problem is represented by an array arr[] where the dimension of the i-th
 * matrix is arr[i-1] x arr[i].
 * The objective is to determine the minimum number of scalar multiplications
 * needed to compute the matrix chain product.
 *
 * Example 1:
 * Input: arr = [2, 1, 3, 4]
 * Output: 20
 * Explanation:
 * There are 3 matrices: M1(2x1), M2(1x3), M3(3x4)
 * Two ways to multiply:
 * - ((M1 x M2) x M3) = (2*1*3) + (2*3*4) = 6 + 24 = 30
 * - (M1 x (M2 x M3)) = (1*3*4) + (2*1*4) = 12 + 8 = 20
 * Minimum is 20.
 *
 * Example 2:
 * Input: arr = [1, 2, 3, 4, 3]
 * Output: 30
 * Explanation:
 * Four matrices: M1(1x2), M2(2x3), M3(3x4), M4(4x3)
 * Optimal parenthesization: ((M1M2)M3)M4
 * Multiplications: (1*2*3) + (1*3*4) + (1*4*3) = 6 + 12 + 12 = 30
 *
 * GeeksForGeeks link:
 * https://www.geeksforgeeks.org/problems/matrix-chain-multiplication0303/1
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 * - Can you reconstruct the optimal parenthesization sequence, not just the
 * minimum cost?
 * → Yes, maintain an additional table to store split points and backtrack
 * through it to build the parenthesization string.
 * - How would you handle the case where matrices have incompatible dimensions?
 * → Add validation to check if arr[i] matches dimensions correctly; return
 * error for invalid input.
 * - What if you need to parallelize matrix chain multiplication?
 * → Divide-and-conquer with parallel execution on independent subproblems,
 * though DP dependencies limit parallelization.
 * - Can you extend this to handle matrix addition or other operations with
 * different costs?
 * → Modify the cost function in DP recurrence to account for different
 * operation costs.
 *
 * Relevant Follow-up Problems:
 * - LeetCode 312 (Burst Balloons):
 * https://leetcode.com/problems/burst-balloons/
 * - LeetCode 1039 (Minimum Score Triangulation of Polygon):
 * https://leetcode.com/problems/minimum-score-triangulation-of-polygon/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MatrixChainMultiplication {

  /**
   * Main method: Calculates minimum scalar multiplications using Top-Down DP
   * (Memoization).
   * Step-by-step:
   * 1. Initialize a memoization table dp where dp[i][j] stores the minimum cost
   * for multiplying matrices from i to j.
   * 2. Use recursive function to explore all possible split points k between i
   * and j.
   * 3. For each split, calculate cost as: dp[i][k] + dp[k+1][j] + arr[i-1] *
   * arr[k] * arr[j]
   * 4. Memoize results to avoid recomputation.
   * 5. Base case: Single matrix (i == j) has zero cost.
   *
   * Algorithm: Dynamic Programming with Memoization (Interval DP).
   * Time Complexity: O(n^3), where n is the number of matrices (n = arr.length -
   * 1).
   * Space Complexity: O(n^2), for the memoization table plus O(n) recursion
   * stack.
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
   * Alternative method: Bottom-Up DP (Tabulation). Avoids recursion overhead and
   * stack space.
   * Step-by-step:
   * 1. Create dp table where dp[i][j] represents minimum cost to multiply
   * matrices from i to j.
   * 2. Fill table diagonally: start with chains of length 2, then 3, and so on.
   * 3. For each chain length, iterate through all possible starting positions.
   * 4. For each subchain, try all split points and update dp[i][j] with minimum
   * cost.
   * 5. Result is in dp[1][n-1].
   *
   * Algorithm: Dynamic Programming with Tabulation (Interval DP).
   * Time Complexity: O(n^3), where n is the number of matrices.
   * Space Complexity: O(n^2), for the dp table only (no recursion stack).
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
}
