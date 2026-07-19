package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Problem: Left View of Binary Tree
 *
 * Return the nodes visible when the tree is viewed from the left side. That is
 * the first node encountered at each depth when levels are scanned from left to
 * right.
 *
 * Leetcode: n/a (GeeksforGeeks left view of binary tree)
 * Rating:   not available
 * Pattern:  Trees | BFS | Level-order traversal | First node per level
 *
 * Example:
 *   Input:  root = [10,2,3,7,8,12,15,null,null,null,null,null,null,14]
 *   Output: [10, 2, 7, 14]
 *   Why:    those are the first nodes reached at levels 0 through 3.
 *
 * Follow-ups:
 *   1. How would you compute the right view?
 *      Capture the last node at each BFS level.
 *   2. Can DFS compute the left view?
 *      Traverse left before right and record the first value seen at each depth.
 *   3. How would you return both views together?
 *      Store first and last node from each BFS level.
 */
public class LeftView {

        public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(7);
        root.left.right = new TreeNode(8);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(15);
        root.right.right.left = new TreeNode(14);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[10,2,3,7,8,12,15,null,null,null,null,null,null,14]", printLeftView(root), "[10, 2, 7, 14]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", printLeftView(null), "[]");
    }


        /**
     * Intuition: the left view is the first node removed from each BFS level.
     * Because children are enqueued left before right, index 0 in every levelSize
     * loop is the visible node for that depth.
     *
     * Algorithm:
     *   1. Return an empty list for a null root.
     *   2. Queue root and process one level at a time.
     *   3. Add node.val when i == 0 for the current level.
     *   4. Enqueue left child before right child for the next level.
     *
     * Time:  O(n) - each node is processed once.
     * Space: O(w) - queue size is bounded by the maximum width.
     *
     * @param root root of the binary tree
     * @return left-view values from top to bottom
     */
    public static List<Integer> printLeftView(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;  // Return empty list if tree is empty
        }

        // Initialize a queue for level-order traversal (BFS)
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // Perform level-order traversal
        while (!queue.isEmpty()) {
            int size = queue.size();  // Get the number of nodes at the current level

            // Traverse all nodes at the current level
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();  // Get the current node

                // If it's the first node at this level, add it to the result
                if (i == 0) {
                    result.add(node.val);
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
