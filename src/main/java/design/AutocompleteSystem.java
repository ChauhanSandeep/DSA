package design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * Problem: Design Search Autocomplete System
 *
 * Build an autocomplete engine that stores historical sentences with hotness
 * scores. For each typed character, return up to three matching sentences ordered
 * by highest frequency, then lexicographic order for ties. A '#' commits the
 * current prefix as a new sentence.
 *
 * Leetcode: https://leetcode.com/problems/design-search-autocomplete-system/ (Hard)
 * Rating:   not available (design problem)
 * Pattern:  Trie | Prefix search | Priority queue ranking
 *
 * Example:
 *   Input:  sentences = ["i love you", "island", "iroman", "i like leetcode"], input = 'i'
 *   Output: ["i love you", "island", "i like leetcode"]
 *   Why:    all three start with 'i'; frequency ranks 5, 3, then 2, with the
 *           lexicographically smaller 2-frequency sentence chosen before "iroman".
 *
 * Follow-ups:
 *   1. How would you support millions of sentences?
 *      Keep only each node's top-k sentence ids and store full metadata outside the trie.
 *   2. How would you support deletes or decayed popularity?
 *      Update terminal counts and recompute affected prefix rankings lazily.
 *   3. How would you shard the service?
 *      Partition by leading prefix and merge top-k results from relevant shards.
 *
 * Related: Implement Trie (208), Search Suggestions System (1268), Top K Frequent Words (692).
 */
public class AutocompleteSystem {

    public static void main(String[] args) {
        String[] sentences = {"i love you", "island", "iroman", "i like leetcode"};
        int[] weights = {5, 3, 2, 2};
        char[] inputs = {'i', ' ', 'l', '#', 'i'};
        String[][] expected = {
                {"i love you", "island", "i like leetcode"},
                {"i love you", "i like leetcode"},
                {"i love you", "i like leetcode"},
                {},
                {"i love you", "island", "i like leetcode"}
        };

        AutocompleteSystem autocompleteSystem = new AutocompleteSystem(sentences, weights);
        for (int i = 0; i < inputs.length; i++) {
            List<String> got = autocompleteSystem.input(inputs[i]);
            System.out.printf("input=%s -> %s  expected=%s%n",
                    inputs[i] == ' ' ? "space" : String.valueOf(inputs[i]),
                    got,
                    Arrays.toString(expected[i]));
        }
    }

    class TrieNode {
        Map<Character, TrieNode> children;
        Map<String, Integer> strWeightMap; // <completeString, weight>
        boolean isWord;
        public TrieNode() {
            children = new HashMap<>();
            strWeightMap = new HashMap<>();
            isWord = false;
        }
    }

    class Pair {
        String str;
        int weight;
        public Pair(String str, int weight) {
            this.str = str;
            this.weight = weight;
        }
    }

    TrieNode root;
    String prefix;
    /**
     * Builds the trie and records every initial sentence under each prefix.
     *
     * Time:  O(T) - T is the total number of characters across all input sentences.
     * Space: O(T) - trie nodes and prefix maps store the inserted sentence data.
     *
     * @param sentences historical sentences to preload
     * @param weights matching hotness values for each sentence
     */
    public AutocompleteSystem(String[] sentences, int[] weights) {
        root = new TrieNode();
        prefix = "";

        for (int i = 0; i < sentences.length; i++) {
            add(sentences[i], weights[i]);
        }
    }

    /** Adds one sentence weight into every trie node on its path. */
    private void add(String str, int weight) {
        TrieNode curr = root;
        for (char c : str.toCharArray()) {
            TrieNode next = curr.children.get(c);
            if (next == null) {
                next = new TrieNode();
                curr.children.put(c, next);
            }
            curr = next;
            curr.strWeightMap.put(str, curr.strWeightMap.getOrDefault(str, 0) + weight);
        }
        curr.isWord = true;
    }

    /**
     * Processes one typed character and returns the current top three completions.
     *
     * Time:  O(P + M log M) - P scans the prefix and M matching sentences are ranked.
     * Space: O(M) - the priority queue stores matching sentences for this prefix.
     *
     * @param c next typed character, or '#' to commit the current prefix
     * @return up to three matching sentences in hotness and lexicographic order
     */
    public List<String> input(char c) {
        if (c == '#') { // add current string to sentences
            add(prefix, 1);
            prefix = "";
            return new ArrayList<>();
        }

        prefix = prefix + c;
        TrieNode currNode = root;
        for (char currChar : prefix.toCharArray()) {
            TrieNode nextNode = currNode.children.get(currChar);
            if (nextNode == null) {
                return new ArrayList<>();
            }
            currNode = nextNode;
        }

        PriorityQueue<Pair> queue = new PriorityQueue<>((a, b) -> {
            return (a.weight == b.weight ? a.str.compareTo(b.str) : b.weight - a.weight);
        });
        for (String str : currNode.strWeightMap.keySet()) {
            queue.add(new Pair(str, currNode.strWeightMap.get(str)));
        }

        List<String> result = new ArrayList<String>();
        for (int i = 0; i < 3 && !queue.isEmpty(); i++) {
            result.add(queue.poll().str);
        }
        return result;
    }
}
