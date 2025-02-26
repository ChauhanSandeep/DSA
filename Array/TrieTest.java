package Array;

import java.util.HashMap;
import java.util.Map;

public class TrieTest {
    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));  // true
        System.out.println(trie.search("app"));    // false
        System.out.println(trie.startsWith("app"));// true
        trie.insert("app");
        System.out.println(trie.search("app"));    // true
        System.out.println(trie.startsWith("app"));// true
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isWord = true;
    }

    public boolean search(String word) {
        TrieNode node = getNode(word);
        return node != null && node.isWord;
    }

    public boolean startsWith(String prefix) {
        return getNode(prefix) != null;
    }

    private TrieNode getNode(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (!node.children.containsKey(c)) return null;
            node = node.children.get(c);
        }
        return node;
    }
}

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord;
}
