package Graph;

/**
 * In this problem, a tree is an undirected graph that is connected and has no cycles.
 * You are given a graph that started as a tree with n nodes labeled from 1 to n,
 * with one additional edge added. The added edge has two different vertices chosen from 1 to n,
 * and was not an edge that already existed.
 * 
 * The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that
 * there is an undirected edge between nodes ai and bi in the graph.
 * 
 * Return an edge that can be removed so that the resulting graph is a tree of n nodes.
 * If there are multiple answers, return the answer that occurs last in the input.
 * 
 * Example 1:
 * Input: edges = [[1,2],[1,3],[2,3]]
 * Output: [2,3]
 * 
 * Example 2:
 * Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 * Output: [1,4]
 * 
 * LeetCode: https://leetcode.com/problems/redundant-connection/
 * 
 * Follow-up Questions:
 * 1. How would you handle the case where multiple edges can be removed to form a tree?
 *    - The problem guarantees exactly one redundant edge, but if there were multiple, we'd need to return all of them.
 * 2. What if the graph is very large (e.g., 10^5 nodes and edges)?
 *    - The Union-Find solution with path compression and union by rank is efficient (near O(1) per operation).
 * 3. How would you modify the solution to return all possible redundant edges?
 *    - We could collect all edges that form cycles during the union process.
 * 
 * Related Problems:
 * - Redundant Connection II (https://leetcode.com/problems/redundant-connection-ii/)
 * - Graph Valid Tree (https://leetcode.com/problems/graph-valid-tree/)
 */
public class RedundantConnection {
    /**
     * Finds the redundant connection in the given edges using Union-Find.
     * 
     * @param edges Array of undirected edges
     * @return The redundant edge that can be removed
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        UnionFind uf = new UnionFind(n + 1); // Nodes are 1-based
        
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            if (!uf.union(u, v)) {
                return edge; // Found the edge that forms a cycle
            }
        }
        
        return new int[0]; // Shouldn't reach here as per problem statement
    }
    
    /**
     * Union-Find (Disjoint Set) data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            
            // Initialize each element as its own parent
            for (int i = 0; i < size; i++) {
                parent[i] = i;
            }
        }
        
        /**
         * Finds the root of the set containing x with path compression.
         */
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        /**
         * Unions the sets containing x and y.
         * 
         * @return true if the union was successful, false if x and y are already in the same set
         */
        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return false; // Already in the same set, this edge forms a cycle
            }
            
            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            
            return true;
        }
    }
    
    /**
     * DFS solution to find the redundant connection.
     * This approach is less efficient than Union-Find for this problem.
     */
    public int[] findRedundantConnectionDFS(int[][] edges) {
        // Build adjacency list
        Map<Integer, List<Integer>> graph = new java.util.HashMap<>();
        
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            
            // Check if there's already a path between u and v
            boolean[] visited = new boolean[edges.length + 1];
            if (hasPath(graph, u, v, visited)) {
                return edge;
            }
            
            // Add the edge to the graph
            graph.computeIfAbsent(u, k -> new java.util.ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new java.util.ArrayList<>()).add(u);
        }
        
        return new int[0]; // Shouldn't reach here as per problem statement
    }
    
    /**
     * Helper method to check if there's a path between u and v using DFS.
     */
    private boolean hasPath(Map<Integer, List<Integer>> graph, int u, int v, boolean[] visited) {
        if (u == v) {
            return true;
        }
        
        visited[u] = true;
        
        if (graph.containsKey(u)) {
            for (int neighbor : graph.get(u)) {
                if (!visited[neighbor] && hasPath(graph, neighbor, v, visited)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
