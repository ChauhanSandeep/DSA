package arrays.binarysearch;

import java.util.Arrays;

/**
 * Problem: Koko Eating Bananas
 *
 * Koko chooses one integer eating speed and eats from one pile per hour. Return the smallest speed that finishes all piles within h hours.
 *
 * Leetcode: https://leetcode.com/problems/koko-eating-bananas/ (Medium)
 * Rating:   zerotrac 1766 (Q3, weekly-94)
 * Pattern:  Binary search on answer | Monotonic feasibility | Ceiling division
 *
 * Example:
 *   Input:  piles = [3,6,7,11], h = 8
 *   Output: 4
 *   Why:    speed 4 finishes in 8 hours, while speed 3 is too slow.
 *
 * Follow-ups:
 *   1. Pile deadlines? This becomes a scheduling feasibility problem.
 *   2. Can split an hour across piles? The model changes to total work per hour.
 *   3. Maximum allowed speed? Search only up to that cap and report impossible if needed.
 *   4. Avoid floating point? Use integer ceiling division.
 *
 * Related: Capacity To Ship Packages Within D Days (1011), Minimum Speed to Arrive on Time (1870).
 */
class KokoEatingBanana {

  public static void main(String[] args) {
    KokoEatingBanana solver = new KokoEatingBanana();
    int[][] piles = { {3,6,7,11}, {312884132}, {30,11,23,4,20} };
    int[] hours = { 8, 968709470, 5 };
    int[] expected = { 4, 1, 30 };
    for (int i = 0; i < piles.length; i++) {
      int got = solver.minEatingSpeed(piles[i], hours[i]);
      System.out.printf("piles=%s h=%d -> %d  expected=%d%n", Arrays.toString(piles[i]), hours[i], got, expected[i]);
    }
  }

    /**
   * Intuition: Eating speed is monotonic: if speed k works, faster speeds work. Simulate hours for a candidate speed and binary search the smallest feasible speed.
   *
   * Algorithm:
   *   1. Search speeds from 1 to the largest pile.
   *   2. Compute total hours needed at currSpeed.
   *   3. If hours fit, keep the lower half including currSpeed.
   *   4. Otherwise raise minSpeed above currSpeed.
   *
   * Time:  O(n log M) - each check scans piles across max pile M.
   * Space: O(1) - only counters and bounds are stored.
   *
   * @param piles banana piles
   * @param allowedTime maximum hours
   * @return minimum feasible eating speed
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
