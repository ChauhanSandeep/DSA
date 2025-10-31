package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.Stack;


/**
 * TreeTraversal performs various tree traversal methods (Preorder, Inorder, and Postorder)
 * both recursively and iteratively.
 *
 * - Preorder: Visit the root first, then the left subtree, and then the right subtree.
 * - Inorder: Visit the left subtree first, then the root, and then the right subtree.
 * - Postorder: Visit the left subtree first, then the right subtree, and then the root.
 *
 * Intuition:
 * - Recursive traversals are straightforward but can cause stack overflow for large trees.
 * - Iterative approaches use an explicit stack to simulate the recursive calls, preventing stack overflow and providing more control.
 *
 * Time Complexity:
 * - All traversals: O(N), where N is the number of nodes in the tree, since each node is visited once.
 *
 * Space Complexity:
 * - Recursive traversals: O(H), where H is the height of the tree due to the recursive call stack.
 * - Iterative traversals: O(N), where N is the number of nodes in the tree, as we store the nodes in a stack.
 *
 * LeetCode Link: https://leetcode.com/tag/tree/
 */
public class TreeTraversal {

    public static void main(String[] args) {
        // Sample binary tree structure
        /*
                5
               /    \
             4       6
            / \       \
           3  14       7
                      / \
                     9   8
        */
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.right = new TreeNode(14);
        root.left.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);

        System.out.println("Postorder traversal (recursive):");
        postorder(root);

        System.out.println("\nInorder traversal (iterative):");
        inorderIterative(root);

        System.out.println("\nPreorder traversal (recursive):");
        preorder(root);

        System.out.println("\nPostorder traversal (iterative):");
        postorderIterative(root);

        System.out.println("\nPreorder traversal (iterative):");
        preorderIterative(root);
    }

    /**
     * Recursive Postorder Traversal.
     * In Postorder, the left subtree is visited first, followed by the right subtree, and then the root.
     *
     * @param root The current node.
     */
    static void postorder(TreeNode root) {
        if (root == null) return;

        postorder(root.left); // Traverse left subtree
        postorder(root.right); // Traverse right subtree
        System.out.print(root.val + "->"); // Visit the node
    }

    /**
     * Recursive Inorder Traversal.
     * In Inorder, the left subtree is visited first, followed by the root, and then the right subtree.
     *
     * @param root The current node.
     */
    static void inorder(TreeNode root) {
        if (root == null) return;

        inorder(root.left); // Traverse left subtree
        System.out.print(root.val + "->"); // Visit the node
        inorder(root.right); // Traverse right subtree
    }

    /**
     * Recursive Preorder Traversal.
     * In Preorder, the root is visited first, followed by the left subtree, and then the right subtree.
     *
     * @param root The current node.
     */
    static void preorder(TreeNode root) {
        if (root == null) return;

        System.out.print(root.val + "->"); // Visit the node
        preorder(root.left); // Traverse left subtree
        preorder(root.right); // Traverse right subtree
    }

    /**
     * Iterative Inorder Traversal using a stack.
     * In Inorder, the left subtree is visited first, followed by the root, and then the right subtree.
     * This iterative version avoids recursion by using an explicit stack.
     *
     * @param root The root of the tree.
     */
    public static void inorderIterative(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;

        while (curr != null || !stack.isEmpty()) {
            // Reach the leftmost Node of the current Node
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            // Current must be null at this point
            curr = stack.pop();
            System.out.print(curr.val + "->");
            // Visit the right subtree
            curr = curr.right;
        }
    }

    /**
     * Iterative Preorder Traversal using a stack.
     * In Preorder, the root is visited first, followed by the left subtree, and then the right subtree.
     * This iterative version avoids recursion by using an explicit stack.
     *
     * @param root The root of the tree.
     */
    public static void preorderIterative(TreeNode root) {
        if (root == null) return;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop(); // Pop the top node
            System.out.print(curr.val + "->"); // Visit the node

            // Right child is pushed first so the left child is processed first (LIFO order)
            if (curr.right != null) stack.push(curr.right);
            if (curr.left != null) stack.push(curr.left);
        }
    }

    /**
     * Iterative Postorder Traversal using two stacks.
     * In Postorder, the left subtree is visited first, followed by the right subtree, and then the root.
     * This iterative version avoids recursion by using two stacks.
     *
     * @param root The root of the tree.
     */
    public static void postorderIterative(TreeNode root) {
        if (root == null) return;

        Stack<TreeNode> stack = new Stack<>();
        Stack<Integer> out = new Stack<>();

        stack.push(root);

        // Process nodes in the same way as Postorder
        while (!stack.isEmpty()) {
            TreeNode curr = stack.pop();
            out.push(curr.val); // Push data into the output stack

            // Push left and right children into the stack
            if (curr.left != null) stack.push(curr.left);
            if (curr.right != null) stack.push(curr.right);
        }

        // Print the postorder traversal by popping from the output stack
        while (!out.isEmpty()) {
            System.out.print(out.pop() + " ");
        }
    }
}
