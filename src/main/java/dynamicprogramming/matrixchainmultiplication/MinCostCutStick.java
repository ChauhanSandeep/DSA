package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;


/**
 * Problem: Minimum Cost to Cut a Stick
 *
 * Given a stick length and cut positions, choose the order of cuts. Each cut
 * costs the length of the current piece being cut, and after cutting that piece
 * splits into two smaller pieces. Return the minimum total cost.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-to-cut-a-stick/
 * Rating:   2116 (zerotrac Elo)
 * Pattern:  Dynamic Programming | Interval DP | Choose the first cut in a segment
 *
 * Example:
 *   Input:  n = 7, cuts = [1,3,4,5]
 *   Output: 16
 *   Why:    cutting at 3 first pays 7, then the left piece costs 3 and the
 *           right piece can be cut for 6 total, giving 16.
 *
 * Follow-ups:
 *   1. Can you reconstruct the cut order?
 *      Store the best first cut for each interval and recursively output that cut before its children.
 *   2. What if duplicate or boundary cuts are present?
 *      Sort, remove duplicates, and ignore cuts at 0 or n before running the same DP.
 *   3. What if cutting cost is not segment length?
 *      Keep the interval split, but replace the segment-length term with the supplied cost function.
 *
 * Related: Burst Balloons (312), Matrix Chain Multiplication.
 */
public class MinCostCutStick {

        /**
     * Intuition: after choosing the first cut inside a stick segment, the left and
     * right pieces become independent, and the current segment length is paid once.
     * Sorting cuts and adding boundaries turns the problem into interval DP.
     *
     * Algorithm:
     *   1. Build allCuts with 0, every cut, and stickLength, then sort it.
     *   2. Recursively choose each cut index inside the current interval.
     *   3. Add current segment length plus left and right subproblem costs.
     *   4. Memoize the minimum cost for each boundary-index pair.
     *
     * Time:  O(c^3) - O(c^2) intervals each try O(c) cuts.
     * Space: O(c^2) - memo table plus recursion depth.
     *
     * @param stickLength length of the stick
     * @param cuts cut positions
     * @return minimum total cutting cost
     */
    public int minCost(int stickLength, int[] cuts) {
        int numberOfCuts = cuts.length;

        // Step 1: Add virtual cuts at the boundaries (0 and stickLength)
        int[] allCuts = new int[numberOfCuts + 2];
        System.arraycopy(cuts, 0, allCuts, 1, numberOfCuts);
        allCuts[0] = 0;
        allCuts[numberOfCuts + 1] = stickLength;

        // Step 2: Sort the cuts so the stick is logically divided
        Arrays.sort(allCuts);

        // Step 3: Create a memoization table
        int[][] memo = new int[allCuts.length][allCuts.length]; // memo[i][j] minimum cost to cut the stick between cuts[i] and cuts[j]
        // Initialize memo with -1 to indicate uncomputed states
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        // Step 4: Compute and return the result
        return findMinCost(allCuts, 0, numberOfCuts + 1, memo);
    }

        /** Returns the minimum cutting cost between two boundary cut indexes. */
    private int findMinCost(int[] allCuts, int leftCutIndex, int rightCutIndex, int[][] memo) {
      // Base case: No more cuts possible between left and right
      if (leftCutIndex + 1 == rightCutIndex) {
        return 0;
      }

      // Return cached result
      if (memo[leftCutIndex][rightCutIndex] != -1) {
        return memo[leftCutIndex][rightCutIndex];
      }

      int minCost = Integer.MAX_VALUE;

      // Try all possible first cuts between left and right
      for (int cutIndex = leftCutIndex + 1; cutIndex < rightCutIndex; cutIndex++) {
        int leftCost = findMinCost(allCuts, leftCutIndex, cutIndex, memo);
        int rightCost = findMinCost(allCuts, cutIndex, rightCutIndex, memo);
        int currStickLength = allCuts[rightCutIndex] - allCuts[leftCutIndex]; // because leftCutIndex and rightCutIndex contains index number but actual length is allCuts[right] - allCuts[left]

        int totalCost = leftCost + rightCost + currStickLength;
        minCost = Math.min(minCost, totalCost);
      }

      // Cache and return result
      memo[leftCutIndex][rightCutIndex] = minCost;
      return minCost;
    }

    /**
   * Intuition: a segment cost depends on smaller left and right cut intervals.
   * Filling intervals from short to long makes every candidate first cut available
   * before the containing segment is evaluated.
   *
   * Algorithm:
   *   1. Sort cuts with 0 and stickLength as boundaries.
   *   2. Iterate intervals by increasing distance between boundary indexes.
   *   3. Try every cut inside the interval as the first cut.
   *   4. Store the minimum cost for the full boundary interval.
   *
   * Time:  O(c^3) - every interval scans its possible first cuts.
   * Space: O(c^2) - interval DP table.
   *
   * @param stickLength length of the stick
   * @param cuts cut positions
   * @return minimum total cutting cost
   */
  public int minCostIterative(int stickLength, int[] cuts) {
    // Handle edge case: no cuts
    if (cuts == null || cuts.length == 0) {
      return 0;
    }

    // Add boundaries and sort
    int numberOfCuts = cuts.length;
    int[] curPositions = new int[numberOfCuts + 2];

    System.arraycopy(cuts, 0, curPositions, 1, numberOfCuts);
    curPositions[0] = 0;
    curPositions[numberOfCuts + 1] = stickLength;
    Arrays.sort(curPositions);

    // DP table
    int[][] dp = new int[numberOfCuts + 2][numberOfCuts + 2]; // dp[i][j] = min cost to cut between curPositions[i] and curPositions[j]

    // Fill DP for increasing interval lengths
    for (int gap = 2; gap < numberOfCuts + 2; gap++) {
      for (int left = 0; left + gap < numberOfCuts + 2; left++) {
        int right = left + gap;
        dp[left][right] = Integer.MAX_VALUE;
        // Try each possible k
        for (int split = left + 1; split < right; split++) {
          // Cost if cut at split first
          int currStickLength = curPositions[right] - curPositions[left];
          int cost = dp[left][split] + dp[split][right] + currStickLength;
          // Update min
          dp[left][right] = Math.min(dp[left][right], cost);
        }
      }
    }

    return dp[0][numberOfCuts + 1];
  }


    public static void main(String[] args) {
        MinCostCutStick solver = new MinCostCutStick();
        int[] lengths = {7, 9, 5};
        int[][] cuts = { {1, 3, 4, 5}, {5, 6, 1, 4, 2}, {} };
        int[] expected = {16, 22, 0};

        for (int i = 0; i < lengths.length; i++) {
            int output = solver.minCostIterative(lengths[i], cuts[i]);
            System.out.printf("n=%d cuts=%s  ->  %d  expected=%d%n",
                lengths[i], Arrays.toString(cuts[i]), output, expected[i]);
        }
    }

}