package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Convert Sorted Array to Binary Search Tree
 *
 * Given a sorted integer array, build a height-balanced BST containing the same
 * values. Picking the middle value as each subtree root keeps left and right sizes
 * as even as possible.
 *
 * Leetcode: https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/ (Easy)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Divide and conquer midpoint construction
 *
 * Example:
 *   Input:  nums = [-10,-3,0,5,9]
 *   Output: root = 0 with left subtree [-10,-3] and right subtree [5,9]
 *   Why:    choosing the middle value as root splits the sorted array into balanced halves.
 *
 * Follow-ups:
 *   1. What if the input is a sorted linked list?
 *      Use slow-fast pointers per subtree or simulate inorder construction.
 *   2. Can you return every height-balanced BST?
 *      Try every near-middle root recursively; the count grows combinatorially.
 *   3. How would you build an AVL tree with heights?
 *      Store height or balance factor while returning from recursion.
 *   4. How would you avoid recursion?
 *      Use an explicit stack of array ranges and parent attachment info.
 *
 * Related: Convert Sorted List to Binary Search Tree (109).
 */
public class ConvertSortedArrayToBinarySearchTree {

    public static void main(String[] args) {
        ConvertSortedArrayToBinarySearchTree solver = new ConvertSortedArrayToBinarySearchTree();
        int[][] inputs = { {-10, -3, 0, 5, 9}, {} };
        String[] expected = { "root=0", "root=null" };

        for (int i = 0; i < inputs.length; i++) {
            TreeNode root = solver.sortedArrayToBST(inputs[i]);
            String output = root == null ? "root=null" : "root=" + root.val;
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

            /**
     * Builds a balanced BST from nums[left..right] by choosing the middle value.
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        return buildBST(nums, 0, nums.length - 1);
    }

    // Helper method to recursively build BST from array range
    private TreeNode buildBST(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        int mid = left + (right - left) / 2;
        TreeNode root = new TreeNode(nums[mid]);

        root.left = buildBST(nums, left, mid - 1);
        root.right = buildBST(nums, mid + 1, right);

        return root;
    }

    /**
     * Iterative approach using explicit stack to simulate recursion.
     * Uses stack to store array ranges and their corresponding parent nodes.
     *
     * Algorithm:
     * 1. Create stack storing tuples of (left, right, parent, isLeft)
     * 2. Process each range by finding middle element as new node
     * 3. Attach new node to parent based on isLeft flag
     * 4. Push left and right subranges onto stack for processing
     *
     * Time Complexity: O(N) where N is the number of elements.
     *
     * Space Complexity: O(log N) for the explicit stack in balanced tree.
     *
     * @param nums sorted array in ascending order
     * @return root node of the constructed height-balanced BST
     */
    public TreeNode sortedArrayToBSTIterative(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        TreeNode root = new TreeNode(nums[nums.length / 2]);
        Stack<Object[]> stack = new Stack<>(); // Each entry: {left, right, parentNode, isLeftChild}

        int mid = nums.length / 2;
        if (mid > 0) {
            stack.push(new Object[]{0, mid - 1, root, true});
        }
        if (mid < nums.length - 1) {
            stack.push(new Object[]{mid + 1, nums.length - 1, root, false});
        }

        while (!stack.isEmpty()) {
            Object[] current = stack.pop();
            int left = (int) current[0];
            int right = (int) current[1];
            TreeNode parent = (TreeNode) current[2];
            boolean isLeft = (boolean) current[3];

            mid = left + (right - left) / 2;
            TreeNode node = new TreeNode(nums[mid]);

            if (isLeft) {
                parent.left = node;
            } else {
                parent.right = node;
            }

            if (left <= mid - 1) {
                stack.push(new Object[]{left, mid - 1, node, true});
            }
            if (mid + 1 <= right) {
                stack.push(new Object[]{mid + 1, right, node, false});
            }
        }

        return root;
    }

    /**
     * Definition for a binary tree node.
     */
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