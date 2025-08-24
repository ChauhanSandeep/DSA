package heap;

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
 * LeetCode Link: https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
 *
 * Problem Statement:
 * - You are given two sorted integer arrays nums1 and nums2 and an integer k.
 * - Return the k pairs (u, v) such that u is from nums1 and v is from nums2 and
 *   the sum u + v is the smallest possible among all possible pairs.
 *
 * Example:
 * Input: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
 * Output: [[1,2],[1,4],[1,6]]
 *
 * Follow-up Questions:
 * 1. How would your solution change if arrays are not sorted?
 *    - Brute force O(N*M log K) or use a custom iterator-like traversal.
 * 2. Can you do it without using a heap?
 *    - Not efficiently; heap ensures optimal time due to min-sum tracking.
 * 3. What if you had to return K largest pairs?
 *    - Use a max heap and invert the comparison logic.
 */
public class kPairsWithSmallestSums {

  public static void main(String[] args) {
    int[] nums1 = {1, 7, 11};
    int[] nums2 = {2, 4, 6};
    int k = 5;

    kPairsWithSmallestSums finder = new kPairsWithSmallestSums();
    List<List<Integer>> result = finder.kSmallestPairs(nums1, nums2, k);

    System.out.println("K Smallest Pairs: " + result);
  }

  /**
   * Finds the k pairs with the smallest sums from two sorted arrays.
   *
   * Approach (Min-Heap + Visited Set):
   * - Since arrays are sorted, the smallest pair is (nums1[0], nums2[0]).
   * - Use a min-heap to always pick the next smallest pair sum.
   * - Push neighbors: (index1+1, index2) and (index1, index2+1) into heap.
   * - Use a set to avoid visiting the same pair again.
   *
   * Time Complexity: O(k log k) — At most k heap operations, each O(log k)
   * Space Complexity: O(k) — For heap and visited set
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
