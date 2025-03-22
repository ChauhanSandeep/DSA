package Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Diagonal Traversal of a binary tree:
 * Given a binary tree, we perform a diagonal traversal where we traverse the tree 
 * diagonally starting from the root node and moving towards the right child nodes.
 * 
 * Intuition:
 * - Diagonal traversal means grouping nodes that lie in the same diagonal, 
 *   where each diagonal consists of nodes that have the same difference in their row and column index.
 * - For each diagonal, we start at the root and traverse down to the right, 
 *   adding nodes to a list until we reach a null node.
 * 
 * Algorithm:
 * 1. Start with the root node, and for each node, explore its left child by moving diagonally down.
 * 2. Move right and add all nodes to the respective diagonal list.
 * 3. Recursively traverse the left and right children to fill the diagonal lists.
 * 
 * Time Complexity:
 * - The time complexity is O(n), where n is the number of nodes in the tree, because each node is visited once.
 * 
 * Space Complexity:
 * - The space complexity is O(n) due to the storage required for the list of diagonals.
 * 
 * InterviewBit Link:
 * https://www.interviewbit.com/problems/diagonal-traversal/
 */
public class DiagonalTraversal {

    /**
     * Main method to test the diagonal traversal on a sample binary tree.
     */
    public static void main(String[] args) {
        TreeNode root = new TreeNode(8);
        root.left = new TreeNode(3);
        root.right = new TreeNode(10);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(6);
        root.right.right = new TreeNode(14);
        root.right.right.left = new TreeNode(13);
        root.left.right.left = new TreeNode(4);
        root.left.right.right = new TreeNode(7);
        
        // Perform diagonal traversal on the tree
        DiagonalTraversal treeTraversal = new DiagonalTraversal();
        ArrayList<Integer> result = treeTraversal.solve(root);
        
        // Print the diagonal traversal result
        System.out.println(result);
    }

    /**
     * Returns a list of values representing the diagonal traversal of the binary tree.
     * 
     * @param root The root of the binary tree.
     * @return A list of integers representing the diagonal traversal order.
     */
    public ArrayList<Integer> solve(TreeNode root) {
        // Edge case: if the root is null, return an empty list
        if (root == null) return new ArrayList<>();
        
        // List to store nodes in each diagonal (indexed by diagonal level)
        List<List<Integer>> diagonals = new ArrayList<>();
        
        // Perform the diagonal traversal
        traverse(root, 0, diagonals);
        
        // Flatten the list of diagonals into a single result list
        ArrayList<Integer> result = new ArrayList<>();
        for (List<Integer> diagonal : diagonals) {
            result.addAll(diagonal);
        }
        return result;
    }

    /**
     * Recursively traverses the tree and populates the diagonals list with nodes at each diagonal level.
     * 
     * @param node       The current node being visited.
     * @param diagonal  The current diagonal index.
     * @param diagonals The list storing nodes for each diagonal.
     */
    private void traverse(TreeNode node, int diagonal, List<List<Integer>> diagonals) {
        // Base case: return if the node is null
        if (node == null) return;
        
        // Ensure the diagonal list exists for the current diagonal
        while (diagonals.size() <= diagonal) {
            diagonals.add(new ArrayList<>());
        }
        
        // Add the current node's value to the appropriate diagonal list
        diagonals.get(diagonal).add(node.val);
        
        // Recursively traverse the left child (move to the next diagonal level)
        traverse(node.left, diagonal + 1, diagonals);
        
        // Recursively traverse the right child (stay on the same diagonal)
        traverse(node.right, diagonal, diagonals);
    }
}
