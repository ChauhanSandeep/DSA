package graphs;

import java.util.*;


/**
 * Problem: Find the top-k highest-ranked items in a grid, based on specific sorting criteria.
 *
 * Statement:
 * - You are given a grid of items represented by integers (0 = inaccessible cell, >0 = item price).
 * - From a given start position, you need to find the top `k` items within a given price range.
 * - Ranking Criteria (in order of priority):
 *   1. Shortest Manhattan distance from the start position.
 *   2. Lower price.
 *   3. Smaller row index.
 *   4. Smaller column index.
 *
 * Example:
 * Input:
 * grid = [[1,2,0,1],
 *         [1,3,3,1],
 *         [0,2,5,1]]
 * pricing = [2,3], start = [2,3], k = 2
 * Output: [[2,1],[1,1]]
 * Explaination:
 *
 *
 * LeetCode Link: https://leetcode.com/problems/k-highest-ranked-items-within-a-price-range/
 *
 * Follow-up Questions:
 * 1. Can this be solved without a priority queue?
 *    - Yes, we can use BFS level-by-level traversal and sort valid items within each level before adding.
 * 2. What if the grid is extremely large (e.g., millions of cells)?
 *    - Use pruning (stop BFS when no further valid items can be found within the required `k`).
 * 3. How to optimize memory if visited array is too large?
 *    - Use a BitSet or encode visited positions into a HashSet with "row*cols+col".
 * LeetCode Contest Rating: 1837
 **/
public class HighestRatedItem {

  public static void main(String[] args) {
    int[][] grid = {{1, 2, 0, 1}, {1, 3, 3, 1}, {0, 2, 5, 1}};
    int[] pricing = {2, 3}; // Price range: [min, max]
    int[] start = {2, 3};   // Starting position
    int k = 2;              // Number of items to return

    HighestRatedItem solution = new HighestRatedItem();
    List<List<Integer>> result = solution.highestRankedKItems(grid, pricing, start, k);

    System.out.println("Top-K highest-ranked items: " + result);
  }

  /**
   * Use heap-based approach. Maintains only top k items during BFS traversal instead of storing all items.
   *
   * Algorithm Steps:
   * 1. Initialize a max heap to store the top k items (worst items at the top)
   * 2. Start BFS from the given starting position
   * 3. For each cell in BFS traversal:
   *    - Check if the cell contains a valid item (within price range)
   *    - If valid, try to add it to our top-k heap
   *    - If heap has space (< k items), add directly
   *    - If heap is full, compare with worst item and replace if better
   * 4. Continue BFS level by level (maintaining Manhattan distance)
   * 5. Extract final results from heap and sort them properly
   *
   * Ranking Priority (best to worst):
   * - Shorter Manhattan distance from start
   * - Lower price
   * - Smaller row index
   * - Smaller column index
   *
   * Time Complexity: O(mn * log k) - better when k << total valid items
   * Space Complexity: O(k) - constant space for heap regardless of total items
   *
   * @param grid 2D array representing shop layout
   * @param pricing valid price range [low, high]
   * @param start starting position [row, col]
   * @param k number of items to return
   * @return list of top k item coordinates
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
