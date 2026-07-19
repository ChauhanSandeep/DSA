package trees.binarysearchtree;

import trees.TreeNode;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Serialize and Deserialize Binary Tree
 *
 * Convert a binary tree to a string and rebuild the same tree. This file keeps a
 * level-order codec as the primary API and also includes a DFS preorder variant.
 *
 * Leetcode: https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BFS codec | Queue-based reconstruction
 *
 * Example:
 *   Input:  root = [1,2,3,null,4]
 *   Output: "1, 2, 3, null, 4, null, null, null, null"
 *   Why:    BFS records each level and null markers preserve missing child slots.
 *
 * Follow-ups:
 *   1. How would you remove redundant trailing nulls?
 *      Trim them during serialization and guard indexes during deserialization.
 *   2. How would you serialize a BST more compactly?
 *      Store preorder only and rebuild using BST value bounds.
 *   3. How would you make the format robust to commas in values?
 *      Use length-prefixed tokens or escaping.
 *   4. How would you stream very large trees?
 *      Serialize level chunks and deserialize from an iterator of tokens.
 *
 * Related: Serialize and Deserialize BST (449), Binary Tree Level Order Traversal (102).
 */

public class SerializeDeserializeBst {

    public static void main(String[] args) {
        SerializeDeserializeBst codec = new SerializeDeserializeBst();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(4);

        String serialized = codec.serialize(root);
        TreeNode restored = codec.deserialize(serialized);
        System.out.printf("root=[1,2,3,null,4] -> %s  expected=1, 2, 3, null, 4, null, null, null, null%n",
            serialized);
        System.out.printf("roundTripRoot -> %d  expected=1%n", restored.val);
        System.out.printf("root=[] -> %s  expected=null%n", codec.serialize(null));
    }


        /**
     * Intuition: level order treats the tree like an array layout. Real nodes enqueue
     * both children, and null tokens mark missing positions so deserialize can attach
     * children to the correct parent later.
     *
     * Algorithm:
     *   1. Return "null" for an empty root.
     *   2. BFS through a queue, appending node values or null markers.
     *   3. Enqueue left and right children for every non-null node.
     *   4. Remove the trailing comma and space before returning.
     *
     * Time:  O(n) - every real node and emitted null child position is processed once.
     * Space: O(n) - queue and serialized string grow with the tree width and size.
     *
     * @param root root of the tree to serialize
     * @return level-order string with null markers
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
     * Intuition: the BFS string lists children in the same order parents are removed
     * from a queue. For each queued parent, the next two tokens are exactly its left
     * and right children.
     *
     * Algorithm:
     *   1. Return null for blank or "null" data.
     *   2. Split tokens and create the root from the first token.
     *   3. Poll parents from a queue and consume the next left and right tokens.
     *   4. Attach non-null children and enqueue them for later expansion.
     *
     * Time:  O(n) - each token is read once.
     * Space: O(n) - token array plus queue.
     *
     * @param data level-order string with null markers
     * @return reconstructed tree root
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
     * Appends preorder DFS tokens for node, including null markers.
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
     * Consumes preorder DFS tokens using index as a shared cursor.
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
