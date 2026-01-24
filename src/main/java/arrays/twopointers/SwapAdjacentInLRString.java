package arrays.twopointers;

import java.util.*;

/**
 * Problem Statement:
 * In a string composed of 'L', 'R', and 'X' characters, like "RXXLRXRXL", a move consists of either
 * replacing one occurrence of "XL" with "LX", or replacing one occurrence of "RX" with "XR".
 * Given the starting string start and the ending string end,
 * return True if and only if there exists a sequence of moves to transform start to end.
 *
 * Example:
 * Input: start = "RXXLRXRXL", end = "XRLXXRRLX"
 * Output: true
 * Explanation: We can transform start to end following these steps:
 * RXXLRXRXL -> XRXLRXRXL -> XRLXRXRXL -> XRLXXRRXL -> XRLXXRRLX
 * The sequence of 'L' and 'R' remains the same, but their positions are adjusted by moving 'L' left and 'R' right through 'X's.
 *
 * LeetCode Link: https://leetcode.com/problems/swap-adjacent-in-lr-string/
 *
 * Follow-up Questions:
 * 1. What if 'L' could also move right or 'R' left? - This would allow reordering, so we'd need to check if the multisets of characters are equal, but positions wouldn't matter as much. However, in this problem, order is fixed.
 * 2. How would you handle if swaps could happen over multiple 'X's at once? - That might require modeling as a graph or sliding window, but here swaps are only adjacent.
 * 3. Can we compute the minimum number of swaps? - Yes, by calculating the total distance each 'L' or 'R' needs to move, but ensuring no crossings.
 * Relevant follow-up problem: https://leetcode.com/problems/move-pieces-to-obtain-a-string/ (similar movement constraints).
 * LeetCode Contest Rating: 1939
 **/
public class SwapAdjacentInLRString {

  /**
   * Determines if the start string can be transformed into the end string using the allowed moves.
   *
   * Step-by-step Explanation:
   * 1. First, remove all 'X' from both strings and check if the resulting sequences of 'L' and 'R' are identical. If not, transformation is impossible.
   * 2. Use two pointers to traverse both strings, skipping 'X' characters.
   * 3. For each non-'X' character:
   *    - If it's 'R', ensure its position in start is <= position in end (can only move right).
   *    - If it's 'L', ensure its position in start is >= position in end (can only move left).
   * 4. If all matching characters satisfy the conditions and both pointers reach the end, return true.
   *
   * Logic Summary:
   * 1. Use two pointers to skip over 'X' in both `source` and `target`.
   * 2. When comparing non-'X' characters:
   *    - They must be the same.
   *    - 'R' can only move to the right (i.e., its index must increase).
   *    - 'L' can only move to the left (i.e., its index must decrease).
   * 3. If all characters satisfy the above rules, return true.
   *
   * Algorithm: Two-pointer technique with position checks.
   * Time Complexity: O(n), where n is the length of the strings (single pass).
   * Space Complexity: O(1), no extra space beyond variables.
   *
   * @param start the starting string
   * @param end the ending string
   * @return true if transformation is possible, false otherwise
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
