package com.sandeep.frazsheet.stack;

import java.util.Stack;
import java.util.Arrays;

/**
 * Problem: Car Fleet
 * 
 * There are n cars going to the same destination along a one-lane road. The destination is target miles away.
 * You are given two integer arrays position and speed of length n, where position[i] is the position of the ith car
 * and speed[i] is the speed of the ith car (in miles per hour).
 * A car cannot pass another car, but it can catch up and drive bumper to bumper at the same speed.
 * The faster car will slow down to match the slower car's speed. The distance between these two cars is ignored.
 * A car fleet is a car or cars driving next to each other. The speed of the car fleet is the speed of the slowest car in the fleet.
 * Return the number of car fleets that will arrive at the destination.
 * 
 * Example:
 * Input: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
 * Output: 3
 * Explanation: Cars starting at 10 and 8 become a fleet, meeting at 12. Car at 0 forms fleet 1.
 * Cars at 5 and 3 become a fleet, meeting before 12. So there are 3 fleets.
 * 
 * LeetCode: https://leetcode.com/problems/car-fleet
 * 
 * Follow-up Questions:
 * 1. What if cars can change lanes to overtake?
 *    Answer: Problem becomes significantly more complex, requiring simulation of lane changes.
 * 
 * 2. How would you handle different target destinations for different cars?
 *    Answer: Track each car's individual journey and group by common destinations.
 * 
 * 3. What if cars have different acceleration capabilities?
 *    Answer: Use physics equations with variable acceleration instead of constant speed.
 *    Related: https://leetcode.com/problems/race-car/
 * 
 * @author Sandeep
 */
public class CarFleet {
    
    /**
     * Calculates number of car fleets using stack-based approach.
     * 
     * Algorithm:
     * 1. Create car objects with position, speed, and time to reach destination
     * 2. Sort cars by position in descending order (closest to target first)
     * 3. Use stack to track fleet leaders (cars that don't catch up to car ahead)
     * 4. For each car, if it reaches destination before or same time as car ahead, it forms fleet
     * 5. Otherwise, it becomes a new fleet leader
     * 
     * Time Complexity: O(n log n) where n is number of cars (dominated by sorting)
     * Space Complexity: O(n) for storing cars and stack
     * 
     * @param target Target destination distance
     * @param position Array of car positions
     * @param speed Array of car speeds
     * @return Number of car fleets that will arrive at destination
     */
    public int carFleet(int target, int[] position, int[] speed) {
        if (position == null || speed == null || position.length == 0 || position.length != speed.length) {
            return 0;
        }
        
        int n = position.length;
        
        // Create car objects with time to reach destination
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            double timeToTarget = (double) (target - position[i]) / speed[i];
            cars[i] = new Car(position[i], speed[i], timeToTarget);
        }
        
        // Sort by position in descending order (closest to target first)
        Arrays.sort(cars, (a, b) -> Integer.compare(b.position, a.position));
        
        Stack<Double> fleetTimes = new Stack<>();
        
        for (Car car : cars) {
            // If this car takes longer than the car ahead, it forms a new fleet
            if (fleetTimes.isEmpty() || car.timeToTarget > fleetTimes.peek()) {
                fleetTimes.push(car.timeToTarget);
            }
            // Otherwise, it catches up and joins the fleet ahead
        }
        
