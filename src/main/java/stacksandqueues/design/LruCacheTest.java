package stacksandqueues.design;

import java.util.HashMap;
import java.util.Map;

/**
 * LRU Cache - LeetCode Problem 146
 *
 * Problem Statement:
 * Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.
 * Implement the LRUCache class with get(key) and put(key, value) operations.
 * When capacity is reached, invalidate the least recently used item before inserting new item.
 * Both operations must run in O(1) average time complexity.
 *
 * Example:
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // cache is {1=1}
 * lRUCache.put(2, 2); // cache is {1=1, 2=2}
 * lRUCache.get(1);    // return 1, cache is {2=2, 1=1} (1 moved to most recent)
 * lRUCache.put(3, 3); // evicts key 2, cache is {1=1, 3=3}
 *
 * LeetCode Link: https://leetcode.com/problems/lru-cache/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you implement a thread-safe LRU cache for concurrent access?
 *    Answer: Use ConcurrentHashMap + synchronized blocks, or lock-free algorithms with atomic operations.
 *    Related: Design thread-safe data structures
 *
 * 2. What if we need LFU (Least Frequently Used) instead of LRU?
 *    Answer: Add frequency counter to nodes, maintain frequency-based ordering.
 *    Related: LeetCode 460 - https://leetcode.com/problems/lfu-cache/
 *
 * 3. How to implement cache with TTL (Time To Live) expiration?
 *    Answer: Add timestamp to nodes, background cleanup thread for expired entries.
 *
 * 4. What if we need to persist cache to disk for recovery?
 *    Answer: Implement write-through or write-back policies with serialization.
 *    Related: Design distributed cache systems
 */
class LRUCache {
  private final int capacity; // Maximum capacity of the cache
  private final Map<Integer, DoublyListNode> keyNodeMap; // mapping between key and node (for O(1) access)
  private final DoublyLinkedList usageOrderList; // Doubly linked list to maintain usage order

  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.keyNodeMap = new HashMap<>();
    this.usageOrderList = new DoublyLinkedList();
  }

  /**
   * Fetch value for the given key.
   * If key exists, move the corresponding node to the head (most recently used).
   */
  public int get(int key) {
    if (!keyNodeMap.containsKey(key)) {
      return -1;
    }

    DoublyListNode node = keyNodeMap.get(key);
    usageOrderList.moveToHead(node); // Mark as recently used
    return node.value;
  }

  /**
   * Insert or update the (key, value) pair.
   * Move the node to the head if it already exists.
   * If the key is new and capacity is exceeded, evict the least recently used node.
   */
  public void put(int key, int value) {
    if (keyNodeMap.containsKey(key)) {
      // Key already exists, update value and move to head
      DoublyListNode existingNode = keyNodeMap.get(key);
      existingNode.value = value; // Update value
      usageOrderList.moveToHead(existingNode); // Mark as recently used
    } else {
      // New key, create a new node and add to the head
      DoublyListNode newNode = new DoublyListNode(key, value);
      usageOrderList.addToHead(newNode);
      keyNodeMap.put(key, newNode);

      if (keyNodeMap.size() > capacity) {
        DoublyListNode lruNode = usageOrderList.removeTail(); // Evict LRU
        keyNodeMap.remove(lruNode.key);
      }
    }
  }
}

/**
 * Doubly Linked List Node class for LRU Cache.
 */
class DoublyListNode {
  int key;
  int value;
  DoublyListNode prev;
  DoublyListNode next;

  public DoublyListNode(int key, int value) {
    this.key = key;
    this.value = value;
  }
}

/**
 * Doubly Linked List to maintain usage order.
 * Most recently used node is near the head.
 * Least recently used node is near the tail.
 */
class DoublyLinkedList {
  private final DoublyListNode head;
  private final DoublyListNode tail;

  public DoublyLinkedList() {
    head = new DoublyListNode(-1, -1); // Dummy head
    tail = new DoublyListNode(-1, -1); // Dummy tail
    head.next = tail;
    tail.prev = head;
  }

  /**
   * Add node right after dummy head (most recently used position).
   */
  public void addToHead(DoublyListNode node) {
    node.next = head.next;
    node.prev = head;

    head.next.prev = node;
    head.next = node;
  }

  /**
   * Move an existing node to the front of the list (most recently used).
   */
  public void moveToHead(DoublyListNode node) {
    removeNode(node);
    addToHead(node);
  }

  /**
   * Remove the least recently used node (before dummy tail) and return it.
   */
  public DoublyListNode removeTail() {
    if (tail.prev == head) {
      return null; // List is empty
    }

    DoublyListNode lruNode = tail.prev;
    removeNode(lruNode);
    return lruNode;
  }

  /**
   * Remove a node from its current position in the list.
   */
  private void removeNode(DoublyListNode node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }
}

public class LruCacheTest {
  public static void main(String[] args) {
    LRUCache cache = new LRUCache(2);

    cache.put(1, 1); // Cache = [1]
    cache.put(2, 2); // Cache = [2, 1]
    System.out.println(cache.get(1)); // Output: 1 (Cache = [1, 2])

    cache.put(3, 3); // Evicts key 2, Cache = [3, 1]
    System.out.println(cache.get(2)); // Output: -1 (not found)

    cache.put(4, 4); // Evicts key 1, Cache = [4, 3]
    System.out.println(cache.get(1)); // Output: -1
    System.out.println(cache.get(3)); // Output: 3
    System.out.println(cache.get(4)); // Output: 4
  }
}