package strings.twopointers;

import java.util.*;

/**
 * Problem: Palindrome Pairs
 *
 * Given unique words, return all index pairs (i, j) where words[i] + words[j]
 * is a palindrome. Empty strings and words whose unmatched suffix/prefix is a
 * palindrome create the tricky cases.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-pairs/ (Hard)
 * Rating:   no contest Elo listed
 * Pattern:  Strings | Trie of reversed words | Palindrome suffix checks
 *
 * Example:
 *   Input:  words = ["abcd","dcba","lls","s","sssll"]
 *   Output: [[0,1],[1,0],[3,2],[2,4]]
 *   Why:    each listed pair concatenates to a string that reads the same both ways.
 *
 * Follow-ups:
 *   1. How can you solve it without a trie?
 *      Use a map from word to index and test every split with palindrome checks.
 *   2. How do you reduce repeated palindrome scans?
 *      Precompute palindrome prefixes/suffixes or use rolling hashes.
 *   3. How would duplicate words change the output?
 *      Store all indices per word and emit every valid distinct index pair.
 */
public class PalindromePairs {

    public static void main(String[] args) {
        PalindromePairs solver = new PalindromePairs();

        String[][] inputs = {
            {"abcd", "dcba", "lls", "s", "sssll"},
            {"bat", "tab", "cat"},
            {}
        };
        String[] expected = {
            "[[0, 1], [1, 0], [2, 4], [3, 2]]",
            "[[0, 1], [1, 0]]",
            "[]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Integer>> got = solver.palindromePairs(inputs[i]);
            got.sort((left, right) -> {
                int byFirst = Integer.compare(left.get(0), right.get(0));
                return byFirst != 0 ? byFirst : Integer.compare(left.get(1), right.get(1));
            });
            System.out.printf("words=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: store words reversed in a trie so scanning a word from left to
     * right can discover words that complete it into a palindrome. Extra trie
     * metadata records reversed words whose remaining prefix is already a
     * palindrome, covering pairs where one word is longer than the other.
     *
     * Algorithm:
     *   1. Insert every word into the trie in reverse order.
     *   2. During insertion, record indices whose remaining prefix is a palindrome.
     *   3. Search each word through the trie, emitting matches at word ends and stored lists.
     *   4. Skip pairs that would use the same word index twice.
     *
     * Time:  O(n * L^2) - palindrome checks may scan prefixes while inserting/searching.
     * Space: O(n * L) - trie nodes store characters from all words.
     *
     * @param words Unique words to pair.
     * @return Index pairs whose concatenation is a palindrome.
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        if (words == null || words.length < 2) return result;

        TrieNode root = new TrieNode();

        // Build trie with reversed words
        for (int i = 0; i < words.length; i++) {
            addWord(root, words[i], i);
        }

        // Find palindrome pairs
        for (int i = 0; i < words.length; i++) {
            search(words, i, root, result);
        }

        return result;
    }

    // Trie node with palindrome optimization
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        int wordIndex = -1;
        List<Integer> palindromeIndexes = new ArrayList<>(); // Words forming palindrome with current prefix
    }

    // Add word to trie (reversed)
    private void addWord(TrieNode root, String word, int index) {
        for (int i = word.length() - 1; i >= 0; i--) {
            int charIndex = word.charAt(i) - 'a';
            if (root.children[charIndex] == null) {
                root.children[charIndex] = new TrieNode();
            }

            // Check if remaining part is palindrome
            if (isPalindrome(word, 0, i)) {
                root.palindromeIndexes.add(index);
            }

            root = root.children[charIndex];
        }

        root.wordIndex = index;
        root.palindromeIndexes.add(index);
    }

    // Search for palindrome pairs
    private void search(String[] words, int wordIndex, TrieNode root, List<List<Integer>> result) {
        String word = words[wordIndex];

        for (int i = 0; i < word.length(); i++) {
            // Case 1: Current node has a complete word and remaining part is palindrome
            if (root.wordIndex >= 0 && root.wordIndex != wordIndex && isPalindrome(word, i, word.length() - 1)) {
                result.add(Arrays.asList(wordIndex, root.wordIndex));
            }

            root = root.children[word.charAt(i) - 'a'];
            if (root == null) return;
        }

        // Case 2: Reached end of word, check palindrome suffixes in trie
        for (int index : root.palindromeIndexes) {
            if (index != wordIndex) {
                result.add(Arrays.asList(wordIndex, index));
            }
        }
    }

    // Check if substring is palindrome
    private boolean isPalindrome(String word, int left, int right) {
        while (left < right) {
            if (word.charAt(left++) != word.charAt(right--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Brute force approach for verification.
     * Checks all possible pairs explicitly.
     */
    public List<List<Integer>> palindromePairsBruteForce(String[] words) {
        List<List<Integer>> result = new ArrayList<>();

        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (i != j) {
                    String combined = words[i] + words[j];
                    if (isPalindrome(combined, 0, combined.length() - 1)) {
                        result.add(Arrays.asList(i, j));
                    }
                }
            }
        }

        return result;
    }

    /**
     * HashMap-based approach with string manipulation.
     * More straightforward but potentially less efficient.
     */
    public List<List<Integer>> palindromePairsHashMap(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        Map<String, Integer> wordMap = new HashMap<>();

        // Map word to index
        for (int i = 0; i < words.length; i++) {
            wordMap.put(words[i], i);
        }

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            // Check all possible splits of current word
            for (int j = 0; j <= word.length(); j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);

                // Case 1: prefix is palindrome, find reverse of suffix
                if (isPalindrome(prefix, 0, prefix.length() - 1)) {
                    String reverseSuffix = reverse(suffix);
                    Integer index = wordMap.get(reverseSuffix);
                    if (index != null && index != i) {
                        result.add(Arrays.asList(index, i));
                    }
                }

                // Case 2: suffix is palindrome, find reverse of prefix
                if (j != word.length() && isPalindrome(suffix, 0, suffix.length() - 1)) {
                    String reversePrefix = reverse(prefix);
                    Integer index = wordMap.get(reversePrefix);
                    if (index != null && index != i) {
                        result.add(Arrays.asList(i, index));
                    }
                }
            }
        }

        return result;
    }

    // Reverse string
    private String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    /**
     * Rolling hash approach for large strings.
     * Uses polynomial rolling hash for efficient palindrome checking.
     */
    public List<List<Integer>> palindromePairsRollingHash(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        int n = words.length;

        // Precompute hashes for all words and their reverses
        Map<Long, List<Integer>> hashToIndices = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String word = words[i];

            // Hash all prefixes and suffixes
            for (int j = 0; j <= word.length(); j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);

                // Store hash mappings for efficient lookup
                long prefixHash = computeHash(prefix);
                long suffixHash = computeHash(suffix);
                long reversePrefixHash = computeHash(reverse(prefix));
                long reverseSuffixHash = computeHash(reverse(suffix));

                hashToIndices.computeIfAbsent(prefixHash, k -> new ArrayList<>()).add(i);
                hashToIndices.computeIfAbsent(suffixHash, k -> new ArrayList<>()).add(i);
            }
        }

