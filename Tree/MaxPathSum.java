package Tree;

/**
 * Given a binary tree, find the maximum path sum from one node to another node in the tree.
 * A path can start and end at any node, not necessarily at the root.
 *
 * Intuition:
 * - A path sum is the sum of values along the path from one node to another node in the tree.
 * - The path may involve any node in the tree, and it can pass through the root or not.
 * - The challenge is to find the path that yields the maximum sum.
 *
 * Algorithm:
 * - Traverse the tree in a post-order manner.
 * - At each node, calculate the maximum path sum starting from the node (i.e., node's value + maximum of its left or right subtree).
 * - For each node, we also consider the path that goes through both children of the node and the node itself (i.e., left + node + right).
 * - Update the result to keep track of the maximum path sum found.
 *
 * Time Complexity: O(N), where N is the number of nodes in the tree (each node is visited once).
 * Space Complexity: O(H), where H is the height of the tree due to recursion stack.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/binary-tree-maximum-path-sum/
 */

public class MaxPathSum {

    // This will hold the maximum path sum found during the traversal.
    private int maxSum = Integer.MIN_VALUE;

    public static void main(String[] args) {
        // Constructing a test tree
        TreeNode root = new TreeNode(-100);
        root.left = new TreeNode(-200);
        root.left.left = new TreeNode(-300);
        root.left.right = new TreeNode(-400);

        // Calling the method to find the max path sum
        int result = new MaxPathSum().maxPathSum(root);

        // Printing the result
        System.out.println("Maximum Path Sum: " + result);
    }

    /**
     * This method finds the maximum path sum in the tree.
     * It initializes the recursion and returns the final result.
     *
     * @param root The root node of the tree.
     * @return The maximum path sum found in the tree.
     */
    public int maxPathSum(TreeNode root) {
        // If the tree is empty, return 0 as no path exists
        if (root == null) return 0;

        // Call the helper function to calculate max path sum from root
        findMaxPathSum(root);

        // Return the final result (maximum path sum found)
        return maxSum;
    }

    /**
     * This helper method recursively computes the maximum path sum for each node.
     * It returns the maximum path sum that can be obtained starting from the current node.
     *
     * @param node The current node in the tree.
     * @return The maximum path sum starting from the current node.
     */
    private int findMaxPathSum(TreeNode node) {
        // If the current node is null, return 0 (no contribution to the path sum)
        if (node == null) return 0;

        // Recursively calculate the maximum path sum for left and right subtrees
        int leftMax = Math.max(0, findMaxPathSum(node.left));  // Only consider positive contributions
        int rightMax = Math.max(0, findMaxPathSum(node.right));  // Only consider positive contributions

        // Calculate the path sum that passes through the current node (node + left + right)
        int currentMaxPath = node.val + leftMax + rightMax;

        // Update the overall maximum path sum if the current path sum is greater
        maxSum = Math.max(maxSum, currentMaxPath);

        // Return the maximum path sum starting from this node, either going left or right
        return node.val + Math.max(leftMax, rightMax);
    }

    // TreeNode class to represent a binary tree node
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
}
