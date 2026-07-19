package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Diagonal Traversal of Binary Tree
 *
 * Group tree nodes by diagonal and return the groups from top-right to
 * bottom-left as one flattened list. Moving right stays on the same diagonal;
 * moving left advances to the next diagonal.
 *
 * Leetcode: n/a (InterviewBit diagonal traversal)
 * Rating:   not available
 * Pattern:  Trees | DFS | Diagonal index | Flatten grouped traversal
 *
 * Example:
 *   Input:  root = [1,2,3]
 *   Output: [1, 3, 2]
 *   Why:    root 1 and right child 3 share diagonal 0, while left child 2 is on diagonal 1.
 *
 * Follow-ups:
 *   1. Can this be implemented with a queue?
 *      Push left children as starts of later diagonals while walking right chains.
 *   2. How would you return separate diagonal groups?
 *      Return diagonals directly instead of flattening them at the end.
 *   3. How would anti-diagonal traversal change?
 *      Swap the roles of left and right when updating the diagonal index.
 */
public class DiagonalTraversal {

    public static void main(String[] args) {
        DiagonalTraversal solver = new DiagonalTraversal();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,2,3]", solver.solve(root), "[1, 3, 2]");
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[]", solver.solve(null), "[]");
    }


        /**
     * Intuition: a left edge moves one diagonal farther from the top-right view,
     * while a right edge stays on the same diagonal. traverse records each value
     * in diagonals[diagonalNumber], then solve flattens those groups in order.
     *
     * Algorithm:
     *   1. Return an empty list for a null root.
     *   2. DFS with diagonalNumber starting at zero.
     *   3. Create a diagonal bucket before adding a node to it.
     *   4. Recurse left with diagonalNumber + 1 and right with diagonalNumber.
     *   5. Flatten the diagonal buckets into the result list.
     *
     * Time:  O(n) - each node is added once and flattened once.
     * Space: O(n) - diagonal storage plus recursion stack.
     *
     * @param root root of the binary tree
     * @return flattened diagonal traversal order
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

        // Places node values into buckets according to their diagonal number.
    private void traverse(TreeNode node, int diagonalNumber, List<List<Integer>> diagonals) {
        // Base case: return if the node is null
        if (node == null) return;

        // Ensure the diagonal list exists for the current diagonal
        while (diagonals.size() <= diagonalNumber) {
            diagonals.add(new ArrayList<>());
        }

        // Add the current node's value to the appropriate diagonal list
        diagonals.get(diagonalNumber).add(node.val);

        // Recursively traverse the left child (move to the next diagonal level)
        traverse(node.left, diagonalNumber + 1, diagonals);

        // Recursively traverse the right child (stay on the same diagonal)
        traverse(node.right, diagonalNumber, diagonals);
    }
}
