package strings;

/**
 * LeetCode Problem: Count and Say
 * https://leetcode.com/problems/count-and-say/
 *
 * 🔹 Problem Statement:
 * Given an integer `n`, generate the nth term of the "Count and Say" sequence.
 *
 * The "Count and Say" sequence is a series of strings where each term is generated
 * by reading aloud the digits of the previous term and counting the number of digits in groups of the same digit.
 *
 * 🔸 Example:
 * Input: n = 4
 * Output: "1211"
 * Explanation:
 * - Term 1: "1"
 * - Term 2: "11"        (one 1)
 * - Term 3: "21"        (two 1s)
 * - Term 4: "1211"      (one 2, one 1)
 *
 * Follow-up Questions:
 * - How would you optimize memory if you don't need to store full intermediate sequences?
 * Answer: You can use a single StringBuilder to build the next sequence without storing all previous terms.
 * - Can this be generated without string builders using character arrays?
 * Answer: Yes, by using a character array and managing indices.
 */
public class CountAndSay {

  public static void main(String[] args) {
    CountAndSay solver = new CountAndSay();
    System.out.println(solver.countAndSay(4));  // Output: "1211"
  }

  /**
   * Generates the nth term in the "Count and Say" sequence.
   *
   * @param n The sequence term to generate.
   * @return The nth term as a string.
   *
   * Steps:
   * - Start with base string "1".
   * - Traverse the string and count consecutive characters.
   * - Append count followed by the digit.
   * - Repeat until reaching the nth term.
   *
   * ⏱ Time Complexity: O(N * M), where:
   *     - N = input `n`
   *     - M = average length of each generated term (which grows approximately ~2x each time)
   * 🧠 Space Complexity: O(M), space used to build each term.
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
