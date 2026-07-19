package design;

import java.util.*;

/**
 * Problem: LRU Cache
 *
 * Design a fixed-capacity cache with get and put in O(1) time. Whenever the cache
 * is full, inserting a new key evicts the least recently used key. Successful get
 * and put operations mark the key as most recently used.
 *
 * Leetcode: https://leetcode.com/problems/lru-cache/ (Medium)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hash map | Doubly linked list recency order
 *
 * Example:
 *   Input:  capacity = 2, put(1,1), put(2,2), get(1), put(3,3), get(2)
 *   Output: [1, -1]
 *   Why:    get(1) refreshes key 1, so key 2 is least recent and is evicted by put(3,3).
 *
 * Follow-ups:
 *   1. How would you make it thread-safe?
 *      Guard map and list mutations with one lock or use segmented caches.
 *   2. How would you add time-based expiration?
 *      Store expiry timestamps in nodes and remove expired entries on access or cleanup.
 *   3. How would you support a different eviction policy?
 *      Keep key lookup in a map and replace the recency list with a policy-specific order.
 *
 * Related: LFU Cache (460), Design Browser History (1472).
 */
public class LRUCache {
    
    /**
     * Classic implementation using HashMap + Doubly Linked List.
     * 
     * Algorithm: Hash table with doubly-linked list
     * - HashMap provides O(1) key lookup
     * - Doubly-linked list maintains access order
     * - Move accessed nodes to head (most recent)
     * - Remove from tail when capacity exceeded
     * 
     * Time Complexity: O(1) for both get and put operations
     * Space Complexity: O(capacity) for storage
     */
    
    private final int capacity;
    private final Map<Integer, Node> cache;
    private final Node head;
    private final Node tail;
    
