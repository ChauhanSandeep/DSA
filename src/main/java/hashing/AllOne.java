package hashing;

import java.util.*;

/**
 * Problem: All O`one Data Structure
 *
 * Design a structure that tracks string counts and can return any key with the
 * current maximum or minimum count. Increment, decrement, max lookup, and min
 * lookup must all run in average O(1) time.
 *
 * Leetcode: https://leetcode.com/problems/all-oone-data-structure/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | Doubly linked list | Frequency buckets
 *
 * Example:
 *   Input:  inc("hello"), inc("hello"), inc("world"), getMaxKey(), getMinKey()
 *   Output: "hello", "world"
 *   Why:    hello has count 2 and world has count 1, so they are the unique max
 *           and min keys.
 *
 * Follow-ups:
 *   1. How would you make the structure thread-safe?
 *      Protect bucket moves and map updates with one lock or finer-grained bucket locks.
 *   2. How would you return all max-count keys instead of one arbitrary key?
 *      Return a copy or view of the key set stored in the tail bucket.
 *   3. How would you support top-k keys?
 *      Walk buckets from the tail until k keys are collected, or maintain extra top-k state.
 *   4. What changes if ordered min/max by key is required?
 *      Store keys inside each bucket in a TreeSet, trading O(1) arbitrary choice for ordering cost.
 *
 * Related: LRU Cache (146), LFU Cache (460), Maximum Frequency Stack (895).
 */
public class AllOne {

    public static void main(String[] args) {
        AllOne empty = new AllOne();
        System.out.printf("input=%s -> max=%s min=%s  expected=max= min=%n",
            "[]", empty.getMaxKey(), empty.getMinKey());

        AllOne allOne = new AllOne();
        allOne.inc("hello");
        allOne.inc("hello");
        allOne.inc("world");
        System.out.printf("input=%s -> max=%s min=%s  expected=max=hello min=world%n",
            "[inc(hello), inc(hello), inc(world)]", allOne.getMaxKey(), allOne.getMinKey());

        allOne.dec("world");
        System.out.printf("input=%s -> max=%s min=%s  expected=max=hello min=hello%n",
            "[dec(world)]", allOne.getMaxKey(), allOne.getMinKey());
    }

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
     * Intuition: each key lives in exactly one frequency bucket. Incrementing a
     * key only moves it one bucket to the right, so the structure either reuses
     * the adjacent count+1 bucket or creates that bucket in place.
     *
     * Algorithm:
     *   1. If the key is new, put it in the count-1 bucket after the head.
     *   2. Otherwise move it from its current bucket to the adjacent count+1 bucket.
     *   3. Remove the old bucket when it becomes empty.
     *
     * Time:  O(1) - map lookup plus constant linked-list updates.
     * Space: O(1) - no extra space beyond the stored key and possible new bucket.
     *
     * @param key string whose count is increased by one
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
     * Intuition: decrement is the mirror of increment. A key either disappears
     * when its count reaches zero, or moves one bucket to the left into the
     * adjacent count-1 bucket.
     *
     * Algorithm:
     *   1. Read the key's current bucket and compute the decremented count.
     *   2. Remove the key entirely if the new count is zero.
     *   3. Otherwise move it to the previous count bucket, creating that bucket if needed.
     *   4. Delete the old bucket if it has no remaining keys.
     *
     * Time:  O(1) - map lookup plus constant linked-list updates.
     * Space: O(1) - no extra space beyond a possible adjacent bucket.
     *
     * @param key existing string whose count is decreased by one
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
     * Intuition: buckets stay sorted by count, so the bucket before the tail
     * sentinel always stores keys with the maximum count.
     *
     * Algorithm:
     *   1. Return an empty string when the list has no real buckets.
     *   2. Otherwise return any key from the tail-side bucket.
     *
     * Time:  O(1) - reads one bucket and one key iterator.
     * Space: O(1) - no auxiliary data structures are allocated.
     *
     * @return any key with the maximum count, or an empty string when empty
     */
    public String getMaxKey() {
        if (tail.prev == head) {
            return "";  // No keys exist
        }
        return tail.prev.keys.stream().findAny().get();
    }

        /**
     * Intuition: buckets stay sorted by count, so the bucket after the head
     * sentinel always stores keys with the minimum count.
     *
     * Algorithm:
     *   1. Return an empty string when the list has no real buckets.
     *   2. Otherwise return any key from the head-side bucket.
     *
     * Time:  O(1) - reads one bucket and one key iterator.
     * Space: O(1) - no auxiliary data structures are allocated.
     *
     * @return any key with the minimum count, or an empty string when empty
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
