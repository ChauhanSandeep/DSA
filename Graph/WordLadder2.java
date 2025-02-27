package Graph;

import java.util.*;

public class WordLadder2 {

    Map<String, List<String>> graph = new HashMap<>();
    List<List<String>> result = new ArrayList<>();
    List<String> currPath = new ArrayList<>();

    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        List<List<String>> ladders = new WordLadder2().findLadders(beginWord, endWord, wordList);
        System.out.println(ladders);
    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) return result; // Early exit

        // Step 1: Build the graph using BFS
        if (!createGraph(beginWord, endWord, wordSet)) return result;

        // Step 2: Find all paths using backtracking
        currPath.add(beginWord);
        backtrack(beginWord, endWord);
        return result;
    }

    private boolean createGraph(String beginWord, String endWord, Set<String> wordSet) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Map<String, Integer> level = new HashMap<>();
        level.put(beginWord, 0);

        boolean foundEndWord = false;
        int depth = 0;

        while (!queue.isEmpty() && !foundEndWord) {
            depth++;
            int size = queue.size();
            Set<String> nextLevelWords = new HashSet<>();

            for (int i = 0; i < size; i++) {
                String word = queue.poll();
                List<String> neighbors = findNeighbors(word, wordSet);

                for (String neighbor : neighbors) {
                    if (!level.containsKey(neighbor)) {
                        level.put(neighbor, depth);
                        queue.add(neighbor);
                    }
                    if (level.get(neighbor) == depth) { // Ensures shortest paths only
                        graph.computeIfAbsent(word, k -> new ArrayList<>()).add(neighbor);
                    }
                    if (neighbor.equals(endWord)) foundEndWord = true;
                }
                nextLevelWords.addAll(neighbors);
            }

            wordSet.removeAll(nextLevelWords); // Remove processed words
        }
        return foundEndWord;
    }

    private List<String> findNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) continue;
                chars[i] = c;
                String newWord = new String(chars);
                if (wordSet.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
            chars[i] = originalChar;
        }
        return neighbors;
    }

    private void backtrack(String source, String destination) {
        if (source.equals(destination)) {
            result.add(new ArrayList<>(currPath));
            return;
        }

        if (!graph.containsKey(source)) return;

        for (String neighbor : graph.get(source)) {
            currPath.add(neighbor);
            backtrack(neighbor, destination);
            currPath.remove(currPath.size() - 1);
        }
    }
}
