package Graph;

import java.util.LinkedList;
import java.util.Queue;

public class MaxInvitations {

    public static void main(String[] args) {
        int[] invitations = {1, 2, 0};
        System.out.println("Maximum Invitations: " + new MaxInvitations().maximumInvitations(invitations));
    }

    public int maximumInvitations(int[] favorite) {
        int size = favorite.length;
        boolean[] visited = new boolean[size];

        // Step 1: Compute In-Degree for Topological Sort
        int[] inDegree = new int[size];
        for (int i = 0; i < size; i++) {
            inDegree[favorite[i]]++;
        }

        // Step 2: Topological Sorting to Remove Acyclic Nodes
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            if (inDegree[i] == 0) {
                visited[i] = true;
                queue.offer(i);
            }
        }

        // Step 3: Compute Longest Path to Any Node
        int[] longestPath = new int[size];
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            int neighbor = favorite[curr];
            longestPath[neighbor] = Math.max(longestPath[neighbor], longestPath[curr] + 1);
            if (--inDegree[neighbor] == 0) {
                visited[neighbor] = true;
                queue.offer(neighbor);
            }
        }

        // Step 4: Identify Cycles
        int maxLoopSize = 0;
        int chainContribution = 0;

        for (int i = 0; i < size; i++) {
            if (!visited[i]) {  // Found a cycle
                int curr = i, loopSize = 0;

                // Count the cycle length
                while (!visited[curr]) {
                    visited[curr] = true;
                    curr = favorite[curr];
                    loopSize++;
                }

                if (loopSize == 2) {
                    // Special case: loops of length 2 with incoming chains
                    chainContribution += 2 + longestPath[i] + longestPath[favorite[i]];
                } else {
                    maxLoopSize = Math.max(maxLoopSize, loopSize);
                }
            }
        }

        return Math.max(maxLoopSize, chainContribution);
    }
}
