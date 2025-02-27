package Hashing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A data structure that supports the following operations in O(1) time:
 * 1. {@code inc(String key)} - Increments the count of a key by 1. If the key is not present, inserts it with a count of 1.
 * 2. {@code dec(String key)} - Decrements the count of a key by 1. If the count becomes 0, removes the key.
 * 3. {@code getMaxKey()} - Returns any key with the highest count.
 * 4. {@code getMinKey()} - Returns any key with the lowest count.
 *
 * Internally, a doubly linked list maintains frequency groups, and a HashMap maps each key to its corresponding node.
 */
class AllOne {

    /**
     * Represents a node in the doubly linked list that stores a set of keys with the same frequency.
     */
    class Node {
        Set<String> strs; // Set of keys with the same frequency
        int freq;         // Frequency of the keys
        Node prev, next;  // Pointers to previous and next nodes

        /**
         * Constructs a node with the given frequency.
         *
         * @param freq The frequency of the keys stored in this node.
         */
        Node(int freq) {
            this.freq = freq;
            this.strs = new HashSet<>();
        }

        /**
         * Constructs a node with a single key and given frequency.
         *
         * @param str  The key to store.
         * @param freq The frequency of the key.
         */
        Node(String str, int freq) {
            this(freq);
            this.strs.add(str);
        }
    }

    private final Map<String, Node> map; // Maps a key to its corresponding frequency node
    private final Node head, tail; // Sentinel nodes to simplify operations

    /**
     * Initializes the data structure.
     */
    public AllOne() {
        this.map = new HashMap<>();
        this.head = new Node(-1);
        this.tail = new Node(-1);

        // Sentinel head & tail to simplify insertion/deletion
        this.head.prev = tail;
        this.tail.next = head;
    }

    /**
     * Removes a node from the doubly linked list.
     *
     * @param node The node to remove.
     */
    private void deleteNode(Node node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    /**
     * Removes a key from a node and deletes the node if it becomes empty.
     *
     * @param key  The key to remove.
     * @param node The node containing the key.
     */
    private void removeKeyAndCleanUp(String key, Node node) {
        node.strs.remove(key);
        if (node.strs.isEmpty()) {
            deleteNode(node);
        }
    }

    /**
     * Inserts a new node after a given node in the doubly linked list.
     *
     * @param node    The existing node after which the new node will be inserted.
     * @param newNode The new node to insert.
     */
    private void insertAfter(Node node, Node newNode) {
        newNode.next = node.next;
        newNode.prev = node;
        node.next.prev = newNode;
        node.next = newNode;
    }

    /**
     * Increments the frequency of a key by 1.
     * If the key does not exist, inserts it with a frequency of 1.
     *
     * @param key The key to increment.
     */
    public void inc(String key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            if (node.freq + 1 == node.next.freq) {
                node.next.strs.add(key);
                map.put(key, node.next);
            } else {
                Node newNode = new Node(key, node.freq + 1);
                insertAfter(node, newNode);
                map.put(key, newNode);
            }
            removeKeyAndCleanUp(key, node);
        } else {
            if (tail.next.freq == 1) {
                tail.next.strs.add(key);
                map.put(key, tail.next);
            } else {
                Node newNode = new Node(key, 1);
                insertAfter(tail, newNode);
                map.put(key, newNode);
            }
        }
    }

    /**
     * Decrements the frequency of a key by 1.
     * If the key's frequency becomes 0, it is removed from the data structure.
     *
     * @param key The key to decrement.
     */
    public void dec(String key) {
        if (!map.containsKey(key)) return;

        Node node = map.get(key);
        if (node.freq == 1) {
            map.remove(key);
        } else if (node.freq - 1 == node.prev.freq) {
            node.prev.strs.add(key);
            map.put(key, node.prev);
        } else {
            Node newNode = new Node(key, node.freq - 1);
            insertAfter(node.prev, newNode);
            map.put(key, newNode);
        }
        removeKeyAndCleanUp(key, node);
    }

    /**
     * Returns one of the keys with the highest frequency.
     * If there are no keys, returns an empty string.
     *
     * @return A key with the highest frequency or an empty string if no keys exist.
     */
    public String getMaxKey() {
        return head.prev == tail ? "" : head.prev.strs.iterator().next();
    }

    /**
     * Returns one of the keys with the lowest frequency.
     * If there are no keys, returns an empty string.
     *
     * @return A key with the lowest frequency or an empty string if no keys exist.
     */
    public String getMinKey() {
        return tail.next == head ? "" : tail.next.strs.iterator().next();
    }
}