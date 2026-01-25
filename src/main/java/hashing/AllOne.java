package hashing;

import java.util.*;

/**
 * AllOne.java
 *
 * Problem Statement:
 * Design a data structure to store strings' count with the ability to return the strings 
 * with minimum and maximum counts. Implement the AllOne class:
 * - AllOne(): Initializes the object of the data structure
 * - inc(String key): Increments the count of the string key by 1. If key does not exist, insert it with count 1
 * - dec(String key): Decrements the count of the string key by 1. If count becomes 0, remove it. Assumes key exists
 * - getMaxKey(): Returns one of the keys with the maximal count. If no element exists, return empty string
 * - getMinKey(): Returns one of the keys with the minimum count. If no element exists, return empty string
 *
 * All operations must be O(1) average time complexity.
 *
 * Example:
 * AllOne allOne = new AllOne();
 * allOne.inc("hello");       // "hello" count = 1
 * allOne.inc("hello");       // "hello" count = 2
 * allOne.getMaxKey();        // return "hello"
 * allOne.getMinKey();        // return "hello"
 * allOne.inc("world");       // "world" count = 1
 * allOne.getMinKey();        // return "world"
 * allOne.dec("hello");       // "hello" count = 1
 * allOne.getMaxKey();        // return "hello" or "world" (both have count 1)
 *
 * LeetCode link: https://leetcode.com/problems/all-oone-data-structure/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - How would you handle concurrent access to this data structure?
 *    → Add synchronization/locks on critical sections or use concurrent data structures.
 *  - What if we need to support range queries (keys with count in range [min, max])?
 *    → Maintain additional TreeMap mapping counts to keys for range queries.
 *  - Can you extend this to support top-K frequent keys efficiently?
 *    → Maintain pointer to kth bucket from tail, update on each operation.
 *  - How would memory usage scale with millions of keys?
 *    → Current design uses O(n) space; could optimize by using arrays instead of HashSets for buckets.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 146 (LRU Cache): https://leetcode.com/problems/lru-cache/
 *  - LeetCode 460 (LFU Cache): https://leetcode.com/problems/lfu-cache/
 *  - LeetCode 895 (Maximum Frequency Stack): https://leetcode.com/problems/maximum-frequency-stack/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class AllOne {

    /**
     * Main solution: Doubly Linked List of frequency buckets with HashMap.
     * 
     * Data Structure Design:
     *  - Doubly Linked List: Each node (bucket) represents a frequency count
     *    - Buckets are ordered by count (ascending)
     *    - Each bucket contains set of keys with that count
     *  - HashMap: Maps each key to its bucket node for O(1) access
     *  - Sentinel nodes: Head and tail sentinels simplify edge case handling
     *
     * Key Operations:
     *  - inc(key): Move key to next bucket (count+1), create bucket if needed
     *  - dec(key): Move key to previous bucket (count-1), remove if count becomes 0
     *  - getMaxKey(): Return any key from tail bucket (highest count)
     *  - getMinKey(): Return any key from head bucket (lowest count)
     *
     * Algorithm: Doubly Linked List with HashMap.
     * Time Complexity: O(1) for all operations (inc, dec, getMaxKey, getMinKey).
     * Space Complexity: O(n) where n is number of unique keys.
     */
    
    private Node head;  // Sentinel head (min count side)
    private Node tail;  // Sentinel tail (max count side)
    private Map<String, Node> keyToNodeMap;  // Maps key to its bucket

    public AllOne() {
        // Initialize with sentinel nodes
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
        keyToNodeMap = new HashMap<>();
    }

    /**
     * Increments the count of the string key by 1. If key does not exist, insert it with count 1.
     */
    public void inc(String key) {
        if (!keyToNodeMap.containsKey(key)) {
            // Key doesn't exist - add to bucket with count 1
            Node firstBucket = head.next;
            Node targetBucket;
            
            if (firstBucket.count == 1) {
                // Reuse existing bucket with count 1
                targetBucket = firstBucket;
            } else {
                // Create new bucket with count 1
                targetBucket = new Node(1);
                insertAfter(head, targetBucket);
            }
            
            targetBucket.keys.add(key);
            keyToNodeMap.put(key, targetBucket);
        } else {
            // Key exists - increment count
            Node currentBucket = keyToNodeMap.get(key);
            int newCount = currentBucket.count + 1;
            Node nextBucket = currentBucket.next;
            
            if (nextBucket.count == newCount) {
                // Next bucket has target count
                nextBucket.keys.add(key);
                keyToNodeMap.put(key, nextBucket);
            } else {
                // Create new bucket with incremented count
                Node newBucket = new Node(newCount);
                newBucket.keys.add(key);
                insertAfter(currentBucket, newBucket);
                keyToNodeMap.put(key, newBucket);
            }
            
            // Remove key from current bucket
            currentBucket.keys.remove(key);
            if (currentBucket.keys.isEmpty()) {
                removeBucket(currentBucket);
            }
        }
    }

    /**
     * Decrements the count of the string key by 1. If count becomes 0, remove it.
     * Assumes key exists.
     */
    public void dec(String key) {
        Node currentBucket = keyToNodeMap.get(key);
        int newCount = currentBucket.count - 1;
        
        if (newCount == 0) {
            // Count becomes 0 - remove key entirely
            keyToNodeMap.remove(key);
        } else {
            // Decrement count
            Node prevBucket = currentBucket.prev;
            
            if (prevBucket.count == newCount) {
                // Previous bucket has target count
                prevBucket.keys.add(key);
                keyToNodeMap.put(key, prevBucket);
            } else {
                // Create new bucket with decremented count
                Node newBucket = new Node(newCount);
                newBucket.keys.add(key);
                insertAfter(prevBucket, newBucket);
                keyToNodeMap.put(key, newBucket);
            }
        }
        
        // Remove key from current bucket
        currentBucket.keys.remove(key);
        if (currentBucket.keys.isEmpty()) {
            removeBucket(currentBucket);
        }
    }

    /**
     * Returns one of the keys with the maximum count.
     * If no keys exist, returns an empty string.
     */
    public String getMaxKey() {
        if (tail.prev == head) {
            return "";  // No keys exist
        }
        return tail.prev.keys.stream().findAny().get();
    }

    /**
     * Returns one of the keys with the minimum count.
     * If no keys exist, returns an empty string.
     */
    public String getMinKey() {
        if (head.next == tail) {
            return "";  // No keys exist
        }
        return head.next.keys.stream().findAny().get();
    }

    // Helper: Insert new bucket after given bucket
    private void insertAfter(Node bucket, Node newBucket) {
        newBucket.prev = bucket;
        newBucket.next = bucket.next;
        bucket.next.prev = newBucket;
        bucket.next = newBucket;
    }

    // Helper: Remove bucket from linked list
    private void removeBucket(Node bucket) {
        bucket.prev.next = bucket.next;
        bucket.next.prev = bucket.prev;
    }

    /**
     * Node class: Represents a frequency bucket in the doubly linked list.
     */
    static class Node {
        int count;              // Frequency count for this bucket
        Set<String> keys;       // Keys with this count
        Node prev;              // Previous bucket (lower count)
        Node next;              // Next bucket (higher count)
        
        Node(int count) {
            this.count = count;
            this.keys = new HashSet<>();
        }
    }
}

