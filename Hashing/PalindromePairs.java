package Hashing;

import java.util.*;

public class PalindromePairs {
    public static void main(String[] args) {
        String[] words = {"abcd", "dcba", "lls", "s", "sssll"};
        System.out.println(new PalindromePairs().palindromePairs(words));
    }

    /**
     * Finds all pairs of indices (i, j) where words[i] + words[j] form a palindrome.
     * @param words - array of words
     * @return list of palindrome index pairs
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        // Map words to their indices for quick lookup
        Map<String, Integer> wordIndexMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordIndexMap.put(words[i], i);
        }

        List<List<Integer>> result = new ArrayList<>();

        for (String word : wordIndexMap.keySet()) {
            int index = wordIndexMap.get(word);
            String reversedWord = new StringBuilder(word).reverse().toString();

            // Case 1: Exact reverse match (word1 == reverse(word2))
            Integer reversedIndex = wordIndexMap.get(reversedWord);
            if (reversedIndex != null && reversedIndex != index) {
                result.add(Arrays.asList(index, reversedIndex));
            }

            // Case 2: Prefix is palindrome, suffix has reverse match
            for (String suffix : allValidSuffixes(word)) {
                Integer suffixIndex = wordIndexMap.get(new StringBuilder(suffix).reverse().toString());
                if (suffixIndex != null) {
                    result.add(Arrays.asList(suffixIndex, index));
                }
            }

            // Case 3: Suffix is palindrome, prefix has reverse match
            for (String prefix : allValidPrefixes(word)) {
                Integer prefixIndex = wordIndexMap.get(new StringBuilder(prefix).reverse().toString());
                if (prefixIndex != null) {
                    result.add(Arrays.asList(index, prefixIndex));
                }
            }
        }
        return result;
    }

    /**
     * Returns a list of prefixes where the remaining suffix is a palindrome.
     * @param word - input string
     * @return list of valid prefixes
     */
    private List<String> allValidPrefixes(String word) {
        List<String> validPrefixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindrome(word, i, word.length() - 1)) {
                validPrefixes.add(word.substring(0, i));
            }
        }
        return validPrefixes;
    }

    /**
     * Returns a list of suffixes where the remaining prefix is a palindrome.
     * @param word - input string
     * @return list of valid suffixes
     */
    private List<String> allValidSuffixes(String word) {
        List<String> validSuffixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindrome(word, 0, i)) {
                validSuffixes.add(word.substring(i + 1)); // No need to specify the end index explicitly
            }
        }
        return validSuffixes;
    }

    /**
     * Checks if a substring of a given word is a palindrome.
     * @param word - input string
     * @param left - start index
     * @param right - end index
     * @return true if the substring is a palindrome, false otherwise
     */
    private boolean isPalindrome(String word, int left, int right) {
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}