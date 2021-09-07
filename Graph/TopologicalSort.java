package Graph;

import java.util.ArrayList;
import java.util.Stack;

public class TopologicalSort {

    public static int[] topoSort(int N, ArrayList<ArrayList<Integer>> adj) {
        Stack<Integer> stack = new Stack<>();
        int[] visited = new int[N];

        for(int i=0; i<N; i++) {
            if(visited[i] == 0) {
                findTopoSort(i, visited, adj, stack);
            }
        }

        // Topological sort is created in stack in reverse order
        int[] result = new int[N];
        int index = 0;
        while(!stack.isEmpty()) {
            result[index++] = stack.pop();
        }
        return result;
    }

    private static void findTopoSort(int node, int[] visited, ArrayList<ArrayList<Integer>> adj, Stack<Integer> stack) {
        //1. Add to visited array
        visited[node] = 1;

        //2. Do DFS on this node
        for(Integer i: adj.get(node)) {
            if(visited[i] == 0) {
                findTopoSort(i, visited, adj, stack);
            }
        }

        //3. Push node to the stack
        stack.push(node);
    }
}
