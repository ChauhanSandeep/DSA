package strings;

import java.util.HashSet;
import java.util.Set;


/**
 * Problem: Permutations II
 *
 * Generate every unique permutation of a string that may contain duplicate
 * characters. This file keeps both swap-based and prefix-based backtracking.
 *
 * Leetcode: https://leetcode.com/problems/permutations-ii/ (Medium)
 * Pattern:  Backtracking | Deduplication | Set of permutations
 *
 * Example:
 *   Input:  input = "AAB"
 *   Output: ["AAB", "ABA", "BAA"]
 *   Why:    duplicate A characters make several swap paths produce the same strings.
 *
 * Follow-ups:
 *   1. Avoid a result set for dedupe? Sort and skip equal choices at each depth.
 *   2. Stream results? Use a callback or iterator instead of storing all permutations.
 *   3. Lexicographic order? Sort input and choose candidates in order.
 *
 * Related: Permutations (46), Next Permutation (31).
 */
public class PermutationII {

  public static void main(String[] args) {
    String[] inputs = {"ABC", "AAB"};
    int[] expected = {6, 3};
    for (int i = 0; i < inputs.length; i++) {
      Set<String> got = generateAllUniquePermutations(inputs[i]);
      System.out.printf("input=%s -> count=%d values=%s  expected=%d%n", inputs[i], got.size(), got, expected[i]);
    }
  }


    /**
   * Intuition: fix one position at a time by swapping each suffix character into
   * it, then recurse on the next position. A set removes duplicate permutations
   * created by equal input characters.
   *
   * Algorithm:
   *   1. Create a set for completed permutations.
   *   2. Start swap-based backtracking at index 0.
   *   3. Swap each suffix candidate into the fixed position and recurse.
   *   4. Add every completed string to the set.
   *
   * Time:  O(n*n!) - each completed permutation costs O(n) to store.
   * Space: O(n*n!) - all unique permutations are returned.
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
