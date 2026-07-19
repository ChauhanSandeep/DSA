package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Binary Tree Level Order Traversal
 *
 * Return the values of a binary tree level by level from top to bottom. Within
 * each level, values are listed from left to right.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-level-order-traversal/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | BFS | Queue | Level-size boundary
 *
 * Example:
 *   Input:  root = [3,9,20,null,null,15,7]
 *   Output: [[3], [9, 20], [15, 7]]
 *   Why:    the queue processes all nodes at one depth before moving to the next.
 *
 * Follow-ups:
 *   1. How would you return bottom-up order?
 *      Reverse the list of levels after BFS or prepend each completed level.
 *   2. How would you produce zigzag order?
 *      Reverse every other level or insert values at opposite ends.
 *   3. Can DFS produce the same grouping?
 *      Pass the depth and append each node value to result[depth].
 *
 * Related: Binary Tree Zigzag Level Order Traversal (103), Binary Tree Right Side View (199).
 */
public class BinaryTreeLevelOrderTraversal {

    public static void main(String[] args) {
        BinaryTreeLevelOrderTraversal solver = new BinaryTreeLevelOrderTraversal();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[3,9,20,null,null,15,7]", solver.levelOrder(root), "[[3], [9, 20], [15, 7]]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", solver.levelOrder(null), "[]");
    }


        /**
     * Intuition: BFS naturally visits nodes by increasing depth. Capturing the
     * queue size before a level starts creates a boundary, so all nodes removed
     * in that loop belong to the same output list.
     *
     * Algorithm:
     *   1. Return an empty result for a null root.
     *   2. Put root in the queue.
     *   3. For each level, remove exactly levelSize nodes and collect their values.
     *   4. Enqueue each removed node's left child before its right child.
     *
     * Time:  O(n) - each node is enqueued and dequeued once.
     * Space: O(w) - the queue can hold the widest level.
     *
     * @param root root of the binary tree
     * @return values grouped by depth from top to bottom
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

    // Adds each node to the list that corresponds to its depth.
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