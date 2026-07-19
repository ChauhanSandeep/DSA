package stacksandqueues;

import java.util.Arrays;

/**
 * Problem: Car Fleet
 *
 * There are n cars going to the same destination along a one-lane road. A car
 * cannot pass another car, but it can catch up and drive bumper to bumper at
 * the slower car's speed. Return how many car fleets arrive at the destination.
 *
 * Leetcode: https://leetcode.com/problems/car-fleet/ (Medium)
 * Rating:   zerotrac 1678
 * Pattern:  Sorting | Greedy from target | Fleet merge by arrival time
 *
 * Example:
 *   Input:  target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
 *   Output: 3
 *   Why:    cars at 10 and 8 merge, cars at 5 and 3 merge, and car at 0 arrives as its own fleet.
 *
 * Follow-ups:
 *   1. Need the composition of each fleet, not just the count?
 *      Store fleet members while scanning cars from nearest to farthest.
 *   2. Cars may change lanes and pass?
 *      The greedy merge no longer applies; model lanes and passing rules as a simulation.
 *   3. Speeds can change over time?
 *      Replace constant arrival time with piecewise travel-time functions and compare catch-up events.
 *   4. Need collision times between fleets?
 *      Use the same reverse scan as Car Fleet II with a stack of candidate fleets.
 *
 * Related: Car Fleet II (1776), Asteroid Collision (735).
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

        // Create array of Car objects
        Car[] cars = new Car[size];
        for (int i = 0; i < size; i++) {
            double timeToTarget = (double) (target - position[i]) / speed[i];
            cars[i] = new Car(position[i], timeToTarget);
        }

        // Sort by position (descending)
        Arrays.sort(cars, (a, b) -> Double.compare(b.position, a.position));

        int fleets = 0;
        double currentFleetTime = 0;

        for (Car car : cars) {
            // If this car takes longer than current fleet, it starts a new fleet
            if (car.timeToTarget > currentFleetTime) {
                fleets++;
                currentFleetTime = car.timeToTarget;
            }
        }

        return fleets;
    }

        /**
     * Intuition: after sorting cars from closest to farthest from the target,
     * each car only cares about the fleet directly ahead. If its solo arrival
     * time is less than or equal to `lastFleetTime`, it catches that fleet; if it
     * is larger, it cannot catch up and becomes a new fleet.
     *
     * Algorithm:
     *   1. Build indices 0..n-1.
     *   2. Sort indices by position descending.
     *   3. Compute each car's time to target in that order.
     *   4. Count a new fleet whenever timeToTarget exceeds `lastFleetTime`.
     *
     * Time:  O(n log n) - sorting dominates the scan.
     * Space: O(n) - the index array stores all cars.
     *
     * @param target destination position
     * @param position starting position for each car
     * @param speed speed for each car
     * @return number of fleets reaching the target
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
    /**
     * Inner class to represent a car with its position and time to reach target.
     */
    private static class Car {
        double position;
        double timeToTarget;

        Car(double position, double timeToTarget) {
            this.position = position;
            this.timeToTarget = timeToTarget;
        }
    }

    public static void main(String[] args) {
        CarFleet solver = new CarFleet();
        int[] targets = {12, 10, 100};
        int[][] positions = { {10, 8, 0, 5, 3}, {3}, {0, 2, 4} };
        int[][] speeds = { {2, 4, 1, 1, 3}, {3}, {4, 2, 1} };
        int[] expected = {3, 1, 1};
        for (int i = 0; i < targets.length; i++) {
            int got = solver.carFleetOptimized(targets[i], positions[i], speeds[i]);
            System.out.printf("target=%d position=%s speed=%s -> %d  expected=%d%n", targets[i], Arrays.toString(positions[i]), Arrays.toString(speeds[i]), got, expected[i]);
        }
    }
}
