package graphs.unionfind;

import java.util.*;

/**
 * NumberOfIslandsII.java
 *
 * Problem Statement:
 * You are given an empty 2D binary grid of size m x n. The grid represents a map where:
 * - 0 represents water
 * - 1 represents land
 * 
 * Initially, all cells are water. You are given an array positions where positions[i] = [ri, ci] 
 * is the position of the land being added at the ith operation.
 * 
 * Return an array of integers answer where answer[i] is the number of islands after adding the 
 * land at positions[i]. An island is surrounded by water and is formed by connecting adjacent 
 * lands horizontally or vertically.
 *
 * Example:
 * Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
 * Output: [1,1,2,3]
 * Explanation:
 * After adding land at (0,0): 1 island
 * After adding land at (0,1): 1 island (connects with previous)
 * After adding land at (1,2): 2 islands
 * After adding land at (2,1): 3 islands
 *
 * LeetCode link: https://leetcode.com/problems/number-of-islands-ii/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if we also need to support removing land (setting to water)?
 *    → Need to rebuild connected components or use more complex data structures.
 *  - Can you handle diagonal connections (8 directions instead of 4)?
 *    → Same algorithm, just check 8 neighbors instead of 4.
 *  - What if grid is very large but positions array is small?
 *    → Current solution already handles this efficiently with sparse representation.
 *  - How would you optimize for repeated queries on same grid?
 *    → Cache intermediate states or use persistent data structures.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 200 (Number of Islands): https://leetcode.com/problems/number-of-islands/
 *  - LeetCode 547 (Number of Provinces): https://leetcode.com/problems/number-of-provinces/
 *  - LeetCode 323 (Number of Connected Components in Undirected Graph): https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/
 */
public class NumberOfIslandsII {

    /**
     * Main method: Union-Find (Disjoint Set Union) approach.
     * Step-by-step:
     *  1. Initialize Union-Find structure for grid cells
     *  2. Track island count, initially 0
     *  3. For each position being added:
     *     a. If already land, skip (or add same count to result)
     *     b. Mark as land, increment island count
     *     c. Check all 4 neighbors (up, down, left, right)
     *     d. If neighbor is land and not in same set:
     *        - Union current cell with neighbor
     *        - Decrement island count (merged two islands)
     *     e. Add current island count to result
     *  4. Return result array
     *
     * Key Insight:
     * When adding land, it forms a new island (+1 count). Then check neighbors:
     * for each neighbor that's land and in different component, we merge islands
     * (-1 count per merge). Union-Find efficiently tracks and merges components.
     *
     * Algorithm: Union-Find with path compression and union by rank.
     * Time Complexity: O(k * α(m*n)), where k is positions length, α is inverse Ackermann (nearly O(1)).
     * Space Complexity: O(m*n) for parent and rank arrays.
     */
    public List<Integer> numIslands2(int rows, int cols, int[][] positions) {
        List<Integer> result = new ArrayList<>();
        if (positions == null || positions.length == 0) {
            return result;
        }

        UnionFind unionFind = new UnionFind(rows * cols);
        boolean[][] islandPresent = new boolean[rows][cols];  // Track which cells are land
        int islandCount = 0;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  // Up, Down, Left, Right
        
        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            
            // Skip if already land
            if (islandPresent[row][col]) {
                result.add(islandCount);
                continue;
            }
            
            // Mark as land and increment island count
            islandPresent[row][col] = true;
            islandCount++;
            
            
            // Check all 4 neighbors
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                // Check bounds and if neighbor is land
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && islandPresent[newRow][newCol]) {
                    int cellId = row * cols + col;  // Convert 2D to 1D index
                    int neighborCellId = newRow * cols + newCol;
                    
                    // If neighbor is in different component, merge
                    if (unionFind.find(cellId) != unionFind.find(neighborCellId)) {
                        unionFind.union(cellId, neighborCellId);
                        islandCount--;  // Merged two islands into one
                    }
                }
            }
            
            result.add(islandCount);
        }
        
        return result;
    }

    /**
     * Union-Find data structure with path compression and union by rank.
     */
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        // Find with path compression
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Path compression
            }
            return parent[x];
        }
        
        // Union by rank
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return;
            
            // Attach smaller rank tree under higher rank tree
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    /**
     * Alternative method: Union-Find with HashMap (Space optimized for sparse grids).
     * Step-by-step:
     *  1. Use HashMap instead of arrays to track only occupied cells
     *  2. More memory efficient when positions.length << m*n
     *  3. Same union-find logic but with HashMap-based parent tracking
     *
     * Key Insight:
     * When grid is very large but only few cells are land, HashMap saves space.
     * Only store parent/rank for cells that are actually land. Trade-off: slightly
     * slower due to HashMap overhead, but much better space complexity.
     *
     * Algorithm: Union-Find with HashMap.
     * Time Complexity: O(k * α(k)), where k is positions length.
     * Space Complexity: O(k), only store data for k land cells.
     */
    public List<Integer> numIslands2Optimized(int m, int n, int[][] positions) {
        List<Integer> result = new ArrayList<>();
        if (positions == null || positions.length == 0) {
            return result;
        }

        UnionFindMap uf = new UnionFindMap();
        int islandCount = 0;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] pos : positions) {
            int row = pos[0];
            int col = pos[1];
            int cellId = row * n + col;
            
            // Skip if already land
            if (uf.contains(cellId)) {
                result.add(islandCount);
                continue;
            }
            
            // Add new land
            uf.add(cellId);
            islandCount++;
            
            // Check neighbors
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n) {
                    int neighborId = newRow * n + newCol;
                    
                    if (uf.contains(neighborId) && 
                        uf.find(cellId) != uf.find(neighborId)) {
                        uf.union(cellId, neighborId);
                        islandCount--;
                    }
                }
            }
            
            result.add(islandCount);
        }
        
        return result;
    }

    /**
     * HashMap-based Union-Find for sparse grids.
     */
    static class UnionFindMap {
        private Map<Integer, Integer> parent;
        private Map<Integer, Integer> rank;
        
        public UnionFindMap() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }
        
        public boolean contains(int x) {
            return parent.containsKey(x);
        }
        
        public void add(int x) {
            parent.put(x, x);
            rank.put(x, 1);
        }
        
        public int find(int x) {
            if (parent.get(x) != x) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return;
            
            int rankX = rank.get(rootX);
            int rankY = rank.get(rootY);
            
            if (rankX < rankY) {
                parent.put(rootX, rootY);
            } else if (rankX > rankY) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rankX + 1);
            }
        }
    }
}

