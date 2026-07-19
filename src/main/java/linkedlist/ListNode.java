package linkedlist;

import java.util.Arrays;

/**
 * Problem: Singly Linked List Node
 *
 * Represents one integer node in the linked-list problems in this package. The
 * fields stay public so pointer-rewiring examples can use the original val and
 * next names directly.
 *
 * Pattern:  Linked list | Node model | Pointer field
 *
 * Example:
 *   Input:  values = [1,2]
 *   Output: first.val = 1, first.next.val = 2
 *   Why:    the first node stores 1 and points to the second node.
 *
 * Follow-ups:
 *   1. How would you make it generic?
 *      Use ListNode<T> with T val.
 *   2. How would you support a doubly linked list?
 *      Add prev and maintain it on every pointer update.
 *   3. How would you hide mutation?
 *      Make fields private and expose controlled methods.
 */
public class ListNode {

  public static void main(String[] args) {
    int[] input = {1, 2};
    ListNode head = new ListNode(1, new ListNode(2));
    System.out.printf("values=%s -> val=%d next=%d  expected=val=%d next=%d%n", Arrays.toString(input), head.getVal(), head.getNext().getVal(), 1, 2);
    int[] singleInput = {7};
    ListNode single = new ListNode(7);
    System.out.printf("values=%s -> val=%d next=%s  expected=val=%d next=%s%n", Arrays.toString(singleInput), single.getVal(), single.getNext(), 7, null);
  }
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
