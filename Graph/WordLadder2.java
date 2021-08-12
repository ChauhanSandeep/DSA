package Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordLadder2 {

    Map<String, List<String>> graph = new HashMap<>();
    List<String> currPath = new ArrayList<>();
    List<List<String>> result = new ArrayList<>();

    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList = Stream.of("hot", "dot", "dog", "lot", "log", "cog").collect(Collectors.toList());
        List<List<String>> ladders = new WordLadder2().findLadders(beginWord, endWord, wordList);
        System.out.println(ladders);
    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // copying the words into the set for efficient deletion in BFS
        Set<String> wordSet = new HashSet<>(wordList);
        // build the DAG using BFS
        createGraph(beginWord, endWord, wordSet);

        // every path will start from the beginWord
        currPath.add(beginWord);
        // traverse the DAG to find all the paths between beginWord and endWord
        backtrack(beginWord, endWord);

        return result;
    }

    private void createGraph(String beginWord, String endWord, Set<String> wordList) {
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        // remove the root word which is the first layer in the BFS
        if (wordList.contains(beginWord)) {
            wordList.remove(beginWord);
        }

        Map<String, Integer> isEnqueued = new HashMap<String, Integer>();
        isEnqueued.put(beginWord, 1);

        while (queue.size() > 0) {
            // currentLayerWords will store the words of current layer
            List<String> currentLayerWords = new ArrayList<>();

            int size = queue.size();
            while(size > 0) {
                String currWord = queue.poll();
                List<String> neighbors = findNeighbors(currWord, wordList);
                for (String neighbor : neighbors) {
                    currentLayerWords.add(neighbor);

                    if (!graph.containsKey(currWord)) {
                        graph.put(currWord, new ArrayList<>());
                    }

                    // add the edge from currWord to neighbor in the list
                    graph.get(currWord).add(neighbor);
                    if (!isEnqueued.containsKey(neighbor)) {
                        queue.add(neighbor);
                        isEnqueued.put(neighbor, 1);
                    }
                }
                size--;
            }
            // removing the words of the previous layer
            for (int i = 0; i < currentLayerWords.size(); i++) {
                if (wordList.contains(currentLayerWords.get(i))) {
                    wordList.remove(currentLayerWords.get(i));
                }
            }
        }
    }

    private List<String> findNeighbors(String word, Set<String> wordList) {
        List<String> neighbors = new ArrayList<String>();
        char charList[] = word.toCharArray();

        for (int i = 0; i < word.length(); i++) {
            char oldChar = charList[i];

            // replace the i-th character with all letters from a to z except the original character
            for (char c = 'a'; c <= 'z'; c++) {
                charList[i] = c;

                // skip if the character is same as original or if the word is not present in the wordList
                if (c == oldChar || !wordList.contains(String.valueOf(charList))) {
                    continue;
                }
                neighbors.add(String.valueOf(charList));
            }
            charList[i] = oldChar;
        }
        return neighbors;
    }

    private void backtrack(String source, String destination) {
        // store the path if we reached the endWord
        if (source.equals(destination)) {
            List<String> tempPath = new ArrayList<>(currPath);
            result.add(tempPath);
        }

        if (!graph.containsKey(source)) {
            return;
        }

        for (int i = 0; i < graph.get(source).size(); i++) {
            currPath.add(graph.get(source).get(i));
            backtrack(graph.get(source).get(i), destination);
            currPath.remove(currPath.size() - 1);
        }
    }

}
