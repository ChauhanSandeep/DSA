package trie;

/**
 * Problem: Design Add and Search Words Data Structure
 *
 * Build a word dictionary that supports adding lowercase words and searching
 * patterns. A dot in the search pattern matches exactly one lowercase letter,
 * so the search may need to branch when the next character is unknown.
 *
 * Leetcode: https://leetcode.com/problems/design-add-and-search-words-data-structure/ (Medium)
 * Rating:   acceptance 48.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Trie | Wildcard DFS | End-of-word matching
 *
 * Example:
 *   Input:  addWord("bad"), addWord("dad"), addWord("mad"), search("pad"), search(".ad"), search("b..")
 *   Output: null, null, null, false, true, true
 *   Why:    "pad" has no p-edge from the root, ".ad" can branch to b/d/m, and "b.."
 *           follows the b-edge while the dots fill the remaining two letters of "bad".
 *
 * Follow-ups:
 *   1. Return every word that matches a dotted pattern?
 *      Store the full word at terminal nodes and collect all successful DFS branches.
 *   2. Let '.' match zero or more characters instead of exactly one?
 *      Add memoized pattern matching over (trie node, pattern index), like regex matching.
 *   3. Support delete(word) safely?
 *      Unmark the terminal node, then prune unused child nodes while recursion unwinds.
 *   4. Reduce memory for sparse alphabets?
 *      Replace the fixed child array with a map or compress single-child paths.
 *
 * Related: Implement Trie (Prefix Tree) (208), Word Search II (212), Regular Expression Matching (10).
 */
public class DesignAddAndSearchWordsDataStructure {

    public static void main(String[] args) {
        WordDictionary emptyDictionary = new WordDictionary();
        System.out.printf("search(\"a\") on empty -> %s  expected=false%n", emptyDictionary.search("a"));

        WordDictionary dictionary = new WordDictionary();
        dictionary.addWord("bad");
        System.out.printf("addWord(\"bad\") -> %s  expected=null%n", "null");
        dictionary.addWord("dad");
        System.out.printf("addWord(\"dad\") -> %s  expected=null%n", "null");
        dictionary.addWord("mad");
        System.out.printf("addWord(\"mad\") -> %s  expected=null%n", "null");

        System.out.printf("search(\"pad\") -> %s  expected=false%n", dictionary.search("pad"));
        System.out.printf("search(\"bad\") -> %s  expected=true%n", dictionary.search("bad"));
        System.out.printf("search(\".ad\") -> %s  expected=true%n", dictionary.search(".ad"));
        System.out.printf("search(\"b..\") -> %s  expected=true%n", dictionary.search("b.."));
        System.out.printf("search(\"..\") -> %s  expected=false%n", dictionary.search(".."));
    }

    /**
     * Approach 1: Trie with DFS and Recursion
     * 
     * Algorithm:
     * 1. Use a Trie (prefix tree) where each node has 26 children (for 'a'-'z')
     * 2. addWord: Insert characters sequentially, creating nodes as needed, mark end of word
     * 3. search: For regular characters, traverse normally. For '.', try all 26 possible branches using DFS
     * 
     * Key Insights:
     * - Trie provides O(L) insertion where L is word length
     * - For search without wildcards: O(L) time
     * - For search with wildcards: O(26^k * L) worst case, where k is number of dots
     * - The constraint of at most 2 dots limits worst-case to O(676 * L)
     * 
     * Wildcard Handling:
     * - When encountering '.', we cannot determine the path, so we explore all 26 possibilities
     * - Use recursive DFS to try each branch
     * - Return true if any branch leads to a valid match
     * 
     * Time Complexity:
     * - addWord: O(L) where L is word length
     * - search: O(26^k * L) where k is number of dots, L is word length
     *   With constraint of max 2 dots: O(676 * L) = O(L) for practical purposes
     * 
     * Space Complexity: O(N * L) where N is number of words, L is average word length
     * - Trie storage for all words
     * - Recursion stack: O(L) for search operations
     */
    static class WordDictionary {
        
        private TrieNode root;
        
        private static class TrieNode {
            TrieNode[] children;
            boolean isEndOfWord;
            
            public TrieNode() {
                children = new TrieNode[26];
                isEndOfWord = false;
            }
        }
        
        public WordDictionary() {
            root = new TrieNode();
        }
        
        /**
         * Adds a lowercase word so later searches can match it exactly or through dots.
         *
         * Time:  O(L) - one trie edge is visited per character.
         * Space: O(L) - a word with no shared prefix can add one node per character.
         *
         * @param word lowercase word to store
         */
        public void addWord(String word) {
            TrieNode current = root;
            
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (current.children[index] == null) {
                    current.children[index] = new TrieNode();
                }
                current = current.children[index];
            }
            
            current.isEndOfWord = true;
        }
        
        /**
         * Searches for a full-word match; each dot can stand for any one letter.
         *
         * Time:  O(26^D * L) - each of D dots may branch across child edges while fixed letters follow one path.
         * Space: O(L) - recursive search keeps one frame per pattern character.
         *
         * @param word lowercase pattern that may contain dots
         * @return true if an inserted word matches the entire pattern
         */
        public boolean search(String word) {
            return searchHelper(word, 0, root);
        }
        
        /** Recursively matches the remaining pattern from one trie node. */
        private boolean searchHelper(String word, int index, TrieNode node) {
            // Base case: reached end of word
            if (index == word.length()) {
                return node.isEndOfWord;
            }
            
            char c = word.charAt(index);
            
            if (c == '.') {
                // Wildcard: try all 26 possible children
                for (int i = 0; i < 26; i++) {
                    if (node.children[i] != null) {
                        if (searchHelper(word, index + 1, node.children[i])) {
                            return true;
                        }
                    }
                }
                return false;
            } else {
                // Regular character: follow the specific path
                int charIndex = c - 'a';
                if (node.children[charIndex] == null) {
                    return false;
                }
                return searchHelper(word, index + 1, node.children[charIndex]);
            }
        }
    }
}

