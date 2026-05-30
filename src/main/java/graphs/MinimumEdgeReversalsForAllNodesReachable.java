package graphs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2858. Minimum Edge Reversals So Every Node Is Reachable  (Hard, ~rating 2400)
 * LeetCode: https://leetcode.com/problems/minimum-edge-reversals-so-every-node-is-reachable
 *
 * ──────────────────────────────────────────────────────────────────────────────
 *  PROBLEM (one line):
 *     The graph is a tree with directed edges. For each node i, return the
 *     minimum number of edges we must reverse so that every other node is
 *     reachable from i by following edge directions.
 *
 *  KEY INSIGHT  (this is the whole solution — re-read this first):
 *     Treat the tree as undirected. Give every edge TWO costs:
 *         cost(u -> v) = 0  if the original arrow already points u -> v
 *         cost(v -> u) = 1  (we'd have to flip it)
 *     For ANY edge: cost(u -> v) + cost(v -> u) == 1.   ← the magic invariant
 *
 *     answer[root] = sum of edge step-costs when walking outward from `root`.
 *
 *     When we MOVE the root from u to a neighbor v, only ONE edge changes
 *     direction relative to the root — the edge (u, v) itself. Every other
 *     edge is still "above vs below" the same way as before. Therefore:
 *
 *         answer[v] = answer[u] - cost(u -> v) + cost(v -> u)        (★)
 *                   = answer[u] - cost(u -> v) + (1 - cost(u -> v))
 *
 *     So once we know answer[0], every other answer follows in O(1) per node.
 *
 *  ALGORITHM (two linear passes, O(n) total):
 *     STEP 1  Build undirected adjacency where each entry carries its step-cost.
 *     STEP 2  BFS from node 0:
 *               - compute answer[0] (sum of step-costs encountered)
 *               - remember parentOf[v] and costFromParent[v] for every v
 *               - remember the BFS order so STEP 3 sees parents before children
 *     STEP 3  Walk nodes in BFS order; apply recurrence (★) in O(1) each.
 *
 *  WORKED EXAMPLE  (edges = [[2,0],[2,1],[1,3]],  n = 4):
 *
 *       2                  Edge costs:
 *      ↓ ↓                   0—2 : 0→2 costs 1, 2→0 costs 0
 *      0 1                   1—2 : 1→2 costs 1, 2→1 costs 0
 *        ↓                   1—3 : 3→1 costs 1, 1→3 costs 0
 *        3
 *
 *     STEP 2 (BFS from 0):
 *       0 → 2 (cost 1), 2 → 1 (cost 0), 1 → 3 (cost 0)
 *       answer[0] = 1.   bfsOrder = [0, 2, 1, 3]
 *
 *     STEP 3 (re-root):
 *       v=2, parent=0, cost(0→2)=1 → answer[2] = 1 - 1 + 0 = 0
 *       v=1, parent=2, cost(2→1)=0 → answer[1] = 0 - 0 + 1 = 1
 *       v=3, parent=1, cost(1→3)=0 → answer[3] = 1 - 0 + 1 = 2
 *
 *     Result = [1, 1, 0, 2]  ✓
 *
 *  WHY ITERATIVE BFS (not recursion)?
 *     n can be 10^5 and the tree can be a straight line → recursion would
 *     blow the JVM call stack. Iterative is mandatory here.
 *
 *  WHY STORE bfsOrder?
 *     STEP 3 needs parents processed before children. BFS order guarantees
 *     that for free, so we replace a second traversal with a flat for-loop.
 * ──────────────────────────────────────────────────────────────────────────────
 */
public class MinimumEdgeReversalsForAllNodesReachable {

    /* ───── Invariant: for any edge, COST_WITH_ARROW + COST_AGAINST_ARROW == 1 ───── */
    private static final int COST_WITH_ARROW    = 0;  // walking with the arrow is free
    private static final int COST_AGAINST_ARROW = 1;  // walking against requires 1 reversal

    /** A neighbor in the undirected adjacency list, plus the cost of stepping toward it. */
    private static final class Edge {
        final int neighbor;
        final int cost;          // 0 = with arrow, 1 = against arrow
        Edge(int neighbor, int cost) { this.neighbor = neighbor; this.cost = cost; }
    }

    /* ─── Shared bookkeeping (fields keep helper signatures small & readable) ─── */
    private Map<Integer, List<Edge>> adjacency;
    private int[] parentOf;          // parentOf[v]       = v's parent in the BFS tree from node 0
    private int[] costFromParent;    // costFromParent[v] = cost of stepping parentOf[v] -> v
    private int[] bfsOrder;          // nodes in discovery order (parents before children)
    private int[] answer;            // answer[i]         = min reversals when rooted at i

    /**
     * Entry point: see class Javadoc for the full algorithm.
     * Time  O(n)  -  two linear BFS-style passes.
     * Space O(n)  -  adjacency list + bookkeeping arrays.
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

    /** STEP 1 — Undirected adjacency where each edge carries its per-direction cost. */
    private void buildWeightedAdjacency(int nodes, int[][] edges) {
        adjacency = new HashMap<>(nodes * 2);
        for (int i = 0; i < nodes; i++) {
            adjacency.put(i, new ArrayList<>());
        }
        for (int[] edge : edges) {
            int from = edge[0], to = edge[1];
            // Original arrow is from -> to.
            adjacency.get(from).add(new Edge(to,   COST_WITH_ARROW));     // with arrow
            adjacency.get(to)  .add(new Edge(from, COST_AGAINST_ARROW));  // against arrow
        }
    }

    /**
     * STEP 2 — BFS from node 0.
     *   - returns answer[0] = total step-cost reaching every node from 0
     *   - fills parentOf[], costFromParent[], bfsOrder[] for STEP 3
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
                costFromParent[edge.neighbor] = edge.cost;   // cost of (current -> neighbor)
                totalCost                    += edge.cost;   // accumulated only for root = 0
                queue.offer(edge.neighbor);
            }
        }
        return totalCost;
    }

    /**
     * STEP 3 — Apply recurrence (★) in BFS order so every parent is already solved
     * when we reach its child:
     *     answer[child] = answer[parent] - cost(parent -> child) + cost(child -> parent)
     *                   = answer[parent] - costParentToChild     + (1 - costParentToChild)
     *
     * Intuition: moving the root across edge (parent, child) flips ONLY that
     * edge's contribution; every other edge stays "above vs below" as before.
     */
    private void rerootToAllOtherNodes() {
        // bfsOrder[0] is node 0; its answer is already set.
        for (int i = 1; i < bfsOrder.length; i++) {
            int child  = bfsOrder[i];
            int parent = parentOf[child];

            int costParentToChild = costFromParent[child];
            int costChildToParent = 1 - costParentToChild;   // invariant: sums to 1

            answer[child] = answer[parent] - costParentToChild + costChildToParent;
        }
    }

    /* ──────────────────────────── Local smoke test ──────────────────────────── */
    public static void main(String[] args) {
        int[][] edges = {{2, 0}, {2, 1}, {1, 3}};
        int[] result  = new MinimumEdgeReversalsForAllNodesReachable()
                            .minEdgeReversals(4, edges);
        System.out.println(Arrays.toString(result));   // Expected: [1, 1, 0, 2]
    }
}
