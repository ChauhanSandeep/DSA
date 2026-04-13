package heaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Problem: Find K Pairs with Smallest Products
 *
 * Problem Statement:
 * - Given two sorted lists of integers (may include negatives), find the first k pairs
 *   with the smallest product. A pair is one element from each list.
 *
 * Example 1 (all positive):
 * Input: nums1 = [1, 2, 5, 9], nums2 = [1, 3, 4, 6], k = 5
 * Output: [[1,1], [2,1], [1,3], [1,4], [5,1]]  (products: 1, 2, 3, 4, 5)
 *
 * Example 2 (with negatives):
 * Input: nums1 = [-3, -1, 2, 5], nums2 = [-2, 1, 4], k = 4
 * Output: [[-3,4], [5,-2], [-1,4], [2,-2]]  (products: -12, -10, -4, -4)
 *
 * Two Approaches:
 *
 * Approach 1 — Min-Heap + Visited Set (positive-only arrays):
 *   Because all values are positive, products grow monotonically as indices increase.
 *   The smallest product is always at (0,0). From any cell (i,j), neighbors (i+1,j) and
 *   (i,j+1) are the only candidates for the next minimum. A visited set prevents
 *   duplicate enqueues since the same cell is reachable from two paths.
 *   Time: O(k log k)  Space: O(k)
 *
 * Approach 2 — Row-wise Merge (general: handles any integers):
 *   For a fixed nums1[i], the products nums1[i] * nums2[j] form a sorted list — but the
 *   direction depends on the sign of nums1[i]:
 *     nums1[i] > 0 → products ascending left→right in nums2  (start j = 0,       dir = +1)
 *     nums1[i] < 0 → products ascending right→left in nums2  (start j = len2-1,  dir = -1)
 *     nums1[i] = 0 → all products are 0                       (start j = 0,       dir = +1)
 *   Seed the heap with the first element of each row's sorted sequence, then advance
 *   one step per row when its entry is popped. No visited set needed — each (i, j)
 *   is visited at most once since each row advances in a single direction.
 *   Time: O(M + k log M)  Space: O(M)  (M = nums1.length)
 *
 * Follow-up Questions:
 * 1. Why does Approach 1 break with negatives?
 *    - neg × large_positive is very negative (smallest), but (0,0) may not hold such a pair.
 *      Moving right/down no longer monotonically increases the product.
 * 2. Why is Approach 2 better for large arrays?
 *    - O(M + k log M) vs O(k log k). When k >> M the heap stays small (size M) rather than
 *      growing to size 2k.
 * 3. What if k > M*N?
 *    - The heap naturally empties; return whatever pairs were collected.
 */
public class KPairsWithSmallestProducts {

  public static void main(String[] args) {
    KPairsWithSmallestProducts finder = new KPairsWithSmallestProducts();

    int[] nums1 = {1, 2, 5, 9};
    int[] nums2 = {1, 3, 4, 6};
    System.out.println("All-positive (Approach 1): " + finder.kSmallestProductPairs(nums1, nums2, 5));
    // Expected: [[1,1], [2,1], [1,3], [1,4], [5,1]]

    System.out.println("All-positive (Approach 2): " + finder.kSmallestProductPairsWithNegatives(nums1, nums2, 5));
    // Expected: [[1,1], [2,1], [1,3], [1,4], [5,1]]

    int[] n1 = {-3, -1, 2, 5};
    int[] n2 = {-2, 1, 4};
    System.out.println("With negatives (Approach 2): " + finder.kSmallestProductPairsWithNegatives(n1, n2, 4));
    // Expected: [[-3,4], [5,-2], [-1,4], [2,-2]]  (products: -12, -10, -4, -4)
  }

  /**
   * Approach 1: Min-Heap + Visited Set.
   * Works correctly only when both arrays contain positive integers.
   *
   * - Seed the heap with (nums1[0], nums2[0]) — the globally smallest product.
   * - On each pop of (i, j): push neighbors (i+1, j) and (i, j+1) if unseen.
   * - Visited set prevents duplicate enqueues (cell reachable from two directions).
   *
   * Time Complexity: O(k log k)
   * Space Complexity: O(k)
   *
   * @param nums1 first sorted array of positive integers
   * @param nums2 second sorted array of positive integers
   * @param k     number of pairs to return
   * @return k pairs with smallest products in non-decreasing order
   */
  public List<List<Integer>> kSmallestProductPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>();

