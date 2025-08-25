package arrays;

/**
 * Leetcode Problem: Jump Game
 * Link: https://leetcode.com/problems/jump-game/
 *
 * Problem Statement:
 * You are given an array of non-negative integers `nums`. Each element represents your maximum jump length at that position.
 * Starting at index 0, determine if you can reach the last index.
 *
 * Example:
 * Input: nums = [2, 3, 1, 1, 4]
 * Output: true
 * Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
 *
 * Follow-up Questions:
 * 1. What if we also want to return the minimum number of jumps to reach the end?
 *    → See Leetcode: https://leetcode.com/problems/jump-game-ii/
 *
 * 2. Can we find all possible paths to reach the end?
 *    → Brute-force backtracking or BFS-style search, but not feasible for large inputs.
 *
 * 3. What if some elements are negative?
 *    → The original problem assumes non-negative integers. If negatives are allowed, it becomes a different problem (may require graph traversal or DP with memoization).
 */
public class JumpGame {

  public static void main(String[] args) {
    int[] nums = {2, 3, 1, 1, 4};
    System.out.println("Can jump to the end? " + canJumpGreedy(nums));
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
    int lastReachableIndex = nums.length - 1;

    for (int i = nums.length - 2; i >= 0; i--) {
      if (i + nums[i] >= lastReachableIndex) {
        lastReachableIndex = i;
      }
    }
    return lastReachableIndex == 0;
  }
}