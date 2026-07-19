package graphs;

import java.util.*;


/**
 * Problem: Root-to-Leaf Paths with Limited Good Nodes
 *
 * Given a rooted tree where each node is marked good or bad, count root-to-leaf
 * paths that contain at most a given number of good nodes. Edges are one-indexed,
 * while the good-node array stores node labels in zero-indexed order.
 *
 * Source: GeeksforGeeks - https://www.geeksforgeeks.org/problems/root-to-leaf-paths-having-k-good-nodes/1
 * Pattern:  Tree | DFS | Path-state pruning
 *
 * Example:
 *   Input:  good = [0,1,0,1,1,1], edges = [[1,2],[1,5],[1,6],[2,3],[2,4]], maxGood = 1
 *   Output: 3
 *   Why:    paths 1-2-3, 1-5, and 1-6 stay within one good node; path
 *           1-2-4 has two good nodes and is pruned.
 *
 * Follow-ups:
 *   1. Handle a tree with millions of nodes?
 *      Use iterative DFS and compressed adjacency arrays to avoid recursion overhead.
 *   2. Count paths with exactly k good nodes?
 *      Change the leaf check from at most k to equal k.
 *   3. Allow updates to node labels?
 *      Use tree decomposition or Euler-tour structures if many online queries are needed.
 */
public class GoodNodesPath {



    public static void main(String[] args) {
        GoodNodesPath solver = new GoodNodesPath();
        int[][][] edgeCases = {
            {{1, 2}, {1, 5}, {1, 6}, {2, 3}, {2, 4}},
            {}
        };
        int[][] goodCases = {{0, 1, 0, 1, 1, 1}, {1}};
        int[] limits = {1, 1};
        int[] expected = {3, 0};

        for (int i = 0; i < edgeCases.length; i++) {
            int output = solver.countValidPaths(goodCases[i], edgeCases[i], limits[i]);
            System.out.printf("good=%s edges=%s maxGood=%d  ->  %d  expected=%d%n",
                Arrays.toString(goodCases[i]), Arrays.deepToString(edgeCases[i]), limits[i], output, expected[i]);
        }
    }
  private List<List<Integer>> adjacencyList;
  private int[] nodesGoodOrBad;
    /**
     * Intuition: the tree is searched from node 1 while tracking how many good nodes
     * have appeared on the current root-to-node path. Once that count exceeds the
     * allowed maximum, every deeper path from that node is invalid and can be pruned.
     * Leaf nodes reached within the limit count as valid root-to-leaf paths.
     *
     * Algorithm:
     *   1. Build the undirected adjacency list from the edges.
     *   2. DFS from node 1, carrying the current number of good nodes.
     *   3. Stop a branch when the good-node count exceeds maxGoodNodes.
     *   4. Count leaf nodes whose path stayed within the limit.
     *
     * Time:  O(n) - each tree edge is traversed a constant number of times.
     * Space: O(n) - adjacency storage plus recursion stack.
     *
     * @param goodNodes array marking whether each node is good
     * @param edges undirected tree edges
     * @param maxGoodNodes maximum good nodes allowed on a root-to-leaf path
     * @return number of valid root-to-leaf paths
     */
  public int countValidPaths(int[] goodNodes, int[][] edges, int maxGoodNodes) {
    int n = goodNodes.length;
    if (n == 0) {
      return 0;
    }

    this.nodesGoodOrBad = goodNodes;
    adjacencyList = new ArrayList<>(n + 1); // 1-based
    for (int i = 0; i <= n; i++) {
      adjacencyList.add(new ArrayList<>());
    }
    for (int[] edge : edges) {
      int u = edge[0], v = edge[1]; // Edges are 1-based
      adjacencyList.get(u).add(v);
      adjacencyList.get(v).add(u);
    }
    // Root is node 1, parent is -1, good-node count is 0
    return dfs(1, -1, 0, maxGoodNodes);
  }

  /**
   * Recursive DFS to count valid root-to-leaf paths.
   *
   * @param currentNode Current node's index (1-based).
   * @param parentNode  Parent node's index to avoid backtracking.
   * @param currentGoodNodes Number of good nodes in this path so far.
   * @param allowedGoodNodes Maximum allowable good nodes.
   * @return Number of valid paths from current node.
   */
  private int dfs(int currentNode, int parentNode, int currentGoodNodes, int allowedGoodNodes) {
    int idx = currentNode - 1; // to use 0-based indexing
    int newGoodCount = currentGoodNodes + nodesGoodOrBad[idx];

    // If this path exceeds good node limit, prune path
    if (newGoodCount > allowedGoodNodes) {
      return 0;
    }

    // Node is a leaf (degree 1, but not the root)
    if (adjacencyList.get(currentNode).size() == 1 && currentNode != 1) {
      return 1;
    }
    int validPaths = 0;
    for (int neighbor : adjacencyList.get(currentNode)) {
      if (neighbor == parentNode) {
        continue; // Avoid cycles
      }
      validPaths += dfs(neighbor, currentNode, newGoodCount, allowedGoodNodes);
    }
    return validPaths;
  }
}
