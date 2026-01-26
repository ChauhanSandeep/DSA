package trees.dfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;

/**
 * Path Sum II
 *
 * Problem Statement:
 * Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths
 * where the sum of the node values in the path equals targetSum.
 * Each path should be returned as a list of the node values, not node references.
 * A root-to-leaf path is a path starting from the root and ending at any leaf node.
 * A leaf is a node with no children.
 *
 * Example:
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * Output: [[5,4,11,2],[5,8,4,5]]
 * Explanation: Two paths sum to 22: 5->4->11->2 and 5->8->4->5
 *
 * LeetCode Link: https://leetcode.com/problems/path-sum-ii
 *
 * Follow-up Questions:
 * 1. What if we need count of paths only? - Same traversal but increment counter
 * 2. What about paths between any two nodes? - DFS from each node or path decomposition
 * 3. How to handle negative numbers? - Same algorithm works, no optimization possible
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PathSum2 {

    /**
     * Finds all root-to-leaf paths with given target sum using DFS with backtracking.
     *
     * Algorithm: DFS with Path Tracking and Backtracking
     * - Maintain current path as we traverse down
     * - Track remaining sum needed
     * - When leaf is reached with sum = 0, add path to result
     * - Backtrack by removing current node when returning
     *
     * Time Complexity: O(n²) - in worst case copy n paths each of length n
     * Space Complexity: O(n²) - to store all valid paths + O(h) for recursion
     *
     * @param root root of binary tree
     * @param targetSum target sum for paths
     * @return list of all valid paths
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        dfsWithBacktracking(root, targetSum, currentPath, result);
        return result;
    }

    // Helper method for DFS with backtracking
    private void dfsWithBacktracking(TreeNode node, int remainingSum, List<Integer> currentPath,
        List<List<Integer>> result) {
        if (node == null) {
            return;
        }

        // Add current node to path
        currentPath.add(node.val);
        remainingSum -= node.val;

        // If leaf node and sum matches, add path to result
        if (node.left == null && node.right == null && remainingSum == 0) {
            result.add(new ArrayList<>(currentPath)); // Create copy
        } else {
            // Continue to children
            dfsWithBacktracking(node.left, remainingSum, currentPath, result);
            dfsWithBacktracking(node.right, remainingSum, currentPath, result);
        }

        // Backtrack: remove current node
        currentPath.remove(currentPath.size() - 1);
    }

    /**
     * Iterative approach using stack.
     *
     * Algorithm: DFS with Stack
     * - Use a stack to simulate recursion
     * - Each stack entry holds current node, remaining sum, and path so far
     * - Process nodes until stack is empty
     * - When leaf is reached with sum = 0, add path to result
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(n²)
     */
    public List<List<Integer>> pathSumIterative(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Stack<PathState> stack = new Stack<>();
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(root.val);
        stack.push(new PathState(root, targetSum - root.val, initialPath));

        while (!stack.isEmpty()) {
            PathState current = stack.pop();
            TreeNode node = current.node;
            int remainingSum = current.remainingSum;
            List<Integer> path = current.path;

            // If leaf and sum matches
            if (node.left == null && node.right == null && remainingSum == 0) {
                result.add(new ArrayList<>(path));
                continue;
            }

            // Add children to stack
            if (node.left != null) {
                List<Integer> leftPath = new ArrayList<>(path);
                leftPath.add(node.left.val);
                stack.push(new PathState(node.left, remainingSum - node.left.val, leftPath));
            }

            if (node.right != null) {
                List<Integer> rightPath = new ArrayList<>(path);
                rightPath.add(node.right.val);
                stack.push(new PathState(node.right, remainingSum - node.right.val, rightPath));
            }
        }

        return result;
    }

    // Helper class for iterative approach
    private static class PathState {
        TreeNode node;
        int remainingSum;
        List<Integer> path;

        PathState(TreeNode node, int remainingSum, List<Integer> path) {
            this.node = node;
            this.remainingSum = remainingSum;
            this.path = path;
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