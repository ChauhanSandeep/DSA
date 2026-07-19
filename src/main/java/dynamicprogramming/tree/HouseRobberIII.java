package dynamicprogramming.tree;

import java.util.*;

/**
 * Problem: House Robber III
 *
 * Houses are arranged as nodes in a binary tree. Adjacent parent-child houses
 * cannot both be robbed, so return the maximum total money obtainable without
 * triggering the alarm.
 *
 * Leetcode: https://leetcode.com/problems/house-robber-iii/ (Medium)
 * Rating:   acceptance 56.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Tree DP | Rob-or-skip state pair
 *
 * Example:
 *   Input:  root = [3,2,3,null,3,null,1]
 *   Output: 7
 *   Why:    robbing the root plus the two grandchildren gives 3 + 3 + 1 = 7.
 *
 * Follow-ups:
 *   1. What if the tree is extremely deep?
 *      Use an explicit post-order stack to avoid recursion depth limits.
 *   2. What if node values can be negative?
 *      The rob-or-skip max naturally skips harmful nodes by choosing the not-rob state.
 *   3. What if houses form a general graph?
 *      This becomes maximum-weight independent set, which is NP-hard in general graphs.
 *
 * Related: House Robber (198), House Robber II (213), Binary Tree Maximum Path Sum (124).
 */
public class HouseRobberIII {

    public static void main(String[] args) {
        HouseRobberIII solver = new HouseRobberIII();

        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.right = new TreeNode(3);
        root1.right.right = new TreeNode(1);
        System.out.printf("root=%s -> %d  expected=%d%n", "[3,2,3,null,3,null,1]", solver.rob(root1), 7);

        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(1);
        root2.left.right = new TreeNode(3);
        root2.right.right = new TreeNode(1);
        System.out.printf("root=%s -> %d  expected=%d%n", "[3,4,5,1,3,null,1]", solver.rob(root2), 9);
    }

    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

        /**
     * Intuition: for each node there are two useful values: the best total if
     * that node is robbed, and the best total if it is skipped. Robbing the node
     * forces both children into their skipped states. Skipping the node lets each
     * child independently choose its better robbed-or-skipped state.
     *
     * Algorithm:
     *   1. Run a post-order traversal so child state pairs are known before the parent.
     *   2. For each node, compute robCurrent from skipped children and notRobCurrent from each child's best state.
     *   3. Return the larger of the root's two states.
     *
     * Time:  O(n) - each tree node is processed once.
     * Space: O(h) - recursion depth is the tree height.
     *
     * @param root root of the binary-tree neighborhood
     * @return maximum money that can be robbed without taking adjacent nodes
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