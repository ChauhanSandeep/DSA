package trees.dfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;


/**
 * Problem: Binary Tree Paths
 *
 * Given the root of a binary tree, return all root-to-leaf paths in any order.
 * A leaf is a node with no children.
 *
 * Example:
 * Input: root = [1,2,3,null,5]
 *              1
 *             / \
 *            2   3
 *             \
 *              5
 * Output: ["1->2->5","1->3"]
 * Explanation: All root-to-leaf paths are "1->2->5" and "1->3".
 *
 * Constraints:
 * - The number of nodes in the tree is in the range [1, 100]
 * - -100 <= Node.val <= 100
 *
 * LeetCode Problem: https://leetcode.com/problems/binary-tree-paths
 *
 * Follow-up Questions:
 *
 * 1. How would you modify this to find paths with a specific sum?
 *    Answer: Add a running sum parameter to the DFS function and check if it equals
 *    target when reaching leaf nodes. Only add paths that match the target sum.
 *    Related problem: https://leetcode.com/problems/path-sum-ii/
 *
 * 2. What if you need to find the path to a specific node, not just leaf nodes?
 *    Answer: Modify the base case to check if current node equals the target node instead
 *    of checking for leaf. Store the path when target is found and return early to avoid
 *    unnecessary traversal.
 *
 * 3. Can you solve this iteratively instead of recursively?
 *    Answer: Yes, use a stack or queue storing pairs of (node, path_string). Pop each pair,
 *    append current node, and push children with updated paths. Collect paths at leaf nodes.
 *
 * 4. How would you optimize if the tree has millions of nodes but only a few leaf nodes?
 *    Answer: The current DFS approach is already optimal as it visits each node once. Early
 *    pruning could help if we have additional constraints (like path sum limits), but for
 *    all paths, we must visit all nodes.
 *
 * 5. What if node values can be multi-digit or contain special characters?
 *    Answer: The current solution handles this correctly. StringBuilder concatenates values
 *    as strings, so multi-digit numbers and special characters are preserved in the output.
 */
public class BinaryTreePaths {

  /**
   * Finds all root-to-leaf paths using DFS with backtracking.
   *
   * Algorithm:
   * 1. Use DFS to traverse tree from root to leaves
   * 2. Maintain current path as a list of nodes during traversal
   * 3. When reaching leaf node, convert path to string format and add to result
   * 4. Backtrack by removing current node after exploring its subtrees
   * 5. Recursively explore left and right subtrees
   *
   * Key insight: Backtracking allows us to reuse the same path list for different
   * branches, maintaining correctness while minimizing space usage.
   *
   * Time Complexity: O(N)
   * Space Complexity: O(H) for recursion stack where H is tree height. The path list
   * also uses O(H) space. Output space is O(N) for storing all paths.
   *
   * @param root the root node of the binary tree
   * @return list of all root-to-leaf paths in "a->b->c" format
   */
  public List<String> binaryTreePaths(TreeNode root) {
    List<String> result = new ArrayList<>();
    if (root == null) {
      return result;
    }

    List<Integer> path = new ArrayList<>();
    dfs(root, path, result);
    return result;
  }

  // Helper method to perform DFS traversal with backtracking
  private void dfs(TreeNode node, List<Integer> path, List<String> result) {
    if (node == null) {
      return;
    }

    path.add(node.val);

    if (node.left == null && node.right == null) {
      result.add(buildPath(path));
    } else {
      dfs(node.left, path, result);
      dfs(node.right, path, result);
    }

    path.remove(path.size() - 1);
  }

  // Helper method to build path string from list of node values
  private String buildPath(List<Integer> path) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < path.size(); i++) {
      sb.append(path.get(i));
      if (i < path.size() - 1) {
        sb.append("->");
      }
    }
    return sb.toString();
  }

  /**
   * Definition for a binary tree node.
   */
  public static class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
      this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
      this.val = val;
      this.left = left;
      this.right = right;
    }
  }
}
