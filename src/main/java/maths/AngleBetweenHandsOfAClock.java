package maths;

/**
 * Problem: Angle Between Hands of a Clock
 *
 * Given two numbers, hour and minutes, return the smaller angle (in degrees) formed between
 * the hour and the minute hand. Answers within 10^-5 of the actual value will be accepted as correct.
 *
 * Example:
 * Input: hour = 12, minutes = 30
 * Output: 165.0
 * Explanation: The angle between hour hand and minute hand is 165 degrees.
 *
 * LeetCode: https://leetcode.com/problems/angle-between-hands-of-a-clock
 *
 * Follow-up Questions:
 * 1. How would you handle a 24-hour clock format?
 *    Answer: Convert 24-hour to 12-hour using modulo operation: hour % 12.
 *
 * 2. What if we need to consider the second hand as well?
 *    Answer: Calculate second hand angle similarly and find minimum angle between any two hands.
 *
 * 3. How would you animate the clock hands smoothly?
 *    Answer: Use continuous time calculations instead of discrete hour/minute values.
 *    Related: https://leetcode.com/problems/minimum-time-difference/
 * LeetCode Contest Rating: 1325
 */
public class AngleBetweenHandsOfAClock {

    public static void main(String[] args) {
        AngleBetweenHandsOfAClock angleBetweenHandsOfAClock = new AngleBetweenHandsOfAClock();
        System.out.println("The angle between hands is: " + angleBetweenHandsOfAClock.angleClock(3, 15)); // 165.0
    }

    /**
     * Calculates the smaller angle between hour and minute hands.
     *
     * Algorithm:
     * 1. Calculate total minutes from 12:00 using formula: totalMinutes = 60 * hour + minutes
     * 2. Calculate hour hand rotation: (360 degree/ 12 * 60 minutes) = 0.5 degree per minute
     * 3. Calculate minute hand rotation: (360 degree/ 60 minutes) = 6 degree per minute
     * 4. Compute absolute difference between hour and minute rotations
     *
     * Time Complexity: O(1) - constant time calculation
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param hour Hour value (12-hour format, 12 represents 0)
     * @param minutes Minutes value (0-59)
     * @return Smaller angle between hands in degrees
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