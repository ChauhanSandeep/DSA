package codingassessment.sidewaystree;

import utils.TreeNode;

/**
 * ✅ Problem: Binary Tree Upside Down
 *
 * 🔗 LeetCode: https://leetcode.com/problems/binary-tree-upside-down/
 *
 * Given a binary tree where every node has either 0 or 2 children and all right children are leaves,
 * flip it upside down so that the original leftmost node becomes the new root.
 *
 * 📈 Example:
 * for example, turn these:
 *
 *        1                1
 *       / \              / \
 *      2   3            2   3
 *     /
 *    4
 *   / \
 *  5   6
 *
 * into these:
 *
 *        1               1
 *       /               /
 *      2---3           2---3
 *     /
 *    4
 *   /
 *  5---6
 *
 * where 5 is the new root node for the left tree, and 2 for the right tree.
 * oriented correctly:
 *
 *     5                  2
 *    / \                / \
 *   6   4              3   1
 *        \
 *         2
 *        / \
 *       3   1
 *
 * 🔁 Transformation Rules:
 * - Original left child becomes new parent
 * - Original right child becomes new left child
 * - Original parent becomes new right child
 *
 */
public class BinaryTreeUpsideDown {

    /**
     * ✅ Recursively flips the binary tree upside down
     *
     * Time Complexity: O(N) – each node visited once
     * Space Complexity: O(H) – height of the tree (due to recursion stack)
     *
     * ⚠️ Edge Cases:
     * - Tree is empty (null)
     * - Tree has only one node
     * - Tree violates assumption (has nodes with only right child): invalid input
     *
     * @param node Root node of the binary tree
     * @return New root after the transformation
     */
    public static TreeNode flipUpsideDown(TreeNode node) {
        if (node == null || node.leftChild == null) {
            return node; // Base case: empty tree or no left child
        }

        // Recurse down to leftmost child
        TreeNode newRoot = flipUpsideDown(node.leftChild);

        // Rearrange current node and its children
        TreeNode left = node.leftChild;
        TreeNode right = node.rightChild;

        left.leftChild = right; // Right child becomes new left
        left.rightChild = node; // Current root becomes right child

        // Cut old links
        node.leftChild = null;
        node.rightChild = null;

        return newRoot;
    }

    /**
     * 📤 Utility to print the tree in Preorder format
     *
     * @param node The root of the subtree to print
     */
    public static void printPreOrder(TreeNode node) {
        if (node == null) {
            System.out.print("null ");
            return;
        }
        System.out.print(node.value + " ");
        printPreOrder(node.leftChild);
        printPreOrder(node.rightChild);
    }

    public static void main(String[] args) {
        System.out.println("✅ Test Case: Problem Example");
        TreeNode root = new TreeNode(1);
        root.leftChild = new TreeNode(2);
        root.rightChild = new TreeNode(3);
        root.leftChild.leftChild = new TreeNode(4);
        root.leftChild.leftChild.leftChild = new TreeNode(5);
        root.leftChild.leftChild.rightChild = new TreeNode(6);

        System.out.println("🔹 Original Tree (Preorder):");
        printPreOrder(root);

        TreeNode flipped = flipUpsideDown(root);
        System.out.println("\n🔄 Flipped Tree (Preorder):");
        printPreOrder(flipped);
    }
}