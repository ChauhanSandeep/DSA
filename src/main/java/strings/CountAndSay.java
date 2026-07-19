package strings;

/**
 * Problem: Count and Say
 *
 * Generate the nth count-and-say term. Each term describes the previous term by
 * writing the count and digit for every run of equal adjacent digits.
 *
 * Leetcode: https://leetcode.com/problems/count-and-say/ (Medium)
 * Rating:   acceptance 63.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  String | Run-length encoding | Iterative generation
 *
 * Example:
 *   Input:  n = 4
 *   Output: "1211"
 *   Why:    term 3 is "21", read as one 2 and one 1.
 *
 * Follow-ups:
 *   1. Reduce memory? Keep only the current and next term.
 *   2. Reduce allocations? Reuse StringBuilder capacity between rounds.
 *   3. Arbitrary seed? Start from the seed and apply the same encoding step.
 *
 * Related: String Compression (443).
 */
public class CountAndSay {

  public static void main(String[] args) {
    CountAndSay solver = new CountAndSay();
    int[] inputs = {1, 4, 5};
    String[] expected = {"1", "1211", "111221"};
    for (int i = 0; i < inputs.length; i++) {
      String got = solver.countAndSay(inputs[i]);
      System.out.printf("n=%d -> %s  expected=%s%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: each term is the run-length encoding of the previous term. Keep
   * only the current term, generate the next encoded term, and repeat until the
   * requested position is reached.
   *
   * Algorithm:
   *   1. Return an empty string for non-positive n.
   *   2. Start from the base term "1".
   *   3. Generate the next term n - 1 times.
   *   4. Return the final term.
   *
   * Time:  O(n*m) - n rounds scan the current term of length m.
   * Space: O(m) - builders hold the current and next term.
   *
   * @param n one-based term index
   * @return nth count-and-say term
   */
  public String countAndSay(int n) {
      if (n <= 0) {
          return "";  // Handle invalid input
      }

    StringBuilder currentSequence = new StringBuilder("1");

    for (int i = 1; i < n; i++) {
      currentSequence = generateNextSequence(currentSequence);
    }

    return currentSequence.toString();
  }

  // Generates the next sequence based on the previous sequence.
  /** Builds the next count-and-say term from the current term. */
  private StringBuilder generateNextSequence(StringBuilder sequence) {
    StringBuilder nextSequence = new StringBuilder();

    int count = 1;
    char currChar = sequence.charAt(0);

    for (int i = 1; i < sequence.length(); i++) {
      char nextChar = sequence.charAt(i);

      if (nextChar == currChar) {
        count++;
      } else {
        nextSequence.append(count).append(currChar);
        currChar = nextChar;
        count = 1;
      }
    }

    // Append the final group
    nextSequence.append(count).append(currChar);
    return nextSequence;
  }
}
