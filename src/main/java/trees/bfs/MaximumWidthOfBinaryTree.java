package trees.bfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;

/**
 * Problem: Maximum Width of Binary Tree
 *
 * Return the largest width across all levels of a binary tree. Width counts the
 * positions between the leftmost and rightmost non-null nodes, including any
 * missing nodes that would appear between them in a complete-tree layout.
 *
 * Leetcode: https://leetcode.com/problems/maximum-width-of-binary-tree/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | BFS | Complete-tree indexing | Level boundaries
 *
 * Example:
 *   Input:  root = [1,3,2,5,3,null,9]
 *   Output: 4
 *   Why:    the third level spans positions 5, 3, null, and 9, so its width is four.
 *
 * Follow-ups:
 *   1. How do you prevent index overflow in a deep tree?
 *      Normalize indices at every level or use long values.
 *   2. How would you return the level with maximum width?
 *      Store the nodes from the level whenever maxWidth improves.
 *   3. Can DFS compute the same width?
 *      Remember the first index seen at each depth and compare later indices to it.
 *
 * Related: Binary Tree Level Order Traversal (102), Vertical Order Traversal.
 */
public class MaximumWidthOfBinaryTree {

    public static void main(String[] args) {
        MaximumWidthOfBinaryTree solver = new MaximumWidthOfBinaryTree();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(3);
        root.right = new TreeNode(2);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(9);

        System.out.printf("root=%s -> %d  expected=%d%n",
            "[1,3,2,5,3,null,9]", solver.widthOfBinaryTree(root), 4);
        System.out.printf("root=%s -> %d  expected=%d%n",
            "[]", solver.widthOfBinaryTree(null), 0);
    }


    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

        /**
     * Intuition: complete-tree positions preserve the gaps created by missing
     * children. During BFS, the first and last indices at each level therefore
     * reveal that level's real width, even when null nodes are not enqueued.
     *
     * Algorithm:
     *   1. Queue each real node together with its complete-tree index.
     *   2. For one BFS level, track the minimum and maximum indices removed.
     *   3. Push non-null children with indices 2 * index and 2 * index + 1.
     *   4. Update maxWidth with rightmost - leftmost + 1.
     *
     * Time:  O(n) - each non-null node is processed once.
     * Space: O(w) - the queue holds at most one level of real nodes.
     *
     * @param root root of the binary tree
     * @return maximum width among all levels
     */
    public int widthOfBinaryTree(TreeNode root) {
        if (root == null) return 0;

        Queue<NodeWithIndex> queue = new LinkedList<>();
        queue.offer(new NodeWithIndex(root, 0));
        int maxWidth = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            int leftmost = Integer.MAX_VALUE;
            int rightmost = Integer.MIN_VALUE;

            for (int i = 0; i < size; i++) {
                NodeWithIndex current = queue.poll();
                TreeNode node = current.node;
                int index = current.index;

                leftmost = Math.min(leftmost, index);
                rightmost = Math.max(rightmost, index);

                if (node.left != null) {
                    queue.offer(new NodeWithIndex(node.left, 2 * index));
                }

                if (node.right != null) {
                    queue.offer(new NodeWithIndex(node.right, 2 * index + 1));
                }
            }

            maxWidth = Math.max(maxWidth, rightmost - leftmost + 1);
        }

