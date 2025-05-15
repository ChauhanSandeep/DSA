package DynamicProgramming.MatrixChainMul;

import java.util.Arrays;


public class MinCostCutStick {

    /**
     * Solves the Minimum Cost to Cut a Stick problem using recursion with memoization.
     * Leetcode Problem: https://leetcode.com/problems/minimum-cost-to-cut-a-stick/
     *
     * Intuition:
     * - Every time we cut the stick, we pay a cost equal to its current length.
     * - We want to choose the order of cuts that minimizes the total cost.
     * - Try placing each cut as the first cut, recursively solve for left and right pieces.
     *
     * Steps:
     * 1. Add virtual boundaries 0 and stickLength to the cuts array.
     * 2. Sort the array so we can treat segments properly.
     * 3. Use memoization to avoid recalculating overlapping subproblems.
     *
     * Time Complexity: O(m^3), where m is the number of cuts
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
        int[][] memo = new int[allCuts.length][allCuts.length];
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
     * @param left the index of the left cut
     * @param right the index of the right cut
     * @param memo memoization table
     * @return minimum cost to cut this segment
     */
    private int findMinCost(int[] allCuts, int left, int right, int[][] memo) {
      // Base case: No more cuts possible between left and right
      if (left + 1 == right) {
        return 0;
      }

      // Return cached result
      if (memo[left][right] != -1) {
        return memo[left][right];
      }

      int minCost = Integer.MAX_VALUE;

      // Try all possible first cuts between left and right
      for (int mid = left + 1; mid < right; mid++) {
        int leftCost = findMinCost(allCuts, left, mid, memo);
        int rightCost = findMinCost(allCuts, mid, right, memo);
        int cutCost = allCuts[right] - allCuts[left]; // because left and right contains index number but actual length is allCuts[right] - allCuts[left]

        int totalCost = leftCost + rightCost + cutCost;
        minCost = Math.min(minCost, totalCost);
      }

      // Cache and return result
      memo[left][right] = minCost;
      return minCost;
    }

    public static void main(String[] args) {
        MinCostCutStick solver = new MinCostCutStick();
        System.out.println("Minimum Cost: " + solver.minCost(7, new int[]{1, 3, 4, 5})); // Output: 16
    }
}