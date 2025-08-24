package Graph;

import java.util.*;

/**
 * You are asked to cut off all the trees in a forest for a golf event. The forest is represented as an m x n matrix.
 * In this matrix:
 * - 0 means the cell cannot be walked through.
 * - 1 represents an empty cell that can be walked through.
 * - A number greater than 1 represents a tree in a cell that can be walked through, and this number is the tree's height.
 * 
 * You start from the point (0, 0) and you must cut the trees in order of their heights (increasing order).
 * 
 * Example 1:
 * Input: forest = [[1,2,3],[0,0,4],[7,6,5]]
 * Output: 6
 * Explanation: The first step is to cut tree at (0,1) for 2, then (0,2) for 3, then (2,2) for 5, and finally (2,1) for 6.
 * 
 * Example 2:
 * Input: forest = [[1,2,3],[0,0,0],[7,6,5]]
 * Output: -1
 * Explanation: The tree at (2,0) cannot be reached.
 * 
 * LeetCode: https://leetcode.com/problems/cut-off-trees-for-golf-event/
 * 
 * Follow-up Questions:
 * 1. What if the forest is very large (e.g., 1000x1000)?
 *    - We can optimize by using A* search with a good heuristic instead of BFS for path finding.
 * 2. How would you handle multiple people cutting trees simultaneously?
 *    - We could use a priority queue to coordinate multiple workers and find optimal paths.
 * 3. What if some trees have the same height?
 *    - The problem guarantees all heights are unique.
 * 
 * Related Problems:
 * - Shortest Path in Binary Matrix (https://leetcode.com/problems/shortest-path-in-binary-matrix/)
 * - The Maze II (https://leetcode.com/problems/the-maze-ii/)
 */
public class CutOffTreesForGolfEvent {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    
    /**
     * Main method to calculate the minimum steps to cut all trees in order.
     * 
     * @param forest The 2D grid representing the forest
     * @return Minimum steps to cut all trees, or -1 if not possible
     */
    public int cutOffTree(List<List<Integer>> forest) {
        if (forest == null || forest.isEmpty() || forest.get(0).get(0) == 0) {
            return -1;
        }
        
        // Collect all trees and sort them by height
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < forest.size(); i++) {
            for (int j = 0; j < forest.get(0).size(); j++) {
                int height = forest.get(i).get(j);
                if (height > 1) {
                    trees.add(new int[]{height, i, j});
                }
            }
        }
        Collections.sort(trees, (a, b) -> Integer.compare(a[0], b[0]));
        
        int totalSteps = 0;
        int startRow = 0, startCol = 0;
        
        // Process each tree in order of height
        for (int[] tree : trees) {
            int endRow = tree[1];
            int endCol = tree[2];
            int steps = bfs(forest, startRow, startCol, endRow, endCol);
            
            if (steps == -1) {
                return -1;
            }
            
            totalSteps += steps;
            startRow = endRow;
            startCol = endCol;
            
            // Mark the cell as walkable after cutting the tree
            forest.get(startRow).set(startCol, 1);
        }
        
        return totalSteps;
    }
    
    /**
     * BFS to find the shortest path from (startRow, startCol) to (endRow, endCol).
     * 
     * @param forest The forest grid
     * @param startRow Starting row
     * @param startCol Starting column
     * @param endRow Target row
     * @param endCol Target column
     * @return Minimum steps or -1 if no path exists
     */
    private int bfs(List<List<Integer>> forest, int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow && startCol == endCol) {
            return 0;
        }
        
        int m = forest.size();
        int n = forest.get(0).size();
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        int steps = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++;
            
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                
                for (int[] dir : DIRECTIONS) {
                    int newRow = current[0] + dir[0];
                    int newCol = current[1] + dir[1];
                    
                    if (newRow == endRow && newCol == endCol) {
                        return steps;
                    }
                    
                    if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && 
                        !visited[newRow][newCol] && forest.get(newRow).get(newCol) > 0) {
                        visited[newRow][newCol] = true;
                        queue.offer(new int[]{newRow, newCol});
                    }
                }
            }
        }
        
        return -1;
    }
}
