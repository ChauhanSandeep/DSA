package StackQueue;

import java.util.*;

/**
 * Implementation of an LRU (Least Recently Used) Cache
 * - LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
 * - int get(int key) Return the value of the key if the key exists, otherwise return -1.
 * - void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to
 * the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
 *
 * The functions get and put must each run in O(1) average time complexity.
 *
 * <p>LeetCode Problem Link:
 * <a href="https://leetcode.com/problems/lru-cache/">LRU Cache</a>
 * </p>
 *
 * <p><b>Approach:</b></p>
 * - Uses **LinkedHashMap** with `accessOrder = true` to maintain LRU order.
 * - **Overrides `removeEldestEntry`** to automatically remove the least recently used entry.
 * - Provides **O(1) time complexity** for `get()` and `put()` operations.
 *
 * <p><b>Time Complexity:</b></p>
 * - **O(1) for `get(key)`** (Direct lookup via HashMap).
 * - **O(1) for `put(key, value)`** (Insertion & reordering in LinkedHashMap).
 *
 * <p><b>Space Complexity:</b> O(capacity) (Stores up to `capacity` items).</p>
 */
public class LruCacheTest {
    public static void main(String[] args) {
        LruCache cache = new LruCache(2);
        cache.set(1, 10);
        cache.set(2, 20);
        System.out.println("Value for the key: 1 is " + cache.get(1));

        cache.set(3, 30);
        System.out.println("Value for the key: 2 is " + cache.get(2));

        cache.set(4, 40);
        System.out.println("Value for the key: 1 is " + cache.get(1));
        System.out.println("Value for the key: 3 is " + cache.get(3));
        System.out.println("Value for the key: 4 is " + cache.get(4));
    }
}

class LruNode {
    int key;
    int value;
    LruNode prev;
    LruNode next;

    public LruNode(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

// Queue used in this will cause O(n) time complexity to fetch a item (To remove a item etc)
// Check LruCacheTestImproved
class LruCache {
    private final int capacity;
    private final Map<Integer, LruNode> cacheMap = new HashMap<>();
    private final LruNode head = new LruNode(0, 0); // dummy head
    private final LruNode tail = new LruNode(0, 0); // dummy tail

    // Initialize the doubly linked list
    public LruCache(int capacity) {
        this.capacity = capacity;
        head.next = tail;
        tail.prev = head;
    }


    /**
     * To get value from cache
     * @param key
     * @return
     */
    public int get(int key) {
        if (!cacheMap.containsKey(key)) return -1;

        LruNode node = cacheMap.get(key);
        // Move the accessed node to the front (most recently used)
        removeNode(node);
        addToFront(node);
        return node.value;
    }

    /**
     * To add the key, value to cache
     * @param key
     * @param value
     */
    public void set(int key, int value) {
        if (cacheMap.containsKey(key)) {
            // If key exists, update the value and move to front
            LruNode node = cacheMap.get(key);
            node.value = value;
            removeNode(node);
            addToFront(node);
        } else {
            // If cache is full, remove the least recently used item
            if (cacheMap.size() >= capacity) {
                LruNode toRemove = tail.prev;
                removeNode(toRemove);
                cacheMap.remove(toRemove.key);
            }
            // Add new node to the front
            LruNode newNode = new LruNode(key, value);
            addToFront(newNode);
            cacheMap.put(key, newNode);
        }
    }

    // Helper method to remove a node from the doubly linked list
    private void removeNode(LruNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // Helper method to add a node to the front of the doubly linked list
    private void addToFront(LruNode node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
