package graphs;

import java.util.*;

/**
 * 127. Word Ladder
 *
 * Problem: Given two words (beginWord and endWord), and a dictionary's word list,
 * find the length of shortest transformation sequence from beginWord to endWord,
 * where only one letter can be changed at a time and each transformed word must exist in the word list.
 *
 * Example:
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: "hit" -> "hot" -> "dot" -> "dog" -> "cog"
 *
 * LeetCode: https://leetcode.com/problems/word-ladder
 *
 * Follow-up questions:
 * Q: What if we need to find all shortest transformation sequences?
 * A: Use BFS to find shortest length, then DFS to enumerate all paths.
 *
 * Q: How to optimize for very large dictionaries?
 * A: Use trie structures or advanced string matching algorithms.
 *
 * Q: Can we handle different transformation rules (e.g., insertions/deletions)?
 * A: Extend distance function to support edit distance operations.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class WordLadder {

    /**
     * BFS approach - optimal for shortest path.
     *
     * Algorithm: Breadth-First Search
     * - Treat each word as a graph node
     * - Two words are connected if they differ by exactly one character
     * - Use BFS to find shortest path from beginWord to endWord
     * - Use visited set to avoid revisiting words
     *
     * Time Complexity: O(M²×N) where M is word length, N is word count
     * Space Complexity: O(M×N) for queue and visited set
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                if (currentWord.equals(endWord)) {
                    return level;
                }

                // Try all possible one-character changes
                for (String neighbor : getNeighbors(currentWord, wordSet)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            level++;
        }

        return 0; // No transformation possible
    }

    // Get all valid neighbors (words differing by one character)
    private List<String> getNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];

            // Try all 26 possible characters
            for (char c = 'a'; c <= 'z'; c++) {
                if (c != originalChar) {
                    chars[i] = c;
                    String newWord = new String(chars);

                    if (wordSet.contains(newWord)) {
                        neighbors.add(newWord);
                    }
                }
            }

            chars[i] = originalChar; // Restore original character
        }

        return neighbors;
    }

    /**
     * Bidirectional BFS for better performance.
     * Searches from both ends to reduce search space.
     */
    public int ladderLengthBidirectional(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();

        beginSet.add(beginWord);
        endSet.add(endWord);
        int level = 1;

        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // Always expand the smaller set
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }

            Set<String> nextBeginSet = new HashSet<>();

            for (String word : beginSet) {
                char[] chars = word.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    char originalChar = chars[i];

                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[i] = c;
                        String newWord = new String(chars);

                        if (endSet.contains(newWord)) {
                            return level + 1;
                        }

                        if (!visited.contains(newWord) && wordSet.contains(newWord)) {
                            visited.add(newWord);
                            nextBeginSet.add(newWord);
                        }
                    }

                    chars[i] = originalChar;
                }
            }

            beginSet = nextBeginSet;
            level++;
        }

        return 0;
    }

    /**
     * Graph preprocessing approach.
     * Precomputes adjacency list for faster neighbor lookup.
     */
    public int ladderLengthPreprocessed(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        // Add beginWord to wordList if not present
        List<String> allWords = new ArrayList<>(wordList);
        if (!allWords.contains(beginWord)) {
            allWords.add(beginWord);
        }

        // Build adjacency list
        Map<String, List<String>> graph = buildGraph(allWords);

        // BFS
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                if (currentWord.equals(endWord)) {
                    return level;
                }

                for (String neighbor : graph.getOrDefault(currentWord, new ArrayList<>())) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            level++;
        }

        return 0;
    }

    // Build adjacency list representation of word graph
    private Map<String, List<String>> buildGraph(List<String> words) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String word : words) {
            graph.put(word, new ArrayList<>());
        }

        // Connect words that differ by one character
        for (int i = 0; i < words.size(); i++) {
            for (int j = i + 1; j < words.size(); j++) {
                String word1 = words.get(i);
                String word2 = words.get(j);

                if (isOneCharDifferent(word1, word2)) {
                    graph.get(word1).add(word2);
                    graph.get(word2).add(word1);
                }
            }
        }

        return graph;
    }

    // Check if two words differ by exactly one character
    private boolean isOneCharDifferent(String word1, String word2) {
        if (word1.length() != word2.length()) return false;

        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;
                if (diffCount > 1) return false;
            }
        }

        return diffCount == 1;
    }

    /**
     * Pattern-based optimization using wildcards.
     * Groups words by patterns to reduce comparison overhead.
     */
    public int ladderLengthPatternBased(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        // Build pattern to words mapping
        Map<String, List<String>> patternDict = new HashMap<>();
        int wordLength = beginWord.length();

        List<String> allWords = new ArrayList<>(wordList);
        if (!allWords.contains(beginWord)) {
            allWords.add(beginWord);
        }

        for (String word : allWords) {
            for (int i = 0; i < wordLength; i++) {
                String pattern = word.substring(0, i) + "*" + word.substring(i + 1);
                patternDict.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
            }
        }

        // BFS using patterns
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                // Check all patterns for current word
                for (int j = 0; j < wordLength; j++) {
                    String pattern = currentWord.substring(0, j) + "*" + currentWord.substring(j + 1);

                    for (String neighbor : patternDict.getOrDefault(pattern, new ArrayList<>())) {
                        if (neighbor.equals(endWord)) {
                            return level + 1;
                        }

                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.offer(neighbor);
                        }
                    }
                }
            }

            level++;
        }

        return 0;
    }

    /**
     * A* search approach with heuristic.
     * Uses edit distance as heuristic for guided search.
     */
    public int ladderLengthAStar(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        PriorityQueue<AStarNode> pq = new PriorityQueue<>((a, b) ->
            Integer.compare(a.fScore, b.fScore));
        Map<String, Integer> gScore = new HashMap<>();

        pq.offer(new AStarNode(beginWord, 0, heuristic(beginWord, endWord)));
        gScore.put(beginWord, 0);

        while (!pq.isEmpty()) {
            AStarNode current = pq.poll();

            if (current.word.equals(endWord)) {
                return current.gScore + 1;
            }

            for (String neighbor : getNeighbors(current.word, wordSet)) {
                int tentativeGScore = current.gScore + 1;

                if (tentativeGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    gScore.put(neighbor, tentativeGScore);
                    int fScore = tentativeGScore + heuristic(neighbor, endWord);
                    pq.offer(new AStarNode(neighbor, tentativeGScore, fScore));
                }
            }
        }

        return 0;
    }

    // A* node structure
    private static class AStarNode {
        String word;
        int gScore; // Actual distance from start
        int fScore; // gScore + heuristic

        AStarNode(String word, int gScore, int fScore) {
            this.word = word;
            this.gScore = gScore;
            this.fScore = fScore;
        }
    }

    // Heuristic function: number of different characters
    private int heuristic(String word, String target) {
        int diff = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != target.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }

    /**
     * Returns all shortest transformation sequences.
     * Extension that finds all optimal paths.
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> result = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(wordList);

        if (!wordSet.contains(endWord)) return result;

        // BFS to find shortest length and build parent mapping
        Queue<String> queue = new LinkedList<>();
        Map<String, List<String>> parents = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        boolean found = false;

        while (!queue.isEmpty() && !found) {
            int size = queue.size();
            Set<String> currentLevelVisited = new HashSet<>();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                for (String neighbor : getNeighbors(currentWord, wordSet)) {
                    if (neighbor.equals(endWord)) {
                        found = true;
                    }

                    if (!visited.contains(neighbor)) {
                        if (!currentLevelVisited.contains(neighbor)) {
                            currentLevelVisited.add(neighbor);
                            queue.offer(neighbor);
                        }

                        parents.computeIfAbsent(neighbor, k -> new ArrayList<>()).add(currentWord);
                    }
                }
            }

            visited.addAll(currentLevelVisited);
        }

        // DFS to reconstruct all paths
        if (found) {
            List<String> path = new ArrayList<>();
            dfsPath(endWord, beginWord, parents, path, result);
        }

        return result;
    }

    // DFS to build all paths from endWord back to beginWord
    private void dfsPath(String word, String beginWord, Map<String, List<String>> parents,
                        List<String> path, List<List<String>> result) {
        path.add(word);

        if (word.equals(beginWord)) {
            List<String> validPath = new ArrayList<>(path);
            Collections.reverse(validPath);
            result.add(validPath);
        } else {
            for (String parent : parents.getOrDefault(word, new ArrayList<>())) {
                dfsPath(parent, beginWord, parents, path, result);
            }
        }

        path.remove(path.size() - 1);
    }

    /**
     * Memory-optimized approach for large word lists.
     * Uses trie structure for efficient prefix matching.
     */
    public int ladderLengthTrie(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) return 0;

        Trie trie = new Trie();
        for (String word : wordList) {
            trie.insert(word);
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                String currentWord = queue.poll();

                if (currentWord.equals(endWord)) {
                    return level;
                }

                // Find neighbors using trie
                List<String> neighbors = trie.findOneEditWords(currentWord);
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }

            level++;
        }

        return 0;
    }

    // Simplified trie for word storage and neighbor finding
    private static class Trie {
        TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new TrieNode();
                }
                node = node.children[c - 'a'];
            }
            node.isEnd = true;
            node.word = word;
        }

        List<String> findOneEditWords(String word) {
            List<String> result = new ArrayList<>();
            char[] chars = word.toCharArray();

            // Try substitutions
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c != original) {
                        chars[i] = c;
                        String newWord = new String(chars);
                        if (search(newWord)) {
                            result.add(newWord);
                        }
                    }
                }
                chars[i] = original;
            }

            return result;
        }

        boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    return false;
                }
                node = node.children[c - 'a'];
            }
            return node.isEnd;
        }

        private static class TrieNode {
            TrieNode[] children = new TrieNode[26];
            boolean isEnd = false;
            String word = null;
        }
    }

    /**
     * Parallel processing approach for very large dictionaries.
     * Uses multiple threads to explore different branches concurrently.
     */
    public int ladderLengthParallel(String beginWord, String endWord, List<String> wordList) {
        if (wordList.size() < 10000) {
            return ladderLength(beginWord, endWord, wordList); // Use sequential for small lists
        }

        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return 0;

        // Use concurrent data structures
        Queue<String> queue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        Set<String> visited = java.util.concurrent.ConcurrentHashMap.newKeySet();

        queue.offer(beginWord);
        visited.add(beginWord);
        int level = 1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<String> currentLevel = new ArrayList<>();

            // Collect current level
            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                if (word != null) {
                    currentLevel.add(word);
                }
            }

            // Process level in parallel
            List<String> nextLevel = currentLevel.parallelStream()
                .flatMap(word -> getNeighbors(word, wordSet).stream())
                .filter(neighbor -> !visited.contains(neighbor))
                .collect(java.util.stream.Collectors.toList());

            // Check if endWord found
            if (nextLevel.contains(endWord)) {
                return level + 1;
            }

            // Add to queue and visited
            for (String word : nextLevel) {
                if (visited.add(word)) {
                    queue.offer(word);
                }
            }

            level++;
        }

        return 0;
    }
}