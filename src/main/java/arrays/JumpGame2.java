package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Leetcode Problem: Jump Game II
 * Link: https://leetcode.com/problems/jump-game-ii/
 *
 * Problem Statement:
 * Given an array of non-negative integers `nums` where each element represents your maximum jump length at that position,
 * return the minimum number of jumps required to reach the last index.
 * You can assume that you can always reach the last index.
 *
 * Example:
 * Input: nums = [2, 3, 1, 1, 4]
 * Output: 2
 * Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
 *
 * Follow-up Questions:
 * 1. What if we want to print the actual jump path?
 *    → Use a tracking array to record indices from where we jumped.
 *
 * 2. Can we solve this using DP?
 *    → Yes, but it will be O(n²) time and less optimal than greedy.
 *
 * 3. What if `nums[i]` can be negative?
 *    → The original problem assumes non-negative inputs. With negatives, we need a different problem model (e.g., graph BFS/DFS).
 */
public class JumpGame2 {

  public static void main(String[] args) {
    int[] nums = {2, 3, 1, 1, 4};
    Result result = calculateMinJumpsWithPath(nums);

    System.out.println("Minimum jumps to reach the end: " + result.jumpCount);
    System.out.println("Jump path (indices): " + result.jumpPath);
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
   * - The approach keeps track of the farthest reachable index in the current jump window.
   * - Whenever the current index reaches the end of this window, a jump is made, and the window
   * is extended to the farthest reachable index calculated so far.
   * - This ensures that the number of jumps is minimized by always jumping to the farthest reachable index.
   *
   * Time Complexity: O(n), where n is the size of the input array.
   * Space Complexity: O(1), as we use only a few extra variables.
   *
   * @param nums An array of non-negative integers representing maximum jump lengths.
   * @return The minimum number of jumps needed to reach the last index.
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