package graphs;

import java.util.*;

/**
 * Problem: Cut Off Trees for Golf Event
 *
 * Given a forest grid, start at (0,0) and cut every tree in increasing height
 * order. Cells with 0 are blocked, 1 is walkable ground, and values greater than
 * 1 are trees. Return the fewest walking steps needed, or -1 if a required tree
 * cannot be reached.
 *
 * Leetcode: https://leetcode.com/problems/cut-off-trees-for-golf-event/ (Hard)
 * Rating:   acceptance 36.6% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | BFS shortest path | Sorted targets
 *
 * Example:
 *   Input:  forest = [[1,2,3],[0,0,4],[7,6,5]]
 *   Output: 6
 *   Why:    the trees are reachable in height order by walking right, right,
 *           down, down, left, left for six total steps.
 *
 * Follow-ups:
 *   1. Speed up pathfinding on very large forests?
 *      Use A* with Manhattan distance as a heuristic between consecutive trees.
 *   2. What if multiple trees have the same height?
 *      Treat equal-height trees as a set of possible next targets and choose the cheapest order.
 *   3. What if several workers can cut trees?
 *      This becomes a scheduling and multi-agent shortest-path problem.
 *
 * Related: Shortest Path in Binary Matrix (1091), The Maze II (505).
 */
public class CutOffTreesForGolfEvent {

    public static void main(String[] args) {
        CutOffTreesForGolfEvent solver = new CutOffTreesForGolfEvent();
        List<List<List<Integer>>> inputs = Arrays.asList(
            Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(0, 0, 4), Arrays.asList(7, 6, 5)),
            Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(0, 0, 0), Arrays.asList(7, 6, 5))
        );
        int[] expected = {6, -1};

        for (int i = 0; i < inputs.size(); i++) {
            int output = solver.cutOffTree(inputs.get(i));
            System.out.printf("forest=%s  ->  %d  expected=%d%n", inputs.get(i), output, expected[i]);
        }
    }
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    /**
     * Intuition: trees must be cut in increasing height order, so the only choice is
     * the shortest route from the current position to the next tree. BFS gives that
     * shortest grid distance through walkable cells. If any required tree cannot be
     * reached, the whole task is impossible.
     *
     * Algorithm:
     *   1. Collect every tree cell and sort trees by height.
     *   2. Starting at (0,0), BFS to the next tree in sorted order.
     *   3. Return -1 immediately if a tree is unreachable.
     *   4. Add each distance and move the current position to the cut tree.
     *
     * Time:  O(T*m*n + T log T) - each of T trees may require one BFS plus sorting.
     * Space: O(m*n + T) - BFS visited/queue storage and the tree list.
     *
     * @param forest grid where 0 is blocked, 1 is grass, and values greater than 1 are tree heights
     * @return minimum steps to cut all trees, or -1 if impossible
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
