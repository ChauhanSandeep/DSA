package arrays.greedy;

import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * Problem: Minimum Cost to Connect Sticks
 *
 * Connect sticks until only one remains. Each merge costs the sum of the two
 * stick lengths being connected, so short sticks should be merged before they
 * are repeatedly carried into later costs.
 *
 * Leetcode: https://leetcode.com/problems/minimum-cost-to-connect-sticks/ (Medium)
 * Rating:   acceptance 72.3% (Medium) - contest rating 1482
 * Pattern:  Greedy | Min heap | Huffman-style merging
 *
 * Example:
 *   Input:  sticks = [1,8,3,5]
 *   Output: 30
 *   Why:    merge 1+3=4, then 4+5=9, then 8+9=17; total cost is 30.
 *
 * Follow-ups:
 *   1. Why always merge the two shortest sticks?
 *      Any optimal merge tree can place the two shortest leaves deepest, matching Huffman coding.
 *   2. Can this be solved without a heap?
 *      Repeated sorting works but is slower; a heap gives O(n log n).
 *   3. What if new sticks are streamed in?
 *      Keep inserting into the same min heap and merge when the batch is finalized.
 */
public class MinimumCostToConnectSticks {

  public static void main(String[] args) {
    MinimumCostToConnectSticks solver = new MinimumCostToConnectSticks();
    int[][] inputs = {{1, 8, 3, 5}, {5}, {2, 4, 3}};
    int[] expected = {30, 0, 14};

    for (int i = 0; i < inputs.length; i++) {
      int got = solver.getMinimumCostToConnectSticks(inputs[i]);
      System.out.printf("sticks=%s -> %d  expected=%d%n",
          Arrays.toString(inputs[i]), got, expected[i]);
    }
  }



  /**
   * Intuition: a stick merged early contributes to the cost of every later merge
   * containing it. To minimize repeated contribution, the smallest sticks should
   * be buried deepest, exactly like building a Huffman tree.
   *
   * Algorithm:
   *   1. Put every stick length into a min heap.
   *   2. Repeatedly remove the two shortest sticks and pay their combined length.
   *   3. Push the combined stick back until one stick remains.
   *
   * Time:  O(n log n) - each heap insertion or removal costs logarithmic time.
   * Space: O(n) - the heap stores the remaining sticks.
   *
   * @param sticks array of stick lengths
   * @return minimum total cost to connect all sticks
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
