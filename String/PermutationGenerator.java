package String;

import java.util.HashSet;
import java.util.Set;


/**
 * Problem: Generate all unique permutations of a given string.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/permutations-ii/
 *
 * 🔍 Example:
 * Input: "ABC"
 * Output: ["ABC", "ACB", "BAC", "BCA", "CAB", "CBA"]
 *
 * 🧠 Intuition:
 * - We can fix each character at one position and recursively permute the rest.
 * - For uniqueness, we use a Set to avoid duplicates.
 *
 * ✅ Constraints:
 * - The string may contain duplicate characters.
 * - The output must contain unique permutations only.
 *
 * 📌 Follow-up Questions (FAANG-style):
 * 1. How would you solve this for a list of characters instead of a string?
 *    - Use a character array, backtracking with a visited boolean[].
 *    - Related: https://leetcode.com/problems/permutations/
 *
 * 2. What if the input string is very large?
 *    - Avoid storing all permutations; yield one at a time using a generator or iterator pattern.
 *
 * 3. Can you sort permutations lexicographically without using a Set?
 *    - Yes. Sort the string initially and skip duplicates during backtracking.
 */
public class PermutationGenerator {

  public static void main(String[] args) {
    String input = "ABC";
    System.out.println("Using swap-based method: " + generateAllUniquePermutations(input));
    System.out.println("Using prefix-based method: " + generateAllUniquePermutationsWithPrefix(input));
  }

  /**
   * Generates all unique permutations of a given string using backtracking and character swapping.
   *
   * Steps:
   * - Fix one character at a time by swapping.
   * - Recur to permute the rest of the string.
   * - Use a Set to ensure uniqueness.
   *
   * Time Complexity: O(N * N!) → N! permutations, each takes O(N) to construct.
   * Space Complexity: O(N!) → for storing all permutations.
   *
   * @param input input string
   * @return set of unique permutations
   */
  public static Set<String> generateAllUniquePermutations(String input) {
    Set<String> uniquePermutations = new HashSet<>();
    generatePermutationsBySwapping(input, 0, uniquePermutations);
    return uniquePermutations;
  }

  /**
   * Recursive backtracking helper function that swaps characters to generate permutations.
   *
   * @param current     current string to permute
   * @param startIndex  current index to fix
   * @param result      set to store unique permutations
   */
  private static void generatePermutationsBySwapping(String current, int startIndex, Set<String> result) {
    if (startIndex == current.length() - 1) {
      result.add(current);
      return;
    }

    for (int i = startIndex; i < current.length(); i++) {
      current = swapCharacters(current, startIndex, i);
      generatePermutationsBySwapping(current, startIndex + 1, result);
      current = swapCharacters(current, startIndex, i); // Backtrack
    }
  }

  /**
   * Swaps characters at positions i and j in the input string.
   *
   * @param str input string
   * @param i   index i
   * @param j   index j
   * @return string with characters at i and j swapped
   */
  private static String swapCharacters(String str, int i, int j) {
    if (i == j) {
      return str;
    }
    char[] characters = str.toCharArray();
    char temp = characters[i];
    characters[i] = characters[j];
    characters[j] = temp;
    return new String(characters);
  }

  /**
   * Alternative way to generate permutations using prefix and remaining string.
   *
   * Steps:
   * - Fix one character as prefix.
   * - Recur on the remaining characters.
   *
   * Time Complexity: O(N * N!)
   * Space Complexity: O(N!) for storing results.
   *
   * @param input input string
   * @return set of unique permutations
   */
  public static Set<String> generateAllUniquePermutationsWithPrefix(String input) {
    Set<String> result = new HashSet<>();
    generatePermutationsByPrefix("", input, result);
    return result;
  }

  /**
   * Recursive helper using prefix + remaining approach.
   *
   * @param prefix     prefix built so far
   * @param remaining  remaining characters
   * @param result     set to store unique permutations
   */
  private static void generatePermutationsByPrefix(String prefix, String remaining, Set<String> result) {
    if (remaining.isEmpty()) {
      result.add(prefix);
      return;
    }

    for (int i = 0; i < remaining.length(); i++) {
      char currentChar = remaining.charAt(i);
      String newPrefix = prefix + currentChar;
      String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
      generatePermutationsByPrefix(newPrefix, newRemaining, result);
    }
  }
}
