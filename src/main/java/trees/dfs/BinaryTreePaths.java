package trees.dfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;


/**
 * Problem: Binary Tree Paths
 *
 * Given a binary tree, return every root-to-leaf path as a string. A valid path
 * must begin at the root, end at a leaf, and use "->" between node values.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-paths/ (Easy)
 * Rating:   not available
 * Pattern:  Trees | DFS | Backtracking | Root-to-leaf path building
 *
 * Example:
 *   Input:  root = [1,2,3,null,5]
 *   Output: ["1->2->5", "1->3"]
 *   Why:    the tree has exactly two leaves, 5 and 3, so there are two root-to-leaf paths.
 *
 * Follow-ups:
 *   1. What if only paths with a target sum should be returned?
 *      Carry a running sum and add the path only at matching leaves.
 *   2. Can this be done iteratively?
 *      Store node and path-so-far pairs in a stack or queue.
 *   3. How would you stream paths instead of storing all of them?
 *      Call a callback when a leaf is reached, then backtrack normally.
 *
 * Related: Path Sum II (113), Sum Root to Leaf Numbers (129).
 */
public class BinaryTreePaths {

  public static void main(String[] args) {
    BinaryTreePaths solver = new BinaryTreePaths();
    TreeNode root = new TreeNode(1);
    root.left = new TreeNode(2);
    root.right = new TreeNode(3);
    root.left.right = new TreeNode(5);
    TreeNode single = new TreeNode(1);

    System.out.printf("root=%s -> %s  expected=%s%n",
        "[1,2,3,null,5]", solver.binaryTreePaths(root), "[1->2->5, 1->3]");
    System.out.printf("root=%s -> %s  expected=%s%n",
        "[1]", solver.binaryTreePaths(single), "[1]");
  }


    /**
   * Intuition: a root-to-leaf path is exactly the recursion stack from root to
   * the current node. Keep that stack in path, add a formatted copy at leaves,
   * and remove the current value while returning so sibling branches start clean.
   *
   * Algorithm:
   *   1. Return an empty result for a null root.
   *   2. DFS from root, appending node values to path.
   *   3. At a leaf, convert path to "a->b->c" and add it to result.
   *   4. After exploring children, remove the current node to backtrack.
   *
   * Time:  O(n * h) - each leaf path formatting can copy up to h values.
   * Space: O(h) - recursion and path hold one root-to-leaf branch, excluding output.
   *
   * @param root root of the binary tree
   * @return all root-to-leaf paths in string form
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

  // Traverses root-to-leaf branches while maintaining the current path list.
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

  // Converts the current list of node values into the required arrow format.
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
