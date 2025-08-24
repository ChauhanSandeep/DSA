package codingassessment.two;

/**
 * Given a binary tree where all the right nodes are either empty or leaf nodes, flip it upside down.
 * The transformed tree should have all left leaf nodes.
 *
 * Constraints:
 * - If a node has a right child, it must also have a left child.
 *
 * Example:
 * Original Tree:         Transformed Tree:
 *       1                    5
 *      / \                  / \
 *     2   3                6   4
 *    /                     \
 *   4                       2
 *  / \                     / \
 * 5   6                   3   1
 *
 * Approach:
 * - We traverse the tree from top to bottom and reassign pointers iteratively or recursively.
 * - The leftmost node becomes the new root.
 * - Each left child becomes a new parent, its right child becomes its new left child,
 *   and the original parent becomes its new right child.
 *
 * Time Complexity: O(N) (Each node is visited once)
 * Space Complexity: O(1) for iterative, O(H) for recursive (stack depth)
 *
 * LeetCode Problem Link (if applicable): https://leetcode.com/problems/binary-tree-upside-down/
 */
public class SidewaysTree {

    /**
     * Iterative Approach: Flips the tree upside down.
     * @param root Root of the original binary tree.
     * @return New root after flipping.
     */
    public TreeNode flipTreeIterative(TreeNode root) {
        if (root == null || root.left == null) {
            return root; // Edge case: Empty tree or single-node tree
        }

        TreeNode current = root;
        TreeNode newRoot = null;
        TreeNode rightSibling = null;
        TreeNode parent = null;

        while (current != null) {
            TreeNode next = current.left; // Store left child before modifying it

            // Reassign pointers
            current.left = rightSibling;
            rightSibling = current.right;
            current.right = parent;

            // Move to the next level
            parent = current;
            current = next;
        }

        return parent; // The new root
    }

    /**
     * Recursive Approach: Flips the tree upside down.
     * @param root Root of the original binary tree.
     * @return New root after flipping.
     */
    public TreeNode flipTreeRecursive(TreeNode root) {
        if (root == null || root.left == null) {
            return root; // Base case: Empty tree or leftmost node
        }

        // Recursively get the new root
        TreeNode newRoot = flipTreeRecursive(root.left);

        // Adjust pointers
        root.left.left = root.right;  // Make right child the new left child
        root.left.right = root;       // Make current root the new right child

        root.left = null;
        root.right = null; // Prevent cycles

        return newRoot; // Return the new root
    }

    /**
     * Definition for a binary tree node.
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
}
