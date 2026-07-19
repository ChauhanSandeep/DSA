package heaps;

import java.util.*;

/**
 * Problem: Swim in Rising Water
 *
 * Each grid value is an elevation, and at time t you may enter cells with
 * elevation at most t. Return the minimum time needed to reach the bottom-right
 * cell from the top-left cell using four-directional moves.
 *
 * Leetcode: https://leetcode.com/problems/swim-in-rising-water/ (Hard)
 * Rating:   2097 (zerotrac Elo)
 * Pattern:  Heap | Dijkstra on maximum path cost | Binary search alternative
 *
 * Example:
 *   Input:  grid = [[0,2],[1,3]]
 *   Output: 3
 *   Why:    every path to the destination must enter elevation 3, and time 3 makes it reachable.
 *
 * Follow-ups:
 *   1. Can you solve it with binary search?
 *      Binary search time and BFS to test whether the destination is reachable.
 *   2. What if diagonal moves are allowed?
 *      Add the four diagonal directions to the neighbor loop.
 *   3. How would you return the path too?
 *      Store parent pointers when a cell is first accepted.
 *   4. What if cells can be blocked forever?
 *      Skip blocked cells during expansion and return -1 if the heap empties.
 *
 * Related: Path With Minimum Effort (1631), Path With Maximum Minimum Value (1102).
 */

public class SwimInRisingWater {

    public static void main(String[] args) {
        SwimInRisingWater solver = new SwimInRisingWater();
        int[][][] inputs = { {{0, 2}, {1, 3}}, {{0}} };
        int[] expected = {3, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.swimInRisingWater(inputs[i]);
            System.out.printf("grid=%s -> %d  expected=%d%n",
                Arrays.deepToString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: a path's cost is the maximum elevation seen along it, not the sum
     * of elevations. This is still Dijkstra-shaped: always expand the cell with
     * the currently smallest possible arrival time, and the first time the target
     * is removed from the heap is optimal.
     *
     * Algorithm:
     *   1. Seed a min heap with the start cell and time grid[0][0].
     *   2. Poll the lowest-time cell, skipping it if it was already visited.
     *   3. Return the time when the destination cell is polled.
     *   4. Offer each unvisited neighbor with max(current time, neighbor elevation).
     *
     * Time:  O(n^2 log n) - each cell can enter the heap and heap operations are logarithmic.
     * Space: O(n^2) - visited matrix and heap can both grow with the grid.
     *
     * @param grid square elevation grid
     * @return minimum time needed to reach the bottom-right cell
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

    /** Checks whether the destination is reachable using only cells at most time. */
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

