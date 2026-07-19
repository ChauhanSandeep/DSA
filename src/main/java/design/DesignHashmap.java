package design;

import java.util.*;

/**
 * Problem: Design HashMap
 *
 * Implement a map from integer keys to integer values without using built-in hash
 * table libraries. The public API must support put, get, and remove while returning
 * -1 for missing keys.
 *
 * Leetcode: https://leetcode.com/problems/design-hashmap/ (Easy)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hashing | Separate chaining
 *
 * Example:
 *   Input:  put(1,1), put(2,2), get(1), remove(2), get(2)
 *   Output: [1, -1]
 *   Why:    key 1 remains mapped to 1, while key 2 is deleted before the final read.
 *
 * Follow-ups:
 *   1. How would you handle high collision rates?
 *      Resize and rehash, or convert long chains into balanced trees.
 *   2. How would you support negative keys?
 *      Normalize the hash with floorMod before indexing into buckets.
 *   3. How would you make operations thread-safe?
 *      Use striped locks per bucket or guard the entire table with one lock.
 *
 * Related: Design HashSet (705), LRU Cache (146).
 */
public class DesignHashmap {
    
    /**
     * Hash Map implementation using separate chaining.
     * Uses array of linked lists to handle collisions.
     * 
     * Time Complexity: O(1) average, O(n) worst case
     * Space Complexity: O(n) where n is number of elements
     */
    public static class MyHashMap {
        private static final int INITIAL_SIZE = 1000;
        private Node[] buckets;
        private int size;
        
        private static class Node {
            int key;
            int value;
            Node next;
            
            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        
        public MyHashMap() {
            buckets = new Node[INITIAL_SIZE];
            size = 0;
        }
        
        /** Maps a key to a bucket index. */
        private int hash(int key) {
            return key % buckets.length;
        }
        
        /**
         * Inserts a new key-value pair or overwrites an existing key.
         *
         * Time:  O(1) average, O(n) worst case when many keys share a bucket.
         * Space: O(1) - adds at most one node.
         *
         * @param key key to insert or update
         * @param value value to store
         */
        public void put(int key, int value) {
            int index = hash(key);
            Node head = buckets[index];
            
            // Search for existing key
            Node current = head;
            while (current != null) {
                if (current.key == key) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            
            // Insert new node at head
            Node newNode = new Node(key, value);
            newNode.next = head;
            buckets[index] = newNode;
            size++;
        }
        
        /**
         * Returns the stored value for a key.
         *
         * Time:  O(1) average, O(n) worst case within one chain.
         * Space: O(1) - scans with one pointer.
         *
         * @param key key to read
         * @return value mapped to key, or -1 if absent
         */
        public int get(int key) {
            int index = hash(key);
            Node current = buckets[index];
            
            while (current != null) {
                if (current.key == key) {
                    return current.value;
                }
                current = current.next;
            }
            
            return -1;
        }
        
        /**
         * Removes a key from the map when present.
         *
         * Time:  O(1) average, O(n) worst case within one chain.
         * Space: O(1) - rewires existing nodes only.
         *
         * @param key key to remove
         */
        public void remove(int key) {
            int index = hash(key);
            Node head = buckets[index];
            
            if (head == null) return;
            
            // Remove head
            if (head.key == key) {
                buckets[index] = head.next;
                size--;
                return;
            }
            
            // Remove from middle/end
            Node current = head;
            while (current.next != null) {
                if (current.next.key == key) {
                    current.next = current.next.next;
                    size--;
                    return;
                }
                current = current.next;
            }
        }
    }
    
    /**
     * HashMap with dynamic resizing for better performance.
     * Resizes when load factor exceeds threshold.
     */
    public static class MyHashMapResizable {
        private static final int INITIAL_CAPACITY = 16;
        private static final double LOAD_FACTOR_THRESHOLD = 0.75;
        
        private Node[] table;
        private int size;
        private int capacity;
        
        private static class Node {
            int key;
            int value;
            Node next;
            
            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        
        public MyHashMapResizable() {
            capacity = INITIAL_CAPACITY;
            table = new Node[capacity];
            size = 0;
        }
        
        private int hash(int key) {
            return key % capacity;
        }
        
