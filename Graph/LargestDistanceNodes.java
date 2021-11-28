package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://www.interviewbit.com/problems/largest-distance-between-nodes-of-a-tree/
 * [-1, 0, 0, 0, 3]
 *              0
 *            / | \
 *          1   2  3
 *                   \
 *                    4
 * Output: 3
 * Explanation: path 1->0->3->4
 *
 */
public class LargestDistanceNodes {
    public static void main(String[] args) {
        ArrayList<Integer> input = (ArrayList<Integer>) Stream.of(-1).collect(Collectors.toList());
        System.out.println(new LargestDistanceNodes().solve(input));
    }

    public int solve(ArrayList<Integer> input) {
        int size = input.size();
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            adj.add(new ArrayList<>());
        }
        int root = -1;
        for (int i = 0; i < size; i++) {
            int num = input.get(i);
            if (num == -1) {
                root = i;
                continue;
            }
            adj.get(i).add(num);
            adj.get(num).add(i);
        }
        // Find the node which is farthest from root node using BFS
        int node = bfs(adj, root, size);
        // Find the maximum distance from farthest node using modified DFS
        return dfs(adj, node, size);
    }

    public int bfs(ArrayList<ArrayList<Integer>> adj, int root, int size) {
        boolean[] visited = new boolean[size];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.poll();
            if (!visited[root]) {
                visited[root] = true;
                for (Integer neighbor : adj.get(root)) {
                    if (!visited[neighbor]) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return root;
    }

    public int dfs(ArrayList<ArrayList<Integer>> adj, int currNode, int size) {
        int max = 0;
        Stack<Integer> nodeStack = new Stack<>();
        Stack<Integer> distStack = new Stack<>();
        boolean[] visited = new boolean[size];

        nodeStack.push(currNode);
        distStack.push(0);

        while (!nodeStack.isEmpty()) {
            currNode = nodeStack.pop();
            int d = distStack.pop();
            if (!visited[currNode]) {
                visited[currNode] = true;
                for (Integer neighbor : adj.get(currNode)) {
                    if (!visited[neighbor]) {
                        max = Math.max(max, d + 1);
                        nodeStack.push(neighbor);
                        distStack.push(d + 1);
                    }
                }
            }
        }
        return max;
    }
}
