package Graph;

/**
 * In a N x N grid composed of 1 x 1 squares, each 1 x 1 square consists of a '/', '\', or blank space ' '.
 * These characters divide the square into contiguous regions.
 * 
 * Return the number of regions.
 * 
 * Example 1:
 * Input: [" /",
 *         "/ "]
 * Output: 2
 * Explanation: The 2x2 grid is as follows:
 *   | |
 *  -+-+-
 *   |/|
 *  -+-+-
 *   | |
 * 
 * Example 2:
 * Input: [" /",
 *         "  "]
 * Output: 1
 * 
 * LeetCode: https://leetcode.com/problems/regions-cut-by-slashes/
 * 
 * Follow-up Questions:
 * 1. How would you handle larger grids (e.g., 1000x1000)?
 *    - The Union-Find solution is efficient with O(α(n)) per operation, making it suitable for large grids.
 * 2. What if we needed to find the area of each region?
 *    - We could modify the solution to track the size of each connected component.
 * 3. How would you handle diagonal slashes or other types of dividers?
 *    - The solution can be extended by adjusting the cell division and union logic.
 * 
 * Related Problems:
 * - Number of Islands (https://leetcode.com/problems/number-of-islands/)
 * - Number of Provinces (https://leetcode.com/problems/number-of-provinces/)
 */
public class RegionsCutBySlashes {
    /**
     * Calculates the number of regions formed by the slashes in the grid.
     * 
     * @param grid Array of strings representing the grid
     * @return Number of regions
     */
    public int regionsBySlashes(String[] grid) {
        int n = grid.length;
        // Each cell is divided into 4 triangles (0: top, 1: right, 2: bottom, 3: left)
        DSU dsu = new DSU(4 * n * n);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int base = 4 * (i * n + j);
                char c = grid[i].charAt(j);
                
                // Connect triangles within the same cell
                if (c == ' ') {
                    // Connect all 4 triangles if it's a space
                    dsu.union(base, base + 1);
                    dsu.union(base, base + 2);
                    dsu.union(base, base + 3);
                } else if (c == '/') {
                    // Connect top with left, and right with bottom
                    dsu.union(base, base + 3);
                    dsu.union(base + 1, base + 2);
                } else { // '\'
                    // Connect top with right, and bottom with left
                    dsu.union(base, base + 1);
                    dsu.union(base + 2, base + 3);
                }
                
                // Connect with right neighbor (if exists)
                if (j + 1 < n) {
                    dsu.union(base + 1, base + 4 + 3); // current right -> next left
                }
                
                // Connect with bottom neighbor (if exists)
                if (i + 1 < n) {
                    dsu.union(base + 2, base + 4 * n); // current bottom -> next top
                }
            }
        }
        
        return dsu.getCount();
    }
    
    /**
     * Disjoint Set Union (Union-Find) data structure with path compression and union by rank.
     */
    private static class DSU {
        private final int[] parent;
        private final int[] rank;
        private int count;
        
        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) {
                return; // Already in the same set
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
            
            count--; // Decrease the number of connected components
        }
        
        public int getCount() {
            return count;
        }
    }
    
    /**
     * Alternative solution using DFS to count regions.
     * This approach converts the grid into a graph and counts connected components.
     */
    public int regionsBySlashesDFS(String[] grid) {
        int n = grid.length;
        // Scale up the grid by 3x to represent each cell with 3x3 pixels
        boolean[][] scaled = new boolean[3 * n][3 * n];
        
        // Fill the scaled grid
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i].charAt(j) == '/') {
                    scaled[3 * i][3 * j + 2] = true;
                    scaled[3 * i + 1][3 * j + 1] = true;
                    scaled[3 * i + 2][3 * j] = true;
                } else if (grid[i].charAt(j) == '\\') {
                    scaled[3 * i][3 * j] = true;
                    scaled[3 * i + 1][3 * j + 1] = true;
                    scaled[3 * i + 2][3 * j + 2] = true;
                }
            }
        }
        
        // Count connected components in the scaled grid
        int count = 0;
        for (int i = 0; i < 3 * n; i++) {
            for (int j = 0; j < 3 * n; j++) {
                if (!scaled[i][j]) {
                    dfs(scaled, i, j);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    // DFS helper for the alternative solution
    private void dfs(boolean[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j]) {
            return;
        }
        
        grid[i][j] = true; // Mark as visited
        
        // Explore all four directions
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
}
