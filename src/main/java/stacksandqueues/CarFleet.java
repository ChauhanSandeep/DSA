package stacksandqueues;

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
 * Input: target = 12, 
 * position = [10,8, 0, 5, 3], 
 * speed =    [2, 4, 1, 1, 3]
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
 */
public class CarFleet {

    /**
     * The major intuition for solving this problem is that if a car catches up to another car,
     * they will form a fleet and move together at the slower car's speed.
     * We can determine the time it takes for each car to reach the target and use a stack to track fleets.
     *
     * Algorithm:
     * 1. Pair each car's position with its calculated time to reach the target.
     * 2. Sort the pairs of cars by their starting positions in descending order. This ensures we process cars from the
     * one closest to the target to the one furthest away.
     * 3. Initialize a variable to track the time of the current leading fleet (`currentFleetTime`).
     * 4. Iterate through the sorted cars. For each car, calculate its `timeToTarget`.
     * 5. Compare the current car's `timeToTarget` with the `currentFleetTime`. If the current car's time is greater,
     * it means it will arrive at the destination after the current leading fleet, thus forming a new fleet.
     * 6. If a new fleet is formed, increment the fleet count and update `currentFleetTime` to the new car's time.
     * 7. If the current car's time is less than or equal to `currentFleetTime`, it will catch up to the fleet ahead and join it.
     * No new fleet is formed, and the `currentFleetTime` remains unchanged.
     * 8. Return the total number of fleets counted.
     *
     * Time Complexity: O(n log n)
     * Space Complexity: O(n)
     */
    public int carFleetSimplified(int target, int[] position, int[] speed) {
        int size = position.length;
        if (size <= 1) return size;

        // Create array of [position, time] pairs
        double[][] carsPositionAndTime = new double[size][2];
        for (int i = 0; i < size; i++) {
            carsPositionAndTime[i][0] = position[i];
            carsPositionAndTime[i][1] = (double) (target - position[i]) / speed[i];
        }

        // Sort by position (descending)
        Arrays.sort(carsPositionAndTime, (a, b) -> Double.compare(b[0], a[0]));

        int fleets = 0;
        double currentFleetTime = 0;

        for (double[] car : carsPositionAndTime) {
            // If this car takes longer than current fleet, it starts a new fleet
            if (car[1] > currentFleetTime) {
                fleets++;
                currentFleetTime = car[1];
            }
        }

        return fleets;
    }

    /**
     * Optimized single-pass approach after sorting.
     * * Algorithm:
     * 1. Create an array of indices from 0 to n-1.
     * 2. Sort this index array based on the values of the `position` array in descending order. This allows us to process
     * cars from closest to furthest away from the destination without creating new objects or arrays for position and speed.
     * 3. Initialize a fleet counter to 0 and a variable `lastFleetTime` to track the time of the last confirmed fleet leader.
     * 4. Iterate through the sorted indices. For each index, calculate the time for that car to reach the target.
     * 5. Compare the current car's time with `lastFleetTime`. If the current car's time is strictly greater, it means it will
     * arrive later and form a new, independent fleet.
     * 6. If a new fleet is formed, increment the fleet counter and update `lastFleetTime` to the current car's time.
     * 7. If the current car's time is less than or equal to `lastFleetTime`, it will eventually join the existing fleet. No
     * action is needed other than continuing the loop.
     * 8. Return the total number of fleets counted.
     *
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n) for the index array
     */
    public int carFleetOptimized(int target, int[] position, int[] speed) {
        int size = position.length;
        if (size <= 1) return size;

        // Create index array for sorting
        Integer[] indices = new Integer[size];
        for (int i = 0; i < size; i++) {
            indices[i] = i;
        }

        // Sort indices by position (descending)
        Arrays.sort(indices, (i, j) -> Integer.compare(position[j], position[i]));

        int fleets = 0;
        double lastFleetTime = 0;

        for (int i : indices) {
            double timeToTarget = (double) (target - position[i]) / speed[i];

            if (timeToTarget > lastFleetTime) {
                // This car starts a new fleet because it can't catch up to the last fleet
                fleets++;
                lastFleetTime = timeToTarget;
            }
        }

        return fleets;
    }
}