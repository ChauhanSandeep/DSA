package dynamicprogramming.MatrixChainMul;

import java.util.Arrays;


/**
 * Problem Statement:
 * Given a wooden stick of length n units and an array cuts where cuts[i] is the position to perform a cut.
 * You can change the order of the cuts. The cost of one cut on a stick is the length of the current stick.
 * After a cut, the stick is split into two smaller sticks. Return the minimum total cost of the cuts.
 *
 * Example:
 * Input: n = 9, cuts = [5,6,1,4,2]
 * Output: 22
 *
 * Explanation: If you try the given cuts ordering the cost will be 25.
 * There are much ordering with total cost <= 25, for example, the order [4, 6, 5, 2, 1] has total cost = 22 which is the minimum possible.
 *
 * LeetCode Link: https://leetcode.com/problems/minimum-cost-to-cut-a-stick/
 *
 * Follow-up Questions:
 * 1. How to modify for maximum cost? - Change min to max in DP, similar to burst balloons.
 * 2. What if cuts are not unique or include 0/n? - Need to handle duplicates by sorting uniquely, but problem says unique.
 * 3. How does this relate to matrix chain multiplication? - It's analogous: cuts are like dimensions, cost is like multiplication cost.
 * Relevant follow-up problem: https://leetcode.com/problems/burst-balloons/ (similar interval DP but maximization).
 */
public class MinCostCutStick {

    /**
     * Solves the Minimum Cost to Cut a Stick problem using recursion with memoization.
     *
     * Intuition:
     * - Every time we cut the stick, we pay a cost equal to its current length.
     * - We want to choose the order of cuts that minimizes the total cost.
     * - Try placing each cut as the first cut, recursively solve for left and right pieces.
     *
     *  Why Greedy (cutting at the middle) does NOT work:
     * - The cost of a cut is the length of the stick at the time of the cut.
     * - Cutting early changes the size of future sub-sticks, affecting their cost.
     * - A locally optimal cut (like cutting at the middle) may lead to expensive future cuts.
     * - Greedy does not consider the global impact of early decisions.
     *
     * Steps:
     * 1. Add virtual boundaries 0 and stickLength to the cuts array.
     * 2. Sort the array so we can treat segments properly.
     * 3. Use memoization to avoid recalculating overlapping subproblems.
     *
     * Time Complexity: O(m^3), where m is the number of cuts, because
     * Space Complexity: O(m^2) for memo + O(m) for call stack (recursive depth)
     *
     * @param stickLength the length of the stick
     * @param cuts the positions where cuts need to be made
     * @return the minimum total cost to perform all cuts
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

    /**
     * Recursive helper to find the minimum cost to cut the stick between allCuts[left] and allCuts[right].
     *
     * @param allCuts the sorted cuts array with boundaries included
     * @param leftCutIndex the index of the left cut
     * @param rightCutIndex the index of the right cut
     * @param memo memoization table
     * @return minimum cost to cut this segment
     */
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
   * Computes the minimum cost to cut the stick at all given positions.
   *
   * Step-by-step Explanation:
   * 1. Add boundaries 0 and stickLength to cuts, sort them.
   * 2. Initialize 2D DP where dp[i][j] is min cost to cut between cuts[i] and cuts[j].
   * 3. For interval lengths from 2 to m-1 (m = cuts.size after adding boundaries).
   * 4. For each interval [i,j], try every possible first cut k between i+1 and j-1.
   * 5. Cost = dp[i][k] + dp[k][j] + (cuts[j] - cuts[i]).
   * 6. Take the min over all k for dp[i][j].
   * 7. Result is dp[0][m-1].
   *
   * Algorithm: Bottom-up Dynamic Programming (interval DP).
   * Time Complexity: O(m^3), where m is cuts.length + 2 (three nested loops).
   * Space Complexity: O(m^2), for DP table.
   *
   * @param stickLength the length of the stick
   * @param cuts array of cut positions
   * @return minimum total cost
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
    for (int len = 2; len < numberOfCuts + 2; len++) {
      for (int i = 0; i + len < numberOfCuts + 2; i++) {
        int j = i + len;
        dp[i][j] = Integer.MAX_VALUE;
        // Try each possible k
        for (int intermediateCut = i + 1; intermediateCut < j; intermediateCut++) {
          // Cost if cut at intermediateCut first
          int currStickLength = curPositions[j] - curPositions[i];
          int cost = dp[i][intermediateCut] + dp[intermediateCut][j] + currStickLength;
          // Update min
          dp[i][j] = Math.min(dp[i][j], cost);
        }
      }
    }

    return dp[0][numberOfCuts + 1];
  }


  public static void main(String[] args) {
        MinCostCutStick solver = new MinCostCutStick();
        System.out.println("Minimum Cost: " + solver.minCost(7, new int[]{1, 3, 4, 5})); // Output: 16
    }
}