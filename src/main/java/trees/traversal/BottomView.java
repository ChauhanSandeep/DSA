package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Bottom View of Binary Tree
 *
 * Return the nodes visible when a binary tree is viewed from below. Nodes share
 * a vertical column by horizontal distance; the bottom view keeps the last node
 * encountered for each column during level-order traversal.
 *
 * Leetcode: n/a (GeeksforGeeks bottom view of binary tree)
 * Rating:   not available
 * Pattern:  Trees | BFS | Horizontal distance | TreeMap ordering
 *
 * Example:
 *   Input:  root = [1,2,3]
 *   Output: [2, 1, 3]
 *   Why:    columns -1, 0, and 1 contain nodes 2, 1, and 3 respectively.
 *
 * Follow-ups:
 *   1. How would you compute the top view instead?
 *      Only write a column the first time it appears in BFS.
 *   2. How would you break ties by depth and value?
 *      Store row and value with each column entry and compare explicitly.
 *   3. Can DFS compute bottom view?
 *      Track depth for each horizontal distance and replace only with deeper nodes.
 *
 * Related: Vertical Order Traversal, Top View of Binary Tree.
 */
public class BottomView {
        public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,2,3]", getBottomView(root), "[2, 1, 3]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", getBottomView(null), "[]");
    }


        /**
     * Intuition: BFS visits shallower nodes before deeper nodes. Replacing the
     * value for a horizontal distance each time it is seen leaves the deepest,
     * latest node for that column, and TreeMap emits columns from left to right.
     *
     * Algorithm:
     *   1. Return an empty list for a null root.
     *   2. BFS nodes with their horizontalDistance, starting root at zero.
     *   3. Overwrite bottomViewMap[horizontalDistance] with each visited value.
     *   4. Enqueue left with distance - 1 and right with distance + 1.
     *   5. Return the TreeMap values in sorted column order.
     *
     * Time:  O(n log n) - each node may update a TreeMap column.
     * Space: O(n) - the map and queue can store many nodes.
     *
     * @param root root of the binary tree
     * @return bottom-view values from leftmost column to rightmost column
     */
    private static List<Integer> getBottomView(TreeNode root) {
        if (root == null) return Collections.emptyList();

        // TreeMap to maintain the last node at each horizontal distance (hd)
        Map<Integer, Integer> bottomViewMap = new TreeMap<>(); // hd -> node value
        Queue<NodeWithHorizontalDistance> queue = new LinkedList<>();

        // Initialize root node with horizontal distance 0
        queue.offer(new NodeWithHorizontalDistance(root, 0));

        while (!queue.isEmpty()) {
            NodeWithHorizontalDistance current = queue.poll();
            TreeNode node = current.node;
            int horizontalDistance = current.horizontalDistance;

            // Update the map with the last seen node at this horizontal distance
            bottomViewMap.put(horizontalDistance, node.val);

            // Enqueue left child with horizontalDistance - 1
            if (node.left != null) {
                queue.offer(new NodeWithHorizontalDistance(node.left, horizontalDistance - 1));
            }
            // Enqueue right child with horizontalDistance + 1
            if (node.right != null) {
                queue.offer(new NodeWithHorizontalDistance(node.right, horizontalDistance + 1));
            }
        }

        // Collect the bottom view nodes in order
        return new ArrayList<>(bottomViewMap.values());
    }
}

/**
 * Helper class to associate a node with its horizontal distance (hd).
 */
class NodeWithHorizontalDistance {
    TreeNode node;
    int horizontalDistance;

    public NodeWithHorizontalDistance(TreeNode node, int horizontalDistance) {
        this.node = node;
        this.horizontalDistance = horizontalDistance;
    }
}
