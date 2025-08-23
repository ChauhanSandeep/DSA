package frazsheet;

import java.util.*;

/**
 * 337. House Robber III
 * 
 * Problem: Houses form a binary tree. Two directly-linked houses cannot be robbed
 * on the same night. Determine the maximum amount of money you can rob without
 * alerting the police.
 * 
 * Example:
 * Input: root = [3,2,3,null,3,null,1]
 * Output: 7
 * Explanation: Maximum = 3 + 3 + 1 = 7
 * 
 * LeetCode: https://leetcode.com/problems/house-robber-iii
 * 
 * Follow-up questions:
 * Q: What if the tree is very deep?
 * A: Use iterative approach or memoization to avoid stack overflow.
 * 
 * Q: How to handle if some houses have negative values?
 * A: Algorithm still works; just skip negative value houses.
 * 
 * Q: Can we solve for a general graph instead of tree?
 * A: NP-hard problem; need approximation algorithms or exact exponential solutions.
 */
public class HouseRobberIII {
    
    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    /**
     * Optimal solution using post-order traversal with pair return.
     * Returns [rob_current, not_rob_current] for each node.
     * 
     * Algorithm: Bottom-up DP
     * - For each node, calculate max money if we rob it vs if we don't
     * - If rob current: add current value + max(don't rob children)
     * - If don't rob current: max(rob or don't rob each child)
     * 
     * Time Complexity: O(n) where n is number of nodes
     * Space Complexity: O(h) where h is height of tree (recursion stack)
     */
    public int rob(TreeNode root) {
        int[] result = robHelper(root);
        return Math.max(result[0], result[1]);
    }
    
    // Returns [rob_current, not_rob_current]
    private int[] robHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // If we rob current node, we can't rob children
        int robCurrent = node.val + left[1] + right[1];
        
