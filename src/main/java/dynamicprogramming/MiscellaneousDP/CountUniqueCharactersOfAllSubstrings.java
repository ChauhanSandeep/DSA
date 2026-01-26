package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * Count Unique Characters of All Substrings of a Given String
 *
 * Let's define a function countUniqueChars(s) that returns the number of unique
 * characters in s. A character is unique if it appears exactly once in the string.
 *
 * Given a string s, return the sum of countUniqueChars(t) where t is a substring of s.
 * The test cases are generated such that the answer fits in a 32-bit integer.
 *
 * Key insight: Instead of examining every substring, we calculate how many substrings
 * each character contributes to as a unique character. This transforms an O(n³)
 * brute force approach into an efficient O(n) solution.
 *
 * Example:
 * Input: s = "ABC"
 * Output: 10
 * Explanation: All substrings are: "A"(1), "B"(1), "C"(1), "AB"(2), "BC"(2), "ABC"(3)
 * Sum = 1 + 1 + 1 + 2 + 2 + 3 = 10
 *
 * Example:
 * Input: s = "ABA"
 * Output: 8
 * Explanation: Substrings: "A"(1), "B"(1), "A"(1), "AB"(2), "BA"(1), "ABA"(1)
 * Sum = 1 + 1 + 1 + 2 + 1 + 1 = 8 (Note: "ABA" has only B as unique)
 *
 * LeetCode: https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. What if we need to find substrings with exactly k unique characters?
 *    Answer: Use sliding window technique with character frequency tracking and adjust window size dynamically.
 * 2. How to handle case-sensitive strings with both uppercase and lowercase letters?
 *    Answer: Extend position arrays from 26 to 58 (26*2 + 6 for other ASCII chars) or use HashMap.
 * 3. What if string length is extremely large (billions of characters) with patterns?
 *    Answer: Use string compression, pattern recognition, or mathematical formulas for repeating sequences.
 * 4. How would you modify this to count characters that appear at least k times?
 *    Answer: Change uniqueness criteria and modify contribution calculation with different boundary constraints.
 * 5: Find substring with maximum unique characters?
 *  * Use sliding window with character frequency tracking (https://leetcode.com/problems/longest-substring-without-repeating-characters/)
 *
 * Related Problems:
 * - LeetCode 3: Longest Substring Without Repeating Characters
 * - LeetCode 159: Longest Substring with At Most Two Distinct Characters
 * - LeetCode 992: Subarrays with K Different Integers
 * LeetCode Contest Rating: 2034
 */
public class CountUniqueCharactersOfAllSubstrings {

  public static void main(String[] args) {
    CountUniqueCharactersOfAllSubstrings solver = new CountUniqueCharactersOfAllSubstrings();
    System.out.println("Result (Optimized): " + solver.uniqueLetterString("ABC")); // Expected: 10
  }

/**
 * Approach (Contribution Method):
 * Instead of generating all substrings, we calculate the contribution of each character occurrence
 * directly using distances to its previous and next occurrence.
 *
 * Steps:
 * 1. Create a map from each character to a list of its positions in the string.
 *    - Initialize each list with -1 as a left boundary.
 * 2. Traverse the string and record positions of each character in the map.
 * 3. For each character's position list:
 *    - Append string.length() as a right boundary.
 *    - For every actual occurrence index 'i':
 *         * Compute distance to its previous occurrence: (positions[i] - positions[i-1])
 *         * Compute distance to its next occurrence: (positions[i+1] - positions[i])
 *         * The product of these two distances gives the number of substrings where this occurrence of the character is unique.
 *    - Add all such contributions to the total sum.
 */
 public int uniqueLetterString(String inputString) {
    if (inputString == null || inputString.isEmpty()) {
      return 0;
    }

    // Map character to list of its positions
    Map<Character, List<Integer>> characterIndexMap = new HashMap<>();

    // Build position map with boundary markers
    for (int i = 0; i < inputString.length(); i++) {
      char currentChar = inputString.charAt(i);

      characterIndexMap.computeIfAbsent(currentChar, k -> {
        List<Integer> list = new ArrayList<>();
        list.add(-1); // Left boundary
        return list;
      }).add(i);
    }

    int totalSum = 0;

    // Calculate contributions for each character
    for (List<Integer> positions : characterIndexMap.values()) {
      positions.add(inputString.length()); // Right boundary

      // Apply contribution formula for each position
      for (int i = 1; i < positions.size() - 1; i++) {
        int leftDistance = positions.get(i) - positions.get(i - 1);
        int rightDistance = positions.get(i + 1) - positions.get(i);
        totalSum += leftDistance * rightDistance; // Because left * right gives count of substrings where this char is unique
      }
    }

    return totalSum;
  }
}
