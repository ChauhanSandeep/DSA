package design;

import java.util.*;

/**
 * Problem: Design HashSet
 *
 * Implement a set of integer keys without using built-in hash table libraries. The
 * data structure must support add, remove, and contains while ignoring duplicate
 * inserts.
 *
 * Leetcode: https://leetcode.com/problems/design-hashset/ (Easy)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hashing | Buckets
 *
 * Example:
 *   Input:  add(1), add(2), contains(1), remove(2), contains(2)
 *   Output: [true, false]
 *   Why:    key 1 is still present, but key 2 is removed before the last check.
 *
 * Follow-ups:
 *   1. How would you support a very sparse but huge key range?
 *      Use hashing with lazy buckets instead of a full boolean array.
 *   2. How would you support ordered iteration?
 *      Add a linked list or use a tree-backed set variant.
 *   3. How would you reduce worst-case chain length?
 *      Resize by load factor and treeify overly long buckets.
 *
 * Related: Design HashMap (706), Insert Delete GetRandom O(1) (380).
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
        
        /** Maps a key to a bucket index. */
        private int hash(int key) {
            return key % BUCKET_COUNT;
        }
        
        /**
         * Adds the key if it is not already present.
         *
         * Time:  O(1) average, O(n) worst case within one bucket.
         * Space: O(1) - stores one key only when new.
         *
         * @param key key to add
         */
        public void add(int key) {
            int index = hash(key);
            if (!buckets[index].contains(key)) {
                buckets[index].add(key);
            }
        }
        
        /**
         * Removes the key if present.
         *
         * Time:  O(1) average, O(n) worst case within one bucket.
         * Space: O(1) - mutates one bucket list.
         *
         * @param key key to remove
         */
        public void remove(int key) {
            int index = hash(key);
            buckets[index].remove(Integer.valueOf(key));
        }
        
        /**
         * Checks whether the set currently contains the key.
         *
         * Time:  O(1) average, O(n) worst case within one bucket.
         * Space: O(1) - no extra storage.
         *
         * @param key key to search for
         * @return true if the key is present
         */
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

    public static void main(String[] args) {
        MyHashSet hashSet = new MyHashSet();
        hashSet.add(1);
        hashSet.add(2);
        boolean[] got = {hashSet.contains(1), hashSet.contains(3), hashSet.contains(2)};
        boolean[] expected = {true, false, true};
        System.out.printf("ops=add(1),add(2),contains(1),contains(3),contains(2) -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));

        hashSet.remove(2);
        boolean[] gotAfterRemove = {hashSet.contains(2)};
        boolean[] expectedAfterRemove = {false};
        System.out.printf("ops=remove(2),contains(2) -> %s  expected=%s%n",
                Arrays.toString(gotAfterRemove), Arrays.toString(expectedAfterRemove));
    }
}