package DynamicProgramming;

/**
 * Problem: Minimum Cost to Merge Stones
 *
 * There are n piles of stones arranged in a row. The ith pile has stones[i] stones.
 * A move consists of merging exactly k consecutive piles into one pile, and the cost of this move
 * is equal to the total number of stones in these k piles.
 * Return the minimum cost to merge all piles into one pile. If it is impossible, return -1.
 *
 * Example:
 * Input: stones = [3,2,4,1], k = 2
 * Output: 20
 * Explanation: We start with [3, 2, 4, 1].
 * We merge [3, 2] for a cost of 5, and we are left with [5, 4, 1].
 * We merge [5, 4] for a cost of 9, and we are left with [9, 1].
 * We merge [9, 1] for a cost of 10, and we are left with [10].
 * The total cost was 20, and this is the minimum possible.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/minimum-cost-to-merge-stones/
 * GeeksforGeeks Link: https://www.geeksforgeeks.org/minimum-cost-to-merge-all-elements-of-array/
 *
 * Follow-up Questions:
 * 1. Q: What if we can merge any number of consecutive piles (not just k)?
 *    A: This becomes standard interval DP - try all possible split points for each range
 * 2. Q: What if we want to maximize the cost instead of minimize?
 *    A: Change Math.min to Math.max in the recurrence relation
 * 3. Q: How to track the actual sequence of merges?
 *    A: Store parent pointers in DP table to reconstruct the optimal merge sequence
 * 4. Q: What if stones have different weights and values?
 *    A: Modify the cost function to account for weighted merging costs
 * 5. Q: Minimum cost to merge into exactly m piles?
 *    A: Add third dimension to DP: dp[i][j][p] = min cost to merge range [i,j] into p piles
 */
public class MinimumCostMergeStones {

  public static void main(String[] args) {
    MinimumCostMergeStones solver = new MinimumCostMergeStones();

    // Test case for k=2 (binary merge)
    int[] stones = {3, 2, 4, 1};
    System.out.println("Min cost (k=2): " + solver.mergeStones(stones, 2)); // Expected: 20

    // Test case for simple adjacent merge
    int[] elements = {1, 2, 3, 4};
    System.out.println("Min adjacent merge cost: " + solver.minMergeCostAdjacent(elements)); // Expected: 19
  }

  /**
   * Finds minimum cost to merge all stone piles into one pile with k-way merging.
   *
   * Algorithm Steps:
   * 1. Check if merging is possible: (n-1) % (k-1) == 0
   * 2. Use 3D DP where dp[i][j][p] = min cost to merge range [i,j] into p piles
   * 3. Base case: dp[i][i][1] = 0 (single pile already merged)
   * 4. Transition: merge p piles by combining smaller groups
   * 5. Final merge: combine k piles into 1 with cost = sum of range
   *
   * Time Complexity: O(n^3 * k) where n is number of piles
   * Space Complexity: O(n^2 * k) for 3D DP table
   *
   * @param stonePiles Array representing number of stones in each pile
   * @param mergeGroupSize Number of consecutive piles that can be merged at once
   * @return Minimum cost to merge all piles into one, or -1 if impossible
   */
  public int mergeStones(int[] stonePiles, int mergeGroupSize) {
    if (stonePiles == null || stonePiles.length == 0) {
      return 0;
    }

    int numPiles = stonePiles.length;

    // Check if merging is possible
    if ((numPiles - 1) % (mergeGroupSize - 1) != 0) {
      return -1;
    }

    // Prefix sum array for quick range sum calculation
    int[] prefixSum = buildPrefixSum(stonePiles);

    // DP table: dp[i][j][p] = min cost to merge range [i,j] into p piles
    int[][][] minCostToMerge = new int[numPiles][numPiles][mergeGroupSize + 1];

    // Initialize DP table with maximum values
    for (int startIndex = 0; startIndex < numPiles; startIndex++) {
      for (int endIndex = 0; endIndex < numPiles; endIndex++) {
        for (int targetPiles = 0; targetPiles <= mergeGroupSize; targetPiles++) {
          minCostToMerge[startIndex][endIndex][targetPiles] = Integer.MAX_VALUE;
        }
      }
    }

    // Base case: single pile requires no cost
    for (int pileIndex = 0; pileIndex < numPiles; pileIndex++) {
      minCostToMerge[pileIndex][pileIndex][1] = 0;
    }

    // Fill DP table for all range lengths
    for (int rangeLength = 2; rangeLength <= numPiles; rangeLength++) {
      for (int startIndex = 0; startIndex + rangeLength - 1 < numPiles; startIndex++) {
        int endIndex = startIndex + rangeLength - 1;

        // Try to form 2 to k piles first
        for (int targetPiles = 2; targetPiles <= mergeGroupSize; targetPiles++) {
          for (int splitPoint = startIndex; splitPoint < endIndex; splitPoint += (mergeGroupSize - 1)) {
            if (minCostToMerge[startIndex][splitPoint][1] != Integer.MAX_VALUE
                && minCostToMerge[splitPoint + 1][endIndex][targetPiles - 1] != Integer.MAX_VALUE) {

              minCostToMerge[startIndex][endIndex][targetPiles] =
                  Math.min(minCostToMerge[startIndex][endIndex][targetPiles],
                      minCostToMerge[startIndex][splitPoint][1] + minCostToMerge[splitPoint + 1][endIndex][targetPiles
                          - 1]);
            }
          }
        }

        // Merge k piles into 1 pile
        if (minCostToMerge[startIndex][endIndex][mergeGroupSize] != Integer.MAX_VALUE) {
          int rangeSum = getRangeSum(prefixSum, startIndex, endIndex);
          minCostToMerge[startIndex][endIndex][1] = minCostToMerge[startIndex][endIndex][mergeGroupSize] + rangeSum;
        }
      }
    }

    return minCostToMerge[0][numPiles - 1][1];
  }

