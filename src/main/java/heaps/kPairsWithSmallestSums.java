package heaps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


/**
 * Problem: Find K Pairs with Smallest Sums
 *
 * Given two sorted arrays nums1 and nums2, return k pairs [u, v] with the
 * smallest sums where u comes from nums1 and v comes from nums2. The sorted grid
 * of pair sums can be explored from the smallest cell with a min heap.
 *
 * Leetcode: https://leetcode.com/problems/find-k-pairs-with-smallest-sums/ (Medium)
 * Rating:   acceptance 42.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Best-first search | Visited grid cells
 *
 * Example:
 *   Input:  nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 *   Output: [[1,2],[1,4],[1,6]]
 *   Why:    sums 3, 5, and 7 are the three smallest pair sums.
 *
 * Follow-ups:
 *   1. Can you reduce the visited set?
 *      Seed one entry per nums1 row and advance only in nums2, like k-way merge.
 *   2. What if arrays are not sorted?
 *      Sort them first if original order is irrelevant, or brute force with a size-k heap.
 *   3. How would you return k largest sums?
 *      Start from the largest index pair and use a max heap with left/up neighbors.
 *   4. How do you handle duplicate values but unique index pairs?
 *      Track visited indices, not pair values, exactly as this implementation does.
 *
 * Related: Kth Smallest Element in a Sorted Matrix (378), K Closest Points to Origin (973).
 */

public class kPairsWithSmallestSums {

  public static void main(String[] args) {
    kPairsWithSmallestSums finder = new kPairsWithSmallestSums();
    int[][] nums1Cases = { {1, 7, 11}, {} };
    int[][] nums2Cases = { {2, 4, 6}, {1, 2} };
    int[] kValues = {3, 3};
    String[] expected = {"[[1, 2], [1, 4], [1, 6]]", "[]"};

    for (int i = 0; i < nums1Cases.length; i++) {
      List<List<Integer>> got = finder.kSmallestPairs(nums1Cases[i], nums2Cases[i], kValues[i]);
      System.out.printf("nums1=%s nums2=%s k=%d -> %s  expected=%s%n",
          Arrays.toString(nums1Cases[i]), Arrays.toString(nums2Cases[i]), kValues[i], got, expected[i]);
    }
  }

    /**
   * Intuition: because both arrays are sorted, pair sums form a grid where moving
   * right or down never decreases the sum. Start at the smallest cell, then use a
   * min heap to always expand the next smallest frontier cell.
   *
   * Algorithm:
   *   1. Return an empty result if either array is empty or k is zero.
   *   2. Seed a min heap with index pair (0,0) and mark it visited.
   *   3. Poll the smallest sum, append its actual pair, and decrease k.
   *   4. Offer unseen down and right neighbors for future consideration.
   *
   * Time:  O(k log k) - each reported pair can add up to two heap entries.
   * Space: O(k) - the heap and visited set store generated frontier cells.
   */

  public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    int len1 = nums1.length;
    int len2 = nums2.length;

    List<List<Integer>> result = new ArrayList<>();

    if (len1 == 0 || len2 == 0 || k == 0) {
      return result;
    }

    // Min-heap stores [sum, index1, index2]
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    // Tracks visited index pairs to avoid duplicates
    Set<String> visited = new HashSet<>();

    // Start with the smallest possible pair (nums1[0], nums2[0])
    minHeap.offer(new int[]{nums1[0] + nums2[0], 0, 0});
    visited.add(0 + "," + 0);

    while (k-- > 0 && !minHeap.isEmpty()) {
      int[] current = minHeap.poll();
      int index1 = current[1];
      int index2 = current[2];

      // Add the actual pair to result
      result.add(new ArrayList<>(Arrays.asList(nums1[index1], nums2[index2])));

      // Explore next pair in nums1
      if (index1 + 1 < len1) {
        String key = (index1 + 1) + "," + index2;
        if (!visited.contains(key)) {
          minHeap.offer(new int[]{nums1[index1 + 1] + nums2[index2], index1 + 1, index2});
          visited.add(key);
        }
      }

      // Explore next pair in nums2
      if (index2 + 1 < len2) {
        String key = index1 + "," + (index2 + 1);
        if (!visited.contains(key)) {
          minHeap.offer(new int[]{nums1[index1] + nums2[index2 + 1], index1, index2 + 1});
          visited.add(key);
        }
      }
    }

    return result;
  }
}
