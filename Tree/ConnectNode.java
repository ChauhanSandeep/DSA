package Tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * **Connect Nodes at the Same Level in a Binary Tree**
 * 
 * The problem requires connecting all nodes at the same level in a binary tree.
 * Specifically, each node should point to its next right node at the same level.
 * If there is no next right node, the pointer should be set to `null`.
 * 
 * **Approach:**
 * - Perform **level-order traversal** using a queue.
 * - For each level, set the `nextRight` pointer of each node to the next node in the same level.
 * - This is done by using a single queue where each level is processed in sequence.
 * 
 * **Time Complexity:** **O(N)** (Each node is processed once)
 * **Space Complexity:** **O(N)** (Space for queue, at worst when the last level is in the queue)
 */
public class ConnectNode {

    public static void main(String[] args) {
        /*
                10
               /  \
             8     2
            /
           3
        */

        Node root = new Node(10);
        root.left = new Node(8);
        root.right = new Node(2);
        root.left.left = new Node(3);

        connect(root);  // Connect nodes at the same level.
        printNextRightNodes(root);  // Print connected nodes for verification.
    }

    /**
     * Connects nodes at the same level in a binary tree using level-order traversal.
     * 
     * @param root The root node of the binary tree.
     */
    public static void connect(Node root) {
        if (root == null) return; // If the tree is empty, return immediately.

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size(); // Number of nodes at the current level.

            // Process each node at the current level.
            for (int i = 0; i < levelSize; i++) {
                Node currentNode = queue.poll();

                // Set the nextRight pointer for the current node, if it is not the last node at this level.
                if (i < levelSize - 1) {
                    currentNode.nextRight = queue.peek();
                }

                // Add left and right children of the current node to the queue.
                if (currentNode.left != null) {
                    queue.offer(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.offer(currentNode.right);
                }
            }
        }
    }

    /**
     * Helper method to print nextRight pointers of nodes.
     * For verification, it prints the value of the node and the nextRight pointer value.
     * 
     * @param root The root node of the binary tree.
     */
    public static void printNextRightNodes(Node root) {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            System.out.println("Node " + currentNode.data + " -> NextRight: " + 
                (currentNode.nextRight != null ? currentNode.nextRight.data : "null"));

            if (currentNode.left != null) {
                queue.offer(currentNode.left);
            }
            if (currentNode.right != null) {
                queue.offer(currentNode.right);
            }
        }
    }

    /**
     * Node structure representing each node in the binary tree.
     */
    static class Node {
        int data;
        Node left, right, nextRight;

        public Node(int data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.nextRight = null;
        }
    }
}
