package binarysearch;

/**
 * 🔹 Problem: Maximal String with Limited Swaps
 * 🔗 LeetCode (related): https://leetcode.com/problems/maximum-swap/
 *
 * 📌 Problem Statement:
 * Given a numeric string and a maximum number of swaps `k`, return the lexicographically
 * largest string obtainable by performing at most `k` swaps between any two digits.
 *
 * 🧪 Example:
 * Input: "254", k = 1
 * Output: "524"
 * Explanation: Swap '2' with '5' once.
 *
 * 🧠 Follow-up Questions:
 * - Q: How would you optimize if you can only swap adjacent characters?
 *   A: Similar to bubble sort optimization or use BFS to explore state-space.
 * - Q: What if the input is very large? (e.g., 100,000 digits)
 *   A: Greedy or segment tree for max digit per suffix, avoid recursive state-space exploration.
 * - Q: What’s the difference between this and Leetcode 670?
 *   A: Leetcode 670 only allows one swap. This allows up to `k` swaps and requires full exploration.
 */
public class MaximalString {

  // Tracks the lexicographically largest string found during recursion
  private String largestString;

  /**
   * Returns the lexicographically largest string possible by at most `maxSwaps` swaps.
   *
   * 📌 Steps:
   * - Initialize max string as original input.
   * - Explore all valid swaps using DFS.
   * - At each level, track the best result.
   *
   * ⏱ Time Complexity: O(n^k) in worst case (where n is string length, k is number of swaps)
   * 📦 Space Complexity: O(n) for recursion stack
   *
   * @param input     Numeric string input
   * @param maxSwaps  Maximum number of allowed swaps
   * @return Lexicographically largest string possible after swaps
   */
  public String findMaximalString(String input, int maxSwaps) {
    this.largestString = input;
    exploreSwapCombinations(input.toCharArray(), maxSwaps);
    return largestString;
  }

  /**
   * Recursively explores swap combinations and updates the global max string.
   *
   * @param charArray       Current character array representing the number
   * @param remainingSwaps  Number of swaps left
   */
  private void exploreSwapCombinations(char[] charArray, int remainingSwaps) {
    String current = new String(charArray);
    if (current.compareTo(largestString) > 0) {
      largestString = current;
    }

    if (remainingSwaps == 0) {
      return;
    }

    int length = charArray.length;

    for (int i = 0; i < length - 1; i++) {
      for (int j = i + 1; j < length; j++) {
        // Only consider swaps that increase lexicographic order
        if (charArray[j] > charArray[i]) {
          swap(charArray, i, j);
          exploreSwapCombinations(charArray, remainingSwaps - 1);
          swap(charArray, i, j); // Backtrack
        }
      }
    }
  }

  /**
   * Greedy approach: for each position, swap with the largest digit in the suffix.
   *
   * 📌 Steps:
   * 1. Convert string to char array.
   * 2. For i from 0 to n-2 and swaps > 0:
   *    a. Find index of max char in [i+1..n-1].
   *    b. If maxChar > arr[i], swap and decrement swaps.
   * 3. Return result string.
   *
   * ⏱ Time Complexity: O(n^2) in worst case
   * 📦 Space Complexity: O(n)
   *
   * @param input    Numeric string input
   * @param maxSwaps Number of allowed swaps
   * @return Lexicographically largest string by greedy
   */
  public String findMaximalStringGreedy(String input, int maxSwaps) {
    char[] arr = input.toCharArray();
    int n = arr.length;
    int swaps = maxSwaps;

    for (int i = 0; i < n - 1 && swaps > 0; i++) {
      // find index of max digit in the suffix
      int maxIdx = i;
      for (int j = i + 1; j < n; j++) {
        if (arr[j] >= arr[maxIdx]) {
          maxIdx = j;
        }
      }
      // perform swap if beneficial
      if (maxIdx != i && arr[maxIdx] > arr[i]) {
        swap(arr, i, maxIdx);
        swaps--;
      }
    }

    return new String(arr);
  }

  /**
   * Utility method to swap characters at two indices in the array
   *
   * @param arr Character array
   * @param i   First index
   * @param j   Second index
   */
  private void swap(char[] arr, int i, int j) {
    char temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }
}
