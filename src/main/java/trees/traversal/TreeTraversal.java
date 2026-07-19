package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.Stack;
import java.util.Arrays;


/**
 * Problem: Binary Tree Traversals
 *
 * Demonstrates preorder, inorder, and postorder traversals of a binary tree in
 * both recursive and iterative forms. Each traversal visits the same nodes but
 * changes when the root is processed relative to its children.
 *
 * Leetcode: https://leetcode.com/tag/tree/ (Traversal fundamentals)
 * Rating:   not available
 * Pattern:  Trees | DFS | Recursion | Explicit stack traversal
 *
 * Example:
 *   Input:  root = [5,4,6,3,14,null,7,null,null,null,null,9,8]
 *   Output: preorder = 5->4->3->14->6->7->9->8->
 *   Why:    preorder visits root first, then the left subtree, then the right subtree.
 *
 * Follow-ups:
 *   1. Can inorder be done with O(1) extra space?
 *      Use Morris traversal by temporarily threading predecessor links.
 *   2. How would you return lists instead of printing?
 *      Append to a List<Integer> at each visit point instead of printing.
 *   3. How would you traverse an n-ary tree?
 *      Generalize child iteration order and stack pushes over each node's children.
 *
 * Related: Binary Tree Preorder Traversal (144), Inorder Traversal (94), Postorder Traversal (145).
 */
public class TreeTraversal {

        public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.right = new TreeNode(14);
        root.left.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);

        System.out.printf("preorder root=%s -> ", Arrays.toString(new int[] {5, 4, 6, 3, 14, 7, 9, 8}));
        preorder(root);
        System.out.printf("  expected=%s%n", "5->4->3->14->6->7->9->8->");
        System.out.printf("inorder root=%s -> ", Arrays.toString(new int[] {5, 4, 6, 3, 14, 7, 9, 8}));
        inorder(root);
        System.out.printf("  expected=%s%n", "3->4->14->5->6->9->7->8->");
    }


        /**
     * Intuition: postorder delays visiting a node until both child subtrees have
     * been completely processed. The recursive call stack naturally remembers
     * where to return after left and right traversal.
     *
     * Algorithm:
     *   1. Return immediately for a null node.
     *   2. Traverse the left subtree.
     *   3. Traverse the right subtree.
     *   4. Print the current root value.
     *
     * Time:  O(n) - each node is printed once.
     * Space: O(h) - recursion stack height.
     *
     * @param root current subtree root
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
     * ITERATIVE APPROACHES
     * https://claude.ai/public/artifacts/0b255d6a-e7dc-45bb-be58-b6f56d50e032
     */

    /**
     * Iterative Inorder Traversal using a stack.
     *
     *                 5
     *                /  \
     *              4     6
     *             / \     \
     *            3  14     7
     *                     / \
     *                    9   8
     * Output : 3->4->14->5->6->9->7->8->
     *
     * Steps:
     * 1. Initialize an empty stack and set the current node to the root.
     * 2. While the current node is not null or the stack is not empty:
     *   a. Traverse to the leftmost node, pushing each node onto the stack.
     *   b. Pop a node from the stack, visit it, and set the current node to its right child.
     * 3. Repeat until all nodes are visited.
     *
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
     *
     *                 5
     *                /  \
     *              4     6
     *             / \     \
     *            3  14     7
     *                     / \
     *                    9   8
     * Output : 5->4->3->14->6->7->9->8->
     *
     * Steps:
     * 1. Initialize an empty stack and push the root node onto it.
     * 2. While the stack is not empty:
     *  a. Pop the top node from the stack and visit it.
     *  b. Push the right child of the popped node onto the stack (if it exists).
     *  c. Push the left child of the popped node onto the stack (if it exists).
     * 3. Repeat until all nodes are visited.
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
     *                 5
     *                /  \
     *              4     6
     *             / \     \
     *            3  14     7
     *                     / \
     *                    9   8
     * Output :
     * Steps: 3->14->4->9->8->7->6->5->
     * 1. Initialize two stacks: one for processing nodes and another for storing the output
     * 2. Push the root node onto the processing stack.
     * 3. While the processing stack is not empty:
     *  a. Pop a node from the processing stack and push its value onto the output
     *  b. Push the left child of the popped node onto the processing stack (if it exists).
     *  c. Push the right child of the popped node onto the processing stack (if it exists).
     * 4. Once all nodes are processed, pop values from the output stack to get the postorder traversal.
     *
     * This is very similar to Preorder but the difference is that
     * - In Postorder, the left subtree is visited first, followed by the right subtree, and then the root.
     * - The output is stored in reverse order using a second stack.
     *
     * @param root The root of the tree.
     */
    public static void postorderIterative(TreeNode root) {
        if (root == null) return;

        Stack<TreeNode> processingStack = new Stack<>();
        Stack<Integer> outputStack = new Stack<>();

        processingStack.push(root);

        // Process nodes in the same way as Postorder
        while (!processingStack.isEmpty()) {
            TreeNode curr = processingStack.pop();
            outputStack.push(curr.val); // Push data into the output stack

            // Push left and right children into the processing stack
            if (curr.left != null) processingStack.push(curr.left);
            if (curr.right != null) processingStack.push(curr.right);
        }

        // Print the postorder traversal by popping from the output stack
        while (!outputStack.isEmpty()) {
            System.out.print(outputStack.pop() + "->");
        }
    }
}
