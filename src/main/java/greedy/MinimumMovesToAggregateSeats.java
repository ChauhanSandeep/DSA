package greedy;

/**
 * Problem: Seats - Minimum Moves to Aggregate All Occupied Seats
 *
 * Given a row of seats where 'X' means occupied and '.' means empty, move people
 * left or right so all occupied seats become one contiguous block. Return the
 * minimum number of one-seat moves, modulo 10000003.
 *
 * InterviewBit: https://www.interviewbit.com/problems/seats/
 * Pattern:      Greedy | Median target block | Preserve relative order
 *
 * Example:
 *   Input:  seatingConfiguration = "....X..XX...X.."
 *   Output: 5
 *   Why:    the four people gather around the middle occupied seats; moving them
 *           to positions 6,7,8,9 costs 2 + 0 + 0 + 3 = 5.
 *
 * Follow-ups:
 *   1. Minimize the maximum move any one person makes?
 *      Binary search the maximum distance and test whether a contiguous block is feasible.
 *   2. Handle a 2D grid of seats?
 *      Separate row and column movement; medians minimize Manhattan distance.
 *   3. Only some seats are allowed as destinations?
 *      Assign people to allowed positions with minimum-cost matching or dynamic programming.
 *   4. People have different movement costs?
 *      Use a weighted median when the destination block is flexible.
 *
 * Related: Best Meeting Point (296), Minimum Moves to Equal Array Elements II (462).
 */
public class MinimumMovesToAggregateSeats {
  public static void main(String[] args) {
    MinimumMovesToAggregateSeats solver = new MinimumMovesToAggregateSeats();
    String[] inputs = {"....X..XX...X..", "......", "X", "X.X.X"};
    int[] expected = {5, 0, 0, 2};

    for (int i = 0; i < inputs.length; i++) {
      int got = solver.calculateMinimumMoves(inputs[i]);
      System.out.printf("seats=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }
  private static final int MODULO = 10000003;

  /**
   * Intuition: people should never cross each other; swapping two people's
   * destinations only adds backtracking. So the task is choosing where the final
   * contiguous block starts. A median minimizes total absolute distance on a
   * line, and a block of consecutive seats is the same idea after subtracting
   * each person's offset inside the block. Anchoring the median person to the
   * median block slot gives the minimum total movement.
   *
   * Algorithm:
   *   1. Collect all occupied seat positions.
   *   2. Use the middle occupied seat as the median anchor.
   *   3. For each occupied seat, add distance to the median minus its block offset.
   *   4. Return the total modulo 10000003.
   *
   * Time:  O(n) - the row is scanned to collect positions and then the occupied positions are scanned once.
   * Space: O(k) - the positions array stores k occupied seats.
   *
   * @param seatingConfiguration string representing the row of seats
   * @return minimum number of moves required
   */
  public int calculateMinimumMoves(String seatingConfiguration) {
    if (seatingConfiguration == null || seatingConfiguration.isEmpty()) {
      return 0;
    }

    // Collect all occupied seat positions
    int[] occupiedPositions = getOccupiedPositions(seatingConfiguration);

    if (occupiedPositions.length <= 1) {
      return 0;
    }

    int medianIndex = occupiedPositions.length / 2; // Median index in the occupied positions array
    int medianPosition = occupiedPositions[medianIndex];

    return calculateMovesUsingMedian(occupiedPositions, medianIndex, medianPosition);
  }

  /** Collects all indices whose seats are occupied. */
  private int[] getOccupiedPositions(String seatingConfiguration) {
    // First count to allocate exact array size
    int count = 0;
    for (int i = 0; i < seatingConfiguration.length(); i++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(i))) {
        count++;
      }
    }

    // Collect positions
    int[] positions = new int[count];
    int index = 0;
    for (int i = 0; i < seatingConfiguration.length(); i++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(i))) {
        positions[index++] = i;
      }
    }

    return positions;
  }

  /** Computes total movement using the median-centered contiguous block formula. */
  private int calculateMovesUsingMedian(int[] positions, int medianIndex, int medianPosition) {
    long totalMoves = 0;

    for (int i = 0; i < positions.length; i++) {
      // Distance from current position to median position
      int distanceToMedian = Math.abs(positions[i] - medianPosition);

      // Distance of current index from median index (offset in consecutive
      // arrangement)
      int indexOffset = Math.abs(i - medianIndex);

      // Actual moves needed = distance to median - offset already accounted for
      long moves = distanceToMedian - indexOffset;
      totalMoves = (totalMoves + moves) % MODULO;
    }

    return (int) totalMoves;
  }

  /** Returns true when the seat character represents an occupied seat. */
  private boolean isOccupiedSeat(char seatChar) {
    return seatChar == 'X' || seatChar == 'x';
  }
}
