package Tree;

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
    public boolean isValidBST(TreeNode root) {
        return validateWithBounds(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    // Helper method to validate with min/max bounds
    private boolean validateWithBounds(TreeNode node, long minVal, long maxVal) {
        if (node == null) {
            return true;
        }

        // Check if current node violates BST property
        if (node.val <= minVal || node.val >= maxVal) {
            return false;
        }

        // Recursively validate subtrees with updated bounds
        return validateWithBounds(node.left, minVal, node.val) &&
               validateWithBounds(node.right, node.val, maxVal);
    }

    /**
     * Alternative approach using inorder traversal.
     * 
     * Algorithm: Inorder Traversal Validation
     * - Inorder traversal of BST should give sorted sequence
     * - Track previous value and ensure current >= previous
     * - If any violation found, return false
     * 
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth
     */
    private Integer previousValue = null;

    public boolean isValidBSTInorder(TreeNode root) {
        previousValue = null;
        return inorderValidate(root);
    }

    // Helper for inorder validation
    private boolean inorderValidate(TreeNode node) {
        if (node == null) {
            return true;
        }

        // Validate left subtree
        if (!inorderValidate(node.left)) {
            return false;
        }

        // Check current node
        if (previousValue != null && node.val <= previousValue) {
            return false;
        }
        previousValue = node.val;

        // Validate right subtree
        return inorderValidate(node.right);
    }

    /**
     * Iterative inorder approach using stack.
     * 
     * More space-efficient for very deep trees.
     * Avoids recursion stack overflow issues.
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

    /**
     * Approach using custom validation with node info.
     * 
     * Returns validation info including min/max values in subtree.
     * Useful when you need subtree statistics.
     */
    static class ValidationInfo {
        boolean isValid;
        int minVal;
        int maxVal;

        ValidationInfo(boolean isValid, int minVal, int maxVal) {
            this.isValid = isValid;
            this.minVal = minVal;
            this.maxVal = maxVal;
        }
    }

    public boolean isValidBSTWithInfo(TreeNode root) {
        return validateWithInfo(root).isValid;
    }

    // Helper that returns validation info
    private ValidationInfo validateWithInfo(TreeNode node) {
        if (node == null) {
            return new ValidationInfo(true, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }

        ValidationInfo leftInfo = validateWithInfo(node.left);
        ValidationInfo rightInfo = validateWithInfo(node.right);

        // Check if current subtree is valid BST
        boolean isValid = leftInfo.isValid && rightInfo.isValid &&
                         (node.left == null || leftInfo.maxVal < node.val) &&
                         (node.right == null || rightInfo.minVal > node.val);

        // Calculate min/max for current subtree
        int minVal = (node.left == null) ? node.val : leftInfo.minVal;
        int maxVal = (node.right == null) ? node.val : rightInfo.maxVal;

        return new ValidationInfo(isValid, minVal, maxVal);
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