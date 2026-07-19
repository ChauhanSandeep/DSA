package trees.traversal;

import java.util.*;

    /**
     * Intuition: first root the tree at 0. postOrder counts each subtree and
     * computes distances from 0. Then preOrder reroots across every edge: moving
     * root from node to child brings count[child] nodes one step closer and the
     * remaining n - count[child] nodes one step farther away.
     *
     * Algorithm:
     *   1. Build an undirected adjacency structure.
     *   2. postOrder fills count[node] and res[0] using subtree contributions.
     *   3. preOrder reroots from each node to each child with the distance formula.
     *   4. Return res after every node has been used as a root.
     *
     * Time:  O(n) - both DFS passes traverse each edge once.
     * Space: O(n) - adjacency sets, arrays, and recursion stack.
     *
     * @param n number of nodes labeled 0 to n - 1
     * @param edges undirected tree edges
     * @return sum of distances from each node to all other nodes
     */
public class SumOfDistancesInTree {

    public static void main(String[] args) {
        SumOfDistancesInTree solver = new SumOfDistancesInTree();
        int[][] edges = {{0, 1}, {0, 2}, {2, 3}, {2, 4}, {2, 5}};
        int[][] emptyEdges = {};

        System.out.printf("n=%d edges=%s -> %s  expected=%s%n",
            6, Arrays.deepToString(edges), Arrays.toString(solver.sumOfDistancesInTree(6, edges)),
            Arrays.toString(new int[] {8, 12, 6, 10, 10, 10}));
        System.out.printf("n=%d edges=%s -> %s  expected=%s%n",
            1, Arrays.deepToString(emptyEdges), Arrays.toString(new SumOfDistancesInTree().sumOfDistancesInTree(1, emptyEdges)),
            Arrays.toString(new int[] {0}));
    }

    private int[] count;
    private int[] res;
    private List<Set<Integer>> tree;

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // Initialize the tree as an adjacency list
        tree = new ArrayList<>();
        count = new int[n];
        res = new int[n];

        for (int i = 0; i < n; i++) {
            tree.add(new HashSet<>());
        }

        // Build the tree
        for (int[] edge : edges) {
            tree.get(edge[0]).add(edge[1]);
            tree.get(edge[1]).add(edge[0]);
        }

        // First post-order traversal to calculate count and res[0]
        postOrder(0, -1);
        // Second pre-order traversal to calculate res for all nodes
        preOrder(0, -1, n);

        return res;
    }

    // Computes subtree sizes and the distance sum for the initial root 0.
    private void postOrder(int node, int parent) {
        for (int child : tree.get(node)) {
            if (child == parent) continue;
            postOrder(child, node);
            count[node] += count[child];
            res[node] += res[child] + count[child];
        }
        count[node]++;
    }

    // Reroots the distance sum from each node to each child.
    private void preOrder(int node, int parent, int n) {
        for (int child : tree.get(node)) {
            if (child == parent) continue;
            res[child] = res[node] - count[child] + n - count[child];
            preOrder(child, node, n);
        }
    }
}
