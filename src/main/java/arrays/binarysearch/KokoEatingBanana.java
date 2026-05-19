package arrays.binarysearch;

import java.util.Arrays;

/**
 * LeetCode Problem 875: Koko Eating Bananas
 * https://leetcode.com/problems/koko-eating-bananas/
 * Problem Statement:
 * Koko loves to eat bananas. There are n piles of bananas, the i-th pile has piles[i] bananas.
 * The guards have gone and will come back in h hours. Koko can decide her eating
 * speed of k bananas per hour. Each hour, she chooses a pile of bananas and eats k bananas from
 * that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any
 * more bananas until the next hour. Find the minimum eating speed k such that she can eat all the
 * bananas within h hours.
 *
 * Example: Input:
 * piles = [1,1,1,1,1,1,1,1,1,1], h = 10
 * Output: 1
 * Explanation: Koko must eat at least 1 banana per hour to finish all 10 piles in 10 hours. *
 *
 * Input:
 * piles = [312884132], h = 968709470
 * Output: 1
 * Explanation:
 * Even at 1 banana per hour, Koko can finish all piles.
 *
 * Follow-up Questions:
 * 1. What if there's a maximum eating speed limit?
 * Answer: Adjust maxSpeed accordingly and validate the constraint.
 * 2. How would you optimize if h is very small?
 * Answer: The algorithm already handles this - binary search will find minimum k efficiently.
 * 3. What if some piles must be eaten at specific times?
 * Answer: Convert to a different problem with time window constraints.
 *
 * Related Problems:
 * - 1891. Cutting Ribbons: https://leetcode.com/problems/cutting-ribbons/
 * - 1870. Minimum Speed to Arrive on Time: https://leetcode.com/problems/minimum-speed-to-arrive-on-time/
 *
 * LeetCode Contest Rating: Medium
 */
class KokoEatingBanana {
  /**
   * Finds the minimum eating speed for Koko to finish all bananas within allowed time.
   *
   * Algorithm (Binary Search):
   * 1. Set search range from 1 (minimum speed) to max pile size (maximum needed speed)
   * 2. For each candidate speed, calculate the total time needed to eat all piles
   * 3. If time needed <= allowed time, record this speed and search for slower speeds
   * 4. If time needed > allowed time, need faster speed, search in upper half
   * 5. Return the minimum valid speed found
   *
   * Time Complexity: O(n * log(max_pile)) - Binary search on speed range, for each speed we iterate all piles
   * Space Complexity: O(1) - Only using constant extra space
   *
   * @param piles Array of integers representing banana piles
   * @param allowedTime The time limit in hours
   * @return The minimum eating speed in bananas per hour
   */
  public int minEatingSpeed(int[] piles, int allowedTime) {
    int minSpeed = 1;
    int maxSpeed = Arrays.stream(piles).max().orElse(1); // Handle edge case of empty piles

    // Binary search for minimum valid speed
    while (minSpeed < maxSpeed) {
      // Calculate candidate speed (mid-point of current range)
      int currSpeed = minSpeed + (maxSpeed - minSpeed) / 2;

      // Calculate total time needed at current speed
      int timeNeeded = 0;
      for (int pile : piles) {
        timeNeeded += (int) Math.ceil((double)pile/currSpeed);
      }

      // If current speed is sufficient, try a slower speed
      if (timeNeeded <= allowedTime) {
        maxSpeed = currSpeed; // Can potentially go slower, narrow search to lower half
      } else {
        // Current speed is too slow, need faster speed
        minSpeed = currSpeed + 1; // Search in upper half
      }
    }

    return minSpeed;
  }
}
