package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Lowest Common Ancestor of a Binary Tree
 *
 * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 * According to the definition of LCA on Wikipedia: The lowest common ancestor is defined
 * between two nodes p and q as the lowest node in T that has both p and q as descendants
 * (where we allow a node to be a descendant of itself).
 *
 * Example 1:
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * Output: 3
 * Explanation: The LCA of nodes 5 and 1 is 3. Node 3 is the first common ancestor
 * when traversing up from both nodes. Node 5 is in the left subtree of 3, and node 1
 * is in the right subtree of 3.
 *
 * LeetCode Problem: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree
 *
 * Follow-up Questions:
 *
 * 1. What if nodes might not exist in the tree?
 *    Answer: We would need to verify that both nodes exist before finding the LCA. This can be
 *    done by performing a pre-traversal to check existence, or by modifying our algorithm to
 *    return additional information (like a boolean flag or count) indicating whether nodes were found.
 *
 * 2. What if we need to find LCA of multiple nodes instead of just two?
 *    Answer: We can extend the recursive approach by tracking all target nodes in a set. The base
 *    case would check if the current node is in the target set. We would return a node if it has
 *    at least two non-null children results or if it's in the target set and has at least one
 *    non-null child result.
 *
 * 3. How would you optimize if we need to answer multiple LCA queries on the same tree?
 *    Answer: We can use preprocessing techniques like Binary Lifting or Tarjan's offline LCA algorithm.
 *    Binary Lifting preprocesses the tree in O(N log N) time and answers each query in O(log N).
 *    For related problem, see: https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
 *
 * 4. What if parent pointers are available for each node?
 *    Answer: We can convert this into a linked list intersection problem. Traverse from both nodes
 *    to the root, calculate path lengths, align them, and find the intersection point. This would
 *    take O(h) time and O(1) space where h is the height of the tree.
 *    Related problem: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree-iii/
 *
 * 5. How would this change for a Binary Search Tree?
 *    Answer: In a BST, we can leverage the ordering property. Starting from root, if both nodes
 *    are smaller than current node, go left; if both are larger, go right; otherwise, current node
 *    is the LCA. This is more efficient as we only traverse one path.
 *    Related problem: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
 */
public class LowestCommonAncestorOfABinaryTree {

    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    /**
     * Finds the lowest common ancestor of two nodes in a binary tree using recursive approach.
     *
     * Algorithm:
     * 1. Base Case: If current node is null or matches either p or q, return current node
     * 2. Recursively search for p and q in left subtree
     * 3. Recursively search for p and q in right subtree
     * 4. Decision Logic:
     *    - If both left and right subtree searches return non-null values, current node is LCA
     *      (p is in one subtree and q is in the other)
     *    - If only left subtree returns non-null, return left result (both nodes in left subtree)
     *    - If only right subtree returns non-null, return right result (both nodes in right subtree)
     *    - If both return null, neither p nor q found in this subtree
     *
     * The algorithm works bottom-up: once we find p or q, we return it up the call stack.
     * When a node receives non-null values from both subtrees, it knows it's the split point
     * and therefore the LCA.
     *
     * Time Complexity: O(N) where N is the number of nodes in the tree.
     * Space Complexity: O(H) where H is the height of the tree due to recursion call stack.
     *
     * @param root the root node of the binary tree
     * @param p first target node
     * @param q second target node
     * @return the lowest common ancestor of nodes p and q
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode leftResult = lowestCommonAncestor(root.left, p, q);
        TreeNode rightResult = lowestCommonAncestor(root.right, p, q);

        if (leftResult != null && rightResult != null) {
            return root;
        }

        return leftResult != null ? leftResult : rightResult;
    }

    /**
     * Alternative approach: Finds LCA by storing paths from root to each target node.
     * This method first finds paths to both nodes, then compares paths to find last common node.
     *
     * Algorithm:
     * 1. Find path from root to node p
     * 2. Find path from root to node q
     * 3. Compare both paths from start to find the last common node
     *
     * Time Complexity: O(N) where N is the number of nodes. We traverse the tree twice
     * in worst case to find both paths, then compare paths which is O(H).
     *
     * Space Complexity: O(H) for storing paths where H is height of tree.
     * - Best case (balanced tree): O(log N)
     * - Worst case (skewed tree): O(N)
     *
     * Note: This approach is less efficient than the recursive approach due to multiple
     * traversals and additional space for storing paths. The recursive approach is preferred
     * in interviews for its elegance and efficiency.
     *
     * @param root the root node of the binary tree
     * @param p first target node
     * @param q second target node
     * @return the lowest common ancestor of nodes p and q
     */
    public TreeNode lowestCommonAncestorPathBased(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }

        List<TreeNode> pathToP = new ArrayList<>();
        List<TreeNode> pathToQ = new ArrayList<>();

        if (!findPath(root, p, pathToP) || !findPath(root, q, pathToQ)) {
            return null;
        }

        TreeNode lca = null;
        int minLength = Math.min(pathToP.size(), pathToQ.size());

        for (int i = 0; i < minLength; i++) {
            if (pathToP.get(i) == pathToQ.get(i)) {
                lca = pathToP.get(i);
            } else {
                break;
            }
        }

        return lca;
    }

    // Helper method to find path from root to target node
    private boolean findPath(TreeNode root, TreeNode target, List<TreeNode> path) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (root == target) {
            return true;
        }

        if (findPath(root.left, target, path) || findPath(root.right, target, path)) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }
}
