package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
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
 * Explanation: This looks like
 *         0
 *         / \
 *       -3   9
 *       /    /
 *     -10   5
 * [0,-10,5,null,-3,null,9] would also be accepted
 *
 * LeetCode Link: https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree
 *
 * Follow-up Questions:
 *
 * 1. What if the input is a sorted linked list instead of an array?
 *    Answer: Convert the linked list to an array first for O(1) access to elements, or use
 *    a two-pointer approach with slow-fast pointers to find the middle element in O(n) time.
 *    The latter avoids extra space but increases time complexity per recursive call.
 *    Related problem: https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/
 *
 * 2. How would you ensure the tree is not just balanced but also complete?
 *    Answer: Use level-order construction instead of recursive midpoint selection. Fill nodes
 *    level by level from left to right to create a complete binary tree structure.
 *
 * 3. What if you need to return all possible balanced BSTs?
 *    Answer: At each step, instead of choosing only the middle element, try all elements as root
 *    and recursively generate all possible left and right subtrees. Combine them to get all
 *    valid BSTs. This increases complexity exponentially (Catalan number).
 *
 * 4. Can you construct the BST iteratively without recursion?
 *    Answer: Yes, use a stack to simulate recursion. Push tuples of (array range, parent node, direction)
 *    onto the stack and process them iteratively. This converts implicit recursion stack to explicit stack.
 *
 * 5. How would you modify this to create an AVL tree with explicit balance factors?
 *    Answer: During construction, track and store the balance factor (height difference between
 *    left and right subtrees) in each node. Calculate heights bottom-up during recursion return.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ConvertSortedArrayToBinarySearchTree {
    /**
     * Converts sorted array to height-balanced BST using divide and conquer.
     *
     * Algorithm:
     * 1. Choose middle element of array as root to ensure balance
     * 2. Elements to the left form the left subtree (all smaller than root)
     * 3. Elements to the right form the right subtree (all larger than root)
     * 4. Recursively apply same process to left and right subarrays
     * 5. Base case: when subarray is empty, return null
     *
     * Time Complexity: O(N) where N is the number of elements in the array. Each element
     * is visited exactly once to create a corresponding tree node.
     *
     * Space Complexity: O(log N) for the recursion call stack in a balanced tree. The tree
     * height is log N, which determines the maximum recursion depth.
     *
     * @param nums sorted array in ascending order
     * @return root node of the constructed height-balanced BST
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