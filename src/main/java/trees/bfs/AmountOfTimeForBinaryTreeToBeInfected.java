package trees.bfs;

import java.util.*;

/**
 * Problem: Amount of Time for Binary Tree to Be Infected
 *
 * An infection starts at the node whose value is start. Each minute it spreads
 * across one parent-child edge to every adjacent uninfected node. Return the
 * number of minutes needed until every node in the tree is infected.
 *
 * Leetcode: https://leetcode.com/problems/amount-of-time-for-binary-tree-to-be-infected/ (Medium)
 * Rating:   1711
 * Pattern:  Trees | BFS | Parent map | Undirected graph from a binary tree
 *
 * Example:
 *   Input:  root = [1,5,3,null,4,10,6,9,2], start = 3
 *   Output: 4
 *   Why:    the farthest nodes, 9 and 2, are four edges away from node 3.
 *
 * Follow-ups:
 *   1. What if there are multiple infection sources?
 *      Seed the BFS queue with all starts and take the farthest first-reached node.
 *   2. What if each edge has a different infection time?
 *      Replace BFS with Dijkstra's algorithm on the parent-child graph.
 *   3. What if some nodes are immune?
 *      Skip immune nodes during traversal and report unreachable nodes separately.
 *   4. What if infection can only move downward?
 *      Start at the target node and return the height of that subtree.
 *
 * Related: All Nodes Distance K in Binary Tree (863), Burn Tree, Binary Tree Maximum Path Sum (124).
 */
public class AmountOfTimeForBinaryTreeToBeInfected {

    public static void main(String[] args) {
        AmountOfTimeForBinaryTreeToBeInfected solver = new AmountOfTimeForBinaryTreeToBeInfected();

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(5);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(4);
        root.left.right.left = new TreeNode(9);
        root.left.right.right = new TreeNode(2);
        root.right.left = new TreeNode(10);
        root.right.right = new TreeNode(6);

        TreeNode single = new TreeNode(1);
        System.out.printf("root=%s start=%d -> %d  expected=%d%n",
            "[1,5,3,null,4,10,6,9,2]", 3, solver.amountOfTime(root, 3), 4);
        System.out.printf("root=%s start=%d -> %d  expected=%d%n",
            "[1]", 1, solver.amountOfTime(single, 1), 0);
    }


        /**
     * Intuition: once parent links are known, infection is just level-order
     * expansion in an undirected graph. The first BFS records every node's
     * parent and finds startNode. The second BFS starts from startNode; each
     * completed level represents one elapsed minute.
     *
     * Algorithm:
     *   1. Build parentMap with BFS while remembering the node whose value is start.
     *   2. Run BFS from startNode and visit parent, left child, and right child.
     *   3. Count BFS levels and subtract the final extra increment after the queue empties.
     *
     * Time:  O(n) - both traversals visit each node at most once.
     * Space: O(n) - parentMap, visited, and the queue can each hold many nodes.
     *
     * @param root root of the binary tree
     * @param start value where infection starts
     * @return minutes needed to infect the entire tree
     */
    public int amountOfTime(TreeNode root, int start) {
        // Build parent map and find start node
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        TreeNode startNode = buildParentMap(root, start, parentMap);

        // BFS to calculate infection time
        return calculateInfectionTime(startNode, parentMap);
    }

    // Builds parent links with BFS and returns the node whose value matches start.
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

    // Runs BFS from the infected node and returns the number of elapsed levels.
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

    // Adds parent-child edges to an adjacency list representation of the tree.
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

    // Returns the farthest DFS depth reachable from node without revisiting values.
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
