package dynamicprogramming.linearpartition;

import java.util.*;


/**
 * Problem: Word Break II
 *
 * Given a string and a dictionary, return every sentence formed by inserting
 * spaces so each token is a dictionary word. Dictionary words may be reused, and
 * every returned sentence must cover the full string.
 *
 * Leetcode: https://leetcode.com/problems/word-break-ii/ (Hard)
 * Rating:   acceptance 55.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Memoized DFS | Sentence generation
 *
 * Example:
 *   Input:  str = "catsanddog", wordDict = ["cat", "cats", "and", "sand", "dog"]
 *   Output: ["cat sand dog", "cats and dog"]
 *   Why:    those are the two full-string splits where every token is in the dictionary.
 *
 * Follow-ups:
 *   1. Count sentences without constructing them?
 *      Memoize counts per start index instead of lists of strings.
 *   2. Return only the shortest sentence?
 *      Store minimum word counts and reconstruct only optimal splits.
 *   3. How do you reduce substring overhead?
 *      Use a trie and scan characters from each start index.
 *
 * Related: Word Break (139), Concatenated Words (472).
 */
public class WordBreak2 {

  public static void main(String[] args) {
    WordBreak2 solver = new WordBreak2();
    String[] strings = { "catsanddog", "catsandog" };
    List<List<String>> dictionaries = Arrays.asList(
        Arrays.asList("cat", "cats", "and", "sand", "dog"),
        Arrays.asList("cats", "dog", "sand", "and", "cat"));
    String[] expected = { "[cat sand dog, cats and dog]", "[]" };

    for (int i = 0; i < strings.length; i++) {
      List<String> got = solver.wordBreakRecursiveApproach(strings[i], dictionaries.get(i));
      System.out.printf("str=%s wordDict=%s -> %s  expected=%s%n",
          strings[i], dictionaries.get(i), got, expected[i]);
    }
  }

    /**
   * Intuition: memo[start] stores every valid sentence that can be built from
   * the suffix str[start..]. For a fixed start, each dictionary prefix is a
   * possible first word. Every sentence returned by the recursive suffix call
   * can then be joined after that prefix, giving all sentences for this start.
   *
   * Algorithm:
   *   1. Convert wordDict to a set and start DFS from index 0.
   *   2. For each start index, try every end index as the next word boundary.
   *   3. Combine each valid prefix with every memoized suffix sentence and cache the list.
   *
   * Time:  O(n^2 + output) - cut pairs are explored and every produced sentence must be written.
   * Space: O(n + output) - memo stores sentence lists by start index.
   *
   * @param str string to segment into sentences
   * @param wordDict dictionary of reusable words
   * @return all valid sentences that cover str
   */
  public List<String> wordBreakRecursiveApproach(String str, List<String> wordDict) {
    Set<String> wordSet = new HashSet<>(wordDict);
    Map<Integer, List<String>> memo = new HashMap<>();
    return wordBreakRecursiveHelper(0, str, wordSet, memo);
  }

  private List<String> wordBreakRecursiveHelper(int start, String str, Set<String> wordSet, Map<Integer, List<String>> memo) {
    if (memo.containsKey(start)) {
      return memo.get(start);
    }

    List<String> result = new ArrayList<>();

    // Base case: if we've reached the end of the string
    if (start == str.length()) {
      result.add(""); // Add empty string to allow sentence concatenation
      return result;
    }

    for (int end = start + 1; end <= str.length(); end++) {
      String prefix = str.substring(start, end);

      if (wordSet.contains(prefix)) {
        List<String> suffixSentences = wordBreakRecursiveHelper(end, str, wordSet, memo);

        for (String sentence : suffixSentences) {
          // If sentence is empty, we don't need extra space
          if (sentence.isEmpty()) {
            result.add(prefix);
          } else {
            result.add(prefix + " " + sentence);
          }
        }
      }
    }

    memo.put(start, result);
    return result;
  }
}