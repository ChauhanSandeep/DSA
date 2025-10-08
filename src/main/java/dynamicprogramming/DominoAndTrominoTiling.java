package dynamicprogramming;

import java.util.*;


/**
 * Domino and Tromino Tiling
 *
 * Problem Statement:
 * You have two types of tiles:
 * 1. A 2 x 1 domino shape (can be rotated to 1 x 2)
 * 2. A tromino shape (L-shaped, can be rotated in 4 ways)
 *
 * Given an integer n, return the number of ways to tile a 2 x n board.
 * Since the answer can be large, return it modulo 10^9 + 7.
 *
 * Example:
 * Input: n = 3
 * Output: 5
 * Explanation: The five different ways to tile 2x3 board are:
 * 1. Three 2x1 vertical dominoes
 * 2. One 2x1 vertical domino + two 1x2 horizontal dominoes
 * 3. Two 1x2 horizontal dominoes + one 2x1 vertical domino
 * 4. One L-tromino + one rotated L-tromino
 * 5. Another combination with L-trominos
 *
 * Input: n = 1
 * Output: 1 (only one 2x1 vertical domino fits)
 *
 * LeetCode Link: https://leetcode.com/problems/domino-and-tromino-tiling
 *
 * Follow-up Questions:
 * 1. What if the board dimensions were 3 x n instead of 2 x n?
 *    Answer: Different recurrence relation based on how 3-width tiles can be arranged.
 * 2. How would you handle if we had additional tile shapes (like 2x2 squares)?
 *    Answer: Extend state space to track all possible partial configurations.
 * 3. What if we want to find actual tilings instead of just count?
 *    Answer: Modify DP to store actual configurations instead of just counts.
 * 4. How to optimize space for very large n?
 *    Answer: Since we only need last 3 values, use rolling variables instead of array.
 */
public class DominoAndTrominoTiling {

    private static final int MOD = 1000000007;

    /**
     * Space-optimized version using rolling variables.
     *
     * Algorithm: Space-Optimized DP with Rolling Variables
     * Step 1: Observe that we only need last 3 values to compute current value
     * Step 2: Use three variables instead of full array
     * Step 3: Update variables in rolling fashion
     * Step 4: Maintain same recurrence relation logic
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1) - constant space usage
     *
     * @param n the width of the board
     * @return number of ways with optimized space usage
     */
    public int numTilingsOptimized(int n) {
      if (n == 1) {
        return 1;
      }
      if (n == 2) {
        return 2;
      }
      if (n == 3) {
        return 5;
      }

        // Step 1-2: Use rolling variables for last three values
        long thirdPrev = 1;  // f(i-3)
        long secondPrev = 2; // f(i-2)
        long firstPrev = 5;  // f(i-1)

        // Step 3-4: Compute using rolling updates
        for (int i = 4; i <= n; i++) {
          /**
           * - From firstPrev (f(i-1)), add a vertical domino
           * - From secondPrev (f(i-2)), add two horizontal dominoes
           * - From thirdPrev (f(i-3)), add two L-trominoes in two orientations. Vertical/Horizontal are not included
           *   as they are already covered in firstPrev and secondPrev
           */
            long current = (firstPrev + secondPrev + 2 * thirdPrev) % MOD;

            // Roll the variables forward
            thirdPrev = secondPrev;
            secondPrev = firstPrev;
            firstPrev = current;
        }

        return (int) firstPrev;
    }

    /**
     * Recursive approach with memoization for better understanding.
     *
     * Algorithm: Top-Down DP with Memoization
     * Step 1: Define recursive function that computes tiling ways for width n
     * Step 2: Use memoization to avoid recomputing same subproblems
     * Step 3: Handle base cases and recursive calls
     * Step 4: Apply modular arithmetic to prevent overflow
     *
     * Time Complexity: O(n) due to memoization
     * Space Complexity: O(n) for recursion stack and memoization table
     *
     * @param n the width of the board
     * @return number of ways using recursive memoization
     */
    public int numTilingsRecursive(int n) {
        Map<Integer, Long> memo = new HashMap<>(); // Key is n, Value is number of ways
        return (int) numTilingRecHelper(n, memo);
    }

    // Helper method for recursive memoized solution
    private long numTilingRecHelper(int n, Map<Integer, Long> memo) {
      if (n == 0) return 1;
      if (n == 1) return 1;
      if (n == 2) return 2;
      if (n == 3) return 5;

        // Check memoization
        if (memo.containsKey(n)) {
            return memo.get(n);
        }

        // Recursive call with memoization
        long result = (numTilingRecHelper(n-1, memo) + numTilingRecHelper(n-2, memo) + 2 * numTilingRecHelper(n-3, memo)) % MOD;

        memo.put(n, result);
        return result;
    }
}

/**
 * Usage Example:
 * DominoAndTrominoTiling solution = new DominoAndTrominoTiling();
 *
 * int result1 = solution.numTilings(3);              // returns 5 - optimal DP approach
 * int result2 = solution.numTilingsSimplified(3);   // returns 5 - simplified recurrence
 * int result3 = solution.numTilingsOptimized(3);    // returns 5 - space optimized O(1)
 * int result4 = solution.numTilingsRecursive(3);    // returns 5 - recursive with memoization
 *
 * // For larger inputs (matrix exponentiation)
 * int result5 = solution.numTilingsMatrix(100);     // efficient for very large n
 *
 * // Verification with brute force (only for small n)
 * int result6 = solution.numTilingsBruteForce(3);   // returns 5 - brute force verification
 *
 * System.out.println("Ways to tile 2x3 board: " + result1); // Output: 5
 * System.out.println("Ways to tile 2x1 board: " + solution.numTilings(1)); // Output: 1
 * System.out.println("Ways to tile 2x4 board: " + solution.numTilings(4)); // Output: 11
 */