        return fleetTimes.size();
    }
    
    /**
     * Simplified approach without explicit Car class.
     * Uses arrays to track positions and times.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int carFleetSimplified(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n <= 1) return n;
        
        // Create array of [position, time] pairs
        double[][] cars = new double[n][2];
        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = (double) (target - position[i]) / speed[i];
        }
        
        // Sort by position (descending)
        Arrays.sort(cars, (a, b) -> Double.compare(b[0], a[0]));
        
        int fleets = 0;
        double currentFleetTime = 0;
        
        for (double[] car : cars) {
            // If this car takes longer than current fleet, it starts a new fleet
            if (car[1] > currentFleetTime) {
                fleets++;
                currentFleetTime = car[1];
            }
        }
        
        return fleets;
    }
    
    /**
     * Mathematical approach using time analysis without stack.
     * More efficient in terms of space complexity.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n) for sorting, O(1) additional space
     */
    public int carFleetMath(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n <= 1) return n;
        
        // Create pairs and sort by position
        int[][] positionSpeed = new int[n][2];
        for (int i = 0; i < n; i++) {
            positionSpeed[i][0] = position[i];
            positionSpeed[i][1] = speed[i];
        }
        
        Arrays.sort(positionSpeed, (a, b) -> Integer.compare(b[0], a[0]));
        
        int fleets = 1; // At least one fleet
        double leadFleetTime = (double) (target - positionSpeed[0][0]) / positionSpeed[0][1];
        
        for (int i = 1; i < n; i++) {
            double currentTime = (double) (target - positionSpeed[i][0]) / positionSpeed[i][1];
            
            // If current car takes longer, it forms a new fleet
            if (currentTime > leadFleetTime) {
                fleets++;
                leadFleetTime = currentTime;
            }
        }
        
        return fleets;
    }
    
    /**
     * Detailed simulation approach that tracks fleet formation step by step.
     * Useful for understanding the problem mechanics.
     * 
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int carFleetDetailed(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n == 0) return 0;
        
        // Create detailed car information
        CarDetailed[] cars = new CarDetailed[n];
        for (int i = 0; i < n; i++) {
            cars[i] = new CarDetailed(i, position[i], speed[i], target);
        }
        
        // Sort by position (closest to target first)
        Arrays.sort(cars, (a, b) -> Integer.compare(b.position, a.position));
        
        java.util.List<java.util.List<CarDetailed>> fleets = new java.util.ArrayList<>();
        
        for (CarDetailed car : cars) {
            boolean joinedFleet = false;
            
            // Check if this car can join any existing fleet
            for (java.util.List<CarDetailed> fleet : fleets) {
                CarDetailed fleetLeader = fleet.get(fleet.size() - 1); // Last car in fleet
                
                if (car.timeToTarget <= fleetLeader.timeToTarget) {
                    fleet.add(car);
                    joinedFleet = true;
                    break;
                }
            }
            
            // If couldn't join any fleet, start a new one
            if (!joinedFleet) {
                java.util.List<CarDetailed> newFleet = new java.util.ArrayList<>();
                newFleet.add(car);
                fleets.add(newFleet);
            }
        }
        
        return fleets.size();
    }
    
    /**
     * Optimized single-pass approach after sorting.
     * Most space-efficient solution.
     */
    public int carFleetOptimized(int target, int[] position, int[] speed) {
        int n = position.length;
        if (n <= 1) return n;
        
        // Create index array for sorting
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // Sort indices by position (descending)
        Arrays.sort(indices, (i, j) -> Integer.compare(position[j], position[i]));
        
        int fleets = 0;
        double lastFleetTime = 0;
        
        for (int i : indices) {
            double timeToTarget = (double) (target - position[i]) / speed[i];
            
            if (timeToTarget > lastFleetTime) {
                fleets++;
                lastFleetTime = timeToTarget;
            }
        }
        
        return fleets;
    }
    
    // Helper class for basic car information
    static class Car {
        int position;
        int speed;
        double timeToTarget;
        
        Car(int position, int speed, double timeToTarget) {
            this.position = position;
            this.speed = speed;
            this.timeToTarget = timeToTarget;
        }
    }
    
    // Helper class for detailed car tracking
    static class CarDetailed {
        int id;
        int position;
        int speed;
        double timeToTarget;
        
        CarDetailed(int id, int position, int speed, int target) {
            this.id = id;
            this.position = position;
            this.speed = speed;
            this.timeToTarget = (double) (target - position) / speed;
        }
        
        @Override
        public String toString() {
            return String.format("Car%d(pos=%d, speed=%d, time=%.2f)", 
                               id, position, speed, timeToTarget);
        }
    }
    
    /**
     * Validates the input arrays and returns detailed fleet information.
     * 
     * @param target Target destination
     * @param position Car positions
     * @param speed Car speeds
     * @return Fleet formation details for debugging
     */
    public String getFleetDetails(int target, int[] position, int[] speed) {
        int n = position.length;
        StringBuilder sb = new StringBuilder();
        
        Car[] cars = new Car[n];
        for (int i = 0; i < n; i++) {
            double timeToTarget = (double) (target - position[i]) / speed[i];
            cars[i] = new Car(position[i], speed[i], timeToTarget);
        }
        
        Arrays.sort(cars, (a, b) -> Integer.compare(b.position, a.position));
        
        sb.append("Fleet Analysis:\\n");
        int fleetCount = 0;
        double currentFleetTime = -1;
        
        for (int i = 0; i < cars.length; i++) {
            Car car = cars[i];
            if (car.timeToTarget > currentFleetTime) {
                fleetCount++;
                currentFleetTime = car.timeToTarget;
                sb.append(String.format("Fleet %d: ", fleetCount));
            }
            sb.append(String.format("Car(pos=%d,speed=%d,time=%.2f) ", 
                                  car.position, car.speed, car.timeToTarget));
        }
        
        sb.append(String.format("\\nTotal fleets: %d", fleetCount));
        return sb.toString();
    }
}