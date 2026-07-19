package stacksandqueues.design;

import java.util.*;

/**
 * Problem: Max Stack
 *
 * Design a stack that supports normal stack operations plus peekMax and popMax.
 * If multiple entries share the maximum value, popMax removes the one closest
 * to the top of the stack.
 *
 * Leetcode: https://leetcode.com/problems/max-stack/ (Hard)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Design | Stack with max tracking | TreeMap with linked list
 *
 * Example:
 *   Input:  push 5, push 1, push 5, top, popMax, top, peekMax, pop, top
 *   Output: [5,5,1,5,1,5]
 *   Why:    popMax removes the topmost 5, leaving 1 on top and another 5 as the max.
 *
 * Follow-ups:
 *   1. Optimize popMax from O(n)?
 *      Use a TreeMap from value to nodes plus a doubly linked list for stack order.
 *   2. Support peekMin and popMin too?
 *      Add a second ordered map keyed by value or use one TreeMap for both extremes.
 *   3. Make duplicate maximum removal deterministic?
 *      Store nodes in insertion order per value and remove the last node for topmost max.
 *   4. Make it thread-safe?
 *      Synchronize operations that update both stack order and max indexes.
 *
 * Related: Min Stack (155), LRU Cache (146), All Oone Data Structure (432).
 */

public class MaxStack {

        /**
     * Intuition: store each pushed value together with the maximum seen up to
     * that stack depth. Then peekMax is just reading the top pair, while popMax
     * temporarily removes values above the topmost maximum and pushes them back
     * so their max-so-far values are rebuilt correctly.
     *
     * Algorithm:
     *   1. On push, store x and max(x, previous max) as a pair.
     *   2. pop, peek, and peekMax read directly from the top pair.
     *   3. popMax moves pairs to a temporary stack until maxVal is on top.
     *   4. Remove maxVal, then push saved values back through push.
     *
     * Time:  O(1) for push/pop/peek/peekMax, O(n) for popMax - popMax may scan the stack.
     * Space: O(n) - each value stores its max-so-far and popMax may use a temporary stack.
     */

    static class MaxStackSimple implements MaxStackInterface {
        private Stack<int[]> stack; // [value, max_so_far]

        public MaxStackSimple() {
            stack = new Stack<>();
        }

        /**
         * Pushes element x onto the stack.
         * Time Complexity: O(1)
         * Space Complexity: O(1)
         * 
         * @param x element to push
         */
        public void push(int x) {
            int currentMax = stack.isEmpty() ? x : Math.max(x, stack.peek()[1]);
            stack.push(new int[]{x, currentMax});
        }

        /**
         * Removes the element on top of the stack and returns it.
         * Time Complexity: O(1)
         * Space Complexity: O(1)
         * 
         * @return the element on top of the stack
         */
        public int pop() {
            return stack.pop()[0];
        }

        /**
         * Gets the element on the top of the stack without removing it.
         * Time Complexity: O(1)
         * Space Complexity: O(1)
         * 
         * @return the element on top of the stack
         */
        public int peek() {
            return stack.peek()[0];
        }

        /**
         * Retrieves the maximum element in the stack without removing it.
         * Time Complexity: O(1)
         * Space Complexity: O(1)
         * 
         * @return the maximum element in the stack
         */
        public int peekMax() {
            return stack.peek()[1];
        }

        /**
         * Retrieves and removes the maximum element in the stack.
         * Time Complexity: O(n) in worst case
         * Space Complexity: O(n)
         * 
         * @return the maximum element removed from the stack
         */
        public int popMax() {
            Stack<int[]> temp = new Stack<>();
            int maxVal = peekMax();

            // Find and remove the max element
            while (stack.peek()[0] != maxVal) {
                temp.push(stack.pop());
            }

            stack.pop(); // Remove the max element

            // Restore the temporary elements
            while (!temp.isEmpty()) {
                int val = temp.pop()[0];
                push(val); // Use push to maintain max values correctly
            }

            return maxVal;
        }
    }

