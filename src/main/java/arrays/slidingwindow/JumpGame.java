package arrays.slidingwindow;

import java.util.Arrays;

/**
 * Problem: Jump Game
 *
 * Each nums[i] is the farthest jump length available from index i. Starting at
 * index 0, decide whether any sequence of jumps can reach the last index.
 *
 * Leetcode: https://leetcode.com/problems/jump-game/ (Medium)
 * Rating:   acceptance 39.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Farthest reachable index
 *
 * Example:
 *   Input:  nums = [2,3,1,1,4]
 *   Output: true
 *   Why:    jump from index 0 to 1, then from index 1 to the last index.
 *
 * Follow-ups:
 *   1. Return the minimum number of jumps?
 *      Use the windowed greedy solution from Jump Game II.
 *   2. Return one valid path?
 *      Track the index that extends reach whenever a jump window closes.
 *   3. What if negative jumps are allowed?
 *      Model indices as graph nodes and use BFS/DFS with visited tracking.
 *
 * Related: Jump Game II (45), Jump Game III (1306), Jump Game VII (1871).
 */
public class JumpGame {

  public static void main(String[] args) {
    JumpGame solver = new JumpGame();
    int[][] inputs = {{2, 3, 1, 1, 4}, {3, 2, 1, 0, 4}, {0}};
    boolean[] expected = {true, false, true};

    for (int i = 0; i < inputs.length; i++) {
      boolean got = solver.canJump(inputs[i]);
      System.out.printf("nums=%s -> %s  expected=%s%n",
          Arrays.toString(inputs[i]), got, expected[i]);
    }
  }



  /**
   * Brute-force Dynamic Programming approach.
   *
   * Steps:
   * - Initialize a boolean array `canReach` where canReach[i] indicates if index i can reach the end.
   * - Start from the last index and work backwards.
   * - For each index, check if any jump leads to a position marked true.
   *
   * Time Complexity: O(n^2)
   * Space Complexity: O(n)
   *
   * This approach is inefficient for large arrays and is mainly useful for understanding the problem structure.
   */
  public static boolean canJumpDP(int[] nums) {
    boolean[] canReach = new boolean[nums.length];
    canReach[nums.length - 1] = true;

    for (int i = nums.length - 2; i >= 0; i--) {
      int maxJump = nums[i];
      for (int jump = 1; jump <= maxJump && i + jump < nums.length; jump++) {
        if (canReach[i + jump]) {
          canReach[i] = true;
          break;
        }
      }
    }
    return canReach[0];
  }

  /**
   * Intuition: while scanning left to right, maxReachable is the farthest index
   * any reachable position has offered so far. If the scan ever steps beyond it,
   * that index and everything after it are unreachable.
   *
   * Algorithm:
   *   1. Start maxReachable at 0.
   *   2. Scan each index; return false if i is beyond maxReachable.
   *   3. Update maxReachable with i + nums[i] and return true once it reaches the end.
   *
   * Time:  O(n) - at most one pass over nums.
   * Space: O(1) - only the farthest reachable index is stored.
   *
   * @param nums maximum jump length from each index
   * @return true if the last index is reachable
   */
  public boolean canJump(int[] nums) {
    int length = nums.length;
    if (length <= 1) {
      return true; // Already at the end or single element
    }

    int maxReachable = 0;

    for (int i = 0; i < length; i++) {
      if (i > maxReachable) {
        return false; // Cannot reach this position
      }
      // Update the farthest we can reach
      maxReachable = Math.max(maxReachable, i + nums[i]);
      if (maxReachable >= length - 1) {
        return true; // Can reach or beyond the end
      }
    }
    return false;
  }

  /**
   * Optimized Greedy approach.
   *
   * Steps:
   * - Track the last known index from which the end can be reached (`lastReachableIndex`).
   * - Traverse the array from end to start.
   * - If from current index `i`, a jump can reach `lastReachableIndex`, update it to `i`.
   * - Finally, check if `lastReachableIndex` is 0.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * This is the most efficient solution and is suitable for interviews.
   */
  public static boolean canJumpGreedy(int[] nums) {
    int minStartIndexToReachEnd = nums.length - 1;

    for (int i = nums.length - 2; i >= 0; i--) {
      if (i + nums[i] >= minStartIndexToReachEnd) {
        minStartIndexToReachEnd = i;
      }
    }
    return minStartIndexToReachEnd == 0;
  }
}
