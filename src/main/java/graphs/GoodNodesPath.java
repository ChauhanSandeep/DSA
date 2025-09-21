package graphs;

import java.util.*;


/**
 * Tree Path with Limited "Good" Nodes
 *
 * Problem Statement:
 * You are given a tree (acyclic connected undirected graph) with n nodes. Each node
 * is either "good" (1) or "bad" (0). Count the number of root-to-leaf paths such that
 * the number of "good" nodes in the path does not exceed a threshold maxGoodNodes.
 *
 * Example:
 * goodNodes = [0, 1, 0, 1, 1, 1]
 * edges = [[1, 2], [1, 5], [1, 6], [2, 3], [2, 4]]
 * maxGoodNodes = 1
 * Output: 2
 * Explanation: Only the paths [1-5] and [1-6] have at most 1 good node.
 *
 * Approach:
 * 1. Build an adjacency list to represent the tree.
 * 2. Use DFS traversal starting from the root. At each node, update the running count of "good" nodes.
 * 3. If we reach a leaf node with a good node count within the allowed threshold, count this path.
 * 4. Avoid revisiting the parent node to prevent cycles.
 *
 * Algorithm: DFS/Backtracking on Tree.
 * Time Complexity: O(N) since each node is visited once.
 * Space Complexity: O(N) due to recursion stack and adjacency list.
 *
 * LeetCode Link: https://www.geeksforgeeks.org/problems/root-to-leaf-paths-having-k-good-nodes/1 (similar)
 *
 * Follow-up:
 * - How do you modify the method for trees with millions of nodes? (Use iterative DFS or BFS)
 * - How would you count such paths but with maxGoodNodes distinct "good" types per path? (Add HashSet/Map for types)
 */
public class GoodNodesPath {

  public static void main(String[] args) {
    int[] goodNodes = {0, 1, 0, 1, 1, 1}; // 0-based node array
    int[][] edges = {{1, 2}, {1, 5}, {1, 6}, {2, 3}, {2, 4}};
    int maxGoodNodes = 1;

    GoodNodesPath solution = new GoodNodesPath();
    int result = solution.countValidPaths(goodNodes, edges, maxGoodNodes);
    System.out.println("Valid paths count: " + result);
  }

  private List<List<Integer>> adjacencyList;
  private int[] nodesGoodOrBad;

  /**
   * Counts the number of root-to-leaf paths in the tree with at most maxGoodNodes good nodes.
   *
   * Steps:
   * 1. Prepare an adjacency list for the undirected tree.
   * 2. Start DFS at the root (node 1 as per problem), marking parent to avoid revisiting.
   * 3. At each node, increase the count if it's 'good'.
   * 4. If leaf node (other than the root), return 1 (valid path) if count <= maxGoodNodes.
   *
   * Time: O(N) - N is number of nodes.
   * Space: O(N) - adjacency list + call stack.
   *
   * @param goodNodes Array marking each node as good(1) or bad(0), 0-indexed.
   * @param edges     2D array denoting bidirectional edges (1-indexed).
   * @param maxGoodNodes Maximum allowed good nodes in a root-to-leaf path.
   * @return Number of valid paths.
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
