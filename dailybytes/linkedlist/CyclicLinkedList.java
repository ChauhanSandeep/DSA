package dailybytes.linkedlist;

/**
 * Find if a singly linked list has a cycle.
 * For example, in the list 1 -> 2 -> 3 -> 4 -> 5, if the next of node 5 points back to node 3,
 * it creates a cycle: 1 -> 2 -> 3 -> 4 -> 5 -> 3 ...
 *
 */
public class CyclicLinkedList {
  /**
   * ✅ Detects cycle in a singly linked list using Floyd's Tortoise and Hare algorithm.
   *
   * Steps:
   * 1. Use two pointers (slow and fast).
   * 2. Move slow one step, fast two steps.
   * 3. If they meet, there’s a cycle.
   * 4. If fast reaches null, no cycle.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * 🔗 Leetcode: https://leetcode.com/problems/linked-list-cycle/
   *
   * @param list Singly linked list
   * @return true if there is a cycle; false otherwise
   */
    public static boolean findCycle(MyLinkedList list) {
        MyNode slow = list.getHead();
        MyNode fast = list.getHead();

        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();        // Move slow pointer one step
            fast = fast.getNext().getNext(); // Move fast pointer two steps

            if (slow == fast) return true; // If they meet, there is a cycle
        }

        return false; // No cycle
    }
}