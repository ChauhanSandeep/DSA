package arrays.twopointers;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Problem: Count Triplets Whose Two Values Sum to the Third
 *
 * Given an integer array, count pairs of values whose sum is also present as a
 * third value in the array. The optimized version sorts the array and fixes the
 * target value from the right.
 *
 * Source: https://www.geeksforgeeks.org/find-the-number-of-triplets-in-array-whose-sum-is-equal-to-a-given-number/
 * Pattern:  Array | Sorting | Fixed target plus two pointers
 *
 * Example:
 *   Input:  arr = [1,2,3]
 *   Output: 1
 *   Why:    1 + 2 equals 3, so exactly one triplet is counted.
 *
 * Follow-ups:
 *   1. Return the actual triplets instead of the count?
 *      Store each matching value triple, using sorting or a set to avoid duplicates.
 *   2. How should duplicate values be counted?
 *      Decide whether the count is by index triples or by unique value triples first.
 *   3. Can this avoid extra space?
 *      Yes, sort in place and use the two-pointer scan for each fixed target.
 *
 * Related: 3Sum (15), 3Sum Closest (16), Valid Triangle Number (611).
 */
public class CountTriplet {

public static void main(String[] args) {
  int[][] inputs = { {5, 32, 1, 7, 10, 50, 19, 21, 2}, {1, 2, 3} };
  int[] expected = { 2, 1 };

  for (int i = 0; i < inputs.length; i++) {
    int hashSetGot = countTripletsHashSet(inputs[i].clone());
    int twoPointerGot = countTripletsTwoPointer(inputs[i].clone());
    System.out.printf("arr=%s -> hash=%d twoPointer=%d  expected=%d%n",
        Arrays.toString(inputs[i]), hashSetGot, twoPointerGot, expected[i]);
  }
}

  /**
   * Counts the number of triplets using HashSet lookup method.
   *
   * Steps:
   * - Store all elements in a HashSet for O(1) lookups
   * - Iterate all pairs (i, j) and check if (arr[i] +
   * arr[j]) exists in the set
   *
   * Time Complexity: O(n^2)
   * Space Complexity: O(n)
   */
  static int countTripletsHashSet(int[] arr) {
    Set<Integer> elements = new HashSet<>();
    for (int num : arr) {
      elements.add(num);
    }

    int count = 0;
    int length = arr.length;

    // Iterate all pairs and check if sum exists in set
    for (int i = 0; i < length - 1; i++) {
      for (int j = i + 1; j < length; j++) {
        int sum = arr[i] + arr[j];
        if (elements.contains(sum)) {
          count++;
        }
      }
    }

    return count;
  }

  /**
 * Intuition: after sorting, a fixed target arr[targetIndex] can be searched the
 * same way as Two Sum II. If the two-pointer sum is too small, only moving the
 * left pointer right can increase it; if too large, only moving the right
 * pointer left can decrease it.
 *
 * Algorithm:
 *   1. Sort arr.
 *   2. Fix targetIndex from the end down to index 2.
 *   3. Search arr[0..targetIndex-1] with leftIndex and rightIndex.
 *   4. Count matches and move both pointers; otherwise move the pointer that fixes the sum.
 *
 * Time:  O(n^2) - each fixed target runs one inward two-pointer scan.
 * Space: O(1) - the scan uses only counters and pointers besides sorting storage.
 *
 * @param arr input array, sorted in place by this method
 * @return number of matching triplets counted by the two-pointer scan
 */
  static int countTripletsTwoPointer(int[] arr) {
    Arrays.sort(arr);
    int length = arr.length;
    int count = 0;

    // Fix the third element from end
    for (int targetIndex = length - 1; targetIndex >= 2; targetIndex--) {
      int leftIndex = 0, rightIndex = targetIndex - 1;

      while (leftIndex < rightIndex) {
        int sum = arr[leftIndex] + arr[rightIndex];
        if (sum == arr[targetIndex]) {
          count++;
          leftIndex++;
          rightIndex--;
        } else if (sum < arr[targetIndex]) {
          leftIndex++;
        } else {
          rightIndex--;
        }
      }
    }
    return count;
  }
}