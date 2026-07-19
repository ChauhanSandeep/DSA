package graphs;

import java.util.*;

/**
 * Problem: Clone Graph
 *
 * Given a reference to a node in a connected undirected graph, return a deep
 * copy of the whole graph. Each cloned node must be new, while preserving the
 * same values and neighbor relationships as the original graph.
 *
 * Leetcode: https://leetcode.com/problems/clone-graph/ (Medium)
 * Rating:   acceptance 65.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | DFS/BFS | Hash map for object cloning
 *
 * Example:
 *   Input:  adjList = [[2,4],[1,3],[2,4],[1,3]]
 *   Output: [[2,4],[1,3],[2,4],[1,3]]
 *   Why:    the clone has four different node objects, but each node keeps the
 *           same value and neighbor values as the corresponding original node.
 *
 * Follow-ups:
 *   1. Clone a disconnected graph?
 *      Iterate over every known node and start cloning from each unvisited component.
 *   2. Avoid recursion for a very deep graph?
 *      Use the BFS method with an explicit queue instead of recursive DFS.
 *   3. Clone nodes that have random extra pointers?
 *      Add every pointer type to the same original-to-clone map before wiring edges.
 *
 * Related: Copy List with Random Pointer (138), Course Schedule (207).
 */
public class CloneGraph {


    public static void main(String[] args) {
        CloneGraph solver = new CloneGraph();

        Node first = new Node(1);
        Node second = new Node(2);
        first.neighbors.add(second);
        second.neighbors.add(first);

        Node[] inputs = { first, null };
        boolean[] expected = { true, true };

        for (int i = 0; i < inputs.length; i++) {
            Node clone = solver.cloneGraph(inputs[i]);
            boolean output = inputs[i] == null ? clone == null : solver.areGraphsIdentical(inputs[i], clone) && inputs[i] != clone;
            System.out.printf("graphCase=%d  ->  %s  expected=%s%n",
                i + 1, output, expected[i]);
        }
    }
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
     * Intuition: cloning a graph is traversal plus a registry. The map says which
     * original nodes already have clones, so cycles and repeated edges reuse the
     * same clone instead of creating duplicates or recursing forever.
     *
     * Algorithm:
     *   1. Return null for a null starting node.
     *   2. If the node already exists in the map, return its clone.
     *   3. Create and store the clone before exploring neighbors.
     *   4. Recursively clone each neighbor and append it to the clone's neighbor list.
     *
     * Time:  O(V+E) - each node is cloned once and each adjacency entry is copied once.
     * Space: O(V) - the map and recursion stack hold graph nodes in the worst case.
     *
     * @param node any node from the connected graph
     * @return deep copy of the connected graph, or null for null input
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
