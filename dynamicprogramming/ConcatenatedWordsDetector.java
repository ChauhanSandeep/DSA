package dynamicprogramming;

import java.util.*;


/**
 * Problem: Concatenated Words
 *
 * Given an array of strings words (without duplicates), return all the concatenated words in the given list of words.
 * A concatenated word is defined as a string that is comprised entirely of at least two shorter words
 * in the given array.
 *
 * Example:
 * Input: words = ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
 * Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
 * Explanation: "catsdogcats" can be concatenated by "cats", "dog" and "cats";
 *              "dogcatsdog" can be concatenated by "dog", "cats" and "dog";
 *              "ratcatdogcat" can be concatenated by "rat", "cat", "dog" and "cat".
 *
 * LeetCode Problem Link: https://leetcode.com/problems/concatenated-words/
 *
 * Follow-up Questions:
 * 1. Q: What if we want to find the minimum number of words needed to form each concatenated word?
 *    A: Modify DP to track minimum count: dp[i] = min count to form word[0..i-1]
 * 2. Q: How to find all possible ways to split a concatenated word?
 *    A: Use DFS with backtracking to find all valid combinations
 * 3. Q: What if words can be reused multiple times in concatenation?
 *    A: Current solution already handles this - same word can appear multiple times
 * 4. Q: Find longest concatenated word?
 *    A: Track maximum length during processing and return the longest found
 * 5. Q: Word Break II - return all possible sentences?
 *    A: Use DFS with memoization to return all valid word combinations
 *       (https://leetcode.com/problems/word-break-ii/)
 */
public class ConcatenatedWordsDetector {

  public static void main(String[] args) {
    String[] wordDictionary =
        {"cat", "cats", "catsdogcats", "dog", "dogcatsdog", "hippopotamuses", "rat", "ratcatdogcat"};

    ConcatenatedWordsDetector solver = new ConcatenatedWordsDetector();
    List<String> result = solver.findAllConcatenatedWordsInArray(wordDictionary);
    System.out.println("Concatenated Words: " + result);

    List<String> optimizedResult = solver.findAllConcatenatedWordsOptimized(wordDictionary);
    System.out.println("Concatenated Words (Optimized): " + optimizedResult);
  }

  /**
   * Finds all concatenated words using dynamic programming approach with sorted processing.
   *
   * Algorithm Steps:
   * 1. Sort words by length to process shorter words first
   * 2. Build dictionary incrementally with processed words
   * 3. For each word, check if it can be formed from existing dictionary words
   * 4. Use DP to verify if word can be broken into valid dictionary words
   * 5. Add valid concatenated words to result and current word to dictionary
   *
   * Time Complexity: O(n log n + n * m^2) where n is number of words, m is average word length
   * Space Complexity: O(n * m) for dictionary storage and DP array
   *
   * @param inputWords Array of strings without duplicates
   * @return List of all concatenated words found in input
   */
  public List<String> findAllConcatenatedWordsInArray(String[] inputWords) {
    if (inputWords == null || inputWords.length < 2) {
      return new ArrayList<>();
    }

    List<String> concatenatedWordsList = new ArrayList<>();
    Set<String> availableWordsDictionary = new HashSet<>();

    // Sort words by length to ensure we build from shorter to longer words
    Arrays.sort(inputWords, (firstWord, secondWord) -> Integer.compare(firstWord.length(), secondWord.length()));

    for (String currentWord : inputWords) {
      // Skip empty words and check if current word can be formed from dictionary
      if (!currentWord.isEmpty() && canWordBeFormedFromDictionary(currentWord, availableWordsDictionary)) {
        concatenatedWordsList.add(currentWord);
      }

      // Add current word to dictionary for future processing
      availableWordsDictionary.add(currentWord);
    }

    return concatenatedWordsList;
  }

  /**
   * Checks if a word can be formed by concatenating words from the dictionary using DP.
   *
   * Algorithm Steps:
   * 1. Create DP array where dp[i] represents if word[0..i-1] can be formed
   * 2. Base case: dp[0] = true (empty string can always be formed)
   * 3. For each position, check all possible word endings
   * 4. If word[left..right-1] exists in dictionary and dp[left] is true, set dp[right] = true
   * 5. Return dp[wordLength] indicating if entire word can be formed
   *
   * Time Complexity: O(m^2) where m is length of word
   * Space Complexity: O(m) for DP array
   *
   * @param wordToCheck The word to verify for concatenation possibility
   * @param wordDictionary Set of available words for concatenation
   * @return true if word can be formed by concatenating dictionary words
   */
  private boolean canWordBeFormedFromDictionary(String wordToCheck, Set<String> wordDictionary) {
    if (wordDictionary.isEmpty()) {
      return false;
    }

    int wordLength = wordToCheck.length();

    // DP array where dp[i] indicates if word[0..i-1] can be formed from dictionary
    boolean[] canBeFormedUpToIndex = new boolean[wordLength + 1];
    canBeFormedUpToIndex[0] = true; // Empty string can always be formed

    // Check all possible ending positions
    for (int endingIndex = 1; endingIndex <= wordLength; endingIndex++) {

      // Try all possible starting positions for current ending
      for (int startingIndex = 0; startingIndex < endingIndex; startingIndex++) {

        // Skip if prefix cannot be formed
        if (!canBeFormedUpToIndex[startingIndex]) {
          continue;
        }

        // Extract substring and check if it exists in dictionary
        String currentSubstring = wordToCheck.substring(startingIndex, endingIndex);

        if (wordDictionary.contains(currentSubstring)) {
          canBeFormedUpToIndex[endingIndex] = true;
          break; // Found valid formation, no need to check other starting positions
        }
      }
    }

    return canBeFormedUpToIndex[wordLength];
  }

