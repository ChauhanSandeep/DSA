package frazsheet;

import java.util.*;

/**
 * 381. Insert Delete GetRandom O(1) - Duplicates allowed
 * 
 * Problem: Design a data structure that supports insert, remove, and getRandom 
 * operations in average O(1) time, allowing duplicate values.
 * 
 * Example:
 * RandomizedCollection collection = new RandomizedCollection();
 * collection.insert(1);   // return true
 * collection.insert(1);   // return false (1 already exists)
 * collection.getRandom(); // return 1
 * 
 * LeetCode: https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed
 * 
 * Follow-up questions:
 * Q: How to optimize for very frequent getRandom operations?
 * A: Use multiple hash functions or weighted sampling for better distribution.
 * 
 * Q: What if we need to get random with specific probabilities?
 * A: Implement alias method or use prefix sums with binary search.
 * 
 * Q: How to handle thread safety?
 * A: Use concurrent data structures and proper synchronization.
 */
public class InsertDeleteGetrandomO1DuplicatesAllowed {
    
    /**
     * Randomized collection allowing duplicates with O(1) operations.
     * 
     * Algorithm: Array + HashMap of indices
     * - Use ArrayList to store values for O(1) random access
     * - Use HashMap<Value, Set<Indices>> to track all positions of each value
     * - Insert: add to end of list, update map
     * - Remove: swap with last element, update indices, remove last
     * - GetRandom: generate random index in list
     * 
     * Time Complexity: O(1) average for all operations
     * Space Complexity: O(n) where n is number of elements
     */
    public static class RandomizedCollection {
        private List<Integer> values;
        private Map<Integer, Set<Integer>> indices;
        private Random random;
        
        public RandomizedCollection() {
            values = new ArrayList<>();
            indices = new HashMap<>();
            random = new Random();
        }
        
        public boolean insert(int val) {
            boolean isNew = !indices.containsKey(val);
            
            // Add value to list
            values.add(val);
            int index = values.size() - 1;
            
            // Update indices map
            indices.computeIfAbsent(val, k -> new HashSet<>()).add(index);
            
            return isNew;
        }
        
        public boolean remove(int val) {
            if (!indices.containsKey(val) || indices.get(val).isEmpty()) {
                return false;
            }
            
            // Get an index to remove (any index from the set)
            Set<Integer> valIndices = indices.get(val);
            int indexToRemove = valIndices.iterator().next();
            valIndices.remove(indexToRemove);
            
            int lastIndex = values.size() - 1;
            int lastElement = values.get(lastIndex);
            
            // Move last element to the position of element to remove
            values.set(indexToRemove, lastElement);
            
            // Update indices for the moved element
            Set<Integer> lastElementIndices = indices.get(lastElement);
            lastElementIndices.remove(lastIndex);
            if (indexToRemove != lastIndex) {
                lastElementIndices.add(indexToRemove);
            }
            
            // Remove last element
            values.remove(lastIndex);
            
            return true;
        }
        
        public int getRandom() {
            if (values.isEmpty()) {
                throw new IllegalStateException("Collection is empty");
            }
            
            int randomIndex = random.nextInt(values.size());
            return values.get(randomIndex);
        }
    }
    
    /**
     * Alternative implementation using LinkedHashSet for stable ordering.
     * Maintains insertion order within duplicate sets.
     */
    public static class RandomizedCollectionOrdered {
        private List<Integer> values;
        private Map<Integer, LinkedHashSet<Integer>> indices;
        private Random random;
        
        public RandomizedCollectionOrdered() {
            values = new ArrayList<>();
            indices = new HashMap<>();
            random = new Random();
        }
        
        public boolean insert(int val) {
            boolean isNew = !indices.containsKey(val);
            
            values.add(val);
            int index = values.size() - 1;
            
            indices.computeIfAbsent(val, k -> new LinkedHashSet<>()).add(index);
            
            return isNew;
        }
        
        public boolean remove(int val) {
            if (!indices.containsKey(val) || indices.get(val).isEmpty()) {
                return false;
            }
            
            LinkedHashSet<Integer> valIndices = indices.get(val);
            int indexToRemove = valIndices.iterator().next();
            valIndices.remove(indexToRemove);
            
            int lastIndex = values.size() - 1;
            int lastElement = values.get(lastIndex);
            
            values.set(indexToRemove, lastElement);
            
            LinkedHashSet<Integer> lastElementIndices = indices.get(lastElement);
            lastElementIndices.remove(lastIndex);
            if (indexToRemove != lastIndex) {
                lastElementIndices.add(indexToRemove);
            }
            
            values.remove(lastIndex);
            
            return true;
        }
        
        public int getRandom() {
            if (values.isEmpty()) {
                throw new IllegalStateException("Collection is empty");
            }
            
            return values.get(random.nextInt(values.size()));
        }
    }
    
    /**
     * Thread-safe implementation using concurrent data structures.
     * Ensures consistency across multiple threads.
     */
    public static class RandomizedCollectionThreadSafe {
        private final List<Integer> values;
        private final Map<Integer, Set<Integer>> indices;
        private final Random random;
        private final Object lock = new Object();
        
        public RandomizedCollectionThreadSafe() {
            values = new ArrayList<>();
            indices = new java.util.concurrent.ConcurrentHashMap<>();
            random = new Random();
        }
        
