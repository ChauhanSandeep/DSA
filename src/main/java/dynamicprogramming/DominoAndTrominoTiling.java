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

  private static final int MODULO = 1_000_000_007;

  /**
   * Using full state DP with explicit tracking of partial tilings.
   * Step-by-step:
   *  1. Define two states:
   *     - fullyTiled[i]: ways to fully tile 2 x i board
   *     - partiallyTiled[i]: ways to tile with one cell in column i+1 already covered
   *  2. Base cases: fullyTiled[0] = 1, partiallyTiled[0] = 0
   *  3. Transitions:
   *     - fullyTiled[i] = fullyTiled[i-1] + fullyTiled[i-2] + 2 * partiallyTiled[i-1]
   *     - partiallyTiled[i] = fullyTiled[i-1] + partiallyTiled[i-1]
   *  4. This approach makes the state transitions more explicit and easier to understand.
   *
   * Algorithm: Dynamic Programming with explicit state tracking.
   * Time Complexity: O(n).
   * Space Complexity: O(n) for both arrays.
   */
  public int numTilingsExplicitStates(int boardWidth) {
    // Base cases
    if (boardWidth == 1) return 1; // Only one vertical domino possible
    if (boardWidth == 2) return 2; // Two ways: 2 vertical dominoes or 2 horizontal dominoes

    // fullyTiled[i] → number of ways to fully cover a 2×i board
    // partiallyTiled[i]  → number of ways to cover a 2×i board with one cell missing at the top or bottom corner
    long[] fullyTiled = new long[boardWidth + 1];
    long[] partiallyTiled = new long[boardWidth + 1];

    // Base initialization
    fullyTiled[0] = 1; // An empty board (2×0) has one valid configuration — do nothing
    fullyTiled[1] = 1; // A 2×1 board can only have one vertical domino
    partiallyTiled[0] = 0;  // You can’t have a half-covered empty board
    partiallyTiled[1] = 0;  // A 2×1 board can’t be half-covered by a valid tromino

    // Fill the DP arrays using recurrence relations
    for (int width = 2; width <= boardWidth; width++) {
      /**
       * fullyTiled[width]:
       *  1. Add a vertical domino to fullyTiled[width - 1]
       *  2. Add two horizontal dominoes to fullyTiled[width - 2]
       *  3. Add two mirrored L-shaped trominoes to partiallyTiled[width - 1]
       */
      fullyTiled[width] = (fullyTiled[width - 1] + fullyTiled[width - 2] + 2 * partiallyTiled[width - 1]) % MODULO;

      /**
       * partiallyTiled[width]:
       *  1. Extend a fully covered board of width-2 by adding one tromino
       *  2. Extend a half-covered board of width-1 by adding one vertical domino
       */
      partiallyTiled[width] = (fullyTiled[width - 2] + partiallyTiled[width - 1]) % MODULO;
    }

    return (int) fullyTiled[boardWidth];
  }
}
