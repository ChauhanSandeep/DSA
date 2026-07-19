package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Binary Search Tree Iterator
 *
 * Implement an iterator that returns BST values in sorted order. The primary
 * iterator keeps only the path to the next smallest node instead of materializing
 * the full inorder traversal up front.
 *
 * Leetcode: https://leetcode.com/problems/binary-search-tree-iterator/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Controlled inorder traversal with stack
 *
 * Example:
 *   Input:  root = [7,3,15,null,null,9,20], calls = next,next,hasNext
 *   Output: [3,7,true]
 *   Why:    inorder traversal of a BST visits values in ascending order.
 *
 * Follow-ups:
 *   1. Can next and hasNext both be worst-case O(1)?
 *      Only if all values are precomputed, which costs O(n) space.
 *   2. How would you add previous()?
 *      Maintain extra history or a second stack for reverse movement.
 *   3. How would you support frequent tree updates?
 *      Use a balanced BST with parent links or restart affected iterator state.
 *   4. How would you get O(1) extra space?
 *      Morris traversal can thread the tree temporarily, but it mutates pointers.
 */
public class BinarySearchTreeIterator {

    public static void main(String[] args) {
        int[][] expected = { {3, 7, 9, 15, 20}, {1} };

        TreeNode root = new TreeNode(7);
        root.left = new TreeNode(3);
        root.right = new TreeNode(15, new TreeNode(9), new TreeNode(20));

        TreeNode single = new TreeNode(1);
        TreeNode[] roots = { root, single };

        for (int i = 0; i < roots.length; i++) {
            BSTIterator iterator = new BSTIterator(roots[i]);
            List<Integer> output = new ArrayList<>();
            while (iterator.hasNext()) {
                output.add(iterator.next());
            }
            System.out.printf("case=%d -> %s  expected=%s%n",
                i + 1, output, Arrays.toString(expected[i]));
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

        /**
     * Primary stack-backed iterator. It simulates the original inorder recursion by
     * keeping the current root-to-leftmost path on stack.
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