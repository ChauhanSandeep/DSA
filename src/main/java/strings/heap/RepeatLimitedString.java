package strings.heap;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * LeetCode Problem: https://leetcode.com/problems/construct-string-with-repeat-limit/
 *
 * Problem Statement:
 * Given a string `s` and an integer `repeatLimit`, construct the **lexicographically largest string**
 * that can be made from `s` such that **no character repeats more than `repeatLimit` times consecutively**.
 *
 * Example:
 * Input: s = "cczazcc", repeatLimit = 3
 * Output: "zzcccac"
 *
 * Follow-up Questions:
 * 1. What if repeatLimit is very large or very small?
 *    - Large repeatLimit means fewer switches; small repeatLimit may require many different characters.
 * 2. Can you solve it without using a priority queue?
 *    - Yes, by simulating character access using a frequency array from 'z' to 'a' and maintaining indices.
 * 3. How to solve for streaming data (continuous characters arriving)?
 *    - Requires window-based frequency handling and deferred write strategies.
 * LeetCode Contest Rating: 1680
 **/

public class RepeatLimitedString {

  public static void main(String[] args) {
    System.out.println(repeatLimitedString("aababab", 2));   // Output: "bababa"
    System.out.println(repeatLimitedString("cczazcc", 3));   // Output: "zzcccac"
    System.out.println(repeatLimitedString("a", 1));         // Output: "a"
  }

  /**
   * Constructs the lexicographically largest string with a repeat limit on consecutive characters.
   *
   * Steps:
   * 1. Count the frequency of each character in the string.
   * 2. Use a max-heap (priority queue) to always pick the lexicographically largest character.
   * 3. Append it up to `repeatLimit` times.
   * 4. If still remaining, use the next largest character as a breaker.
   * 5. Reinsert characters with remaining frequencies back into the heap.
   *
   * Time Complexity: O(N log 26) ≈ O(N), where N = length of input string.
   * Space Complexity: O(1) — constant space for frequency map since only lowercase letters are used.
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
