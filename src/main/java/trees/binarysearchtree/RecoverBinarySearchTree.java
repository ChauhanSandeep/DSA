package trees.binarysearchtree;

import utils.TreeNode;

import java.util.Stack;

/**
 * Problem: Recover Binary Search Tree
 *
 * Exactly two nodes in a BST were swapped by mistake. Restore the BST by swapping
 * the two misplaced values back without changing the tree structure.
 *
 * Leetcode: https://leetcode.com/problems/recover-binary-search-tree/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Inorder inversion detection
 *
 * Example:
 *   Input:  root = [3,1,4,null,null,2]
 *   Output: [2,1,4,null,null,3]
 *   Why:    inorder should be sorted; values 3 and 2 create an inversion and must be swapped.
 *
 * Follow-ups:
 *   1. How would you use O(1) extra space?
 *      Morris traversal threads the tree temporarily and restores links afterward.
 *   2. What if more than two nodes are misplaced?
 *      Collect all inorder violations and sort or rebuild the value order.
 *   3. How would you verify recovery?
 *      Run a strict inorder validation pass after swapping.
 *   4. What if duplicate values are allowed?
 *      The inversion check must match the tree's duplicate-side ordering policy.
 *
 * Related: Validate Binary Search Tree (98), Binary Tree Inorder Traversal (94).
 */
public class RecoverBinarySearchTree {

    public static void main(String[] args) {
        RecoverBinarySearchTree solver = new RecoverBinarySearchTree();
        TreeNode root = new TreeNode(3);
        root.leftChild = new TreeNode(1);
        root.rightChild = new TreeNode(4);
        root.rightChild.leftChild = new TreeNode(2);
        solver.recoverTree(root);
        System.out.printf("root=[3,1,4,null,null,2] -> root=%d rightLeft=%d  expected=root=2 rightLeft=3%n",
            root.value, root.rightChild.leftChild.value);

        TreeNode adjacent = new TreeNode(1);
        adjacent.leftChild = new TreeNode(3);
        adjacent.leftChild.rightChild = new TreeNode(2);
        solver.recoverTreeRecursive(adjacent);
        System.out.printf("root=[1,3,null,null,2] -> root=%d left=%d  expected=root=3 left=1%n",
            adjacent.value, adjacent.leftChild.value);
    }

    private TreeNode first = null;    // First misplaced node
    private TreeNode second = null;   // Second misplaced node
    private TreeNode prev = null;     // Previous node in in-order traversal

        /**
     * Intuition: inorder traversal of a valid BST is increasing. Swapping two values
     * creates one inversion if the values are adjacent in inorder, or two inversions
     * otherwise. The original fields first, second, and prev capture those misplaced
     * nodes during traversal.
     *
     * Algorithm:
     *   1. Traverse the tree in inorder to find misplaced nodes.
     *   2. On the first inversion, record prev as first and current as second.
     *   3. On a later inversion, update second to current.
     *   4. Swap first.value and second.value if both were found.
     *
     * Time:  O(n) - inorder traversal visits each node once.
     * Space: O(h) - the iterative stack can hold one root-to-leaf path.
     *
     * @param root root of the BST to repair
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
     * Finds the swapped nodes by scanning inorder values for inversions.
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
     * Recursive inorder scan that records the first and second misplaced nodes.
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
