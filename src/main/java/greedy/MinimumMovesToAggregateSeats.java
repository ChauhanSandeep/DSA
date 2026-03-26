package greedy;

/**
 * Problem: Seats - Minimum Moves to Aggregate All Occupied Seats
 *
 * Given a row of seats represented by '.' (empty) and 'X' (occupied),
 * find the minimum moves required to aggregate all 'X' together in a contiguous
 * block.
 * A move consists of moving a person one position left or right to an empty
 * seat.
 *
 * Example:
 * Input: "....X..XX...X.."
 * Output: 5
 * Explanation: Move seats to form "......XXXX....." (5 total moves)
 *
 * InterviewBit: https://www.interviewbit.com/problems/seats/
 *
 * Follow-up Questions (FAANG-style):
 * 1. What if we want to minimize the maximum distance any person has to move?
 * - Use minimax approach or binary search on the answer with feasibility check.
 * 2. What if there are different types of people and they need to be grouped
 * separately?
 * - Apply the same median-based approach for each group independently.
 * 3. How would you handle a 2D grid of seats?
 * - Apply median approach in both dimensions, treat as separate 1D problems.
 * - Related:
 * https://leetcode.com/problems/minimum-moves-to-equal-array-elements/
 * 4. What if moving has different costs based on distance?
 * - Use weighted median or dynamic programming approach.
 * 5. What if we can only move people to specific designated positions?
 * - Model as minimum cost bipartite matching problem using Hungarian algorithm.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MinimumMovesToAggregateSeats {

  public static void main(String[] args) {
    String seatingConfiguration = "....X..XX...X..";
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves(seatingConfiguration));

    // Test edge cases
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves("......")); // 0 (no occupied seats)
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves("X")); // 0 (single seat)
    System.out.println(
        new MinimumMovesToAggregateSeats().calculateMinimumMoves("XXX")); // 0 (already aggregated)
  }

  private static final int MODULO = 10000003;

  /**
   * Greedy Approach: Median-Based Simplified Formula
   *
   * Key Insight:
   * The median minimizes the sum of absolute deviations. To form a contiguous
   * block,
   * each seat needs to move to the median position minus its offset from the
   * median index.
   *
   * Formula: For each occupied seat at index i with position p[i]:
   * moves = |p[i] - median_position| - |i - median_index|
   *
   * This accounts for both:
   * 1. Distance to median position
   * 2. Offset needed to form consecutive seats (subtract relative index distance)
   *
   * Algorithm:
   * 1. Collect all occupied seat positions
   * 2. Find median position (position of median occupied seat)
   * 3. Apply formula: sum(|pos[i] - median_pos| - |i - median_idx|)
   *
   * Time Complexity: O(n) - single pass to collect positions, single pass to
   * calculate
   * Space Complexity: O(k) - where k is number of occupied seats
   *
   * @param seatingConfiguration string representing seat row
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

  /**
   * Collects all positions where seats are occupied.
   *
   * @param seatingConfiguration string representing seat row
   * @return array of occupied positions
   */
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

  /**
   * Calculates total moves using simplified median formula.
   *
   * For each seat: distance_to_median - distance_of_index_from_median_index
   * This directly computes how much extra movement is needed beyond reaching
   * median
   * to form consecutive seats.
   *
   * @param positions      array of occupied seat positions
   * @param medianIndex    index of median seat in the array
   * @param medianPosition position of median seat in the row
   * @return total number of moves required
   */
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

  /**
   * Checks if a seat character represents an occupied seat.
   *
   * @param seatChar character representing seat state
   * @return true if seat is occupied, false otherwise
   */
  private boolean isOccupiedSeat(char seatChar) {
    return seatChar == 'X' || seatChar == 'x';
  }
}
