package DynamicProgramming;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * https://leetcode.com/problems/minimum-number-of-refueling-stops/
 *
 * Return the minimum number of refueling stops the car must make in order to reach its destination.
 * If it cannot reach the destination, return -1.
 */
public class MinRefuel {
    public static void main(String[] args) {
        int[][] stations = {
                {25, 25},
                {50, 25},
                {75, 25}
        };
        int result = new MinRefuel().minRefuelStops(100, 25, stations);
        System.out.println(result);
    }

    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        if (startFuel >= target) return 0;

        int i = 0;
        int len = stations.length;
        int stops = 0;
        int maxReachable = startFuel;

        Queue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        while (maxReachable < target) {
            while (i < len && stations[i][0] <= maxReachable) {
                queue.offer(stations[i++][1]);
            }
            if (queue.isEmpty()) return -1;
            maxReachable += queue.poll();
            stops++;
        }
        return stops;
    }
}
