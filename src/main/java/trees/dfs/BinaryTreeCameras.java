package trees.dfs;

/**
 * Binary Tree Cameras
 *
 * Problem Statement:
 * You are given the root of a binary tree. We install cameras on the tree nodes where each camera at a node can
 * monitor its parent, itself, and its immediate children.
 * Return the minimum number of cameras needed to monitor all nodes of the tree.
 *
 * Example:
 * Input: root = [0,0,null,0,0]
                0
               /
              0
            /  \
           0    0
 * Output: 1
 * Explanation: One camera at the root's left child can monitor all nodes.
 *
 *
 * LeetCode Link: https://leetcode.com/problems/binary-tree-cameras
 *
 * Follow-up Questions:
 * 1. What if cameras had different ranges? - Use DP with different states for each range
 * 2. What if we want to minimize cost instead of count? - Modify state values to track costs
 * 3. What about forest of trees? - Apply same logic to each tree and sum results
 * LeetCode Contest Rating: 2124
 */
public class BinaryTreeCameras {

    private int cameras = 0;

    // Node states: 0 = needs camera, 1 = has camera, 2 = covered
    private static final int NOT_COVERED = 0;
    private static final int HAS_CAMERA = 1;
    private static final int COVERED = 2;

    /**
     * Finds minimum number of cameras needed to monitor all nodes.
     *
     * Algorithm: Post-order DFS with state tracking
     * - Traverse bottom-up to make optimal decisions
     * - Each node can be in one of three states: needs camera, has camera, or covered
     * - Place cameras greedily when a node needs coverage and parent can't provide it
     *
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(h) - recursion stack depth where h is tree height
     *
     * @param root root of binary tree
     * @return minimum number of cameras needed
     */
    public int minCameraCover(TreeNode root) {
        cameras = 0;

        // If root is not covered, we need a camera at root
        if (dfs(root) == NOT_COVERED) {
            cameras++;
        }

        return cameras;
    }

    // Returns the state of current node after processing
    private int dfs(TreeNode node) {
        if (node == null) {
            return COVERED; // null nodes are considered covered
        }

        int leftState = dfs(node.left);
        int rightState = dfs(node.right);

        // If any child needs camera, current node must have camera
        if (leftState == NOT_COVERED || rightState == NOT_COVERED) {
            cameras++;
            return HAS_CAMERA;
        }

        // If any child has camera, current node is covered
        if (leftState == HAS_CAMERA || rightState == HAS_CAMERA) {
            return COVERED;
        }

        // Both children are covered but don't have cameras
        // Current node needs camera (will be handled by parent)
        return NOT_COVERED;
    }

    // Definition for a binary tree node
    private static class TreeNode {
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