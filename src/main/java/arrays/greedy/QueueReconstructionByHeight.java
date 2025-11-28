package arrays.greedy;

import java.util.*;


/**
 * Problem Statement:
 * You are given an array of people, people, which are the attributes of some people in a queue (not necessarily in order). 
 * Each people[i] = [hi, ki] represents the ith person of height hi with exactly ki other people in front (on left side of array) 
 * who have a height greater than or equal to hi
 * 
 * Reconstruct and return the queue that is represented by the input array people. The returned queue should be formatted as an array queue, 
 * where queue[j] = [hj, kj] is the attributes of the jth person in the queue (queue[0] is the person at the front of the queue).
 *
 * Example:
 * Input: people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
 * Output: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
 * Explanation: Person with height 5 and k=0 is first, followed by height 7 k=0, and so on, satisfying all k conditions.
 *
 * LeetCode Link: https://leetcode.com/problems/queue-reconstruction-by-height/
 *
 * Follow-up Questions:
 * 1. How would you reconstruct if k represented people behind instead of in front?
 *    - Reverse the logic: sort by height ascending and insert from the end or adjust indices accordingly.
 * 2. What if we need to minimize the total height difference between adjacent people after reconstruction?
 *    - The reconstruction is unique based on constraints; if multiple possible, we'd need additional sorting or DP.
 * 3. How to handle if the input may not form a valid queue (invalid k values)?
 *    - Add validation during insertion to check if k is feasible, but problem assumes valid input.
 */
public class QueueReconstructionByHeight {

  public static void main(String[] args) {
    int[][] people = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
    int[][] reconstructedQueue = reconstructQueue(people);
    System.out.println(Arrays.deepToString(reconstructedQueue)); // Expected: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
  }

  /**
   * Reconstructs the queue by sorting people by height descending and k ascending, then inserting at k index.
   * This is the optimal greedy approach.
   * 
   * Intuition:
   * If you process the people from tallest to shortest, you can ensure that when you place a person P, 
   * all the people already in the queue are guaranteed to be taller than or equal to P. 
   * 
   * Step-by-step explanation:
   * 1. Sort the people array: first by height in descending order, then by k in ascending order for ties.
   * 2. Initialize an empty list for the result.
   * 3. For each person in the sorted array, insert them at index equal to their k value in the result list. This works because:
   *    - Taller people are placed first, so when we insert a shorter person at index k, there are already k taller or equal-height people in front of them.
   * 4. Convert the list back to a 2D array and return.
   *
   * Algorithm: Greedy Sort and Insert
   * Time Complexity: O(n^2) - Due to insertions in list (worst-case O(n) per insertion), but acceptable for n <= 1100.
   * Space Complexity: O(n) - For the result list.
   *
   * @param people the input array of [height, k] pairs
   * @return the reconstructed queue as 2D array
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