package linkedlist;

import java.util.Arrays;

/**
 * Problem: Linked List Utility
 *
 * A small wrapper around ListNode that tracks head and tail. It supports demos in
 * this package by appending nodes and printing the current list without repeated
 * setup code.
 *
 * Pattern:  Linked list | Tail pointer | Utility wrapper
 *
 * Example:
 *   Input:  add [1,2,3]
 *   Output: head = 1, tail = 3
 *   Why:    each add appends at tail and moves the tail pointer forward.
 *
 * Follow-ups:
 *   1. How would you delete from tail in O(1)?
 *      Use a doubly linked list or keep more predecessor metadata.
 *   2. How would you support O(1) size?
 *      Maintain a size field on every mutation.
 *   3. How would you make this thread-safe?
 *      Synchronize mutations or protect head and tail with a lock.
 */
public class LinkedList {

  public static void main(String[] args) {
    int[] input = {1, 2, 3};
    LinkedList list = new LinkedList();
    for (int value : input) list.add(new ListNode(value));
    System.out.printf("values=%s -> head=%d tail=%d  expected=head=%d tail=%d%n", Arrays.toString(input), list.getHead().getVal(), list.getTail().getVal(), 1, 3);
    int[] edgeInput = {};
    LinkedList empty = new LinkedList();
    empty.add(null);
    System.out.printf("values=%s -> head=%s tail=%s  expected=head=%s tail=%s%n", Arrays.toString(edgeInput), empty.getHead(), empty.getTail(), null, null);
  }

  private ListNode head;
  private ListNode tail;

  /**
   * Constructs a linked list initialized with the given node as head and tail.
   *
   * @param head the starting node of the list
   */
  public LinkedList(ListNode head) {
    this.head = head;
    this.tail = head;
  }

  /**
   * Constructs an empty linked list with null head and tail.
   */
  public LinkedList() {
    this.head = null;
    this.tail = null;
  }

  /**
   * Returns the head of the linked list.
   *
   * @return the head node, or null if the list is empty
   */
  public ListNode getHead() {
    return head;
  }

  /**
   * Updates the head of the linked list.
   * Also updates the tail if the list was previously empty.
   *
   * @param head the new head node
   */
  public void setHead(ListNode head) {
    this.head = head;
    if (tail == null) {
      this.tail = head;
    }
  }

  /**
   * Returns the tail of the linked list.
   *
   * @return the tail node, or null if the list is empty
   */
  public ListNode getTail() {
    return tail;
  }

  /**
   * Updates the tail of the linked list.
   *
   * @param tail the new tail node
   */
  public void setTail(ListNode tail) {
    this.tail = tail;
  }

  /**
   * Appends a new node to the end of the linked list.
   *
   * <p>Handles both empty and non-empty lists. If the input node is null,
   * the method does nothing.
   *
   * @param newNode the node to append to the end of the list
   */
  public void add(ListNode newNode) {
    if (newNode == null) {
      return;
    }

    if (head == null) {
      head = newNode;
      tail = newNode;
    } else {
      tail.next = newNode;
      tail = newNode;
    }
  }

  /**
   * Prints the current state of the linked list from head to tail.
   *
   * <p>Output format: val1 -> val2 -> ... -> NULL
   */
  public void printList() {
    StringBuilder builder = new StringBuilder();
    ListNode current = head;

    while (current != null) {
      builder.append(current.getVal()).append(" -> ");
      current = current.getNext();
    }

    builder.append("NULL");
    System.out.println(builder);
  }
}