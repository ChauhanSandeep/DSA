package heaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Problem: K Pairs with Smallest Products
 *
 * Given two sorted integer arrays, return up to k pairs whose products are the
 * smallest. The positive-only method uses grid monotonicity; the general method
 * handles negatives by merging one sorted product sequence per nums1 row.
 *
 * Pattern:  Heap | K-way merge | Grid best-first search
 *
 * Example:
 *   Input:  nums1 = [-3,-1,2,5], nums2 = [-2,1,4], k = 2
 *   Output: [[-3,4], [5,-2]]
 *   Why:    the products are -12 and -10, smaller than every other pair product.
 *
 * Follow-ups:
 *   1. Why does the grid-walk method fail with negatives?
 *      Products stop being monotonic when moving right or down can make values smaller.
 *   2. How do you cap work when k exceeds nums1.length * nums2.length?
 *      Let the heap empty naturally and return all generated pairs.
 *   3. How would you return largest products instead?
 *      Reverse the heap ordering and seed each row from its largest product direction.
 *   4. How do you avoid overflow for large values?
 *      Store products as long and compare with long arithmetic.
 */

public class KPairsWithSmallestProducts {

  public static void main(String[] args) {
    KPairsWithSmallestProducts finder = new KPairsWithSmallestProducts();

    int[] positiveNums1 = {1, 2, 5, 9};
    int[] positiveNums2 = {1, 3, 4, 6};
    List<List<Integer>> positiveGot = finder.kSmallestProductPairs(positiveNums1, positiveNums2, 5);
    System.out.printf("nums1=%s nums2=%s k=%d -> %s  expected=%s%n",
        Arrays.toString(positiveNums1), Arrays.toString(positiveNums2), 5,
        positiveGot, "[[1, 1], [2, 1], [1, 3], [1, 4], [5, 1]]");

    int[] mixedNums1 = {-3, -1, 2, 5};
    int[] mixedNums2 = {-2, 1, 4};
    List<List<Integer>> mixedGot = finder.kSmallestProductPairsWithNegatives(mixedNums1, mixedNums2, 2);
    System.out.printf("nums1=%s nums2=%s k=%d -> %s  expected=%s%n",
        Arrays.toString(mixedNums1), Arrays.toString(mixedNums2), 2,
        mixedGot, "[[-3, 4], [5, -2]]");
  }

    /**
   * Intuition: with positive sorted arrays, the product grid grows as you move
   * down or right. That makes (0,0) the first answer, and every next answer must
   * come from a right/down neighbor of a pair already removed from the heap.
   *
   * Algorithm:
   *   1. Return an empty list when either array is empty or k is zero.
   *   2. Seed a min heap with product cell (0,0) and mark it visited.
   *   3. Repeatedly poll the smallest product and append its pair to result.
   *   4. Offer unseen down and right neighbors so each index pair appears once.
   *
   * Time:  O(k log k) - at most two offers and one poll for each reported pair.
   * Space: O(k) - the heap and visited set grow with the frontier of generated pairs.
   *
   * @param nums1 first sorted positive array
   * @param nums2 second sorted positive array
   * @param k number of pairs to return
   * @return pairs with the smallest products in poll order
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
   * Intuition: for one fixed nums1[i], multiplying across nums2 is sorted, but
   * the direction depends on the sign of nums1[i]. Seed each row at its smallest
   * product, then k-way merge those row sequences with a min heap.
   *
   * Algorithm:
   *   1. Return an empty list when either array is empty or k is zero.
   *   2. For each nums1 row, seed the smallest product and its j direction.
   *   3. Poll the globally smallest row head and append its pair.
   *   4. Advance that same row by dir and offer the next product if it is in range.
   *
   * Time:  O(m + k log m) - m initial row heads, then k heap polls over m rows.
   * Space: O(m) - the heap stores at most one active entry per nums1 row.
   *
   * @param nums1 first sorted array, possibly containing negatives
   * @param nums2 second sorted array, possibly containing negatives
   * @param k number of pairs to return
   * @return pairs with the smallest products in poll order
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
