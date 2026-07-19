package arrays.greedy;

import java.util.Arrays;


/**
 * Problem: Minimum Number of Taps to Open to Water a Garden
 *
 * A garden is the line segment [0, n]. Tap i waters an interval centered at i
 * with radius ranges[i]. Open the fewest taps needed to cover the whole segment,
 * or return -1 when a gap cannot be covered.
 *
 * Leetcode: https://leetcode.com/problems/minimum-number-of-taps-to-open-to-water-a-garden/ (Hard)
 * Rating:   acceptance 53.1% (Hard) - contest rating 1885
 * Pattern:  Greedy | Interval covering | Jump Game II transform
 *
 * Example:
 *   Input:  n = 5, ranges = [3,4,1,1,0,0]
 *   Output: 1
 *   Why:    tap 1 covers [0,5], so one tap waters the entire garden.
 *
 * Follow-ups:
 *   1. Return the actual taps used?
 *      Track which interval provides each greedy extension.
 *   2. What if intervals arrive online?
 *      Use a priority queue of active intervals ordered by farthest right endpoint.
 *   3. What if the garden is two-dimensional?
 *      General disk or rectangle cover is much harder and often NP-hard.
 *
 * Related: Jump Game II (45), Video Stitching (1024).
 */
public class MinTaps {

  public static void main(String[] args) {
    MinTaps solver = new MinTaps();
    int[] gardenLengths = {5, 3, 7};
    int[][] ranges = {
        {3, 4, 1, 1, 0, 0},
        {0, 0, 0, 0},
        {1, 2, 1, 0, 2, 1, 0, 1}
    };
    int[] expected = {1, -1, 3};

    for (int i = 0; i < ranges.length; i++) {
      int got = solver.minTapsToWaterGarden(gardenLengths[i], ranges[i]);
      System.out.printf("n=%d ranges=%s -> %d  expected=%d%n",
          gardenLengths[i], Arrays.toString(ranges[i]), got, expected[i]);
    }
  }



  /**
   * Intuition: each tap is an interval, and covering [0, n] is the same shape as
   * Jump Game II. For every left boundary, remember the farthest right endpoint.
   * Then scan left to right, opening a tap only when the current committed
   * coverage ends.
   *
   * Algorithm:
   *   1. Convert each tap into an interval and store the best right endpoint in coverage[left].
   *   2. Scan garden points, updating currentReachable with the best interval seen so far.
   *   3. When i reaches maxReachable, open one tap and extend to currentReachable.
   *
   * Time:  O(n) - one pass builds coverage and one pass chooses taps.
   * Space: O(n) - coverage stores one farthest endpoint per garden position.
   *
   * @param gardenLength the length n of the garden segment [0, n]
   * @param ranges ranges[i] is the watering radius of tap i
   * @return minimum taps to open, or -1 if the garden cannot be fully watered
   */
  public int minTapsToWaterGarden(int gardenLength, int[] ranges) {

    // coverage[i] = farthest right position we can water
    // using a tap whose watering interval starts at position i.
    // Think of this as compressing all intervals by their left boundary.
    int[] coverage = new int[gardenLength + 1];

    // Convert taps into intervals and record the best interval for each left boundary.
    // Tap at position i waters:
    //      [i - ranges[i], i + ranges[i]]
    for (int i = 0; i <= gardenLength; i++) {
        int leftReachable = Math.max(0, i - ranges[i]);
        int rightReachable = Math.min(gardenLength, i + ranges[i]);

        // If multiple taps start at the same left boundary,
        // keep the one that extends farthest.
        coverage[leftReachable] = Math.max(coverage[leftReachable], rightReachable);
    }

    int tapsCount = 0;

    // maxReachable = end of coverage using taps we have already "opened"
    int maxReachable = 0;

    // currentReachable = farthest position we could reach
    // by opening one more tap among those seen so far
    int currentReachable = 0;

    // Scan from left to right through the garden.
    for (int i = 0; i < gardenLength; i++) {

        // If there is a tap whose interval starts at i,
        // update the best extension we can achieve.
        currentReachable = Math.max(currentReachable, coverage[i]);

        // When we reach the end of the current committed coverage,
        // we must open another tap to extend coverage.
        if (i == maxReachable) {

            // If even the best candidate tap cannot extend coverage,
            // there is a gap and watering the entire garden is impossible.
            if (currentReachable <= i) {
                return -1;
            }

            // Open the tap that extends coverage the farthest.
            tapsCount++;

            // Extend the committed coverage boundary.
            maxReachable = currentReachable;
        }
    }

    // If the final coverage reaches the end of the garden,
    // return the number of taps opened.
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