        public boolean insert(int val) {
            synchronized (lock) {
                boolean isNew = !indices.containsKey(val);
                
                values.add(val);
                int index = values.size() - 1;
                
                indices.computeIfAbsent(val, k -> 
                    java.util.concurrent.ConcurrentHashMap.newKeySet()).add(index);
                
                return isNew;
            }
        }
        
        public boolean remove(int val) {
            synchronized (lock) {
                if (!indices.containsKey(val) || indices.get(val).isEmpty()) {
                    return false;
                }
                
                Set<Integer> valIndices = indices.get(val);
                int indexToRemove = valIndices.iterator().next();
                valIndices.remove(indexToRemove);
                
                int lastIndex = values.size() - 1;
                int lastElement = values.get(lastIndex);
                
                values.set(indexToRemove, lastElement);
                
                Set<Integer> lastElementIndices = indices.get(lastElement);
                lastElementIndices.remove(lastIndex);
                if (indexToRemove != lastIndex) {
                    lastElementIndices.add(indexToRemove);
                }
                
                values.remove(lastIndex);
                
                return true;
            }
        }
        
        public int getRandom() {
            synchronized (lock) {
                if (values.isEmpty()) {
                    throw new IllegalStateException("Collection is empty");
                }
                
                return values.get(random.nextInt(values.size()));
            }
        }
    }
    
    /**
     * Memory-optimized version using primitive collections.
     * Reduces memory overhead for integer values.
     */
    public static class RandomizedCollectionOptimized {
        private int[] values;
        private int size;
        private Map<Integer, Set<Integer>> indices;
        private Random random;
        private static final int INITIAL_CAPACITY = 16;
        
        public RandomizedCollectionOptimized() {
            values = new int[INITIAL_CAPACITY];
            size = 0;
            indices = new HashMap<>();
            random = new Random();
        }
        
        public boolean insert(int val) {
            boolean isNew = !indices.containsKey(val);
            
            // Resize if needed
            if (size >= values.length) {
                resize();
            }
            
            values[size] = val;
            indices.computeIfAbsent(val, k -> new HashSet<>()).add(size);
            size++;
            
            return isNew;
        }
        
        public boolean remove(int val) {
            if (!indices.containsKey(val) || indices.get(val).isEmpty()) {
                return false;
            }
            
            Set<Integer> valIndices = indices.get(val);
            int indexToRemove = valIndices.iterator().next();
            valIndices.remove(indexToRemove);
            
            int lastIndex = size - 1;
            int lastElement = values[lastIndex];
            
            values[indexToRemove] = lastElement;
            
            Set<Integer> lastElementIndices = indices.get(lastElement);
            lastElementIndices.remove(lastIndex);
            if (indexToRemove != lastIndex) {
                lastElementIndices.add(indexToRemove);
            }
            
            size--;
            
            return true;
        }
        
        public int getRandom() {
            if (size == 0) {
                throw new IllegalStateException("Collection is empty");
            }
            
            return values[random.nextInt(size)];
        }
        
        private void resize() {
            int[] newValues = new int[values.length * 2];
            System.arraycopy(values, 0, newValues, 0, size);
            values = newValues;
        }
    }
    
    /**
     * Implementation with additional statistics tracking.
     * Provides insights into collection usage patterns.
     */
    public static class RandomizedCollectionWithStats {
        private List<Integer> values;
        private Map<Integer, Set<Integer>> indices;
        private Random random;
        
        // Statistics
        private int insertCount;
        private int removeCount;
        private int getRandomCount;
        private Map<Integer, Integer> valueFrequency;
        
        public RandomizedCollectionWithStats() {
            values = new ArrayList<>();
            indices = new HashMap<>();
            random = new Random();
            valueFrequency = new HashMap<>();
        }
        
        public boolean insert(int val) {
            insertCount++;
            boolean isNew = !indices.containsKey(val);
            
            values.add(val);
            int index = values.size() - 1;
            
            indices.computeIfAbsent(val, k -> new HashSet<>()).add(index);
            valueFrequency.put(val, valueFrequency.getOrDefault(val, 0) + 1);
            
            return isNew;
        }
        
        public boolean remove(int val) {
            removeCount++;
            if (!indices.containsKey(val) || indices.get(val).isEmpty()) {
                return false;
            }
            
            Set<Integer> valIndices = indices.get(val);
            int indexToRemove = valIndices.iterator().next();
            valIndices.remove(indexToRemove);
            
            int lastIndex = values.size() - 1;
            int lastElement = values.get(lastIndex);
            
            values.set(indexToRemove, lastElement);
            
            Set<Integer> lastElementIndices = indices.get(lastElement);
            lastElementIndices.remove(lastIndex);
            if (indexToRemove != lastIndex) {
                lastElementIndices.add(indexToRemove);
            }
            
            values.remove(lastIndex);
            valueFrequency.put(val, valueFrequency.get(val) - 1);
            if (valueFrequency.get(val) == 0) {
                valueFrequency.remove(val);
            }
            
            return true;
        }
        
        public int getRandom() {
            getRandomCount++;
            if (values.isEmpty()) {
                throw new IllegalStateException("Collection is empty");
            }
            
            return values.get(random.nextInt(values.size()));
        }
        
        // Statistics methods
        public int size() {
            return values.size();
        }
        
        public int getInsertCount() {
            return insertCount;
        }
        
        public int getRemoveCount() {
            return removeCount;
        }
        
        public int getGetRandomCount() {
            return getRandomCount;
        }
        
        public Map<Integer, Integer> getValueFrequency() {
            return new HashMap<>(valueFrequency);
        }
    }
}