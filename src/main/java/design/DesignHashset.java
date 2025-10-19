package design;

import java.util.*;

/**
 * 705. Design HashSet
 * 
 * Problem: Design a HashSet without using built-in hash table libraries.
 * Implement add(key), remove(key), and contains(key) operations.
 * 
 * Example:
 * MyHashSet hashSet = new MyHashSet();
 * hashSet.add(1);
 * hashSet.contains(1); // returns true
 * hashSet.remove(1);
 * 
 * LeetCode: https://leetcode.com/problems/design-hashset
 * 
 * Follow-up questions:
 * Q: How to minimize memory usage for sparse data?
 * A: Use bit vector or compressed sparse representation.
 * 
 * Q: What's optimal bucket size for chaining?
 * A: Keep load factor around 0.75, resize dynamically.
 * 
 * Q: How to handle very large key ranges?
 * A: Use consistent hashing or hierarchical hash tables.
 */
public class DesignHashset {
    
    /**
     * HashSet using separate chaining with linked lists.
     * 
     * Time Complexity: O(1) average for all operations
     * Space Complexity: O(n) where n is number of elements
     */
    public static class MyHashSet {
        private static final int BUCKET_COUNT = 1000;
        private List<Integer>[] buckets;
        
        @SuppressWarnings("unchecked")
        public MyHashSet() {
            buckets = new List[BUCKET_COUNT];
            for (int i = 0; i < BUCKET_COUNT; i++) {
                buckets[i] = new ArrayList<>();
            }
        }
        
        private int hash(int key) {
            return key % BUCKET_COUNT;
        }
        
        public void add(int key) {
            int index = hash(key);
            if (!buckets[index].contains(key)) {
                buckets[index].add(key);
            }
        }
        
        public void remove(int key) {
            int index = hash(key);
            buckets[index].remove(Integer.valueOf(key));
        }
        
        public boolean contains(int key) {
            int index = hash(key);
            return buckets[index].contains(key);
        }
    }
    
    /**
     * Optimized version using boolean array for limited key range.
     * Most memory efficient for dense key distributions.
     */
    public static class MyHashSetArray {
        private boolean[] data;
        private static final int MAX_SIZE = 1000001;
        
        public MyHashSetArray() {
            data = new boolean[MAX_SIZE];
        }
        
        public void add(int key) {
            data[key] = true;
        }
        
        public void remove(int key) {
            data[key] = false;
        }
        
        public boolean contains(int key) {
            return data[key];
        }
    }
    
    /**
     * Binary Search Tree implementation for ordered operations.
     * Provides additional functionality like range queries.
     */
    public static class MyHashSetBST {
        private TreeNode root;
        
        private static class TreeNode {
            int val;
            TreeNode left;
            TreeNode right;
            
            TreeNode(int val) {
                this.val = val;
            }
        }
        
        public MyHashSetBST() {
            root = null;
        }
        
        public void add(int key) {
            root = addNode(root, key);
        }
        
        private TreeNode addNode(TreeNode node, int key) {
            if (node == null) {
                return new TreeNode(key);
            }
            
            if (key < node.val) {
                node.left = addNode(node.left, key);
            } else if (key > node.val) {
                node.right = addNode(node.right, key);
            }
            
            return node;
        }
        
        public void remove(int key) {
            root = removeNode(root, key);
        }
        
        private TreeNode removeNode(TreeNode node, int key) {
            if (node == null) return null;
            
            if (key < node.val) {
                node.left = removeNode(node.left, key);
            } else if (key > node.val) {
                node.right = removeNode(node.right, key);
            } else {
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;
                
                TreeNode minRight = findMin(node.right);
                node.val = minRight.val;
                node.right = removeNode(node.right, minRight.val);
            }
            
            return node;
        }
        
        private TreeNode findMin(TreeNode node) {
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }
        
        public boolean contains(int key) {
            return search(root, key);
        }
        
        private boolean search(TreeNode node, int key) {
            if (node == null) return false;
            if (key == node.val) return true;
            return key < node.val ? search(node.left, key) : search(node.right, key);
        }
    }
    
    /**
     * Bit vector implementation for memory efficiency.
     * Uses minimal space for large sparse sets.
     */
    public static class MyHashSetBitVector {
        private int[] bits;
        private static final int BITS_PER_INT = 32;
        private static final int MAX_SIZE = 1000001;
        
        public MyHashSetBitVector() {
            bits = new int[(MAX_SIZE + BITS_PER_INT - 1) / BITS_PER_INT];
        }
        
        public void add(int key) {
            int index = key / BITS_PER_INT;
            int bit = key % BITS_PER_INT;
            bits[index] |= (1 << bit);
        }
        
        public void remove(int key) {
            int index = key / BITS_PER_INT;
            int bit = key % BITS_PER_INT;
            bits[index] &= ~(1 << bit);
        }
        
        public boolean contains(int key) {
            int index = key / BITS_PER_INT;
            int bit = key % BITS_PER_INT;
            return (bits[index] & (1 << bit)) != 0;
        }
    }
    
    /**
     * Two-level hashing for better distribution.
     * Reduces collision probability significantly.
     */
    public static class MyHashSetTwoLevel {
        private static final int BUCKET_SIZE = 1000;
        private List<Integer>[] buckets;
        
        @SuppressWarnings("unchecked")
        public MyHashSetTwoLevel() {
            buckets = new List[BUCKET_SIZE];
        }
        
        private int hash1(int key) {
            return key % BUCKET_SIZE;
        }
        
        private int hash2(int key) {
            return key / BUCKET_SIZE;
        }
        
        public void add(int key) {
            int bucket = hash1(key);
            if (buckets[bucket] == null) {
                buckets[bucket] = new ArrayList<>();
            }
            
            if (!buckets[bucket].contains(key)) {
                buckets[bucket].add(key);
            }
        }
        
        public void remove(int key) {
            int bucket = hash1(key);
            if (buckets[bucket] != null) {
                buckets[bucket].remove(Integer.valueOf(key));
            }
        }
        
        public boolean contains(int key) {
            int bucket = hash1(key);
            return buckets[bucket] != null && buckets[bucket].contains(key);
        }
    }
}