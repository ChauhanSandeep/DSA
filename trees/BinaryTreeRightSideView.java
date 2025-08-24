package trees;

import java.util.*;

/**
 * Binary Tree Right Side View
 *
 * Problem Statement:
 * Given the root of a binary tree, imagine yourself standing on the right side of it,
 * return the values of the nodes you can see ordered from top to bottom.
 *
 * Example:
 * Input: root = [1,2,3,null,5,null,4]
 * Output: [1,3,4]
 * Explanation: From right side view: 1 (root), 3 (rightmost at level 1), 4 (rightmost at level 2)
 *
 * LeetCode Link: https://leetcode.com/problems/binary-tree-right-side-view
 *
 * Follow-up Questions:
 * 1. What about left side view? - Same logic but track leftmost nodes
 * 2. How to get both left and right views? - Track both first and last nodes per level
 * 3. What if tree is very wide? - BFS might use too much memory, prefer DFS
 */
public class BinaryTreeRightSideView {

    /**
     * Returns right side view using BFS (level order traversal).
     *
     * Algorithm: Breadth-First Search
     * - Process nodes level by level using queue
     * - For each level, the last node processed is the rightmost
     * - Add rightmost node of each level to result
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(w) - where w is maximum width of tree
     *
     * @param root root of binary tree
     * @return list of rightmost nodes at each level
     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            TreeNode rightmostNode = null;

            // Process all nodes at current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                rightmostNode = node; // Last node processed is rightmost

                // Add children for next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            // Add rightmost node of current level
            result.add(rightmostNode.val);
        }

        return result;
    }

    /**
     * Alternative DFS approach - more space efficient for deep trees.
     *
     * Algorithm: Depth-First Search (Right-Root-Left)
     * - Traverse right subtree first, then left
     * - Track current level and add first node seen at each level
     * - Since we go right first, first node at each level is rightmost
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth
     */
    public List<Integer> rightSideViewDFS(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        dfsRightFirst(root, 0, result);
        return result;
    }

    // Helper for DFS approach - traverse right first
    private void dfsRightFirst(TreeNode node, int level, List<Integer> result) {
        if (node == null) {
            return;
        }

        // If first time visiting this level, add node (it's rightmost)
        if (level == result.size()) {
            result.add(node.val);
        }

        // Traverse right subtree first, then left
        dfsRightFirst(node.right, level + 1, result);
        dfsRightFirst(node.left, level + 1, result);
    }

    /**
     * Alternative BFS with cleaner rightmost tracking.
     *
     * Uses the fact that rightmost node is at index (levelSize - 1)
     * in each level iteration.
     */
    public List<Integer> rightSideViewBFSOptimized(TreeNode root) {
        List<Integer> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                // Add to result only if it's the rightmost node (last in level)
                if (i == levelSize - 1) {
                    result.add(node.val);
                }

                // Add children for next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return result;
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