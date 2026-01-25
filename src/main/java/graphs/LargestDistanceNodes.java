package graphs;

import java.util.*;

/**
 * Problem Statement:
 * Given an undirected tree (connected acyclic graph) with n nodes labeled from 0 to n-1,
 * find the largest distance between any two nodes in the tree.
 * 
 * The tree is given as an adjacency list where A[i] contains all nodes connected to node i.
 * The distance between two nodes is the number of edges in the path connecting them.
 *
 * Example:
 * Input: A = [[1,2], [0,3], [0], [1,4], [3]]
 * Output: 3
 * Explanation:
 * Tree structure:
 *       0
 *      / \
 *     1   2
 *    /
 *   3
 *  /
 * 4
 * Longest path: 2 - 0 - 1 - 3 - 4 (distance = 4 edges means diameter = 4)
 * OR nodes 4 to 2 with distance 3 edges
 *
 * InterviewBit link: https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - Can you find the actual path (sequence of nodes) forming the diameter?
 *    → Track parent pointers during second DFS and backtrack to reconstruct path.
 *  - What if the graph has cycles (not a tree)?
 *    → Problem becomes finding longest simple path, which is NP-hard; need different approach.
 *  - How would you handle weighted edges?
 *    → Same algorithm works, but track weighted distances instead of edge counts.
 *  - Can you find the diameter if tree is dynamically changing (adding/removing nodes)?
 *    → Would need more complex data structures; diameter might change significantly with single edge addition.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 543 (Diameter of Binary Tree): https://leetcode.com/problems/diameter-of-binary-tree/
 *  - LeetCode 1245 (Tree Diameter): https://leetcode.com/problems/tree-diameter/
 *  - LeetCode 310 (Minimum Height Trees): https://leetcode.com/problems/minimum-height-trees/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class LargestDistanceNodes {

    /**
     * Main method: Finds tree diameter using Two-Pass DFS (Optimal).
     * Step-by-step:
     *  1. First DFS: Start from any arbitrary node (node 0)
     *     - Find the farthest node from starting node
     *     - This farthest node is guaranteed to be one endpoint of the diameter
     *  2. Second DFS: Start from the farthest node found in step 1
     *     - Find the farthest node from this endpoint
     *     - The distance to this second farthest node is the diameter
     *  3. Return the diameter (maximum distance)
     *
     * Key Insight:
     * Property: In a tree, if we start DFS from any node and find the farthest node,
     * that farthest node is guaranteed to be one endpoint of the diameter path.
     * Why? Because the diameter is the longest path, and any DFS from any node will
     * eventually reach one of its endpoints. Running second DFS from that endpoint
     * finds the other endpoint and gives us the actual diameter.
     *
     * Algorithm: Two-Pass DFS for Tree Diameter.
     * Time Complexity: O(n), where n is number of nodes. Two complete DFS traversals.
     * Space Complexity: O(n) for adjacency list and recursion stack.
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
