package Tree;

import java.util.*;

/**
 * Binary Tree Paths
 * 
 * Problem Statement:
 * Given the root of a binary tree, return all root-to-leaf paths in any order.
 * A leaf is a node with no children.
 * 
 * Example:
 * Input: root = [1,2,3,null,5]
 * Output: ["1->2->5","1->3"]
 * Explanation: All root-to-leaf paths are: 1->2->5, 1->3
 * 
 * LeetCode Link: https://leetcode.com/problems/binary-tree-paths
 * 
 * Follow-up Questions:
 * 1. What if we need paths with specific sum? - Add sum tracking parameter
 * 2. What about paths between any two nodes? - Use LCA and path finding
 * 3. How to find longest path? - Track and compare path lengths
 */
public class BinaryTreePaths {

    /**
     * Finds all root-to-leaf paths using DFS with backtracking.
     * 
     * Algorithm: Depth-First Search with Path Tracking
     * - Build path string as we traverse down
     * - When leaf is reached, add path to result
     * - Backtrack by removing current node when returning
     * 
     * Time Complexity: O(n²) - in worst case (skewed tree), we have n paths each of length n
     * Space Complexity: O(n²) - to store all paths, plus O(h) for recursion stack
     * 
     * @param root root of binary tree
     * @return list of all root-to-leaf paths
     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        dfsWithString(root, "", result);
        return result;
    }

    // Helper method using string concatenation
    private void dfsWithString(TreeNode node, String path, List<String> result) {
        if (node == null) {
            return;
        }

        // Add current node to path
        path += node.val;

        // If leaf node, add path to result
        if (node.left == null && node.right == null) {
            result.add(path);
            return;
        }

        // Continue to children with arrow separator
        path += "->";
        dfsWithString(node.left, path, result);
        dfsWithString(node.right, path, result);
    }

    /**
     * Alternative approach using StringBuilder for better performance.
     * 
     * Algorithm: DFS with StringBuilder and backtracking
     * - Use StringBuilder for efficient string building
     * - Manually handle backtracking by tracking string length
     * - More memory efficient for deep trees
     * 
     * Time Complexity: O(n²) - same as above but with better constants
     * Space Complexity: O(n) for result storage + O(h) for recursion
     */
    public List<String> binaryTreePathsOptimized(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        dfsWithStringBuilder(root, new StringBuilder(), result);
        return result;
    }

    // Helper using StringBuilder with manual backtracking
    private void dfsWithStringBuilder(TreeNode node, StringBuilder path, List<String> result) {
        if (node == null) {
            return;
        }

        int originalLength = path.length();

        // Add current node to path
        if (path.length() > 0) {
            path.append("->");
        }
        path.append(node.val);

        // If leaf node, add path to result
        if (node.left == null && node.right == null) {
            result.add(path.toString());
        } else {
            // Continue to children
            dfsWithStringBuilder(node.left, path, result);
            dfsWithStringBuilder(node.right, path, result);
        }

        // Backtrack by restoring original length
        path.setLength(originalLength);
    }

    /**
     * Approach using list for path tracking.
     * 
     * Most memory efficient approach for building paths.
     * Uses list to track path and joins at the end.
     */
    public List<String> binaryTreePathsList(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        List<Integer> currentPath = new ArrayList<>();
        dfsWithList(root, currentPath, result);
        return result;
    }

    // Helper using list with backtracking
    private void dfsWithList(TreeNode node, List<Integer> path, List<String> result) {
        if (node == null) {
            return;
        }

        // Add current node to path
        path.add(node.val);

        // If leaf node, convert path to string
        if (node.left == null && node.right == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) {
                    sb.append("->");
                }
                sb.append(path.get(i));
            }
            result.add(sb.toString());
        } else {
            // Continue to children
            dfsWithList(node.left, path, result);
            dfsWithList(node.right, path, result);
        }

        // Backtrack
        path.remove(path.size() - 1);
    }

    // Definition for a binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}