package graphs;

import java.util.*;


/**
 * Problem: Maximum Employees to Be Invited to a Meeting
 *
 * Each employee has exactly one favorite employee and will attend only if seated
 * next to that favorite at a circular table. Return the largest number of
 * employees that can be invited while satisfying everyone.
 *
 * Leetcode: https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/ (Hard)
 * Rating:   2449 (zerotrac Elo)
 * Pattern:  Graph | Functional graph | Topological pruning and cycle analysis
 *
 * Example:
 *   Input:  favorite = [2,2,1,2]
 *   Output: 3
 *   Why:    employees 1 and 2 are mutual favorites, and employee 0 can chain into
 *           employee 2, so three people can be seated as 0,2,1.
 *
 * Follow-ups:
 *   1. Return the invited employee set?
 *      Store chain predecessors and reconstruct either the best large cycle or all mutual-pair chains.
 *   2. What if each employee accepts several favorites?
 *      The functional graph property disappears; this becomes a harder seating constraint problem.
 *   3. Handle dynamic favorite changes?
 *      Maintain indegrees, cycles, and longest incoming chains for affected components.
 *
 * Related: Course Schedule (207), Find Eventual Safe States (802).
 */
public class MaxInvitations {



    public static void main(String[] args) {
        MaxInvitations solver = new MaxInvitations();
        int[][] inputs = {{2, 2, 1, 2}, {1, 2, 0}};
        int[] expected = {3, 3};
        for (int i = 0; i < inputs.length; i++) {
            int output = solver.maximumInvitations(inputs[i]);
            System.out.printf("favorite=%s  ->  %d  expected=%d%n", Arrays.toString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: the favorite relation is a functional graph: every employee points
     * to exactly one next employee. The answer is either one large directed cycle,
     * or the sum of all mutual-pair cycles with the longest incoming chains attached
     * to each side.
     *
     * Algorithm:
     *   1. Compute indegrees and peel non-cycle nodes with a queue.
     *   2. Track the longest chain length ending at every node while peeling.
     *   3. Inspect remaining cycle nodes to find the largest cycle.
     *   4. Separately sum mutual pairs plus their two best incoming chain lengths.
     *
     * Time:  O(n) - each employee is queued or visited a constant number of times.
     * Space: O(n) - indegree, depth, visited, and queue storage.
     *
     * @param favoriteArr favoriteArr[i] is employee i's favorite employee
     * @return maximum employees that can attend the meeting
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
