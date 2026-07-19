package stacksandqueues.design;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;


/**
 * Problem: LFU Cache
 *
 * Design a cache with get and put in constant time on average. When full, evict
 * the least frequently used key; if multiple keys share the same frequency,
 * evict the least recently used key among that frequency bucket.
 *
 * Leetcode: https://leetcode.com/problems/lfu-cache/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Design | Hash maps | Frequency buckets with LRU tie-break
 *
 * Example:
 *   Input:  capacity 2, put(1,1), put(2,2), get(1), put(3,3), get(2), get(3)
 *   Output: [1,-1,3]
 *   Why:    get(1) raises key 1's frequency, so put(3,3) evicts less-frequent key 2.
 *
 * Follow-ups:
 *   1. Replace LinkedHashSet with a custom doubly linked list?
 *      Keep one list per frequency and store node pointers for O(1) removals.
 *   2. Add TTL expiration?
 *      Track expiry timestamps and lazily purge expired keys before normal LFU eviction.
 *   3. Make it thread-safe?
 *      Guard all maps and frequency buckets with one lock or use carefully ordered striped locks.
 *   4. Support weighted cache entries?
 *      Evict repeatedly or by score until enough capacity weight is available.
 *
 * Related: LRU Cache (146), All Oone Data Structure (432), Design In-Memory File System (588).
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
   * Intuition: a new key always starts at frequency 1, while an existing key is
   * an access and must move to the next frequency bucket. If the cache is full,
   * minFrequency identifies the lowest bucket, and LinkedHashSet iteration gives
   * the least recently used key inside that bucket.
   *
   * Algorithm:
   *   1. Return immediately when capacity is 0.
   *   2. If key exists, update its value and call incrementFrequency.
   *   3. If full, evictLeastFrequentKey from the minFrequency bucket.
   *   4. Insert the new key with frequency 1 and set minFrequency to 1.
   *
   * Time:  O(1) - hash maps and LinkedHashSet updates are constant time on average.
   * Space: O(capacity) - each key appears in the maps and one frequency bucket.
   *
   * @param key cache key
   * @param value value to insert or update
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
   * Intuition: reading a present key is also a use, so the key must move from
   * its current frequency bucket to the next one before its value is returned.
   * Missing keys do not affect cache state.
   *
   * Algorithm:
   *   1. Return -1 if keyToValueMap does not contain key.
   *   2. Call incrementFrequency to move the key to its next frequency bucket.
   *   3. Return the stored value.
   *
   * Time:  O(1) - map lookup and frequency movement are constant time on average.
   * Space: O(1) - no extra storage grows with the operation.
   *
   * @param key key to read
   * @return value for key, or -1 if absent
   */

  public int get(int key) {
    if (!keyToValueMap.containsKey(key)) {
      return -1;
    }

    incrementFrequency(key);
    return keyToValueMap.get(key);
  }

    /** Moves a key from its current frequency bucket to the next bucket. */

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
        // we are sure that minFrequency + 1 will be available because input key is now updatedFreq (minFrequency + 1)
        minFrequency++;
      }
    }

    // Add to updated frequency bucket
    frequencyToKeysMap.computeIfAbsent(updatedFreq, unused -> new LinkedHashSet<>()).add(key);
  }

    /** Removes the least recently used key from the current minimum-frequency bucket. */

  private void evictLeastFrequentKey() {
    LinkedHashSet<Integer> keysWithMinFreq = frequencyToKeysMap.get(minFrequency);
    int keyToRemove = keysWithMinFreq.stream().findFirst().get(); // Get the least recently used key

    // Remove the key from all maps
    keysWithMinFreq.remove(keyToRemove);
    if (keysWithMinFreq.isEmpty()) {
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
    int first = cache.get(1);
    cache.put(3, 3);
    int second = cache.get(2);
    int third = cache.get(3);
    cache.put(4, 4);
    int fourth = cache.get(1);
    int fifth = cache.get(3);
    int sixth = cache.get(4);

    int[] got = { first, second, third, fourth, fifth, sixth };
    int[] expected = { 1, -1, 3, -1, 3, 4 };
    System.out.printf("ops=%s -> %s  expected=%s%n",
        "put(1),put(2),get(1),put(3),get(2),get(3),put(4),get(1),get(3),get(4)",
        Arrays.toString(got), Arrays.toString(expected));
  }
}