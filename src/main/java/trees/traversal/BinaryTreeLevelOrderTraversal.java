package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Binary Tree Level Order Traversal
 *
 * Problem Statement:
 * Given the root of a binary tree, return the level order traversal of its nodes' values.
 * (i.e., from left to right, level by level).
 *
 * Example:
 * Input: root = [3,9,20,null,null,15,7]
 * Output: [[3],[9,20],[15,7]]
 * Explanation: Level 0: [3], Level 1: [9,20], Level 2: [15,7]
 *
 * LeetCode Link: https://leetcode.com/problems/binary-tree-level-order-traversal
 *
 * Follow-up Questions:
 * 1. What if we need reverse level order? - Use Collections.reverse() or build result backwards
 * 2. What about zigzag traversal? - Alternate direction flag with Collections.reverse()
 * 3. How to handle very wide trees? - Consider iterative approach to avoid stack overflow
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class BinaryTreeLevelOrderTraversal {

    /**
     * Returns level order traversal using BFS with queue.
     *
     * Algorithm: Breadth-First Search
     * - Use queue to process nodes level by level
     * - For each level, process all nodes currently in queue
     * - Add children of current level to queue for next iteration
     *
     * Time Complexity: O(n) - visit each node exactly once
     * Space Complexity: O(w) - where w is maximum width of tree (queue size)
     *
     * @param root root of binary tree
     * @return list of lists representing level order traversal
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            // Process all nodes at current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);

                // Add children for next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            result.add(currentLevel);
        }

        return result;
    }

    /**
     * Alternative recursive DFS approach.
     *
     * Algorithm: Depth-First Search with level tracking
     * - Pass current level as parameter in recursion
     * - Add nodes to appropriate level in result list
     * - Ensure result list has enough levels before adding
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth
     */
    public List<List<Integer>> levelOrderDFS(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        dfsHelper(root, 0, result);
        return result;
    }

    // Helper method for DFS approach
    private void dfsHelper(TreeNode node, int level, List<List<Integer>> result) {
        if (node == null) {
            return;
        }

        // Ensure result has enough levels
        if (level >= result.size()) {
            result.add(new ArrayList<>());
        }

        // Add current node to its level
        result.get(level).add(node.val);

        // Recurse to children
        dfsHelper(node.left, level + 1, result);
        dfsHelper(node.right, level + 1, result);
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