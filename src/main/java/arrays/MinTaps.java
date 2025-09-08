package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode Problem 1326: Minimum Number of Taps to Open to Water a Garden
 * https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/
 *
 * Problem Statement:
 * You are given an integer n representing the length of a one-dimensional garden [0, n], and an integer array ranges of length n + 1 where ranges[i] (0-indexed)
 * means the i-th tap can water the area [i - ranges[i], i + ranges[i]] if opened (interval bounds are clipped to [0, n]).
 * Return the minimum number of taps you need to open to water the entire garden. If it's impossible to water the garden, return -1.
 *
 * Example:
 * Input: n = 5, ranges = [3,4,1,1,0,0]
 * Output: 1
 * Explanation:
 * - Tap at position 0 waters [0,3].
 * - Tap at position 1 waters [0,5] (fully covers [0,5]).
 * - Tap at position 2 waters [1,3], and so on.
 * By opening only the tap at position 1, the whole garden [0,5] is watered.
 *
 * Follow-up Questions (FAANG interview style):
 * 1. What if the intervals are given unsorted or as random events?
 *    - You could generalize to interval covering problems, map to "minimum number of intervals to cover a line" (see LeetCode 452, 435).
 * 2. How would you efficiently update taps' locations or ranges frequently?
 *    - Data structures like segment trees, interval trees, or advanced greedy with update queries can help.
 * 3. Can you design for a 2D garden (or arbitrary-shaped domains)?
 *    - The problem generalizes to disk/rectangle covering; NP-hard in higher dimensions, often solved via approximation or greedy heuristics.
 * 4. How would you return the list of taps used in the optimal solution?
 *    - This requires tracking selections during the greedy decision (e.g., store indices while jumping).
 * Related Problems:
 * - Jump Game II (LeetCode 45): https://leetcode.com/problems/jump-game-ii/
 * - Minimum Number of Arrows to Burst Balloons (LeetCode 452): https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/
 */
public class MinTaps {

  public static void main(String[] args) {
    int target = 5;
    int[] ranges = {4, 3, 1, 2, 0, 0};
    int taps = new MinTaps().minTapsToWaterGarden(target, ranges);
    System.out.println("Minimum taps required: " + taps);
  }

  /**
   * Main solution method - Greedy (Interval Jump).
   *
   * Algorithm Overview:
   * 1. For each tap position, compute its reachable interval in the garden.
   * 2. For each garden point, store the farthest right it can be watered from any tap starting at or before that point.
   * 3. Use a greedy "jump" technique (similar to Jump Game II):
   *    - At each point, if we've reached the end of the current watering range, open the tap that extends coverage furthest.
   *    - If we can't extend coverage, return -1.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(n)
   *
   * @param gardenLength the length of the garden
   * @param ranges the watering ranges for each tap
   * @return minimum number of taps to open, or -1 if impossible
   */
  public int minTapsToWaterGarden(int gardenLength, int[] ranges) {
    // coverage[i] = farthest point we can water starting from position i
    // index is left boundary, value is right boundary
    int[] coverage = new int[gardenLength + 1];

    // Preprocess: build max coverage intervals for each tap
    for (int i = 0; i <= gardenLength; i++) {
      int leftReachable = Math.max(0, i - ranges[i]);
      int rightReachable = Math.min(gardenLength, i + ranges[i]);
      coverage[leftReachable] = Math.max(coverage[leftReachable], rightReachable);
    }

    int tapsCount = 0;
    int maxReachable = 0; // end of current coverage
    int currentReachable = 0; // farthest we can reach in the next step

    // Go from left to right in the garden. Keep tracking the best tap to open.
    // Once we reach the end of current coverage, we open the best tap found.
    for (int i = 0; i < gardenLength; i++) {
      int currTapLeftRange = i;
      int currTapRightRange = coverage[i];
      // It means that there is a tap that can water from currTapLeftRange to currTapRightRange

      currentReachable = Math.max(currentReachable, currTapRightRange);

      if (currTapLeftRange == maxReachable) { // we have reached the end of current coverage
        if (currentReachable <= currTapLeftRange) {
          // stuck: no tap can extend coverage
          return -1;
        }
        // Open the best tap found and extend coverage
        tapsCount++;
        maxReachable = currentReachable;
      }
    }

    return maxReachable >= gardenLength ? tapsCount : -1;
  }

  /**
   * Alternative solution
   *
   * Algorithm Overview:
   * 1. Start from the leftmost point of the garden.
   * 2. At each step, look for the tap that can cover the current point and extends coverage furthest to the right.
   * 3. If no such tap exists, return -1 (impossible to cover).
   * 4. Otherwise, open that tap and move the current coverage boundary to the rightmost point it can reach.
   * 5. Repeat until the entire garden is covered.
   *
   * Time Complexity: O(n^2) in worst case (due to nested loops).
   * Space Complexity: O(1)
   * @param target the length of the garden
   * @param ranges the watering ranges for each tap
   * @return minimum number of taps to open, or -1 if impossible
   */
  public int minTapsToWaterGardenUsingBrute(int target, int[] ranges) {
    int currReachable = 0;   // Left boundary of current watered region
    int maxReachable = 0;  // Rightmost garden position we can extend to
    int tapsOpened = 0;

    while (maxReachable < target) {
      int nextReachable = maxReachable;

      // Try all taps: pick the one that extends coverage the most
      for (int i = 0; i < ranges.length; i++) {
        int leftRange = i - ranges[i];
        int rightRange = i + ranges[i];

        if (leftRange <= currReachable && rightRange > nextReachable) {
          nextReachable = rightRange;
        }
      }

      if (nextReachable == maxReachable) {
        return -1; // No tap can extend coverage further → impossible
      }
      // Open the best tap found
      tapsOpened++;
      currReachable = maxReachable = nextReachable;
    }

    return tapsOpened;
  }
}