    /**
    * Approach 2: Optimal Solution using Doubly Linked List + TreeMap.
    * This achieves O(log n) time complexity for all operations.
    * 
    * Data structures:
    *  - Doubly linked list: maintains stack order
    *  - TreeMap<Integer, List<Node>>: maps values to their nodes for quick max lookup
    * 
    * Operations:
    *  - push(x): O(log n) - add to list and TreeMap
    *  - pop(): O(log n) - remove from list and TreeMap
    *  - top(): O(1) - access list tail
    *  - peekMax(): O(log n) - access TreeMap lastKey
    *  - popMax(): O(log n) - find max in TreeMap and remove node
    * 
    * Time Complexity: O(log n) for all operations
    * Space Complexity: O(n) for doubly linked list and TreeMap
    */
    static class MaxStackOptimal implements MaxStackInterface {
        // Node class for doubly linked list
        private static class Node {
            int value;
            Node previous;
            Node next;
        
            Node(int value) {
                this.value = value;
            }
        }
    
        // Doubly linked list to maintain stack order
        private Node head;
        private Node tail;
    
        // TreeMap to efficiently find maximum element
        // Key: element value, Value: list of nodes with that value
        private TreeMap<Integer, List<Node>> valueToNodes;
    
        public MaxStackOptimal() {
            head = new Node(0);  // Sentinel head
            tail = new Node(0);  // Sentinel tail
            head.next = tail;
            tail.previous = head;
            valueToNodes = new TreeMap<>();
        }

        /**
         * Push element onto stack
         * Time Complexity: O(log n) : For TreeMap insertion
         * Space Complexity: O(1)
         */
        @Override
        public void push(int x) {
            Node newNode = new Node(x);
        
            // Add to end of doubly linked list (top of stack)
            newNode.previous = tail.previous;
            newNode.next = tail;
            tail.previous.next = newNode;
            tail.previous = newNode;
        
            // Add to TreeMap
            valueToNodes.computeIfAbsent(x, k -> new ArrayList<>()).add(newNode);
        }

        /**
         * Removes the element on top of the stack and returns it
         * Time Complexity: O(log n) : For TreeMap removal
         * Space Complexity: O(1)
         */        
        @Override
        public int pop() {
            Node topNode = tail.previous;
            removeNode(topNode);
        
               // Remove from TreeMap
            List<Node> nodes = valueToNodes.get(topNode.value);
            nodes.remove(nodes.size() - 1);  // Remove last occurrence
            if (nodes.isEmpty()) {
                valueToNodes.remove(topNode.value);
            }
        
               return topNode.value;
        }

        /**
         * Get the element on top of the stack without removing it
         * Time Complexity: O(1)
         * Space Complexity: O(1)
         */
        @Override
        public int peek() {
            return tail.previous.value;
        }

        /**
         * Retrieve the maximum element in the stack without removing it
         * Time Complexity: O(log n)
         * Space Complexity: O(1)
         */
        @Override
        public int peekMax() {
            return valueToNodes.lastKey();
        }

        /**
         * Remove and return the maximum element
         * Time Complexity: O(log n)
         * Space Complexity: O(1)
         */
        @Override
        public int popMax() {
            int maxValue = peekMax();
            List<Node> nodes = valueToNodes.get(maxValue);
        
            // Remove the last node with max value (closest to top)
            Node nodeToRemove = nodes.remove(nodes.size() - 1);
            removeNode(nodeToRemove);
        
               if (nodes.isEmpty()) {
                valueToNodes.remove(maxValue);
            }
        
               return maxValue;
        }

        /** Removes a node from the doubly linked list. */
        private void removeNode(Node node) {
            node.previous.next = node.next;
            node.next.previous = node.previous;
        }
    }

    // Common interface for all implementations
    interface MaxStackInterface {
        void push(int x);
        int pop();
        int peek();
        int peekMax();
        int popMax();
    }

    public static void main(String[] args) {
        MaxStackInterface stack = new MaxStackSimple();
        stack.push(5);
        stack.push(1);
        stack.push(5);
        int first = stack.peek();
        int second = stack.popMax();
        int third = stack.peek();
        int fourth = stack.peekMax();
        int fifth = stack.pop();
        int sixth = stack.peek();

        int[] got = { first, second, third, fourth, fifth, sixth };
        int[] expected = { 5, 5, 1, 5, 1, 5 };
        System.out.printf("ops=%s -> %s  expected=%s%n",
            "push(5),push(1),push(5),top,popMax,top,peekMax,pop,top",
            Arrays.toString(got), Arrays.toString(expected));
    }
}