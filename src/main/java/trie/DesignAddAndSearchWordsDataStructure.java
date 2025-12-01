package trie;

/**
 * Problem: Design Add and Search Words Data Structure
 * 
 * Design a data structure that supports adding new words and finding if a string 
 * matches any previously added string.
 * 
 * Implement the WordDictionary class:
 * - WordDictionary() Initializes the object.
 * - void addWord(word) Adds word to the data structure, it can be matched later.
 * - boolean search(word) Returns true if there is any string in the data structure 
 *   that matches word or false otherwise. word may contain dots '.' where dots can 
 *   be matched with any letter.
 * 
 * Example:
 * Input:
 * ["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
 * [[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]
 * 
 * Output:
 * [null,null,null,null,false,true,true,true]
 * 
 * Explanation:
 * WordDictionary wordDictionary = new WordDictionary();
 * wordDictionary.addWord("bad");
 * wordDictionary.addWord("dad");
 * wordDictionary.addWord("mad");
 * wordDictionary.search("pad"); // return False (no match)
 * wordDictionary.search("bad"); // return True (exact match)
 * wordDictionary.search(".ad"); // return True (matches "bad", "dad", "mad")
 * wordDictionary.search("b.."); // return True (matches "bad")
 * 
 * The pattern ".ad" matches because '.' can represent any character, so it matches 
 * 'b' in "bad", 'd' in "dad", and 'm' in "mad".
 * 
 * Constraints:
 * - 1 <= word.length <= 25
 * - word in addWord consists of lowercase English letters.
 * - word in search consist of '.' or lowercase English letters.
 * - There will be at most 2 dots in word for search queries.
 * - At most 10^4 calls will be made to addWord and search.
 * 
 * LeetCode: https://leetcode.com/problems/design-add-and-search-words-data-structure/
 * 
 * Follow-up Questions:
 * 
 * 1. Q: How would you modify this to support prefix matching (e.g., "app*" matches "apple", "application")?
 *    A: Add a method that searches up to the wildcard position, then returns true if any valid 
 *    path exists from that node (i.e., check if the subtree is non-empty). This is simpler than 
 *    full wildcard matching since you only need to verify the prefix exists.
 *    Related: LeetCode 208 - Implement Trie (https://leetcode.com/problems/implement-trie-prefix-tree/)
 * 
 * 2. Q: What if wildcard '.' could match zero or more characters instead of exactly one?
 *    A: This becomes regex pattern matching with '.*' patterns. Use dynamic programming or 
 *    backtracking with memoization. For each '.*' sequence, try matching 0, 1, 2, ... characters 
 *    from the current position.
 *    Related: LeetCode 10 - Regular Expression Matching (https://leetcode.com/problems/regular-expression-matching/)
 * 
 * 3. Q: How would you handle case-insensitive search?
 *    A: Convert all input to lowercase during addWord and search operations. Alternatively, 
 *    expand the Trie nodes to have 52 children (26 lowercase + 26 uppercase) to preserve case 
 *    information while searching case-insensitively.
 * 
 * 4. Q: How would you optimize memory usage if you have millions of words?
 *    A: Use a HashMap instead of fixed-size array for children nodes to avoid allocating 26 slots 
 *    per node. Implement node compression (Patricia Trie) to merge chains of single-child nodes. 
 *    Use bit manipulation to store common prefixes more efficiently.
 * 
 * 5. Q: How would you modify this to return all matching words instead of just boolean?
 *    A: Store the complete word at each end node. During DFS search with wildcards, collect all 
 *    words where is_end is true. Return the collected list instead of a boolean.
 *    Related: LeetCode 212 - Word Search II (https://leetcode.com/problems/word-search-ii/)
 */
public class DesignAddAndSearchWordsDataStructure {

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
         * Adds a word into the data structure.
         * 
         * Algorithm:
         * 1. Start from root node
         * 2. For each character, compute its index (0-25)
         * 3. Create child node if it doesn't exist
         * 4. Move to child node
         * 5. Mark the last node as end of word
         * 
         * Time Complexity: O(L) where L is word length
         * Space Complexity: O(L) in worst case when all characters are new
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
         * Returns if the word is in the data structure. A word could contain the dot 
         * character '.' to represent any one letter.
         * 
         * Algorithm:
         * 1. Start DFS from root with full word
         * 2. For regular character: traverse to corresponding child
         * 3. For '.': recursively try all 26 possible children
         * 4. When word is fully processed, check if current node marks end of word
         * 
         * Time Complexity: O(26^k * L) where k is number of dots
         * Space Complexity: O(L) for recursion stack
         */
        public boolean search(String word) {
            return searchHelper(word, 0, root);
        }
        
        // Helper method for recursive search with wildcard support
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

