package Array;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * LeetCode Problem: https://leetcode.com/problems/minimum-cost-to-connect-sticks/
 *
 * Given an array of stick lengths, the goal is to connect all sticks with minimum cost.
 * The cost of connecting two sticks of lengths `a` and `b` is `a + b`.
 *
 * Approach:
 * - Use a **Min-Heap (PriorityQueue)** to always combine the smallest two sticks first.
 * - Continue until only one stick remains.
 * - Runs in **O(N log N) time complexity**, where N is the number of sticks.
 * - Space complexity is **O(N)** due to heap storage.
 */
public class ConnectedStick {
    public static void main(String[] args) {
        int[] sticks = {1, 8, 3, 5};
        int result = new ConnectedStick().connectSticks(sticks);
        System.out.println("Minimum cost to connect sticks: " + result);
    }

    /**
     * Connects all sticks with minimum cost using a priority queue.
     *
     * @param sticks Array of stick lengths.
     * @return Minimum cost required to connect all sticks.
     */
    public int connectSticks(int[] sticks) {
        if (sticks == null || sticks.length == 0) return 0;
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int stick : sticks) {
            minHeap.offer(stick);
        }
        
        int totalCost = 0;
        while (minHeap.size() > 1) {
            int stick1 = minHeap.poll();
            int stick2 = minHeap.poll();
            int cost = stick1 + stick2;
            totalCost += cost;
            minHeap.offer(cost);
        }
        return totalCost;
    }
}
