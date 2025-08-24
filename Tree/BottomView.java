package Tree;

import java.util.*;

/**
 * Computes the Bottom View of a Binary Tree.
 *
 * The Bottom View consists of the last visible nodes when the tree is viewed from the bottom.
 *
 *          20
 *         /  \
 *       8     22
 *      / \    / \
 *     5   3  4  25
 *        / \
 *      10  14
 *
 *  output : [5, 10, 3, 4, 25]
 *
 * **Approach:**
 * - Perform a level-order traversal using a queue (BFS).
 * - Maintain a TreeMap to store the last encountered node for each horizontal distance (hd).
 * - Store nodes in a map where the key is `hd` and the value is the last node encountered at that `hd`.
 * - Extract values from the TreeMap to get the final bottom view.
 *
 * **Time Complexity:** O(N) - We visit each node once in BFS.
 * **Space Complexity:** O(N) - For storing nodes in a queue and map.
 *
 * **LeetCode Link:** https://leetcode.com/problems/binary-tree-bottom-view/ (similar problem)
 */
public class BottomView {
    public static void main(String[] args) {
        // Constructing the binary tree
        TreeNode root = new TreeNode(20);
        root.left = new TreeNode(8);
        root.right = new TreeNode(22);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(25);
        root.left.right.left = new TreeNode(10);
        root.left.right.right = new TreeNode(14);

        // Get and print bottom view
        List<Integer> bottomView = getBottomView(root);
        System.out.println(bottomView);
    }

    /**
     * Computes the bottom view of a binary tree using BFS.
     *
     * @param root The root node of the tree.
     * @return A list containing the bottom view of the tree.
     */
    private static List<Integer> getBottomView(TreeNode root) {
        if (root == null) return Collections.emptyList();

        // TreeMap to maintain the last node at each horizontal distance (hd)
        Map<Integer, Integer> bottomViewMap = new TreeMap<>();
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
