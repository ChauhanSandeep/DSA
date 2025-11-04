package arrays.greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * LeetCode: https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/
 *
 * Problem:
 * Given a string, return the minimum number of characters to delete so that no two characters
 * have the same frequency.
 *
 * Example:
 * Input: "aabbcc"
 * Output: 2
 * Explanation: Delete one 'a' and one 'b' to make frequencies [2,1,1]
 *
 * Follow-up Questions:
 * - Q: Can we do better than sorting?
 *   A: You can use a priority queue or bucket sort, but time complexity will still be O(N).
 */
public class MinimumDeletionsToMakeFrequenciesUnique {

  public static void main(String[] args) {
    String input = "aabbcc";
    int result = minDeletions(input);
    System.out.println("Minimum deletions: " + result);  // Output: 2
  }

  /**
   * Finds minimum deletions using greedy approach with frequency sorting.
   *
   * Algorithm:
   * 1. Count frequency of each character in string
   * 2. Sort frequencies in descending order (process highest first)
   * 3. Track previously assigned frequency to ensure uniqueness
   * 4. For each frequency:
   *    - If >= previous, reduce to (previous - 1)
   *    - Count deletions = original - reduced
   *    - Update previous for next iteration
   * 5. Handle edge case: frequency can't go below 0
   *
   * Key insight: Process from highest to lowest frequency. If current frequency
   * equals or exceeds previous, reduce it to (previous - 1) to maintain uniqueness.
   * This greedy choice minimizes deletions because we preserve higher frequencies.
   *
   * Time Complexity: O(N + K log K) where N is string length and K is unique characters.
   * Counting is O(N), sorting frequencies is O(K log K) where K ≤ 26.
   *
   * Space Complexity: O(K) for frequency array, where K ≤ 26 for lowercase letters.
   *
   * @param input input string of lowercase letters
   * @return minimum number of deletions to make all frequencies unique
   */
  public static int minDeletions(String input) {
    // Here input = "aaabbcc"
    // Count frequency of each character
    int[] frequency = new int[26];
    for (char ch : input.toCharArray()) {
      frequency[ch - 'a']++;
    }
    // Here frequency = [3, 2, 2, 0, 0, ..., 0] for "aaabbcc"

    // Collect non-zero frequencies and sort descending
    List<Integer> frequencies = new ArrayList<>();
    for (int freq : frequency) {
      if (freq > 0) {
        frequencies.add(freq);
      }
    }
    Collections.sort(frequencies, Collections.reverseOrder());
    // frequencies = [3, 2, 2] for "aaabbcc"

    int deletions = 0;
    int maxAllowedFreq = Integer.MAX_VALUE;

    for (int currentFrequency : frequencies) {
      // If current frequency conflicts with previous, reduce it
      if (currentFrequency >= maxAllowedFreq) {
        int newFrequency = Math.max(0, maxAllowedFreq - 1);
        deletions += currentFrequency - newFrequency;
        maxAllowedFreq = newFrequency;
      } else {
        maxAllowedFreq = currentFrequency;
      }
    }

    return deletions;
  }

  /**
   * Alternative approach using HashSet to track used frequencies.
   * More intuitive but slightly less efficient.
   *
   * Algorithm:
   * 1. Count frequencies
   * 2. For each frequency, if already used:
   *    - Decrement until finding unused frequency or reaching 0
   *    - Count each decrement as a deletion
   * 3. Mark frequency as used
   *
   * Time Complexity: O(N + K * M) where M is maximum frequency.
   * Worst case: O(N + K^2) when all frequencies equal.
   *
   * Space Complexity: O(K) for frequency map and used set.
   *
   * @param s input string of lowercase letters
   * @return minimum number of deletions to make all frequencies unique
   */
  public int minDeletionsWithSet(String s) {
    // Count frequency of each character
    int[] frequency = new int[26];
    for (char ch : s.toCharArray()) {
      frequency[ch - 'a']++;
    }

    Set<Integer> usedFrequencies = new HashSet<>();
    int deletions = 0;

    for (int freq : frequency) {
      if (freq == 0) continue;

      // Keep decrementing until we find unused frequency
      while (freq > 0 && usedFrequencies.contains(freq)) {
        freq--;
        deletions++;
      }

      // Mark this frequency as used (if > 0)
      if (freq > 0) {
        usedFrequencies.add(freq);
      }
    }

    return deletions;
  }
}