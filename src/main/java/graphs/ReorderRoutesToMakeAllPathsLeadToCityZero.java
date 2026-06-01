package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 1466. Reorder Routes to Make All Paths Lead to the City Zero  (Medium)
 *
 * Problem: There are n cities numbered 0..n-1 and n-1 one-way roads. The
 * underlying undirected structure forms a tree. Return the minimum number of
 * roads that must be reversed so that EVERY city can reach city 0.
 *
 * Input:  n = 6, connections = [[0,1],[1,3],[2,3],[4,0],[4,5]]
 * Output: 3
 * Explanation: Reverse 0→1, 1→3, 4→5 so every city can reach city 0.
 *
 * Constraints:
 *   2 <= n <= 5 * 10^4
 *   connections.length == n - 1
 *   The underlying graph (ignoring directions) is a tree.
 *
 * LeetCode: https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero
 */
public class ReorderRoutesToMakeAllPathsLeadToCityZero {

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

    public static void main(String[] args) {
        ReorderRoutesToMakeAllPathsLeadToCityZero solver = new ReorderRoutesToMakeAllPathsLeadToCityZero();
        System.out.println(solver.minReorder(6, new int[][]{{0,1},{1,3},{2,3},{4,0},{4,5}})); // 3
        System.out.println(solver.minReorder(5, new int[][]{{1,0},{1,2},{3,2},{3,4}}));       // 2
        System.out.println(solver.minReorder(3, new int[][]{{1,0},{2,0}}));                   // 0
    }
}
