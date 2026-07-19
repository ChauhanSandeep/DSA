package trees.traversal;

import trees.Node;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Problem: Vertical Order Traversal of Binary Tree
 *
 * Group nodes by horizontal distance from the root and return columns from left
 * to right. This implementation uses BFS, so nodes in the same column appear in
 * level-order encounter order.
 *
 * Leetcode: https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/ (Hard)
 * Rating:   1676
 * Pattern:  Trees | BFS | Horizontal distance | TreeMap columns
 *
 * Example:
 *   Input:  root = [1,2,3,4,5,6,7]
 *   Output: [4, 2, 1, 5, 6, 3, 7]
 *   Why:    columns are hd -2, -1, 0, 1, and 2 from left to right.
 *
 * Follow-ups:
 *   1. How does Leetcode 987's ordering differ?
 *      It also sorts ties by row and value, so a priority structure is needed.
 *   2. Can DFS produce vertical order?
 *      Yes, pass horizontal distance recursively and sort or preserve row data as needed.
 *   3. How would you return columns as lists?
 *      Return the TreeMap values without flattening them.
 *
 * Related: Binary Tree Vertical Order Traversal (314), Bottom View of Binary Tree.
 */
public class VerticalTraversal {

        public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,2,3,4,5,6,7]", verticalOrder(root), "[4, 2, 1, 5, 6, 3, 7]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", verticalOrder(null), null);
    }


        /**
     * Intuition: horizontal distance identifies a vertical column: left child is
     * one column left, right child is one column right. BFS preserves top-to-bottom
     * encounter order inside each column, and TreeMap keeps columns sorted.
     *
     * Algorithm:
     *   1. Return null for a null root, matching the original method contract.
     *   2. BFS IndexNode entries containing a node and its horizontal distance.
     *   3. Append each value to map[hd].
     *   4. Enqueue left child with hd - 1 and right child with hd + 1.
     *   5. Flatten TreeMap columns from smallest hd to largest.
     *
     * Time:  O(n log n) - each node may update a TreeMap column.
     * Space: O(n) - queue and map store traversal state.
     *
     * @param root root of the binary tree
     * @return flattened vertical order, or null for an empty tree
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
