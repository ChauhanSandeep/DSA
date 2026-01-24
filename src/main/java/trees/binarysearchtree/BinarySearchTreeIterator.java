package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * 173. Binary Search Tree Iterator
 *
 * Problem: Implement an iterator over a binary search tree (BST) that will iterate
 * over the BST in in-order traversal (smallest to largest).
 *
 * Example:
 * Input: root = [7,3,15,null,null,9,20]
 * BSTIterator iterator = new BSTIterator(root);
 * iterator.next();    // return 3
 * iterator.hasNext(); // return True
 * iterator.next();    // return 7
 *
 * LeetCode: https://leetcode.com/problems/binary-search-tree-iterator
 *
 * Follow-up questions:
 * Q: Can you implement with O(1) time for both operations?
 * A: No, amortized O(1) is the best we can do due to tree traversal nature.
 *
 * Q: How to implement previous() method?
 * A: Need to maintain parent pointers or use two stacks (forward/backward).
 *
 * Q: What if we need to support arbitrary position jumping?
 * A: Pre-compute all nodes in array or use augmented BST with size information.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class BinarySearchTreeIterator {

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

    /**
     * BST Iterator using controlled inorder traversal with stack.
     *
     * Algorithm: Controlled recursion simulation
     * - Use stack to simulate inorder traversal
     * - Push all left children of current node to stack
     * - On next(): pop from stack, process right subtree of popped node
     * - Maintains O(h) space where h is tree height
     *
     * Time Complexity: O(1) amortized for both operations
     * Space Complexity: O(h) where h is height of tree
     */
    public static class BSTIterator {

        private Stack<TreeNode> stack;

        public BSTIterator(TreeNode root) {
            stack = new Stack<>();
            pushAllLeft(root);
        }

        // Pushes all left children of node to stack
        private void pushAllLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        /**
         * Returns the next smallest number in the BST.
         */
        public int next() {
            TreeNode node = stack.pop();
            pushAllLeft(node.right);
            return node.val;
        }

        /**
         * Returns whether we have a next smallest number.
         */
        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    /**
     * Alternative implementation using precomputed list.
     * Trade-off: more space for guaranteed O(1) operations.
     */
    public static class BSTIteratorPrecomputed {

        private List<Integer> nodes;
        private int index;

        public BSTIteratorPrecomputed(TreeNode root) {
            nodes = new ArrayList<>();
            index = 0;
            inorderTraversal(root);
        }

        // Precompute all nodes in inorder
        private void inorderTraversal(TreeNode root) {
            if (root == null) return;
            inorderTraversal(root.left);
            nodes.add(root.val);
            inorderTraversal(root.right);
        }

        public int next() {
            return nodes.get(index++);
        }

        public boolean hasNext() {
            return index < nodes.size();
        }
    }

    /**
     * Space-optimized iterator using Morris traversal concept.
     * Modifies tree structure temporarily but restores it.
     */
    public static class BSTIteratorMorris {

        private TreeNode current;

        public BSTIteratorMorris(TreeNode root) {
            current = root;
        }

        public int next() {
            int result = -1;

            while (current != null) {
                if (current.left == null) {
                    result = current.val;
                    current = current.right;
                    break;
                } else {
                    // Find inorder predecessor
                    TreeNode predecessor = current.left;
                    while (predecessor.right != null && predecessor.right != current) {
                        predecessor = predecessor.right;
                    }

                    if (predecessor.right == null) {
                        // Make current the right child of predecessor
                        predecessor.right = current;
                        current = current.left;
                    } else {
                        // Restore the tree structure
                        predecessor.right = null;
                        result = current.val;
                        current = current.right;
                        break;
                    }
                }
            }

            return result;
        }

        public boolean hasNext() {
            return current != null;
        }
    }

    /**
     * Iterator with additional functionality - supports peeking.
     * Useful extension for some applications.
     */
    public static class EnhancedBSTIterator {

        private Stack<TreeNode> stack;
        private TreeNode nextNode;

        public EnhancedBSTIterator(TreeNode root) {
            stack = new Stack<>();
            pushAllLeft(root);
            prepareNext();
        }

        private void pushAllLeft(TreeNode node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        // Prepares the next node without consuming it
        private void prepareNext() {
            if (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                nextNode = node;
                pushAllLeft(node.right);
            } else {
                nextNode = null;
            }
        }

        public int next() {
            int result = nextNode.val;
            prepareNext();
            return result;
        }

        public boolean hasNext() {
            return nextNode != null;
        }

        // Additional method to peek at next value without consuming
        public int peek() {
            if (!hasNext()) {
                throw new RuntimeException("No more elements");
            }
            return nextNode.val;
        }
    }
}