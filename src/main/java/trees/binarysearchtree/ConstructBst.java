package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;

/**
 * Given preorder and inorder traversal of a binary tree, construct the binary tree.
 * Preorder is where the root node is visited first, followed by the left subtree and then the right subtree.
 * Inorder is where the left subtree is visited first, followed by the root node and then the right subtree.
 *
 * Example:
 * Input:   preorder = [1,2,4,5,3,6],
 *          inorder =  [4,2,5,1,3,6]
 * Output: [1,2,3,4,5,6]
 *          1
 *         / \
 *        2   3
 *       / \   \
 *      4   5   6
 *
 * LeetCode Link:
 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 */
public class ConstructBst {

    /**
     * Main method to test the tree construction.
     */
    public static void main(String[] args) {
        int[] preorder = {1,2,4,5,3,6};
        int[] inorder = {4,2,5,1,3,6};

        // Build the binary tree using the given preorder and inorder traversals
        ConstructBst treeBuilder = new ConstructBst();
        TreeNode root = treeBuilder.buildTree(preorder, inorder);

        // Print the resulting tree (optional: toString method of TreeNode must be implemented to display tree)
        System.out.println(root);
    }

    /**
     * This method constructs the binary tree using preorder and inorder traversals.
     *
     * Intuition:
     * - In preorder traversal, the first element is the root of the tree.
     * - In inorder traversal, the elements to the left of the root represent the left subtree,
     *   and the elements to the right represent the right subtree.
     * - The goal is to recursively build the tree using this information.
     *
     * Algorithm:
     * 1. Identify the root node of the current subtree from the first element of the preorder array segment.
     * 2. Locate the root node's index in the inorder array segment to determine the boundaries of the left and right subtrees.
     * 3. Recursively construct the left subtree using the elements to the left of the root in the inorder array and
     *    the corresponding elements in the preorder array.
     * 4. Recursively construct the right subtree using the elements to the right of the root in the inorder array
     *    and the corresponding elements in the preorder array.
     * 5. Attach the constructed left and right subtrees to the root node and repeat the process until all nodes are placed.
     *
     * Time Complexity:
     * - The time complexity is O(n) where n is the number of nodes in the tree because each node is processed once.
     * Space Complexity:
     * - The space complexity is O(n) due to the recursive call stack and the space required to store the tree.
     *
     *
     * @param preorder  The preorder traversal array.
     * @param inorder   The inorder traversal array.
     * @return          The root of the constructed binary tree.
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) {
            return null; // Base case: if input arrays are empty, return null
        }
        // Call the helper method to build the tree
        return buildTree(preorder, inorder, 0, inorder.length - 1, 0);
    }

    /**
     * Helper method to recursively construct the binary tree.
     *
     * @param preorder   The preorder traversal array.
     * @param inorder    The inorder traversal array.
     * @param inorderLeftIndex     The left index of the inorder array for the current subtree.
     * @param inorderRightIndex    The right index of the inorder array for the current subtree.
     * @param preorderStartIndex   The current index in the preorder array.
     * @return           The root of the current subtree.
     */
    private TreeNode buildTree(int[] preorder, int[] inorder, int inorderLeftIndex, int inorderRightIndex, int preorderStartIndex) {
        // Base case: if the current segment is invalid, return null
        if (inorderLeftIndex > inorderRightIndex || preorderStartIndex >= preorder.length) {
            return null;
        }

        // Create the current node (root) using the current element from preorder
        TreeNode rootNode = new TreeNode(preorder[preorderStartIndex]);

        // Find the index of the current root in inorder traversal
        int rootIndexInInorder = findIndex(inorder, preorder[preorderStartIndex]);

        // Recursively build the left and right subtrees
        // Left subtree will be constructed using elements before the root in inorder
        TreeNode leftSubtree = buildTree(preorder, inorder, inorderLeftIndex, rootIndexInInorder - 1, preorderStartIndex + 1);

        // Right subtree will be constructed using elements after the root in inorder
        int nextPreorderStartIndex = preorderStartIndex + 1 + (rootIndexInInorder - inorderLeftIndex); // because we have already used one element for the left subtree
        TreeNode rightSubtree = buildTree(preorder, inorder, rootIndexInInorder + 1, inorderRightIndex, nextPreorderStartIndex);

        // Attach the left and right subtrees to the current root
        rootNode.left = leftSubtree;
        rootNode.right = rightSubtree;

        return rootNode;
    }

    /**
     * Finds the index of the target element in the given array.
     *
     * @param arr   The array to search in.
     * @param target The element to find in the array.
     * @return      The index of the target element, or -1 if not found.
     */
    private int findIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if the target is not found
    }
}
