package arrays.slidingwindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem: Jump Game II
 *
 * Each nums[i] is the maximum jump length from index i. Return the minimum
 * number of jumps needed to reach the last index; the original problem assumes
 * the last index is reachable.
 *
 * Leetcode: https://leetcode.com/problems/jump-game-ii/ (Medium)
 * Rating:   acceptance 41.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | BFS layers | Farthest jump window
 *
 * Example:
 *   Input:  nums = [2,3,1,1,4]
 *   Output: 2
 *   Why:    jump from index 0 to 1, then from index 1 to the last index.
 *
 * Follow-ups:
 *   1. Return the actual jump path?
 *      Track the best index inside each current jump window.
 *   2. What if the end may be unreachable?
 *      Return -1 when the farthest reach cannot move beyond the current index.
 *   3. What if jumps have costs?
 *      Use shortest-path or DP instead of unweighted greedy layers.
 *
 * Related: Jump Game (55), Minimum Number of Taps to Open to Water a Garden (1326).
 */
public class JumpGame2 {

  public static void main(String[] args) {
    JumpGame2 solver = new JumpGame2();
    int[][] inputs = {{2, 3, 1, 1, 4}, {2, 3, 0, 1, 4}, {0}};
    int[] expected = {2, 2, 0};

    for (int i = 0; i < inputs.length; i++) {
      int got = solver.minJumpsGreedy(inputs[i]);
      System.out.printf("nums=%s -> %d  expected=%d%n",
          Arrays.toString(inputs[i]), got, expected[i]);
    }
  }



  /**
   * Computes minimum jumps using bottom-up DP
   *
   * Time complexity : O(n²)
   * Space complexity : O(n)
   *
   * This approach is not preferred because of its time complexity.
   *
   * @param nums Array of max jump lengths at each index
   * @return Minimum number of jumps to reach the last index
   */
  public static int minJumpsDP(int[] nums) {
    int n = nums.length;
    int[] dp = new int[n]; // dp[i] denotes the minimum jumps to reach index i
    Arrays.fill(dp, Integer.MAX_VALUE);
    dp[0] = 0; // start at index 0

    for (int i = 1; i < n; i++) {
      for (int j = 0; j < i; j++) {
        // if j can reach i
        if (j + nums[j] >= i && dp[j] != Integer.MAX_VALUE) {
          dp[i] = Math.min(dp[i], dp[j] + 1);
        }
      }
    }

    return dp[n - 1];
  }

  /**
   * Intuition: treat all indexes reachable with the same number of jumps as one
   * BFS layer. currentReachable is the end of the current layer; maxReachable is
   * the farthest index reachable by taking one more jump from this layer.
   *
   * Algorithm:
   *   1. Scan indexes before the last one, extending maxReachable with i + nums[i].
   *   2. When i reaches currentReachable, take one jump and move the layer end to maxReachable.
   *   3. Return -1 if a layer cannot extend; otherwise return jumpsTaken.
   *
   * Time:  O(n) - one pass over the array.
   * Space: O(1) - only jump-window boundaries are stored.
   *
   * @param nums maximum jump length from each index
   * @return minimum jumps to reach the last index, or -1 if unreachable
   */
  public int minJumpsGreedy(int[] nums) {
    int jumpsTaken = 0;
    int currentReachable = 0;
    int maxReachable = 0;

    for (int i = 0; i < nums.length - 1; i++) {
      // Update farthest reach from this index
      maxReachable = Math.max(maxReachable, i + nums[i]);

      // If we have reached the end of current jump coverage
      if (i == currentReachable) {
        if (maxReachable <= i) return -1; // Cannot reach further
        jumpsTaken++;
        currentReachable = maxReachable;
      }
    }

    return jumpsTaken;
  }

  /**
   * Greedy approach to find the minimum number of jumps to reach the end of the array.
   *
   * Algorithm:
   * 1. Initialize `jumpsTaken` to 0, `currentJumpEnd` to 0, and `farthestReach` to 0.
   * 2. Iterate through the array:
   *    - For each index, update `farthestReach` based on the maximum reachable index from the current position.
   *    - If we reach the end of our current jump window (`currentJumpEnd`), increment `jumpsTaken` and update `currentJumpEnd`.
   *    - Track the indices we jumped from in a list (`path`).
   *
   * Time Complexity: O(n) — we iterate through the array once.
   * Space Complexity: O(1) — no extra memory is used.
   *
   * @param nums Array representing the maximum jump length at each position.
   * @return Minimum number of jumps to reach the last index.
   */
  public static Result calculateMinJumpsWithPath(int[] nums) {
    if (nums == null || nums.length <= 1) {
      return new Result(0, java.util.Collections.singletonList(0)); // Already at the end or invalid input
    }

    List<Integer> path = new ArrayList<>();
    int jumpsTaken = 0;
    int currentJumpEnd = 0;
    int farthestReach = 0;
    int bestJumpFrom = 0; // Index we plan to jump from next

    path.add(0); // Always start from index 0

    for (int i = 0; i < nums.length - 1; i++) {
      // Find how far we can reach from current index
      if (i + nums[i] > farthestReach) {
        // here we are calculating how far we can jump from the index but are not immediately jumping
        // by the time we reach end of the current jump window, we will have the best index to jump from
        farthestReach = i + nums[i];
        bestJumpFrom = i; // Track the index that gives this best reach
      }

      // Time to jump: we've reached the end of our current jump window
      if (i == currentJumpEnd) {
        jumpsTaken++;
        currentJumpEnd = farthestReach;

        // Add the index we’re jumping to
        path.add(bestJumpFrom);

        if (currentJumpEnd >= nums.length - 1) {
          path.add(nums.length - 1); // Add the final destination
          break;
        }
      }
    }

    return new Result(jumpsTaken, path);
  }

  /**
   * A helper class to store the result: number of jumps and the path taken.
   */
  static class Result {
    int jumpCount;
    List<Integer> jumpPath;

    Result(int count, List<Integer> path) {
      this.jumpCount = count;
      this.jumpPath = path;
    }
  }
}
