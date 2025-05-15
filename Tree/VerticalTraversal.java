package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * This class provides methods for performing a vertical order traversal of a binary tree.
 *
 * Intuition:
 * - A vertical order traversal groups nodes that are aligned vertically along the same
 *   vertical line in the tree. Each node's horizontal distance (hd) from the root is tracked
 *   during the traversal. Nodes with the same horizontal distance are considered in the same vertical column.
 * - There are two ways to implement this traversal:
 *   1. Level order traversal using a queue (Breadth-First Search).
 *   2. Recursion, using the horizontal distance to group nodes.
 *
 * Algorithm:
 * - **Method 1 (Using Queue and Level Order Traversal)**:
 *   - Perform a BFS starting from the root.
 *   - Track the horizontal distance (hd) of each node and store them in a map.
 *   - Finally, traverse the map in order of hd to get the vertical order.
 *
 * - **Method 2 (Using Recursion)**:
 *   - Perform DFS traversal and track the horizontal distance of each node.
 *   - Use a map to store nodes based on their horizontal distance, and print them accordingly.
 *
 * Time Complexity: O(n) - Both methods visit every node once.
 * Space Complexity: O(n) - Storing nodes in the map.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/
 */
public class VerticalTraversal {

    public static void main(String[] args) {
        // Example usage: Construct a binary tree and perform vertical order traversal
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        root.right.left.right = new Node(8);
        root.right.right.right = new Node(9);

        System.out.println("Vertical Order traversal is:");
        System.out.println(verticalOrder(root)); // Expected output based on tree structure
    }

    /**
     * Performs vertical order traversal of a binary tree using level order traversal (BFS).
     *
     * @param root The root node of the binary tree.
     * @return A list of integers representing the vertical order traversal of the tree.
     */
    static ArrayList<Integer> verticalOrder(Node root) {
        if (root == null) return null;

        // Queue for BFS: Stores IndexNode, which contains node and horizontal distance
        Queue<IndexNode> queue = new LinkedList<>();
        IndexNode indexRoot = new IndexNode(root); // Start from the root at horizontal distance 0
        queue.offer(indexRoot);

        // Map to store nodes grouped by their horizontal distance
        Map<Integer, List<Integer>> map = new TreeMap<>();

        while (!queue.isEmpty()) {
            IndexNode indexNode = queue.poll();

            // Add the node's data to the map at its horizontal distance (hd)
            map.computeIfAbsent(indexNode.hd, k -> new ArrayList<>()).add(indexNode.node.data);

            // Enqueue left and right child nodes with updated horizontal distance
            if (indexNode.node.left != null) {
                queue.offer(new IndexNode(indexNode.node.left, indexNode.hd - 1));
            }
            if (indexNode.node.right != null) {
                queue.offer(new IndexNode(indexNode.node.right, indexNode.hd + 1));
            }
        }

        // Flatten the map into a result list
        ArrayList<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            result.addAll(entry.getValue());
        }

        return result;
    }

    /**
     * Prints the vertical order traversal of a binary tree using DFS and recursion.
     *
     * @param root The root node of the binary tree.
     */
    public static void printVerticalOrder(Node root) {
        Map<Integer, List<Integer>> map = new TreeMap<>();
        int hd = 0; // Starting horizontal distance for the root

        // Perform the recursive DFS traversal to populate the map
        printVerticalOrder(root, hd, map);

        // Print the vertical order traversal from the map
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    /**
     * A recursive helper method to perform DFS traversal and populate the map with vertical order.
     *
     * @param node The current node.
     * @param hd The horizontal distance of the current node.
     * @param map The map to store nodes by their horizontal distance.
     */
    public static void printVerticalOrder(Node node, int hd, Map<Integer, List<Integer>> map) {
        if (node == null) return;

        // Add the node's data to the map at the corresponding horizontal distance (hd)
        map.computeIfAbsent(hd, k -> new ArrayList<>()).add(node.data);

        // Recursively process the left and right subtrees with updated horizontal distance
        printVerticalOrder(node.left, hd - 1, map);
        printVerticalOrder(node.right, hd + 1, map);
    }
}

/**
 * IndexNode class to store a node along with its horizontal distance.
 */
class IndexNode {
    Node node;
    int hd;

    public IndexNode(Node node) {
        this.node = node;
        this.hd = 0; // Default horizontal distance for the root node
    }

    public IndexNode(Node node, int hd) {
        this.node = node;
        this.hd = hd;
    }
}
