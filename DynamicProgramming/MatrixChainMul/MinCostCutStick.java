package DynamicProgramming.MatrixChainMul;

import java.util.Arrays;


/**
 * Given an integer array cuts where cuts[i] denotes a position you should perform a cut at.
 * You should perform the cuts in order, you can change the order of the cuts as you wish.
 *
 * The cost of one cut is the length of the stick to be cut, the total cost is the sum of costs of all cuts.
 * When you cut a stick, it will be split into two smaller sticks (i.e. the sum of their lengths is the
 * length of the stick before the cut).
 * Example:
 * Input: n = 9, cuts = [5,6,1,4,2]
 * Output: 22
 * Explanation: If you try the given cuts ordering the cost will be 25.
 * There are much ordering with total cost <= 25, for example, the order [4, 6, 5, 2, 1] has total cost = 22 which is the minimum possible.
 *
 * Leetcode Problem: <a href="https://leetcode.com/problems/minimum-cost-to-cut-a-stick/">...</a>
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
     * ❌ Why Greedy (cutting at the middle) does NOT work:
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
        int cutCost = allCuts[rightCutIndex] - allCuts[leftCutIndex]; // because leftCutIndex and rightCutIndex contains index number but actual length is allCuts[right] - allCuts[left]

        int totalCost = leftCost + rightCost + cutCost;
        minCost = Math.min(minCost, totalCost);
      }

      // Cache and return result
      memo[leftCutIndex][rightCutIndex] = minCost;
      return minCost;
    }

    public static void main(String[] args) {
        MinCostCutStick solver = new MinCostCutStick();
        System.out.println("Minimum Cost: " + solver.minCost(7, new int[]{1, 3, 4, 5})); // Output: 16
    }
}