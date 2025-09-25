package graphs;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Find Center of Star Graph
 *
 * There is an undirected star graph consisting of n nodes labeled from 1 to n. A star graph
 * is a graph where there is one center node and exactly n - 1 edges that connect the center
 * node with every other node. You are given a 2D integer array edges where each edges[i] = [ui, vi]
 * indicates that there is an edge between the nodes ui and vi. Return the center of the given star graph.
 *
 * Example:
 * Input: edges = [[1,2],[2,3],[4,2]]
 * Output: 2
 * Explanation: Node 2 is connected to every other node, so 2 is the center.
 *
 * LeetCode: https://leetcode.com/problems/find-center-of-star-graph
 *
 * Follow-up Questions:
 * 1. What if the graph is not guaranteed to be a star graph?
 *    Answer: Validate by checking if exactly one node has degree n-1 and others have degree 1.
 *
 * 2. How would you find the center in a weighted star graph?
 *    Answer: Same approach works - center node still has the highest degree.
 *
 * 3. What if you need to find centers of multiple star graphs in one graph?
 *    Answer: Use connected components and find center for each component.
 *    Related: https://leetcode.com/problems/find-eventual-safe-states/
 *
 * @author Sandeep
 */
public class FindCenterOfStarGraph {

    /**
     * Finds center using the fact that center appears in all edges (optimal O(1) solution).
     *
     * Algorithm:
     * 1. In a star graph, the center node appears in every edge
     * 2. Take first two edges and find the common node
     * 3. The common node is the center
     *
     * Time Complexity: O(1) - only examine first two edges
     * Space Complexity: O(1) - use constant extra space
     *
     * @param edges Array of edges in the star graph
     * @return The center node of the star graph
     */
    public int findCenter(int[][] edges) {
        // Edge case validation
        if (edges == null || edges.length < 2) {
            return -1; // Invalid input
        }

        // Get nodes from first two edges
        int edge1Node1 = edges[0][0];
        int edge1Node2 = edges[0][1];
        int edge2Node1 = edges[1][0];
        int edge2Node2 = edges[1][1];

        // Find common node between first two edges
        if (edge1Node1 == edge2Node1 || edge1Node1 == edge2Node2) {
            return edge1Node1;
        } else {
            return edge1Node2;
        }
    }

    /**
     * Alternative approach using degree counting.
     * This approach validates that the graph is indeed a star graph.
     *
     * Steps:
     * 1. Count the degree of each node using a HashMap.
     * 2. The center node will have a degree of n-1 (where n is number of nodes).
     *
     * Time Complexity: O(n) where n is number of edges
     * Space Complexity: O(v) where v is number of vertices
     */
    public int findCenterByDegree(int[][] edges) {
        if (edges == null || edges.length == 0) {
            return -1;
        }

        Map<Integer, Integer> degreeCount = new HashMap<>();
        int maxDegree = 0;
        int centerNode = -1;

        // Count degree of each node
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];

            degreeCount.put(node1, degreeCount.getOrDefault(node1, 0) + 1);
            degreeCount.put(node2, degreeCount.getOrDefault(node2, 0) + 1);

            // Track node with maximum degree
            if (degreeCount.get(node1) > maxDegree) {
                maxDegree = degreeCount.get(node1);
                centerNode = node1;
            }
            if (degreeCount.get(node2) > maxDegree) {
                maxDegree = degreeCount.get(node2);
                centerNode = node2;
            }
        }
        if (maxDegree != edges.length + 1) {
            return -1; // Not a valid star graph
        }

        return centerNode;
    }

    /**
     * Validation method to check if the graph is actually a star graph.
     * Useful for debugging and validation.
     *
     * @param edges Array of edges
     * @param n Total number of nodes (1 to n)
     * @return true if the graph is a valid star graph
     */
    public boolean isValidStarGraph(int[][] edges, int n) {
        if (edges == null || edges.length != n - 1) {
            return false; // Star graph must have exactly n-1 edges
        }

        Map<Integer, Integer> degreeCount = new HashMap<>();

        // Count degrees
        for (int[] edge : edges) {
            degreeCount.put(edge[0], degreeCount.getOrDefault(edge[0], 0) + 1);
            degreeCount.put(edge[1], degreeCount.getOrDefault(edge[1], 0) + 1);
        }

        // Check if exactly one node has degree n-1 and others have degree 1
        int centerNodes = 0;
        int leafNodes = 0;

        for (int degree : degreeCount.values()) {
            if (degree == n - 1) {
                centerNodes++;
            } else if (degree == 1) {
                leafNodes++;
            } else {
                return false; // Invalid degree
            }
        }

        return centerNodes == 1 && leafNodes == n - 1;
    }
}