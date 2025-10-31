package trees;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/**
 * Problem: Binary Tree Right Side View
 *
 * Given the root of a binary tree, imagine yourself standing on the right side of it.
 * Return the values of the nodes you can see ordered from top to bottom.
 *
 * Example:
 * Input: root = [1,2,3,null,5,null,4]
 *          1
 *         / \
 *        2   3
 *         \   \
 *          5   4
 * Output: [1,3,4]
 * Explanation: Looking from the right side, at each level we see: 1 (root), 3 (rightmost at level 1),
 * and 4 (rightmost at level 2). Node 2 is hidden behind 3, and node 5 is hidden behind 4.
 *
 * LeetCode Problem: https://leetcode.com/problems/binary-tree-right-side-view
 *
 * Follow-up Questions:
 * 1. How would you implement the left side view instead?
 *    Answer: For BFS, capture the first node at each level instead of the last. For DFS,
 *    traverse left child before right child and update the result only when visiting a level
 *    for the first time.
 *
 * 2. What if you need both left and right side views simultaneously?
 *    Answer: During level-order traversal, capture both the first and last nodes at each level.
 *    Store them as pairs or in separate lists. This requires minimal additional logic.
 *    Related discussion: https://leetcode.com/discuss/interview-question/
 *
 * 3. Can you solve this without using a queue?
 *    Answer: Yes, use DFS with depth tracking. Visit right child before left and record the
 *    first node encountered at each depth. This avoids queue overhead while achieving the same result.
 *
 * 4. How would you extend this to return nodes at a specific viewing angle?
 *    Answer: Generalize by assigning horizontal distances to nodes. At each level, return nodes
 *    within a specified horizontal distance range based on the viewing angle.
 *
 * 5. What if the tree is extremely wide but shallow?
 *    Answer: BFS is optimal as it processes one level at a time with O(width) space. DFS would
 *    use less space (O(height)) but the height-to-width ratio makes BFS more practical.
 */
public class BinaryTreeRightSideView {
  /**
   * Finds right side view using level-order traversal (BFS).
   *
   * Algorithm:
   * 1. Use queue for level-order traversal of the tree
   * 2. Process nodes level by level using queue size to track level boundaries
   * 3. At each level, capture the last node's value (rightmost visible node)
   * 4. Add children to queue for next level processing
   * 5. Continue until all levels are processed
   *
   * Time Complexity: O(N) where N is the number of nodes. Each node is visited once
   * during the BFS traversal.
   * Space Complexity: O(W) where W is the maximum width of the tree. In the worst case
   * of a complete binary tree, the last level contains N/2 nodes, making it O(N).
   *
   * @param root the root node of the binary tree
   * @return list of values visible from the right side
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

  // Helper method for DFS traversal with depth tracking
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