/**
 * Alternative solution: Using TreeMap for frequency buckets.
 * 
 * Data Structure Design:
 *  - TreeMap<Integer, Set<String>>: Maps count to set of keys with that count
 *    - TreeMap maintains sorted order for O(log n) access to min/max
 *  - HashMap<String, Integer>: Maps each key to its current count
 *
 * Trade-offs:
 *  - Simpler implementation than doubly linked list
 *  - O(log n) for inc/dec operations (TreeMap operations)
 *  - O(log n) for getMaxKey/getMinKey (TreeMap firstKey/lastKey)
 *  - Not true O(1) but acceptable for many use cases
 *
 * Algorithm: TreeMap-based frequency tracking.
 * Time Complexity: O(log n) for all operations.
 * Space Complexity: O(n) where n is number of unique keys.
 */
class AllOneTreeMap {
    
    private TreeMap<Integer, Set<String>> countToKeys;  // Count -> keys with that count
    private Map<String, Integer> keyToCount;            // Key -> its current count

    public AllOneTreeMap() {
        countToKeys = new TreeMap<>();
        keyToCount = new HashMap<>();
    }

    public void inc(String key) {
        int oldCount = keyToCount.getOrDefault(key, 0);
        int newCount = oldCount + 1;
        
        // Remove from old count bucket
        if (oldCount > 0) {
            countToKeys.get(oldCount).remove(key);
            if (countToKeys.get(oldCount).isEmpty()) {
                countToKeys.remove(oldCount);
            }
        }
        
        // Add to new count bucket
        countToKeys.computeIfAbsent(newCount, k -> new HashSet<>()).add(key);
        keyToCount.put(key, newCount);
    }

    public void dec(String key) {
        int oldCount = keyToCount.get(key);
        int newCount = oldCount - 1;
        
        // Remove from old count bucket
        countToKeys.get(oldCount).remove(key);
        if (countToKeys.get(oldCount).isEmpty()) {
            countToKeys.remove(oldCount);
        }
        
        if (newCount == 0) {
            // Remove key entirely
            keyToCount.remove(key);
        } else {
            // Add to new count bucket
            countToKeys.computeIfAbsent(newCount, k -> new HashSet<>()).add(key);
            keyToCount.put(key, newCount);
        }
    }

    public String getMaxKey() {
        if (countToKeys.isEmpty()) {
            return "";
        }
        return countToKeys.lastEntry().getValue().stream().findAny().get();
    }

    public String getMinKey() {
        if (countToKeys.isEmpty()) {
            return "";
        }
        return countToKeys.firstEntry().getValue().stream().findAny().get();
    }
}
