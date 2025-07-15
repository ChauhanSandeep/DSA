package Array;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InterviewBit: https://www.interviewbit.com/problems/maximum-sum-triplet/
 *
 * Problem: Find the maximum sum of a triplet (Ai + Aj + Ak) such that:
 *    0 <= i < j < k < N and Ai < Aj < Ak
 * Return 0 if no such triplet exists.
 *
 * Approach:
 * - Use a TreeSet to maintain the prefix (left) values in sorted order.
 * - Use a precomputed maxRight[] array to track max value to the right of each element.
 *
 * Example:
 * Input:  [2, 5, 3, 1, 4, 9]
 * Output: 16  (2 + 5 + 9)
 *
 * Follow-up Questions:
 * - Q: Can this be done in O(n)?
 *   A: Not easily. Maintaining prefix minimum/maximum and suffix max efficiently needs a TreeSet or Segment Tree.
 * - Q: What if duplicates are allowed?
 *   A: TreeSet handles uniqueness. You may switch to TreeMap with count tracking.
 */
public class MaximumSumIncreasingTriplet {

  public static void main(String[] args) {
    List<Integer> input = Stream.of(2, 5, 3, 1, 4, 9).collect(Collectors.toList());
    int result = new MaximumSumIncreasingTriplet().findMaxTripletSum(input);
    System.out.println("Maximum Sum Triplet: " + result); // Output: 16
  }

  /**
   * Finds the maximum sum of a triplet (Ai + Aj + Ak) such that i < j < k and Ai < Aj < Ak.
   *
   * ✅ Algorithm:
   * - For each element Aj at index j:
   *     - Use TreeSet to find max Ai < Aj to the left.
   *     - Use precomputed maxRight[j+1] to find Ak > Aj to the right.
   *
   * Time Complexity: O(n log n) due to TreeSet operations.
   * Space Complexity: O(n) for maxRight array and TreeSet.
   */
  public int findMaxTripletSum(List<Integer> list) {
    int length = list.size();
    if (length < 3) return 0;

    // Step 1: Build maxRight array, where maxRight[i] = max of list[i...length-1]
    int[] maxRight = new int[length + 1];
    maxRight[length] = 0;

    for (int i = length - 1; i >= 0; i--) {
      maxRight[i] = Math.max(maxRight[i + 1], list.get(i));
    }

    // Step 2: TreeSet to maintain sorted prefix values (Ai candidates)
    TreeSet<Integer> sortedPrefixSet = new TreeSet<>();
    sortedPrefixSet.add(Integer.MIN_VALUE); // To avoid null when using lower()

    int maxSum = 0;

    // Step 3: Traverse list to consider each element as middle Aj
    for (int j = 0; j < length - 1; j++) {
      int current = list.get(j);
      int rightMax = maxRight[j + 1];

      // Valid increasing triplet condition: Ai < Aj < Ak
      if (rightMax > current) {
        Integer leftMax = sortedPrefixSet.lower(current); // max from left side but less than current
        if (leftMax != null) {
          int tripletSum = leftMax + current + rightMax;
          maxSum = Math.max(maxSum, tripletSum);
        }
      }

      // Add current element to TreeSet for future prefix lookups
      sortedPrefixSet.add(current);
    }

    return maxSum;
  }
}