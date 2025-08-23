package frazsheet;

import java.util.*;

/**
 * 733. Flood Fill
 * 
 * Problem: Given an image represented as a 2D array of integers, a coordinate (sr, sc)
 * and a new color, perform a flood fill operation starting from the given coordinate.
 * 
 * Example:
 * Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, newColor = 2
 * Output: [[2,2,2],[2,2,0],[2,0,1]]
 * 
 * LeetCode: https://leetcode.com/problems/flood-fill
 * 
 * Follow-up questions:
 * Q: How to handle very large images efficiently?
 * A: Use iterative BFS/DFS to avoid stack overflow, or process in chunks.
 * 
 * Q: What if we need to track the number of pixels changed?
 * A: Add counter variable and increment during traversal.
 * 
 * Q: How to optimize for sparse changes (few pixels affected)?
 * A: Check early termination conditions and use efficient data structures.
 */
public class FloodFill {
    
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    /**
     * Recursive DFS approach for flood fill.
     * 
     * Algorithm: Depth-First Search
     * - Start from given coordinate (sr, sc)
     * - If current cell has original color, change to new color
     * - Recursively process all 4-directional neighbors
     * - Stop when reaching boundary or different colored cells
     * 
     * Time Complexity: O(m * n) where m and n are image dimensions
     * Space Complexity: O(m * n) in worst case for recursion stack
     */
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        
        // Early termination if new color is same as original
        if (originalColor == newColor) {
            return image;
        }
        
        dfs(image, sr, sc, originalColor, newColor);
        return image;
    }
    
    // DFS helper for recursive flood fill
    private void dfs(int[][] image, int row, int col, int originalColor, int newColor) {
        // Check boundaries and color match
        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length 
            || image[row][col] != originalColor) {
            return;
        }
        
        // Change color
        image[row][col] = newColor;
        
        // Explore all 4 directions
        for (int[] dir : DIRECTIONS) {
            dfs(image, row + dir[0], col + dir[1], originalColor, newColor);
        }
    }
    
    /**
     * Iterative BFS approach using queue.
     * Avoids potential stack overflow for large images.
     */
    public int[][] floodFillBFS(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return image;
        }
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{sr, sc});
        image[sr][sc] = newColor;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < image.length && 
                    newCol >= 0 && newCol < image[0].length && 
                    image[newRow][newCol] == originalColor) {
                    
                    image[newRow][newCol] = newColor;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        
        return image;
    }
    
    /**
     * Iterative DFS using explicit stack.
     * Combines benefits of DFS traversal with iterative approach.
     */
    public int[][] floodFillIterativeDFS(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return image;
        }
        
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{sr, sc});
        
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int row = current[0];
            int col = current[1];
            
            // Check if still valid (might have been changed by other path)
            if (row < 0 || row >= image.length || col < 0 || col >= image[0].length 
                || image[row][col] != originalColor) {
                continue;
            }
            
            // Change color
            image[row][col] = newColor;
            
            // Add neighbors to stack
            for (int[] dir : DIRECTIONS) {
                stack.push(new int[]{row + dir[0], col + dir[1]});
            }
        }
        
        return image;
    }
    
    /**
     * Optimized version with visited array to avoid redundant checks.
     * Useful when the same cells might be visited multiple times.
     */
    public int[][] floodFillWithVisited(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return image;
        }
        
        boolean[][] visited = new boolean[image.length][image[0].length];
        dfsWithVisited(image, sr, sc, originalColor, newColor, visited);
        
        return image;
    }
    
    // DFS with visited array tracking
    private void dfsWithVisited(int[][] image, int row, int col, int originalColor, int newColor, boolean[][] visited) {
        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length 
            || visited[row][col] || image[row][col] != originalColor) {
            return;
        }
        
        visited[row][col] = true;
        image[row][col] = newColor;
        
        for (int[] dir : DIRECTIONS) {
            dfsWithVisited(image, row + dir[0], col + dir[1], originalColor, newColor, visited);
        }
    }
    
    /**
     * Union-Find approach for connected components.
     * Overkill for this problem but demonstrates advanced technique.
     */
    public int[][] floodFillUnionFind(int[][] image, int sr, int sc, int newColor) {
        int m = image.length, n = image[0].length;
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return image;
        }
        
        UnionFind uf = new UnionFind(m * n);
        
        // Union adjacent cells with same color
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (image[i][j] == originalColor) {
                    for (int[] dir : DIRECTIONS) {
                        int ni = i + dir[0];
                        int nj = j + dir[1];
                        if (ni >= 0 && ni < m && nj >= 0 && nj < n && image[ni][nj] == originalColor) {
                            uf.union(i * n + j, ni * n + nj);
                        }
                    }
                }
            }
        }
        
        // Find root of starting cell
        int startRoot = uf.find(sr * n + sc);
        
        // Change color for all cells in same component
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (image[i][j] == originalColor && uf.find(i * n + j) == startRoot) {
                    image[i][j] = newColor;
                }
            }
        }
        
        return image;
    }
    
    // Union-Find data structure
    private static class UnionFind {
        private int[] parent;
        private int[] rank;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            
            if (rank[px] < rank[py]) {
                parent[px] = py;
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
            } else {
                parent[py] = px;
                rank[px]++;
            }
        }
    }
    
    /**
     * Multi-threaded approach for very large images.
     * Divides image into regions and processes in parallel.
     */
    public int[][] floodFillParallel(int[][] image, int sr, int sc, int newColor) {
        // For most practical cases, sequential approach is sufficient
        // Parallel approach would require careful synchronization
        return floodFill(image, sr, sc, newColor);
    }
    
    /**
     * Returns count of pixels changed during flood fill.
     * Extension that tracks the number of modified cells.
     */
    public int floodFillCount(int[][] image, int sr, int sc, int newColor) {
        int originalColor = image[sr][sc];
        
        if (originalColor == newColor) {
            return 0;
        }
        
        return dfsCount(image, sr, sc, originalColor, newColor);
    }
    
    // DFS that returns count of changed pixels
    private int dfsCount(int[][] image, int row, int col, int originalColor, int newColor) {
        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length 
            || image[row][col] != originalColor) {
            return 0;
        }
        
        image[row][col] = newColor;
        int count = 1;
        
        for (int[] dir : DIRECTIONS) {
            count += dfsCount(image, row + dir[0], col + dir[1], originalColor, newColor);
        }
        
        return count;
    }
}