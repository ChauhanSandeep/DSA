package DailyBytes.LinkedListPackage;

public class MyNode {

    private int data;
    private MyNode next;

    public MyNode() { }

    public MyNode(int data) {
        this.data = data;
        this.next = null;
    }

    public MyNode(int data, MyNode next) {
        this.data = data;
        this.next = next;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", next=" + next +
                '}';
    }
}
