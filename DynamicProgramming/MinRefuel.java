package DynamicProgramming;

import java.util.PriorityQueue;

/**
 * Problem: Minimum Refueling Stops (Greedy Approach)
 * 
 * - Given a target distance, an initial fuel amount, and a list of fuel stations.
 * - Return the **minimum number of refueling stops** needed to reach the target.
 * - If it's not possible, return **-1**.
 * 
 * Approach:
 * - Use a **max-heap (PriorityQueue)** to always pick the station with the most fuel within reach.
 * - Keep refueling greedily until the target is reached.
 * 
 * Time Complexity: **O(n log n)** (heap operations for n stations)
 * Space Complexity: **O(n)** (heap stores at most n stations)
 */
public class MinRefuel {
    public static void main(String[] args) {
        int[][] stations = {
                {25, 25},
                {50, 25},
                {75, 25}
        };
        int result = new MinRefuel().minRefuelStops(100, 25, stations);
        System.out.println("Minimum Refueling Stops: " + result); // Output: 3
    }

    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        if (startFuel >= target) return 0; // No stops needed if initial fuel is enough

        int stops = 0, index = 0, n = stations.length;
        int currentFuel = startFuel;

        // Max heap to store fuel amounts of reachable stations
        PriorityQueue<Integer> fuelPQ = new PriorityQueue<>((a, b) -> b - a);

        while (currentFuel < target) {
            // Add all reachable fuel stations to the max heap
            while (index < n && stations[index][0] <= currentFuel) {
                fuelPQ.offer(stations[index][1]);
                index++;
            }

            // If no station is reachable and we haven't reached the target, return -1
            if (fuelPQ.isEmpty()) return -1;

            // Refuel with the station offering the **maximum** fuel within reach
            currentFuel += fuelPQ.poll();
            stops++;
        }

        return stops;
    }
}
