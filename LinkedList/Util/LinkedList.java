package LinkedList.Util;

public class LinkedList {

    ListNode head;
    ListNode tail;
    public LinkedList(ListNode head) {
        this.head = head;
        this.tail = head;
    }
    public LinkedList(){}

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    public void setTail(ListNode tail) {
        this.tail = tail;
    }

    public void add(ListNode node) {
        tail.next = node;
        tail = tail.next;
    }

    public void printList(ListNode head) {
        ListNode temp = head;
        while(temp != null) {
            System.out.println(temp.getVal());
            temp = temp.getNext();
        }
    }
}