  /**
   * Optimized version using DFS with memoization for better performance on sparse dictionaries.
   *
   * Algorithm Steps:
   * 1. Build complete dictionary first without sorting
   * 2. For each word, use DFS to check if it can be split into valid parts
   * 3. Use memoization to avoid redundant calculations
   * 4. Ensure at least 2 parts are used (not just the word itself)
   *
   * Time Complexity: O(n * m^2) where n is number of words, m is average word length
   * Space Complexity: O(n * m) for dictionary and memoization
   *
   * @param inputWords Array of strings without duplicates
   * @return List of all concatenated words found in input
   */
  public List<String> findAllConcatenatedWordsOptimized(String[] inputWords) {
    if (inputWords == null || inputWords.length < 2) {
      return new ArrayList<>();
    }

    Set<String> completeWordDictionary = new HashSet<>(Arrays.asList(inputWords));
    List<String> concatenatedWordsList = new ArrayList<>();

    for (String currentWord : inputWords) {
      if (canWordBeSplitIntoParts(currentWord, completeWordDictionary, new HashMap<>(), 0)) {
        concatenatedWordsList.add(currentWord);
      }
    }

    return concatenatedWordsList;
  }

  /**
   * DFS helper method with memoization to check if word can be split into valid parts.
   *
   * Algorithm Steps:
   * 1. Base case: if we've processed entire word and used multiple parts, return true
   * 2. Check memoization cache for current position
   * 3. Try all possible prefixes from current position
   * 4. Recursively check if remaining part can be split
   * 5. Cache and return result
   *
   * Time Complexity: O(m^2) per word with memoization
   * Space Complexity: O(m) for recursion stack and memoization
   *
   * @param wordToSplit Word being analyzed for splitting
   * @param wordDictionary Complete set of available words
   * @param memoizationCache Cache for previously computed results
   * @param currentIndex Current position in word being processed
   * @return true if word can be split starting from currentIndex
   */
  private boolean canWordBeSplitIntoParts(String wordToSplit, Set<String> wordDictionary,
      Map<Integer, Boolean> memoizationCache, int currentIndex) {

    // Base case: reached end of word, valid if we used multiple parts
    if (currentIndex == wordToSplit.length()) {
      return true;
    }

    // Check memoization cache
    if (memoizationCache.containsKey(currentIndex)) {
      return memoizationCache.get(currentIndex);
    }

    // Try all possible prefixes from current position
    for (int endIndex = currentIndex + 1; endIndex <= wordToSplit.length(); endIndex++) {
      String currentPrefix = wordToSplit.substring(currentIndex, endIndex);

      // Check if prefix exists in dictionary (but not the entire word to ensure concatenation)
      if (wordDictionary.contains(currentPrefix) && !currentPrefix.equals(wordToSplit) && canWordBeSplitIntoParts(
          wordToSplit, wordDictionary, memoizationCache, endIndex)) {

        memoizationCache.put(currentIndex, true);
        return true;
      }
    }

    // No valid split found from current position
    memoizationCache.put(currentIndex, false);
    return false;
  }

  /**
   * Alternative implementation that returns concatenated words with their constituent parts.
   *
   * Time Complexity: O(n * m^2) where n is number of words, m is average word length
   * Space Complexity: O(n * m) for storage and processing
   *
   * @param inputWords Array of strings without duplicates
   * @return Map of concatenated words to their constituent parts
   */
  public Map<String, List<String>> findConcatenatedWordsWithParts(String[] inputWords) {
    if (inputWords == null || inputWords.length < 2) {
      return new HashMap<>();
    }

    Set<String> wordDictionary = new HashSet<>(Arrays.asList(inputWords));
    Map<String, List<String>> concatenatedWordsMap = new HashMap<>();

    for (String currentWord : inputWords) {
      List<String> wordParts = findWordParts(currentWord, wordDictionary);
      if (wordParts.size() > 1) {
        concatenatedWordsMap.put(currentWord, wordParts);
      }
    }

    return concatenatedWordsMap;
  }

  /**
   * Helper method to find constituent parts of a concatenated word.
   *
   * @param word Word to decompose
   * @param dictionary Available words for decomposition
   * @return List of constituent parts, empty if word cannot be decomposed
   */
  private List<String> findWordParts(String word, Set<String> dictionary) {
    int wordLength = word.length();
    List<String>[] dpParts = new List[wordLength + 1];
    dpParts[0] = new ArrayList<>();

    for (int endIndex = 1; endIndex <= wordLength; endIndex++) {
      for (int startIndex = 0; startIndex < endIndex; startIndex++) {
          if (dpParts[startIndex] == null) {
              continue;
          }

        String substring = word.substring(startIndex, endIndex);
        if (dictionary.contains(substring) && !substring.equals(word)) {
          if (dpParts[endIndex] == null) {
            dpParts[endIndex] = new ArrayList<>(dpParts[startIndex]);
            dpParts[endIndex].add(substring);
          }
        }
      }
    }

    return dpParts[wordLength] != null ? dpParts[wordLength] : new ArrayList<>();
  }
}
