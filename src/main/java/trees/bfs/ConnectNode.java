package trees.bfs;

import trees.Node;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Populating Next Right Pointers in Each Node
 *
 * Given a binary tree node type with a nextRight pointer, connect every node to
 * its immediate neighbor on the same level. The last node on each level should
 * keep nextRight as null.
 *
 * Leetcode: https://leetcode.com/problems/populating-next-right-pointers-in-each-node/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | BFS | Level-order traversal | Queue by level size
 *
 * Example:
 *   Input:  root = [1,2,3]
 *   Output: level links 2 -> 3 and 3 -> null
 *   Why:    nodes 2 and 3 are adjacent on the same level, while root has no peer.
 *
 * Follow-ups:
 *   1. Can this be solved with O(1) extra space for a perfect tree?
 *      Walk already-created next pointers level by level.
 *   2. What changes for an arbitrary binary tree?
 *      Track the next level's head and tail while scanning current next pointers.
 *   3. How would you print all levels using nextRight?
 *      Start from each level's leftmost node and follow nextRight until null.
 */
public class ConnectNode {

        public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        connect(root);
        boolean linkedLevel = root.nextRight == null
            && root.left.nextRight == root.right
            && root.right.nextRight == null;

        Node single = new Node(7);
        connect(single);
        boolean linkedSingle = single.nextRight == null;

        System.out.printf("root=%s -> %s  expected=%s%n", "[1,2,3]", linkedLevel, true);
        System.out.printf("root=%s -> %s  expected=%s%n", "[7]", linkedSingle, true);
    }


        /**
     * Intuition: nodes that should be connected are exactly the nodes removed
     * from the queue during the same BFS level. queue.peek() is the next node on
     * that level before children for later levels are added.
     *
     * Algorithm:
     *   1. Put root in a queue and process one level at a time.
     *   2. For every node except the last at that level, set nextRight to queue.peek().
     *   3. Enqueue left and right children for the next level.
     *
     * Time:  O(n) - each node is dequeued once.
     * Space: O(w) - the queue stores at most one level of nodes.
     *
     * @param root root of the binary tree whose nextRight links are filled
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