    /**
     * Creates an empty cache with dummy head and tail nodes for O(1) list edits.
     *
     * Time:  O(1) - initializes the map and sentinel nodes.
     * Space: O(1) - no real cache entries are stored yet.
     *
     * @param capacity maximum number of key-value pairs to keep
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        
        // Create dummy head and tail nodes
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);
        head.next = tail;
        tail.prev = head;
    }
    
    /**
     * Reads a value and marks the key as most recently used when present.
     *
     * Time:  O(1) - one map lookup plus constant pointer rewiring.
     * Space: O(1) - uses only a node reference.
     *
     * @param key cache key to read
     * @return cached value, or -1 if the key is absent
     */
    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) {
            return -1;
        }
        
        // Move to head (mark as recently used)
        moveToHead(node);
        return node.value;
    }
    
    /**
     * Inserts or updates a key and evicts the least recently used key if full.
     *
     * Time:  O(1) - map operations and list edits are constant time.
     * Space: O(1) - at most one new node is created.
     *
     * @param key cache key to insert or update
     * @param value value to store
     */
    public void put(int key, int value) {
        Node existing = cache.get(key);
        
        if (existing != null) {
            // Update existing node
            existing.value = value;
            moveToHead(existing);
        } else {
            // Add new node
            Node newNode = new Node(key, value);
            
            if (cache.size() >= capacity) {
                // Remove least recently used (tail)
                Node lru = tail.prev;
                removeNode(lru);
                cache.remove(lru.key);
            }
            
            addToHead(newNode);
            cache.put(key, newNode);
        }
    }
    
    // Doubly-linked list node
    private static class Node {
        int key, value;
        Node prev, next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    
    // Add node right after head
    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    // Remove node from list
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    // Move node to head
    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }
    
    /**
     * Alternative implementation using LinkedHashMap.
     * Leverages Java's built-in LinkedHashMap for simplicity.
     */
    public static class LRUCacheLinkedHashMap {
        private final int capacity;
        private final LinkedHashMap<Integer, Integer> cache;
        
        public LRUCacheLinkedHashMap(int capacity) {
            this.capacity = capacity;
            this.cache = new LinkedHashMap<Integer, Integer>(16, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > capacity;
                }
            };
        }
        
        public int get(int key) {
            return cache.getOrDefault(key, -1);
        }
        
        public void put(int key, int value) {
            cache.put(key, value);
        }
    }
    
    /**
     * Thread-safe implementation using synchronized methods.
     * Ensures correctness under concurrent access.
     */
    public static class LRUCacheThreadSafe {
        private final int capacity;
        private final Map<Integer, Node> cache;
        private final Node head;
        private final Node tail;
        private final Object lock = new Object();
        
        public LRUCacheThreadSafe(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }
        
        public int get(int key) {
            synchronized (lock) {
                Node node = cache.get(key);
                if (node == null) {
                    return -1;
                }
                
                moveToHead(node);
                return node.value;
            }
        }
        
        public void put(int key, int value) {
            synchronized (lock) {
                Node existing = cache.get(key);
                
                if (existing != null) {
                    existing.value = value;
                    moveToHead(existing);
                } else {
                    Node newNode = new Node(key, value);
                    
                    if (cache.size() >= capacity) {
                        Node lru = tail.prev;
                        removeNode(lru);
                        cache.remove(lru.key);
                    }
                    
                    addToHead(newNode);
                    cache.put(key, newNode);
                }
            }
        }
        
        // Same helper methods as base implementation
        private void addToHead(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }
        
        private static class Node {
            int key, value;
            Node prev, next;
            
            Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * Enhanced LRU Cache with additional features.
     * Includes statistics, TTL support, and bulk operations.
     */
    public static class EnhancedLRUCache {
        private final int capacity;
        private final Map<Integer, Node> cache;
        private final Node head;
        private final Node tail;
        
        // Statistics
        private long hits = 0;
        private long misses = 0;
        private long evictions = 0;
        
        public EnhancedLRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            
            this.head = new Node(-1, -1, -1);
            this.tail = new Node(-1, -1, -1);
            head.next = tail;
            tail.prev = head;
        }
        
        public int get(int key) {
            Node node = cache.get(key);
            if (node == null) {
                misses++;
                return -1;
            }
            
            // Check TTL
            if (node.expireTime > 0 && System.currentTimeMillis() > node.expireTime) {
                removeNode(node);
                cache.remove(key);
                misses++;
                return -1;
            }
            
            hits++;
            moveToHead(node);
            return node.value;
        }
        
        public void put(int key, int value) {
            put(key, value, -1); // No TTL
        }
        
        public void put(int key, int value, long ttlMillis) {
            Node existing = cache.get(key);
            long expireTime = ttlMillis > 0 ? System.currentTimeMillis() + ttlMillis : -1;
            
            if (existing != null) {
                existing.value = value;
                existing.expireTime = expireTime;
                moveToHead(existing);
            } else {
                Node newNode = new Node(key, value, expireTime);
                
                if (cache.size() >= capacity) {
                    Node lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                    evictions++;
                }
                
                addToHead(newNode);
                cache.put(key, newNode);
            }
        }
        
        // Bulk operations
        public Map<Integer, Integer> getAll(List<Integer> keys) {
            Map<Integer, Integer> result = new HashMap<>();
            for (int key : keys) {
                int value = get(key);
                if (value != -1) {
                    result.put(key, value);
                }
            }
            return result;
        }
        
        public void putAll(Map<Integer, Integer> entries) {
            for (Map.Entry<Integer, Integer> entry : entries.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
        
        // Statistics
        public CacheStats getStats() {
            return new CacheStats(hits, misses, evictions, cache.size(), capacity);
        }
        
        // Clear expired entries
        public int evictExpired() {
            long now = System.currentTimeMillis();
            int evicted = 0;
            
            Iterator<Map.Entry<Integer, Node>> iterator = cache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Node> entry = iterator.next();
                Node node = entry.getValue();
                
                if (node.expireTime > 0 && now > node.expireTime) {
                    removeNode(node);
                    iterator.remove();
                    evicted++;
                }
            }
            
            return evicted;
        }
        
        private static class Node {
            int key, value;
            long expireTime;
            Node prev, next;
            
            Node(int key, int value, long expireTime) {
                this.key = key;
                this.value = value;
                this.expireTime = expireTime;
            }
        }
        
        private void addToHead(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }
    }
    
    // Statistics class
    public static class CacheStats {
        public final long hits;
        public final long misses;
        public final long evictions;
        public final int currentSize;
        public final int capacity;
        
        public CacheStats(long hits, long misses, long evictions, int currentSize, int capacity) {
            this.hits = hits;
            this.misses = misses;
            this.evictions = evictions;
            this.currentSize = currentSize;
            this.capacity = capacity;
        }
        
        public double getHitRate() {
            long total = hits + misses;
            return total == 0 ? 0.0 : (double) hits / total;
        }
        
        @Override
        public String toString() {
            return String.format("Hits: %d, Misses: %d, Hit Rate: %.2f%%, Evictions: %d, Size: %d/%d",
                               hits, misses, getHitRate() * 100, evictions, currentSize, capacity);
        }
    }
    
    /**
     * Generic LRU Cache supporting any key-value types.
     * Template implementation for type safety.
     */
    public static class GenericLRUCache<K, V> {
        private final int capacity;
        private final Map<K, Node<K, V>> cache;
        private final Node<K, V> head;
        private final Node<K, V> tail;
        
        public GenericLRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();
            
            this.head = new Node<>(null, null);
            this.tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }
        
        public V get(K key) {
            Node<K, V> node = cache.get(key);
            if (node == null) {
                return null;
            }
            
            moveToHead(node);
            return node.value;
        }
        
        public void put(K key, V value) {
            Node<K, V> existing = cache.get(key);
            
            if (existing != null) {
                existing.value = value;
                moveToHead(existing);
            } else {
                Node<K, V> newNode = new Node<>(key, value);
                
                if (cache.size() >= capacity) {
                    Node<K, V> lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                }
                
                addToHead(newNode);
                cache.put(key, newNode);
            }
        }
        
        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev, next;
            
            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        
        private void addToHead(Node<K, V> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node<K, V> node) {
            removeNode(node);
            addToHead(node);
        }
    }
    
    /**
     * LRU Cache with size-based eviction.
     * Tracks memory usage and evicts based on total size.
     */
    public static class SizeBasedLRUCache {
        private final long maxSize;
        private final Map<Integer, Node> cache;
        private final Node head;
        private final Node tail;
        private long currentSize;
        
        public SizeBasedLRUCache(long maxSize) {
            this.maxSize = maxSize;
            this.cache = new HashMap<>();
            this.currentSize = 0;
            
            this.head = new Node(-1, null, 0);
            this.tail = new Node(-1, null, 0);
            head.next = tail;
            tail.prev = head;
        }
        
        public byte[] get(int key) {
            Node node = cache.get(key);
            if (node == null) {
                return null;
            }
            
            moveToHead(node);
            return node.value;
        }
        
        public void put(int key, byte[] value) {
            long valueSize = value.length;
            Node existing = cache.get(key);
            
            if (existing != null) {
                currentSize -= existing.size;
                existing.value = value;
                existing.size = valueSize;
                currentSize += valueSize;
                moveToHead(existing);
            } else {
                // Evict until we have enough space
                while (currentSize + valueSize > maxSize && !cache.isEmpty()) {
                    Node lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                    currentSize -= lru.size;
                }
                
                if (currentSize + valueSize <= maxSize) {
                    Node newNode = new Node(key, value, valueSize);
                    addToHead(newNode);
                    cache.put(key, newNode);
                    currentSize += valueSize;
                }
            }
        }
        
        private static class Node {
            int key;
            byte[] value;
            long size;
            Node prev, next;
            
            Node(int key, byte[] value, long size) {
                this.key = key;
                this.value = value;
                this.size = size;
            }
        }
        
        private void addToHead(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        private void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        private void moveToHead(Node node) {
            removeNode(node);
            addToHead(node);
        }
        
        public long getCurrentSize() {
            return currentSize;
        }
        
        public long getMaxSize() {
            return maxSize;
        }
    }
    
    /**
     * Persistent LRU Cache with serialization support.
     * Can save/load cache state to/from disk.
     */
    public static class PersistentLRUCache {
        private final EnhancedLRUCache cache;
        private final String persistenceFile;
        
        public PersistentLRUCache(int capacity, String persistenceFile) {
            this.cache = new EnhancedLRUCache(capacity);
            this.persistenceFile = persistenceFile;
        }
        
        public int get(int key) {
            return cache.get(key);
        }
        
        public void put(int key, int value) {
            cache.put(key, value);
        }
        
        // Save cache state (simplified - would use proper serialization)
        public void save() {
            // Implementation would serialize cache state to file
            System.out.println("Saving cache to " + persistenceFile);
        }
        
        // Load cache state (simplified - would use proper deserialization)
        public void load() {
            // Implementation would load cache state from file
            System.out.println("Loading cache from " + persistenceFile);
        }
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        int firstGet = cache.get(1);
        cache.put(3, 3);
        int secondGet = cache.get(2);
        int[] got = {firstGet, secondGet};
        int[] expected = {1, -1};
        System.out.printf("ops=put(1),put(2),get(1),put(3),get(2) -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));

        cache.put(4, 4);
        int[] gotAfterEviction = {cache.get(1), cache.get(3), cache.get(4)};
        int[] expectedAfterEviction = {-1, 3, 4};
        System.out.printf("ops=put(4),get(1),get(3),get(4) -> %s  expected=%s%n",
                Arrays.toString(gotAfterEviction), Arrays.toString(expectedAfterEviction));
    }
}