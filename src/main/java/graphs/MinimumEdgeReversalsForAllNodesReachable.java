package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Problem: Minimum Edge Reversals So Every Node Is Reachable
 *
 * Given a directed tree, compute for every possible root how many directed edges
 * must be reversed so that all other nodes become reachable from that root.
 * Return one answer per node.
 *
 * Leetcode: https://leetcode.com/problems/minimum-edge-reversals-so-every-node-is-reachable/ (Hard)
 * Rating:   2295 (zerotrac Elo)
 * Pattern:  Tree | Re-rooting DP | Edge reversal costs
 *
 * Example:
 *   Input:  n = 4, edges = [[2,0],[2,1],[1,3]]
 *   Output: [1, 1, 0, 2]
 *   Why:    root 2 already reaches everyone, root 0 or 1 needs one reversed edge,
 *           and root 3 needs two reversals to point paths outward.
 *
 * Follow-ups:
 *   1. Return which edges to reverse for each root?
 *      Store parent edges during traversal and reconstruct choices from the re-rooted orientation.
 *   2. Support weighted reversal costs?
 *      Store forward and backward costs per edge and adjust the re-root formula accordingly.
 *   3. Handle a graph that is not a tree?
 *      The one-edge re-root invariant breaks; use shortest paths or arborescence ideas instead.
 *
 * Related: Reorder Routes to Make All Paths Lead to City Zero (1466).
 */
public class MinimumEdgeReversalsForAllNodesReachable {


    public static void main(String[] args) {
        MinimumEdgeReversalsForAllNodesReachable solver = new MinimumEdgeReversalsForAllNodesReachable();
        int[][][] edges = {{{2, 0}, {2, 1}, {1, 3}}, {{1, 2}, {2, 0}}};
        int[] nodeCounts = {4, 3};
        String[] expected = {"[1, 1, 0, 2]", "[2, 0, 1]"};
        for (int i = 0; i < edges.length; i++) {
            int[] output = solver.minEdgeReversals(nodeCounts[i], edges[i]);
            System.out.printf("n=%d edges=%s  ->  %s  expected=%s%n",
                nodeCounts[i], Arrays.deepToString(edges[i]), Arrays.toString(output), expected[i]);
        }
    }
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
     * Intuition: choose node 0 as a temporary root. A DFS counts how many original
     * edges point the wrong way for every node to be reachable from 0. Re-rooting
     * across one edge adjusts the answer by exactly one: using an original forward
     * edge costs one more reversal for the child root, while using an original
     * backward edge costs one fewer.
     *
     * Algorithm:
     *   1. Build the original adjacency list with edge costs for direction changes.
     *   2. DFS from node 0 to compute the reversal count for root 0.
     *   3. DFS again to reroot answers across every edge.
     *   4. Store each node's computed reversal count in the result array.
     *
     * Time:  O(n) - each directed edge representation is traversed a constant number of times.
     * Space: O(n) - adjacency list, result array, and recursion stack.
     *
     * @param nodes number of nodes labeled 0 to nodes - 1
     * @param edges directed edges
     * @return reversals needed when each node is chosen as the root
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

}
