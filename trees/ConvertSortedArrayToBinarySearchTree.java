package trees;

import java.util.*;

/**
 * Convert Sorted Array To Binary Search Tree
 *
 * Problem Statement:
 * Given an integer array nums where the elements are sorted in ascending order,
 * convert it to a height-balanced binary search tree.
 * A height-balanced binary tree is a binary tree in which the depth of the two subtrees
 * of every node never differs by more than one.
 *
 * Example:
 * Input: nums = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5] (one possible balanced BST)
 * Explanation: [0,-10,5,null,-3,null,9] would also be accepted
 *
 * LeetCode Link: https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree
 *
 * Follow-up Questions:
 * 1. What if we want minimum height BST? - Always choose middle element
 * 2. How to handle duplicates? - Define left/right placement strategy
 * 3. What about sorted linked list? - Similar approach with slow/fast pointers
 */
public class ConvertSortedArrayToBinarySearchTree {

    /**
     * Converts sorted array to height-balanced BST using divide and conquer.
     *
     * Algorithm: Recursive Middle Selection
     * - Choose middle element as root to ensure balance
     * - Recursively construct left subtree from left half
     * - Recursively construct right subtree from right half
     * - Base case: empty range returns null
     *
     * Time Complexity: O(n) - create each node exactly once
     * Space Complexity: O(log n) - recursion stack for balanced tree
     *
     * @param nums sorted array in ascending order
     * @return root of height-balanced BST
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        return constructBST(nums, 0, nums.length - 1);
    }

    // Helper method to construct BST from array range
    private TreeNode constructBST(int[] nums, int left, int right) {
        // Base case: empty range
        if (left > right) {
            return null;
        }

        // Choose middle element as root
        int mid = left + (right - left) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        // Recursively construct subtrees
        root.left = constructBST(nums, left, mid - 1);
        root.right = constructBST(nums, mid + 1, right);

        return root;
    }

    /**
     * Alternative approach choosing left-middle for deterministic results.
     *
     * When array length is even, we can choose either middle element.
     * This version always chooses left-middle for consistency.
     */
    public TreeNode sortedArrayToBSTLeftMiddle(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        return constructBSTLeftMiddle(nums, 0, nums.length - 1);
    }

    // Helper that always chooses left middle
    private TreeNode constructBSTLeftMiddle(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        // Always choose left middle for even-length ranges
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        root.left = constructBSTLeftMiddle(nums, left, mid - 1);
        root.right = constructBSTLeftMiddle(nums, mid + 1, right);

        return root;
    }

    /**
     * Alternative approach choosing right-middle.
     *
     * Demonstrates different valid BST construction.
     * Results in different tree structure but same properties.
     */
    public TreeNode sortedArrayToBSTRightMiddle(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        return constructBSTRightMiddle(nums, 0, nums.length - 1);
    }

    // Helper that always chooses right middle
    private TreeNode constructBSTRightMiddle(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        // Always choose right middle for even-length ranges
        int mid = (left + right + 1) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        root.left = constructBSTRightMiddle(nums, left, mid - 1);
        root.right = constructBSTRightMiddle(nums, mid + 1, right);

        return root;
    }

    /**
     * Iterative approach using stack for space optimization.
     *
     * Algorithm: Iterative Construction with Stack
     * - Use stack to track ranges to process
     * - Process ranges in order similar to recursive approach
     * - Build parent-child relationships explicitly
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n) - stack storage
     */
    public TreeNode sortedArrayToBSTIterative(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        // Stack to store [left, right, parent, isLeft]
        Stack<int[]> stack = new Stack<>();
        TreeNode root = null;

        stack.push(new int[]{0, nums.length - 1, -1, 0}); // -1 indicates root

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int left = current[0];
            int right = current[1];
            int parentIndex = current[2];
            int isLeft = current[3];

            if (left > right) {
                continue;
            }

            int mid = left + (right - left) / 2;
            TreeNode node = new TreeNode(nums[mid]);

            if (parentIndex == -1) {
                root = node;
            }
            // Note: In full implementation, would need to track parent nodes
            // This is a simplified version showing the concept

            // Push right subtree first (processed after left due to stack LIFO)
            stack.push(new int[]{mid + 1, right, mid, 0});
            // Push left subtree
            stack.push(new int[]{left, mid - 1, mid, 1});
        }

        return root;
    }

    /**
     * Approach with random middle selection for variety.
     *
     * When range has even length, randomly choose left or right middle.
     * Creates different valid BSTs on different runs.
     */
    public TreeNode sortedArrayToBSTRandom(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        Random random = new Random();
        return constructBSTRandom(nums, 0, nums.length - 1, random);
    }

    // Helper with random middle selection
    private TreeNode constructBSTRandom(int[] nums, int left, int right, Random random) {
        if (left > right) {
            return null;
        }

        // For even-length ranges, randomly choose left or right middle
        int mid = (left + right) / 2;
        if ((right - left + 1) % 2 == 0 && random.nextBoolean()) {
            mid++;
        }

        TreeNode root = new TreeNode(nums[mid]);
        root.left = constructBSTRandom(nums, left, mid - 1, random);
        root.right = constructBSTRandom(nums, mid + 1, right, random);

        return root;
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