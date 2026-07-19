package strings.greedy;

import java.util.PriorityQueue;


/**
 * Problem: Longest Happy String
 *
 * Given counts of 'a', 'b', and 'c', build the longest possible string that
 * never contains "aaa", "bbb", or "ccc" as a substring. Any longest valid
 * string may be returned.
 *
 * Leetcode: https://leetcode.com/problems/longest-happy-string/ (Medium)
 * Rating:   acceptance 56.5% (Medium), contest rating 1821
 * Pattern:  Greedy | Max heap | Avoid three consecutive equal characters
 *
 * Example:
 *   Input:  a = 1, b = 1, c = 7
 *   Output: "ccaccbcc"
 *   Why:    it uses as many characters as possible while placing a or b before a third c.
 *
 * Follow-ups:
 *   1. Generalize to k character types?
 *      Store all remaining counts in the same max heap and skip choices that violate the streak limit.
 *   2. Change the limit from 2 to L repeats?
 *      Track the current run length and only block a character when its run reaches L.
 *   3. Return the lexicographically smallest longest happy string?
 *      Break ties by smaller character while still respecting the same feasibility rule.
 *
 * Related: Reorganize String (767), Task Scheduler (621), Construct String With Repeat Limit (2182).
 */
public class LongestHappyString {

    public static void main(String[] args) {
        LongestHappyString solver = new LongestHappyString();
        int[][] counts = { {1, 1, 7}, {2, 2, 1}, {0, 0, 0} };
        String[] expected = {"ccaccbcc", "ababc", ""};

        for (int i = 0; i < counts.length; i++) {
            String got = solver.generateHappyStringUsingHeap(counts[i][0], counts[i][1], counts[i][2]);
            System.out.printf("counts=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(counts[i]), got, expected[i]);
        }
    }


  /**
   * Intuition: always spend the character with the largest remaining count, unless
   * it would create three equal characters in a row. In that blocked case, the
   * next largest character is the cheapest separator that lets the dominant
   * character be used again later.
   *
   * Algorithm:
   *   1. Push every positive character count into a max heap.
   *   2. Pop the best character and append it if it does not match the last two characters.
   *   3. If it is blocked, append the next best character and push the blocked one back.
   *   4. Reinsert any character whose remaining count is still positive.
   *
   * Time:  O(n log k) - one heap operation per appended character, with k = 3.
   * Space: O(n) - the builder stores the answer and the heap stores at most three entries.
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