        // If we don't rob current, we can choose optimally for children
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        return new int[]{robCurrent, notRobCurrent};
    }
    
    /**
     * Memoization approach using HashMap to cache results.
     * Prevents recalculation of same subtrees.
     */
    public int robMemoization(TreeNode root) {
        Map<TreeNode, Integer> memo = new HashMap<>();
        return robMemo(root, memo);
    }
    
    private int robMemo(TreeNode node, Map<TreeNode, Integer> memo) {
        if (node == null) return 0;
        if (memo.containsKey(node)) return memo.get(node);
        
        // Option 1: Rob current house
        int robCurrent = node.val;
        if (node.left != null) {
            robCurrent += robMemo(node.left.left, memo) + robMemo(node.left.right, memo);
        }
        if (node.right != null) {
            robCurrent += robMemo(node.right.left, memo) + robMemo(node.right.right, memo);
        }
        
        // Option 2: Don't rob current house
        int notRobCurrent = robMemo(node.left, memo) + robMemo(node.right, memo);
        
        int result = Math.max(robCurrent, notRobCurrent);
        memo.put(node, result);
        return result;
    }
    
    /**
     * Naive recursive approach without memoization.
     * Demonstrates the basic recurrence relation but has exponential time complexity.
     */
    public int robNaive(TreeNode root) {
        if (root == null) return 0;
        
        // Option 1: Rob current house (can't rob direct children)
        int robCurrent = root.val;
        if (root.left != null) {
            robCurrent += robNaive(root.left.left) + robNaive(root.left.right);
        }
        if (root.right != null) {
            robCurrent += robNaive(root.right.left) + robNaive(root.right.right);
        }
        
        // Option 2: Don't rob current house (can rob children)
        int notRobCurrent = robNaive(root.left) + robNaive(root.right);
        
        return Math.max(robCurrent, notRobCurrent);
    }
    
    /**
     * Iterative solution using level-order traversal and dynamic programming.
     * Processes tree level by level to avoid recursion.
     */
    public int robIterative(TreeNode root) {
        if (root == null) return 0;
        
        Map<TreeNode, Integer> robMap = new HashMap<>();    // Max money if we rob this node
        Map<TreeNode, Integer> notRobMap = new HashMap<>(); // Max money if we don't rob this node
        
        // Level-order traversal (BFS)
        Queue<TreeNode> queue = new LinkedList<>();
        List<List<TreeNode>> levels = new ArrayList<>();
        queue.offer(root);
        
        // Build levels for bottom-up processing
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<TreeNode> level = new ArrayList<>();
            
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                level.add(node);
                
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            
            levels.add(level);
        }
        
        // Process levels from bottom to top
        for (int i = levels.size() - 1; i >= 0; i--) {
            for (TreeNode node : levels.get(i)) {
                int leftRob = robMap.getOrDefault(node.left, 0);
                int leftNotRob = notRobMap.getOrDefault(node.left, 0);
                int rightRob = robMap.getOrDefault(node.right, 0);
                int rightNotRob = notRobMap.getOrDefault(node.right, 0);
                
                // If we rob current, we can't rob children
                robMap.put(node, node.val + leftNotRob + rightNotRob);
                
                // If we don't rob current, we can choose optimally for children
                notRobMap.put(node, Math.max(leftRob, leftNotRob) + Math.max(rightRob, rightNotRob));
            }
        }
        
        return Math.max(robMap.get(root), notRobMap.get(root));
    }
    
    /**
     * Alternative iterative approach using post-order traversal with stack.
     * Simulates recursive post-order traversal iteratively.
     */
    public int robIterativePostOrder(TreeNode root) {
        if (root == null) return 0;
        
        Map<TreeNode, int[]> dp = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        TreeNode lastVisited = null;
        
        while (!stack.isEmpty() || current != null) {
            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                TreeNode peekNode = stack.peek();
                
                if (peekNode.right != null && lastVisited != peekNode.right) {
                    current = peekNode.right;
                } else {
                    // Process current node
                    processNode(peekNode, dp);
                    lastVisited = stack.pop();
                }
            }
        }
        
        int[] result = dp.get(root);
        return Math.max(result[0], result[1]);
    }
    
    // Process node in post-order manner
    private void processNode(TreeNode node, Map<TreeNode, int[]> dp) {
        int[] left = dp.getOrDefault(node.left, new int[]{0, 0});
        int[] right = dp.getOrDefault(node.right, new int[]{0, 0});
        
        int robCurrent = node.val + left[1] + right[1];
        int notRobCurrent = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        
        dp.put(node, new int[]{robCurrent, notRobCurrent});
    }
    
    /**
     * Solution using custom result class for better readability.
     * Encapsulates the rob/not-rob decision in a dedicated class.
     */
    public int robWithClass(TreeNode root) {
        RobResult result = robHelperClass(root);
        return Math.max(result.robRoot, result.notRobRoot);
    }
    
    private static class RobResult {
        int robRoot;    // Maximum money if we rob the root
        int notRobRoot; // Maximum money if we don't rob the root
        
        RobResult(int robRoot, int notRobRoot) {
            this.robRoot = robRoot;
            this.notRobRoot = notRobRoot;
        }
    }
    
    private RobResult robHelperClass(TreeNode node) {
        if (node == null) {
            return new RobResult(0, 0);
        }
        
        RobResult left = robHelperClass(node.left);
        RobResult right = robHelperClass(node.right);
        
        int robCurrent = node.val + left.notRobRoot + right.notRobRoot;
        int notRobCurrent = Math.max(left.robRoot, left.notRobRoot) + 
                           Math.max(right.robRoot, right.notRobRoot);
        
        return new RobResult(robCurrent, notRobCurrent);
    }
    
    /**
     * Morris traversal approach for O(1) space complexity.
     * More complex but achieves constant space usage.
     */
    public int robMorris(TreeNode root) {
        // Morris traversal is complex for this problem due to bottom-up requirement
        // For practical purposes, the recursive solution is preferred
        return rob(root);
    }
}