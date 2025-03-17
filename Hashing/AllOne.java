package Hashing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * LeetCode Problem: https://leetcode.com/problems/all-oone-data-structure/
 * 
 * Implements a data structure that supports the following operations in O(1) time:
 * - {@code inc(String key)}: Increments the count of a key by 1. If the key is not present, inserts it with count 1.
 * - {@code dec(String key)}: Decrements the count of a key by 1. If the count becomes 0, removes the key.
 * - {@code getMaxKey()}: Returns a key with the highest count.
 * - {@code getMinKey()}: Returns a key with the lowest count.
 * 
 * Uses a **doubly linked list** to maintain frequency groups and a **HashMap** to store key-node mappings.
 * 
 * Time Complexity:
 * - `inc()`, `dec()`, `getMaxKey()`, `getMinKey()` → **O(1)**
 * - Space Complexity: **O(N)** (where N is the number of unique keys)
 */
class AllOne {

    /**
     * Represents a node in the doubly linked list that groups keys with the same frequency.
     */
    class Node {
        Set<String> keys; // Stores keys with the same frequency
        int frequency;    // Frequency of the keys
        Node prev, next;  // Pointers to previous and next nodes

        /**
         * Constructs a node with the given frequency.
         */
        Node(int frequency) {
            this.frequency = frequency;
            this.keys = new HashSet<>();
        }

        /**
         * Constructs a node with a single key and given frequency.
         */
        Node(String key, int frequency) {
            this(frequency);
            this.keys.add(key);
        }
    }

    private final Map<String, Node> keyNodeMap; // Maps a key to its corresponding frequency node
    private final Node head, tail; // Sentinel nodes for easy insertion/deletion

    /**
     * Initializes the data structure.
     */
    public AllOne() {
        this.keyNodeMap = new HashMap<>();
        this.head = new Node(-1); // Dummy head (max frequency node will be just before this)
        this.tail = new Node(-1); // Dummy tail (min frequency node will be just after this)

        head.prev = tail;
        tail.next = head;
    }

    /**
     * Removes a node from the doubly linked list.
     */
    private void deleteNode(Node node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    /**
     * Removes a key from a node and deletes the node if it becomes empty.
     */
    private void removeKeyAndCleanUp(String key, Node node) {
        node.keys.remove(key);
        if (node.keys.isEmpty()) {
            deleteNode(node);
        }
    }

    /**
     * Inserts a new node after a given node in the doubly linked list.
     */
    private void insertAfter(Node existingNode, Node newNode) {
        newNode.next = existingNode.next;
        newNode.prev = existingNode;
        existingNode.next.prev = newNode;
        existingNode.next = newNode;
    }

    /**
     * Retrieves or creates the next frequency node.
     */
    private Node createOrGetNextNode(Node currentNode, String key, int newFrequency) {
        if (currentNode.next.frequency == newFrequency) {
            currentNode.next.keys.add(key);
            return currentNode.next;
        }
        Node newNode = new Node(key, newFrequency);
        insertAfter(currentNode, newNode);
        return newNode;
    }

    /**
     * Retrieves or creates the previous frequency node.
     */
    private Node createOrGetPrevNode(Node currentNode, String key, int newFrequency) {
        if (currentNode.prev.frequency == newFrequency) {
            currentNode.prev.keys.add(key);
            return currentNode.prev;
        }
        Node newNode = new Node(key, newFrequency);
        insertAfter(currentNode.prev, newNode);
        return newNode;
    }

    /**
     * Increments the frequency of a key by 1.
     * If the key does not exist, inserts it with a frequency of 1.
     */
    public void inc(String key) {
        if (keyNodeMap.containsKey(key)) {
            Node currentNode = keyNodeMap.get(key);
            Node nextNode = createOrGetNextNode(currentNode, key, currentNode.frequency + 1);
            keyNodeMap.put(key, nextNode);
            removeKeyAndCleanUp(key, currentNode);
        } else {
            Node firstNode = (tail.next.frequency == 1) ? tail.next : new Node(key, 1);
            if (firstNode == tail.next) {
                firstNode.keys.add(key);
            } else {
                insertAfter(tail, firstNode);
            }
            keyNodeMap.put(key, firstNode);
        }
    }

    /**
     * Decrements the frequency of a key by 1.
     * If the key's frequency becomes 0, it is removed from the data structure.
     */
    public void dec(String key) {
        if (!keyNodeMap.containsKey(key)) return;

        Node currentNode = keyNodeMap.get(key);
        if (currentNode.frequency == 1) {
            keyNodeMap.remove(key); // Remove key entirely
        } else {
            Node prevNode = createOrGetPrevNode(currentNode, key, currentNode.frequency - 1);
            keyNodeMap.put(key, prevNode);
        }
        removeKeyAndCleanUp(key, currentNode);
    }

    /**
     * Returns a key with the highest frequency.
     * If no keys exist, returns an empty string.
     */
    public String getMaxKey() {
        return head.prev == tail ? "" : head.prev.keys.iterator().next();
    }

    /**
     * Returns a key with the lowest frequency.
     * If no keys exist, returns an empty string.
     */
    public String getMinKey() {
        return tail.next == head ? "" : tail.next.keys.iterator().next();
    }
}
