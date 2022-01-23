package Graph;


import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/
 */
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
        int rows = grid.length;
        int cols = grid[0].length;
        int lower = pricing[0];
        int higher = pricing[1];
        List<List<Integer>> result = new ArrayList<>();

        PriorityQueue<int[]> pQueue = new PriorityQueue<>((a, b) -> {
            // [row, col, dist]
            int res = 0;
            res = a[2] - b[2];
            if (res == 0) res = grid[a[0]][a[1]] - grid[b[0]][b[1]];
            if (res == 0) res = a[0] - b[0];
            if (res == 0) res = a[1] - b[1];
            return res;
        });

        pQueue.offer(new int[]{start[0], start[1], 0});

        while (!pQueue.isEmpty()) {
            int[] curr = pQueue.poll();
            int currX = curr[0];
            int currY = curr[1];
            int currDist = curr[2];
            if (grid[currX][currY] == 0) continue;

            if (grid[currX][currY] >= lower && grid[currX][currY] <= higher) {
                List<Integer> temp = Stream.of(currX, currY).collect(Collectors.toList());
                result.add(temp);
                k--;
                if (k == 0) return result;
            }

            if (currX > 0 && grid[currX - 1][currY] != 0) {
                pQueue.offer(new int[]{currX - 1, currY, currDist + 1});
            }
            if (currX < rows - 1 && grid[currX + 1][currY] != 0) {
                pQueue.offer(new int[]{currX + 1, currY, currDist + 1});
            }

            if (currY > 0 && grid[currX][currY - 1] != 0) {
                pQueue.offer(new int[]{currX, currY - 1, currDist + 1});
            }
            if (currY < cols - 1 && grid[currX][currY + 1] != 0) {
                pQueue.offer(new int[]{currX, currY + 1, currDist + 1});
            }

            grid[currX][currY] = 0;

        }

        return result;

    }
}
