package StackQueue;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

// Queue used in this will cause O(n) time complexity to fetch a item (To remove a item etc)
// Check LruCacheTestImproved
class LruCache {
    LinkedList<Node> lruQueue = new LinkedList<>();
    Map<Integer, Node> cacheMap = new HashMap<>();
    int capacity;

    public LruCache(int capacity) {
        this.capacity = capacity;
    }

    /**
     * To get value from cache
     * @param key
     * @return
     */
    public int get(int key) {
        if(!cacheMap.containsKey(key)) return -1;

        Node node = cacheMap.get(key);
        lruQueue.remove(node);
        lruQueue.addFirst(node);
        return node.value;
    }

    /**
     * To add the key, value to cache
     * @param key
     * @param value
     */
    public void set(int key, int value) {
        if(!cacheMap.containsKey(key)) {
            Node node = new Node(key, value);
            lruQueue.addFirst(node);
            cacheMap.put(key, node);
        }else {
            Node node = cacheMap.get(key);
            lruQueue.remove(node);
            node.value = value;
            lruQueue.addFirst(node);
            cacheMap.put(key, node);
        }

        int currCapacity = cacheMap.size();
        while(currCapacity > capacity) {
            Node node = lruQueue.pollLast();
            cacheMap.remove(node.key);
            currCapacity--;
        }
    }


}
