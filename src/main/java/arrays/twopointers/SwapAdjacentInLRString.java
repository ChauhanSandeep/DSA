package arrays.twopointers;

import java.util.*;

/**
 * Problem: Swap Adjacent in LR String
 *
 * In strings made of L, R, and X, moves replace XL with LX or RX with XR. Return
 * whether start can transform into end under those movement constraints.
 *
 * Leetcode: https://leetcode.com/problems/swap-adjacent-in-lr-string/ (Medium)
 * Rating:   zerotrac 1939 (Q3, weekly-contest-70)
 * Pattern:  String | Two pointers | Direction-constrained movement
 *
 * Example:
 *   Input:  start = "RXXLRXRXL", end = "XRLXXRRLX"
 *   Output: true
 *   Why:    the L/R order is unchanged; each R only moves right and each L only moves left.
 *
 * Follow-ups:
 *   1. Compute the minimum number of swaps?
 *      Sum the movement distances of matching L/R pieces after validating constraints.
 *   2. What if L and R could move both directions?
 *      Then only character counts would matter, because directional constraints disappear.
 *   3. Support more piece types with different directions?
 *      Validate the non-X sequence and apply a direction rule for each piece type.
 *
 * Related: Move Pieces to Obtain a String (2337), Transform String (777).
 */
public class SwapAdjacentInLRString {

public static void main(String[] args) {
  SwapAdjacentInLRString solver = new SwapAdjacentInLRString();
  String[][] cases = { {"RXXLRXRXL", "XRLXXRRLX"}, {"X", "R"} };
  boolean[] expected = { true, false };

  for (int i = 0; i < cases.length; i++) {
    boolean got = solver.canTransform(cases[i][0], cases[i][1]);
    System.out.printf("start=%s end=%s -> %s  expected=%s%n",
        cases[i][0], cases[i][1], got, expected[i]);
  }
}

  /**
 * Intuition: X is empty space, so removing X must leave the same sequence of
 * L and R pieces. After that, only direction matters: R can move right but not
 * left, and L can move left but not right. Two pointers compare the next real
 * piece in each string and enforce those index rules.
 *
 * Algorithm:
 *   1. Return false for different lengths or different non-X sequences.
 *   2. Move startIndex and endIndex forward, skipping X in both strings.
 *   3. Compare the next non-X characters and reject mismatches.
 *   4. Reject L moving right or R moving left; otherwise advance both pointers.
 *
 * Time:  O(n) - each pointer scans its string once.
 * Space: O(1) - aside from removeX results used by the original code path.
 *
 * @param start starting configuration
 * @param end desired ending configuration
 * @return true when start can transform into end
 */
  public boolean canTransform(String start, String end) {
    // Quick check for length mismatch, though constraints ensure same length
    if (start.length() != end.length()) {
      return false;
    }

    // Check if sequences without 'X' match
    if (!removeX(start).equals(removeX(end))) {
      return false;
    }

    int startIndex = 0; // Pointer for start
    int endIndex = 0; // Pointer for end

    while (startIndex < start.length() && endIndex < end.length()) {
      // Skip 'X' in start
      while (startIndex < start.length() && start.charAt(startIndex) == 'X') {
        startIndex++;
      }
      // Skip 'X' in end
      while (endIndex < end.length() && end.charAt(endIndex) == 'X') {
        endIndex++;
      }

      // If both reached end, success
      if (startIndex == start.length() && endIndex == end.length()) {
        return true;
      }

      // If one reached end but not the other, fail (shouldn't happen if sequences match)
      if (startIndex == start.length() || endIndex == end.length()) {
        return false;
      }

      char startChar = start.charAt(startIndex);
      // Ensure characters match (they should if sequences are equal)
      if (startChar != end.charAt(endIndex)) {
        return false;
      }

      // At this point both pointers are at non-'X' and same characters
      // but if startChar is 'L' and startIndex is less than targetIndex, then
      // it means 'L' is trying to move right which is not allowed
      if (startChar == 'L' && startIndex < endIndex) {
        // 'L' cannot move right
        return false;
      }

      // At this point both pointers are at non-'X' and same characters
      // but if startChar is 'R' and startIndex is greater than targetIndex, then
      // it means 'R' is trying to move left which is not allowed
      if (startChar == 'R' && startIndex > endIndex) {
        // 'R' cannot move left
        return false;
      }

      startIndex++;
      endIndex++;
    }

    return true;
  }

  // Helper method to remove 'X' from a string and return the result
  private String removeX(String s) {
    StringBuilder sb = new StringBuilder();
    for (char c : s.toCharArray()) {
      if (c != 'X') {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Alternative approach: Collect positions of 'L' and 'R' in both strings and compare them.
   *
   * Step-by-step Explanation:
   * 1. Collect lists of positions for 'L' and 'R' in start and end, ignoring 'X'.
   * 2. Ensure the sequences of characters match.
   * 3. For each corresponding 'L', check start position >= end position.
   * 4. For each corresponding 'R', check start position <= end position.
   *
   * Algorithm: Position collection and comparison.
   * Time Complexity: O(n), single pass to collect positions.
   * Space Complexity: O(n), for storing positions.
   *
   * @param start the starting string
   * @param end the ending string
   * @return true if transformation is possible, false otherwise
   */
  public boolean canTransformAlternative(String start, String end) {
    if (start.length() != end.length()) {
      return false;
    }

    // Lists to hold positions of non-'X' characters
    List<Integer> startL = new ArrayList<>();
    List<Integer> startR = new ArrayList<>();
    List<Integer> endL = new ArrayList<>();
    List<Integer> endR = new ArrayList<>();

    // Collect positions
    for (int i = 0; i < start.length(); i++) {
      char startChar = start.charAt(i);
      char endChar = end.charAt(i);

      if (startChar == 'L') startL.add(i);
      else if (startChar == 'R') startR.add(i);

      if (endChar == 'L') endL.add(i);
      else if (endChar == 'R') endR.add(i);
    }

    // Check if number of 'L' and 'R' match
    if (startL.size() != endL.size() || startR.size() != endR.size()) {
      return false;
    }

    // Check 'R' positions: start <= end
    for (int i = 0; i < startR.size(); i++) {
      if (startR.get(i) > endR.get(i)) {
        return false; // 'R' cannot move left so start position must be <= end position
      }
    }

    // Check 'L' positions: start >= end
    for (int i = 0; i < startL.size(); i++) {
      if (startL.get(i) < endL.get(i)) {
        return false; // 'L' cannot move right so start position must be >= end position
      }
    }

    return true;
  }
}
