package design;

import java.util.*;

/**
 * Problem: Design Browser History
 *
 * Design a browser history object with visit, back, and forward operations. A new
 * visit from the middle of history must discard every forward page, while back and
 * forward clamp at the oldest and newest reachable pages.
 *
 * Leetcode: https://leetcode.com/problems/design-browser-history/ (Medium)
 * Rating:   LeetCode contest rating 1454
 * Pattern:  Design | Dynamic array | Cursor over history
 *
 * Example:
 *   Input:  homepage = "leetcode.com", visit("google.com"), back(1), forward(1)
 *   Output: "facebook.com" after a later forward in the standard sequence
 *   Why:    the cursor moves through a linear history, and visit clears any branch ahead.
 *
 * Follow-ups:
 *   1. How would you support branching history like real browser tabs?
 *      Store pages as a tree and let forward choose or list child branches.
 *   2. How would you cap memory for very long sessions?
 *      Use a circular buffer or evict old nodes beyond a configured limit.
 *   3. How would you persist history across restarts?
 *      Append operations to a log and replay or checkpoint the current list and cursor.
 *
 * Related: Design Circular Queue (622), LRU Cache (146).
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
        
        /**
         * Starts history at the homepage.
         *
         * Time:  O(1) - stores one page.
         * Space: O(1) - one history entry is allocated.
         *
         * @param homepage first page in the session
         */
        public BrowserHistory(String homepage) {
            history = new ArrayList<>();
            history.add(homepage);
            current = 0;
        }
        
        /**
         * Visits a new URL and clears all forward history.
         *
         * Time:  O(f) - removes f forward entries before appending the URL.
         * Space: O(1) - adds one page after discarded entries.
         *
         * @param url page to visit
         */
        public void visit(String url) {
            // Clear forward history and add new page
            while (history.size() > current + 1) {
                history.remove(history.size() - 1);
            }
            history.add(url);
            current++;
        }
        
        /**
         * Moves the cursor back by at most the requested number of steps.
         *
         * Time:  O(1) - clamps and updates one index.
         * Space: O(1) - no extra storage.
         *
         * @param steps maximum pages to move backward
         * @return current page after moving
         */
        public String back(int steps) {
            current = Math.max(0, current - steps);
            return history.get(current);
        }
        
        /**
         * Moves the cursor forward by at most the requested number of steps.
         *
         * Time:  O(1) - clamps and updates one index.
         * Space: O(1) - no extra storage.
         *
         * @param steps maximum pages to move forward
         * @return current page after moving
         */
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

    public static void main(String[] args) {
        BrowserHistory browserHistory = new BrowserHistory("leetcode.com");
        browserHistory.visit("google.com");
        browserHistory.visit("facebook.com");
        browserHistory.visit("youtube.com");

        String[] got = {
                browserHistory.back(1),
                browserHistory.back(1),
                browserHistory.forward(1)
        };
        String[] expected = {"facebook.com", "google.com", "facebook.com"};
        System.out.printf("ops=back,back,forward -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));

        browserHistory.visit("linkedin.com");
        String[] gotAfterBranch = {browserHistory.forward(2), browserHistory.back(2), browserHistory.back(7)};
        String[] expectedAfterBranch = {"linkedin.com", "google.com", "leetcode.com"};
        System.out.printf("ops=visit,forward,back,back -> %s  expected=%s%n",
                Arrays.toString(gotAfterBranch), Arrays.toString(expectedAfterBranch));
    }
}