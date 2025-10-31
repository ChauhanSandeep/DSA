package trees.binarysearchtree;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Given a preorder traversal sequence, determine if a valid Binary Search Tree (BST)
 * can be constructed from it.
 * Example: [40, 30, 25, 35, 80, 100]
 * - This sequence can form a valid BST.
 *                    40
 *                  /   \
 *                30    80
 *               /  \     \
 *              25  35    100
 *
 * <p>LeetCode Link: https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/
 */
public class ValidBstFromPreorder {
    public static void main(String[] args) {
        /*
                   40
                 /   \
               30    80
              /  \     \
             25  35    100
         */
        int[] preorderSequence = {40, 30, 25, 35, 80, 100};
        boolean isValid = new ValidBstFromPreorder().isValidPreorderBST(preorderSequence);
        System.out.println("Is valid BST? " + isValid);
    }
    /**
     * This method checks if the given preorder sequence can form a valid BST.
     * It uses a recursive approach to verify the properties of BST.
     * This is suboptimal than using stack approach because it uses O(N) space for recursion.
     *
     * Time Complexity: O(N) where N is the number of nodes in the sequence.
     * Space Complexity: O(N) for the recursion stack.
     */
    public boolean isValidPreorderBstSuboptimal(int[] preorderSequence) {
        return verify(preorderSequence, 0, preorderSequence.length - 1);
    }

    /**
     * Recursive function to verify if the subarray from start to end can form a valid BST preorder.
     */
    private boolean verify(int[] preorder, int start, int end) {
        if (start >= end) return true; // Empty or one-element subtree is valid

        int root = preorder[start];

        // Step 1: Find the first element greater than root — this is the start of right subtree
        int rightStart = start + 1;
        while (rightStart <= end && preorder[rightStart] < root) {
            rightStart++;
        }

        // Step 2: Ensure all elements in the right subtree are > root
        for (int i = rightStart; i <= end; i++) {
            if (preorder[i] < root) {
                return false; // Invalid BST
            }
        }

        // Step 3: Recursively verify left and right subtrees
        boolean leftValid = verify(preorder, start + 1, rightStart - 1);
        boolean rightValid = verify(preorder, rightStart, end);

        return leftValid && rightValid;
    }

    /**
     * Thinking intuition process (How we came up with this approach?)
     * -
     *
     *  <p>Logic:
     * - Use a monotonic stack to track ancestors.
     * - Maintain a `lowerBound` to track the valid range for the next node.
     * - If we encounter a node smaller than `lowerBound`, it's invalid.
     * - This is because all the nodes in right subtree should be greater than the current node.(lowerBound)
     * - When encountering a larger node, pop from the stack to update the last known root.
     *
     * Dry run:
     *
     * - For each node in preorder:
     *   - Check if it's valid against `lowerBound`.
     *   - Pop nodes from stack while current node is greater than the top of the stack.
     *   - Push the current node onto the stack.
     *
     * <p>Example:
     * Given preorder sequence: [40, 30, 25, 35, 80, 100]
     * - Start with empty stack and `lowerBound = Integer.MIN_VALUE`.
     * - Process each value:
     *   - 40: push to stack
     *   - 30: push to stack
     *   - 25: push to stack
     *   - 35: pop 25 (valid), pop 30 (valid), update `lowerBound` to 30, push 35
     *   - 80: pop all smaller values, update `lowerBound` to 40, push 80
     *
     * <p>Time Complexity: O(N), where N is the number of nodes in the sequence.
     * <p>Space Complexity: O(N) for the stack in the worst case.
     *
     * @param preorderSequence The preorder traversal array.
     * @return true if a valid BST can be formed, otherwise false.
     */
    public boolean isValidPreorderBST(int[] preorderSequence) {
        if (preorderSequence == null || preorderSequence.length == 0) {
            return true; // An empty sequence is trivially a valid BST.
        }

        Deque<Integer> stack = new ArrayDeque<>();
        int lowerBound = Integer.MIN_VALUE; // max of left subtree

        for (int value : preorderSequence) {
            // If current value is smaller than the last valid root, it violates BST properties.
            if (value < lowerBound) {
                // why? because all the nodes in right subtree should be greater than the max of left subtree.
                return false;
            }

            // Remove smaller elements (they are left children), update `lowerBound` to last popped.
            while (!stack.isEmpty() && stack.peek() < value) {
                lowerBound = stack.pop();
            }

            // Push current value as a potential new root.
            stack.push(value);
        }

        return true;
    }
}