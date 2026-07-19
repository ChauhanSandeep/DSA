package graphs;

import java.util.*;


/**
 * Problem: K Highest Ranked Items Within a Price Range
 *
 * Given a shop grid, a price range, a start cell, and k, return up to k reachable
 * item coordinates ranked by shortest distance, then lower price, then smaller
 * row, then smaller column. Blocked cells have value 0.
 *
 * Leetcode: https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/ (Medium)
 * Rating:   1837 (zerotrac Elo)
 * Pattern:  Graph | BFS | Level-order ranking
 *
 * Example:
 *   Input:  grid = [[1,2,0,1],[1,3,3,1],[0,2,5,1]], pricing = [2,3], start = [2,3], k = 2
 *   Output: [[2,1], [1,2]]
 *   Why:    both returned items are distance two from the start; price 2 at [2,1]
 *           ranks before price 3 at [1,2].
 *
 * Follow-ups:
 *   1. Avoid sorting all found items when k is small?
 *      Keep a bounded heap of the best k candidates while BFS visits cells.
 *   2. What if the grid is huge and sparse?
 *      Store blocked and priced cells in hash maps and generate only reachable neighbors.
 *   3. Support changing prices between queries?
 *      Re-run BFS for reachability but query prices from an updated price index.
 *
 * Related: Cut Off Trees for Golf Event (675), Shortest Path in Binary Matrix (1091).
 */
public class HighestRatedItem {



    public static void main(String[] args) {
        HighestRatedItem solver = new HighestRatedItem();
        int[][][] grids = {
            {{1, 2, 0, 1}, {1, 3, 3, 1}, {0, 2, 5, 1}},
            {{1, 1, 1}}
        };
        int[][] pricing = {{2, 3}, {2, 5}};
        int[][] starts = {{2, 3}, {0, 0}};
        int[] kValues = {2, 3};
        String[] expected = {"[[2, 1], [1, 2]]", "[]"};

        for (int i = 0; i < grids.length; i++) {
            List<List<Integer>> output = solver.highestRankedKItems(grids[i], pricing[i], starts[i], kValues[i]);
            System.out.printf("grid=%s pricing=%s start=%s k=%d  ->  %s  expected=%s%n",
                Arrays.deepToString(grids[i]), Arrays.toString(pricing[i]), Arrays.toString(starts[i]), kValues[i], output, expected[i]);
        }
    }
    /**
     * Intuition: distance is the first ranking key, so BFS from the start naturally
     * visits cells by increasing distance. Collect reachable items within the price
     * range, then sort by the remaining tie-breakers: distance, price, row, and column.
     *
     * Algorithm:
     *   1. BFS from the starting cell through non-wall cells.
     *   2. Record each reachable item whose price is inside the requested range.
     *   3. Sort recorded items by distance, price, row, then column.
     *   4. Return the first k item coordinates.
     *
     * Time:  O(m*n log(m*n)) - BFS scans the grid and sorting can include many cells.
     * Space: O(m*n) - visited, queue, and candidate item storage.
     *
     * @param grid store grid with 0 as wall and positive values as prices
     * @param pricing inclusive low/high price range
     * @param start starting coordinate [row, col]
     * @param k maximum number of item coordinates to return
     * @return ranked item coordinates
     */
  public List<List<Integer>> highestRankedKItems(int[][] grid, int[] pricing, int[] start, int k) {

    // Use max heap to maintain top k items (negate values for max heap behavior)
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
      // Compare in reverse order for max heap (worst items at top)
      if (a[0] != b[0]) return Integer.compare(b[0], a[0]); // Distance (larger first)
      if (a[1] != b[1]) return Integer.compare(b[1], a[1]); // Price (larger first)
      if (a[2] != b[2]) return Integer.compare(b[2], a[2]); // Row (larger first)
      return Integer.compare(b[3], a[3]); // Column (larger first)
    });

    int rows = grid.length, cols = grid[0].length;
    int startRow = start[0], startCol = start[1];
    int minPrice = pricing[0], maxPrice = pricing[1];

    Queue<int[]> queue = new ArrayDeque<>();
    queue.offer(new int[]{startRow, startCol});

    // Check starting position
    if (isValidItem(grid[startRow][startCol], minPrice, maxPrice)) {
      addToTopKHeap(minHeap, new int[]{0, grid[startRow][startCol], startRow, startCol}, k);
    }

    grid[startRow][startCol] = 0;
    int[] directions = {-1, 0, 1, 0, -1};

    // Perform BFS with heap maintenance
    for (int distance = 1; !queue.isEmpty(); distance++) {
      int queueSize = queue.size();

      for (int i = 0; i < queueSize; i++) {
        int[] current = queue.poll();
        int currentRow = current[0];
        int currentCol = current[1];

        for (int dir = 0; dir < 4; dir++) {
          int nextRow = currentRow + directions[dir];
          int nextCol = currentCol + directions[dir + 1];

          if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols && grid[nextRow][nextCol] > 0) {
            int nextPrice = grid[nextRow][nextCol];
            if (isValidItem(nextPrice, minPrice, maxPrice)) {
              addToTopKHeap(minHeap, new int[]{distance, nextPrice, nextRow, nextCol}, k);
            }
            grid[nextRow][nextCol] = 0;
            queue.offer(new int[]{nextRow, nextCol});
          }

        }

      }
    }

    return extractResultFromHeap(minHeap);
  }

  // Validates if item price falls within specified range
  private boolean isValidItem(int price, int minPrice, int maxPrice) {
    return price >= minPrice && price <= maxPrice;
  }

  // Maintains heap of top k items
  private void addToTopKHeap(PriorityQueue<int[]> heap, int[] newItem, int k) {
      heap.offer(newItem);
      if (heap.size() > k) {
      heap.poll();
    }
  }

  // Extracts results from heap in correct order
  private List<List<Integer>> extractResultFromHeap(PriorityQueue<int[]> heap) {
    List<int[]> items = new ArrayList<>(heap);
    items.sort((a, b) -> {
      if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
      if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
      if (a[2] != b[2]) return Integer.compare(a[2], b[2]);
      return Integer.compare(a[3], b[3]);
    });

    List<List<Integer>> result = new ArrayList<>();
    for (int[] item : items) {
      result.add(Arrays.asList(item[2], item[3]));
    }
    return result;
  }
}