  /**
   * Simplified version for adjacent element merging (k=2 case).
   *
   * Algorithm Steps:
   * 1. Use 2D DP where dp[i][j] = min cost to merge range [i,j]
   * 2. Base case: dp[i][i] = 0 (single element)
   * 3. For each range, try all possible split points
   * 4. Cost = left_cost + right_cost + sum_of_range
   *
   * Time Complexity: O(n^3) where n is number of elements
   * Space Complexity: O(n^2) for 2D DP table
   *
   * @param elements Array of integers to be merged
   * @return Minimum cost to merge all elements into one
   */
  public int minMergeCostAdjacent(int[] elements) {
    if (elements == null || elements.length <= 1) {
      return 0;
    }

    int numElements = elements.length;
    int[] prefixSum = buildPrefixSum(elements);

    // DP table: dp[i][j] = min cost to merge elements from index i to j
    int[][] minMergeCost = new int[numElements][numElements];

    // Fill DP table for all possible range lengths
    for (int rangeLength = 2; rangeLength <= numElements; rangeLength++) {
      for (int startIndex = 0; startIndex + rangeLength - 1 < numElements; startIndex++) {
        int endIndex = startIndex + rangeLength - 1;
        minMergeCost[startIndex][endIndex] = Integer.MAX_VALUE;

        int rangeSum = getRangeSum(prefixSum, startIndex, endIndex);

        // Try all possible split points
        for (int splitPoint = startIndex; splitPoint < endIndex; splitPoint++) {
          int leftCost = minMergeCost[startIndex][splitPoint];
          int rightCost = minMergeCost[splitPoint + 1][endIndex];

          minMergeCost[startIndex][endIndex] =
              Math.min(minMergeCost[startIndex][endIndex], leftCost + rightCost + rangeSum);
        }
      }
    }

    return minMergeCost[0][numElements - 1];
  }

  /**
   * Builds prefix sum array for efficient range sum queries.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(n)
   *
   * @param elements Input array
   * @return Prefix sum array where prefixSum[i] = sum of elements[0] to elements[i-1]
   */
  private int[] buildPrefixSum(int[] elements) {
    int[] prefixSum = new int[elements.length + 1];
    for (int i = 0; i < elements.length; i++) {
      prefixSum[i + 1] = prefixSum[i] + elements[i];
    }
    return prefixSum;
  }

  /**
   * Calculates sum of elements in range [startIndex, endIndex] using prefix sum.
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   *
   * @param prefixSum Prefix sum array
   * @param startIndex Starting index (inclusive)
   * @param endIndex Ending index (inclusive)
   * @return Sum of elements in the specified range
   */
  private int getRangeSum(int[] prefixSum, int startIndex, int endIndex) {
    return prefixSum[endIndex + 1] - prefixSum[startIndex];
  }
}
