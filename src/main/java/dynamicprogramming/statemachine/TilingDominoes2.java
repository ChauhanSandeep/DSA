package dynamicprogramming.statemachine;

/**
 * Problem: Tiling a 3 x N Board with 2 x 1 Dominoes
 *
 * You are given a 3 x N board. Count the number of ways to tile it completely using 2 x 1 dominoes.
 * - Dominoes can be placed either vertically or horizontally.
 * - All cells must be filled. Overlapping and out-of-bound placements are not allowed.
 *
 * Approach: Dynamic Programming with Bitmasking (State Compression)
 * - Represent the state of each column using a 3-bit mask (8 possible states: 0 to 7).
 *   - Bit 0: top row
 *   - Bit 1: middle row
 *   - Bit 2: bottom row
 *   - 1 means filled, 0 means empty
 * - Base state: `prev[7] = 1` (a fully filled column before the first)
 * - Use precomputed transitions from one state to another for each column.
 *
 * Leetcode version (different constraints): N/A
 * Reference: https://www.youtube.com/watch?v=yn2jnmlepY8
 *
 * Example:
 * Input: n = 2 → Output: 3
 * Ways:
 * - Two vertical dominoes in each column
 * - Four horizontal dominoes stacked
 * - Two L-shaped placements (top+middle, middle+bottom)
 *
 * Follow-up Questions:
 * - How would this change if trominoes or L-shaped tiles were allowed?
 * - How do you extend this to an M x N board? (Matrix DP with memoization or matrix exponentiation)
 */
public class TilingDominoes2 {

  private static final int MOD = 1_000_000_007;
  private static final int STATE_COUNT = 8; // 3-bit masks from 000 to 111
  private static final int FULL_MASK = 7;   // 111 in binary — all rows filled

  public static void main(String[] args) {
    int n = 50;
    System.out.println("Ways to tile a 3 x " + n + " board: " + countWaysToTile3xN(n));
  }

  /**
   * DP with state compression using bitmasks.
   *
   * Each column can be represented using a 3-bit mask (0–7),
   * where each bit represents the fill status of a cell (top to bottom).
   *
   * Time Complexity: O(N)
   * Space Complexity: O(1) – constant space for 8 states
   */
  public static int countWaysToTile3xN(int length) {
    if (length <= 0) return 0;

    if (length == 1) return 0; // 3x1 can't be fully tiled with 2x1 dominoes

    // curr[i] = x means that there are x ways to tile columns 1...col such that the current column ends in state i (bitmask i pattern)
    int[] curr;
    int[] prev = new int[STATE_COUNT]; // similarly for previous column states

    // Base case: an imaginary column before the first is fully filled
    prev[FULL_MASK] = 1;

    for (int col = 1; col <= length; col++) {
      curr = new int[STATE_COUNT];

      // Transition logic:
      curr[0b000] = prev[0b111];                                       //
      curr[0b001] = prev[0b110];                                       // By placing a horizontal domino in the first row
      curr[0b010] = prev[0b101];                                       // By placing a horizontal domino in the second row
      curr[0b011] = (prev[0b100] + prev[0b111]) % MOD;                 // By either (placing a horizontal domino in the first two rows) or (filling the first two rows with a vertical domino)
      curr[0b100] = prev[0b011];                                       // By placing a horizontal domino in the third row
      curr[0b101] = prev[0b010];                                       // By placing a horizontal domino in the first and third rows
      curr[0b110] = (prev[0b001] + prev[0b111]) % MOD;                 // By either (placing 2 vertical dominoes in second and third rows) or (filling the first two rows with a vertical domino)
      curr[0b111] = ((prev[0b000] + prev[0b011]) % MOD + prev[0b110]) % MOD; // By either (placing 3 horizontal dominoes) or (filling third row with a horizontal domino and the first two rows with a vertical domino)

      prev = curr; // Move to next column
    }

    return prev[FULL_MASK]; // Final column must be completely filled
  }
}