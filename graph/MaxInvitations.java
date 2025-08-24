package graph;

import java.util.*;


/**
 * LeetCode 2127: Maximum Employees to Be Invited to a Meeting
 * Problem Link: https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/
 *
 * Problem Statement:
 * Given an array 'favorite' where favorite[i] is the employee that employee 'i' prefers to sit next to,
 * find the maximum number of employees that can be invited to a circular meeting table such that:
 * - Each invited employee sits directly next to their favorite employee.
 * - The arrangement forms a valid circular seating.
 *
 * Example:
 * Input: favorite = [2,2,1,2]
 * Output: 3
 * Explanation: Employee 0 likes 2, employee 1 likes 2, employee 2 likes 1, employee 3 likes 2.
 * We can invite employees 1, 2, 3 where 1 sits next to 2, 2 sits next to 1, and they form a valid circle.
 *
 * Approach:
 * The key insight is that the graph forms a functional graph (each node has exactly one outgoing edge).
 * We need to handle two cases:
 * 1. Large cycles (size > 2): We can invite all members of the largest cycle.
 * 2. Two-node mutual cycles: We can combine multiple 2-cycles with their incoming chains.
 *
 * Follow-up Questions:
 * - What if employees can have multiple favorites? (Convert to bipartite matching problem)
 * - How to handle dynamic updates to favorites? (Maintain cycle information incrementally)
 *
 * Time Complexity: O(N) - Each node is processed exactly once
 * Space Complexity: O(N) - For arrays and data structures
 */
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
   * 1. **Big cycle (size > 2):** Can seat all of them in a circle. But only **one such cycle** can be used,
   *    because multiple disconnected cycles can't be connected together in one big circle.
   * 2. **2-cycles (A ↔ B):** You can use **multiple such pairs** and add longest incoming chains to both A and B.
   *    These structures can be placed one after another in a larger circular arrangement.
   *
   * Final answer = max(largest cycle size, sum of all 2-cycles + their incoming chains)
   *
   * Time Complexity: O(N)
   * Space Complexity: O(N)
   *
   * @param favorites Array where favorites[i] is the preferred neighbor of employee i
   * @return Maximum number of employees that can be invited
   */
  public int maximumInvitations(int[] favorites) {
    int totalEmployees = favorites.length;
    boolean[] isProcessed = new boolean[totalEmployees];

    // Step 1: Calculate in-degrees for topological sorting
    int[] inDegreeCount = new int[totalEmployees];
    for (int employee = 0; employee < totalEmployees; employee++) {
      inDegreeCount[favorites[employee]]++;
    }

    // Step 2: Remove nodes with no incoming edges (start of chains)
    Queue<Integer> processingQueue = new LinkedList<>();
    for (int employee = 0; employee < totalEmployees; employee++) {
      if (inDegreeCount[employee] == 0) {
        isProcessed[employee] = true;
        processingQueue.offer(employee);
      }
    }

    // Step 3: Calculate longest chain leading to each node
    int[] longestIncomingChain = new int[totalEmployees]; // longestIncomingChain[i] = length of longest chain leading to employee i
    while (!processingQueue.isEmpty()) {
      int currentEmployee = processingQueue.poll();
      int favoriteEmployee = favorites[currentEmployee];

      // Update the longest chain to the favorite employee
      longestIncomingChain[favoriteEmployee] =
          Math.max(longestIncomingChain[favoriteEmployee], longestIncomingChain[currentEmployee] + 1);

      // Remove this edge and check if favorites becomes a leaf
      if (--inDegreeCount[favoriteEmployee] == 0) {
        isProcessed[favoriteEmployee] = true;
        processingQueue.offer(favoriteEmployee);
      }
    }

    // Step 4: Process remaining cycles
    int largestCycleSize = 0;
    int totalTwoCycleContribution = 0;

    for (int employee = 0; employee < totalEmployees; employee++) {
      if (!isProcessed[employee]) {
        // Found start of a cycle - traverse to find cycle length
        // Start of cycle is not processed in previous steps because it has incoming edges
        int cycleLength = 0;
        int currentNode = employee;

        while (!isProcessed[currentNode]) {
          isProcessed[currentNode] = true;
          currentNode = favorites[currentNode];
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
          int mutulaFav2 = favorites[employee];
          totalTwoCycleContribution += 2 + longestIncomingChain[mutualFav1] + longestIncomingChain[mutulaFav2];
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
}
