package trees.dfs;

import java.util.Arrays;

/**
 * Problem: Binary Tree Cameras
 *
 * Place the fewest cameras so every node in a binary tree is monitored. A camera
 * on one node covers its parent, itself, and its immediate children.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-cameras/ (Hard)
 * Rating:   2124
 * Pattern:  Trees | DFS | Postorder states | Greedy coverage
 *
 * Example:
 *   Input:  root = [0,0,null,0,0]
 *   Output: 1
 *   Why:    one camera on the internal child covers its parent and both children.
 *
 * Follow-ups:
 *   1. What if cameras have range k?
 *      Track distance-to-camera and distance-to-uncovered states for each node.
 *   2. What if cameras have different costs?
 *      Use tree DP states instead of a simple greedy counter.
 *   3. What if the input is a forest?
 *      Apply the same root check to each tree and sum the camera counts.
 *
 * Related: House Robber III (337), Binary Tree Maximum Path Sum (124).
 */
public class BinaryTreeCameras {

    public static void main(String[] args) {
        BinaryTreeCameras solver = new BinaryTreeCameras();
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(0);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(0);

        TreeNode single = new TreeNode(0);
        System.out.printf("root=%s -> %d  expected=%d%n",
            Arrays.toString(new int[] {0, 0, 0, 0}), solver.minCameraCover(root), 1);
        System.out.printf("root=%s -> %d  expected=%d%n",
            Arrays.toString(new int[] {0}), solver.minCameraCover(single), 1);
    }


    private int cameras = 0;

    // Node states: 0 = needs camera, 1 = has camera, 2 = covered
    private static final int NOT_COVERED = 0;
    private static final int HAS_CAMERA = 1;
    private static final int COVERED = 2;

        /**
     * Intuition: a leaf should not own a camera if its parent can cover it and
     * maybe more nodes at the same time. Postorder DFS lets children report
     * whether they still need coverage; a parent installs a camera exactly when
     * at least one child is NOT_COVERED.
     *
     * Algorithm:
     *   1. Reset cameras and run dfs from the root.
     *   2. In dfs, treat null children as already COVERED.
     *   3. If any child is NOT_COVERED, place a camera here and return HAS_CAMERA.
     *   4. If any child HAS_CAMERA, return COVERED; otherwise return NOT_COVERED.
     *   5. Add one final camera if the root itself returns NOT_COVERED.
     *
     * Time:  O(n) - every node returns one state once.
     * Space: O(h) - recursion stack height.
     *
     * @param root root of the binary tree
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

    // Returns the coverage state of node after processing both children.
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