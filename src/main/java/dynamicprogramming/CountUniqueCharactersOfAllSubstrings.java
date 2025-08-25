package dynamicprogramming;

import java.util.*;


/**
 * Problem: Count Unique Characters of All Substrings of a Given String
 *
 * Given a string s, return the sum of countUniqueChars(t) where t is all the substrings of s.
 * Notice that some substrings can be repeated so in this case you have to count the repeated ones too.
 * A character is unique in string t if it occurs exactly once in t.
 *
 * Example:
 * Input: s = "ABC"
 * Output: 10
 * Explanation: All possible substrings are: "A","B","C","AB","BC","ABC".
 * Unique characters in each substring: [1,1,1,2,2,3].
 * Sum = 1+1+1+2+2+3 = 10
 *
 * LeetCode Problem Link: https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/
 *
 * Follow-up Questions:
 * 1. Q: What if we need to count characters appearing exactly K times in substrings?
 *    A: Extend the contribution method by tracking frequency states and transitions
 * 2. Q: How to handle case-sensitive strings with both upper and lower case?
 *    A: Use HashMap instead of fixed array or extend array size to handle all ASCII characters
 * 3. Q: What if string contains Unicode characters?
 *    A: Use HashMap<Character, Integer> for character indexing instead of array-based approach
 * 4. Q: Find substring with maximum unique characters?
 *    A: Use sliding window with character frequency tracking (https://leetcode.com/problems/longest-substring-without-repeating-characters/)
 * 5. Q: Count substrings with all unique characters?
 *    A: Similar to above, use sliding window and count all valid windows
 */
public class CountUniqueCharactersOfAllSubstrings {

  public static void main(String[] args) {
    CountUniqueCharactersOfAllSubstrings solver = new CountUniqueCharactersOfAllSubstrings();
    System.out.println("Result (Optimized): " + solver.uniqueLetterString("ABC")); // Expected: 10
    System.out.println("Result (Contribution): " + solver.uniqueLetterStringContribution("ABC")); // Expected: 10
  }

  /**
   * Calculates sum of unique characters across all substrings using contribution method.
   *
   * Algorithm Steps:
   * 1. For each character occurrence, calculate how many substrings it appears uniquely in
   * 2. Find previous and next occurrence of same character to determine valid range
   * 3. Count substrings where character appears exactly once using range formula
   * 4. Sum contributions of all character occurrences
   *
   * Time Complexity: O(n) where n is length of string
   * Space Complexity: O(n) for storing character occurrence indices
   *
   * @param inputString String containing only uppercase English letters
   * @return Sum of unique character counts across all substrings
   */
  public int uniqueLetterString(String inputString) {
    if (inputString == null || inputString.length() == 0) {
      return 0;
    }

    // Map to store all occurrence indices for each character
    Map<Character, List<Integer>> characterOccurrences = new HashMap<>();

    // Build occurrence map for all characters
    for (int charIndex = 0; charIndex < inputString.length(); charIndex++) {
      char currentChar = inputString.charAt(charIndex);
      characterOccurrences.computeIfAbsent(currentChar, k -> new ArrayList<>()).add(charIndex);
    }

    int totalUniqueCharacterSum = 0;

    // Calculate contribution of each character occurrence
    for (Map.Entry<Character, List<Integer>> characterEntry : characterOccurrences.entrySet()) {
      List<Integer> occurrenceIndices = characterEntry.getValue();

      for (int occurrenceIndex = 0; occurrenceIndex < occurrenceIndices.size(); occurrenceIndex++) {
        int currentPosition = occurrenceIndices.get(occurrenceIndex);

        // Find boundaries where this character appears uniquely
        int leftBoundary = (occurrenceIndex == 0) ? -1 : occurrenceIndices.get(occurrenceIndex - 1);
        int rightBoundary = (occurrenceIndex == occurrenceIndices.size() - 1) ? inputString.length()
            : occurrenceIndices.get(occurrenceIndex + 1);

        // Calculate number of valid substrings where this character is unique
        int leftRange = currentPosition - leftBoundary;
        int rightRange = rightBoundary - currentPosition;

        totalUniqueCharacterSum += leftRange * rightRange;
      }
    }

    return totalUniqueCharacterSum;
  }

