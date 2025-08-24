package hashing;

import java.util.*;


/**
 * ✅ Problem: All O`one Data Structure
 * 🔗 LeetCode: https://leetcode.com/problems/all-oone-data-structure/
 *
 * Design a data structure to:
 *  - Increment a string key's frequency in O(1)
 *  - Decrement frequency in O(1)
 *  - Retrieve any key with max/min frequency in O(1)
 *
 * 🎯 Use Case Example:
 *    AllOne ao = new AllOne();
 *    ao.inc("hello");
 *    ao.inc("hello");
 *    ao.inc("world");
 *    ao.getMaxKey(); // "hello"
 *    ao.getMinKey(); // "world"
 *
 * 🧠 Core Idea:
 *    - Doubly Linked List of frequency buckets (each holds keys with same frequency)
 *    - HashMap maps keys to their bucket node
 *    - Maintain head (max freq) and tail (min freq) sentinels
 *    - While incrementing/decrementing:
 *      1. Find current node
 *      2. Create or find next/prev frequency node
 *      3. Move key to new node
 *      4. Remove key from old node
 *      5. Clean up empty nodes
 *    - While retrieving max/min key:
 *      1. Return any key from head.prev (max) or tail.next (min) node
 *
 * Time: O(1) for all operations
 * Space: O(N), where N is number of keys
 *
 * 🔄 Follow-ups:
 *    - How would you return all keys with max frequency?
 *    Answer: Iterate through the max frequency node's keys.
 *    - What if frequency updates needed to support batching?
 *    Answer: Use a queue to batch updates and process them in O(1) amortized time.
 */
public class AllOne {

  /**
   * Each node represents a frequency bucket and holds a set of keys with the same frequency.
   */
  private static class Node {
    int frequency; // Frequency of the keys
    Set<String> keys; // Stores keys with the same frequency
    Node prev, next;  // Pointers to previous and next nodes

    Node(int frequency) {
      this.frequency = frequency;
      this.keys = new LinkedHashSet<>(); // Maintain insertion order
    }
  }

  private final Map<String, Node> keyToNodeMap;
  private final Node head; // Highest frequency sentinel (head.prev is max)
  private final Node tail; // Lowest frequency sentinel (tail.next is min)

  public AllOne() {
    keyToNodeMap = new HashMap<>();
    head = new Node(-1); // Dummy head sentinel
    tail = new Node(-1); // Dummy tail sentinel
    head.prev = tail;
    tail.next = head;
  }

  /**
   * Increments the frequency of a key by 1.
   *
   * Steps:
   * 1. If key doesn't exist, create a new node for frequency 1.
   * 2. If key exists, find its current node.
   * 3. If next frequency node exists, use it; otherwise create a new node for the incremented frequency.
   * 4. Move the key to the new node and remove it from the old node.
   *
   * * Time Complexity: O(1) for all operations
   * * Space Complexity: O(N) for storing keys and nodes
   */
  public void inc(String key) {
    if (!keyToNodeMap.containsKey(key)) {
      // First time insertion
      Node node;
      if (tail.next.frequency == 1) {
        node = tail.next;
      } else {
        node = new Node(1);
      }
      if (tail.next.frequency != 1) {
        insertAfter(tail, node);
      }
      node.keys.add(key);
      keyToNodeMap.put(key, node);
    } else {
      Node curr = keyToNodeMap.get(key);
      Node next = (curr.next.frequency == curr.frequency + 1) ? curr.next : new Node(curr.frequency + 1);
      if (curr.next.frequency != curr.frequency + 1) {
        insertAfter(curr, next);
      }
      next.keys.add(key);
      keyToNodeMap.put(key, next);
      removeKeyFromNode(curr, key);
    }
  }

  /**
   * Decrements the frequency of a key by 1. Removes key if frequency hits 0.
   *
   * Steps:
   * 1. If key doesn't exist, do nothing.
   * 2. Find the current node for the key.
   * 3. If frequency is 1, remove the key from the map.
   * 4. If frequency > 1, find or create the previous frequency node.
   * 5. Move the key to the previous frequency node and remove it from the current node.
   *
   * Time Complexity: O(1) for all operations
   * Space Complexity: O(N) for storing keys and nodes
   */
  public void dec(String key) {
    if (!keyToNodeMap.containsKey(key)) {
      return;
    }

    Node curr = keyToNodeMap.get(key);
    if (curr.frequency == 1) {
      // Frequency becomes 0 → remove
      keyToNodeMap.remove(key);
    } else {
      Node prev = (curr.prev.frequency == curr.frequency - 1) ? curr.prev : new Node(curr.frequency - 1);
      if (curr.prev.frequency != curr.frequency - 1) {
        insertAfter(curr.prev, prev);
      }
      prev.keys.add(key);
      keyToNodeMap.put(key, prev);
    }
    removeKeyFromNode(curr, key);
  }

  /**
   * Returns any key with the highest frequency.
   *
   * Steps:
   * 1. If the list is empty (head.prev == tail), return an empty string.
   * 2. Otherwise, return any key from the max frequency node (head.prev).
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   */
  public String getMaxKey() {
    return (head.prev == tail) ? "" : head.prev.keys.iterator().next();
  }

  /**
   * Returns any key with the lowest frequency.
   *
   * Steps:
   * 1. If the list is empty (tail.next == head), return an empty string.
   * 2. Otherwise, return any key from the min frequency node (tail.next).
   *
   * Time Complexity: O(1)
   * Space Complexity: O(1)
   */
  public String getMinKey() {
    return (tail.next == head) ? "" : tail.next.keys.iterator().next();
  }

  /**
   * Inserts `newNode` right after `existing`.
   */
  private void insertAfter(Node existing, Node newNode) {
    newNode.next = existing.next;
    newNode.prev = existing;
    existing.next.prev = newNode;
    existing.next = newNode;
  }

  /**
   * Removes a node from the doubly linked list.
   */
  private void deleteNode(Node node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  /**
   * Removes the given key from the node. If node becomes empty, delete it.
   */
  private void removeKeyFromNode(Node node, String key) {
    node.keys.remove(key);
    if (node.keys.isEmpty()) {
      deleteNode(node);
    }
  }

  /**
   * Example Usage.
   */
  public static void main(String[] args) {
    AllOne allOne = new AllOne();
    allOne.inc("a");
    allOne.inc("b");
    allOne.inc("a");
    allOne.inc("c");
    allOne.dec("b");

    System.out.println("Max Key: " + allOne.getMaxKey()); // a
    System.out.println("Min Key: " + allOne.getMinKey()); // c or b (after b is decremented)
  }
}