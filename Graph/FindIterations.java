package Graph;

import java.util.*;

public class FindIterations {
    public static void main(String[] args) {
        String[] words = {"but", "put", "big", "pot", "pog", "pig", "dog", "lot"};
        String source = "bit", target = "pog";

        int result = findShortestTransformation(source, target, words);
        System.out.println(result);
    }

    public static int findShortestTransformation(String source, String target, String[] words) {
        Set<String> wordSet = new HashSet<>(Arrays.asList(words));
        if (!wordSet.contains(target)) return -1; // If target word isn't in the dictionary, no transformation is possible.

        // Build graph using adjacency list
        Map<String, List<String>> graph = new HashMap<>();
        for (String word : words) {
            graph.put(word, new ArrayList<>());
        }

        // Connect words that differ by one letter
        for (String word1 : words) {
            for (String word2 : words) {
                if (isNeighbour(word1, word2)) {
                    graph.get(word1).add(word2);
                    graph.get(word2).add(word1);
                }
            }
        }

        // Add source word to the graph
        graph.put(source, new ArrayList<>());
        for (String word : words) {
            if (isNeighbour(source, word)) {
                graph.get(source).add(word);
            }
        }

        // BFS for shortest path
        Queue<String> queue = new LinkedList<>();
        queue.add(source);
        Map<String, Integer> distance = new HashMap<>();
        distance.put(source, 1);

        while (!queue.isEmpty()) {
            String currentWord = queue.poll();
            int currDistance = distance.get(currentWord);

            for (String neighbor : graph.getOrDefault(currentWord, new ArrayList<>())) {
                if (!distance.containsKey(neighbor)) { // Not visited yet
                    distance.put(neighbor, currDistance + 1);
                    queue.add(neighbor);
                    if (neighbor.equals(target)) {
                        return currDistance + 1;
                    }
                }
            }
        }
        return -1; // No transformation possible
    }

    public static boolean isNeighbour(String a, String b) {
        if (a.length() != b.length()) return false;

        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) diff++;
        }
        return diff == 1;
    }
}
