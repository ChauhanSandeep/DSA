package arrays.twopointers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem: Maximum Sum Increasing Triplet
 *
 * Given an array, find the maximum sum Ai + Aj + Ak with i < j < k and
 * Ai < Aj < Ak. Return 0 when no strictly increasing triplet exists.
 *
 * Source: https://www.interviewbit.com/problems/maximum-sum-triplet/
 * Pattern:  Array | Ordered set | Prefix predecessor plus suffix maximum
 *
 * Example:
 *   Input:  nums = [2,5,3,1,4,9]
 *   Output: 16
 *   Why:    both 2 + 5 + 9 and 3 + 4 + 9 are valid, and no valid triplet sums higher.
 *
 * Follow-ups:
 *   1. Return the triplet values too?
 *      Store the chosen left, middle, and right values when maxSum improves.
 *   2. Find the kth largest increasing triplet sum?
 *      Generate candidates with a priority queue, but the state space grows quickly.
 *   3. Handle online updates to the array?
 *      Use segment trees or Fenwick trees over compressed values for prefix/suffix queries.
 *
 * Related: Increasing Triplet Subsequence (334), Maximum Sum of 3 Non-Overlapping Subarrays (689).
 */
public class MaximumSumIncreasingTriplet {

public static void main(String[] args) {
  MaximumSumIncreasingTriplet solver = new MaximumSumIncreasingTriplet();
  List<List<Integer>> inputs = Arrays.asList(
      Arrays.asList(2, 5, 3, 1, 4, 9),
      Arrays.asList(5, 4, 3)
  );
  int[] expected = { 16, 0 };

  for (int i = 0; i < inputs.size(); i++) {
    int got = solver.findMaxTripletSum(inputs.get(i));
    System.out.printf("nums=%s -> %d  expected=%d%n", inputs.get(i), got, expected[i]);
  }
}

  /**
 * Intuition: choose each index as the middle value Aj. The best right side is
 * simply the maximum value after it, while the best left side must be the
 * largest seen value that is still smaller than Aj. A TreeSet gives that left
 * predecessor while maxRight gives the right candidate.
 *
 * Algorithm:
 *   1. Build maxRight so maxRight[i] is the maximum value from i to the end.
 *   2. Keep sortedPrefixSet for values to the left of the current index.
 *   3. For each middle value, require maxRight[i + 1] > currentElement.
 *   4. Query lower(currentElement), update maxSum, then add currentElement to the set.
 *
 * Time:  O(n log n) - each index performs one TreeSet query and insertion.
 * Space: O(n) - maxRight and sortedPrefixSet can each grow linearly.
 *
 * @param list input values
 * @return maximum increasing triplet sum, or 0 if no such triplet exists
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
    for (int i = 0; i < length - 1; i++) {
      int currentElement = list.get(i);
      int rightMax = maxRight[i + 1];

      // Valid increasing triplet condition: Ai < Aj < Ak
      if (rightMax > currentElement) {
        Integer leftMax = sortedPrefixSet.lower(currentElement); // max from left side but less than current
        if (leftMax != null) {
          int tripletSum = leftMax + currentElement + rightMax;
          maxSum = Math.max(maxSum, tripletSum);
        }
      }

      // Add current element to TreeSet for future prefix lookups
      sortedPrefixSet.add(currentElement);
    }

    return maxSum;
  }
}