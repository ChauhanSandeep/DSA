package design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * https://leetcode.com/problems/design-search-autocomplete-system/
 */
public class AutocompleteSystem {

    public static void main(String[] args) {
        String[] sentences = {
                "i love you",
                "island",
                "iroman",
                "i like leetcode"
        };
        int[] weight = {
                5,
                3,
                2,
                2
        };
        AutocompleteSystem autocompleteSystem = new AutocompleteSystem(sentences, weight);
        List<String> input = autocompleteSystem.input('i');
        System.out.println(input);
        input = autocompleteSystem.input(' ');
        System.out.println(input);
        input = autocompleteSystem.input('l');
        System.out.println(input);
        input = autocompleteSystem.input('i');
        System.out.println(input);
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
    public AutocompleteSystem(String[] sentences, int[] weights) {
        root = new TrieNode();
        prefix = "";

        for (int i = 0; i < sentences.length; i++) {
            add(sentences[i], weights[i]);
        }
    }

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
