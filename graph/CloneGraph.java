package graph;

import java.util.*;

/**
 * 133. Clone Graph
 *
 * Problem: Given a reference of a node in a connected undirected graph,
 * return a deep copy (clone) of the graph.
 *
 * Example:
 * Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
 * Output: [[2,4],[1,3],[2,4],[1,3]]
 *
 * LeetCode: https://leetcode.com/problems/clone-graph
 *
 * Follow-up questions:
 * Q: How to handle disconnected graphs?
 * A: Need to iterate through all nodes, current solution works for connected components only.
 *
 * Q: What if graph has cycles?
 * A: Current solution handles cycles using visited map to avoid infinite loops.
 *
 * Q: Can we optimize space usage?
 * A: Can use iterative DFS instead of recursion to avoid call stack overhead.
 */
public class CloneGraph {

    // Definition for a Node
    static class Node {
        public int val;
        public List<Node> neighbors;

        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }

        public Node(int val) {
            this.val = val;
            neighbors = new ArrayList<Node>();
        }

        public Node(int val, ArrayList<Node> neighbors) {
            this.val = val;
            this.neighbors = neighbors;
        }
    }

    /**
     * Clones an undirected graph using DFS traversal.
     *
     * Algorithm: DFS with hash map to track cloned nodes
     * - Use map to store mapping from original node to cloned node
     * - For each node, create clone if not exists, then recursively clone neighbors
     * - Map prevents infinite loops in cyclic graphs and ensures single copy per node
     *
     * Time Complexity: O(N + M) where N is nodes and M is edges
     * Space Complexity: O(N) for the hash map and recursion stack
     */
    public Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }

        Map<Node, Node> visited = new HashMap<>();
        return dfs(node, visited);
    }

    // DFS helper for cloning nodes
    private Node dfs(Node node, Map<Node, Node> visited) {
        if (visited.containsKey(node)) {
            return visited.get(node);
        }

        // Create clone of current node
        Node clone = new Node(node.val);
        visited.put(node, clone);

        // Clone all neighbors
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(dfs(neighbor, visited));
        }

        return clone;
    }

    /**
     * Iterative BFS approach for cloning graph.
     * Avoids recursion stack overflow for very deep graphs.
     */
    public Node cloneGraphBFS(Node node) {
        if (node == null) {
            return null;
        }

        Map<Node, Node> visited = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();

        // Create clone of start node and add to queue
        Node clone = new Node(node.val);
        visited.put(node, clone);
        queue.offer(node);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Process all neighbors
            for (Node neighbor : current.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Create clone for unvisited neighbor
                    visited.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                // Add cloned neighbor to current node's clone
                visited.get(current).neighbors.add(visited.get(neighbor));
            }
        }

        return clone;
    }

    /**
     * Alternative iterative DFS using explicit stack.
     * Combines benefits of DFS traversal with iterative approach.
     */
    public Node cloneGraphIterativeDFS(Node node) {
        if (node == null) {
            return null;
        }

        Map<Node, Node> visited = new HashMap<>();
        Stack<Node> stack = new Stack<>();

        // Create clone of start node
        visited.put(node, new Node(node.val));
        stack.push(node);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            for (Node neighbor : current.neighbors) {
                if (!visited.containsKey(neighbor)) {
                    // Create clone for unvisited neighbor
                    visited.put(neighbor, new Node(neighbor.val));
                    stack.push(neighbor);
                }
                // Add cloned neighbor to current node's clone
                visited.get(current).neighbors.add(visited.get(neighbor));
            }
        }

        return visited.get(node);
    }

    /**
     * Memory-optimized version that processes nodes in single pass.
     * Reduces constant factors in space usage.
     */
    public Node cloneGraphOptimized(Node node) {
        if (node == null) return null;

        return cloneNode(node, new HashMap<>());
    }

    // Optimized recursive helper
    private Node cloneNode(Node node, Map<Integer, Node> cloned) {
        if (cloned.containsKey(node.val)) {
            return cloned.get(node.val);
        }

        Node clone = new Node(node.val);
        cloned.put(node.val, clone);

        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(cloneNode(neighbor, cloned));
        }

        return clone;
    }

    /**
     * Utility method to verify if two graphs are identical clones.
     * Useful for testing clone correctness.
     */
    public boolean areGraphsIdentical(Node graph1, Node graph2) {
        if (graph1 == null && graph2 == null) return true;
        if (graph1 == null || graph2 == null) return false;

        Set<Node> visited1 = new HashSet<>();
        Set<Node> visited2 = new HashSet<>();

        return compareGraphs(graph1, graph2, visited1, visited2);
    }

    // Helper to compare two graphs structure
    private boolean compareGraphs(Node node1, Node node2, Set<Node> visited1, Set<Node> visited2) {
        if (node1.val != node2.val || node1.neighbors.size() != node2.neighbors.size()) {
            return false;
        }

        visited1.add(node1);
        visited2.add(node2);

        for (int i = 0; i < node1.neighbors.size(); i++) {
            Node neighbor1 = node1.neighbors.get(i);
            Node neighbor2 = node2.neighbors.get(i);

            boolean visited1Contains = visited1.contains(neighbor1);
            boolean visited2Contains = visited2.contains(neighbor2);

            if (visited1Contains != visited2Contains) {
                return false;
            }

            if (!visited1Contains && !compareGraphs(neighbor1, neighbor2, visited1, visited2)) {
                return false;
            }
        }

        return true;
    }
}