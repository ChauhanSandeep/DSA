package trees;

import utils.TreeNode;

import java.util.Stack;

/**
 * You are given the root of a binary search tree (BST), where the values of exactly two nodes of the tree were swapped by mistake.
 * Recover the tree without changing its structure.
 *
 * Example 1:
 * Input: root = [1,3,null,null,2]
 * Output: [3,1,null,null,2]
 * Explanation: 3 cannot be a leftChild child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
 *
 * Example 2:
 * Input: root = [3,1,4,null,null,2]
 * Output: [2,1,4,null,null,3]
 * Explanation: 2 cannot be in the rightChild subtree of 3 because 2 < 3. Swapping 2 and 3 makes the BST valid.
 *
 * LeetCode: https://leetcode.com/problems/recover-binary-search-tree/
 *
 * Follow-up Questions:
 * 1. How would you handle the case where more than two nodes are swapped?
 *    - The problem guarantees exactly two nodes are swapped, but for k swaps, we'd need to track all violations.
 * 2. What if the tree is very large (e.g., 10^5 nodes)?
 *    - The Morris traversal solution uses O(1) extra space, making it suitable for large trees.
 * 3. How would you verify if the tree is a valid BST after recovery?
 *    - We could perform an in-order traversal and check if the sequence is strictly increasing.
 *
 * Related Problems:
 * - Validate Binary Search Tree (https://leetcode.com/problems/validate-binary-search-tree/)
 * - Binary Tree Inorder Traversal (https://leetcode.com/problems/binary-tree-inorder-traversal/)
 */
public class RecoverBinarySearchTree {
    private TreeNode first = null;    // First misplaced node
    private TreeNode second = null;   // Second misplaced node
    private TreeNode prev = null;     // Previous node in in-order traversal

    /**
     * Recovers the BST by swapping the two misplaced nodes.
     *
     * @param root Root of the binary search tree
     */
    public void recoverTree(TreeNode root) {
        // Find the two misplaced nodes
        findMisplacedNodes(root);

        // Swap the values of the two misplaced nodes
        if (first != null && second != null) {
            int temp = first.value;
            first.value = second.value;
            second.value = temp;
        }
    }

    /**
     * Performs in-order traversal to find the two misplaced nodes.
     */
    private void findMisplacedNodes(TreeNode root) {
        if (root == null) {
            return;
        }

        // In-order traversal using stack (iterative)
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            // Go to the leftmost node
            while (current != null) {
                stack.push(current);
                current = current.leftChild;
            }

            // Process the current node
            current = stack.pop();

            // Check if current node is out of order
            if (prev != null && current.value < prev.value) {
                // If this is the first violation, record both nodes
                if (first == null) {
                    first = prev;
                    second = current; // In case the two nodes are adjacent
                } else {
                    // Second violation, update second node
                    second = current;
                    break; // No need to continue after finding both nodes
                }
            }

            prev = current;
            current = current.rightChild; // Move to rightChild subtree
        }
    }

    /**
     * Morris Traversal solution with O(1) space complexity.
     * This approach modifies the tree structure temporarily during traversal.
     */
    public void recoverTreeMorris(TreeNode root) {
        TreeNode current = root;
        TreeNode prevNode = null;
        TreeNode firstNode = null;
        TreeNode secondNode = null;

        while (current != null) {
            if (current.leftChild == null) {
                // Process current node
                if (prevNode != null && prevNode.value > current.value) {
                    if (firstNode == null) {
                        firstNode = prevNode;
                    }
                    secondNode = current;
                }
                prevNode = current;
                current = current.rightChild;
            } else {
                // Find the inorder predecessor
                TreeNode predecessor = current.leftChild;
                while (predecessor.rightChild != null && predecessor.rightChild != current) {
                    predecessor = predecessor.rightChild;
                }

                if (predecessor.rightChild == null) {
                    // Create a temporary link to current node
                    predecessor.rightChild = current;
                    current = current.leftChild;
                } else {
                    // Remove the temporary link and process current node
                    predecessor.rightChild = null;

                    if (prevNode != null && prevNode.value > current.value) {
                        if (firstNode == null) {
                            firstNode = prevNode;
                        }
                        secondNode = current;
                    }
                    prevNode = current;
                    current = current.rightChild;
                }
            }
        }

        // Swap the values of the two misplaced nodes
        if (firstNode != null && secondNode != null) {
            int temp = firstNode.value;
            firstNode.value = secondNode.value;
            secondNode.value = temp;
        }
    }

    /**
     * Recursive in-order traversal solution.
     */
    public void recoverTreeRecursive(TreeNode root) {
        // Reset class variables
        first = null;
        second = null;
        prev = new TreeNode(Integer.MIN_VALUE);

        // Find the two misplaced nodes
        inOrderTraversal(root);

        // Swap the values of the two misplaced nodes
        if (first != null && second != null) {
            int temp = first.value;
            first.value = second.value;
            second.value = temp;
        }
    }

    /**
     * Performs in-order traversal to find the two misplaced nodes (recursive).
     */
    private void inOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        inOrderTraversal(node.leftChild);

        // Check if current node is out of order
        if (first == null && prev.value > node.value) {
            first = prev; // First violation
        }
        if (first != null && prev.value > node.value) {
            second = node; // Second violation or adjacent nodes
        }

        prev = node;
        inOrderTraversal(node.rightChild);
    }
}
