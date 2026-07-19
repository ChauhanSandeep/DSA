package maths;

/**
 * Problem: Angle Between Hands of a Clock
 *
 * Given an hour and minute on a 12-hour analog clock, return the smaller
 * angle between the hour hand and the minute hand. The hour hand moves
 * continuously as minutes pass, so 3:15 is not exactly a right angle.
 *
 * Leetcode: https://leetcode.com/problems/angle-between-hands-of-a-clock/ (Medium)
 * Rating:   1325 (zerotrac Elo)
 * Pattern:  Math | Clock geometry | Modular angles
 *
 * Example:
 *   Input:  hour = 12, minutes = 30
 *   Output: 165.0
 *   Why:    the minute hand is at 180 degrees and the hour hand is at 15 degrees.
 *
 * Follow-ups:
 *   1. How would this change for a 24-hour input format?
 *      Reduce the hour with hour % 12 before applying the same clock geometry.
 *   2. How would you include the second hand?
 *      Compute a third continuous angle and compare each pair of hands.
 *   3. How would you support arbitrary clock scales?
 *      Parameterize the number of hours and derive degrees-per-unit from 360.
 *
 * Related: Minimum Time Difference (539).
 */

public class AngleBetweenHandsOfAClock {

    public static void main(String[] args) {
        AngleBetweenHandsOfAClock solver = new AngleBetweenHandsOfAClock();
        int[][] inputs = { {12, 30}, {3, 15}, {1, 57} };
        double[] expected = { 165.0, 7.5, 76.5 };

        for (int i = 0; i < inputs.length; i++) {
            double got = solver.angleClock(inputs[i][0], inputs[i][1]);
            System.out.printf("hour=%d minutes=%d -> %.1f  expected=%.1f%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }

        /**
     * Intuition: convert both hands into angles measured from 12 o'clock.
     * The minute hand moves 6 degrees per minute, while the hour hand moves
     * only 0.5 degrees per elapsed minute, including the minutes past the hour.
     * Once both positions are known, the answer is the smaller of the clockwise
     * and counter-clockwise gaps.
     *
     * Algorithm:
     *   1. Convert hour and minutes into totalMinutes since 12:00.
     *   2. Compute hourHandRotation and minHandRotation using the original rates.
     *   3. Return the smaller angle between the direct gap and 360 minus that gap.
     *
     * Time:  O(1) - a fixed number of arithmetic operations.
     * Space: O(1) - only scalar values are stored.
     *
     * @param hour hour value on a 12-hour clock
     * @param minutes minute value from 0 to 59
     * @return smaller angle between the two hands in degrees
     */

    public double angleClock(int hour, int minutes) {
        double HOUR_HAND_DEGREES_PER_MINUTE = 0.5;
        double MINUTE_HAND_DEGREES_PER_MINUTE = 6;

        int totalMinutes = 60*hour + minutes;

        double hourHandRotation = (totalMinutes * HOUR_HAND_DEGREES_PER_MINUTE)%360;
        double minHandRotation = (totalMinutes * MINUTE_HAND_DEGREES_PER_MINUTE)%360;

        // Return the smaller angle (clockwise or counterclockwise)
        return Math.min(Math.abs(minHandRotation - hourHandRotation),
            360 - Math.abs(minHandRotation - hourHandRotation));
    }
}