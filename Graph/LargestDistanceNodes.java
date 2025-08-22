package Graph;

import java.util.*;

/**
 * Problem: Find the largest distance (diameter) between any two nodes in a tree.
 *
 * Problem Statement:
 * - The diameter of a tree is defined as the length of the longest path between any two nodes.
 * - Given a tree in the form of a parent array, find this largest distance.
 *
 * Example:
 * Input: parentNodes = [-1, 0, 0, 0, 3]
 * Output: 3
 * Explanation: The longest path is between node 1 → 0 → 3 → 4, length = 3.
 *
 * LeetCode Link: https://leetcode.com/problems/diameter-of-binary-tree/ (binary tree variant)
 * InterviewBit Link: https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 *
 * Follow-up Questions:
 * 1. Can you solve this using DFS instead of BFS?
 *    - Yes, by recursively calculating depth and diameter from each node.
 * 2. Can the algorithm be optimized to a single DFS traversal?
 *    - Yes, DFS can compute the diameter in O(N) with a single pass.
 * 3. What changes if the graph is not guaranteed to be a tree?
 *    - We must detect cycles and ensure connectivity. The problem reduces to longest path in a graph (NP-hard).
 */
public class LargestDistanceNodes {

    public static void main(String[] args) {
        List<Integer> parentNodes = Arrays.asList(-1, 0, 0, 0, 3);
        System.out.println("Largest Distance (BFS method): " +
            new LargestDistanceNodes().findLargestDistanceBFS(parentNodes));
        System.out.println("Largest Distance (DFS method): " +
            new LargestDistanceNodes().findLargestDistanceDFS(parentNodes));
    }

    /**
     * Finds the largest distance (diameter) between any two nodes using BFS (two-pass approach).
     *
     * Steps:
     * 1. Build the adjacency list from parent array.
     * 2. Run BFS from root to find farthest node (nodeA).
     * 3. Run BFS from nodeA to find the farthest node (nodeB) and distance.
     *
     * Time Complexity: O(N) - Two BFS traversals.
     * Space Complexity: O(N) - Adjacency list + BFS queue.
     */
    public int findLargestDistanceBFS(List<Integer> parentNodes) {
        List<List<Integer>> adjacencyList = buildGraph(parentNodes); // <node, list of neighbors>

        int root = findRoot(parentNodes);

        int[] farthestNodeAndItsDistanceFromRoot = bfs(adjacencyList, root); // Find farthest node from root
        int farthestFromRoot = farthestNodeAndItsDistanceFromRoot[0];

        int[] farthestNodeAndItsDistanceFromNode = bfs(adjacencyList, farthestFromRoot); // Find farthest node from farthest node
        return farthestNodeAndItsDistanceFromNode[1];
    }

    /**
     * BFS to find farthest node and its distance from startNode.
     * Returns [farthestNode, maxDistance].
     */
    private int[] bfs(List<List<Integer>> adjacencyList, int startNode) {
        int size = adjacencyList.size();
        Queue<int[]> queue = new LinkedList<>();
        boolean[] visited = new boolean[size];

        queue.offer(new int[]{startNode, 0});
        visited[startNode] = true;

        int farthestNode = startNode;
        int maxDistance = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0], distance = current[1];

            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }

            for (int neighbor : adjacencyList.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, distance + 1});
                }
            }
        }
        return new int[]{farthestNode, maxDistance};
    }

    /**
     * Finds the largest distance (diameter) between any two nodes using DFS in one pass.
     *
     * Steps:
     * 1. Build the adjacency list from parent array.
     * 2. Perform DFS that returns an array containing:
     *      - [0] = max depth from this node
     *      - [1] = max diameter found in the subtree
     * 3. Return the diameter.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(N) - recursion stack + adjacency list.
     */
    public int findLargestDistanceDFS(List<Integer> parentNodes) {
        List<List<Integer>> adjacencyList = buildGraph(parentNodes);
        int root = findRoot(parentNodes);

        // DFS returns [maxDepth, maxDiameter] for the root
        int[] result = dfs(root, -1, adjacencyList);
        return result[1]; // maxDiameter
    }

    /**
     * DFS helper that returns [maxDepthFromNode, maxDiameterInSubtree]
     *
     * maxDepthFromNode = max depth from this node to a leaf
     * maxDiameterInSubtree = max diameter in the subtree, either from children or through this node
     */
    private int[] dfs(int currentNode, int parentNode, List<List<Integer>> adjacencyList) {
        int deepestNodeLength = 0, secondDeepestNodeLength = 0; // top two depths from children
        int maxDiameterInSubtree = 0;

        for (int neighbor : adjacencyList.get(currentNode)) {
            if (neighbor == parentNode) continue;

            int[] childResult = dfs(neighbor, currentNode, adjacencyList);
            int childDepth = childResult[0];
            int childDiameter = childResult[1];

            // track top two max depths
            if (childDepth > deepestNodeLength) {
                secondDeepestNodeLength = deepestNodeLength;
                deepestNodeLength = childDepth;
            } else if (childDepth > secondDeepestNodeLength) {
                secondDeepestNodeLength = childDepth;
            }

            // track max diameter seen in children
            maxDiameterInSubtree = Math.max(maxDiameterInSubtree, childDiameter);
        }

        // local diameter through this node
        int diameterThroughThisNode = deepestNodeLength + secondDeepestNodeLength;

        // max diameter, either from subtree or through this node
        int maxDiameter = Math.max(maxDiameterInSubtree, diameterThroughThisNode);

        // return [maxDepthFromNode, maxDiameterInSubtree]
        return new int[]{deepestNodeLength + 1, maxDiameter};
    }

    /**
     * Builds an adjacency list representation of the tree.
     */
    private List<List<Integer>> buildGraph(List<Integer> parentNodes) {
        int size = parentNodes.size();
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (int i = 0; i < size; i++) {
            int parent = parentNodes.get(i);
            int child = i;
            if (parent != -1) {
                adjacencyList.get(child).add(parent);
                adjacencyList.get(parent).add(child);
            }
        }
        return adjacencyList;
    }

    /**
     * Finds the root node (node with parent -1).
     */
    private int findRoot(List<Integer> parentNodes) {
        for (int i = 0; i < parentNodes.size(); i++) {
            if (parentNodes.get(i) == -1) return i;
        }
        throw new IllegalArgumentException("Tree must have a root node.");
    }
}
