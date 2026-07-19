package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Validate Binary Search Tree
 *
 * Determine whether a binary tree satisfies strict BST ordering. Every node must
 * be greater than all values allowed by its left ancestors and smaller than all
 * values allowed by its right ancestors.
 *
 * Leetcode: https://leetcode.com/problems/validate-binary-search-tree/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Recursive bounds and inorder validation
 *
 * Example:
 *   Input:  root = [2,1,3]
 *   Output: true
 *   Why:    the inorder order is [1,2,3], and every subtree respects ancestor bounds.
 *
 * Follow-ups:
 *   1. What if the tree contains Integer.MIN_VALUE or Integer.MAX_VALUE?
 *      Use long bounds, as the recursive method does.
 *   2. What if duplicates are allowed?
 *      Adjust the strict inequalities according to the duplicate-side rule.
 *   3. How would you avoid recursion?
 *      Use iterative inorder traversal and ensure values strictly increase.
 *   4. How would you return a violation path?
 *      Carry path state with bounds and stop at the first invalid node.
 *
 * Related: Recover Binary Search Tree (99), Binary Tree Inorder Traversal (94).
 */
public class ValidateBinarySearchTree {

    public static void main(String[] args) {
        ValidateBinarySearchTree solver = new ValidateBinarySearchTree();
        TreeNode valid = new TreeNode(2, new TreeNode(1), new TreeNode(3));
        TreeNode invalid = new TreeNode(5, new TreeNode(1), new TreeNode(4, new TreeNode(3), new TreeNode(6)));

        System.out.printf("root=[2,1,3] -> %b  expected=true%n", solver.isValidBstRecursiveApproach(valid));
        System.out.printf("root=[5,1,4,null,null,3,6] -> %b  expected=false%n", solver.isValidBstRecursiveApproach(invalid));
    }


            /**
     * Validates one subtree against inherited open min and max bounds.
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