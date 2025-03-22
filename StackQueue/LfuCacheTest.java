package StackQueue;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Problem: Implement an LFU (Least Frequently Used) Cache.
 * 
 * <p>LeetCode Problem Link:
 * <a href="https://leetcode.com/problems/lfu-cache/">LFU Cache</a>
 * </p>
 *
 * <p><b>Approach:</b></p>
 * - Uses **3 HashMaps**:
 *   1. `keyToValueMap`: Stores the mapping of `key -> value`.
 *   2. `keyToFrequencyMap`: Stores the mapping of `key -> frequency count`.
 *   3. `frequencyToKeysMap`: Stores the mapping of `frequency -> list of keys` (ordered using `LinkedHashSet`).
 * - Maintains a `minFrequency` to track the least frequently used element efficiently.
 * - When `capacity` is reached, removes the **least frequently used key**.
 * 
 * <p><b>Time Complexity:</b></p>
 * - **O(1)** for `get()` (HashMap + LinkedHashSet operations).
 * - **O(1)** for `put()` (Removals & insertions are constant time).
 *
 * <p><b>Space Complexity:</b> O(capacity) (HashMaps storing at most `capacity` keys).</p>
 */
class LFUCache {
    private final int capacity;
    private int minFrequency;
    private final Map<Integer, Integer> keyToValueMap; // Stores key -> value
    private final Map<Integer, Integer> keyToFrequencyMap; // Stores key -> frequency count
    private final Map<Integer, LinkedHashSet<Integer>> frequencyToKeysMap; // Stores frequency -> list of keys

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFrequency = 0;
        this.keyToValueMap = new HashMap<>();
        this.keyToFrequencyMap = new HashMap<>();
        this.frequencyToKeysMap = new HashMap<>();
        frequencyToKeysMap.put(1, new LinkedHashSet<>());
    }

    /**
     * Retrieves the value of the key if it exists, otherwise returns -1.
     * @param key The key to lookup.
     * @return Value of the key if found, otherwise -1.
     */
    public int get(int key) {
        if (!keyToValueMap.containsKey(key)) return -1;

        // Update frequency
        updateFrequency(key);
        return keyToValueMap.get(key);
    }

    /**
     * Inserts or updates the key-value pair in the cache.
     * If the capacity is reached, it removes the least frequently used key.
     * @param key   The key to insert or update.
     * @param value The value to associate with the key.
     */
    public void put(int key, int value) {
        if (capacity == 0) return;

        // If key already exists, update value and frequency
        if (keyToValueMap.containsKey(key)) {
            keyToValueMap.put(key, value);
            updateFrequency(key);
            return;
        }

        // If cache is full, remove least frequently used key
        if (keyToValueMap.size() >= capacity) {
            removeLeastFrequentKey();
        }

        // Insert new key-value pair
        keyToValueMap.put(key, value);
        keyToFrequencyMap.put(key, 1);
        frequencyToKeysMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1; // Reset min frequency to 1 since a new key is added
    }

    /**
     * Updates the frequency of an accessed key.
     * @param key The key whose frequency is updated.
     */
    private void updateFrequency(int key) {
        int currentFrequency = keyToFrequencyMap.get(key);
        keyToFrequencyMap.put(key, currentFrequency + 1);

        // Remove key from old frequency bucket
        frequencyToKeysMap.get(currentFrequency).remove(key);
        if (frequencyToKeysMap.get(currentFrequency).isEmpty()) {
            frequencyToKeysMap.remove(currentFrequency);
            if (minFrequency == currentFrequency) {
                minFrequency++; // If removed, increase minFrequency
            }
        }

        // Add key to new frequency bucket
        frequencyToKeysMap.computeIfAbsent(currentFrequency + 1, k -> new LinkedHashSet<>()).add(key);
    }

    /**
     * Removes the least frequently used key.
     */
    private void removeLeastFrequentKey() {
        if (!frequencyToKeysMap.containsKey(minFrequency)) return;

        // Get the least frequently used key
        int keyToRemove = frequencyToKeysMap.get(minFrequency).iterator().next();
        frequencyToKeysMap.get(minFrequency).remove(keyToRemove);

        // Remove the key from all maps
        keyToValueMap.remove(keyToRemove);
        keyToFrequencyMap.remove(keyToRemove);

        // Cleanup if the frequency list becomes empty
        if (frequencyToKeysMap.get(minFrequency).isEmpty()) {
            frequencyToKeysMap.remove(minFrequency);
        }
    }
}

/**
 * Driver class to test LFU Cache functionality.
 */
public class LfuCacheTest {
    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // Output: 1
        
        cache.put(3, 3); // Evicts key 2
        System.out.println(cache.get(2)); // Output: -1 (not found)
        System.out.println(cache.get(3)); // Output: 3
        
        cache.put(4, 4); // Evicts key 1
        System.out.println(cache.get(1)); // Output: -1 (not found)
        System.out.println(cache.get(3)); // Output: 3
        System.out.println(cache.get(4)); // Output: 4
    }
}
