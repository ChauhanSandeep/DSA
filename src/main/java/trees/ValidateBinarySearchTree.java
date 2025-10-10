package trees;

import java.util.*;

/**
 * Validate Binary Search Tree
 *
 * Problem Statement:
 * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 * A valid BST is defined as follows:
 * - The left subtree contains only nodes with keys less than the node's key
 * - The right subtree contains only nodes with keys greater than the node's key
 * - Both left and right subtrees must also be BSTs
 *
 * Example:
 * Input: root = [2,1,3]
 * Output: true
 * Explanation: This forms a valid BST: 1 < 2 < 3
 *
 * LeetCode Link: https://leetcode.com/problems/validate-binary-search-tree
 *
 * Follow-up Questions:
 * 1. What if duplicates are allowed? - Modify conditions to use <= or >=
 * 2. How to fix an invalid BST? - Find violations and swap nodes
 * 3. What about BST with range constraints? - Use additional min/max parameters
 */
public class ValidateBinarySearchTree {

    /**
     * Validates BST using min/max bounds approach.
     *
     * Algorithm: Top-down validation with bounds
     * - Pass min and max allowable values for each subtree
     * - Root can have any value, left must be < root, right must be > root
     * - Recursively narrow the bounds for subtrees
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth
     *
     * @param root root of binary tree
     * @return true if tree is valid BST, false otherwise
     */
    public boolean isValidBstRecursiveApproach(TreeNode root) {
        return isValidRecHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    // Helper method to validate with min/max bounds
    private boolean isValidRecHelper(TreeNode node, long minVal, long maxVal) {
        if (node == null) {
            return true;
        }

        // Check if current node violates BST property
        if (node.val <= minVal || node.val >= maxVal) {
            return false;
        }

        // Recursively validate subtrees with updated bounds
        return isValidRecHelper(node.left, minVal, node.val) &&
               isValidRecHelper(node.right, node.val, maxVal);
    }

    /**
     * Iterative inorder approach using stack.
     * More space-efficient for very deep trees. Avoids recursion stack overflow issues.
     *
     * Algorithm:
     * 1. Use a stack to perform iterative inorder traversal
     * 2. Keep track of the previous node's value
     * 3. For each node, ensure current value > previous value
     * 4. If any violation occurs, return false.
     * 5. If traversal completes without violations, return true.
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - stack space for tree height
     */
    public boolean isValidBSTIterative(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        Integer previousValue = null;

        while (current != null || !stack.isEmpty()) {
            // Go to leftmost node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // Process current node
            current = stack.pop();

            // Check BST property
            if (previousValue != null && current.val <= previousValue) {
                return false;
            }
            previousValue = current.val;

            // Move to right subtree
            current = current.right;
        }

        return true;
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