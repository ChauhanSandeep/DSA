package dynamicprogramming.statemachine;


/**
 * Problem: Domino and Tromino Tiling
 *
 * Count the ways to tile a 2 x n board using dominoes and L-shaped trominoes.
 * Dominoes may be vertical or horizontal, and trominoes may be rotated. Return
 * the count modulo 1,000,000,007.
 *
 * Leetcode: https://leetcode.com/problems/domino-and-tromino-tiling/
 * Rating:   1830 (zerotrac Elo)
 * Pattern:  Dynamic Programming | State machine | Full and partial board states
 *
 * Example:
 *   Input:  n = 3
 *   Output: 5
 *   Why:    three domino-only tilings plus two mirrored tromino tilings cover all valid 2 x 3 boards.
 *
 * Follow-ups:
 *   1. Can space be reduced to O(1)?
 *      Yes; each state only depends on the previous two full states and previous partial state.
 *   2. What if more tile shapes are allowed?
 *      Track every possible frontier mask and generate transitions for each tile shape.
 *   3. Can this be solved with matrix exponentiation?
 *      Yes; encode the state recurrence as a small transition matrix for O(log n) time.
 *
 * Related: Tiling a Rectangle with the Fewest Squares (1240).
 */
public class DominoAndTrominoTiling {

  private static final int MODULO = 1_000_000_007;

    /**
   * Intuition: a board prefix can be fully covered or have one missing square at
   * the next column. Dominoes extend full states, while trominoes move between
   * full and partial states.
   *
   * Algorithm:
   *   1. Track full, top-missing, and bottom-missing counts for each width.
   *   2. Extend full boards with vertical dominoes or two horizontal dominoes.
   *   3. Use tromino transitions between full and partial states.
   *   4. Return the full-board count modulo MOD.
   *
   * Time:  O(boardWidth) - each column state is computed once.
   * Space: O(boardWidth) - DP arrays store states for every width.
   *
   * @param boardWidth number of columns in the 2 x n board
   * @return number of tilings modulo MOD
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


    public static void main(String[] args) {
        DominoAndTrominoTiling solver = new DominoAndTrominoTiling();
        int[] inputs = {1, 2, 3, 4};
        int[] expected = {1, 2, 5, 11};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.numTilingsExplicitStates(inputs[i]);
            System.out.printf("n=%d  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}
