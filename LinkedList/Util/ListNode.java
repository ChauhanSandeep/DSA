package LinkedList.Util;

public class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int data) {
            this.val = data;
            this.next = null;
        }

        public ListNode(int data, ListNode next) {
            this.val = data;
            this.next = next;
        }

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
    }