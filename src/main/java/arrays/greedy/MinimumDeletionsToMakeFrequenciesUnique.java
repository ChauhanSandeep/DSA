package arrays.greedy;

import java.util.Arrays;


/**
 * 🔗 LeetCode: https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/
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
 * ✅ Greedy Strategy:
 * - Count character frequencies
 * - Sort frequencies in descending order
 * - From highest to lowest, ensure each frequency is less than the previous
 * - If not, reduce frequency (simulate deletions)
 *
 * Time Complexity: O(N log N) due to sorting
 * Space Complexity: O(1) since array size is fixed at 26
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
   * Returns the minimum number of deletions required to make all character frequencies unique.
   *
   * Steps:
   * 1. Count character frequencies (26 lowercase letters only).
   * 2. Sort the frequency array in descending order.
   * 3. For each frequency:
   *    - If it's greater than the maximum allowed frequency, delete extra characters.
   *    - Update the maximum allowed frequency for the next character.
   *
   * @param str Input string consisting of lowercase letters
   * @return Minimum number of deletions needed
   */
  public static int minDeletions(String str) {
    // example str = "aabbcc"
    int[] freq = new int[26];

    // Step 1: Count frequency of each character
    for (char ch : str.toCharArray()) {
      freq[ch - 'a']++;
    }
    // freq here : // [2, 2, 2, 0, 0, ..., 0] for "aabbcc" (only 'a', 'b', 'c' have non-zero frequencies)

    // Step 2: Sort frequencies in ascending order (we'll iterate from end)
    Arrays.sort(freq);
    // freq after sorting: [
    // 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2] for "aabbcc"

    int deletions = 0;
    int maxAllowedFreq = str.length(); // Highest allowed frequency initially

    // Step 3: Iterate from highest to lowest frequency
    for (int i = 25; i >= 0 && freq[i] > 0; i--) {
      if (freq[i] > maxAllowedFreq) {
        // Need to reduce frequency to avoid collision
        deletions += freq[i] - maxAllowedFreq;
        freq[i] = maxAllowedFreq;
      }

      // Update maxAllowedFreq for the next lower frequency
      maxAllowedFreq = Math.max(0, freq[i] - 1);
    }

    return deletions;
  }
}