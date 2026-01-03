package heaps;

import java.util.*;

/**
 *
 * You are given an n x n integer matrix grid where each value grid[i][j] represents the elevation 
 * at that point (i, j).
 * 
 * The rain starts to fall. At time t, the depth of the water everywhere is t. You can swim from 
 * a square to another 4-directionally adjacent square if and only if the elevation of both squares 
 * individually are at most t. You can swim infinite distances in zero time.
 * 
 * Starting at the top left square (0, 0), return the minimum time until you can reach the bottom 
 * right square (n-1, n-1).
 *
 * Example:
 * Input: grid = [[0,2],[1,3]]
 * Output: 3
 * Explanation:
 * At time 0, you are at grid[0][0] with elevation 0.
 * At time 1, you can move to grid[1][0] (elevation 1).
 * At time 2, you cannot move yet (grid[0][1] has elevation 2, grid[1][1] has elevation 3).
 * At time 3, you can move to grid[1][1].
 * Minimum time = 3.
 *
 * LeetCode link: https://leetcode.com/problems/swim-in-rising-water/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if you can move diagonally (8 directions)?
 *    → Same algorithm, just check 8 neighbors instead of 4.
 *  - Can you handle very large grids efficiently?
 *    → Current O(n² log n) is optimal; for massive grids, consider approximation algorithms.
 *  - What if there are obstacles (cells you can never visit)?
 *    → Mark obstacles, skip them during traversal, might return -1 if no path exists.
 *  - How would you find the actual path taken, not just the time?
 *    → Track parent pointers during traversal and backtrack from destination.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 778 (Swim in Rising Water): https://leetcode.com/problems/swim-in-rising-water/
 *  - LeetCode 1631 (Path With Minimum Effort): https://leetcode.com/problems/path-with-minimum-effort/
 *  - LeetCode 1102 (Path With Maximum Minimum Value): https://leetcode.com/problems/path-with-maximum-minimum-value/
 */
public class SwimInRisingWater {

    /**
     * Main method: Dijkstra's algorithm with min heap (Optimal for interviews).
     * Step-by-step:
     *  1. Use min heap to process cells in order of minimum time needed
     *  2. Track visited cells to avoid reprocessing
     *  3. Start from (0,0) with time = grid[0][0]
     *  4. For each cell popped from heap:
     *     a. If reached destination, return current time
     *     b. Explore all 4 neighbors
     *     c. For each unvisited neighbor:
     *        - Time to reach = max(current time, neighbor's elevation)
     *        - Add to heap with this time
     *  5. Continue until destination reached
     *
     * Key Insight:
     * This is shortest path problem where "cost" is the maximum elevation along path.
     * Use modified Dijkstra: instead of summing costs, track maximum elevation seen.
     * Min heap ensures we explore paths with lower maximum elevations first.
     *
     * Algorithm: Modified Dijkstra's algorithm.
     * Time Complexity: O(n² log n), each cell processed once, heap operations are O(log n²).
     * Space Complexity: O(n²) for visited array and heap.
     */
    public int swimInRisingWater(int[][] grid) {
        int size = grid.length;
        boolean[][] visited = new boolean[size][size];
        
        // Min heap: [time, row, col]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // Start from top-left with initial time = grid[0][0]
        minHeap.offer(new int[]{grid[0][0], 0, 0});
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            int time = current[0];
            int row = current[1];
            int col = current[2];
            
            // Skip if already visited
            if (visited[row][col]) {
                continue;
            }
            
            visited[row][col] = true;
            
            // Reached destination
            if (row == size - 1 && col == size - 1) {
                return time;
            }
            
            // Explore neighbors
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size && !visited[newRow][newCol]) {
                    
                    // Time to reach neighbor = max(current time, neighbor's elevation)
                    int newTime = Math.max(time, grid[newRow][newCol]);
                    minHeap.offer(new int[]{newTime, newRow, newCol});
                }
            }
        }
        
        return -1; // Should never reach here if grid is valid
    }

    /**
     * Alternative method: Binary Search + BFS/DFS.
     * Step-by-step:
     *  1. Binary search on answer (time t from 0 to max elevation)
     *  2. For each candidate time t:
     *     a. Use BFS/DFS to check if path exists using only cells with elevation ≤ t
     *     b. If path exists: try smaller t (search left)
     *     c. If no path: try larger t (search right)
     *  3. Return minimum t where path exists
     *
     * Key Insight:
     * If we can swim at time t, we can also swim at time t+1 (monotonic property).
     * Binary search finds minimum t where path becomes possible. BFS/DFS verifies
     * if path exists at given time by only visiting cells with elevation ≤ t.
     *
     * Algorithm: Binary Search + BFS.
     * Time Complexity: O(n² log(max_elevation)), binary search O(log max), BFS O(n²) each.
     * Space Complexity: O(n²) for BFS queue and visited array.
     */
    public int swimInWaterBinarySearch(int[][] grid) {
        int size = grid.length;
        int left = grid[0][0];
        int right = size * size - 1;  // Max possible elevation
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Check if path exists at time mid
            if (canReach(grid, mid)) {
                right = mid;  // Try smaller time
            } else {
                left = mid + 1;  // Need more time
            }
        }
        
        return left;
    }

    // Helper: Check if destination is reachable at given time using BFS
    private boolean canReach(int[][] grid, int time) {
        int n = grid.length;
        
        // If start or end elevation > time, impossible
        if (grid[0][0] > time || grid[n-1][n-1] > time) {
            return false;
        }
        
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            
            // Reached destination
            if (row == n - 1 && col == n - 1) {
                return true;
            }
            
            // Explore neighbors
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (newRow >= 0 && newRow < n && newCol >= 0 && newCol < n && 
                    !visited[newRow][newCol] && grid[newRow][newCol] <= time) {
                    
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        
        return false;
    }
}

