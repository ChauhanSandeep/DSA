package Tree;

import java.util.*;

/**
 * 662. Maximum Width of Binary Tree
 * 
 * Problem: Given the root of a binary tree, return the maximum width of the given tree.
 * The width is the length between the end-nodes (the leftmost and rightmost non-null
 * nodes) at the same level, measured as the number of null nodes between them plus 1.
 * 
 * Example:
 * Input: root = [1,3,2,5,3,null,9]
 * Output: 4
 * Explanation: Level 2 has width 4 ([5,3,null,9]).
 * 
 * LeetCode: https://leetcode.com/problems/maximum-width-of-binary-tree
 * 
 * Follow-up questions:
 * Q: How to handle very deep trees to avoid integer overflow?
 * A: Use coordinate compression or reset indices at each level.
 * 
 * Q: What if we need to find the actual nodes at maximum width level?
 * A: Store node references along with positions during traversal.
 * 
 * Q: Can we optimize space for very wide trees?
 * A: Use level-by-level processing to avoid storing all positions.
 */
public class MaximumWidthOfBinaryTree {
    
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
     * BFS approach with position tracking.
     * 
     * Algorithm: Level-order traversal with indexing
     * - Use BFS to traverse level by level
     * - Assign index to each node: left child = 2*i, right child = 2*i+1
     * - For each level, calculate width as (rightmost_index - leftmost_index + 1)
     * - Track maximum width across all levels
     * 
     * Time Complexity: O(n) where n is number of nodes
     * Space Complexity: O(w) where w is maximum width of tree
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
    
    // DFS helper returning maximum width found
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
    
    // Remove trailing null nodes
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
    
    // Remove indices corresponding to trimmed nulls
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
    
    // Check if there are non-null nodes after given index
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
    
    // Collect all indices for each level
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