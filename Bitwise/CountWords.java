package Bitwise;

import java.util.*;


/**
 * LeetCode Problem: https://leetcode.com/problems/count-words-obtained-after-adding-a-letter/
 *
 * Problem Statement:
 * You are given two string arrays `startWords` and `targetWords`. A target word is valid if it can be obtained
 * by adding exactly one letter to any of the `startWords` and then rearranging the resulting string.
 * Return the number of valid target words.
 *
 * Example:
 * Input: startWords = ["ant", "act", "tack"], targetWords = ["tack", "act", "acti"]
 * Output: 2
 * Explanation:
 * - "tack" can be formed from "act" by adding 'k'.
 * - "acti" can be formed from "ant" by adding 'i'.
 *
 * Follow-up Questions:
 * - Can we avoid sorting for each word comparison?
 *   👉 Yes. Use a bitmask (int) to represent the characters in each word for constant-time comparison.
 * - Is it necessary to group words by length?
 *   👉 Not with bitmasking; length grouping is only helpful when comparing sorted strings.
 */
public class CountWords {

  public static void main(String[] args) {
    String[] startWords = {"ant", "act", "tack"};
    String[] targetWords = {"tack", "act", "acti"};
    int result = new CountWords().wordCount(startWords, targetWords);
    System.out.println(result); // Output: 2
  }

  /**
   * Main method to count how many targetWords can be formed by adding one character to any startWord.
   *
   * Algorithm:
   * - Preprocess startWords by sorting them and storing in a map by their lengths.
   * - Sort each targetWord and remove one character at a time to generate potential startWord.
   * - Check if this potential word exists in the map of startWords.
   *
   * Time Complexity: O(n * m * log m), where n = total words, m = average word length.
   * Space Complexity: O(n * m)
   *
   * @param startWords  List of original words.
   * @param targetWords List of target words formed by adding one letter and rearranging.
   * @return Count of valid target words.
   */
  public int wordCount(String[] startWords, String[] targetWords) {
    Map<Integer, Set<String>> lengthToSortedWordMap = new HashMap<>();

    // Preprocess startWords: sort and group by length
    for (String word : startWords) {
      String sorted = sortChars(word);
      lengthToSortedWordMap.computeIfAbsent(sorted.length(), k -> new HashSet<>()).add(sorted);
    }

    int validCount = 0;

    for (String target : targetWords) {
      String sortedTarget = sortChars(target);
      int targetLength = sortedTarget.length();
      Set<String> possibleStartWords = lengthToSortedWordMap.get(targetLength - 1);

      if (possibleStartWords != null) {
        // Try removing each character to form a potential start word
        for (int i = 0; i < targetLength; i++) {
          String possibleStartWord = sortedTarget.substring(0, i) + sortedTarget.substring(i + 1);
          if (possibleStartWords.contains(possibleStartWord)) {
            validCount++;
            break;
          }
        }
      }
    }

    return validCount;
  }

  /**
   * Utility method to return sorted version of the input string.
   *
   * @param word Input string.
   * @return Sorted string.
   */
  private String sortChars(String word) {
    char[] chars = word.toCharArray();
    Arrays.sort(chars);
    return new String(chars);
  }

  /**
   * Optimized bitmasking approach.
   * NOTE: Use this only if there are unique characters in the words. If unique characters are not guaranteed,
   * this method will not work correctly and instead you should use the `wordCount` method above.
   *
   * Intuition:
   * - Each word can be represented as a 26-bit integer where each bit corresponds to a letter from 'a' to 'z'.
   * - For example, "abc" would be represented as 0b00000000000000000000000000000111 (bits for 'a', 'b', 'c' set).
   * * Steps:
   * 1. Convert each startWord to a bitmask.
   * 2. For each targetWord, generate possible startWord masks by removing one letter.
   * 3. Check if any of these masks exist in the set of startWord masks.
   *
   * Time Complexity: O(n + m * 26) => O(n), where n = total words, m = average word length.
   * Space Complexity: O(n)
   */
  public int wordCountUsingBitmask(String[] startWords, String[] targetWords) {
    Set<Integer> startWordMasks = new HashSet<>();

    // Step 1: Convert each startWord into a bitmask and add to the set
    for (String word : startWords) {
      int mask = getBitMask(word); // Each word becomes a unique 26-bit representation
      startWordMasks.add(mask);    // e.g. "abc" => 0b111 => 7
    }

    int count = 0;

    // Step 2: For each targetWord, try removing each letter and check if the resulting mask exists in startWordMasks
    for (String target : targetWords) {
      int targetMask = getBitMask(target); // Bitmask of entire targetWord

      // Try removing one letter at a time
      for (int i = 0; i < 26; i++) {
        int charBit = 1 << i; // Create a bitmask for character 'a' + i
        // e.g., i = 0 -> 1 << 0 = 0b1 => 'a'
        //       i = 1 -> 1 << 1 = 0b10 => 'b'

        // Check if the ith bit is present in the target mask (i.e., target contains the character)
        if ((targetMask & charBit) != 0) {
          // Remove the ith character by flipping its bit
          int possibleStartMask = targetMask ^ charBit;

          // Check if the resulting word (after removing one letter) exists in startWords
          if (startWordMasks.contains(possibleStartMask)) {
            count++; // Found a valid pair
            break;   // No need to try removing other characters
          }
        }
      }
    }

    return count;
  }

  /**
   * Converts a string into a 26-bit integer using bit masking.
   * Each bit represents one of the lowercase letters 'a' to 'z'.
   *
   * For example:
   *   - "a" -> 0b1             (1 << 0)
   *   - "b" -> 0b10            (1 << 1)
   *   - "abc" -> 0b111         (1 << 0 | 1 << 1 | 1 << 2) = 1 + 2 + 4 = 7
   *
   * This works because bit positions directly correspond to letter positions
   * in the alphabet. No duplicate letters assumed.
   */
  private int getBitMask(String word) {
    int mask = 0;
    for (char ch : word.toCharArray()) {
      int bit = 1 << (ch - 'a'); // Calculate bitmask for the character
      mask |= bit;               // Set the corresponding bit in the final mask
    }
    return mask;
  }
}