  /**
   * Space-optimized approach using dynamic programming with character contribution tracking.
   *
   * Algorithm Steps:
   * 1. Track last occurrence index of each character
   * 2. For each position, calculate contribution of each character to substrings ending here
   * 3. Update contributions dynamically as we process each character
   * 4. Sum all contributions to get final result
   *
   * Time Complexity: O(n * 26) = O(n) where n is length of string
   * Space Complexity: O(1) - only using fixed size arrays for 26 characters
   *
   * @param inputString String containing only uppercase English letters
   * @return Sum of unique character counts across all substrings
   */
  public int uniqueLetterStringContribution(String inputString) {
    if (inputString == null || inputString.length() == 0) {
      return 0;
    }

    final int ALPHABET_SIZE = 26;

    // Track last seen index for each character (-1 if not seen)
    int[] lastOccurrenceIndex = new int[ALPHABET_SIZE];
    Arrays.fill(lastOccurrenceIndex, -1);

    // Track contribution of each character to current position
    int[] characterContribution = new int[ALPHABET_SIZE];

    int totalUniqueCharacterSum = 0;

    for (int currentIndex = 0; currentIndex < inputString.length(); currentIndex++) {
      char currentChar = inputString.charAt(currentIndex);
      int charArrayIndex = currentChar - 'A';

      // Update contribution for current character
      // Contribution = number of substrings ending at current position where char is unique
      int substringCountEndingHere = currentIndex + 1;
      int lastSeenPosition = lastOccurrenceIndex[charArrayIndex];

      characterContribution[charArrayIndex] = substringCountEndingHere - (lastSeenPosition + 1);

      // Sum contributions of all characters for substrings ending at current position
      int currentPositionContribution = 0;
      for (int charIndex = 0; charIndex < ALPHABET_SIZE; charIndex++) {
        currentPositionContribution += characterContribution[charIndex];
      }

      totalUniqueCharacterSum += currentPositionContribution;

      // Update last occurrence for current character
      lastOccurrenceIndex[charArrayIndex] = currentIndex;
    }

    return totalUniqueCharacterSum;
  }

  /**
   * Alternative implementation using contribution formula with cleaner logic.
   *
   * Algorithm Steps:
   * 1. For each character position, calculate its contribution using mathematical formula
   * 2. Contribution = (current_pos - prev_occurrence) * (next_occurrence - current_pos)
   * 3. Use padding with -1 and string.length() for boundary cases
   * 4. Sum all individual contributions
   *
   * Time Complexity: O(n) where n is length of string
   * Space Complexity: O(n) for storing indices, O(1) if we process on the fly
   *
   * @param inputString String containing only uppercase English letters
   * @return Sum of unique character counts across all substrings
   */
  public int uniqueLetterStringMathematical(String inputString) {
    if (inputString == null || inputString.length() == 0) {
      return 0;
    }

    int stringLength = inputString.length();
    int totalContribution = 0;

    for (int currentIndex = 0; currentIndex < stringLength; currentIndex++) {
      char currentChar = inputString.charAt(currentIndex);

      // Find previous occurrence of same character
      int previousOccurrence = -1;
      for (int leftIndex = currentIndex - 1; leftIndex >= 0; leftIndex--) {
        if (inputString.charAt(leftIndex) == currentChar) {
          previousOccurrence = leftIndex;
          break;
        }
      }

      // Find next occurrence of same character
      int nextOccurrence = stringLength;
      for (int rightIndex = currentIndex + 1; rightIndex < stringLength; rightIndex++) {
        if (inputString.charAt(rightIndex) == currentChar) {
          nextOccurrence = rightIndex;
          break;
        }
      }

      // Calculate contribution using mathematical formula
      int leftDistance = currentIndex - previousOccurrence;
      int rightDistance = nextOccurrence - currentIndex;

      totalContribution += leftDistance * rightDistance;
    }

    return totalContribution;
  }
}
