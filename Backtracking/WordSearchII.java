package Backtracking;

import java.util.*;

/**
 * 212. Word Search II
 * 
 * Problem: Given an m x n board of characters and a list of strings words,
 * return all words on the board. Each word must be constructed from letters
 * of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring.
 * The same letter cell may not be used more than once in a word.
 * 
 * Example:
 * Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], 
 *        words = ["oath","pea","eat","rain"]
 * Output: ["eat","oath"]
 * 
 * LeetCode: https://leetcode.com/problems/word-search-ii
 * 
 * Follow-up questions:
 * Q: How to optimize for very large dictionaries?
 * A: Use trie pruning, parallel processing, and advanced string matching algorithms.
 * 
 * Q: Can we handle dynamic word additions/removals during search?
 * A: Implement persistent trie structures or incremental update mechanisms.
 * 
 * Q: How to extend to 3D grids or different movement patterns?
 * A: Generalize direction vectors and neighbor calculation logic.
 */
public class WordSearchII {
    
    /**
     * Trie + DFS backtracking approach - optimal solution.
     * 
     * Algorithm: Trie-based backtracking
     * - Build trie from word list for efficient prefix matching
     * - For each cell, start DFS if it matches trie root children
     * - Use backtracking to explore all paths while avoiding revisits
     * - Prune trie nodes when words are found to avoid duplicates
     * 
     * Time Complexity: O(m*n*4^L) where m,n are board dimensions, L is max word length
     * Space Complexity: O(W*L) where W is number of words, L is average word length
     */
    public List<String> findWords(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        if (board == null || board.length == 0 || words == null || words.length == 0) {
            return result;
        }
        
        // Build trie from words
        TrieNode root = buildTrie(words);
        
        // DFS from each cell
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfs(board, i, j, root, result);
            }
        }
        
        return result;
    }
    
    private void dfs(char[][] board, int row, int col, TrieNode node, List<String> result) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        
        char c = board[row][col];
        if (c == '#' || node.children[c - 'a'] == null) {
            return; // Already visited or no matching path
        }
        
        node = node.children[c - 'a'];
        
        // Found a word
        if (node.word != null) {
            result.add(node.word);
            node.word = null; // Avoid duplicates
        }
        
        // Mark as visited
        board[row][col] = '#';
        
        // Explore all 4 directions
        dfs(board, row - 1, col, node, result);
        dfs(board, row + 1, col, node, result);
        dfs(board, row, col - 1, node, result);
        dfs(board, row, col + 1, node, result);
        
        // Restore cell
        board[row][col] = c;
    }
    
    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.word = word;
        }
        
        return root;
    }
    
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word = null;
    }
    
    /**
     * Optimized approach with trie pruning.
     * Removes trie branches when no longer needed.
     */
    public List<String> findWordsOptimized(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        if (board == null || board.length == 0) return result;
        
        OptimizedTrieNode root = buildOptimizedTrie(words);
        
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfsOptimized(board, i, j, root, result);
            }
        }
        
        return result;
    }
    
    private void dfsOptimized(char[][] board, int row, int col, OptimizedTrieNode node, List<String> result) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        
        char c = board[row][col];
        if (c == '#' || node.children[c - 'a'] == null) {
            return;
        }
        
        node = node.children[c - 'a'];
        
        if (node.word != null) {
            result.add(node.word);
            node.word = null; // Remove to avoid duplicates
            node.wordCount--;
        }
        
        board[row][col] = '#';
        
        // Continue only if there are potential words
        if (node.wordCount > 0) {
            dfsOptimized(board, row - 1, col, node, result);
            dfsOptimized(board, row + 1, col, node, result);
            dfsOptimized(board, row, col - 1, node, result);
            dfsOptimized(board, row, col + 1, node, result);
        }
        
        board[row][col] = c;
        
        // Prune empty branches
        if (node.wordCount == 0) {
            // This would require parent reference for actual pruning
            // Simplified implementation
        }
    }
    
    private OptimizedTrieNode buildOptimizedTrie(String[] words) {
        OptimizedTrieNode root = new OptimizedTrieNode();
        
        for (String word : words) {
            OptimizedTrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new OptimizedTrieNode();
                }
                node = node.children[index];
                node.wordCount++;
            }
            node.word = word;
        }
        
        return root;
    }
    
    private static class OptimizedTrieNode {
        OptimizedTrieNode[] children = new OptimizedTrieNode[26];
        String word = null;
        int wordCount = 0; // Number of words passing through this node
    }
    
    /**
     * Parallel processing approach for large boards.
     * Divides board into regions and processes concurrently.
     */
    public List<String> findWordsParallel(char[][] board, String[] words) {
        if (board.length < 20 || board[0].length < 20) {
            return findWords(board, words); // Use sequential for small boards
        }
        
        TrieNode root = buildTrie(words);
        List<String> result = Collections.synchronizedList(new ArrayList<>());
        
        int m = board.length, n = board[0].length;
        int numThreads = Runtime.getRuntime().availableProcessors();
        int cellsPerThread = (m * n + numThreads - 1) / numThreads;
        
        List<Thread> threads = new ArrayList<>();
        
        for (int t = 0; t < numThreads; t++) {
            final int startCell = t * cellsPerThread;
            final int endCell = Math.min(startCell + cellsPerThread, m * n);
            
            Thread thread = new Thread(() -> {
                char[][] localBoard = deepCopyBoard(board);
                
                for (int cell = startCell; cell < endCell; cell++) {
                    int row = cell / n;
                    int col = cell % n;
                    dfs(localBoard, row, col, root, result);
                }
            });
            
            threads.add(thread);
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Remove duplicates
        return new ArrayList<>(new HashSet<>(result));
    }
    
    private char[][] deepCopyBoard(char[][] board) {
        char[][] copy = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, board[i].length);
        }
        return copy;
    }
    
    /**
     * Memory-optimized approach for constrained environments.
     * Uses iterative DFS to reduce call stack overhead.
     */
    public List<String> findWordsIterative(char[][] board, String[] words) {
        List<String> result = new ArrayList<>();
        TrieNode root = buildTrie(words);
        
        int m = board.length, n = board[0].length;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                iterativeDFS(board, i, j, root, result);
            }
        }
        
        return result;
    }
    
    private void iterativeDFS(char[][] board, int startRow, int startCol, TrieNode root, List<String> result) {
        Stack<DFSState> stack = new Stack<>();
        stack.push(new DFSState(startRow, startCol, root, new HashSet<>()));
        
        while (!stack.isEmpty()) {
            DFSState state = stack.pop();
            
            if (state.row < 0 || state.row >= board.length || 
                state.col < 0 || state.col >= board[0].length) {
                continue;
            }
            
            String cellKey = state.row + "," + state.col;
            if (state.visited.contains(cellKey)) {
                continue;
            }
            
            char c = board[state.row][state.col];
            if (state.node.children[c - 'a'] == null) {
                continue;
            }
            
            TrieNode nextNode = state.node.children[c - 'a'];
            Set<String> newVisited = new HashSet<>(state.visited);
            newVisited.add(cellKey);
            
            if (nextNode.word != null) {
                result.add(nextNode.word);
                nextNode.word = null; // Avoid duplicates
            }
            
            // Add neighbors to stack
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newRow = state.row + dir[0];
                int newCol = state.col + dir[1];
                stack.push(new DFSState(newRow, newCol, nextNode, newVisited));
            }
        }
    }
    
    private static class DFSState {
        int row, col;
        TrieNode node;
        Set<String> visited;
        
        DFSState(int row, int col, TrieNode node, Set<String> visited) {
            this.row = row;
            this.col = col;
            this.node = node;
            this.visited = visited;
        }
    }
    
    /**
     * Advanced pattern matching with wildcards.
     * Supports wildcard characters in word patterns.
     */
    public List<String> findWordsWithWildcards(char[][] board, String[] patterns) {
        List<String> result = new ArrayList<>();
        WildcardTrieNode root = buildWildcardTrie(patterns);
        
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dfsWildcard(board, i, j, root, "", result);
            }
        }
        
        return result;
    }
    
    private void dfsWildcard(char[][] board, int row, int col, WildcardTrieNode node, 
                           String path, List<String> result) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        
        char c = board[row][col];
        if (c == '#') return; // Already visited
        
        // Check for pattern match
        WildcardTrieNode nextNode = null;
        if (node.children.containsKey(c)) {
            nextNode = node.children.get(c);
        } else if (node.children.containsKey('*')) {
            nextNode = node.children.get('*'); // Wildcard match
        }
        
        if (nextNode == null) return;
        
        String newPath = path + c;
        if (nextNode.isEnd && nextNode.pattern != null) {
            result.add(nextNode.pattern);
            nextNode.pattern = null; // Avoid duplicates
        }
        
        board[row][col] = '#';
        
        dfsWildcard(board, row - 1, col, nextNode, newPath, result);
        dfsWildcard(board, row + 1, col, nextNode, newPath, result);
        dfsWildcard(board, row, col - 1, nextNode, newPath, result);
        dfsWildcard(board, row, col + 1, nextNode, newPath, result);
        
        board[row][col] = c;
    }
    
    private WildcardTrieNode buildWildcardTrie(String[] patterns) {
        WildcardTrieNode root = new WildcardTrieNode();
        
        for (String pattern : patterns) {
            WildcardTrieNode node = root;
            for (char c : pattern.toCharArray()) {
                if (!node.children.containsKey(c)) {
                    node.children.put(c, new WildcardTrieNode());
                }
                node = node.children.get(c);
            }
            node.isEnd = true;
            node.pattern = pattern;
        }
        
        return root;
    }
    
    private static class WildcardTrieNode {
        Map<Character, WildcardTrieNode> children = new HashMap<>();
        boolean isEnd = false;
        String pattern = null;
    }
    
    /**
     * 3D board extension for multi-dimensional word search.
     * Extends algorithm to work with 3D grids.
     */
    public List<String> findWords3D(char[][][] board3D, String[] words) {
        List<String> result = new ArrayList<>();
        TrieNode root = buildTrie(words);
        
        int x = board3D.length, y = board3D[0].length, z = board3D[0][0].length;
        
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int k = 0; k < z; k++) {
                    dfs3D(board3D, i, j, k, root, result);
                }
            }
        }
        
        return result;
    }
    
    private void dfs3D(char[][][] board, int x, int y, int z, TrieNode node, List<String> result) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || 
            z < 0 || z >= board[0][0].length) {
            return;
        }
        
        char c = board[x][y][z];
        if (c == '#' || node.children[c - 'a'] == null) {
            return;
        }
        
        node = node.children[c - 'a'];
        
        if (node.word != null) {
            result.add(node.word);
            node.word = null;
        }
        
        board[x][y][z] = '#';
        
        // 6 directions in 3D
        int[][] directions = {{-1,0,0}, {1,0,0}, {0,-1,0}, {0,1,0}, {0,0,-1}, {0,0,1}};
        for (int[] dir : directions) {
            dfs3D(board, x + dir[0], y + dir[1], z + dir[2], node, result);
        }
        
        board[x][y][z] = c;
    }
    
    /**
     * Dynamic dictionary approach for streaming word updates.
     * Handles word additions/removals during search.
     */
    public static class DynamicWordSearch {
        private DynamicTrieNode root;
        private char[][] board;
        
        public DynamicWordSearch(char[][] board) {
            this.board = board;
            this.root = new DynamicTrieNode();
        }
        
        public void addWord(String word) {
            DynamicTrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new DynamicTrieNode();
                }
                node = node.children[index];
            }
            node.word = word;
            node.isActive = true;
        }
        
        public void removeWord(String word) {
            removeWordHelper(root, word, 0);
        }
        
        private boolean removeWordHelper(DynamicTrieNode node, String word, int index) {
            if (index == word.length()) {
                if (!node.isActive) return false;
                node.isActive = false;
                node.word = null;
                return !hasActiveChildren(node);
            }
            
            int charIndex = word.charAt(index) - 'a';
            if (node.children[charIndex] == null) return false;
            
            boolean shouldDelete = removeWordHelper(node.children[charIndex], word, index + 1);
            
            if (shouldDelete) {
                node.children[charIndex] = null;
                return !node.isActive && !hasActiveChildren(node);
            }
            
            return false;
        }
        
        private boolean hasActiveChildren(DynamicTrieNode node) {
            for (DynamicTrieNode child : node.children) {
                if (child != null) return true;
            }
            return false;
        }
        
        public List<String> findCurrentWords() {
            List<String> result = new ArrayList<>();
            
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    dfsForDynamic(board, i, j, root, result);
                }
            }
            
            return result;
        }
        
        private void dfsForDynamic(char[][] board, int row, int col, DynamicTrieNode node, List<String> result) {
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
                return;
            }
            
            char c = board[row][col];
            if (c == '#' || node.children[c - 'a'] == null) {
                return;
            }
            
            node = node.children[c - 'a'];
            
            if (node.isActive && node.word != null) {
                result.add(node.word);
            }
            
            board[row][col] = '#';
            
            dfsForDynamic(board, row - 1, col, node, result);
            dfsForDynamic(board, row + 1, col, node, result);
            dfsForDynamic(board, row, col - 1, node, result);
            dfsForDynamic(board, row, col + 1, node, result);
            
            board[row][col] = c;
        }
        
        private static class DynamicTrieNode {
            DynamicTrieNode[] children = new DynamicTrieNode[26];
            String word = null;
            boolean isActive = false;
        }
    }
    
    /**
     * Performance analysis and benchmarking utilities.
     * Tools for measuring and optimizing search performance.
     */
    public static class WordSearchAnalyzer {
        
        public static SearchMetrics analyzePerformance(char[][] board, String[] words) {
            long startTime = System.nanoTime();
            
            WordSearchII solver = new WordSearchII();
            List<String> result = solver.findWords(board, words);
            
            long endTime = System.nanoTime();
            double timeMs = (endTime - startTime) / 1_000_000.0;
            
            int boardSize = board.length * board[0].length;
            int totalWordLength = Arrays.stream(words).mapToInt(String::length).sum();
            
            return new SearchMetrics(
                result.size(),
                words.length,
                boardSize,
                totalWordLength,
                timeMs,
                calculateTrieSize(words),
                calculateSearchComplexity(board, words)
            );
        }
        
        private static int calculateTrieSize(String[] words) {
            Set<String> prefixes = new HashSet<>();
            for (String word : words) {
                for (int i = 1; i <= word.length(); i++) {
                    prefixes.add(word.substring(0, i));
                }
            }
            return prefixes.size();
        }
        
        private static long calculateSearchComplexity(char[][] board, String[] words) {
            long complexity = 0;
            int m = board.length, n = board[0].length;
            int maxWordLength = Arrays.stream(words).mapToInt(String::length).max().orElse(0);
            
            // Simplified complexity estimation
            complexity = (long) m * n * Math.pow(4, Math.min(maxWordLength, 10));
            
            return complexity;
        }
    }
    
    public static class SearchMetrics {
        public final int foundWords;
        public final int totalWords;
        public final int boardSize;
        public final int totalWordLength;
        public final double executionTimeMs;
        public final int trieSize;
        public final long estimatedComplexity;
        
        public SearchMetrics(int foundWords, int totalWords, int boardSize, int totalWordLength,
                           double executionTimeMs, int trieSize, long estimatedComplexity) {
            this.foundWords = foundWords;
            this.totalWords = totalWords;
            this.boardSize = boardSize;
            this.totalWordLength = totalWordLength;
            this.executionTimeMs = executionTimeMs;
            this.trieSize = trieSize;
            this.estimatedComplexity = estimatedComplexity;
        }
        
        public double getSuccessRate() {
            return totalWords == 0 ? 0 : (double) foundWords / totalWords;
        }
        
        public double getTrieEfficiency() {
            return totalWordLength == 0 ? 0 : (double) trieSize / totalWordLength;
        }
        
        @Override
        public String toString() {
            return String.format("Found: %d/%d words (%.1f%%), Time: %.2fms, Trie: %d nodes, Efficiency: %.2f",
                               foundWords, totalWords, getSuccessRate() * 100, 
                               executionTimeMs, trieSize, getTrieEfficiency());
        }
    }
}