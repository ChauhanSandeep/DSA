package Backtracking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Problem:
 * Given a pattern string and a target string, determine if there exists a bijective mapping
 * from pattern characters to non-empty substrings of the target string such that the pattern
 * is fully matched.
 *
 * Example:
 * Input: pattern = "abab", str = "redblueredblue"
 * Output: true
 * Explanation:
 *   'a' -> "red", 'b' -> "blue"
 *
 * LeetCode Link: https://leetcode.com/problems/word-pattern-ii/
 *
 * Follow-up Questions (FAANG Style):
 * 1. What if multiple mappings are possible? → Return any one valid mapping.
 * 2. Can we return all possible mappings? → Yes, use a list to store mappings instead of boolean.
 * 3. How would you handle overlapping mappings? → Not possible here due to bijection constraint.
 * 4. Can you optimize substring creation? → Use `StringBuilder` with backtracking to avoid excessive string creation.
 */
public class WordPattern2 {

  public static void main(String[] args) {
    String pattern = "abab";
    String str = "redblueredblue";
    System.out.println("Word matches pattern? " + wordPatternMatch(pattern, str));
  }

  /**
   * Determines if a bijective mapping exists between pattern characters and substrings of the given string.
   *
   * Intuition & Steps:
   * 1. We use a recursive backtracking approach with:
   *    - `Map<Character, String>`: Maps each pattern character to a specific substring.
   *    - `Set<String>`: Tracks substrings already mapped to avoid duplicates.
   * 2. If a pattern character already has a mapping:
   *    - Check if the current position in the string matches the mapped substring.
   *    - If yes, move forward in both pattern and string.
   *    - If no, return false.
   * 3. If no mapping exists:
   *    - Try every possible substring starting from the current position.
   *    - Assign mapping, recurse, and if recursion fails, backtrack.
   * 4. Success occurs when both the pattern and string are fully processed.
   *
   * Time Complexity: O(N^M) where:
   *      N = length of the string, M = length of the pattern.
   *      Because we need to try all possible substrings of length M in the string of length N.
   * Space Complexity: O(M) for the mapping and visited set, plus recursion depth.
   *
   * @param pattern the pattern string (e.g., "abab")
   * @param str the target string (e.g., "redblueredblue")
   * @return true if a valid bijection mapping exists, false otherwise
   */
  public static boolean wordPatternMatch(String pattern, String str) {
    return backtrack(pattern, 0, str, 0, new HashMap<>(), new HashSet<>());
  }

  /**
   * Recursive helper function for backtracking.
   *
   * @param pattern pattern string
   * @param pIndex current index in pattern
   * @param target target string
   * @param tIndex current index in string
   * @param mapping stores current mapping from pattern chars to substrings
   * @param usedSubstrings tracks substrings already assigned to avoid duplicates
   * @return true if a valid mapping is found
   */
  private static boolean backtrack(String pattern, int pIndex, String target, int tIndex, Map<Character, String> mapping,
      Set<String> usedSubstrings) {

    // Base case: both pattern and string fully matched
    if (pIndex == pattern.length() && tIndex == target.length()) {
      return true;
    }

    // Mismatch in length (one finished but the other not)
    if (pIndex >= pattern.length() || tIndex >= target.length()) {
      return false;
    }

    char currentChar = pattern.charAt(pIndex);

    // Case 1: Already mapped character
    if (mapping.containsKey(currentChar)) {
      String mappedValue = mapping.get(currentChar);

      // Check if the substring at tIndex matches mapped value
      if (!target.startsWith(mappedValue, tIndex)) {
        return false;
      }

      // Continue to the next pattern character
      return backtrack(pattern, pIndex + 1, target, tIndex + mappedValue.length(), mapping, usedSubstrings);
    }

    // Case 2: New mapping needed
    for (int end = tIndex + 1; end <= target.length(); end++) {
      String candidate = target.substring(tIndex, end);

      // Skip if substring already used for another mapping
      if (usedSubstrings.contains(candidate)) {
        continue;
      }

      // Assign and recurse
      mapping.put(currentChar, candidate);
      usedSubstrings.add(candidate);

      if (backtrack(pattern, pIndex + 1, target, end, mapping, usedSubstrings)) {
        return true;
      }

      // Backtrack: undo assignment
      mapping.remove(currentChar);
      usedSubstrings.remove(candidate);
    }

    return false;
  }
}
