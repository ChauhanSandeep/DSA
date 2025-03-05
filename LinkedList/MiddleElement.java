package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * Given the head of a singly linked list, find the middle element.
 * Uses the slow and fast pointer approach to find the middle efficiently.
 * Time Complexity: O(N)
 * Space Complexity: O(1)
 */
public class MiddleElement {
  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(4));
    list.add(new ListNode(5));
    list.add(new ListNode(6));

    System.out.println("Middle Element: " + findMiddleElement(head));
  }

  /**
   * Finds the middle element of a linked list using the slow-fast pointer approach.
   * @param head Head node of the linked list.
   * @return Value of the middle element.
   */
  private static int findMiddleElement(ListNode head) {
    if (head == null) return -1; // Handle empty list case

    ListNode slow = head;
    ListNode fast = head;

    while (fast != null && fast.getNext() != null) {
      slow = slow.getNext();
      fast = fast.getNext().getNext();
    }
    return slow.getVal();
  }
}
