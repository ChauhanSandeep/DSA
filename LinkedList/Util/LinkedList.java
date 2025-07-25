package LinkedList.Util;

/**
 * A utility class for singly linked list operations.
 *
 * <p>This class provides support for:
 * - Constructing a linked list with or without an initial node
 * - Adding nodes at the tail
 * - Accessing/modifying head and tail
 * - Printing the entire list for debugging or visualization
 *
 * <p>Example usage:
 * <pre>{@code
 *   LinkedList list = new LinkedList();
 *   list.add(new ListNode(1));
 *   list.add(new ListNode(2));
 *   list.printList(); // Output: 1 -> 2 -> NULL
 * }</pre>
 */
public class LinkedList {

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