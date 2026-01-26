package arrays.twopointers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem Statement:
 * Given an array A containing N integers, find the maximum sum of a strictly increasing triplet (Ai + Aj + Ak)
 * such that 0 <= i < j < k < N and Ai < Aj < Ak. If no such triplet exists, return 0.
 *
 * Example:
 * Input: A = [2, 5, 3, 1, 4, 9]
 * Output: 16
 * Explanation: Possible triplets include 2 + 5 + 9 = 16 or 3 + 4 + 9 = 16. The maximum sum is 16.
 *
 * InterviewBit Link: https://www.interviewbit.com/problems/maximum-sum-triplet/
 *
 * Follow-up Questions:
 * 1. How would you modify this to find the maximum sum without the strictly increasing condition?
 *    - Sort the array and sum the three largest elements, handling negatives by checking combinations.
 * 2. What if we need to find the kth largest such triplet sum?
 *    - Use a priority queue to track top sums while generating valid triplets, but complexity increases.
 * 3. How to handle very large arrays (n=10^6) efficiently?
 *    - The O(n log n) approach scales well; for larger, parallelize or approximate if exact not needed.
 *    Relevant problem: https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MaximumSumIncreasingTriplet {

  public static void main(String[] args) {
    List<Integer> input = Stream.of(2, 5, 3, 1, 4, 9).collect(Collectors.toList());
    int result = new MaximumSumIncreasingTriplet().findMaxTripletSum(input);
    System.out.println("Maximum Sum Triplet: " + result); // Output: 16
  }

  /**
   * Finds the maximum sum of a strictly increasing triplet using suffix maximum and TreeSet for efficient queries.
   * This is the optimal approach for O(n log n) time.
   *
   * Step-by-step explanation:
   * 1. Compute a rightMax array where rightMax[i] is the maximum value from i to n-1.
   * 2. Initialize a TreeSet to keep track of elements seen so far (left of current j).
   * 3. For each possible j from 0 to n-2:
   *    - Check if there is a maxRight > A[j] from j+1 to n-1 using rightMax[j+1].
   *    - Query the TreeSet for the largest value strictly less than A[j] (maxLeft).
   *    - If both exist, compute sum maxLeft + A[j] + maxRight and update the answer.
   *    - Add A[j] to the TreeSet.
   * 4. Return the maximum sum found, or 0 if none.
   *
   * Algorithm: Suffix Maximum + Ordered Set (TreeSet)
   * Time Complexity: O(n log n) - Due to n insertions and queries on TreeSet.
   * Space Complexity: O(n) - For suffix array and TreeSet.
   *
   * @param list the input array of integers
   * @return the maximum sum of strictly increasing triplet, or 0 if none exists
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