        private void resize() {
            Node[] oldTable = table;
            capacity *= 2;
            table = new Node[capacity];
            size = 0;
            
            // Rehash all elements
            for (Node head : oldTable) {
                while (head != null) {
                    put(head.key, head.value);
                    head = head.next;
                }
            }
        }
        
        public void put(int key, int value) {
            // Check if resize needed
            if (size >= capacity * LOAD_FACTOR_THRESHOLD) {
                resize();
            }
            
            int index = hash(key);
            Node head = table[index];
            Node current = head;
            
            // Update existing
            while (current != null) {
                if (current.key == key) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            
            // Insert new
            Node newNode = new Node(key, value);
            newNode.next = head;
            table[index] = newNode;
            size++;
        }
        
        public int get(int key) {
            int index = hash(key);
            Node current = table[index];
            
            while (current != null) {
                if (current.key == key) {
                    return current.value;
                }
                current = current.next;
            }
            
            return -1;
        }
        
        public void remove(int key) {
            int index = hash(key);
            Node head = table[index];
            
            if (head == null) return;
            
            if (head.key == key) {
                table[index] = head.next;
                size--;
                return;
            }
            
            Node current = head;
            while (current.next != null) {
                if (current.next.key == key) {
                    current.next = current.next.next;
                    size--;
                    return;
                }
                current = current.next;
            }
        }
    }
    
    /**
     * Open addressing implementation using linear probing.
     * More memory efficient but requires tombstone handling.
     */
    public static class MyHashMapOpenAddressing {
        private static final int INITIAL_CAPACITY = 1000;
        private static final int EMPTY = -1;
        private static final int DELETED = -2;
        
        private int[] keys;
        private int[] values;
        private int size;
        private int capacity;
        
        public MyHashMapOpenAddressing() {
            capacity = INITIAL_CAPACITY;
            keys = new int[capacity];
            values = new int[capacity];
            Arrays.fill(keys, EMPTY);
            size = 0;
        }
        
        private int hash(int key) {
            return key % capacity;
        }
        
        private int findSlot(int key) {
            int index = hash(key);
            
            while (keys[index] != EMPTY) {
                if (keys[index] == key) {
                    return index;
                }
                index = (index + 1) % capacity;
            }
            
            return index;
        }
        
        public void put(int key, int value) {
            if (size >= capacity * 0.75) {
                resize();
            }
            
            int index = findSlot(key);
            
            if (keys[index] == EMPTY || keys[index] == DELETED) {
                size++;
            }
            
            keys[index] = key;
            values[index] = value;
        }
        
        public int get(int key) {
            int index = hash(key);
            
            while (keys[index] != EMPTY) {
                if (keys[index] == key) {
                    return values[index];
                }
                index = (index + 1) % capacity;
            }
            
            return -1;
        }
        
        public void remove(int key) {
            int index = hash(key);
            
            while (keys[index] != EMPTY) {
                if (keys[index] == key) {
                    keys[index] = DELETED;
                    size--;
                    return;
                }
                index = (index + 1) % capacity;
            }
        }
        
        private void resize() {
            int[] oldKeys = keys;
            int[] oldValues = values;
            int oldCapacity = capacity;
            
            capacity *= 2;
            keys = new int[capacity];
            values = new int[capacity];
            Arrays.fill(keys, EMPTY);
            size = 0;
            
            for (int i = 0; i < oldCapacity; i++) {
                if (oldKeys[i] != EMPTY && oldKeys[i] != DELETED) {
                    put(oldKeys[i], oldValues[i]);
                }
            }
        }
    }

    public static void main(String[] args) {
        MyHashMap hashMap = new MyHashMap();
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        int[] got = {hashMap.get(1), hashMap.get(3)};
        int[] expected = {1, -1};
        System.out.printf("ops=put(1,1),put(2,2),get(1),get(3) -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));

        hashMap.put(2, 1);
        hashMap.remove(2);
        int[] gotAfterRemove = {hashMap.get(2)};
        int[] expectedAfterRemove = {-1};
        System.out.printf("ops=put(2,1),remove(2),get(2) -> %s  expected=%s%n",
                Arrays.toString(gotAfterRemove), Arrays.toString(expectedAfterRemove));
    }
}