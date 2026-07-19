package dynamicprogramming.statemachine;

/**
 * Problem: Tile a 3 x n Board With Dominoes
 *
 * Count the ways to completely cover a 3 x n board using only 2 x 1 dominoes.
 * Dominoes may be vertical or horizontal, and every cell must be covered exactly
 * once.
 *
 * Source: Classic state-compression domino tiling problem
 * Pattern:  Dynamic Programming | State machine | Bitmask frontier DP
 *
 * Example:
 *   Input:  n = 2
 *   Output: 3
 *   Why:    the board can be filled with three vertical dominoes or by either of
 *           the two horizontal-pair patterns across the two columns.
 *
 * Follow-ups:
 *   1. How would this extend to an m x n board?
 *      Use masks of m bits and generate transitions by DFS for each column frontier.
 *   2. What if trominoes are also allowed?
 *      Add tile placements to the transition generator; the mask DP framework stays the same.
 *   3. Can n be very large?
 *      Build the 8 x 8 transition matrix and exponentiate it in O(log n).
 */
public class TilingDominoes2 {

  private static final int MOD = 1_000_000_007;
  private static final int STATE_COUNT = 8; // 3-bit masks from 000 to 111
  private static final int FULL_MASK = 7;   // 111 in binary — all rows filled

    /**
   * Intuition: a 3 x n board needs frontier states because a partial column can
   * have several filled-cell masks. Moving column by column, each mask records
   * which cells of the current column are already occupied by horizontal dominoes
   * from the previous column.
   *
   * Algorithm:
   *   1. Reject odd lengths because a 3 x odd board has odd area.
   *   2. Keep counts for all eight occupancy masks.
   *   3. For each column, transition masks by placing vertical and horizontal dominoes.
   *   4. Return the full-mask count after processing all columns.
   *
   * Time:  O(length) - a constant number of mask transitions per column.
   * Space: O(1) - only eight mask states are stored.
   *
   * @param length board length
   * @return number of ways to tile a 3 x length board
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


    public static void main(String[] args) {
        int[] inputs = {1, 2, 4};
        int[] expected = {0, 3, 11};

        for (int i = 0; i < inputs.length; i++) {
            int output = countWaysToTile3xN(inputs[i]);
            System.out.printf("n=%d  ->  %d  expected=%d%n", inputs[i], output, expected[i]);
        }
    }

}