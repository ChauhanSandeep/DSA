package trees.traversal;

import trees.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Problem: Perform a spiral (zigzag) level-order traversal of a binary tree.
 * Input:
 *          1
 *         / \
 *        2   3
 *       / \ / \
 *      7  6 5  4
 * Output: [1, 3, 2, 7, 6, 5, 4]
 *
 * Intuition:
 * The traversal alternates between left-to-right and right-to-left levels.
 * To achieve this, two stacks are used:
 * - One stack for left-to-right level traversal.
 * - Another stack for right-to-left level traversal.
 * The algorithm processes the tree level by level, alternately pushing the nodes
 * into one of the two stacks and popping nodes to create the spiral order output.
 *
 * Algorithm:
 * 1. Start with the root node and push it into `stack1`.
 * 2. For each level, pop nodes from `stack1` (left-to-right) and push their children into `stack2`.
 * 3. Then, pop nodes from `stack2` (right-to-left) and push their children into `stack1`.
 * 4. Repeat this process until both stacks are empty.
 *
 * Time Complexity: O(N), where N is the number of nodes in the tree.
 * Space Complexity: O(N), due to the space used by the stacks and the result list.
 *
 * LeetCode Link: https://leetcode.com/problems/spiral-order-traversal-of-binary-tree/
 */

public class SpiralTraversal {

    public static void main(String[] args) {
        // Constructing the binary tree
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(7);
        root.left.right = new Node(6);
        root.right.left = new Node(5);
        root.right.right = new Node(4);

        // Printing the spiral order traversal
        System.out.println("Spiral Order traversal of Binary Tree is ");
        System.out.println(spiralTraversal(root)); // Expected Output: [1, 3, 2, 7, 6, 5, 4]
    }

    /**
     * Perform a spiral order traversal (zigzag level-order traversal) of a binary tree.
     *
     * @param root The root of the binary tree.
     * @return A list containing the nodes in spiral order.
     */
    public static List<Integer> spiralTraversal(Node root) {
        List<Integer> result = new ArrayList<>();

        // Return an empty list if the tree is empty
        if (root == null) return result;

        // Two stacks for alternating level-order traversal
        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        // Push the root node to the first stack
        stack1.push(root);

        // Loop until both stacks are empty
        while (!stack1.isEmpty() || !stack2.isEmpty()) {

            // Process nodes in stack1 (left-to-right level traversal)
            while (!stack1.isEmpty()) {
                Node node = stack1.pop();
                result.add(node.data);

                // Push the right child first to ensure the left child is processed next
                if (node.right != null) {
                    stack2.push(node.right);
                }
                if (node.left != null) {
                    stack2.push(node.left);
                }
            }

            // Process nodes in stack2 (right-to-left level traversal)
            while (!stack2.isEmpty()) {
                Node node = stack2.pop();
                result.add(node.data);

                // Push the left child first to ensure the right child is processed next
                if (node.left != null) {
                    stack1.push(node.left);
                }
                if (node.right != null) {
                    stack1.push(node.right);
                }
            }
        }
        return result;
    }
}
