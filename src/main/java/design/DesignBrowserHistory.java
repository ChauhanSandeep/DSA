package design;

import java.util.*;

/**
 * 1472. Design Browser History
 * 
 * Problem: Design a browser history system with visit, back, and forward operations.
 * 
 * Example:
 * BrowserHistory browserHistory = new BrowserHistory("leetcode.com");
 * browserHistory.visit("google.com");       // Visit google.com
 * browserHistory.visit("facebook.com");     // Visit facebook.com
 * browserHistory.back(1);                   // Back 1 step to google.com
 * browserHistory.forward(1);                // Forward 1 step to facebook.com
 * 
 * LeetCode: https://leetcode.com/problems/design-browser-history
 * 
 * Follow-up questions:
 * Q: How to handle very large history?
 * A: Use circular buffer with size limit, or implement LRU cache.
 * 
 * Q: Can we optimize for memory when many pages visited?
 * A: Use linked list with size cap, or compress old history.
 * 
 * Q: How to support branching history (tree structure)?
 * A: Use tree structure instead of linear array/list.
 */
public class DesignBrowserHistory {
    
    /**
     * Array-based implementation for browser history.
     * 
     * Algorithm: Use array with current index tracking
     * - visit(): add to current+1, clear forward history
     * - back(): move current index backward
     * - forward(): move current index forward
     * 
     * Time Complexity: O(1) for all operations
     * Space Complexity: O(n) where n is number of pages
     */
    public static class BrowserHistory {
        private List<String> history;
        private int current;
        
        public BrowserHistory(String homepage) {
            history = new ArrayList<>();
            history.add(homepage);
            current = 0;
        }
        
        public void visit(String url) {
            // Clear forward history and add new page
            while (history.size() > current + 1) {
                history.remove(history.size() - 1);
            }
            history.add(url);
            current++;
        }
        
        public String back(int steps) {
            current = Math.max(0, current - steps);
            return history.get(current);
        }
        
        public String forward(int steps) {
            current = Math.min(history.size() - 1, current + steps);
            return history.get(current);
        }
    }
    
    /**
     * Linked List implementation with explicit node structure.
     * More memory efficient for sparse operations.
     */
    public static class BrowserHistoryLinkedList {
        private class HistoryNode {
            String url;
            HistoryNode prev;
            HistoryNode next;
            
            HistoryNode(String url) {
                this.url = url;
            }
        }
        
        private HistoryNode current;
        
        public BrowserHistoryLinkedList(String homepage) {
            current = new HistoryNode(homepage);
        }
        
        public void visit(String url) {
            HistoryNode newNode = new HistoryNode(url);
            current.next = newNode;
            newNode.prev = current;
            current = newNode;
        }
        
        public String back(int steps) {
            while (steps > 0 && current.prev != null) {
                current = current.prev;
                steps--;
            }
            return current.url;
        }
        
        public String forward(int steps) {
            while (steps > 0 && current.next != null) {
                current = current.next;
                steps--;
            }
            return current.url;
        }
    }
    
    /**
     * Stack-based implementation using two stacks.
     * Good for understanding the concept but less efficient.
     */
    public static class BrowserHistoryStacks {
        private Stack<String> backStack;
        private Stack<String> forwardStack;
        private String current;
        
        public BrowserHistoryStacks(String homepage) {
            backStack = new Stack<>();
            forwardStack = new Stack<>();
            current = homepage;
        }
        
        public void visit(String url) {
            backStack.push(current);
            current = url;
            forwardStack.clear(); // Clear forward history
        }
        
        public String back(int steps) {
            while (steps > 0 && !backStack.isEmpty()) {
                forwardStack.push(current);
                current = backStack.pop();
                steps--;
            }
            return current;
        }
        
        public String forward(int steps) {
            while (steps > 0 && !forwardStack.isEmpty()) {
                backStack.push(current);
                current = forwardStack.pop();
                steps--;
            }
            return current;
        }
    }
    
    /**
     * Memory-optimized version with circular buffer.
     * Limits memory usage to fixed size.
     */
    public static class BrowserHistoryCircular {
        private String[] history;
        private int capacity;
        private int size;
        private int current;
        private int start;
        
        public BrowserHistoryCircular(String homepage, int capacity) {
            this.capacity = capacity;
            this.history = new String[capacity];
            this.history[0] = homepage;
            this.size = 1;
            this.current = 0;
            this.start = 0;
        }
        
        public BrowserHistoryCircular(String homepage) {
            this(homepage, 1000); // Default capacity
        }
        
        public void visit(String url) {
            current++;
            if (current >= capacity) {
                // Shift everything left
                System.arraycopy(history, 1, history, 0, capacity - 1);
                current = capacity - 1;
                start = 0;
            }
            
            history[current] = url;
            size = current - start + 1;
        }
        
        public String back(int steps) {
            current = Math.max(start, current - steps);
            return history[current];
        }
        
        public String forward(int steps) {
            int maxForward = start + size - 1;
            current = Math.min(maxForward, current + steps);
            return history[current];
        }
    }
    
    /**
     * Tree-based implementation supporting branching history.
     * Advanced version for complex navigation patterns.
     */
    public static class BrowserHistoryTree {
        private class HistoryNode {
            String url;
            HistoryNode parent;
            List<HistoryNode> children;
            
            HistoryNode(String url) {
                this.url = url;
                this.children = new ArrayList<>();
            }
        }
        
        private HistoryNode current;
        private HistoryNode root;
        
        public BrowserHistoryTree(String homepage) {
            root = new HistoryNode(homepage);
            current = root;
        }
        
        public void visit(String url) {
            HistoryNode newNode = new HistoryNode(url);
            newNode.parent = current;
            current.children.add(newNode);
            current = newNode;
        }
        
        public String back(int steps) {
            while (steps > 0 && current.parent != null) {
                current = current.parent;
                steps--;
            }
            return current.url;
        }
        
        // For tree structure, forward goes to most recent child
        public String forward(int steps) {
            while (steps > 0 && !current.children.isEmpty()) {
                current = current.children.get(current.children.size() - 1);
                steps--;
            }
            return current.url;
        }
        
        // Additional method to navigate to specific child branch
        public String forwardToBranch(int branchIndex, int steps) {
            if (branchIndex < current.children.size()) {
                current = current.children.get(branchIndex);
                steps--;
                return forward(steps);
            }
            return current.url;
        }
    }
}