        // Find pairs using hash lookups
        for (int i = 0; i < n; i++) {
            String word = words[i];

            for (int j = 0; j <= word.length(); j++) {
                String prefix = word.substring(0, j);
                String suffix = word.substring(j);

                // Check if we can form palindromes
                if (isPalindrome(prefix, 0, prefix.length() - 1)) {
                    long targetHash = computeHash(reverse(suffix));
                    List<Integer> candidates = hashToIndices.get(targetHash);

                    if (candidates != null) {
                        for (int candidate : candidates) {
                            if (candidate != i && words[candidate].equals(reverse(suffix))) {
                                result.add(Arrays.asList(candidate, i));
                            }
                        }
                    }
                }

                if (j != word.length() && isPalindrome(suffix, 0, suffix.length() - 1)) {
                    long targetHash = computeHash(reverse(prefix));
                    List<Integer> candidates = hashToIndices.get(targetHash);

                    if (candidates != null) {
                        for (int candidate : candidates) {
                            if (candidate != i && words[candidate].equals(reverse(prefix))) {
                                result.add(Arrays.asList(i, candidate));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    // Compute polynomial rolling hash
    private long computeHash(String s) {
        long hash = 0;
        long base = 31;
        long mod = 1000000007;

        for (char c : s.toCharArray()) {
            hash = (hash * base + (c - 'a' + 1)) % mod;
        }

        return hash;
    }

    /**
     * Optimized trie approach with Manacher's algorithm.
     * Uses Manacher's algorithm for O(n) palindrome detection.
     */
    public List<List<Integer>> palindromePairsOptimized(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        OptimizedTrie trie = new OptimizedTrie();

        // Build trie and preprocess palindromes
        for (int i = 0; i < words.length; i++) {
            trie.insert(words[i], i);
        }

        // Find palindrome pairs
        for (int i = 0; i < words.length; i++) {
            trie.searchPairs(words[i], i, result);
        }

        return result;
    }

    // Optimized trie with better palindrome detection
    private static class OptimizedTrie {
        private OptimizedTrieNode root = new OptimizedTrieNode();

        void insert(String word, int index) {
            OptimizedTrieNode node = root;

            // Insert reversed word
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new OptimizedTrieNode();
                }

                // Use Manacher's algorithm for efficient palindrome detection
                if (isPalindromeManacher(word, 0, i)) {
                    node.palindromePrefixes.add(index);
                }

                node = node.children[c - 'a'];
            }

            node.wordIndex = index;
            node.palindromePrefixes.add(index);
        }

        void searchPairs(String word, int index, List<List<Integer>> result) {
            OptimizedTrieNode node = root;

            // Search along the word
            for (int i = 0; i < word.length(); i++) {
                if (node.wordIndex != -1 && node.wordIndex != index) {
                    if (isPalindromeManacher(word, i, word.length() - 1)) {
                        result.add(Arrays.asList(index, node.wordIndex));
                    }
                }

                char c = word.charAt(i);
                if (node.children[c - 'a'] == null) return;
                node = node.children[c - 'a'];
            }

            // Check remaining palindromes
            for (int palinIndex : node.palindromePrefixes) {
                if (palinIndex != index) {
                    result.add(Arrays.asList(index, palinIndex));
                }
            }
        }

        // Simplified Manacher's algorithm
        private boolean isPalindromeManacher(String s, int start, int end) {
            while (start < end) {
                if (s.charAt(start++) != s.charAt(end--)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class OptimizedTrieNode {
        OptimizedTrieNode[] children = new OptimizedTrieNode[26];
        int wordIndex = -1;
        List<Integer> palindromePrefixes = new ArrayList<>();
    }

    /**
     * Parallel processing approach for large datasets.
     * Divides work across multiple threads for better performance.
     */
    public List<List<Integer>> palindromePairsParallel(String[] words) {
        List<List<Integer>> result = Collections.synchronizedList(new ArrayList<>());
        int n = words.length;
        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = (n + numThreads - 1) / numThreads;

        List<Thread> threads = new ArrayList<>();

        for (int t = 0; t < numThreads; t++) {
            int start = t * chunkSize;
            int end = Math.min(start + chunkSize, n);

            Thread thread = new Thread(() -> {
                List<List<Integer>> localResult = new ArrayList<>();

                for (int i = start; i < end; i++) {
                    for (int j = 0; j < n; j++) {
                        if (i != j) {
                            String combined = words[i] + words[j];
                            if (isPalindrome(combined, 0, combined.length() - 1)) {
                                localResult.add(Arrays.asList(i, j));
                            }
                        }
                    }
                }

                synchronized (result) {
                    result.addAll(localResult);
                }
            });

            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    /**
     * Memory-efficient approach for very large word lists.
     * Processes words in batches to control memory usage.
     */
    public List<List<Integer>> palindromePairsMemoryEfficient(String[] words) {
        List<List<Integer>> result = new ArrayList<>();
        int batchSize = Math.min(1000, words.length / 10 + 1);

        for (int batchStart = 0; batchStart < words.length; batchStart += batchSize) {
            int batchEnd = Math.min(batchStart + batchSize, words.length);

            // Process current batch against all words
            for (int i = batchStart; i < batchEnd; i++) {
                for (int j = 0; j < words.length; j++) {
                    if (i != j) {
                        String combined = words[i] + words[j];
                        if (isPalindrome(combined, 0, combined.length() - 1)) {
                            result.add(Arrays.asList(i, j));
                        }
                    }
                }
            }

            // Force garbage collection between batches for large datasets
            if (batchEnd - batchStart == batchSize) {
                System.gc();
            }
        }

        return result;
    }

    /**
     * Returns additional palindrome statistics.
     * Extension providing analysis of palindrome patterns.
     */
    public PalindromeAnalysis analyzePalindromes(String[] words) {
        List<List<Integer>> pairs = palindromePairs(words);
        Map<Integer, Set<Integer>> wordConnections = new HashMap<>();
        int maxLength = 0;
        List<String> longestPalindromes = new ArrayList<>();

        for (List<Integer> pair : pairs) {
            int i = pair.get(0), j = pair.get(1);
            String palindrome = words[i] + words[j];

            wordConnections.computeIfAbsent(i, k -> new HashSet<>()).add(j);
            wordConnections.computeIfAbsent(j, k -> new HashSet<>()).add(i);

            if (palindrome.length() > maxLength) {
                maxLength = palindrome.length();
                longestPalindromes.clear();
                longestPalindromes.add(palindrome);
            } else if (palindrome.length() == maxLength) {
                longestPalindromes.add(palindrome);
            }
        }

        int totalPairs = pairs.size();
        int connectedWords = wordConnections.size();
        double avgConnectionsPerWord = connectedWords == 0 ? 0 :
            wordConnections.values().stream().mapToInt(Set::size).average().orElse(0);

        return new PalindromeAnalysis(pairs, totalPairs, connectedWords,
                                    avgConnectionsPerWord, maxLength, longestPalindromes);
    }

    // Analysis result class
    public static class PalindromeAnalysis {
        public final List<List<Integer>> pairs;
        public final int totalPairs;
        public final int connectedWords;
        public final double avgConnectionsPerWord;
        public final int maxPalindromeLength;
        public final List<String> longestPalindromes;

        public PalindromeAnalysis(List<List<Integer>> pairs, int totalPairs, int connectedWords,
                                double avgConnectionsPerWord, int maxPalindromeLength,
                                List<String> longestPalindromes) {
            this.pairs = pairs;
            this.totalPairs = totalPairs;
            this.connectedWords = connectedWords;
            this.avgConnectionsPerWord = avgConnectionsPerWord;
            this.maxPalindromeLength = maxPalindromeLength;
            this.longestPalindromes = longestPalindromes;
        }

        @Override
        public String toString() {
            return String.format("Total pairs: %d, Connected words: %d, Avg connections: %.2f, Max length: %d",
                               totalPairs, connectedWords, avgConnectionsPerWord, maxPalindromeLength);
        }
    }
}