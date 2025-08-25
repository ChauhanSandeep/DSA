package stacksandqueues;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Design a data structure that behaves like an LRU (Least Recently Used) cache.
 * Each operation (get/put) must run in O(1) time complexity.
 *
 * Approach:
 * - Use a HashMap to store key -> Node mappings for constant-time access.
 * - Use a Doubly Linked List to track usage order (most recently used at the front).
 * - When capacity is exceeded, evict the least recently used node (tail.prev).
 *
 * Time Complexity:
 * - get(key): O(1)
 * - put(key, value): O(1)
 *
 * Space Complexity: O(capacity)
 *
 * LeetCode Link: https://leetcode.com/problems/lru-cache/
 */
public class LruCacheTestImproved {
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

/**
 * LRU Cache class using HashMap + Doubly Linked List.
 */
class LRUCache {
  private final int capacity; // Maximum capacity of the cache
  private final Map<Integer, DoublyListNode> keyNodeMap; // mapping between key and node (for O(1) access)
  private final DoublyLinkedList usageList; // Doubly linked list to maintain usage order

  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.keyNodeMap = new HashMap<>();
    this.usageList = new DoublyLinkedList();
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
    usageList.moveToHead(node); // Mark as recently used
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
      usageList.moveToHead(existingNode); // Mark as recently used
    } else {
      // New key, create a new node and add to the head
      DoublyListNode newNode = new DoublyListNode(key, value);
      usageList.addToHead(newNode);
      keyNodeMap.put(key, newNode);

      if (keyNodeMap.size() > capacity) {
        DoublyListNode lruNode = usageList.removeTail(); // Evict LRU
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