package StackQueue;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode Problem: LRU Cache
 * Problem Link: https://leetcode.com/problems/lru-cache/
 *
 * Implementation of an LRU (Least Recently Used) Cache using a HashMap and a Doubly Linked List.
 * - HashMap provides O(1) lookup for keys.
 * - Doubly Linked List maintains the order of use (most recently used at head).
 *
 * Time Complexity:
 * - get(key): O(1)
 * - put(key, value): O(1)
 *
 * Space Complexity: O(capacity)
 */
public class LruCacheTestImproved {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // Output: 1
        
        cache.put(3, 3); // Evicts key 2
        System.out.println(cache.get(2)); // Output: -1 (not found)
        
        cache.put(4, 4); // Evicts key 1
        System.out.println(cache.get(1)); // Output: -1 (not found)
        System.out.println(cache.get(3)); // Output: 3
        System.out.println(cache.get(4)); // Output: 4
    }
}

/**
 * LRU Cache implementation using a HashMap and a Doubly Linked List.
 */
class LRUCache {
    private final int capacity;
    private final Map<Integer, Node> cache;
    private final DoublyLinkedList dll;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.dll = new DoublyLinkedList();
    }

    /**
     * Retrieves a value from the cache.
     * Moves the accessed node to the front of the list.
     */
    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1; // Key not found
        }

        Node node = cache.get(key);
        dll.moveToHead(node); // Mark as most recently used
        return node.value;
    }

    /**
     * Inserts a new key-value pair or updates an existing key.
     * If the cache exceeds its capacity, removes the least recently used item.
     */
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            // Update existing node and move to front
            Node node = cache.get(key);
            node.value = value;
            dll.moveToHead(node);
        } else {
            // Create a new node
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            dll.addFirst(newNode);

            // If capacity exceeded, remove LRU item
            if (cache.size() > capacity) {
                int removedKey = dll.removeLast();
                cache.remove(removedKey);
            }
        }
    }
}

/**
 * Node class for Doubly Linked List.
 */
class Node {
    int key, value;
    Node prev, next;

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

/**
 * Doubly Linked List to maintain LRU order.
 */
class DoublyLinkedList {
    private final Node head, tail;

    public DoublyLinkedList() {
        head = new Node(-1, -1); // Dummy head
        tail = new Node(-1, -1); // Dummy tail
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Adds a node to the front of the list (marking it as most recently used).
     */
    public void addFirst(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    /**
     * Removes the least recently used node (last node before dummy tail).
     */
    public int removeLast() {
        if (tail.prev == head) return -1; // No elements
        Node lastNode = tail.prev;
        removeNode(lastNode);
        return lastNode.key;
    }

    /**
     * Moves a node to the front (most recently used).
     */
    public void moveToHead(Node node) {
        removeNode(node);
        addFirst(node);
    }

    /**
     * Removes a node from the linked list.
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
