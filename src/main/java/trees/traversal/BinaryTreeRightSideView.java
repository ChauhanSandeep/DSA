package trees.traversal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Problem: Binary Tree Right Side View
 *
 * Imagine standing to the right of a binary tree. Return the node values visible
 * from top to bottom, which is one value per depth: the rightmost node at that
 * level.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-right-side-view/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | BFS | Level-order traversal | Last node per level
 *
 * Example:
 *   Input:  root = [1,2,3,null,5,null,4]
 *   Output: [1, 3, 4]
 *   Why:    at each depth, 1, then 3, then 4 are the rightmost visible nodes.
 *
 * Follow-ups:
 *   1. How would you return the left side view?
 *      Capture the first node at each BFS level or DFS left-first by depth.
 *   2. Can DFS compute the right view?
 *      Visit right before left and record the first node seen at each depth.
 *   3. How would you return both side views in one pass?
 *      Store the first and last values for each BFS level.
 *
 * Related: Binary Tree Level Order Traversal (102), Boundary of Binary Tree (545).
 */
public class BinaryTreeRightSideView {

  public static void main(String[] args) {
    BinaryTreeRightSideView solver = new BinaryTreeRightSideView();
    TreeNode root = new TreeNode(1);
    root.left = new TreeNode(2);
    root.right = new TreeNode(3);
    root.left.right = new TreeNode(5);
    root.right.right = new TreeNode(4);

    System.out.printf("root=%s -> %s  expected=%s%n",
        "[1,2,3,null,5,null,4]", solver.rightSideView(root), "[1, 3, 4]");
    System.out.printf("root=%s -> %s  expected=%s%n",
        "[]", solver.rightSideView(null), "[]");
  }

    /**
   * Intuition: a right side view is the last node encountered in normal
   * left-to-right BFS for each level. The levelSize loop tells us exactly which
   * node is last before the next level's children begin.
   *
   * Algorithm:
   *   1. Return an empty list for a null root.
   *   2. Process BFS one level at a time.
   *   3. Add node.val when i is levelSize - 1.
   *   4. Enqueue left child, then right child, preserving original level order.
   *
   * Time:  O(n) - each node is processed once.
   * Space: O(w) - the queue stores the current frontier.
   *
   * @param root root of the binary tree
   * @return values visible from the right side
   */
  public List<Integer> rightSideView(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    if (root == null) {
      return result;
    }

    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);

    while (!queue.isEmpty()) {
      int levelSize = queue.size();

      for (int i = 0; i < levelSize; i++) {
        TreeNode node = queue.poll();

        if (i == levelSize - 1) {
          result.add(node.val);
        }

        if (node.left != null) {
          queue.offer(node.left);
        }
        if (node.right != null) {
          queue.offer(node.right);
        }
      }
    }

    return result;
  }

  /**
   * Alternative DFS approach with depth tracking.
   * Prioritizes right child traversal to capture rightmost nodes first.
   *
   * Algorithm:
   * 1. Traverse tree using DFS, visiting right child before left
   * 2. Track current depth during traversal
   * 3. When visiting a depth for the first time, add node value to result
   * 4. Since we traverse right-first, the first node at each depth is rightmost
   *
   * Time Complexity: O(N) where N is the number of nodes. Each node is visited once.
   * Space Complexity: O(H) where H is the height of the tree for recursion stack.
   * For a balanced tree, H = log(N). For skewed tree, H = N.
   *
   * @param root the root node of the binary tree
   * @return list of values visible from the right side
   */
  public List<Integer> rightSideViewDFS(TreeNode root) {
    List<Integer> result = new ArrayList<>();
    dfs(root, 0, result);
    return result;
  }

  // Right-first DFS helper that records the first node seen at each depth.
  private void dfs(TreeNode node, int depth, List<Integer> result) {
    if (node == null) {
      return;
    }

    if (depth == result.size()) {
      // means that we are visiting this depth for the first time
      result.add(node.val);
    }

    dfs(node.right, depth + 1, result);
    dfs(node.left, depth + 1, result);
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
