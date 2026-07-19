package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Arrays;

/**
 * Problem: Reorder Routes to Make All Paths Lead to the City Zero
 *
 * There are n cities and n - 1 one-way roads whose undirected shape is a tree.
 * Reverse the fewest roads so every city can eventually travel to city 0.
 *
 * Leetcode: https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/
 * Rating:   1634 (zerotrac Elo)
 * Pattern:  Graph | Tree BFS | Count edges pointing away from root
 *
 * Example:
 *   Input:  n = 6, connections = [[0,1],[1,3],[2,3],[4,0],[4,5]]
 *   Output: 3
 *   Why:    roads 0->1, 1->3, and 4->5 point away from city 0 during a traversal
 *           rooted at 0, so those three directions must be reversed.
 *
 * Follow-ups:
 *   1. Return which roads to reverse?
 *      Store the original edge whenever the traversal pays cost 1.
 *   2. The graph is not guaranteed to be a tree?
 *      Run shortest path on edge-reversal costs or first validate connectivity and cycles.
 *   3. Need every city to reach a different root?
 *      Re-root the traversal at that target and count outward-pointing original edges.
 *
 * Related: Minimum Edge Reversals So Every Node Is Reachable (2858), All Paths Lead to Destination (1059).
 */
public class ReorderRoutesToMakeAllPathsLeadToCityZero {

    public static void main(String[] args) {
        ReorderRoutesToMakeAllPathsLeadToCityZero solver = new ReorderRoutesToMakeAllPathsLeadToCityZero();
        int[] nodes = {6, 3};
        int[][][] connections = {{{0, 1}, {1, 3}, {2, 3}, {4, 0}, {4, 5}}, {{1, 0}, {2, 0}}};
        int[] expected = {3, 0};
        for (int i = 0; i < nodes.length; i++) {
            int output = solver.minReorder(nodes[i], connections[i]);
            System.out.printf("nodes=%d connections=%s -> %d  expected=%d%n", nodes[i], Arrays.deepToString(connections[i]), output, expected[i]);
        }
    }

    /**
     * Insight:
     *   The undirected shape is a tree, so BFS from city 0 visits every edge
     *   exactly once. For each edge we cross:
     *     - if the original arrow points AWAY from 0 → must be reversed (cost 1)
     *     - if the original arrow points TOWARD 0    → already fine     (cost 0)
     *   The answer is the sum of those costs.
     *
     * Steps:
     *   1. Build an undirected adjacency map where every edge is stored twice
     *      with the per-direction cost (see {@link #createAdjMap}).
     *   2. BFS from city 0; whenever we discover a new neighbor, add the cost
     *      of the edge we just crossed to the running total.
     *
     * Time  O(n) — one BFS over the tree.
     * Space O(n) — adjacency map + visited array + queue.
     */
    public int minReorder(int nodes, int[][] connections) {
        Map<Integer, List<int[]>> adjMap = createAdjMap(connections);

        Queue<Integer> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[nodes];
        queue.offer(0);
        visited[0] = true;

        int totalCost = 0;
        while (!queue.isEmpty()) {
            int currNode = queue.poll();
            List<int[]> neighbors = adjMap.getOrDefault(currNode, new ArrayList<>());

            for (int[] neighbor : neighbors) {
                if (visited[neighbor[0]]) continue;
                queue.offer(neighbor[0]);
                visited[neighbor[0]] = true;
                totalCost += neighbor[1];   // 1 if we had to walk against the original arrow
            }
        }
        return totalCost;
    }

    /**
     * Builds an undirected adjacency map where each entry is {neighbor, cost}.
     * For original edge from → to: store (to, 1) at `from` and (from, 0) at `to`,
     * so a BFS outward from 0 picks up cost 1 exactly when the arrow points away.
     */
    private Map<Integer, List<int[]>> createAdjMap(int[][] connections) {
        Map<Integer, List<int[]>> result = new HashMap<>();
        for (int[] connection : connections) {
            int from = connection[0];
            int to   = connection[1];
            result.computeIfAbsent(from, k -> new ArrayList<>()).add(new int[]{to,   1});
            result.computeIfAbsent(to,   k -> new ArrayList<>()).add(new int[]{from, 0});
        }
        return result;
    }

}
