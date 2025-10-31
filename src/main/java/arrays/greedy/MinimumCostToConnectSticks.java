package arrays.greedy;

import java.util.PriorityQueue;

/**
 * Problem: Minimum Cost to Connect Sticks
 *
 * You are given an array of integers representing the lengths of sticks.
 * You can connect any two sticks into one. The cost is equal to the sum of their lengths.
 * The goal is to connect all sticks into one stick with the **minimum total cost**.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/minimum-cost-to-connect-sticks/
 *
 * Example:
 * Input: [1, 8, 3, 5]
 * Output: 30
 * Explanation:
 *  - Combine 1 + 3 = 4 → cost = 4
 *  - Combine 4 + 5 = 9 → cost = 9
 *  - Combine 8 + 9 = 17 → cost = 17
 *  - Total = 4 + 9 + 17 = 30
 *
 * Follow-up:
 * 1. Why does greedy (always combine smallest two) work?
 *    ➤ Huffman coding principle — combining smaller parts early reduces cost growth.
 * 2. Can you avoid using a heap?
 *    ➤ No, heap is required for optimality in O(N log N). Without it, solution becomes brute-force.
 * 3. Can this be parallelized?
 *    ➤ Only partially. Final result is inherently sequential due to dependency.
 */
public class MinimumCostToConnectSticks {

  public static void main(String[] args) {
    int[] stickLengths = {1, 8, 3, 5};
    MinimumCostToConnectSticks solver = new MinimumCostToConnectSticks();
    int result = solver.getMinimumCostToConnectSticks(stickLengths);
    System.out.println("Minimum cost to connect sticks: " + result);
  }

  /**
   * Greedy approach using a Min-Heap (PriorityQueue) to minimize total cost.
   *
   * Steps:
   * 1. Add all sticks into a min-heap.
   * 2. While more than one stick exists:
   *    a. Remove the two smallest sticks.
   *    b. Add their sum to the total cost.
   *    c. Insert the combined stick back into the heap.
   * 3. Return the accumulated cost.
   *
   * Algorithm: Greedy + Heap
   * Time Complexity: O(N log N), where N is the number of sticks.
   * Space Complexity: O(N), due to heap storage.
   *
   * @param sticks Array of stick lengths.
   * @return Minimum cost to connect all sticks.
   */
  public int getMinimumCostToConnectSticks(int[] sticks) {
    if (sticks == null || sticks.length <= 1) return 0;

    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    for (int stick : sticks) {
      minHeap.offer(stick);
    }

    int totalCost = 0;

    while (minHeap.size() > 1) {
      // Always combine the two shortest sticks
      int shortest = minHeap.poll();
      int secondShortest = minHeap.poll();
      int combinedCost = shortest + secondShortest;

      totalCost += combinedCost;
      minHeap.offer(combinedCost);
    }

    return totalCost;
  }
}