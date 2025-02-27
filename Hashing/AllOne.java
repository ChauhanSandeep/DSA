package Hashing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* doubly linked list + hashmap
- Using the hashmap as the index to find out where the key is located in linked list.
- Dealing with the key in a double linked list.

A Node contains all keys with the same value. The head keeps track of the Node with highest value and tail keeps track of Node with lowest value. 
The nodes from tail to head have values in increasing order. A hashmap has been used to reach the desired Node with the given key in O(1) time.

*/
class AllOne {

    class Node {
        Set<String> strs; // all keys with frequency = val
        int freq; // frequency
        Node prev = null;
        Node next = null;
        
        Node(int freq) {
            this.freq = freq;
            this.strs = new HashSet<>();
        }
        
        Node(String str, int freq) {
            this(freq);
            this.strs.add(str);
        }
    }
    
    Map<String, Node> map; // key -> node
    Node head;
    Node tail;
    
    /** Initialize your data structure here. */
    public AllOne() {
        this.map = new HashMap<>();
        this.head = new Node(-1);
        this.tail = new Node(-1);
        
        // sentinel head & tail to simplify insertion/deletion at start/end
        this.head.prev = tail;
        this.tail.next = head;
    }
    
    private void deleteNode(Node node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }
    
    private void removeKeyFromNode(String key, Node node) {
        node.strs.remove(key);
        if (node.strs.isEmpty()) {
            deleteNode(node);
        }
    }
    
    // insert 'newNode' next to 'node'
    private void insertNext(Node node, Node newNode) {
        node.next.prev = newNode;
        newNode.next = node.next;
        node.next = newNode;
        newNode.prev = node;
    }
    
    /** Inserts a new key <Key> with value 1. Or increments an existing key by 1. */
    public void inc(String key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            if (node.freq + 1 == node.next.freq) {
                node.next.strs.add(key);
                map.put(key, node.next);
            } else {
                Node newNode = new Node(key, node.freq + 1);
                insertNext(node, newNode);
                map.put(key, newNode);
            }
            removeKeyFromNode(key, node);
        } else {
            if (tail.next.freq == 1) {
                tail.next.strs.add(key);
                map.put(key, tail.next);
            } else {
                Node newNode = new Node(key, 1);
                insertNext(tail, newNode);
                map.put(key, newNode);
            }
        }
    }
    
    /** Decrements an existing key by 1. If Key's value is 1, remove it from the data structure. */
    public void dec(String key) {
        if (!map.containsKey(key)) {
            return;
        }
        
        Node node = map.get(key);
        if (node.freq == 1) {
            map.remove(key);
        } else if (node.freq - 1 == node.prev.freq) {
            node.prev.strs.add(key);
            map.put(key, node.prev);
        } else {
            Node newNode = new Node(key, node.freq - 1);
            map.put(key, newNode);
            insertNext(node.prev, newNode);
        }
        removeKeyFromNode(key, node);
    }
    
    /** Returns one of the keys with maximal value. */
    public String getMaxKey() {
        return head.prev.strs.isEmpty() ? "" : head.prev.strs.iterator().next();
    }
    
    /** Returns one of the keys with Minimal value. */
    public String getMinKey() {
        return tail.next.strs.isEmpty() ? "" : tail.next.strs.iterator().next();
    }
}

/**
 * Your Al