package trees;

import java.util.Deque;
import java.util.LinkedList;

/**
 * **BST Iterator (Inorder Traversal)**
 *
 * This class implements an iterator over a Binary Search Tree (BST).
 * It returns the elements in **ascending order** (inorder traversal).
 *
 * **Approach:**
 * - Uses a **stack-based iterative inorder traversal**.
 * - At initialization, pushes all left subtree nodes onto the stack.
 * - `next()` retrieves the smallest element, processes the right subtree if present.
 * - `hasNext()` checks if there are remaining elements.
 *
 * **Time Complexity:**
 * - `hasNext()`: **O(1)** (constant-time check).
 * - `next()`: **O(1)** amortized (each node is pushed & popped once).
 *
 * **Space Complexity:** **O(H)** (height of tree, worst case O(N) for a skewed tree).
 *
 * **LeetCode/InterviewBit Link:** https://www.interviewbit.com/problems/bst-iterator/
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
     * @return `true` if there is a next smallest number, otherwise `false`.
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    /**
     * @return The next smallest number in BST.
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