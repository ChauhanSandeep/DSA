package linkedlist;

/**
 * ListNode represents a single node in a singly linked list.
 * Each node contains an integer value and a reference to the next node.
 */
public class ListNode {
  public int val;  // Value stored in the node
  public ListNode next; // Pointer to the next node

  // Constructor to initialize a node with a value and no next node
  public ListNode(int value) {
    this.val = value;
    this.next = null;
  }

  // Constructor to initialize a node with a value and a reference to the next node
  public ListNode(int value, ListNode next) {
    this.val = value;
    this.next = next;
  }

  // Getters and Setters for the value and next node
  public int getVal() {
    return val;
  }

  public void setVal(int val) {
    this.val = val;
  }

  public ListNode getNext() {
    return next;
  }

  public void setNext(ListNode next) {
    this.next = next;
  }

  /**
   * Returns the string representation of the ListNode.
   * Useful for debugging.
   */
  @Override
  public String toString() {
    return "ListNode{" + "val=" + val + ", next=" + (next != null ? next.val : "null") + '}';
  }
}
