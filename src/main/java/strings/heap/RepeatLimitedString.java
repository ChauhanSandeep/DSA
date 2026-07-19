package strings.heap;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * Problem: Construct String With Repeat Limit
 *
 * Rearrange characters from s to build the lexicographically largest possible
 * string where no character appears more than repeatLimit times consecutively.
 * Not every input character must be used.
 *
 * Leetcode: https://leetcode.com/problems/construct-string-with-repeat-limit/ (Medium)
 * Rating:   acceptance 67.1% (Medium), contest rating 1680
 * Pattern:  Greedy | Max heap | Break repeated runs
 *
 * Example:
 *   Input:  s = "cczazcc", repeatLimit = 3
 *   Output: "zzcccac"
 *   Why:    z is used first for lexicographic size, and a smaller breaker allows more c characters.
 *
 * Follow-ups:
 *   1. Solve without a heap?
 *      Use a 26-count array and scan from z down to find the current and breaker characters.
 *   2. Require using every character?
 *      Return impossible when no breaker exists for a still-over-limit character.
 *   3. Support arbitrary alphabets?
 *      Replace character comparison and frequency storage with an ordered map or heap entries.
 *
 * Related: Longest Happy String (1405), Reorganize String (767), Task Scheduler (621).
 */
public class RepeatLimitedString {

    public static void main(String[] args) {
        String[] inputs = {"cczazcc", "aababab", "a"};
        int[] limits = {3, 2, 1};
        String[] expected = {"zzcccac", "bbabaa", "a"};

        for (int i = 0; i < inputs.length; i++) {
            String got = repeatLimitedString(inputs[i], limits[i]);
            System.out.printf("s=%s repeatLimit=%d -> %s  expected=%s%n", inputs[i], limits[i], got, expected[i]);
        }
    }


  /**
   * Intuition: lexicographic maximum means always try the largest remaining
   * character first. If that character still has copies after hitting the repeat
   * limit, insert the next largest available character as a breaker so the larger
   * character can be used again.
   *
   * Algorithm:
   *   1. Count character frequencies.
   *   2. Put characters in a max heap ordered by character value.
   *   3. Append the largest character up to repeatLimit times.
   *   4. If copies remain, append one breaker character and reinsert remaining counts.
   *
   * Time:  O(n log k) - each appended character may update the heap, with k distinct characters.
   * Space: O(k) - the heap and frequency map store distinct characters only.
   */
  public static String repeatLimitedString(String str, int repeatLimit) {
    // Step 1: Count frequencies
    Map<Character, Integer> freqMap = new HashMap<>();
    for (char ch : str.toCharArray()) {
      freqMap.put(ch, freqMap.getOrDefault(ch, 0) + 1);
    }

    // Step 2: Max heap sorted by descending character value (lexicographically largest first)
    PriorityQueue<CharNode> maxHeap = new PriorityQueue<>((a, b) -> Character.compare(b.character, a.character));

    for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
      maxHeap.offer(new CharNode(entry.getKey(), entry.getValue()));
    }

    StringBuilder result = new StringBuilder();

    while (!maxHeap.isEmpty()) {
      CharNode current = maxHeap.poll();
      int usableCount = Math.min(current.frequency, repeatLimit);

      // Step 3: Append character up to repeatLimit times
      for (int i = 0; i < usableCount; i++) {
        result.append(current.character);
      }
      current.frequency -= usableCount;

      if (current.frequency > 0) {
        // Step 4: Need a breaker character if current still has frequency
          if (maxHeap.isEmpty()) {
              break; // No breaker available, can't add more
          }

        CharNode next = maxHeap.poll(); // Get next lexicographically smaller character
        result.append(next.character);
        next.frequency--;

        // Reinsert both if they still have remaining counts
          if (next.frequency > 0) {
              maxHeap.offer(next);
          }
        maxHeap.offer(current);
      }
    }

    return result.toString();
  }

  /**
   * Helper class to represent a character and its frequency.
   */
  static class CharNode {
    char character;
    int frequency;

    public CharNode(char character, int frequency) {
      this.character = character;
      this.frequency = frequency;
    }
  }
}
