package StackQueue;

import java.util.Stack;

public class QueueUsingStack {
    public static void main(String[] args) {
        StackQueue queue = new StackQueue();
        queue.push(2);
        queue.push(3);
        System.out.println(queue.pop());
        queue.push(10);
        System.out.println(queue.pop());
    }
}

/**
 * Create queue using stack
 */
class StackQueue
{
    Stack<Integer> s1 = new Stack<Integer>();
    Stack<Integer> s2 = new Stack<Integer>();

    void push(int x)
    {
	   s1.push(x);
    }

    int pop()
    {
	   if(s1.isEmpty() && s2.isEmpty()) return -1;
	   if(!s2.isEmpty()) return s2.pop();
	   
	  while(!s1.isEmpty()) {
	      s2.push(s1.pop());
	  }
	  return s2.pop();
    }
}