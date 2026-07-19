package codingassessment.sidewaystree;

import utils.TreeNode;

/**
 * Problem: Binary Tree Upside Down
 *
 * Given a binary tree where every right child is either a leaf with a left sibling
 * or null, flip the tree so the original leftmost node becomes the new root. For
 * each old parent, its old right child becomes the new left child, and the old
 * parent becomes the new right child.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-upside-down/ (Medium)
 * Rating:   acceptance 65.5% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Binary tree | Recursion | Pointer rewiring
 *
 * Example:
 *   Input:  preorder with nulls = [1,2,4,null,null,5,null,null,3,null,null]
 *   Output: preorder with nulls = [4,5,null,null,2,3,null,null,1,null,null]
 *   Why:    node 4 was the leftmost node, so it becomes the root; node 5 becomes
 *           its left child and old parent 2 becomes its right child.
 *
 * Follow-ups:
 *   1. Write the same transform iteratively?
 *      Walk down the left spine while carrying the previous parent and previous right child.
 *   2. Validate the input shape before flipping?
 *      Reject any node that has a right child without a left sibling or a non-leaf right child.
 *   3. Flip the tree without mutating the original?
 *      Build copied nodes while following the same left-spine rewiring order.
 *   4. Restore the original tree after flipping?
 *      Apply the inverse pointer assignment along the new right spine.
 *
 * Related: Invert Binary Tree (226), Flatten Binary Tree to Linked List (114).
 */
public class BinaryTreeUpsideDown {

    /**
     * Intuition: trying to flip the current root first loses the real root of the
     * answer, because the new root is the leftmost node. So the recursion first
     * walks down the left spine until that base node is found, then rewires while
     * unwinding. At each old parent, the already-flipped lower part is correct; the
     * local job is to rotate this parent under its old left child.
     *
     * Algorithm:
     *   1. Return the node itself for an empty tree or a node with no left child.
     *   2. Recurse on node.leftChild to find and keep the final newRoot.
     *   3. Save the old left and right children.
     *   4. Make old right the new left, old parent the new right, then cut old links.
     *
     * Time:  O(n) - each node is visited and rewired once.
     * Space: O(h) - recursion uses one stack frame per tree level.
     *
     * @param node root of the tree to flip in place
     * @return new root after the upside-down transform
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
     * Prints the tree in preorder with null markers.
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
        TreeNode example = new TreeNode(1);
        example.leftChild = new TreeNode(2);
        example.rightChild = new TreeNode(3);
        example.leftChild.leftChild = new TreeNode(4);
        example.leftChild.rightChild = new TreeNode(5);

        TreeNode single = new TreeNode(7);

        TreeNode[] inputs = {example, single};
        String[] inputLabels = {
            "1 2 4 null null 5 null null 3 null null",
            "7 null null"
        };
        String[] expected = {
            "4 5 null null 2 3 null null 1 null null",
            "7 null null"
        };

        java.io.PrintStream originalOut = System.out;
        for (int i = 0; i < inputs.length; i++) {
            TreeNode outputRoot = flipUpsideDown(inputs[i]);
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            System.setOut(new java.io.PrintStream(buffer));
            printPreOrder(outputRoot);
            System.out.flush();
            System.setOut(originalOut);
            String output = buffer.toString().trim();
            System.out.printf("preorder=%s -> %s  expected=%s%n", inputLabels[i], output, expected[i]);
        }
    }
}