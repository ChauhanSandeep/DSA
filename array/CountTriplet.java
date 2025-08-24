package array;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Leetcode-like Problem (commonly asked):
 * Find all triplets in an array such that the sum of two elements equals the third element.
 *
 * ### Problem Statement:
 * Given an integer array, count the number of unique triplets (i, j, k) such that:
 *   arr[i] + arr[j] == arr[k], and all indices are distinct.
 *
 * ### Example:
 * Input: [5, 32, 1, 7, 10, 50, 19, 21, 2]
 * Output: 4
 * Explanation: (1, 2, 3), (1, 4, 5), etc.
 *
 * ### Link:
 * https://www.geeksforgeeks.org/find-the-number-of-triplets-in-array-whose-sum-is-equal-to-a-given-number/
 *
 * ### Follow-up Questions:
 * - Can you avoid using extra space? Yes, using sorting + two pointers.
 * - What if we want to return all triplets, not just the count? Use a `Set<List<Integer>>` to store triplets.
 */
public class CountTriplet {

  public static void main(String[] args) {
    int[] arr = {5, 32, 1, 7, 10, 50, 19, 21, 2};
    System.out.println("Total triplets (HashSet approach): " + countTripletsHashSet(arr));
    System.out.println("Total triplets (Two pointer approach): " + countTripletsTwoPointer(arr));
  }

  /**
   * Counts the number of triplets using HashSet lookup method.
   *
   * Steps:
   * - Store all elements in a set for O(1) lookup.
   * - Iterate all pairs and check if their sum exists in the set.
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
   * Optimized approach using sorting and two pointers.
   * Finds triplets such that arr[i] + arr[j] == arr[k]
   *
   * Steps:
   * - Sort the array
   * - Fix the third element (from end to start)
   * - Use two pointers to find two elements whose sum == current element
   *
   * Time Complexity: O(n^2)
   * Space Complexity: O(1)
   */
  static int countTripletsTwoPointer(int[] arr) {
    Arrays.sort(arr);
    int length = arr.length;
    int count = 0;

    // Fix the third element from end
    for (int k = length - 1; k >= 2; k--) {
      int i = 0, j = k - 1;

      while (i < j) {
        int sum = arr[i] + arr[j];
        if (sum == arr[k]) {
          count++;
          i++;
          j--;
        } else if (sum < arr[k]) {
          i++;
        } else {
          j--;
        }
      }
    }

    return count;
  }
}