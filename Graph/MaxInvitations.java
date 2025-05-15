package Graph;

import java.util.*;

/**
 * LeetCode 2127: Maximum Employees to Be Invited to a Meeting
 * Problem Link: https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/
 *
 * Problem:
 * - Given an array 'favorite' where favorite[i] is the employee that employee 'i' prefers to sit next to.
 * - The goal is to find the maximum number of employees that can be invited to the meeting.
 *
 * Approach:
 * 1. **Compute In-Degree** → Find the number of incoming edges for each node.
 * 2. **Topological Sort (BFS)** → Remove nodes with no incoming edges (acyclic parts).
 * 3. **Process Remaining Cycles** → Identify cycles and compute their sizes.
 * 4. **Handle Special Case of 2-Node Cycles** → Consider longest incoming chains.
 *
 * Time Complexity: O(N) → Each node is processed once.
 * Space Complexity: O(N) → For visited array, in-degree array, and queue.
 */
public class MaxInvitations {

    public static void main(String[] args) {
        int[] invitations = {1, 2, 0};
        System.out.println("Maximum Invitations: " + new MaxInvitations().maximumInvitations(invitations));
    }

    /**
     * Finds the maximum number of employees that can be invited to the meeting.
     * @param favorite - Array representing favorite coworkers for each employee
     * @return Maximum number of invitations possible
     */
    public int maximumInvitations(int[] favorite) {
        int n = favorite.length;
        boolean[] visited = new boolean[n];

        // Step 1: Compute In-Degree for Topological Sort
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            inDegree[favorite[i]]++;
        }

        // Step 2: Topological Sorting to Remove Acyclic Nodes
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                visited[i] = true;
                queue.offer(i);
            }
        }

        // Step 3: Compute Longest Path to Any Node (Handles Chains Leading to Cycles)
        int[] longestPath = new int[n];
        while (!queue.isEmpty()) {
            int employee = queue.poll();
            int next = favorite[employee];

            longestPath[next] = Math.max(longestPath[next], longestPath[employee] + 1);
            if (--inDegree[next] == 0) {
                visited[next] = true;
                queue.offer(next);
            }
        }

        // Step 4: Identify Cycles and Compute Max Invitation Count
        int maxCycleSize = 0, chainContribution = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {  // Found a cycle
                int cycleSize = 0, current = i;

                // Count the cycle length
                while (!visited[current]) {
                    visited[current] = true;
                    current = favorite[current];
                    cycleSize++;
                }

                if (cycleSize == 2) {
                    // Special case: 2-node cycles with incoming chains
                    chainContribution += 2 + longestPath[i] + longestPath[favorite[i]];
                } else {
                    maxCycleSize = Math.max(maxCycleSize, cycleSize);
                }
            }
        }

        return Math.max(maxCycleSize, chainContribution);
    }
}
