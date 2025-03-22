package StackQueue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of an LRU (Least Recently Used) Cache using LinkedHashMap.
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
class LRUCache extends LinkedHashMap<Integer, Integer> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // Enable access-order for LRU behavior
        this.capacity = capacity;
    }

    /**
     * Retrieves the value associated with the key, or -1 if not found.
     * @param key The key to retrieve.
     * @return The associated value, or -1 if the key is not found.
     */
    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    /**
     * Inserts a new key-value pair or updates an existing one.
     * If the cache exceeds capacity, the least recently used entry is removed.
     * @param key The key to insert/update.
     * @param value The value associated with the key.
     */
    public void put(int key, int value) {
        super.put(key, value);
    }

    /**
     * Removes the eldest entry if the cache exceeds its capacity.
     * This method is automatically called by LinkedHashMap when inserting a new entry.
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity;
    }
}

/**
 * Driver class to test LRU Cache functionality.
 */
public class LruCacheTest {
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 10);
        cache.put(2, 20);
        System.out.println("Value for key 1: " + cache.get(1)); // Output: 10

        cache.put(3, 30); // Evicts key 2
        System.out.println("Value for key 2: " + cache.get(2)); // Output: -1 (not found)

        cache.put(4, 40); // Evicts key 1
        System.out.println("Value for key 1: " + cache.get(1)); // Output: -1 (not found)
        System.out.println("Value for key 3: " + cache.get(3)); // Output: 30
        System.out.println("Value for key 4: " + cache.get(4)); // Output: 40
    }
}
