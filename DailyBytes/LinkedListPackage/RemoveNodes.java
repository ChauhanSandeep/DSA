package DailyBytes.LinkedListPackage;

public class RemoveNodes {

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.push(new MyNode(3));
        list.push(new MyNode(3));
        list.push(new MyNode(2));
        list.push(new MyNode(3));
        list.printList();
        removeNodes(list, 3);
        list.printList();
    }

    /**
     * Given a linked list and a value, remove all nodes containing the provided value, and return the resulting list.
     */
    public static void removeNodes(MyLinkedList list, int val) {
        MyNode head = list.getHead();
        // remvoe the head ahead if head is equal to value
        while (head != null && head.getData() == val) {
            head = head.getNext();
        }
        MyNode temp = head;
        while(temp != null && temp.getNext() != null) {
            if(temp.getNext().getData() == val) {
                temp.setNext(temp.getNext().getNext());
            }
            temp = temp.getNext();
        }
        list.setHead(head);
    }
}
