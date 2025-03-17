package Graph;

import java.util.*;

/**
 * Problem: Find the largest distance (diameter) between any two nodes in a tree.
 * 
 * Intuition:
 * - A tree's **diameter** is the longest path between two of its nodes.
 * - This can be found using **two BFS traversals**:
 *   1. **Start BFS from any node (usually root)** → Find the farthest node (`nodeA`).
 *   2. **Start BFS from `nodeA`** → Find the farthest node (`nodeB`), and record the max distance.
 * - This works because BFS guarantees that we find the longest path in an unweighted graph.
 * 
 * Algorithm:
 * 1. **Build the adjacency list representation** of the tree.
 * 2. **Perform the first BFS from the root** to find `nodeA` (farthest from root).
 * 3. **Perform the second BFS from `nodeA`** to find `nodeB` and get the max distance.
 * 4. Return the maximum distance found.
 * 
 * Time Complexity: O(N) - We perform **two BFS traversals** in O(N) time.
 * Space Complexity: O(N) - For adjacency list and BFS queue.
 * 
 * LeetCode/InterviewBit Link: https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 */
public class LargestDistanceNodes {
    public static void main(String[] args) {
        List<Integer> parentNodes = Arrays.asList(-1, 0, 0, 0, 3);
        System.out.println("Largest Distance: " + new LargestDistanceNodes().findLargestDistance(parentNodes));
    }

    /**
     * Finds the largest distance (diameter) between any two nodes in the given tree.
     *
     * @param parentNodes List representing the parent-child relationships in the tree.
     * @return The largest distance (diameter) between two nodes.
     */
    public int findLargestDistance(List<Integer> parentNodes) {
        int size = parentNodes.size();
        List<List<Integer>> adjacencyList = buildGraph(parentNodes);

        // First BFS: Find the farthest node from the root
        int farthestNode = bfs(adjacencyList, findRoot(parentNodes))[0];

        // Second BFS: Find the max distance from the farthest node found in the first BFS
        return bfs(adjacencyList, farthestNode)[1];
    }

    /**
     * Builds an adjacency list representation of the tree.
     *
     * @param parentNodes List where the index represents a node and the value represents its parent.
     * @return Adjacency list representation of the tree.
     */
    private List<List<Integer>> buildGraph(List<Integer> parentNodes) {
        int size = parentNodes.size();
        List<List<Integer>> adjacencyList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (int i = 0; i < size; i++) {
            int parent = parentNodes.get(i);
            if (parent != -1) {
                adjacencyList.get(i).add(parent);
                adjacencyList.get(parent).add(i);
            }
        }
        return adjacencyList;
    }

    /**
     * Finds the root node (node with parent -1).
     *
     * @param parentNodes List where the index represents a node and the value represents its parent.
     * @return The root node index.
     */
    private int findRoot(List<Integer> parentNodes) {
        for (int i = 0; i < parentNodes.size(); i++) {
            if (parentNodes.get(i) == -1) return i;
        }
        return -1; // This should never happen as there is always a root.
    }

    /**
     * Performs BFS to find the farthest node and its distance.
     *
     * @param adjacencyList The adjacency list representation of the tree.
     * @param startNode The node from which BFS starts.
     * @return An array where:
     *         - result[0] is the farthest node found.
     *         - result[1] is the distance to the farthest node.
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

            // Update max distance and farthest node
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }

            // Traverse all adjacent nodes
            for (int neighbor : adjacencyList.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, distance + 1});
                }
            }
        }
        return new int[]{farthestNode, maxDistance};
    }
}
