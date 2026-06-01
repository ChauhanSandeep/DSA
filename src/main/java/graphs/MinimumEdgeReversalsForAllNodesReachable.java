package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2858. Minimum Edge Reversals So Every Node Is Reachable  (Hard)
 *
 * Problem: You are given a directed graph with n nodes labeled 0..n-1 and
 * exactly n-1 edges. The underlying undirected structure forms a tree. For
 * each node i, return the minimum number of edge reversals needed so that
 * every other node is reachable from i by following edge directions.
 *
 * Example 1:
 *   Input:  n = 4, edges = [[2,0],[2,1],[1,3]]
 *   Output: [1, 1, 0, 2]
 *   Explanation:
 *     - From 0: reverse 2→0 so 0 → 2 → 1 → 3. 1 reversal.
 *     - From 1: reverse 2→1 so 1 → 2 → 0, and 1 → 3 already works. 1 reversal.
 *     - From 2: nothing to reverse. 0 reversals.
 *     - From 3: reverse 1→3 and 2→1. 2 reversals.
 *
 * Example 2:
 *   Input:  n = 3, edges = [[1,2],[2,0]]
 *   Output: [2, 0, 1]
 *
 * Constraints:
 *   2 <= n <= 10^5
 *   edges.length == n - 1
 *   The underlying graph (ignoring directions) is a tree.
 *
 * LeetCode: https://leetcode.com/problems/minimum-edge-reversals-so-every-node-is-reachable
 */
public class MinimumEdgeReversalsForAllNodesReachable {

    /** Cost of walking with the original arrow (free) — invariant: WITH + AGAINST == 1. */
    private static final int COST_WITH_ARROW    = 0;
    /** Cost of walking against the original arrow (needs one reversal). */
    private static final int COST_AGAINST_ARROW = 1;

    /** A neighbor in the undirected adjacency list, plus the cost of stepping toward it. */
    private static final class Edge {
        final int neighbor;
        final int cost;          // 0 = with arrow, 1 = against arrow
        Edge(int neighbor, int cost) { this.neighbor = neighbor; this.cost = cost; }
    }

    // Shared bookkeeping (fields keep helper signatures small).
    private Map<Integer, List<Edge>> adjacency;
    private int[] parentOf;          // parentOf[v]       = v's parent in BFS tree from node 0
    private int[] costFromParent;    // costFromParent[v] = cost of stepping parentOf[v] -> v
    private int[] bfsOrder;          // discovery order from node 0 (parents before children)
    private int[] answer;            // answer[i]         = min reversals when rooted at i

    /**
     * Insight (re-rooting tree DP):
     *   Treat the tree as undirected. Give every edge TWO costs:
     *       cost(u→v) = 0 if the original arrow already points u→v, else 1.
     *   For ANY edge: cost(u→v) + cost(v→u) == 1.   ← the magic invariant
     *
     *   answer[root] = sum of edge step-costs when walking outward from `root`.
     *
     *   Moving the root from u to a neighbor v flips ONLY the edge (u, v);
     *   every other edge is still "above vs below" the same way. So:
     *       answer[v] = answer[u] - cost(u→v) + cost(v→u)              (★)
     *                 = answer[u] - cost(u→v) + (1 - cost(u→v))
     *   Once we know answer[0], every other answer follows in O(1).
     *
     * Steps:
     *   1. Build the weighted undirected adjacency (see {@link #buildWeightedAdjacency}).
     *   2. BFS from node 0 to compute answer[0] and record parent / step-cost / BFS order
     *      (see {@link #seedAnswerForRootZero}).
     *   3. Walk bfsOrder; apply recurrence (★) once per child
     *      (see {@link #rerootToAllOtherNodes}).
     *
     * Worked trace (Example 1, edges = [[2,0],[2,1],[1,3]]):
     *   Step 2 — BFS from 0: 0→2 (+1), 2→1 (+0), 1→3 (+0)  →  answer[0] = 1
     *            bfsOrder = [0, 2, 1, 3]
     *   Step 3 — apply (★):
     *     v=2, parent=0, cost(0→2)=1 → answer[2] = 1 - 1 + 0 = 0
     *     v=1, parent=2, cost(2→1)=0 → answer[1] = 0 - 0 + 1 = 1
     *     v=3, parent=1, cost(1→3)=0 → answer[3] = 1 - 0 + 1 = 2
     *   Result = [1, 1, 0, 2]  ✓
     *
     * Why iterative BFS (not recursion)?
     *   n can be 10^5 and the tree can be a straight line → recursion would blow
     *   the JVM call stack.
     *
     * Time  O(n) — two linear BFS-style passes.
     * Space O(n) — adjacency map + bookkeeping arrays.
     */
    public int[] minEdgeReversals(int nodes, int[][] edges) {
        parentOf       = new int[nodes];
        costFromParent = new int[nodes];
        bfsOrder       = new int[nodes];
        answer         = new int[nodes];
        Arrays.fill(parentOf, -1);

        buildWeightedAdjacency(nodes, edges);
        answer[0] = seedAnswerForRootZero();
        rerootToAllOtherNodes();

        return answer;
    }

    /**
     * Builds an undirected adjacency map where each entry is {neighbor, cost}.
     * For original edge from → to: store (to, 0) at `from` and (from, 1) at `to`.
     */
    private void buildWeightedAdjacency(int nodes, int[][] edges) {
        adjacency = new HashMap<>(nodes * 2);
        for (int i = 0; i < nodes; i++) {
            adjacency.put(i, new ArrayList<>());
        }
        for (int[] edge : edges) {
            int from = edge[0], to = edge[1];
            adjacency.get(from).add(new Edge(to,   COST_WITH_ARROW));
            adjacency.get(to)  .add(new Edge(from, COST_AGAINST_ARROW));
        }
    }

    /**
     * BFS from node 0. Returns answer[0] (sum of step-costs to reach everyone)
     * and fills parentOf, costFromParent, bfsOrder for the re-rooting pass.
     */
    private int seedAnswerForRootZero() {
        int totalCost = 0;
        boolean[] visited = new boolean[adjacency.size()];
        Deque<Integer> queue = new ArrayDeque<>();

        queue.offer(0);
        visited[0] = true;
        int writeIndex = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            bfsOrder[writeIndex++] = current;

            for (Edge edge : adjacency.get(current)) {
                if (visited[edge.neighbor]) continue;
                visited[edge.neighbor]        = true;
                parentOf[edge.neighbor]       = current;
                costFromParent[edge.neighbor] = edge.cost;
                totalCost                    += edge.cost;
                queue.offer(edge.neighbor);
            }
        }
        return totalCost;
    }

    /**
     * Walks bfsOrder so every parent's answer is final before its child is read,
     * applying recurrence (★): answer[child] = answer[parent] - cP + (1 - cP).
     */
    private void rerootToAllOtherNodes() {
        for (int i = 1; i < bfsOrder.length; i++) {
            int child  = bfsOrder[i];
            int parent = parentOf[child];

            int costParentToChild = costFromParent[child];
            int costChildToParent = 1 - costParentToChild;   // invariant: sums to 1

            answer[child] = answer[parent] - costParentToChild + costChildToParent;
        }
    }

    public static void main(String[] args) {
        MinimumEdgeReversalsForAllNodesReachable solver = new MinimumEdgeReversalsForAllNodesReachable();
        System.out.println(Arrays.toString(solver.minEdgeReversals(4, new int[][]{{2,0},{2,1},{1,3}}))); // [1,1,0,2]
        System.out.println(Arrays.toString(solver.minEdgeReversals(3, new int[][]{{1,2},{2,0}})));       // [2,0,1]
    }
}
