package Graph;

import java.util.*;

public class HighestRatedItem {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 2, 0, 1},
                {1, 3, 3, 1},
                {0, 2, 5, 1}
        };
        int[] pricing = {2, 3};
        int[] start = {2, 3};
        int k = 2;

        System.out.println(new HighestRatedItem().highestRankedKItems(grid, pricing, start, k));
    }

    public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {
        int rows = grid.length, cols = grid[0].length;
        int lower = pricing[0], higher = pricing[1];

        List<List<Integer>> result = new ArrayList<>();
        boolean[][] visited = new boolean[rows][cols];

        // Min Heap: Sorting based on distance, price, row, col
        PriorityQueue<int[]> pQueue = new PriorityQueue<>((a, b) -> {
            if (a[2] != b[2]) return Integer.compare(a[2], b[2]); // Sort by distance
            if (grid[a[0]][a[1]] != grid[b[0]][b[1]]) return Integer.compare(grid[a[0]][a[1]], grid[b[0]][b[1]]); // Sort by price
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]); // Sort by row
            return Integer.compare(a[1], b[1]); // Sort by col
        });

        // Directions for BFS traversal
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        pQueue.offer(new int[]{start[0], start[1], 0});
        visited[start[0]][start[1]] = true;

        while (!pQueue.isEmpty() && k > 0) {
            int[] curr = pQueue.poll();
            int currX = curr[0], currY = curr[1], currDist = curr[2];

            if (grid[currX][currY] >= lower && grid[currX][currY] <= higher) {
                result.add(Arrays.asList(currX, currY));
                k--;
            }

            for (int[] dir : directions) {
                int newX = currX + dir[0], newY = currY + dir[1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols &&
                        grid[newX][newY] != 0 && !visited[newX][newY]) {
                    visited[newX][newY] = true;
                    pQueue.offer(new int[]{newX, newY, currDist + 1});
                }
            }
        }

        return result;
    }
}
