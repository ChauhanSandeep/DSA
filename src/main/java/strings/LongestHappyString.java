package strings;

import java.util.PriorityQueue;


/**
 * ---------------------------------------------------------------
 * 💡 Problem: Longest Happy String
 * ---------------------------------------------------------------
 * You are given three integers: `a`, `b`, and `c` representing the number of 'a', 'b', and 'c' characters respectively.
 * Construct the longest possible string such that it does not contain "aaa", "bbb", or "ccc" as a substring.
 * The string is considered "happy" if there are no three consecutive identical characters.
 *
 * 🔹 Example:
 * Input: a = 1, b = 1, c = 7
 * Output: "ccbccacc"
 * Explanation: A valid happy string with no "ccc" and max usage of 'c's.
 *
 * 🔗 Leetcode Link: https://leetcode.com/problems/longest-happy-string/
 *
 * ---------------------------------------------------------------
 * 🔄 Follow-up Questions:
 * ---------------------------------------------------------------
 * 1. Can we extend the logic for N characters (not just 'a', 'b', 'c')?
 *    → Yes, use a max-heap and generalize the logic for any char with streak constraint.
 * 2. Can we do it without heap?
 *    → Yes, greedy character selection can be done using manual comparisons.
 */
public class LongestHappyString {

  public static void main(String[] args) {
    LongestHappyString solver = new LongestHappyString();

    String result = solver.generateHappyStringUsingHeap(1, 1, 7);
    System.out.println("Heap-based happy string: " + result);

    String optimized = solver.generateHappyStringGreedy(1, 1, 7);
    System.out.println("Greedy-based happy string: " + optimized);
  }

  /**
   * Heap-based greedy approach to build the longest "happy" string.
   *
   * Steps:
   * 1. Use a max-heap to always pick the character with the highest remaining count.
   * 2. If last two characters in the result are same as the top char, skip it for now and use the next best.
   * 3. Put back unused characters with updated count.
   *
   * Time Complexity: O(n log k), where n = total characters, k = 3 (fixed size)
   * Space Complexity: O(n) for result, O(k) for heap
   */
  public String generateHappyStringUsingHeap(int aCount, int bCount, int cCount) {
    PriorityQueue<CharCount> maxHeap = new PriorityQueue<>((x, y) -> Integer.compare(y.count, x.count));

      if (aCount > 0) {
          maxHeap.offer(new CharCount('a', aCount));
      }
      if (bCount > 0) {
          maxHeap.offer(new CharCount('b', bCount));
      }
      if (cCount > 0) {
          maxHeap.offer(new CharCount('c', cCount));
      }

    StringBuilder result = new StringBuilder();

    while (!maxHeap.isEmpty()) {
      CharCount best = maxHeap.poll();

      int len = result.length();
      // Check last two characters
      if (len >= 2 && result.charAt(len - 1) == best.character && result.charAt(len - 2) == best.character) {
          if (maxHeap.isEmpty()) {
              break;
          }

        // Try next best character
        CharCount nextBest = maxHeap.poll();
        result.append(nextBest.character);
        nextBest.count--;

          if (nextBest.count > 0) {
              maxHeap.offer(nextBest);
          }
        maxHeap.offer(best);  // Push back the skipped one
      } else {
        result.append(best.character);
        best.count--;

          if (best.count > 0) {
              maxHeap.offer(best);
          }
      }
    }

    return result.toString();
  }

  /**
   * Generates the longest possible happy string using characters 'a', 'b', and 'c'
   * such that no three consecutive characters are the same.
   *
   * Approach:
   * At each step, choose the character with the highest remaining count that
   * hasn't been used in the last two consecutive positions. This ensures we
   * always try to maximize the usage of higher frequency characters while
   * still respecting the 'happy' condition (no three in a row).
   *
   * Why Greedy Works:
   * - We always want to use the character with the highest count to maximize length.
   * - But we avoid it if using it would violate the rule of not having 3 consecutive same characters.
   * - Greedy ensures locally optimal choice leads to a globally valid result.
   *
   * ⏱ Time Complexity: O(a + b + c)
   * We can add at most a + b + c characters, each step is O(1)
   * Space Complexity: O(1)
   * Constant space for arrays and result builder (excluding output string)
   */
  public String generateHappyStringGreedy(int countA, int countB, int countC) {
    StringBuilder happyString = new StringBuilder();

    int[] remainingCounts = new int[]{countA, countB, countC};
    int[] consecutiveStreaks = new int[3]; // streaks for 'a', 'b', 'c'
    char[] charOptions = new char[]{'a', 'b', 'c'};

    while (true) {
      int bestCandidate = -1;

      // Choose the character with max remaining count that doesn’t violate the happy string rule
      for (int i = 0; i < 3; i++) {
        if (remainingCounts[i] > 0 && consecutiveStreaks[i] < 2) {
          if (bestCandidate == -1 || remainingCounts[i] > remainingCounts[bestCandidate]) {
            bestCandidate = i;
          }
        }
      }

        if (bestCandidate == -1) {
            break; // No valid character to add
        }

      // Append the selected character
      happyString.append(charOptions[bestCandidate]);
      remainingCounts[bestCandidate]--;
      consecutiveStreaks[bestCandidate]++;

      // Reset the streaks of other characters
      for (int i = 0; i < 3; i++) {
        if (i != bestCandidate) {
          consecutiveStreaks[i] = 0;
        }
      }
    }

    return happyString.toString();
  }

  /**
   * Helper class for storing a character with its remaining count.
   */
  static class CharCount {
    char character;
    int count;

    CharCount(char character, int count) {
      this.character = character;
      this.count = count;
    }
  }
}
