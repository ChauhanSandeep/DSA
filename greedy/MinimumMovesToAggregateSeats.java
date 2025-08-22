package greedy;

import java.util.ArrayList;
import java.util.List;


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
   * Calculates the minimum number of moves required to aggregate all occupied seats.
   *
   * Algorithm: Median-based Greedy Approach
   * Steps:
   * 1. Find all occupied seat positions
   * 2. Determine median position (optimal aggregation center)
   * 3. Calculate moves needed to bring all seats to median vicinity
   * 4. Sum moves for left side seats and right side seats separately
   *
   * Time Complexity: O(n) where n is length of seating row
   * Space Complexity: O(k) where k is number of occupied seats
   *
   * @param seatingConfiguration string representing seat row ('.' = empty, 'X' = occupied)
   * @return minimum number of moves required modulo 10000003
   */
  public int calculateMinimumMoves(String seatingConfiguration) {
    if (seatingConfiguration == null || seatingConfiguration.isEmpty()) {
      return 0;
    }

    List<Integer> occupiedSeatPositions = extractOccupiedPositions(seatingConfiguration);

    // Handle edge cases
    if (occupiedSeatPositions.size() <= 1) {
      return 0; // No moves needed for 0 or 1 occupied seats
    }

    int medianSeatIndex = findOptimalAggregationPosition(occupiedSeatPositions);
    return computeTotalMovesToMedian(occupiedSeatPositions, medianSeatIndex);
  }

  /**
   * Optimized single-pass solution that calculates moves without storing all positions.
   *
   * Algorithm: Direct Calculation without Extra Storage
   * Steps:
   * 1. First pass: count total occupied seats
   * 2. Second pass: find median position directly
   * 3. Third pass: calculate total moves using running position counters
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1) - constant space optimization
   *
   * @param seatingConfiguration string representing seat row
   * @return minimum number of moves required
   */
  public int calculateMinimumMovesOptimized(String seatingConfiguration) {
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
    int medianPosition = findMedianPositionDirectly(seatingConfiguration, medianRank);

    return calculateMovesDirectly(seatingConfiguration, medianPosition);
  }

  /**
   * Extracts positions of all occupied seats from the seating configuration.
   *
   * @param seatingConfiguration string representing seat row
   * @return list of indices where seats are occupied
   */
  private List<Integer> extractOccupiedPositions(String seatingConfiguration) {
    List<Integer> occupiedPositions = new ArrayList<>();

    for (int position = 0; position < seatingConfiguration.length(); position++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(position))) {
        occupiedPositions.add(position);
      }
    }

    return occupiedPositions;
  }

  /**
   * Determines the optimal position for seat aggregation using median approach.
   *
   * The median minimizes the sum of absolute deviations, making it optimal
   * for minimizing total movement distance.
   *
   * @param occupiedPositions list of occupied seat positions (already sorted)
   * @return index of median occupied seat position
   */
  private int findOptimalAggregationPosition(List<Integer> occupiedPositions) {
    int medianIndex = occupiedPositions.size() / 2;
    return occupiedPositions.get(medianIndex);
  }

  /**
   * Computes total moves required to aggregate all seats around the median position.
   *
   * Algorithm Details:
   * - Left side seats: move towards median from left, accounting for intermediate positions
   * - Right side seats: move towards median from right, accounting for intermediate positions
   * - Use consecutive target positions to avoid overlapping
   *
   * @param occupiedPositions list of all occupied seat positions
   * @param medianPosition the optimal center position for aggregation
   * @return total number of moves required
   */
  private int computeTotalMovesToMedian(List<Integer> occupiedPositions, int medianPosition) {
    long totalMoves = 0;
    int medianIndexInList = occupiedPositions.indexOf(medianPosition);

    // Calculate moves for seats to the left of median
    totalMoves += calculateLeftSideMoves(occupiedPositions, medianIndexInList, medianPosition);

    // Calculate moves for seats to the right of median
    totalMoves += calculateRightSideMoves(occupiedPositions, medianIndexInList, medianPosition);

    return (int) (totalMoves % MODULO);
  }

  /**
   * Calculates moves required for seats positioned to the left of median.
   *
   * @param positions list of occupied positions
   * @param medianIndex index of median in the positions list
   * @param medianPosition actual position value of median
   * @return total moves for left side seats
   */
  private long calculateLeftSideMoves(List<Integer> positions, int medianIndex, int medianPosition) {
    long leftSideMoves = 0;
    int targetPosition = medianPosition - 1;

    for (int i = medianIndex - 1; i >= 0; i--) {
      int currentPosition = positions.get(i);
      leftSideMoves = (leftSideMoves + (targetPosition - currentPosition)) % MODULO;
      targetPosition--; // Next person will occupy the adjacent left position
    }

    return leftSideMoves;
  }

  /**
   * Calculates moves required for seats positioned to the right of median.
   *
   * @param positions list of occupied positions
   * @param medianIndex index of median in the positions list
   * @param medianPosition actual position value of median
   * @return total moves for right side seats
   */
  private long calculateRightSideMoves(List<Integer> positions, int medianIndex, int medianPosition) {
    long rightSideMoves = 0;
    int targetPosition = medianPosition + 1;

    for (int i = medianIndex + 1; i < positions.size(); i++) {
      int currentPosition = positions.get(i);
      rightSideMoves = (rightSideMoves + (currentPosition - targetPosition)) % MODULO;
      targetPosition++; // Next person will occupy the adjacent right position
    }

    return rightSideMoves;
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
   * Finds median position directly without storing all positions.
   *
   * @param seatingConfiguration string representing seat row
   * @param medianRank the rank of median element (0-based)
   * @return position of median occupied seat
   */
  private int findMedianPositionDirectly(String seatingConfiguration, int medianRank) {
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
   * Calculates total moves directly without storing intermediate positions.
   *
   * @param seatingConfiguration string representing seat row
   * @param medianPosition the center position for aggregation
   * @return total number of moves required
   */
  private int calculateMovesDirectly(String seatingConfiguration, int medianPosition) {
    long totalMoves = 0;
    int leftTargetPosition = medianPosition - 1;
    int rightTargetPosition = medianPosition + 1;

    // Process left side
    for (int position = medianPosition - 1; position >= 0; position--) {
      if (isOccupiedSeat(seatingConfiguration.charAt(position))) {
        totalMoves = (totalMoves + (leftTargetPosition - position)) % MODULO;
        leftTargetPosition--;
      }
    }

    // Process right side
    for (int position = medianPosition + 1; position < seatingConfiguration.length(); position++) {
      if (isOccupiedSeat(seatingConfiguration.charAt(position))) {
        totalMoves = (totalMoves + (position - rightTargetPosition)) % MODULO;
        rightTargetPosition++;
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
