package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Problem: Binary Search Tree Iterator
 *
 * Implement an iterator over a BST that returns values from smallest to largest.
 * The iterator exposes hasNext and next while preserving the original stack-based
 * controlled inorder traversal.
 *
 * Leetcode: https://leetcode.com/problems/binary-search-tree-iterator/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Inorder traversal with explicit stack
 *
 * Example:
 *   Input:  root = [10,7,13,3,9,null,15]
 *   Output: [3,7,9,10,13,15]
 *   Why:    inorder traversal visits every BST node in sorted order.
 *
 * Follow-ups:
 *   1. Can next be worst-case O(1)?
 *      Precompute the inorder list, trading O(n) memory for O(1) calls.
 *   2. How would you support previous()?
 *      Keep a visited history stack or use parent pointers.
 *   3. What changes for duplicate values?
 *      The iterator logic stays the same; the BST insertion policy decides order.
 *   4. How would you make it thread-safe?
 *      Synchronize state-changing calls or give each thread its own iterator.
 */
public class BstIterator {
    private final Deque<TreeNode> stack;

    /**
     * Initializes the iterator with the root of the BST.
     * @param root The root node of the BST.
     */
    public BstIterator(TreeNode root) {
        stack = new LinkedList<>();
        pushLeftSubtree(root);
    }

    /**
     * Steps:
     * 1. Check if the stack is not empty.
     * 2. If not empty, there are more elements to iterate over.
     *
     * Time Complexity: O(1)
     * Space Complexity: O(1)
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

        /**
     * Intuition: inorder is the sorted traversal of a BST. The stack stores exactly
     * the ancestors whose left side has already been explored but whose value has not
     * been returned yet. Popping gives the next smallest node; pushing the left spine
     * of its right child prepares the next larger value.
     *
     * Algorithm:
     *   1. Pop the current smallest node from stack.
     *   2. Save its val as the result.
     *   3. If it has a right child, push that right subtree's left spine.
     *   4. Return the saved value.
     *
     * Time:  O(1) amortized - each node is pushed and popped once across all calls.
     * Space: O(h) - stack holds at most one root-to-leaf path.
     *
     * @return next smallest BST value
     */
    public int next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements in the BST.");
        }

        TreeNode node = stack.pop();
        int result = node.val;

        // If the popped node has a right subtree, push its leftmost nodes
        if (node.right != null) {
            pushLeftSubtree(node.right);
        }

        return result;
    }

        /**
     * Pushes the left spine starting at node onto stack.
     */
    private void pushLeftSubtree(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

        public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(7);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(9);
        root.right = new TreeNode(13);
        root.right.right = new TreeNode(15);

        int[] expected = {3, 7, 9, 10, 13, 15};
        int[] output = new int[expected.length];
        BstIterator iterator = new BstIterator(root);
        int index = 0;
        while (iterator.hasNext()) {
            output[index++] = iterator.next();
        }

        TreeNode single = new TreeNode(1);
        BstIterator singleIterator = new BstIterator(single);
        System.out.printf("root=[10,7,13,3,9,null,15] -> %s  expected=%s%n",
            Arrays.toString(output), Arrays.toString(expected));
        System.out.printf("root=[1] -> %d  expected=1%n", singleIterator.next());
    }

}