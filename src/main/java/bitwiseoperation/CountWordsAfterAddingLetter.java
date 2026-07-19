package bitwiseoperation;

import java.util.*;


/**
 * Problem: Count Words Obtained After Adding a Letter
 *
 * Given start words and target words, count how many targets can be made by
 * adding exactly one new letter to a start word and then rearranging the
 * letters. The bitmask solution assumes each word has unique lowercase letters.
 *
 * Leetcode: https://leetcode.com/problems/count-words-obtained-after-adding-a-letter/ (Medium)
 * Rating:   1828 (zerotrac Elo)
 * Pattern:  Bit manipulation | Letter-set mask | Reverse one-letter deletion
 *
 * Example:
 *   Input:  startWords = ["ant", "act", "tack"], targetWords = ["tack", "act", "acti"]
 *   Output: 2
 *   Why:    "tack" can delete k to match "act", and "acti" can delete i to
 *           match "act"; "act" itself is not one letter longer than any start word.
 *
 * Follow-ups:
 *   1. What if words may contain duplicate letters?
 *      Use sorted strings or a 26-count signature because one bit cannot store multiplicity.
 *   2. Can target words be processed as a stream?
 *      Keep the start-word masks in a set and test each target independently.
 *   3. How would you return the matching start word too?
 *      Map each mask to an example start word and return it when a removed-letter mask matches.
 *   4. What if the alphabet is larger than 26 letters?
 *      Use a BitSet or sorted-character signature instead of packing letters into one int.
 *
 * Related: Maximum Product of Word Lengths (318), Word Subsets (916).
 */
public class CountWordsAfterAddingLetter {

  public static void main(String[] args) {
    CountWordsAfterAddingLetter solver = new CountWordsAfterAddingLetter();

    String[][] startCases = {
        {"ant", "act", "tack"},
        {"ab", "a"},
        {}
    };
    String[][] targetCases = {
        {"tack", "act", "acti"},
        {"abc", "abcd"},
        {"a"}
    };
    int[] expected = {2, 1, 0};

    for (int i = 0; i < startCases.length; i++) {
      int output = solver.wordCountUsingBitmask(startCases[i], targetCases[i]);
      System.out.printf("startWords=%s targetWords=%s -> %d  expected=%d%n",
          Arrays.toString(startCases[i]), Arrays.toString(targetCases[i]), output, expected[i]);
    }
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

  /** Returns the characters of the word in sorted order. */
  private String sortChars(String word) {
    char[] chars = word.toCharArray();
    Arrays.sort(chars);
    return new String(chars);
  }

  /**
   * Intuition: a word with unique lowercase letters can be represented as a
   * 26-bit set, where bit i means the word contains the character 'a' + i. A
   * target is valid if removing exactly one present bit leaves a mask already
   * seen among the start words. The original loop tries all 26 bit positions,
   * skips letters absent from the target, and checks each one-letter deletion.
   *
   * Algorithm:
   *   1. Convert every start word to its bitmask and store the masks in a set.
   *   2. Convert each target word to a mask.
   *   3. For each of the 26 possible letters, remove it with XOR if present.
   *   4. Count the target once when the removed-letter mask exists in the start set.
   *
   * Time:  O(W * L + T * 26) - each start word is scanned once, and each target tests 26 letter bits.
   * Space: O(W) - one integer mask is stored for each start word.
   *
   * @param startWords words before adding one letter
   * @param targetWords candidate words after adding one letter and rearranging
   * @return count of target words that can be formed
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

  /** Converts a lowercase word with unique letters into a 26-bit letter mask. */
  private int getBitMask(String word) {
    int mask = 0;
    for (char ch : word.toCharArray()) {
      int bit = 1 << (ch - 'a'); // Calculate bitmask for the character
      mask |= bit;               // Set the corresponding bit in the final mask
    }
    return mask;
  }
}