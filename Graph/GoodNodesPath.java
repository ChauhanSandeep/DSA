package Graph;

import java.util.*;

public class GoodNodesPath {
    public static void main(String[] args) {
        int[] goodNodes = {0, 1, 0, 1, 1, 1}; // 0-based indexing
        int[][] relations = {
                {1, 2},
                {1, 5},
                {1, 6},
                {2, 3},
                {2, 4}
        };
        int k = 1;
        int result = new GoodNodesPath().countValidPaths(goodNodes, relations, k);
        System.out.println(result);
    }

    List<List<Integer>> adj;
    int[] goodNodes;

    public int countValidPaths(int[] goodNodes, int[][] relations, int maxGoodNodes) {
        int size = goodNodes.length;
        if (size == 0) return 0;

        this.goodNodes = goodNodes;
        adj = new ArrayList<>();

        // Initialize adjacency list
        for (int i = 0; i <= size; i++) {
            adj.add(new ArrayList<>());
        }

        // Build tree
        for (int[] relation : relations) {
            adj.get(relation[0]).add(relation[1]);
            adj.get(relation[1]).add(relation[0]);
        }

        // Start DFS from root (node 1), with 0 good nodes counted
        return dfs(1, -1, 0, maxGoodNodes);
    }

    private int dfs(int node, int parent, int goodCount, int maxGoodNodes) {
        if (goodNodes[node - 1] == 1) { // Adjusting for 0-based index
            goodCount++;
        }
        if (goodCount > maxGoodNodes) {
            return 0; // Path invalid
        }

        // If the node is a leaf (only one edge to parent), it's a valid path
        if (adj.get(node).size() == 1 && adj.get(node).get(0) == parent) {
            return 1;
        }

        int validPaths = 0;
        for (int neighbor : adj.get(node)) {
            if (neighbor != parent) { // Avoid revisiting parent
                validPaths += dfs(neighbor, node, goodCount, maxGoodNodes);
            }
        }
        return validPaths;
    }
}
