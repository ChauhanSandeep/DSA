package trees.bfs;

import java.util.*;

/**
 * Problem: Amount of Time for Binary Tree to Be Infected
 *
 * You are given the root of a binary tree with unique values, and an integer start.
 * At minute 0, an infection starts from the node with value start.
 *
 * Each minute, a node becomes infected if:
 * - The node is currently uninfected
 * - The node is adjacent to an infected node (parent or child relationship)
 *
 * Return the number of minutes needed for the entire tree to be infected.
 *
 * Example:
 * Input: root = [1,5,3,null,4,10,6,9,2], start = 3
 * Output: 4
 * Explanation:
 * - Minute 0: Node 3
 * - Minute 1: Nodes 1, 10, 6
 * - Minute 2: Node 5
 * - Minute 3: Node 4
 * - Minute 4: Nodes 9, 2
 *
 * Constraints:
 * - The number of nodes in the tree is in the range [1, 10^5]
 * - 1 <= Node.val <= 10^5
 * - Each node has a unique value
 * - A node with a value of start exists in the tree
 *
 * LeetCode Problem: https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected
 *
 * Follow-up Questions:
 *
 * 1. What if infection can only spread downward (parent to children)?
 *    Answer: Use simple BFS from starting node going only to children. Time becomes
 *    the height of subtree rooted at start node. No need to build parent map.
 *
 * 2. How would you modify if some nodes are immune to infection?
 *    Answer: Add immune node values to a set. During BFS, skip nodes in immune set.
 *    The infection stops spreading through immune nodes, potentially leaving parts
 *    of the tree uninfected.
 *
 * 3. What if you need to return which nodes get infected at each minute?
 *    Answer: Store nodes at each BFS level in separate lists. Return list of lists
 *    where result[i] contains all nodes infected at minute i.
 *
 * 4. How would you handle multiple starting infection points?
 *    Answer: Initialize BFS queue with all starting nodes at minute 0. Run BFS
 *    tracking visited nodes. Time is still maximum distance from any start node.
 *
 * 5. What if infection spreads with different speeds (some edges take longer)?
 *    Answer: Use Dijkstra's algorithm instead of BFS. Assign weights to edges based
 *    on infection speed. Find longest shortest path from start node to any other node.
 */
public class AmountOfTimeForBinaryTreeToBeInfected {

    /**
     * Finds infection time using graph conversion and BFS.
     *
     * Algorithm:
     * 1. Build parent map using BFS to enable upward traversal
     * 2. Find the starting infection node during traversal
     * 3. Run BFS from start node treating tree as undirected graph
     * 4. Track visited nodes to avoid cycles
     * 5. Count levels in BFS - each level is one minute
     *
     * Key insight: Binary tree infection spreads like BFS in undirected graph.
     * Parent pointers allow infection to spread upward. BFS naturally processes
     * nodes level by level (minute by minute).
     *
     * Time Complexity: O(N) where N is number of nodes. First BFS to build parent
     * map visits all nodes once. Second BFS visits all nodes once.
     *
     * Space Complexity: O(N) for parent map, queue, and visited set. Each stores
     * up to N nodes in worst case.
     *
     * @param root root of the binary tree
     * @param start value of node where infection starts
     * @return minutes needed for entire tree to be infected
     */
    public int amountOfTime(TreeNode root, int start) {
        // Build parent map and find start node
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        TreeNode startNode = buildParentMap(root, start, parentMap);

        // BFS to calculate infection time
        return calculateInfectionTime(startNode, parentMap);
    }

    // Build parent map and return the start node
    private TreeNode buildParentMap(TreeNode root, int start, Map<TreeNode, TreeNode> parentMap) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        parentMap.put(root, null);
        TreeNode startNode = null;

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            if (node.val == start) {
                startNode = node;
            }

            if (node.left != null) {
                parentMap.put(node.left, node);
                queue.offer(node.left);
            }

            if (node.right != null) {
                parentMap.put(node.right, node);
                queue.offer(node.right);
            }
        }

        return startNode;
    }

    // Calculate infection time using BFS from start node
    private int calculateInfectionTime(TreeNode startNode, Map<TreeNode, TreeNode> parentMap) {
        Queue<TreeNode> queue = new LinkedList<>();
        Set<TreeNode> visited = new HashSet<>();

        queue.offer(startNode);
        visited.add(startNode);
        int minutes = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Process all nodes at current level (current minute)
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();

                // Try to infect parent
                TreeNode parent = parentMap.get(node);
                if (parent != null && !visited.contains(parent)) {
                    queue.offer(parent);
                    visited.add(parent);
                }

                // Try to infect left child
                if (node.left != null && !visited.contains(node.left)) {
                    queue.offer(node.left);
                    visited.add(node.left);
                }

                // Try to infect right child
                if (node.right != null && !visited.contains(node.right)) {
                    queue.offer(node.right);
                    visited.add(node.right);
                }
            }

            minutes++;
        }

        // Subtract 1 because last increment happens after all nodes processed
        return minutes - 1;
    }

    /**
     * Alternative DFS approach converting tree to graph with adjacency list.
     *
     * Algorithm:
     * 1. Build undirected graph representation using adjacency list
     * 2. Use DFS from start node to find maximum depth
     * 3. Track visited nodes to avoid revisiting
     *
     * Time Complexity: O(N) where N is number of nodes.
     *
     * Space Complexity: O(N) for graph and recursion stack.
     *
     * @param root root of the binary tree
     * @param start value of node where infection starts
     * @return minutes needed for entire tree to be infected
     */
    public int amountOfTimeDFS(TreeNode root, int start) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        buildGraph(root, null, graph);

        Set<Integer> visited = new HashSet<>();
        return dfs(start, graph, visited) - 1;
    }

    // Build undirected graph from binary tree
    private void buildGraph(TreeNode node, TreeNode parent, Map<Integer, List<Integer>> graph) {
        if (node == null) {
            return;
        }

        graph.putIfAbsent(node.val, new ArrayList<>());

        if (parent != null) {
            graph.get(node.val).add(parent.val);
            graph.get(parent.val).add(node.val);
        }

        buildGraph(node.left, node, graph);
        buildGraph(node.right, node, graph);
    }

    // DFS to find maximum distance from start node
    private int dfs(int node, Map<Integer, List<Integer>> graph, Set<Integer> visited) {
        visited.add(node);
        int maxDepth = 0;

        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                maxDepth = Math.max(maxDepth, dfs(neighbor, graph, visited));
            }
        }

        return maxDepth + 1;
    }

    /**
     * Definition for a binary tree node.
     */
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
}
