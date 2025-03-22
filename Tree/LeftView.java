package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class provides a solution to print the left view of a binary tree.
 * The left view consists of the nodes visible when the tree is viewed from the left side.
 * 
 * Intuition:
 * - To determine the left view of a binary tree, we need to print the first node at each level (from top to bottom).
 * - This can be achieved by using a level-order traversal (BFS) and selecting the first node encountered at each level.
 * 
 * Algorithm:
 * 1. Use a queue to implement level-order traversal of the tree.
 * 2. For each level, capture the first node and add it to the result list.
 * 3. Continue the traversal until all levels are processed.
 * 
 * Time Complexity:
 * - O(N), where N is the number of nodes in the tree. Each node is processed once.
 * 
 * Space Complexity:
 * - O(W), where W is the width of the tree (the maximum number of nodes at any level). The queue will store up to W nodes at a time.
 */
public class LeftView {

    public static void main(String[] args) {
        // Constructing the binary tree:
        //        10
        //       /  \
        //      2    3
        //     / \    / \
        //    7   8  12  15
        //               /
        //             14
        Node root = new Node(10);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(7);
        root.left.right = new Node(8);
        root.right.right = new Node(15);
        root.right.left = new Node(12);
        root.right.right.left = new Node(14);

        // Print the left view of the tree
        List<Integer> leftNodes = printLeftView(root);
        System.out.println(leftNodes);  // Output: [10, 2, 7, 14]
    }

    /**
     * Prints the left view of the binary tree.
     * The left view consists of the nodes visible when the tree is viewed from the left side.
     * 
     * @param root The root node of the binary tree.
     * @return A list containing the left view of the binary tree.
     */
    public static List<Integer> printLeftView(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;  // Return empty list if tree is empty
        }

        // Initialize a queue for level-order traversal (BFS)
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        // Perform level-order traversal
        while (!queue.isEmpty()) {
            int size = queue.size();  // Get the number of nodes at the current level

            // Traverse all nodes at the current level
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();  // Get the current node

                // If it's the first node at this level, add it to the result
                if (i == 0) {
                    result.add(node.data);
                }

                // Add the left and right children of the current node to the queue for next level
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }

        return result;  // Return the left view nodes
    }
}

/**
 * Definition for a binary tree node.
 */
class Node {
    int data;
    Node left, right;

    Node(int data) {
        this.data = data;
        left = right = null;
    }
}