    if (nums1.length == 0 || nums2.length == 0 || k == 0) {
      return result;
    }

    // Min-heap: [product, index1, index2]
    PriorityQueue<long[]> minHeap = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
    Set<String> visited = new HashSet<>();

    minHeap.offer(new long[]{(long) nums1[0] * nums2[0], 0, 0});
    visited.add("0,0");

    while (k-- > 0 && !minHeap.isEmpty()) {
      long[] current = minHeap.poll();
      int i = (int) current[1];
      int j = (int) current[2];

      result.add(new ArrayList<>(Arrays.asList(nums1[i], nums2[j])));

      if (i + 1 < nums1.length) {
        String key = (i + 1) + "," + j;
        if (!visited.contains(key)) {
          minHeap.offer(new long[]{(long) nums1[i + 1] * nums2[j], i + 1, j});
          visited.add(key);
        }
      }

      if (j + 1 < nums2.length) {
        String key = i + "," + (j + 1);
        if (!visited.contains(key)) {
          minHeap.offer(new long[]{(long) nums1[i] * nums2[j + 1], i, j + 1});
          visited.add(key);
        }
      }
    }

    return result;
  }

  /**
   * Approach 2: Row-wise Sorted-List Merge (handles any integers including negatives).
   *
   * Key insight — for a fixed nums1[i], the sequence of products with each nums2[j] is sorted,
   * but the traversal direction through nums2 depends on the sign of nums1[i]:
   *
   *   nums1[i] > 0 : multiplying by larger nums2 gives larger product  → traverse left→right
   *                  seed at j = 0 (smallest nums2), direction = +1
   *
   *   nums1[i] < 0 : multiplying by larger nums2 gives more-negative product → traverse right→left
   *                  seed at j = len2-1 (largest nums2), direction = -1
   *
   *   nums1[i] = 0 : product is always 0 → traverse in any direction
   *                  seed at j = 0, direction = +1
   *
   * Each row's products form an independent ascending sequence. We merge M such sequences
   * with a size-M min-heap, advancing each row's pointer by one step (in its direction)
   * after it is popped.
   *
   * No visited set is required: each (i, j) is enqueued exactly once — seeded initially
   * and then advanced one step at a time, so no duplicate paths exist.
   *
   * Time Complexity: O(M + k log M) — M initial inserts + k heap pops each O(log M)
   * Space Complexity: O(M) — heap holds at most M entries at any point
   *
   * @param nums1 first sorted array (may contain negative integers)
   * @param nums2 second sorted array (may contain negative integers)
   * @param k     number of pairs to return
   * @return k pairs with smallest products in non-decreasing order
   */
  public List<List<Integer>> kSmallestProductPairsWithNegatives(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> result = new ArrayList<>();

    if (nums1.length == 0 || nums2.length == 0 || k == 0) {
      return result;
    }

    int len2 = nums2.length;

    // Min-heap: [product, index1, index2, direction (+1 or -1 for advancing j)]
    PriorityQueue<long[]> minHeap = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));

    for (int i = 0; i < nums1.length; i++) {
      if (nums1[i] >= 0) {
        // Smallest product for this row is at j=0 (smallest nums2 element)
        minHeap.offer(new long[]{(long) nums1[i] * nums2[0], i, 0, 1});
      } else {
        // Smallest (most negative) product for this row is at j=len2-1 (largest nums2 element)
        minHeap.offer(new long[]{(long) nums1[i] * nums2[len2 - 1], i, len2 - 1, -1});
      }
    }

    while (k-- > 0 && !minHeap.isEmpty()) {
      long[] current = minHeap.poll();
      int i = (int) current[1];
      int j = (int) current[2];
      int dir = (int) current[3];

      result.add(new ArrayList<>(Arrays.asList(nums1[i], nums2[j])));

      int nextJ = j + dir;
      if (nextJ >= 0 && nextJ < len2) {
        minHeap.offer(new long[]{(long) nums1[i] * nums2[nextJ], i, nextJ, dir});
      }
    }

    return result;
  }
}
