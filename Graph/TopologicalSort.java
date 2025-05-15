package Graph;

import java.util.*;

/**
 * **Topological Sort using DFS (with cycle detection)**
 *
 * ### **Approach:**
 * - Uses **Depth First Search (DFS)** to explore the graph.
 * - Detects cycles using **three-state marking**: `UNVISITED`, `VISITING`, `VISITED`.
 * - If a cycle is found, **returns an empty array** (since topological sorting isn't possible).
 *
 * ### **Time Complexity:**
 * - `O(V + E)`, where `V` is the number of vertices and `E` is the number of edges.
 * - Each node and edge are processed **only once**.
 *
 * ### **Space Complexity:**
 * - `O(V)`, storing the adjacency list, visited state, and result list.
 */
public class TopologicalSort {

    private static final int UNVISITED = 0;
    private static final int VISITING = 1;  // Marks nodes in the current DFS stack (for cycle detection)
    private static final int VISITED = 2;

    /**
     * **Performs Topological Sorting on a Directed Acyclic Graph (DAG).**
     *
     * @param N   Number of nodes in the graph.
     * @param adj Adjacency list representation of the graph.
     * @return Topological ordering as an integer array. If a cycle is detected, returns an empty array.
     */
    public static int[] topoSort(int N, ArrayList<ArrayList<Integer>> adj) {
        if (adj == null || N == 0) return new int[0];  // Edge case: Empty graph

        List<Integer> topoOrder = new ArrayList<>();
        int[] visited = new int[N];

        for (int i = 0; i < N; i++) {
            if (visited[i] == UNVISITED) {
                if (!dfsTopoSort(i, visited, adj, topoOrder)) {
                    return new int[0]; // Cycle detected, return empty array
                }
            }
        }

        // Reverse the result since DFS adds nodes in postorder (dependencies first)
        Collections.reverse(topoOrder);
        return topoOrder.stream().mapToInt(i -> i).toArray();
    }

    /**
     * **DFS-based function to perform Topological Sorting.**
     *
     * @param node      Current node being visited.
     * @param visited   Tracking array (`UNVISITED`, `VISITING`, `VISITED`).
     * @param adj       Graph adjacency list.
     * @param topoOrder List storing the topological order.
     * @return `true` if the graph is acyclic, `false` if a cycle is detected.
     */
    private static boolean dfsTopoSort(int node, int[] visited, ArrayList<ArrayList<Integer>> adj, List<Integer> topoOrder) {
        visited[node] = VISITING; // Mark as visiting (part of current DFS recursion stack)

        for (Integer neighbor : adj.get(node)) {
            if (visited[neighbor] == UNVISITED) {
                if (!dfsTopoSort(neighbor, visited, adj, topoOrder)) {
                    return false; // Cycle detected
                }
            } else if (visited[neighbor] == VISITING) {
                return false; // Cycle detected
            }
        }

        visited[node] = VISITED; // Mark as fully processed
        topoOrder.add(node); // Append node after all dependencies are processed
        return true;
    }

    public static void main(String[] args) {
        int N = 6;
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < N; i++) adj.add(new ArrayList<>());

        // Graph edges: Directed Acyclic Graph (DAG)
        adj.get(5).add(2);
        adj.get(5).add(0);
        adj.get(4).add(0);
        adj.get(4).add(1);
        adj.get(2).add(3);
        adj.get(3).add(1);

        int[] result = topoSort(N, adj);
        if (result.length == 0) {
            System.out.println("Cycle detected! Topological sorting not possible.");
        } else {
            System.out.println("Topological Sort: " + Arrays.toString(result));
        }
    }
}
