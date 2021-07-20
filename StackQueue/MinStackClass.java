package StackQueue;

import java.util.EmptyStackException;
import java.util.Stack;

public class MinStackClass {

    public static void main(String[] args) {
        MyStack s = new MyStack();
        s.push(3);
        s.push(5);
        s.getMin();
        s.push(2);
        s.push(1);
        s.getMin();
        s.pop();
        s.getMin();
        s.pop();
        s.getMin();


    }
}

/**
 * Create stack with functionality to push pop and findMin element in stack
 */
class MyStack {
    Stack<Integer> stack = new Stack<>();
    Stack<Integer> minStack = new Stack<>();

    public void push(int i) {
        stack.push(i);
        if(minStack.isEmpty() || minStack.peek() >= i) {
            minStack.push(i);
        }
    }

    public int pop() {
        if(stack.isEmpty()) throw new EmptyStackException();

        if(stack.peek().equals(minStack.peek())) {
            minStack.pop();
        }
        return stack.pop();
    }

    public int getMin() {
        if(minStack.isEmpty()) throw new EmptyStackException();
        int ans = minStack.peek();
        System.out.println(ans);
        return ans;
    }
}
