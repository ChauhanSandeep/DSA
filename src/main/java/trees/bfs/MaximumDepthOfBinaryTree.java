package trees.bfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;

/**
 * Maximum Depth Of Binary Tree
 *
 * Problem Statement:
 * Given the root of a binary tree, return its maximum depth.
 * A binary tree's maximum depth is the number of nodes along the longest path
 * from the root node down to the farthest leaf node.
 *
 * Example:
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 3
 * Explanation: The longest path is 3 -> 20 -> 15 (or 7), which has depth 3
 *
 * LeetCode Link: https://leetcode.com/problems/maximum-depth-of-binary-tree
 *
 * Follow-up Questions:
 * 1. What about minimum depth? - Similar approach but careful with incomplete paths
 * 2. How to find diameter? - Maximum depth through any node (not necessarily root)
 * 3. What if tree is extremely deep? - Use iterative approach to avoid stack overflow
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MaximumDepthOfBinaryTree {

    /**
     * Finds maximum depth using recursive DFS.
     *
     * Algorithm: Post-order DFS
     * - Base case: null node has depth 0
     * - Recursively find depth of left and right subtrees
     * - Return 1 + maximum depth of subtrees
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth where h is tree height
     *
     * @param root root of binary tree
     * @return maximum depth of tree
     */
    public int maxDepthRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftDepth = maxDepthRecursive(root.left);
        int rightDepth = maxDepthRecursive(root.right);

        return 1 + Math.max(leftDepth, rightDepth);
    }

    /**
     * Iterative approach using level-order traversal (BFS).
     *
     * Algorithm: Level-by-level processing
     * - Use queue to process nodes level by level
     * - Count number of levels processed
     * - Return total level count
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(w) - where w is maximum width of tree
     */
    public int maxDepthIterativeBFS(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            depth++;

            // Process all nodes at current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return depth;
    }

    /**
     * Iterative DFS approach using stack.
     *
     * Algorithm: DFS with explicit stack
     * - Use stack to store (node, depth) pairs
     * - Track maximum depth seen so far
     * - Process children with incremented depth
     *
     * Time Complexity: O(n)
     * Space Complexity: O(h) - stack depth
     */
    public int maxDepthIterativeDFS(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, 1));
        int maxDepth = 0;

        while (!stack.isEmpty()) {
            Pair current = stack.pop();
            TreeNode node = current.node;
            int depth = current.depth;

            maxDepth = Math.max(maxDepth, depth);

            if (node.left != null) {
                stack.push(new Pair(node.left, depth + 1));
            }
            if (node.right != null) {
                stack.push(new Pair(node.right, depth + 1));
            }
        }

        return maxDepth;
    }

    // Helper class for iterative DFS
    private static class Pair {
        TreeNode node;
        int depth;

        Pair(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
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