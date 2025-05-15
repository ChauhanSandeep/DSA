package one;

import utils.TreeNode;

/**
 * LeetCode: https://leetcode.com/problems/binary-tree-upside-down/
 *
 * Given a binary tree where each node has at most one left child and one right child,
 * flip the tree upside down so that the leftmost node becomes the new root.
 *
 * Transformation:
 * - The original left child becomes the new root.
 * - The original right child becomes the new left child.
 * - The original root becomes the new right child.
 *
 * Algorithm:
 * - Recursively traverse to the leftmost node.
 * - Reassign pointers to flip the tree.
 *
 * Time Complexity: O(N) → Each node is visited once.
 * Space Complexity: O(H) → Recursive stack, where H is the tree height.
 */
public class TreeFlipTest {

    /**
     * Flips the given binary tree upside down.
     * @param root The root of the tree.
     * @return The new root after flipping.
     */
    public static TreeNode flipUpsideDown(TreeNode root) {
        if (root == null || root.left == null) {
            return root; // Base case: return if empty tree or single-node tree
        }

        // Recursively process the left subtree
        TreeNode newRoot = flipUpsideDown(root.left);

        // Rearranging pointers to flip the tree
        root.left.left = root.right;  // Make right child the new left child
        root.left.right = root;       // Make parent the new right child
        root.left = null;             // Remove original left and right references
        root.right = null;

        return newRoot; // Return new root (leftmost node)
    }

    /**
     * Prints the tree in Preorder traversal.
     * @param node The root node of the tree.
     */
    public static void printPreOrder(TreeNode node) {
        if (node == null) {
            System.out.print("null ");
            return;
        }
        System.out.print(node.data + " ");
        printPreOrder(node.left);
        printPreOrder(node.right);
    }

    public static void main(String[] args) {
        // Test Case 1: Example from the problem
        System.out.println("\nTest Case 1: Problem Example");
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.left.left = new TreeNode(5);
        root1.left.left.right = new TreeNode(6);

        System.out.println("Original Tree (Preorder):");
        printPreOrder(root1);
        TreeNode flipped1 = flipUpsideDown(root1);
        System.out.println("\nFlipped Tree (Preorder):");
        printPreOrder(flipped1);
    }
}
