package trees;

import java.util.ArrayList;

/**
 * Given a binary tree, this class finds all the nodes that are at a distance `K` from a target node.
 * The tree is traversed to locate the target node, and from there, nodes at the specified distance `K` are found.
 *
 * Leetcode link: https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/
 *
 * For example:
 *         3
 *        /  \
 *       5    1
 *      / \   / \
 *     6   2  0   8
 *        / \
 *       7   4
 *       If the target node is 5 and K is 2, the output will be [7, 4, 1].
 */
public class KDistance {

    private ArrayList<Integer> result;

    public static void main(String[] args) {
        // Constructing the example tree:
        //        0
        //         \
        //          1
        //           \
        //            2
        //             \
        //              3
        TreeNode root = new TreeNode(0);
        root.right = new trees.TreeNode(1);
        root.right.right = new TreeNode(2);
        root.right.right.right = new TreeNode(3);

        // Calling the method with target node value 1 and distance 2
        KDistance kDistance = new KDistance();
        ArrayList<Integer> nodesAtDistanceK = kDistance.distanceK(root, 1, 2);

        // Output the nodes at distance 2 from node with value 1
        System.out.println(nodesAtDistanceK);
    }

    /**
     * Algorithm:
     * 1. Perform a depth-first search (DFS) to find the target node.
     * 2. Once the target is found, call a helper function (`markChild`) to explore nodes at distance `K`.
     * 3. Explore both the left and right subtrees while also considering the parent node by traversing upwards.
     *
     * Time Complexity:
     * - Finding the target and marking nodes at distance `K` takes O(N), where N is the number of nodes in the binary tree.
     *
     * Space Complexity:
     * - The space complexity is O(H), where H is the height of the binary tree due to recursive calls on the stack.
     *
     * @param root The root node of the binary tree.
     * @param target The value of the target node.
     * @param distance The distance `K` from the target node.
     * @return A list of node values that are at distance `K` from the target node.
     */
    public ArrayList<Integer> distanceK(TreeNode root, int target, int distance) {
        this.result = new ArrayList<>();
        findNodes(root, target, distance);
        return result;
    }

    /**
     * A helper function that recursively searches for the target node in the binary tree and
     * calls `markChild` to find nodes at the required distance.
     *
     * @param node The current node being explored in the binary tree.
     * @param target The value of the target node.
     * @param distance The distance `K` from the target node.
     * @return The distance of the target node from the current node. Returns -1 if the target is not found.
     */
    private int findNodes(TreeNode node, int target, int distance) {
        if (node == null) return -1; // If current node is null, return -1.

        // If the current node is the target node, start marking children at distance K
        if (node.val == target) {
            markChild(node, distance, 0); // Start marking from the target node.
            return 1; // Return 1 to signify that we've found the target node.
        }

        // Search in the left subtree
        int left = findNodes(node.left, target, distance);

        // If the target is found in the left subtree and is within the distance
        if (left > 0 && left <= distance) {
            if (left == distance) {
                result.add(node.val); // Add the current node to the result if it is at distance K
                return left + 1; // Return the distance from the current node to its parent
            }
            // Otherwise, continue searching in the right subtree
            markChild(node.right, distance, left + 1);
            return left + 1;
        }

        // Search in the right subtree
        int right = findNodes(node.right, target, distance);

        // If the target is found in the right subtree and is within the distance
        if (right > 0 && right <= distance) {
            if (right == distance) {
                result.add(node.val); // Add the current node to the result if it is at distance K
                return right + 1;
            }
            // Otherwise, continue searching in the left subtree
            markChild(node.left, distance, right + 1);
            return right + 1;
        }

        return -1; // Return -1 if the target node is not found in this subtree.
    }

    /**
     * A helper function that marks all nodes at a certain distance starting from the given node.
     *
     * @param node The current node being explored.
     * @param distance The distance `K` from the target node.
     * @param curr The current distance from the target node.
     */
    public void markChild(TreeNode node, int distance, int curr) {
        if (node == null || curr > distance) return; // Stop if we reach a null node or exceed the distance.

        // If the current distance matches the target distance, add the node value to the result
        if (curr == distance) result.add(node.val);

        // Recur for both left and right children
        markChild(node.left, distance, curr + 1);
        markChild(node.right, distance, curr + 1);
    }

}


