package DailyBytes.LinkedListPackage;

public class MyLinkedList {
    MyNode head;

    public MyLinkedList() { }

    public MyLinkedList(MyNode head) {
        this.head = head;
    }

    public void push(MyNode node) {
        if(head == null) {
            head = node;
            return;
        }
        MyNode temp = head;
        while(temp.getNext() != null) {
            temp = temp.getNext();
        }
        temp.setNext(node);
    }

    public void printList() {
        MyNode temp = head;
        while(temp != null) {
            System.out.print(temp.getData() + " -> ");
            temp = temp.getNext();
        }
        System.out.println("NULL");
    }

    public MyNode getHead() {
        return head;
    }

    public void setHead(MyNode head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "MyLinkedList{" +
                "head=" + head +
                '}';
    }
}
