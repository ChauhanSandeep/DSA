package String;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * Problem: Rearrange characters in a string so that no two adjacent characters are the same.
 *
 * LeetCode Link: https://leetcode.com/problems/reorganize-string/
 *
 * Problem Statement:
 * - Given a string, return any rearrangement of the string such that no two adjacent characters are the same.
 * - If no such rearrangement exists, return an empty string.
 *
 * Example:
 * - Input: "aab" → Output: "aba"
 * - Input: "aaab" → Output: ""
 *
 * Follow-up Questions:
 * 1. What if you had to return *all* possible valid rearrangements?
 *    - Use backtracking and pruning to generate all valid permutations.
 * 2. Can this problem be solved using a greedy + bucket approach instead of heap?
 *    - Yes, you can pre-place the most frequent character in even indices and fill others afterward.
 */
public class ReorganizeString {

  public static void main(String[] args) {
    System.out.println("Reorganized: " + reorganizeString("aab"));   // Example Output: "aba"
    System.out.println("Reorganized: " + reorganizeString("aaab"));  // Example Output: ""
  }

  /**
   * Rearranges the string so that no two adjacent characters are the same.
   *
   * Steps:
   * 1. Count character frequencies using HashMap.
   * 2. Use max heap to always pick the most frequent character.
   * 3. Greedily add two highest-frequency characters to result at a time.
   * 4. If one character is left at the end, check if it can be safely placed.
   *
   * Algorithm: Greedy using Max-Heap
   * Time Complexity: O(N log A), where N = input string length, A = alphabet size (constant)
   * Space Complexity: O(A)
   *
   * @param input The input string.
   * @return A rearranged string or empty string if not possible.
   */
  public static String reorganizeString(String input) {
    if (input == null || input.length() <= 1) {
      return input;
    }

    // Step 1: Count frequency of each character
    Map<Character, Integer> charFrequency = new HashMap<>();
    for (char ch : input.toCharArray()) {
      charFrequency.put(ch, charFrequency.getOrDefault(ch, 0) + 1);
    }

    // Step 2: Create a max heap sorted by character frequency
    PriorityQueue<Character> maxHeap =
        new PriorityQueue<>((ch1, ch2) -> charFrequency.get(ch2) - charFrequency.get(ch1));
    maxHeap.addAll(charFrequency.keySet());

    // Step 3: Check for immediate impossibility
    int maxFreq = charFrequency.get(maxHeap.peek());
    if (maxFreq > (input.length() + 1) / 2) {
      return ""; // Not possible to rearrange
    }

    // Step 4: Build the result by placing two highest frequency characters each time
    StringBuilder rearranged = new StringBuilder();
    while (maxHeap.size() >= 2) {
      char first = maxHeap.poll();
      char second = maxHeap.poll();

      rearranged.append(first).append(second);

      charFrequency.put(first, charFrequency.get(first) - 1);
      charFrequency.put(second, charFrequency.get(second) - 1);

      if (charFrequency.get(first) > 0) {
        maxHeap.offer(first);
      }
      if (charFrequency.get(second) > 0) {
        maxHeap.offer(second);
      }
    }

    // Step 5: Add the last character if remaining
    if (!maxHeap.isEmpty()) {
      char lastChar = maxHeap.poll();
      if (charFrequency.get(lastChar) > 1) {
        return ""; // Still more than one occurrence left, cannot place safely
      }
      rearranged.append(lastChar);
    }

    return rearranged.toString();
  }
}
