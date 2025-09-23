package greedy;

/**
 * Problem: Seats - Minimum Moves to Aggregate All Occupied Seats
 *
 * Given a row of seats represented by '.' (empty) and 'X' (occupied),
 * find the minimum moves required to aggregate all 'X' together in a contiguous block.
 * A move consists of moving a person one position left or right to an empty seat.
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
 *    - Use minimax approach or binary search on the answer with feasibility check.
 * 2. What if there are different types of people and they need to be grouped separately?
 *    - Apply the same median-based approach for each group independently.
 * 3. How would you handle a 2D grid of seats?
 *    - Apply median approach in both dimensions, treat as separate 1D problems.
 *    - Related: https://leetcode.com/problems/minimum-moves-to-equal-array-elements/
 * 4. What if moving has different costs based on distance?
 *    - Use weighted median or dynamic programming approach.
 * 5. What if we can only move people to specific designated positions?
 *    - Model as minimum cost bipartite matching problem using Hungarian algorithm.
 */
public class MinimumMovesToAggregateSeats {

  public static void main(String[] args) {
    String seatingConfiguration = "....X..XX...X..";
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves(seatingConfiguration));

    // Test edge cases
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves("......"));     // 0 (no occupied seats)
    System.out.println(new MinimumMovesToAggregateSeats().calculateMinimumMoves("X"));          // 0 (single seat)
    System.out.println(
        new MinimumMovesToAggregateSeats().calculateMinimumMoves("XXX"));        // 0 (already aggregated)
  }

  private static final int MODULO = 10000003;


  /**
   * Greedy Approach: Median-Based Aggregation
   *
   * The key insight is that the median minimizes the sum of absolute deviations from a set of points.
   * This is because moving towards the median reduces the total distance more effectively than any other point.
   *
   * Algorithm: Direct Calculation without Extra Storage
   * Steps:
   * 1.Iterate through the string and count the total number of occupied seats ('X').
   * 2. Iterate again to find the position of the median occupied seat (the seat at index totalOccupiedSeats / 2 among all 'X').
   * 3. For each occupied seat to the left and right of the median occupied seat, calculate the moves needed to bring
   * them next to the median, summing the distances while skipping empty seats.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1) - constant space optimization
   *
   * @param seatingConfiguration string representing seat row
   * @return minimum number of moves required
   */
  public int calculateMinimumMoves(String seatingConfiguration) {
    if (seatingConfiguration == null || seatingConfiguration.isEmpty()) {
      return 0;
    }

    // Count total occupied seats
    int totalOccupiedSeats = countOccupiedSeats(seatingConfiguration);
    if (totalOccupiedSeats <= 1) {
      return 0;
    }

    // Find median position in single pass
    int medianRank = totalOccupiedSeats / 2;
    int medianPosition = findMedianPosition(seatingConfiguration, medianRank);

    return calculateMoves(seatingConfiguration, medianPosition, totalOccupiedSeats);
  }

  /**
   * Counts total number of occupied seats in the configuration.
   *
   * @param seatingConfiguration string representing seat row
   * @return count of occupied seats
   */
  private int countOccupiedSeats(String seatingConfiguration) {
    int count = 0;
    for (int i = 0; i < seatingConfiguration.length(); i++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(i))) {
        count++;
      }
    }
    return count;
  }

  /**
   * Finds the position of the median occupied seat.
   *
   * @param seatingConfiguration string representing seat row
   * @param medianRank the rank of median element (0-based)
   * @return position of median occupied seat
   */
  private int findMedianPosition(String seatingConfiguration, int medianRank) {
    int currentRank = 0;

    for (int position = 0; position < seatingConfiguration.length(); position++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(position))) {
        if (currentRank == medianRank) {
          return position;
        }
        currentRank++;
      }
    }

    return -1; // Should never reach here with valid input
  }

  /**
   * Calculates total moves required to form a contiguous block centered around median position.
   *
   * Maps each occupied seat to its target position in the contiguous block.
   * The contiguous block is formed with the median seat at the center, and all other seats
   * are mapped to consecutive positions around it.
   *
   * @param seating string representing seat row
   * @param medianPosition the center position for aggregation
   * @param totalOccupiedSeats total number of occupied seats
   * @return total number of moves required
   */
  private int calculateMoves(String seating, int medianPosition, int totalOccupiedSeats) {
    long totalMoves = 0;

    // Calculate the starting position of the contiguous block
    // The block should be centered around the median position
    int blockStartPosition = medianPosition - (totalOccupiedSeats / 2);

    int targetIndexInBlock = 0; // Index within the contiguous block (0, 1, 2, ...)

    // Iterate through all positions and map occupied seats to target positions
    for (int currentPosition = 0; currentPosition < seating.length(); currentPosition++) {
      if (isOccupiedSeat(seating.charAt(currentPosition))) {
        // Calculate target position in the contiguous block
        int targetPosition = blockStartPosition + targetIndexInBlock;

        // Add the absolute distance (number of moves required)
        long moves = Math.abs(currentPosition - targetPosition);
        totalMoves = (totalMoves + moves) % MODULO;

        targetIndexInBlock++; // Move to next position in the contiguous block
      }
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
