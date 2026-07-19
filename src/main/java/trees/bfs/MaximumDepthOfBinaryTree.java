package trees.bfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;

/**
 * Problem: Maximum Depth of Binary Tree
 *
 * Given the root of a binary tree, return the number of nodes on the longest
 * path from the root down to any leaf. An empty tree has depth 0.
 *
 * Leetcode: https://leetcode.com/problems/maximum-depth-of-binary-tree/ (Easy)
 * Rating:   not available
 * Pattern:  Trees | DFS | BFS | Height from children
 *
 * Example:
 *   Input:  root = [3,9,20,null,null,15,7]
 *   Output: 3
 *   Why:    the longest root-to-leaf paths contain three nodes, such as 3 -> 20 -> 15.
 *
 * Follow-ups:
 *   1. How would you find minimum depth?
 *      Stop only at real leaves, because a missing child is not a path.
 *   2. How would you avoid recursion depth overflow?
 *      Use the existing BFS or stack-based DFS variants.
 *   3. How would you return the deepest path itself?
 *      Store parent links or return both height and path during DFS.
 *
 * Related: Minimum Depth of Binary Tree (111), Diameter of Binary Tree (543).
 */
public class MaximumDepthOfBinaryTree {

    public static void main(String[] args) {
        MaximumDepthOfBinaryTree solver = new MaximumDepthOfBinaryTree();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.printf("root=%s -> %d  expected=%d%n",
            "[3,9,20,null,null,15,7]", solver.maxDepthRecursive(root), 3);
        System.out.printf("root=%s -> %d  expected=%d%n",
            "[]", solver.maxDepthRecursive(null), 0);
    }


        /**
     * Intuition: the depth of a node is one plus the deeper of its two child
     * depths. A null child contributes zero, so postorder recursion naturally
     * builds the answer from leaves back to the root.
     *
     * Algorithm:
     *   1. Return 0 for a null node.
     *   2. Recursively compute leftDepth and rightDepth.
     *   3. Return 1 plus the larger child depth.
     *
     * Time:  O(n) - every node contributes one constant-time calculation.
     * Space: O(h) - recursion depth is the tree height.
     *
     * @param root root of the binary tree
     * @return maximum root-to-leaf depth
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