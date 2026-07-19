package graphs;

import java.util.*;

/**
 * Problem: Largest Distance Between Nodes of a Tree
 *
 * Given an undirected tree as adjacency lists, return the largest number of edges
 * on any path between two nodes. This largest distance is the tree diameter.
 *
 * Source: InterviewBit - https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 * Pattern:  Tree | Diameter | Two-pass DFS
 *
 * Example:
 *   Input:  adjacency = [[1,2],[0,3],[0],[1,4],[3]]
 *   Output: 4
 *   Why:    the longest path is 2-0-1-3-4, which uses four edges.
 *
 * Follow-ups:
 *   1. Return the actual diameter path?
 *      Store parents during the second DFS and backtrack from the farthest node.
 *   2. Handle weighted tree edges?
 *      Accumulate edge weights instead of adding one per edge.
 *   3. Handle a general graph with cycles?
 *      Longest simple path is NP-hard, so the tree-specific trick no longer applies.
 *
 * Related: Diameter of Binary Tree (543), Tree Diameter (1245).
 */
public class LargestDistanceNodes {


    public static void main(String[] args) {
        LargestDistanceNodes solver = new LargestDistanceNodes();
        int[][][] edges = {{{0, 1}, {0, 2}, {1, 3}, {3, 4}}, {}};
        int[] nodeCounts = {5, 1};
        int[] expected = {4, 0};

        for (int i = 0; i < edges.length; i++) {
            @SuppressWarnings("unchecked")
            ArrayList<Integer>[] graph = (ArrayList<Integer>[]) new ArrayList<?>[nodeCounts[i]];
            for (int node = 0; node < nodeCounts[i]; node++) {
                graph[node] = new ArrayList<>();
            }
            for (int[] edge : edges[i]) {
                graph[edge[0]].add(edge[1]);
                graph[edge[1]].add(edge[0]);
            }
            int output = solver.solve(graph);
            System.out.printf("edges=%s  ->  %d  expected=%d%n", Arrays.deepToString(edges[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: the largest distance in a tree is its diameter. DFS computes the
     * deepest downward path from each node; the best diameter through that node is
     * the sum of its two largest child heights. Taking the maximum over nodes gives
     * the answer.
     *
     * Algorithm:
     *   1. DFS from the root through the adjacency list.
     *   2. For each node, track the two largest child depths returned by recursion.
     *   3. Update the global best distance with the sum of those two depths.
     *   4. Return the largest single downward depth to the parent.
     *
     * Time:  O(n) - every node and tree edge is processed once.
     * Space: O(n) - recursion stack in the worst case.
     *
     * @param adjacencyList tree adjacency list
     * @return diameter length measured in edges
     */
    public int solve(ArrayList<Integer>[] adjacencyList) {
        int numNodes = adjacencyList.length;
        if (numNodes <= 1) return 0;

        // First DFS: Find farthest node from node 0
        int[] firstDFS = dfs(adjacencyList, 0, -1);
        int farthestNode = firstDFS[0];

        // Second DFS: Find farthest node from the node found in first DFS
        int[] secondDFS = dfs(adjacencyList, farthestNode, -1);
        int diameter = secondDFS[1];

        return diameter;
    }

    /**
     * Helper: Performs DFS to find farthest node and its distance from start.
     *
     * @param graph Adjacency list representation of tree
     * @param currentNode Current node being visited
     * @param parentNode Parent of current node (-1 for root)
     * @return Array [farthestNode, maxDistance] where:
     *         - farthestNode: node with maximum distance from start
     *         - maxDistance: distance to farthest node
     */
    private int[] dfs(ArrayList<Integer>[] graph, int currentNode, int parentNode) {
        int maxDistance = 0;
        int farthestNode = currentNode;

        // Explore all neighbors except parent
        for (int neighbor : graph[currentNode]) {
            if (neighbor == parentNode) continue;

            // Recursively find farthest node in subtree
            int[] result = dfs(graph, neighbor, currentNode);
            int subtreeFarthestNode = result[0];
            int subtreeDistance = result[1] + 1; // +1 for edge to neighbor

            // Update farthest node if we found a longer path
            if (subtreeDistance > maxDistance) {
                maxDistance = subtreeDistance;
                farthestNode = subtreeFarthestNode;
            }
        }

        return new int[]{farthestNode, maxDistance};
    }

    /**
     * Alternative method: Single DFS with height tracking (for binary trees).
     * Step-by-step:
     *  1. Define diameter as max path length passing through any node
     *  2. For each node, calculate:
     *     - Height of left subtree
     *     - Height of right subtree
     *     - Diameter passing through this node = leftHeight + rightHeight
     *  3. Track maximum diameter seen across all nodes
     *  4. Return height of subtree for recursive computation
     *
     * Note: This approach works best for binary trees. For general trees with
     * multiple children, need to find two tallest subtrees.
     *
     * Key Insight:
     * Diameter through any node = sum of heights of two tallest subtrees from that node.
     * We compute this for every node and return the maximum.
     *
     * Algorithm: Single DFS with height computation.
     * Time Complexity: O(n), single traversal.
     * Space Complexity: O(h) for recursion stack, where h is tree height.
     */
    public int solveWithHeights(ArrayList<Integer>[] adjacencyList) {
        int numNodes = adjacencyList.length;
        if (numNodes <= 1) return 0;

        int[] maxDiameter = new int[1]; // Use array to pass by reference
        calculateHeight(adjacencyList, 0, -1, maxDiameter);

        return maxDiameter[0];
    }

    /**
     * Helper: Calculates height of subtree and updates maximum diameter.
     *
     * @param graph Adjacency list
     * @param currentNode Current node being processed
     * @param parentNode Parent of current node
     * @param maxDiameter Array holding maximum diameter found so far
     * @return Height of subtree rooted at currentNode
     */
    private int calculateHeight(ArrayList<Integer>[] graph, int currentNode,
                               int parentNode, int[] maxDiameter) {
        int maxHeight1 = 0; // Tallest subtree height
        int maxHeight2 = 0; // Second tallest subtree height

        // Process all children (neighbors except parent)
        for (int neighbor : graph[currentNode]) {
            if (neighbor == parentNode) continue;

            // Get height of subtree rooted at child
            int childHeight = calculateHeight(graph, neighbor, currentNode, maxDiameter) + 1;

            // Update two tallest subtree heights
            if (childHeight > maxHeight1) {
                maxHeight2 = maxHeight1;
                maxHeight1 = childHeight;
            } else if (childHeight > maxHeight2) {
                maxHeight2 = childHeight;
            }
        }

        // Diameter through current node = sum of two tallest subtrees
        int diameterThroughNode = maxHeight1 + maxHeight2;
        maxDiameter[0] = Math.max(maxDiameter[0], diameterThroughNode);

        // Return height of subtree rooted at current node
        return maxHeight1;
    }
}
