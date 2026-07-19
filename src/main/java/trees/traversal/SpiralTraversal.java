package trees.traversal;

import trees.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Problem: Spiral Traversal of Binary Tree
 *
 * Return a zigzag level-order traversal of a binary tree. The first level is
 * left-to-right, the next level is right-to-left, and direction alternates until
 * all levels are processed.
 *
 * Leetcode: n/a (GeeksforGeeks spiral traversal variant)
 * Rating:   not available
 * Pattern:  Trees | DFS/BFS traversal | Two stacks | Alternating level order
 *
 * Example:
 *   Input:  root = [1,2,3,7,6,5,4]
 *   Output: [1, 3, 2, 7, 6, 5, 4]
 *   Why:    level 0 is left-to-right, level 1 is right-to-left, and level 2 flips back.
 *
 * Follow-ups:
 *   1. Can this be done with one queue?
 *      Yes, collect each level and reverse alternate levels.
 *   2. How would you stream output without reversing lists?
 *      Use two stacks as this implementation does.
 *   3. How would you return separate levels?
 *      Start a new list for each stack-draining phase instead of one flat result.
 *
 * Related: Binary Tree Zigzag Level Order Traversal (103).
 */

public class SpiralTraversal {

        public static void main(String[] args) {
        Node root = new Node(1);
        root.right = new Node(2);
        root.right.right = new Node(3);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,null,2,null,3]", spiralTraversal(root), "[1, 2, 3]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", spiralTraversal(null), "[]");
    }


        /**
     * Intuition: the restored original uses two stacks and alternates which
     * stack is drained. stack1 drains a level while pushing right then left into
     * stack2; stack2 drains the next level while pushing left then right back
     * into stack1.
     *
     * Algorithm:
     *   1. Return an empty result for a null root.
     *   2. Push root into stack1.
     *   3. Drain stack1, adding node values and pushing right then left into stack2.
     *   4. Drain stack2, adding node values and pushing left then right into stack1.
     *   5. Repeat until both stacks are empty.
     *
     * Time:  O(n) - each node is pushed and popped once.
     * Space: O(w) - stacks store nodes from the current frontier.
     *
     * @param root root of the binary tree
     * @return flattened spiral traversal order
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
