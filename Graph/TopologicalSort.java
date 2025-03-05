package Graph;

import java.util.*;

public class TopologicalSort {

    private static final int UNVISITED = 0;
    private static final int VISITING = 1;
    private static final int VISITED = 2;

    public static int[] topoSort(int N, ArrayList<ArrayList<Integer>> adj) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] visited = new int[N];
        boolean[] onStack = new boolean[N]; // For cycle detection

        for (int i = 0; i < N; i++) {
            if (visited[i] == UNVISITED) {
                if (!findTopoSort(i, visited, adj, stack, onStack)) {
                    return new int[0]; // Cycle detected, return empty array
                }
            }
        }

        // Convert stack to result array
        int[] result = new int[N];
        int index = 0;
        while (!stack.isEmpty()) {
            result[index++] = stack.pop();
        }
        return result;
    }

    private static boolean findTopoSort(int node, int[] visited, ArrayList<ArrayList<Integer>> adj, Deque<Integer> stack, boolean[] onStack) {
        visited[node] = VISITING;
        onStack[node] = true;

        for (Integer neighbor : adj.get(node)) {
            if (visited[neighbor] == UNVISITED) {
                if (!findTopoSort(neighbor, visited, adj, stack, onStack)) {
                    return false; // Cycle detected
                }
            } else if (onStack[neighbor]) {
                return false; // Cycle detected
            }
        }

        visited[node] = VISITED;
        onStack[node] = false;
        stack.push(node);
        return true;
    }

    public static void main(String[] args) {
        int N = 6;
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < N; i++) adj.add(new ArrayList<>());

        adj.get(5).add(2);
        adj.get(5).add(0);
        adj.get(4).add(0);
        adj.get(4).add(1);
        adj.get(2).add(3);
        adj.get(3).add(1);

        int[] result = topoSort(N, adj);
        if (result.length == 0) {
            System.out.println("Cycle detected! Topological sorting not possible.");
        } else {
            System.out.println("Topological Sort: " + Arrays.toString(result));
        }
    }
}
