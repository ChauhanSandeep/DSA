package graphs;

import java.util.*;


/**
 * A company is organizing a meeting and has a list of n employees, waiting to be invited.
 * They have arranged for a large circular table, capable of seating any number of employees.
 * The employees are numbered from 0 to n - 1. Each employee has a favorite person and they will
 * attend the meeting only if they can sit next to their favorite person at the table.
 * The favorite person of an employee is not themself.
 *
 * Given a 0-indexed integer array favorite, where favorite[i] denotes the favorite person of
 * the ith employee, return the maximum number of employees that can be invited to the meeting.
 *
 * Example:
 * Input: favorite = [2,2,1,2]
 * Output: 3
 * Explanation: The maximum number of employees that can be invited to the meeting is 3.
 * Employee 0 can sit next to employee 2, employee 1 can sit next to employee 2, and employee 2
 * can sit next to employee 1. This forms a circular arrangement where everyone sits next to their favorite.
 *
 * LeetCode: https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if employees have multiple favorite people they can sit next to?
 *    Answer: Use maximum bipartite matching or modify to handle weighted graph preferences.
 * 2. How would you handle dynamic changes to favorite relationships during the meeting?
 *    Answer: Implement incremental graph algorithms with efficient updates to cycle detection.
 * 3. What if the table has limited capacity or specific seating constraints?
 *    Answer: Add constraint satisfaction problem solving with backtracking or integer programming.
 * 4. How to optimize for very large numbers of employees (10^6+)?
 *    Answer: Use parallel processing for independent components, compressed graph representations.
 *
 * Related Problems:
 * - LeetCode 207: Course Schedule (Cycle Detection)
 * - LeetCode 210: Course Schedule II (Topological Sort)
 * - LeetCode 1059: All Paths from Source Lead to Destination
 * LeetCode Contest Rating: 2449
 **/
public class MaxInvitations {

  public static void main(String[] args) {
    int[] favoriteEmployees = {1, 2, 0};
    MaxInvitations solution = new MaxInvitations();
    int result = solution.maximumInvitations(favoriteEmployees);
    System.out.println("Maximum employees that can be invited: " + result);
  }

  /**
   * Finds the maximum number of employees that can be invited to the meeting.
   *
   * Graph Insight:
   * - Each employee points to one other → forms a directed graph where each node has 1 outgoing edge.
   * - This creates:
   *   → Chains: A → B → C → cycle
   *   → Cycles: A → B → C → A or A ↔ B
   *
   * Two Possibilities in graph:
   * 1. Big cycle (size > 2): A big cycle is a cycle in the graph with more than two employees, e.g., A → B → C → A.
   *    You Can seat all of them in a circle. But only one such cycle can be used,
   *    because multiple disconnected cycles can't be connected together in one big circle.
   * 2. 2-cycles (A ↔ B): A 2-cycle is a mutual favorite pair, e.g., A ↔ B (A likes B, B likes A).
   *    You can use multiple such pairs and add longest incoming chains to both A and B.
   *    These structures can be placed one after another in a larger circular arrangement.
   *
   * Steps:
   * Step 1: Model as directed graph (each person points to their favorite)
   * Step 2: Remove chains using topological sort to find cycle lengths
   * Step 3: Calculate longest chains leading into each person
   * Step 4: Process cycles - either one large cycle OR multiple small cycles with chains
   * Step 5: Return maximum of the two possibilities
   * Final answer = max(largest cycle size, sum of all 2-cycles + their incoming chains)
   *
   * Time Complexity: O(N)
   * Space Complexity: O(N)
   *
   * @param favoriteArr Array where favorites[i] is the preferred neighbor of employee i
   * @return Maximum number of employees that can be invited
   */
  public int maximumInvitations(int[] favoriteArr) {
    int length = favoriteArr.length;
    boolean[] visited = new boolean[length]; // Node is considered processed when removed in topological sort

    // Step 1: Calculate in-degrees for topological sorting
    int[] inDegree = new int[length];
    for (int favorite : favoriteArr) {
      inDegree[favorite]++;
    }

    // Step 2: Remove nodes with no incoming edges (start of chains)
    Queue<Integer> queue = new LinkedList<>();
    for (int employee = 0; employee < length; employee++) {
      if (inDegree[employee] == 0) {
        visited[employee] = true;
        queue.offer(employee);
      }
    }

    // Step 3: Calculate the longest chain leading to each node
    // maxChainLen[i] = length of longest chain leading to employee i
    int[] maxChainLen = getMaxChainLength(favoriteArr, length, queue, inDegree, visited);

    // Step 4: Process remaining cycles
    int largestCycleSize = 0;
    int totalTwoCycleContribution = 0;

    for (int employee = 0; employee < length; employee++) {
      if (!visited[employee]) {
        // Found start of a cycle - traverse to find cycle length
        // Start of cycle is not processed in previous steps because it has incoming edges
        int cycleLength = 0;
        int currentNode = employee;

        while (!visited[currentNode]) {
          visited[currentNode] = true;
          currentNode = favoriteArr[currentNode];
          cycleLength++;
        }

        if (cycleLength == 2) {
          // Special Case: 2-size cycle (mutual favorites)
          // Example: A <-> B (A likes B, B likes A)
          //
          // This small cycle can be safely used in the final circular arrangement,
          // and we can extend it on both sides with the longest incoming chains
          // (employees who eventually lead into A or B).
          //
          // For example:
          //    C → A ↔ B ← D
          // => we can seat C, A, B, D in this order forming a part of the final circle.
          //
          // We are allowed to have multiple such 2-cycles (as long as they are disjoint),
          // and combine all of them in the final arrangement.
          //
          // So, we compute the contribution of this 2-cycle as:
          // 2 (for A and B) + longest incoming chain to A + longest incoming chain to B
          int mutualFav1 = employee;
          int mutualFav2 = favoriteArr[employee];
          totalTwoCycleContribution += 2 + maxChainLen[mutualFav1] + maxChainLen[mutualFav2];
        } else {
          // Regular Cycle of size > 2 (like A → B → C → A)
          //
          // This cycle is already self-contained and can sit together in a circle.
          // But we cannot attach any extra people (chains) to it, or it will break the condition
          // that everyone must sit next to their favorite.
          //
          // Since we can only include **one such cycle** in the final answer,
          // we just track the length of the largest one.
          largestCycleSize = Math.max(largestCycleSize, cycleLength);
        }
      }
    }

    // Return the maximum possible invitations
    return Math.max(largestCycleSize, totalTwoCycleContribution);
  }

  // Helper to calculate longest chains leading to each employee
  private static int[] getMaxChainLength(int[] favoriteArr, int length, Queue<Integer> queue, int[] inDegree,
      boolean[] visited) {
    int[] maxChainLen = new int[length]; // maxChainLen[i] = length of longest chain leading to employee i

    while (!queue.isEmpty()) {
      int currentEmployee = queue.poll();
      int favoriteEmployee = favoriteArr[currentEmployee];

      // Update the longest chain to the favorite employee
      maxChainLen[favoriteEmployee] = Math.max(maxChainLen[favoriteEmployee], maxChainLen[currentEmployee] + 1);

      // Remove this edge and check if favorites becomes a leaf
      inDegree[favoriteEmployee]--;
      if (inDegree[favoriteEmployee] == 0) {
        visited[favoriteEmployee] = true;
        queue.offer(favoriteEmployee);
      }
    }
    return maxChainLen;
  }
}
