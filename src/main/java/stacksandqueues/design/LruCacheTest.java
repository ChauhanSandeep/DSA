package stacksandqueues.design;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: LRU Cache
 *
 * Design a cache with get and put in O(1) average time. When capacity is
 * exceeded, remove the least recently used key, while every successful get or
 * put makes that key the most recently used.
 *
 * Leetcode: https://leetcode.com/problems/lru-cache/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Design | Hash map | Doubly linked list
 *
 * Example:
 *   Input:  capacity 2, put(1,1), put(2,2), get(1), put(3,3), get(2)
 *   Output: [1,-1]
 *   Why:    get(1) makes key 1 recent, so put(3,3) evicts key 2.
 *
 * Follow-ups:
 *   1. Make the cache thread-safe?
 *      Protect the map and linked list with the same lock or a carefully designed concurrent policy.
 *   2. Add TTL expiration?
 *      Store expiry timestamps and purge expired nodes before normal LRU eviction.
 *   3. Support LFU instead of LRU?
 *      Add frequency buckets and evict from the minimum-frequency bucket.
 *   4. Persist the cache after restart?
 *      Serialize entries in recency order and rebuild the map and list on startup.
 *
 * Related: LFU Cache (460), Design HashMap (706), All Oone Data Structure (432).
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
   * Intuition: the hash map finds the node in O(1), and the linked list records
   * recency. A successful read moves that node to the head so future evictions
   * remove older nodes first.
   *
   * Algorithm:
   *   1. Return -1 when key is absent from keyNodeMap.
   *   2. Read the mapped node.
   *   3. Move that node to the head of usageOrderList.
   *   4. Return node.value.
   *
   * Time:  O(1) - map lookup and linked-list relink are constant time.
   * Space: O(1) - no additional storage grows during get.
   *
   * @param key key to read
   * @return value for key, or -1 if absent
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
   * Intuition: updating an existing key is also a recent use, so that node moves
   * to the head. A new key is inserted at the head; if capacity is exceeded, the
   * tail-side real node is the least recently used and is removed from both data
   * structures.
   *
   * Algorithm:
   *   1. If key exists, update its value and move its node to the head.
   *   2. Otherwise create a new node, add it to the head, and put it in keyNodeMap.
   *   3. If the map size exceeds capacity, removeTail and delete that key from the map.
   *
   * Time:  O(1) - hash map operations and list relinks are constant time.
   * Space: O(capacity) - the map and list store one node per cached key.
   *
   * @param key key to insert or update
   * @param value value to store
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

    /** Unlinks a node from its current list position. */

  private void removeNode(DoublyListNode node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }
}

public class LruCacheTest {
    public static void main(String[] args) {
    LRUCache cache = new LRUCache(2);
    cache.put(1, 1);
    cache.put(2, 2);
    int first = cache.get(1);
    cache.put(3, 3);
    int second = cache.get(2);
    cache.put(4, 4);
    int third = cache.get(1);
    int fourth = cache.get(3);
    int fifth = cache.get(4);

    int[] got = { first, second, third, fourth, fifth };
    int[] expected = { 1, -1, -1, 3, 4 };
    System.out.printf("ops=%s -> %s  expected=%s%n",
        "put(1),put(2),get(1),put(3),get(2),put(4),get(1),get(3),get(4)",
        Arrays.toString(got), Arrays.toString(expected));
  }
}