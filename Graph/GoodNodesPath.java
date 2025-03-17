package Graph;

import java.util.*;

public class GoodNodesPath {
    /**
     * Problem: Count valid paths in a tree where the number of "good" nodes does not exceed a given threshold.
     * 
     * Intuition:
     * - The tree is represented as an adjacency list.
     * - A depth-first search (DFS) is used to traverse the tree while tracking the number of "good" nodes in the path.
     * - If the count of good nodes exceeds the limit `maxGoodNodes`, the path is invalid.
     * - If a node is a leaf (excluding the root), and its path is valid, count it.
     *
     * Algorithm:
     * - Construct an adjacency list from the given edge list.
     * - Perform a DFS starting from the root (node 1).
     * - If a path is valid, increment the path count.
     *
     * Time Complexity: O(N) - Since we traverse each node once.
     * Space Complexity: O(N) - For the adjacency list and recursion stack.
     * 
     * LeetCode Link (if applicable): [Add relevant problem link here if available]
     */
    public static void main(String[] args) {
        int[] goodNodes = {0, 1, 0, 1, 1, 1}; // 0-based indexing
        int[][] edges = {
                {1, 2},
                {1, 5},
                {1, 6},
                {2, 3},
                {2, 4}
        };
        int maxGoodNodes = 1;

        GoodNodesPath solution = new GoodNodesPath();
        int result = solution.countValidPaths(goodNodes, edges, maxGoodNodes);
        System.out.println("Valid paths count: " + result);
    }

    private List<List<Integer>> adjacencyList;
    private int[] goodNodes;

    /**
     * Counts the number of valid paths in the tree.
     * 
     * @param goodNodes Array indicating whether a node is "good" (1) or "bad" (0).
     * @param edges Array representing bidirectional edges in the tree.
     * @param maxGoodNodes Maximum allowed "good" nodes in a valid path.
     * @return Count of valid paths.
     */
    public int countValidPaths(int[] goodNodes, int[][] edges, int maxGoodNodes) {
        int nodeCount = goodNodes.length;
        if (nodeCount == 0) return 0;

        this.goodNodes = goodNodes;
        adjacencyList = new ArrayList<>(nodeCount + 1);

        // Initialize adjacency list
        for (int i = 0; i <= nodeCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        // Build the adjacency list for the tree
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            adjacencyList.get(u).add(v);
            adjacencyList.get(v).add(u);
        }

        // Start DFS from root (node 1), parent = -1 (no parent), initial good node count = 0
        return dfs(1, -1, 0, maxGoodNodes);
    }

    /**
     * DFS traversal to count valid paths.
     * 
     * @param currentNode Current node in traversal.
     * @param parentNode Parent node to avoid revisiting.
     * @param currentGoodCount Count of good nodes encountered so far.
     * @param maxGoodNodes Maximum allowed good nodes in a path.
     * @return Count of valid paths.
     */
    private int dfs(int currentNode, int parentNode, int currentGoodCount, int maxGoodNodes) {
        if (goodNodes[currentNode - 1] == 1) { // Adjust for 0-based index
            currentGoodCount++;
        }
        
        // If good nodes exceed the allowed limit, terminate this path
        if (currentGoodCount > maxGoodNodes) {
            return 0;
        }

        // If the node is a leaf (excluding root), it's a valid path
        if (adjacencyList.get(currentNode).size() == 1 && adjacencyList.get(currentNode).get(0) == parentNode) {
            return 1;
        }

        int validPathCount = 0;
        for (int neighbor : adjacencyList.get(currentNode)) {
            if (neighbor != parentNode) { // Avoid revisiting parent
                validPathCount += dfs(neighbor, currentNode, currentGoodCount, maxGoodNodes);
            }
        }
        return validPathCount;
    }
}
