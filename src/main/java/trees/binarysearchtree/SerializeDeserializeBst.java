package trees.binarysearchtree;

import trees.TreeNode;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Serialize and Deserialize a binary tree.
 *
 * Intuition:
 * The goal is to convert a binary tree into a string representation that can be stored
 * and later restored back into the original tree structure.
 * We use a level-order traversal (breadth-first traversal) for both serialization
 * and deserialization. For serialization, we capture the values of nodes at each
 * level, using "null" to represent missing children. For deserialization, we rebuild
 * the tree from the string by using the same level-order approach, constructing each
 * node and assigning its left and right children.
 *            1
 *          /   \
 *         2     3
 *        / \   / \
 *      null 4  5  6
 *             / \
 *            7   8
 *
 *      Serialized : [1, 2, 3, null, 4, 5, 6, null, null, 7, 8, null, null]
 *
 * Algorithm:
 * 1. **Serialize**:
 * - Perform a level-order traversal (BFS) of the binary tree.
 * - For each node, append its value to a string. If a node is `null`, append "null".
 * 2. **Deserialize**:
 * - Split the serialized string into an array of node values.
 * - Rebuild the tree from the array by using a queue to manage node construction.
 *
 * Time Complexity: O(N), where N is the number of nodes in the tree (since we visit each node once for both serialization and deserialization).
 * Space Complexity: O(N) for storing the serialized string and the queue during deserialization.
 *
 * LeetCode Link: https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */

public class SerializeDeserializeBst {

    /**
     * Serializes a binary tree into a string representation.
     * Approach:
     * - Use level-order traversal (BFS) to visit each node.
     * - Append the value of each node to a string.
     * - Use "null" to represent missing children.
     *
     * @param root The root of the binary tree.
     * @return A string representing the serialized binary tree.
     */
    public String serialize(TreeNode root) {
        /**
         *            1
         *          /   \
         *         2     3
         *        / \   / \
         *      null 4  5  6
         *             / \
         *            7   8
         *
         *  Serialized : [1, 2, 3, null, 4, 5, 6, null, null, 7, 8, null, null]
         */
        if (root == null) return "null"; // Return "null" for an empty tree

        StringBuilder builder = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        // Perform level-order traversal (BFS)
        while (!queue.isEmpty()) {
            TreeNode curr = queue.poll();
            if (curr == null) {
                builder.append("null, "); // Append "null" for missing nodes
            } else {
                builder.append(curr.val).append(", ");
                queue.offer(curr.left);   // Add left child to the queue
                queue.offer(curr.right);  // Add right child to the queue
            }
        }

        // Remove the trailing comma and space
        builder.setLength(builder.length() - 2);
        return builder.toString();
    }

    /**
     * Deserializes a string into a binary tree.
     * Approach:
     * - Split the serialized string into an array of node values.
     * - Use a queue to reconstruct the tree in level-order.
     * - For each node, create its left and right children based on the array values.
     * - Return the root of the reconstructed tree.
     *
     * @param data A string representing the serialized binary tree.
     * @return The root node of the deserialized binary tree.
     */
    public TreeNode deserialize(String data) {
        /**
         * Serialized : [1, 2, 3, null, 4, 5, 6, null, null, 7, 8, null, null]
         *
         *            1
         *          /   \
         *         2     3
         *        / \   / \
         *      null 4  5  6
         *             / \
         *            7   8
         */
        if (data == null || data.trim().length() == 0 || data.equals("null")) return null;

        // Split the data into an array of values
        String[] nodes = data.split(", ");
        Queue<TreeNode> queue = new LinkedList<>();

        // Create the root of the tree
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        queue.offer(root);

        int index = 1;

        // Perform level-order traversal to reconstruct the tree
        while (!queue.isEmpty()) {
            TreeNode currNode = queue.poll();

            // Get the left child
            String leftVal = nodes[index++];
            if (!leftVal.equals("null")) {
                TreeNode leftNode = new TreeNode(Integer.parseInt(leftVal));
                currNode.left = leftNode;
                queue.offer(leftNode);
            }

            // Get the right child
            String rightVal = nodes[index++];
            if (!rightVal.equals("null")) {
                TreeNode rightNode = new TreeNode(Integer.parseInt(rightVal));
                currNode.right = rightNode;
                queue.offer(rightNode);
            }
        }
        return root;
    }

    /**
     * -- DFS-based Approach --
     * Serializes a binary tree into a string using pre-order traversal.
     * Approach:
     * - Use pre-order traversal (DFS) to visit each node.
     * - Append the value of each node to a string.
     * - Use "null" to represent missing children.
     * Example:   
     *  
     *           1
     *          /   \
     *         2     3
     *        / \   / \
     *      null 4  5  6
     *             / \
     *            7   8
     *
     * Serialized : [1,2,null,4,null,null,3,5,7,null,null,8,null,null,6,null,null]
     */
    public String serialize_UsingDfsApproach(TreeNode root) {
        StringBuilder builder = new StringBuilder();
        serializeRec(root, builder);
        String res = builder.toString();
        System.out.println(res);
        return res;
    }

    private void serializeRec(TreeNode node, StringBuilder builder) {
        if (node == null) {
            builder.append("null,");
            return;
        }
        builder.append(node.val).append(",");
        serializeRec(node.left, builder);
        serializeRec(node.right, builder);
    }

    /**
     * -- DFS-based Approach --
     * Deserializes a string into a binary tree using pre-order traversal.
     * Approach:
     * - Split the serialized string into an array of node values.
     * - Use a recursive helper function to rebuild the tree in pre-order.
     * - For each node, create its left and right children based on the array values.
     * - Return the root of the reconstructed tree.
     * Example:
     * Serialized : [1,2,null,4,null,null,3,5,7,null,null,8,null,null,6,null,null]
     */
    public TreeNode deserialize_UsingDfsApproach(String data) {
        if (data.isEmpty()) return null;
        String[] arr = data.split(",");
        int[] index = {0};
        return deserializeRec(arr, index);
    }

    private TreeNode deserializeRec(String[] arr, int[] index) {
        if (index[0] >= arr.length || "null".equals(arr[index[0]])) {
            index[0]++;
            return null;
        }

        TreeNode node = new TreeNode(Integer.parseInt(arr[index[0]]));
        index[0]++;
        node.left = deserializeRec(arr, index);
        node.right = deserializeRec(arr, index);
        return node;
    }
}
