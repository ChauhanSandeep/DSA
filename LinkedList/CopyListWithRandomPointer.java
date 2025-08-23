package LinkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Copy List with Random Pointer
 * 
 * A linked list of length n is given such that each node contains an additional random pointer,
 * which could point to any node in the list, or null. Construct a deep copy of the list.
 * The deep copy should consist of exactly n brand new nodes, where each new node has its value
 * set to the value of its corresponding original node. Both the next and random pointer of the
 * new nodes should point to new nodes in the copied list.
 * 
 * Example:
 * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Explanation: Each node's random pointer points to the node at the specified index.
 * 
 * LeetCode: https://leetcode.com/problems/copy-list-with-random-pointer
 * 
 * Follow-up Questions:
 * 1. Can you solve it without using extra space (HashMap)?
 *    Answer: Yes, using the interweaving technique where we place copied nodes between originals.
 * 
 * 2. How would you handle cycles in the random pointers?
 *    Answer: The current approach naturally handles cycles since we create all nodes first.
 * 
 * 3. What if we needed to copy a graph instead of just a linked list?
 *    Answer: Similar approach with DFS/BFS and HashMap to track visited nodes.
 *    Related: https://leetcode.com/problems/clone-graph/
 * 
 * @author Sandeep
 */
public class CopyListWithRandomPointer {
    
    // Definition for a Node
    static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }
    
    /**
     * Creates a deep copy of the linked list using HashMap approach.
     * 
     * Algorithm:
     * 1. First pass: Create all new nodes and store mapping from old to new
     * 2. Second pass: Set next and random pointers using the mapping
     * 
     * Time Complexity: O(n) where n is the number of nodes
     * Space Complexity: O(n) for the HashMap
     * 
     * @param head Head of the original linked list
     * @return Head of the deep copied linked list
     */
    public Node copyRandomList(Node head) {
        if (head == null) return null;
        
        Map<Node, Node> nodeMap = new HashMap<>();
        
        // First pass: Create all nodes and store mapping
        Node current = head;
        while (current != null) {
            nodeMap.put(current, new Node(current.val));
            current = current.next;
        }
        
        // Second pass: Set next and random pointers
        current = head;
        while (current != null) {
            Node copiedNode = nodeMap.get(current);
            copiedNode.next = nodeMap.get(current.next);
            copiedNode.random = nodeMap.get(current.random);
            current = current.next;
        }
        
        return nodeMap.get(head);
    }
    
    /**
     * Space-optimized approach using interweaving technique.
     * Creates copied nodes between original nodes to avoid using extra space.
     * 
     * Algorithm:
     * 1. Create copied nodes and interweave with original nodes
     * 2. Set random pointers for copied nodes
     * 3. Separate the two lists
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(1) excluding the result list
     * 
     * @param head Head of the original linked list
     * @return Head of the deep copied linked list
     */
    public Node copyRandomListOptimized(Node head) {
        if (head == null) return null;
        
        // Step 1: Create copied nodes and interweave
        Node current = head;
        while (current != null) {
            Node copiedNode = new Node(current.val);
            copiedNode.next = current.next;
            current.next = copiedNode;
            current = copiedNode.next;
        }
        
        // Step 2: Set random pointers for copied nodes
        current = head;
        while (current != null) {
            if (current.random != null) {
                current.next.random = current.random.next;
            }
            current = current.next.next;
        }
        
        // Step 3: Separate the two lists
        Node copiedHead = head.next;
        Node originalCurrent = head;
        Node copiedCurrent = copiedHead;
        
        while (originalCurrent != null) {
            originalCurrent.next = copiedCurrent.next;
            originalCurrent = originalCurrent.next;
            
            if (originalCurrent != null) {
                copiedCurrent.next = originalCurrent.next;
                copiedCurrent = copiedCurrent.next;
            }
        }
        
        return copiedHead;
    }
    
    /**
     * Recursive approach with memoization.
     * Uses HashMap to avoid infinite recursion on cycles.
     */
    public Node copyRandomListRecursive(Node head) {
        return copyHelper(head, new HashMap<>());
    }
    
    // Helper method for recursive approach
    private Node copyHelper(Node node, Map<Node, Node> visited) {
        if (node == null) return null;
        
        if (visited.containsKey(node)) {
            return visited.get(node);
        }
        
        Node copiedNode = new Node(node.val);
        visited.put(node, copiedNode);
        
        copiedNode.next = copyHelper(node.next, visited);
        copiedNode.random = copyHelper(node.random, visited);
        
        return copiedNode;
    }
}