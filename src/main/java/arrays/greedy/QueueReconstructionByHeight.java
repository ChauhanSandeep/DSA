package arrays.greedy;

import java.util.*;


/**
 * Problem: Queue Reconstruction by Height
 *
 * Each person is [height, k], where k is the number of people in front with
 * height at least that person's height. Reconstruct a queue satisfying every
 * person's k value.
 *
 * Leetcode: https://leetcode.com/problems/queue-reconstruction-by-height/ (Medium)
 * Rating:   acceptance 75.9% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Sorting | Indexed insertion
 *
 * Example:
 *   Input:  people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
 *   Output: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
 *   Why:    after taller people are placed first, inserting each shorter person
 *           at index k creates exactly k taller-or-equal people before them.
 *
 * Follow-ups:
 *   1. What if k counts people behind instead of in front?
 *      Mirror the insertion direction or transform k relative to the final length.
 *   2. What if the input may be invalid?
 *      Validate each insertion index and re-count the final queue constraints.
 *   3. Can insertion be faster than O(n^2)?
 *      Use an order-statistics tree or Fenwick tree over empty slots.
 *
 * Related: Count of Smaller Numbers After Self (315), Insert Interval (57).
 */
public class QueueReconstructionByHeight {

  public static void main(String[] args) {
    int[][][] inputs = {
        {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}},
        {{1, 0}}
    };
    int[][][] expected = {
        {{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}},
        {{1, 0}}
    };

    for (int i = 0; i < inputs.length; i++) {
      int[][] input = Arrays.stream(inputs[i]).map(int[]::clone).toArray(int[][]::new);
      int[][] got = reconstructQueue(input);
      System.out.printf("people=%s -> %s  expected=%s%n",
          Arrays.deepToString(inputs[i]), Arrays.deepToString(got), Arrays.deepToString(expected[i]));
    }
  }



  /**
   * Intuition: place taller people first because shorter people do not affect any
   * taller person's k count. Once all taller-or-equal people are already in the
   * list, inserting a person at index k creates exactly k qualifying people in
   * front of that person.
   *
   * Algorithm:
   *   1. Sort by height descending, and by k ascending for equal heights.
   *   2. Insert each person into the queue list at index person[1].
   *   3. Convert the list back to a 2D array.
   *
   * Time:  O(n^2) - list insertion can shift O(n) people for each person.
   * Space: O(n) - the reconstructed queue list stores all people.
   *
   * @param people array of [height, k] pairs
   * @return reconstructed queue satisfying every k constraint
   */
  public static int[][] reconstructQueue(int[][] people) {
    if (people == null || people.length == 0) {
      return new int[0][];
    }

    // Step 1: Sort by height descending, then k ascending
    Arrays.sort(people, (p1, p2) -> {
      if (p1[0] != p2[0]) {
        return Integer.compare(p2[0], p1[0]); // Taller first
      } else {
        return Integer.compare(p1[1], p2[1]); // Smaller k first for the same height
      }
    });
    // Here the array would be [{7, 0}, {7, 1}, {6, 1}, {5, 0}, {5, 2}, {4, 4}]

    // Step 2: Insert into list based on k position
    List<int[]> queue = new LinkedList<>();

    for (int[] person : people) {
      int indexToInsert = person[1];
      queue.add(indexToInsert, person); // Insert person at index k
    }
    // Here the queue would be [{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}]

    // Step 3: Convert list back to 2D array
    return queue.toArray(new int[people.length][2]);
  }
}
