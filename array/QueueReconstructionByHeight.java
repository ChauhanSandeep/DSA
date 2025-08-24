package array;

import java.util.*;


/**
 * 🔗 LeetCode: https://leetcode.com/problems/queue-reconstruction-by-height/
 *
 * Problem: Reconstruct the queue from a list of people.
 * Each person is represented as an array [h, k], where:
 *  - h = height of the person
 *  - k = number of people in front of them who have height >= h
 *
 * ✅ You must return a queue (array of people) such that:
 *  - People are positioned according to their `k` values.
 *
 * Example:
 * Input:  [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
 * Output: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
 *
 * ✅ Approach:
 *    1. Sort the input by descending height (`h`) and then by ascending `k`.
 *    2. Insert each person at index `k` in the list.
 *
 * Time Complexity: O(n²) (due to insertion at index in list)
 * Space Complexity: O(n)
 *
 * Follow-up Questions:
 * - Q: Can we optimize this to O(n log n)?
 *   A: No, because list insertion at index `k` is inherently O(n) in the worst case.
 * - Q: Can we use a different data structure to improve performance?
 *   A: Balanced BST or segment tree could help in variations, but not needed here.
 */
public class QueueReconstructionByHeight {

  public static void main(String[] args) {
    int[][] people = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
    int[][] reconstructedQueue = reconstructQueue(people);
    System.out.println(Arrays.deepToString(reconstructedQueue)); // Expected: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
  }

  /**
   * Reconstructs the queue based on height and the number of people in front.
   *
   * Steps:
   *    1. Sort by height descending, then by k-value ascending.
   *    2. Insert each person at index = k in the final queue.
   *
   * @param people array of people represented as [height, k]
   * @return the reconstructed queue
   */
  public static int[][] reconstructQueue(int[][] people) {
    if (people == null || people.length == 0) {
      return new int[0][];
    }

    // Step 1: Sort by height descending, then k ascending
    Arrays.sort(people, (p1, p2) -> {
      if (p1[0] != p2[0]) {
        return Integer.compare(p2[0], p1[0]); // Descending height
      } else {
        return Integer.compare(p1[1], p2[1]); // Ascending k
      }
    });
    // Here the array would be [{7, 0}, {7, 1}, {6, 1}, {5, 0}, {5, 2}, {4, 4}]

    // Step 2: Insert into list based on k position
    List<int[]> queue = new LinkedList<>();
    for (int[] person : people) {
      queue.add(person[1], person); // Insert person at index k
    }
    // Here the queue would be [{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}]

    // Step 3: Convert list back to 2D array
    return queue.toArray(new int[people.length][2]);
  }
}