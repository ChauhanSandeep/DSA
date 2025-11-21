package stacksandqueues.design;

import java.util.*;

/**
 * Problem: Max Stack
 *
 * Design a max stack data structure that supports the stack operations and supports finding the
 * stack's maximum element.
 *
 * Implement the MaxStack class:
 * - MaxStack() Initializes the stack object.
 * - void push(int x) Pushes element x onto the stack.
 * - int pop() Removes the element on top of the stack and returns it.
 * - int top() Gets the element on the top of the stack without removing it.
 * - int peekMax() Retrieves the maximum element in the stack without removing it.
 * - int popMax() Retrieves and removes the maximum element in the stack.
 *
 * Example:
 * Input: ["MaxStack", "push", "push", "push", "top", "popMax", "top", "peekMax", "pop", "top"]
 *        [[], [5], [1], [5], [], [], [], [], [], []]
 * Output: [null, null, null, null, 5, 5, 1, 5, 1, 5]
 *
 * LeetCode: https://leetcode.com/problems/max-stack
 *
 * Follow-up Questions:
 * 1. How would you optimize for frequent popMax operations?
 *    Answer: Use TreeMap with doubly linked list for O(log n) popMax instead of O(n).
 *
 * 2. What if we need to support getMin operation as well?
 *    Answer: Maintain separate min stack or extend current approach to track both max and min.
 *
 * 3. How would you handle concurrent access to the stack?
 *    Answer: Add synchronization using locks or use concurrent data structures.
 *    Related: https://leetcode.com/problems/min-stack/
 */
public class MaxStack {

    /**
     * Simple implementation using single stack with pairs.
     * Each element stores (value, maxSoFar).
     *
     * Algorithm:
     * 1. Use a single stack to store pairs of (value, currentMax).
     * 2. On push, determine the new max and store it with the value.
     * 3. On pop, simply pop from the stack.
     * 4. On top, return the value from the top pair.
     * 5. On peekMax, return the max from the top pair.
     * 6. On popMax, use a temporary stack to hold elements until max is found,
     *   then restore elements back to the main stack.
     *
     * Time Complexities:
     * - push, pop, top, peekMax: O(1)
     * - popMax: O(n) in worst case
     */
    static class MaxStackSimple implements MaxStackInterface {
        private Stack<int[]> stack; // [value, max_so_far]

        public MaxStackSimple() {
            stack = new Stack<>();
        }

        public void push(int x) {
            int currentMax = stack.isEmpty() ? x : Math.max(x, stack.peek()[1]);
            stack.push(new int[]{x, currentMax});
        }

        public int pop() {
            return stack.pop()[0];
        }

        public int peek() {
            return stack.peek()[0];
        }

        public int peekMax() {
            return stack.peek()[1];
        }

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

        // Push element onto stack
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

        // Remove and return the top element
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

        // Get the element on top of the stack
        public int top() {
            return tail.previous.value;
        }

        // Retrieve the maximum element in the stack
        public int peekMax() {
            return valueToNodes.lastKey();
        }

        // Remove and return the maximum element
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

        // Helper: Remove a node from doubly linked list
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
}