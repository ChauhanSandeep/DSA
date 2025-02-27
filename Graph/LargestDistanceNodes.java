package Graph;

import java.util.*;

/**
 * https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 */
public class LargestDistanceNodes {
    public static void main(String[] args) {
        List<Integer> input = Arrays.asList(-1, 0, 0, 0, 3);
        System.out.println(new LargestDistanceNodes().solve(input));
    }

    public int solve(List<Integer> input) {
        int size = input.size();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            adj.add(new ArrayList<>());
        }
        int root = -1;

        // Build the adjacency list
        for (int i = 0; i < size; i++) {
            int parent = input.get(i);
            if (parent == -1) {
                root = i;
                continue;
            }
            adj.get(i).add(parent);
            adj.get(parent).add(i);
        }

        // First BFS: Find the farthest node from root
        int farthestNode = bfs(adj, root)[0];

        // Second BFS: Find the max distance from the farthest node
        return bfs(adj, farthestNode)[1];
    }

    private int[] bfs(List<List<Integer>> adj, int start) {
        int size = adj.size();
        Queue<int[]> queue = new LinkedList<>();
        boolean[] visited = new boolean[size];
        queue.offer(new int[]{start, 0});
        visited[start] = true;

        int farthestNode = start;
        int maxDistance = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int node = curr[0];
            int dist = curr[1];

            // Update max distance
            if (dist > maxDistance) {
                maxDistance = dist;
                farthestNode = node;
            }

            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(new int[]{neighbor, dist + 1});
                }
            }
        }
        return new int[]{farthestNode, maxDistance};
    }
}
