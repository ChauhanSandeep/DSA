package trees.binarysearchtree;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Problem: Verify Preorder Sequence in Binary Search Tree
 *
 * Given an integer array, decide whether it can be the preorder traversal of a
 * BST. Preorder visits root first, then the entire left subtree, then the entire
 * right subtree.
 *
 * Leetcode: https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/ (Medium)
 * Rating:   not available (premium problem)
 * Pattern:  Trees | BST | Monotonic stack with lower bound
 *
 * Example:
 *   Input:  preorder = [40,30,25,35,80,100]
 *   Output: true
 *   Why:    after leaving the left side of 40, all later right-subtree values stay above 40.
 *
 * Follow-ups:
 *   1. Can this be done in O(1) extra space?
 *      Reuse the preorder array itself as the stack if mutation is allowed.
 *   2. How would duplicates be handled?
 *      Decide whether duplicates go left or right and adjust comparisons.
 *   3. How would you validate postorder instead?
 *      Mirror the bounds logic by scanning from the end.
 *   4. How would you construct the BST if valid?
 *      Recurse with min/max bounds or use a stack of ancestors.
 *
 * Related: Construct BST from Preorder Traversal (1008).
 */
public class ValidBstFromPreorder {
        public static void main(String[] args) {
        ValidBstFromPreorder solver = new ValidBstFromPreorder();
        int[][] inputs = { {40, 30, 25, 35, 80, 100}, {40, 30, 35, 20, 80} };
        boolean[] expected = { true, false };

        for (int i = 0; i < inputs.length; i++) {
            boolean got = solver.isValidPreorderBST(inputs[i]);
            System.out.printf("preorder=%s -> %b  expected=%b%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * This method checks if the given preorder sequence can form a valid BST.
     * It uses a recursive approach to verify the properties of BST.
     * This is suboptimal than using stack approach because it has worse time complexity.
     *
     * Time Complexity: O(N²) in worst case - for each node, we iterate through remaining elements
     *                  to find and validate the right subtree. In a skewed tree, this becomes
     *                  N + (N-1) + (N-2) + ... + 1 = O(N²).
     * Space Complexity: O(N) for the recursion stack in worst case (skewed tree).
     */
    public boolean isValidPreorderBstSuboptimal(int[] preorderSequence) {
        return verify(preorderSequence, 0, preorderSequence.length - 1);
    }

        /**
     * Recursively verifies one preorder slice by splitting left and right subtrees.
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
     * Intuition: once preorder moves from a node's left subtree into its right
     * subtree, every future value in that subtree must stay above that node. The stack
     * keeps ancestors we have not closed yet, and lowerBound records the most recent
     * ancestor whose right side we entered.
     *
     * Algorithm:
     *   1. Treat null or empty input as valid.
     *   2. For each value, reject it if it is below lowerBound.
     *   3. Pop smaller ancestors while moving into their right subtree and update lowerBound.
     *   4. Push the current value as a possible ancestor for later nodes.
     *
     * Time:  O(n) - each value is pushed and popped at most once.
     * Space: O(n) - stack may hold a decreasing preorder sequence.
     *
     * @param preorderSequence preorder values to validate
     * @return true if the sequence can represent a BST preorder traversal
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