package trees;

import java.util.*;

/**
 * Kth Smallest Element In A BST
 *
 * Problem Statement:
 * Given the root of a binary search tree, and an integer k, return the kth smallest value (1-indexed) of all the values of the nodes in the tree.
 *
 * Example:
 * Input: root = [3,1,4,null,2], k = 1
 * Output: 1
 * Explanation: The inorder traversal gives [1,2,3,4], so 1st smallest is 1
 *
 * LeetCode Link: https://leetcode.com/problems/kth-smallest-element-in-a-bst
 *
 * Follow-up Questions:
 * 1. What if BST is modified frequently? - Use augmented BST with subtree sizes
 * 2. What about kth largest? - Reverse inorder (right-root-left) or (n-k+1)th smallest
 * 3. How to handle very large k? - Early termination when k is reached
 */
public class KthSmallestElementInABst {

    /**
     * Finds kth smallest element using iterative inorder traversal.
     *
     * Algorithm: Iterative Inorder Traversal with Early Termination
     * - Use stack to simulate inorder traversal
     * - Count nodes as we visit them
     * - Return when we reach kth node
     *
     * Time Complexity: O(h + k) - where h is tree height, k is target position
     * Space Complexity: O(h) - stack space for tree height
     *
     * @param root root of binary search tree
     * @param k target position (1-indexed)
     * @return kth smallest element value
     */
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        int count = 0;

        while (current != null || !stack.isEmpty()) {
            // Go to leftmost node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // Process current node
            current = stack.pop();
            count++;

            // If we've reached kth node, return its value
            if (count == k) {
                return current.val;
            }

            // Move to right subtree
            current = current.right;
        }

        return -1; // Should never reach here for valid input
    }

    /**
     * Recursive approach using inorder traversal.
     *
     * Algorithm: Recursive Inorder with Counter
     * - Use instance variable to track count
     * - Perform inorder traversal recursively
     * - Return result when count reaches k
     *
     * Time Complexity: O(k) in best case, O(n) in worst case
     * Space Complexity: O(h) - recursion stack depth
     */
    private int count = 0;
    private int result = 0;

    public int kthSmallestRecursive(TreeNode root, int k) {
        count = 0;
        result = 0;
        inorderTraversal(root, k);
        return result;
    }

    // Helper for recursive inorder traversal
    private void inorderTraversal(TreeNode node, int k) {
        if (node == null || count >= k) {
            return;
        }

        // Traverse left subtree
        inorderTraversal(node.left, k);

        // Process current node
        if (count < k) {
            count++;
            if (count == k) {
                result = node.val;
                return;
            }
        }

        // Traverse right subtree
        inorderTraversal(node.right, k);
    }

    /**
     * Follow-up: If BST is modified frequently, use augmented BST.
     *
     * Add size field to each node indicating subtree size.
     * This allows O(h) lookup for kth element.
     */
    static class AugmentedTreeNode {
        int val;
        int size; // Size of subtree rooted at this node
        AugmentedTreeNode left;
        AugmentedTreeNode right;

        AugmentedTreeNode(int val) {
            this.val = val;
            this.size = 1;
        }
    }

    // Find kth smallest in augmented BST
    public int kthSmallestAugmented(AugmentedTreeNode root, int k) {
        int leftSize = (root.left != null) ? root.left.size : 0;

        if (k <= leftSize) {
            return kthSmallestAugmented(root.left, k);
        } else if (k == leftSize + 1) {
            return root.val;
        } else {
            return kthSmallestAugmented(root.right, k - leftSize - 1);
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