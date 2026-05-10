package backtrack;

import java.util.Arrays;

/**
 * 473. Matchsticks to Square
 *
 * Problem:
 * Given an integer array matchsticks where matchsticks[i] is the length of the i-th matchstick,
 * return true if you can make one square using all the matchsticks, otherwise return false.
 *
 * Notes:
 * - You cannot break any stick.
 * - You must use every matchstick exactly once.
 *
 * LeetCode: https://leetcode.com/problems/matchsticks-to-square/
 *
 * Example:
 * Input: [1,1,2,2,2]
 * Output: true
 * Explanation: One possible square has sides [2,2,2,2].
 *
 * Follow-up questions:
 * 1. How do we reduce the search space in backtracking?
 *    - Sort sticks in descending order, place larger sticks first, and prune symmetric states.
 * 2. Can this be solved with bitmask DP?
 *    - Yes, subset DP can solve it, but backtracking with pruning is usually simpler and faster in practice.
 * 3. What if we need to construct rectangles instead of only squares?
 *    - Similar partitioning idea, but side constraints change (2 pairs of equal sides for rectangles).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MatchsticksToSquare {

  public static void main(String[] args) {
    MatchsticksToSquare solution = new MatchsticksToSquare();
    System.out.println(solution.makesquare(new int[] {1, 1, 2, 2, 2})); // true
    System.out.println(solution.makesquare(new int[] {3, 3, 3, 3, 4})); // false
  }

  /**
   * Backtracking + pruning solution.
   *
   * Algorithm:
   * 1. Compute total sum; if not divisible by 4, impossible.
   * 2. Sort matchsticks in descending order so larger sticks are placed first.
   * 3. Recursively assign each stick to one of 4 sides if side length does not exceed target.
   * 4. Try placing the current stick on each side and backtrack.
   *
   * Time Complexity: O(4^n) in worst case, but pruning greatly reduces practical runtime.
   * Space Complexity: O(n) recursion depth + O(1) extra side array.
   *
   * @param matchsticks lengths of available matchsticks
   * @return true if all sticks can form a square, false otherwise
   */
  public boolean makesquare(int[] matchsticks) {
    if (matchsticks == null || matchsticks.length < 4) {
      return false;
    }

    int length = matchsticks.length;
    int sum = 0;
    for (int stick : matchsticks) {
      sum += stick;
    }

    if (sum % 4 != 0) {
      return false;
    }

    int target = sum / 4;

    Arrays.sort(matchsticks);
    if (matchsticks[length - 1] > target) {
      return false;
    }

    int[] sideLengths = new int[4];
    return canBuildSquare(matchsticks, length - 1, sideLengths, target);
  }

  private boolean canBuildSquare(int[] matchsticks, int index, int[] sideLengths, int target) {
    if (index == -1) {
      for (int side = 0; side < 4; side++) {
        if (sideLengths[side] != target) {
          return false;
        }
      }
      return true;
    }

    int currentStick = matchsticks[index];
    
    for (int side = 0; side < 4; side++) {
      if (sideLengths[side] + currentStick > target) {
        continue;
      }

      boolean duplicateState = false;
      for (int prev = 0; prev < side; prev++) {
        if (sideLengths[prev] == sideLengths[side]) {
          duplicateState = true;
          break;
        }
      }
      // Same side length means same path of recursive tree, so skip duplicate branch.
      if (duplicateState) {
        continue;
      }

      sideLengths[side] += currentStick;
      if (canBuildSquare(matchsticks, index - 1, sideLengths, target)) {
        return true;
      }
      sideLengths[side] -= currentStick;
    }

    return false;
  }

}
