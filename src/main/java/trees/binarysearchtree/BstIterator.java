package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 173. Binary Search Tree Iterator
 *
 * Problem: Implement an iterator over a binary search tree (BST) that will iterate
 * over the BST in in-order traversal (smallest to largest).
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
     * Steps:
     * 1. Pop the top node from the stack (this is the next smallest element).
     * 2. If the popped node has a right child, push all its left descendants onto the stack.
     * 3. Return the value of the popped node.
     *
     * Time Complexity: O(1) amortized, because each node is pushed and popped exactly once.
     * Space Complexity: O(H) where H is the height of the tree
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
     * Pushes all left descendants of a given node onto the stack.
     * @param node The starting node.
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

        BstIterator iterator = new BstIterator(root);
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}