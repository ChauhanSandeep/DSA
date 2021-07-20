package DailyBytes.LinkedListPackage;
import java.util.*;

// stack using a single queue;
public class QueueStack {
    Queue<Integer> linkedList = new LinkedList<>();

    public static void main(String[] args) {
        QueueStack myStack = new QueueStack();
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        myStack.push(4);
        myStack.push(5);
        myStack.pop();
        myStack.pop();
        myStack.pop();
        myStack.pop();
        myStack.pop();
    }

    public void push(int i) {
        linkedList.add(i);
    }

    public int pop() {
        if(linkedList.isEmpty()) throw new RuntimeException("The list is empty");

        int size = linkedList.size();

        while(size > 1){
            int data = linkedList.poll();
            linkedList.offer(data);
            size--;
        }
        return linkedList.poll();
    }

    public int peek() {
        return linkedList.peek();
    }
}