        return maxWidth;
    }

    // Helper class to store node with its index
    private static class NodeWithIndex {
        TreeNode node;
        int index;

        NodeWithIndex(TreeNode node, int index) {
            this.node = node;
            this.index = index;
        }
    }

    /**
     * DFS approach with level-based index tracking.
     * Uses depth-first traversal to calculate widths.
     */
    public int widthOfBinaryTreeDFS(TreeNode root) {
        if (root == null) return 0;

        Map<Integer, Integer> leftmostIndex = new HashMap<>();
        return dfs(root, 0, 0, leftmostIndex);
    }

    // Returns the best width below node while recording the first index at each level.
    private int dfs(TreeNode node, int level, int index, Map<Integer, Integer> leftmostIndex) {
        if (node == null) return 0;

        // Record leftmost index for this level
        leftmostIndex.putIfAbsent(level, index);

        // Calculate current width
        int currentWidth = index - leftmostIndex.get(level) + 1;

        // Recursively check children
        int leftMaxWidth = dfs(node.left, level + 1, 2 * index, leftmostIndex);
        int rightMaxWidth = dfs(node.right, level + 1, 2 * index + 1, leftmostIndex);

        return Math.max(currentWidth, Math.max(leftMaxWidth, rightMaxWidth));
    }

    /**
     * BFS with index normalization to prevent overflow.
     * Resets indices at each level to handle very deep trees.
     */
    public int widthOfBinaryTreeNormalized(TreeNode root) {
        if (root == null) return 0;

        Queue<NodeWithIndex> queue = new LinkedList<>();
        queue.offer(new NodeWithIndex(root, 0));
        int maxWidth = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            // Normalize indices to prevent overflow
            int minIndex = queue.peek().index;
            List<NodeWithIndex> currentLevel = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                NodeWithIndex current = queue.poll();
                current.index -= minIndex; // Normalize
                currentLevel.add(current);
            }

            // Calculate width for current level
            if (!currentLevel.isEmpty()) {
                int leftmost = currentLevel.get(0).index;
                int rightmost = currentLevel.get(currentLevel.size() - 1).index;
                maxWidth = Math.max(maxWidth, rightmost - leftmost + 1);
            }

            // Add next level
            for (NodeWithIndex current : currentLevel) {
                if (current.node.left != null) {
                    queue.offer(new NodeWithIndex(current.node.left, 2 * current.index));
                }
                if (current.node.right != null) {
                    queue.offer(new NodeWithIndex(current.node.right, 2 * current.index + 1));
                }
            }
        }

        return maxWidth;
    }

    /**
     * Memory-optimized approach processing one level at a time.
     * Reduces space complexity for very wide trees.
     */
    public int widthOfBinaryTreeMemoryOptimized(TreeNode root) {
        if (root == null) return 0;

        List<TreeNode> currentLevel = Arrays.asList(root);
        List<Integer> currentIndices = Arrays.asList(0);
        int maxWidth = 1;

        while (!currentLevel.isEmpty()) {
            List<TreeNode> nextLevel = new ArrayList<>();
            List<Integer> nextIndices = new ArrayList<>();

            // Process current level
            Integer minIndex = null;
            Integer maxIndex = null;

            for (int i = 0; i < currentLevel.size(); i++) {
                TreeNode node = currentLevel.get(i);
                Integer index = currentIndices.get(i);

                if (node != null) {
                    if (minIndex == null) minIndex = index;
                    maxIndex = index;

                    // Add children
                    nextLevel.add(node.left);
                    nextLevel.add(node.right);
                    nextIndices.add(2 * index);
                    nextIndices.add(2 * index + 1);
                } else {
                    nextLevel.add(null);
                    nextLevel.add(null);
                    nextIndices.add(null);
                    nextIndices.add(null);
                }
            }

            if (minIndex != null && maxIndex != null) {
                maxWidth = Math.max(maxWidth, maxIndex - minIndex + 1);
            }

            // Remove trailing nulls and prepare for next iteration
            currentLevel = trimNulls(nextLevel);
            currentIndices = trimNullIndices(nextIndices, nextLevel);
        }

        return maxWidth;
    }

    // Removes trailing null placeholders after a level has been expanded.
    private List<TreeNode> trimNulls(List<TreeNode> nodes) {
        int lastNonNull = -1;
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (nodes.get(i) != null) {
                lastNonNull = i;
                break;
            }
        }
        return lastNonNull >= 0 ? nodes.subList(0, lastNonNull + 1) : new ArrayList<>();
    }

    // Keeps indices that still correspond to retained placeholders or real nodes.
    private List<Integer> trimNullIndices(List<Integer> indices, List<TreeNode> nodes) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < Math.min(indices.size(), nodes.size()); i++) {
            if (nodes.get(i) != null) {
                result.add(indices.get(i));
            } else if (i < nodes.size() - 1 && hasNonNullAfter(nodes, i)) {
                result.add(indices.get(i));
            }
        }
        return result;
    }

    // Reports whether a later entry contains a real node.
    private boolean hasNonNullAfter(List<TreeNode> nodes, int index) {
        for (int i = index + 1; i < nodes.size(); i++) {
            if (nodes.get(i) != null) return true;
        }
        return false;
    }

    /**
     * Recursive approach with explicit level tracking.
     * Alternative DFS implementation with cleaner structure.
     */
    public int widthOfBinaryTreeRecursive(TreeNode root) {
        if (root == null) return 0;

        Map<Integer, List<Integer>> levelIndices = new HashMap<>();
        collectIndices(root, 0, 0, levelIndices);

        int maxWidth = 0;
        for (List<Integer> indices : levelIndices.values()) {
            if (!indices.isEmpty()) {
                int width = indices.get(indices.size() - 1) - indices.get(0) + 1;
                maxWidth = Math.max(maxWidth, width);
            }
        }

        return maxWidth;
    }

    // Collects complete-tree indices for every node grouped by depth.
    private void collectIndices(TreeNode node, int level, int index, Map<Integer, List<Integer>> levelIndices) {
        if (node == null) return;

        levelIndices.computeIfAbsent(level, k -> new ArrayList<>()).add(index);

        collectIndices(node.left, level + 1, 2 * index, levelIndices);
        collectIndices(node.right, level + 1, 2 * index + 1, levelIndices);
    }

    /**
     * Returns the actual nodes at maximum width level.
     * Extension that provides the nodes forming the maximum width.
     */
    public List<TreeNode> getMaxWidthLevelNodes(TreeNode root) {
        if (root == null) return new ArrayList<>();

        Queue<NodeWithIndex> queue = new LinkedList<>();
        queue.offer(new NodeWithIndex(root, 0));
        int maxWidth = 1;
        List<TreeNode> maxWidthNodes = Arrays.asList(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<TreeNode> currentLevelNodes = new ArrayList<>();
            int leftmost = Integer.MAX_VALUE;
            int rightmost = Integer.MIN_VALUE;

            for (int i = 0; i < size; i++) {
                NodeWithIndex current = queue.poll();
                TreeNode node = current.node;
                int index = current.index;

                currentLevelNodes.add(node);
                leftmost = Math.min(leftmost, index);
                rightmost = Math.max(rightmost, index);

                if (node.left != null) {
                    queue.offer(new NodeWithIndex(node.left, 2 * index));
                }

                if (node.right != null) {
                    queue.offer(new NodeWithIndex(node.right, 2 * index + 1));
                }
            }

            int currentWidth = rightmost - leftmost + 1;
            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
                maxWidthNodes = new ArrayList<>(currentLevelNodes);
            }
        }

        return maxWidthNodes;
    }

    /**
     * Iterative approach using two arrays for current and next level.
     * Space-efficient implementation avoiding queue overhead.
     */
    public int widthOfBinaryTreeIterative(TreeNode root) {
        if (root == null) return 0;

        List<TreeNode> currentLevel = new ArrayList<>();
        List<Integer> currentIndices = new ArrayList<>();
        currentLevel.add(root);
        currentIndices.add(0);

        int maxWidth = 1;

        while (!currentLevel.isEmpty()) {
            List<TreeNode> nextLevel = new ArrayList<>();
            List<Integer> nextIndices = new ArrayList<>();

            // Calculate current level width
            int leftmostIndex = currentIndices.get(0);
            int rightmostIndex = currentIndices.get(currentIndices.size() - 1);
            maxWidth = Math.max(maxWidth, rightmostIndex - leftmostIndex + 1);

            // Build next level
            for (int i = 0; i < currentLevel.size(); i++) {
                TreeNode node = currentLevel.get(i);
                int index = currentIndices.get(i);

                if (node.left != null) {
                    nextLevel.add(node.left);
                    nextIndices.add(2 * index);
                }

                if (node.right != null) {
                    nextLevel.add(node.right);
                    nextIndices.add(2 * index + 1);
                }
            }

            currentLevel = nextLevel;
            currentIndices = nextIndices;
        }

        return maxWidth;
    }

    /**
     * Long integer approach to handle very deep trees.
     * Uses long integers to avoid overflow in deep trees.
     */
    public int widthOfBinaryTreeLong(TreeNode root) {
        if (root == null) return 0;

        Queue<NodeWithLongIndex> queue = new LinkedList<>();
        queue.offer(new NodeWithLongIndex(root, 0L));
        int maxWidth = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            long leftmost = Long.MAX_VALUE;
            long rightmost = Long.MIN_VALUE;

            for (int i = 0; i < size; i++) {
                NodeWithLongIndex current = queue.poll();
                TreeNode node = current.node;
                long index = current.index;

                leftmost = Math.min(leftmost, index);
                rightmost = Math.max(rightmost, index);

                if (node.left != null) {
                    queue.offer(new NodeWithLongIndex(node.left, 2L * index));
                }

                if (node.right != null) {
                    queue.offer(new NodeWithLongIndex(node.right, 2L * index + 1L));
                }
            }

            long width = rightmost - leftmost + 1L;
            maxWidth = Math.max(maxWidth, (int) Math.min(width, Integer.MAX_VALUE));
        }

        return maxWidth;
    }

    // Helper class with long index
    private static class NodeWithLongIndex {
        TreeNode node;
        long index;

        NodeWithLongIndex(TreeNode node, long index) {
            this.node = node;
            this.index = index;
        }
    }
}