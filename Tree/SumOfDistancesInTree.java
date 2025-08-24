package Tree;

import java.util.*;

/**
 * Problem: Sum of Distances in Tree
 * 
 * There is an undirected connected tree with n nodes labeled from 0 to n - 1 and n - 1 edges.
 * You are given the integer n and the array edges where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.
 * 
 * Return an array answer of length n where answer[i] is the sum of the distances between the ith node in the tree and all other nodes.
 * 
 * Example:
 * Input: n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 * Output: [8,12,6,10,10,10]
 * Explanation: The tree is shown below.
 *       0
 *     /   \
 *    1     2
 *         /|\
 *        3 4 5
 * We can see that dist(0,1) + dist(0,2) + dist(0,3) + dist(0,4) + dist(0,5)
 * equals 1 + 1 + 2 + 2 + 2 = 8.  Hence, answer[0] = 8, and so on.
 * 
 * LeetCode: https://leetcode.com/problems/sum-of-distances-in-tree
 * 
 * Time Complexity: O(n) where n is the number of nodes
 * Space Complexity: O(n) for the graph and other data structures
 */
public class SumOfDistancesInTree {
    private int[] count;
    private int[] res;
    private List<Set<Integer>> tree;
    
    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // Initialize the tree as an adjacency list
        tree = new ArrayList<>();
        count = new int[n];
        res = new int[n];
        
        for (int i = 0; i < n; i++) {
            tree.add(new HashSet<>());
        }
        
        // Build the tree
        for (int[] edge : edges) {
            tree.get(edge[0]).add(edge[1]);
            tree.get(edge[1]).add(edge[0]);
        }
        
        // First post-order traversal to calculate count and res[0]
        postOrder(0, -1);
        // Second pre-order traversal to calculate res for all nodes
        preOrder(0, -1, n);
        
        return res;
    }
    
    // Post-order traversal to calculate count and res[0]
    private void postOrder(int node, int parent) {
        for (int child : tree.get(node)) {
            if (child == parent) continue;
            postOrder(child, node);
            count[node] += count[child];
            res[node] += res[child] + count[child];
        }
        count[node]++;
    }
    
    // Pre-order traversal to calculate res for all nodes
    private void preOrder(int node, int parent, int n) {
        for (int child : tree.get(node)) {
            if (child == parent) continue;
            res[child] = res[node] - count[child] + n - count[child];
            preOrder(child, node, n);
        }
    }
}
