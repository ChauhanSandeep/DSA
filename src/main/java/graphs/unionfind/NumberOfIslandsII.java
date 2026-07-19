package graphs.unionfind;

import java.util.*;

/**
 * Problem: Number of Islands II
 *
 * Start with an all-water grid and add land cells one operation at a time. After
 * each addition, return the current number of islands, where islands connect
 * horizontally and vertically.
 *
 * Leetcode: https://leetcode.com/problems/number-of-islands-ii/ (Hard)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Graph | Union-Find on grid | Dynamic connectivity
 *
 * Example:
 *   Input:  m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
 *   Output: [1,1,2,3]
 *   Why:    the second land touches the first, while the later two additions form separate islands.
 *
 * Follow-ups:
 *   1. Support removing land?
 *      Standard DSU cannot split components; use offline reverse processing or rebuild affected components.
 *   2. Allow diagonal connections?
 *      Check eight directions before unioning neighboring land cells.
 *   3. Very large grid with few positions?
 *      Use hash maps keyed by active cell id instead of allocating rows * cols arrays.
 *
 * Related: Number of Islands (200), Number of Provinces (547), Dynamic Connectivity.
 */
public class NumberOfIslandsII {

    public static void main(String[] args) {
        NumberOfIslandsII solver = new NumberOfIslandsII();
        int[][] positions1 = {{0, 0}, {0, 1}, {1, 2}, {2, 1}};
        int[][] positions2 = {{0, 0}, {0, 0}};

        System.out.printf("rows=3 cols=3 positions=%s -> %s  expected=[1, 1, 2, 3]%n",
            Arrays.deepToString(positions1), solver.numIslands2(3, 3, positions1));
        System.out.printf("rows=1 cols=1 positions=%s -> %s  expected=[1, 1]%n",
            Arrays.deepToString(positions2), solver.numIslands2(1, 1, positions2));
    }

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
                int neighborRow = row + dir[0];
                int neighborCol = col + dir[1];
                
                // Check bounds and if neighbor is land
                if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols 
                        && islandPresent[neighborRow][neighborCol]) {
                    int cellId = row * cols + col;  // Convert 2D to 1D index
                    int neighborCellId = neighborRow * cols + neighborCol;
                    
                    // If neighbor is in different component, merge
                    if (unionFind.findRoot(cellId) != unionFind.findRoot(neighborCellId)) {
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
        public int findRoot(int x) {
            if (parent[x] != x) {
                parent[x] = findRoot(parent[x]);  // Path compression
            }
            return parent[x];
        }
        
        // Union by rank
        public void union(int x, int y) {
            int rootX = findRoot(x);
            int rootY = findRoot(y);
            
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
}

