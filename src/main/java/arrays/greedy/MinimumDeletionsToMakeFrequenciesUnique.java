package arrays.greedy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Problem: Minimum Deletions to Make Character Frequencies Unique
 *
 * Given a lowercase string, delete the fewest characters so every remaining
 * character has a distinct non-zero frequency. Characters may be reduced to zero
 * frequency by deleting all of their occurrences.
 *
 * Leetcode: https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/ (Medium)
 * Rating:   acceptance 62.5% (Medium) - contest rating 1510
 * Pattern:  Greedy | Frequency counting | Descending caps
 *
 * Example:
 *   Input:  s = "aaabbbcc"
 *   Output: 2
 *   Why:    reduce frequencies [3,3,2] to [3,2,1] by deleting two characters.
 *
 * Follow-ups:
 *   1. What if the alphabet is very large?
 *      Count with a hash map, then apply the same greedy frequency reduction.
 *   2. Can this avoid sorting?
 *      Use a set of used frequencies and decrement conflicts until each is unique.
 *   3. What if deletion costs differ by character?
 *      Greedy by frequency is no longer enough; model weighted choices with DP or a heap.
 *
 * Related: Remove Duplicate Letters (316), Minimum Deletions to Make String Balanced (1653).
 */
public class MinimumDeletionsToMakeFrequenciesUnique {

  public static void main(String[] args) {
    String[] inputs = {"aaabbbcc", "aab"};
    int[] expected = {2, 0};

    for (int i = 0; i < inputs.length; i++) {
      int got = minDeletions(inputs[i]);
      System.out.printf("s=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }



  /**
   * Intuition: after frequencies are sorted high to low, each next character only
   * needs to fit below the previous kept frequency. Keeping it as high as allowed
   * minimizes deletions now and leaves the most room for smaller frequencies.
   *
   * Algorithm:
   *   1. Count character frequencies and collect the non-zero counts.
   *   2. Sort frequencies descending.
   *   3. Reduce any frequency that conflicts with maxAllowedFreq and count the deletions.
   *
   * Time:  O(n + k log k) - count n characters, then sort k non-zero frequencies.
   * Space: O(k) - stores the non-zero frequencies, with k at most 26 here.
   *
   * @param input lowercase string
   * @return minimum deletions needed so all non-zero frequencies are unique
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
        int newMaxAllowedFreq = Math.max(0, maxAllowedFreq - 1);
        deletions += currentFrequency - newMaxAllowedFreq;
        maxAllowedFreq = newMaxAllowedFreq;
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
   * Time Complexity: O(N + K * M) where M is maximum frequency and K is number of unique characters.
   * Because:
   * - Counting frequencies: O(N)
   * - For each of K unique characters, in worst case we may decrement M times.
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
