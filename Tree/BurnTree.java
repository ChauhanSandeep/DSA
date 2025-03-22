package Tree;

/**
 * **Burning a Binary Tree from a Leaf Node**
 * 
 * Given a binary tree and a leaf node, the fire spreads to all connected nodes (parent, left, right)
 * every second. The goal is to determine the minimum time required to completely burn the tree.
 * 
 * **Approach:**
 * - Perform a postorder traversal to find the leaf node.
 * - As we return back, calculate the depth at which the leaf node is found.
 * - Keep track of the farthest node burning using a **global max time counter**.
 * - Burn child nodes on the opposite subtree after encountering the burning leaf.
 * 
 * **Time Complexity:** **O(N)** (visiting each node once)
 * **Space Complexity:** **O(H)** (recursion stack, worst case O(N) for skewed tree)
 */
public class BurnTree {
    
    private int maxBurnTime = 0; // Tracks the maximum time taken to burn the tree

    public static void main(String[] args) {
        /*
                5
               /  \
             4      6
            / \      \
           3  14       7
                      / \
                     9   8
        */
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.right = new TreeNode(14);
        root.left.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);

        int leafNode = 14;
        System.out.println("Time required to burn the tree: " + new BurnTree().burnTree(root, leafNode));
    }

    /**
     * Computes the minimum time required to burn the entire tree starting from a given leaf node.
     * 
     * @param root The root of the binary tree.
     * @param leafValue The value of the leaf node where the fire starts.
     * @return The minimum time required to burn the entire tree.
     */
    public int burnTree(TreeNode root, int leafValue) {
        findBurningNode(root, leafValue);
        return maxBurnTime;
    }

    /**
     * Recursively finds the burning node and calculates burning times.
     * 
     * @param node Current node in traversal.
     * @param leafValue The target leaf node from which the fire starts.
     * @return The depth of the burning node if found, otherwise -1.
     */
    private int findBurningNode(TreeNode node, int leafValue) {
        if (node == null) return -1;

        if (node.val == leafValue) return 0; // Found the leaf node, fire starts here

        // Recurse into left and right subtrees
        int leftDepth = findBurningNode(node.left, leafValue);
        if (leftDepth != -1) { // If leaf found in left subtree
            maxBurnTime = Math.max(maxBurnTime, leftDepth + 1);
            propagateFire(node.right, leftDepth + 1); // Burn right subtree
            return leftDepth + 1;
        }

        int rightDepth = findBurningNode(node.right, leafValue);
        if (rightDepth != -1) { // If leaf found in right subtree
            maxBurnTime = Math.max(maxBurnTime, rightDepth + 1);
            propagateFire(node.left, rightDepth + 1); // Burn left subtree
            return rightDepth + 1;
        }

        return -1; // Leaf node not found in this path
    }

    /**
     * Burns all child nodes in a subtree at increasing time intervals.
     * 
     * @param node Current node in the opposite subtree.
     * @param time Time taken for fire to reach this node.
     */
    private void propagateFire(TreeNode node, int time) {
        if (node == null) return;
        
        maxBurnTime = Math.max(maxBurnTime, time + 1);
        propagateFire(node.left, time + 1);
        propagateFire(node.right, time + 1);
    }
}

/**
 * Basic structure for a TreeNode.
 */
class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int value) {
        this.val = value;
        this.left = null;
        this.right = null;
    }
}
