package linkedlist;

/**
 * Problem: Given the head of a singly linked list, group all the nodes with odd indices together
 * followed by the nodes with even indices, and return the reordered list.
 *
 * Example:
 * Input: 1 -> 2 -> 3 -> 4 -> 5
 * Output: 1 -> 3 -> 5 -> 2 -> 4
 *
 * Approach:
 * 1. Find the middle of the linked list using the slow-fast pointer approach.
 * 2. Reverse the second half of the list.
 * 3. Compare the first half with the reversed second half.
 *     - If they are the same, the list is a palindrome.
 *     - If they differ, the list is not a palindrome.
 * 4. Restore the list to its original state (useful in real-world scenarios).
 *
 * Time Complexity: O(N) - Traversing the list multiple times.
 * Space Complexity: O(1) - No extra space used apart from pointers.
 *
 * Edge Cases Considered:
 * - Empty list (returns true).
 * - Single-node list (returns true).
 * - Even and odd-length lists.
 *
 * Link: https://leetcode.com/problems/palindrome-linked-list/
 */
public class OddEvenLinkedList {

  public static void main(String[] args) {
    ListNode head = new ListNode(1);
    LinkedList list = new LinkedList(head);
    list.add(new ListNode(2));
    list.add(new ListNode(3));
    list.add(new ListNode(2));
    list.add(new ListNode(1));

    ListNode newHead = oddEvenList(head);
    System.out.println("Reordered List: " + newHead);
  }

  /**
   * Rearranges the list by grouping odd and even indexed nodes.
   *
   * Approach:
   * 1. Use two pointers: one for odd indexed nodes and another for even indexed nodes.
   * 2. Traverse the list, linking odd indexed nodes together and even indexed nodes together.
   * 3. At the end, link the last odd indexed node to the head of the even indexed nodes.
   *
   * * Time Complexity: O(N) - where N is the number of nodes in the list.
   * * Space Complexity: O(1) - no extra space used apart from pointers.
   *
   * @param head Head of the linked list.
   * @return Reordered list with odd-indexed nodes first, then even-indexed.
   */
  public static ListNode oddEvenList(ListNode head) {
      if (head == null || head.getNext() == null) {
          return head;
      }

    ListNode odd = head;                     // Points to current odd node
    ListNode even = head.getNext();          // Points to current even node
    ListNode evenHead = even;                // To reconnect at the end

    while (even != null && even.getNext() != null) {
      odd.setNext(even.getNext());
      odd = odd.getNext();

      even.setNext(odd.getNext());
      even = even.getNext();
    }

    odd.setNext(evenHead); // Attach even list after odd list
    return head;
  }

  /**
   * Alternative approach using explicit odd and even list construction.
   * This approach is more intuitive but uses slightly more space for dummy nodes.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   */
  public ListNode oddEvenListAlternative(ListNode head) {
    if (head == null) return null;

    // Create dummy heads for odd and even lists
    ListNode oddDummy = new ListNode(0);
    ListNode evenDummy = new ListNode(0);
    ListNode oddTail = oddDummy;
    ListNode evenTail = evenDummy;

    boolean isOddIndex = true;

    while (head != null) {
      if (isOddIndex) {
        oddTail.next = head;
        oddTail = oddTail.next;
      } else {
        evenTail.next = head;
        evenTail = evenTail.next;
      }

      head = head.next;
      isOddIndex = !isOddIndex;
    }

    // Terminate even list and connect odd list to even list
    evenTail.next = null;
    oddTail.next = evenDummy.next;

    return oddDummy.next;
  }
}
