package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
        ArrayList<Integer> input = (ArrayList<Integer>) Stream.of(-1, 0, 0, 0, 3).collect(Collectors.toList());
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
        System.out.println("largest distance node is " + node);
        // Find the maximum distance from farthest node using modified DFS
        return dfs(adj, node, size);
    }

    public int bfs(ArrayList<ArrayList<Integer>> adj, int root, int size) {
        boolean[] visited = new boolean[size];
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.offer(root);
        visited[root] = true;
        while(!queue.isEmpty()) {
            root = queue.poll();
            for(Integer neighbor: adj.get(root)) {
                if(!visited[neighbor]) {
                    queue.offer(neighbor);
                    visited[neighbor] = true;
                }

            }
        }
        return root;
    }

    public int dfs(ArrayList<ArrayList<Integer>> adj, int node, int size) {
        int max = 0;
        Stack<Integer> distStack = new Stack<>();
        Stack<Integer> nodeStack = new Stack<>();
        boolean[] visited = new boolean[size];

        nodeStack.push(node);
        distStack.push(0);
        visited[node] = true;

        while(!nodeStack.isEmpty()) {
            int currNode = nodeStack.pop();
            int currDist = distStack.pop();
            max = Math.max(max, currDist);
            for(Integer neighbor: adj.get(currNode)) {
                if(!visited[neighbor]) {
                    visited[neighbor] = true;
                    nodeStack.push(neighbor);
                    distStack.push(currDist + 1);
                }
            }
        }
        return max;
    }
}
