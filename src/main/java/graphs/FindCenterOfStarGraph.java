package graphs;

import java.util.HashMap;
import java.util.Map;

import java.util.Arrays;
/**
 * Problem: Find Center of Star Graph
 *
 * Given the edges of an undirected star graph, return the center node. In a star,
 * exactly one node is connected to every other node, and every edge includes that
 * center.
 *
 * Leetcode: https://leetcode.com/problems/find-center-of-star-graph/ (Easy)
 * Rating:   1287 (zerotrac Elo)
 * Pattern:  Graph | Degree observation | Constant-time edge intersection
 *
 * Example:
 *   Input:  edges = [[1,2],[2,3],[4,2]]
 *   Output: 2
 *   Why:    node 2 is the only value present in every edge, so it is the hub of
 *           the star.
 *
 * Follow-ups:
 *   1. Validate that the graph really is a star?
 *      Count degrees and check for one degree n - 1 node and n - 1 leaf nodes.
 *   2. Find centers of many star components in one graph?
 *      Split into connected components and apply the degree check per component.
 *   3. What if the graph is almost a star with one bad edge?
 *      Use degree counts to identify the likely center and then report edges not touching it.
 *
 * Related: Maximal Network Rank (1615), Find the Town Judge (997).
 */
public class FindCenterOfStarGraph {


    public static void main(String[] args) {
        FindCenterOfStarGraph solver = new FindCenterOfStarGraph();
        int[][][] inputs = {
            {{1, 2}, {2, 3}, {4, 2}},
            {{1, 2}, {5, 1}, {1, 3}, {1, 4}}
        };
        int[] expected = {2, 1};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.findCenter(inputs[i]);
            System.out.printf("edges=%s  ->  %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), output, expected[i]);
        }
    }
    /**
     * Intuition: in a star graph, the center appears in every edge. Therefore the
     * first two edges are enough: the value common to both must be the center, and
     * no degree array is needed.
     *
     * Algorithm:
     *   1. Read the two endpoints of the first edge.
     *   2. Compare them with the two endpoints of the second edge.
     *   3. Return the endpoint from the first edge that appears in the second edge.
     *
     * Time:  O(1) - only the first two edges are inspected.
     * Space: O(1) - no extra data structure is used.
     *
     * @param edges edges of a valid star graph
     * @return label of the center node
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
