package stacksandqueues;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Problem: Design and implement a data structure for LFU (Least Frequently Used) Cache.
 *
 * Requirements:
 * - get(key): Retrieve the value of the key if it exists, otherwise return -1.
 * - put(key, value): Insert or update the value. If the cache reaches its capacity,
 *   it should evict the least frequently used item. If there's a tie, evict the least recently used.
 *
 * Example:
 * Input:
 *   LFUCache cache = new LFUCache(2);
 *   cache.put(1, 1);
 *   cache.put(2, 2);
 *   cache.get(1);       // returns 1
 *   cache.put(3, 3);    // evicts key 2
 *   cache.get(2);       // returns -1 (not found)
 *   cache.get(3);       // returns 3
 *   cache.put(4, 4);    // evicts key 1
 *   cache.get(1);       // returns -1 (not found)
 *   cache.get(3);       // returns 3
 *   cache.get(4);       // returns 4
 *
 * Leetcode Link:
 * https://leetcode.com/problems/lfu-cache/
 *
 * Follow-Up Questions:
 * - Can you implement LFU cache using a double linked list instead of LinkedHashSet?
 *   (Yes, it can give O(1) performance with more control over recency)
 * - What would you change if frequency updates were batched or delayed?
 *   (Use a lazy update mechanism and sync when accessed)
 * - What if frequent inserts and gets happen in high-concurrency systems?
 *   (Consider using ConcurrentHashMap + locking strategies or `java.util.concurrent` data structures)
 */
class LFUCache {
  private final int capacity; // Maximum capacity of the cache
  private int minFrequency; // Minimum frequency of any key in the cache
  private final Map<Integer, Integer> keyToValueMap; // Maps keys to their values
  private final Map<Integer, Integer> keyToFrequencyMap; // Maps keys to their frequency counts
  // Maps frequencies to sets of keys with that frequency. LinkedHashSet is used to maintain insertion order for LRU eviction.
  private final Map<Integer, LinkedHashSet<Integer>> frequencyToKeysMap;
  public LFUCache(int capacity) {
    this.capacity = capacity;
    this.minFrequency = 0;
    this.keyToValueMap = new HashMap<>();
    this.keyToFrequencyMap = new HashMap<>();
    this.frequencyToKeysMap = new HashMap<>();
  }

  /**
   * Inserts or updates a key-value pair in the cache.
   * If the cache exceeds its capacity, evicts the least frequently used item.
   *
   * Steps:
   * - If capacity is 0, return immediately.
   * - If key exists, update its value and frequency.
   * - If capacity is reached, evict the least frequently used and least recently used key.
   * - Insert new key with frequency 1 and reset minFrequency.
   *
   * Time Complexity: O(1)
   * Space Complexity: O(capacity)
   */
  public void put(int key, int value) {
      if (capacity == 0) {
          return;
      }

      // If key already exists, update its value and frequency
    if (keyToValueMap.containsKey(key)) {
      keyToValueMap.put(key, value);
      incrementFrequency(key);
      return;
    }

    // If key does not exist, check if we need to evict
    if (keyToValueMap.size() >= capacity) {
      evictLeastFrequentKey();
    }

    // Insert the new key with value and frequency 1
    keyToValueMap.put(key, value);
    keyToFrequencyMap.put(key, 1);
    frequencyToKeysMap.computeIfAbsent(1, unused -> new LinkedHashSet<>()).add(key);
    minFrequency = 1;
  }

  /**
   * Retrieves the value for the given key if it exists and updates its frequency.
   *
   * Steps:
   * - If key doesn't exist, return -1.
   * - Otherwise:
   *    - Update the frequency of the key.
   *    - Return the value associated with the key.
   *    - If the key is accessed, it becomes the most recently used for its frequency.
   *    - If the frequency set becomes empty and was the minimum frequency, increment minFrequency.
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   */
  public int get(int key) {
    if (!keyToValueMap.containsKey(key)) {
      return -1;
    }

    incrementFrequency(key);
    return keyToValueMap.get(key);
  }

  /**
   * Updates the frequency count for a key and moves it to the new frequency bucket.
   *
   * Steps:
   * - Retrieve current frequency and increment it.
   * - Remove the key from its current frequency set.
   * - update minFrequency if applicable.
   * - Add the key to the new frequency bucket.
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   */
  private void incrementFrequency(int key) {
    int currentFreq = keyToFrequencyMap.get(key);
    int updatedFreq = currentFreq + 1;
    keyToFrequencyMap.put(key, updatedFreq);

    // Remove from current frequency bucket because frequency is being updated
    LinkedHashSet<Integer> keysAtCurrentFreq = frequencyToKeysMap.get(currentFreq);
    keysAtCurrentFreq.remove(key);

    // If the current frequency set is empty, remove it from the map
    if (keysAtCurrentFreq.isEmpty()) {
      frequencyToKeysMap.remove(currentFreq);
      if (minFrequency == currentFreq) {
        // we are sure that minFrequency + 1 is be available because input key is now updatedFreq (minFrequency + 1)
        minFrequency++;
      }
    }

    // Add to updated frequency bucket
    frequencyToKeysMap.computeIfAbsent(updatedFreq, unused -> new LinkedHashSet<>()).add(key);
  }

  /**
   * Evicts the least frequently used key from the cache.
   * If there's a tie, the least recently used key is evicted (based on LinkedHashSet insertion order).
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   */
  private void evictLeastFrequentKey() {
    LinkedHashSet<Integer> keysAtMinFreq = frequencyToKeysMap.get(minFrequency);
    int keyToRemove = keysAtMinFreq.iterator().next();

    // Remove the key from all maps
    keysAtMinFreq.remove(keyToRemove);
    if (keysAtMinFreq.isEmpty()) {
      frequencyToKeysMap.remove(minFrequency);
    }

    keyToValueMap.remove(keyToRemove);
    keyToFrequencyMap.remove(keyToRemove);
  }
}

/**
 * Driver code to test LFU Cache implementation.
 */
public class LfuCacheTest {
  public static void main(String[] args) {
    LFUCache cache = new LFUCache(2);

    cache.put(1, 1);
    cache.put(2, 2);
    System.out.println(cache.get(1)); // returns 1

    cache.put(3, 3); // evicts key 2
    System.out.println(cache.get(2)); // returns -1
    System.out.println(cache.get(3)); // returns 3

    cache.put(4, 4); // evicts key 1
    System.out.println(cache.get(1)); // returns -1
    System.out.println(cache.get(3)); // returns 3
    System.out.println(cache.get(4)); // returns 4
  }
}