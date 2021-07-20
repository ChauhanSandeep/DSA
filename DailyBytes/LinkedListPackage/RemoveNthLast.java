package DailyBytes.LinkedListPackage;

public class RemoveNthLast {

    public static void main(String[] args) {

        MyLinkedList list = new MyLinkedList();
        MyNode head = new MyNode(1);
        list.push(head);
        list.push(new MyNode(2));
        list.push(new MyNode(3));
        list.push(new MyNode(4));
        list.push(new MyNode(5));
        removeFromLast(list, 0);
        list.printList();
    }

    public static void removeFromLast(MyLinkedList list, int index) {
        if(index <= 0) {
            return;
        }
        MyNode head = list.getHead();
        if(head == null) return;

        int len = 0;
        MyNode temp = head;
        while(temp != null) {
            temp = temp.getNext();
            len++;
        }

        int curr = 0;
        temp = head;

        // remove first element
        if(curr > len - index - 1) {
            head = head.getNext();
            list.setHead(head);
            return;
        }

        while (curr < len - index - 1) {
            temp = temp.getNext();
            curr++;
        }
        temp.setNext(temp.getNext().getNext());
    }
}
