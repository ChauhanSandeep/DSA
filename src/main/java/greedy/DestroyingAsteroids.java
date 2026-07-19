package greedy;

import java.util.Arrays;

/**
 * Problem: Destroying Asteroids
 *
 * You start with a planet of mass mass and may choose the order in which it
 * collides with asteroids. The planet can absorb an asteroid only when its
 * current mass is at least that asteroid's mass; after absorbing it, the planet
 * gains that mass. Return whether some ordering destroys every asteroid.
 *
 * Leetcode: https://leetcode.com/problems/destroying-asteroids/ (Medium)
 * Rating:   1335 (zerotrac Elo)
 * Pattern:  Greedy | Sort ascending | Take easiest asteroid first
 *
 * Example:
 *   Input:  mass = 10, asteroids = [3,9,19,5,21]
 *   Output: true
 *   Why:    destroying [3,5,9,19,21] grows the planet before each harder asteroid,
 *           so it reaches enough mass before meeting 21.
 *
 * Follow-ups:
 *   1. Return one valid destruction order?
 *      Sort ascending and return that order if the simulation succeeds.
 *   2. What if asteroids give different rewards from their mass?
 *      Sorting by mass no longer proves safety; model it as scheduling or dynamic programming.
 *   3. What if some asteroids reduce the planet's mass?
 *      The greedy choice can fail, so you need state search or a problem-specific ordering rule.
 *   4. What if the final mass can overflow a 32-bit integer?
 *      Store the running mass in a long, as this implementation does.
 */
public class DestroyingAsteroids {

    public static void main(String[] args) {
        DestroyingAsteroids solver = new DestroyingAsteroids();
        int[] masses = {10, 5, 1};
        int[][] asteroids = { {3, 9, 19, 5, 21}, {4, 9, 23, 4}, {} };
        boolean[] expected = {true, false, true};

        for (int i = 0; i < masses.length; i++) {
            boolean got = solver.asteroidsDestroyed(masses[i], asteroids[i].clone());
            System.out.printf("mass=%d asteroids=%s -> %s  expected=%s%n",
                masses[i], Arrays.toString(asteroids[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: absorbing an asteroid only increases mass, so taking the
     * smallest available asteroid first is never worse than taking a larger one.
     * If the smallest remaining asteroid is already too large, every other
     * remaining asteroid is also too large and no ordering can help. Sorting turns
     * that exchange argument into a single left-to-right simulation.
     *
     * Algorithm:
     *   1. Return true for an empty asteroid list.
     *   2. If even the smallest asteroid exceeds the initial mass, return false early.
     *   3. Sort asteroid masses in ascending order.
     *   4. Absorb each asteroid in order, failing if the current mass is too small.
     *
     * Time:  O(n log n) - sorting dominates the scan.
     * Space: O(1) - the simulation uses constant extra space besides the in-place sort.
     *
     * @param mass initial planet mass
     * @param asteroids asteroid masses that may be reordered
     * @return true if every asteroid can be destroyed in some order
     */
    public boolean asteroidsDestroyed(int mass, int[] asteroids) {
        if (asteroids == null || asteroids.length == 0) return true;

        // Early check: if smallest asteroid is larger than initial mass, impossible
        int minAsteroid = Arrays.stream(asteroids).min().orElse(0);
        if (mass < minAsteroid) return false;

        // Sort asteroids in ascending order to apply greedy strategy
        Arrays.sort(asteroids);

        // Use long to prevent overflow as mass can grow very large
        long currentMass = mass;

        for (int asteroidMass : asteroids) {
            // If planet cannot destroy this asteroid, impossible to proceed
            if (currentMass < asteroidMass) {
                return false;
            }

            // Absorb the asteroid's mass
            currentMass += asteroidMass;
        }

        return true;
    }

}