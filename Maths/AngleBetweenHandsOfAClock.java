package com.sandeep.frazsheet.math;

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
 * 
 * @author Sandeep
 */
public class AngleBetweenHandsOfAClock {
    
    /**
     * Calculates the smaller angle between hour and minute hands.
     * 
     * Algorithm:
     * 1. Calculate minute hand angle: 6 * minutes (360°/60 minutes = 6° per minute)
     * 2. Calculate hour hand angle: 30 * hour + 0.5 * minutes
     *    - 30° per hour (360°/12 hours = 30° per hour)
     *    - 0.5° per minute (30°/60 minutes = 0.5° per minute for hour hand movement)
     * 3. Find absolute difference between the two angles
     * 4. Return minimum of difference and (360 - difference)
     * 
     * Time Complexity: O(1) - constant time calculation
     * Space Complexity: O(1) - only using constant extra space
     * 
     * @param hour Hour value (12-hour format, 12 represents 0)
     * @param minutes Minutes value (0-59)
     * @return Smaller angle between hands in degrees
     */
    public double angleClock(int hour, int minutes) {
        // Validate input ranges
        if (hour < 1 || hour > 12 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time input");
        }
        
        // Convert hour to 0-11 range for calculation
        hour = hour % 12;
        
        // Calculate angles from 12 o'clock position
        double minuteAngle = 6.0 * minutes; // 360°/60 = 6° per minute
        double hourAngle = 30.0 * hour + 0.5 * minutes; // 30° per hour + continuous movement
        
        // Find absolute difference
        double angleDiff = Math.abs(hourAngle - minuteAngle);
        
        // Return the smaller angle (clockwise or counterclockwise)
        return Math.min(angleDiff, 360.0 - angleDiff);
    }
    
    /**
     * Alternative implementation with explicit step-by-step calculation.
     * More readable for understanding the problem logic.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    public double angleClockVerbose(int hour, int minutes) {
        // Normalize hour to 0-11 range
        int normalizedHour = hour % 12;
        
        // Minute hand moves 360° in 60 minutes = 6° per minute
        double minuteHandAngle = minutes * 6.0;
        
        // Hour hand moves 360° in 12 hours = 30° per hour
        // Also moves continuously: 30° in 60 minutes = 0.5° per minute
        double hourHandAngle = normalizedHour * 30.0 + minutes * 0.5;
        
        // Calculate the difference
        double difference = Math.abs(hourHandAngle - minuteHandAngle);
        
        // The smaller angle is either the difference or 360° - difference
        if (difference <= 180.0) {
            return difference;
        } else {
            return 360.0 - difference;
        }
    }
    
    /**
     * Mathematical formula approach using modular arithmetic.
     * Most concise implementation.
     * 
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    public double angleClockFormula(int hour, int minutes) {
        // Calculate absolute angle difference
        double angle = Math.abs(30 * (hour % 12) + 0.5 * minutes - 6 * minutes);
        
        // Return smaller angle
        return Math.min(angle, 360 - angle);
    }
    
    /**
     * Handles 24-hour format input by converting to 12-hour format.
     * 
     * @param hour24 Hour in 24-hour format (0-23)
     * @param minutes Minutes (0-59)
     * @return Smaller angle between hands
     */
    public double angleClock24Hour(int hour24, int minutes) {
        if (hour24 < 0 || hour24 > 23) {
            throw new IllegalArgumentException("Invalid 24-hour format");
        }
        
        // Convert to 12-hour format
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;
        
        return angleClock(hour12, minutes);
    }
    
    /**
     * Calculates angles for all three hands including seconds.
     * Returns minimum angle between any two hands.
     * 
     * @param hour Hour value
     * @param minutes Minutes value
     * @param seconds Seconds value
     * @return Minimum angle between any two hands
     */
    public double minAngleWithSeconds(int hour, int minutes, int seconds) {
        hour = hour % 12;
        
        // Calculate angles for all three hands
        double secondAngle = seconds * 6.0; // 360°/60 = 6° per second
        double minuteAngle = minutes * 6.0 + seconds * 0.1; // 6° per minute + continuous movement
        double hourAngle = hour * 30.0 + minutes * 0.5 + seconds * (0.5 / 60.0); // Continuous movement
        
        // Calculate all three possible angles
        double angle1 = Math.abs(hourAngle - minuteAngle);
        double angle2 = Math.abs(hourAngle - secondAngle);
        double angle3 = Math.abs(minuteAngle - secondAngle);
        
        // Adjust for smaller angles
        angle1 = Math.min(angle1, 360.0 - angle1);
        angle2 = Math.min(angle2, 360.0 - angle2);
        angle3 = Math.min(angle3, 360.0 - angle3);
        
        return Math.min(Math.min(angle1, angle2), angle3);
    }
    
    /**
     * Calculates the exact time when hands form a specific angle.
     * Returns all times in a 12-hour period when the angle occurs.
     * 
     * @param targetAngle Desired angle between hands (0-180 degrees)
     * @return List of times when hands form the target angle
     */
    public java.util.List<String> timesForAngle(double targetAngle) {
        java.util.List<String> times = new java.util.ArrayList<>();
        
        if (targetAngle < 0 || targetAngle > 180) {
            return times; // Invalid angle
        }
        
        // Check every minute of 12-hour period
        for (int h = 1; h <= 12; h++) {
            for (int m = 0; m < 60; m++) {
                double currentAngle = angleClock(h, m);
                
                // Check if current angle matches target (within small tolerance)
                if (Math.abs(currentAngle - targetAngle) < 0.1) {
                    times.add(String.format("%d:%02d", h, m));
                }
            }
        }
        
        return times;
    }
    
    /**
     * Validates the calculated angle against known test cases.
     * Useful for testing and verification.
     * 
     * @param hour Hour input
     * @param minutes Minutes input
     * @param expectedAngle Expected angle result
     * @param tolerance Allowed tolerance for floating point comparison
     * @return true if calculated angle matches expected within tolerance
     */
    public boolean validateAngle(int hour, int minutes, double expectedAngle, double tolerance) {
        double calculatedAngle = angleClock(hour, minutes);
        return Math.abs(calculatedAngle - expectedAngle) <= tolerance;
    }
    
    /**
     * Utility method to format time display for debugging.
     * 
     * @param hour Hour value
     * @param minutes Minutes value
     * @return Formatted time string
     */
    public String formatTime(int hour, int minutes) {
        return String.format("%d:%02d", hour, minutes);
    }
    
    /**
     * Generates a visual representation of clock hand positions.
     * Returns ASCII art representation of the clock.
     * 
     * @param hour Hour value
     * @param minutes Minutes value
     * @return ASCII representation of clock
     */
    public String visualizeClock(int hour, int minutes) {
        StringBuilder sb = new StringBuilder();
        double angle = angleClock(hour, minutes);
        
        sb.append("Clock at ").append(formatTime(hour, minutes)).append("\\n");
        sb.append("Angle between hands: ").append(String.format("%.1f", angle)).append("°\\n");
        sb.append("\\n    12\\n");
        sb.append("9       3\\n");
        sb.append("    6\\n");
        
        return sb.toString();
    }
}