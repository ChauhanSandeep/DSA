package design;

import java.util.*;

/**
 * Problem: Insert Delete GetRandom O(1) - Duplicates Allowed
 *
 * Design a randomized collection that supports inserting values, removing one
 * occurrence of a value, and returning a random stored value in average O(1) time.
 * Duplicate values are allowed, so each value must track all of its current indices.
 *
 * Leetcode: https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/ (Hard)
 * Rating:   not available (design problem)
 * Pattern:  Design | Array list | Hash map from value to index set
 *
 * Example:
 *   Input:  insert(1), insert(1), insert(2), remove(1)
 *   Output: [true, false, true, true]
 *   Why:    the second 1 is a duplicate, and remove deletes one existing occurrence.
 *
 * Follow-ups:
 *   1. How would you return random values with custom weights?
 *      Maintain prefix weights or an alias table instead of uniform array indices.
 *   2. How would you make the collection thread-safe?
 *      Guard the list and all index sets with one lock for each mutation/read.
 *   3. How would you support removeAll(value)?
 *      Delete every tracked index while repeatedly swapping with the last live element.
 *
 * Related: Insert Delete GetRandom O(1) (380), Random Pick Index (398).
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
        
        /**
         * Inserts one occurrence of a value.
         *
         * Time:  O(1) average - appends to the list and updates one index set.
         * Space: O(1) - stores one index for the new occurrence.
         *
         * @param val value to insert
         * @return true if the value did not already have an index set
         */
        public boolean insert(int val) {
            boolean isNew = !indices.containsKey(val);
            
            // Add value to list
            values.add(val);
            int index = values.size() - 1;
            
            // Update indices map
            indices.computeIfAbsent(val, k -> new HashSet<>()).add(index);
            
            return isNew;
        }
        
        /**
         * Removes one occurrence of a value when present.
         *
         * Time:  O(1) average - swaps with the last element and updates index sets.
         * Space: O(1) - mutates existing storage in place.
         *
         * @param val value to remove
         * @return true if an occurrence was removed
         */
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
        
        /**
         * Returns a uniformly random stored value.
         *
         * Time:  O(1) - samples one array-list index.
         * Space: O(1) - no extra storage.
         *
         * @return random value from the collection
         */
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

    public static void main(String[] args) {
        RandomizedCollection collection = new RandomizedCollection();
        boolean[] got = {collection.insert(1), collection.insert(1), collection.insert(2)};
        boolean[] expected = {true, false, true};
        System.out.printf("ops=insert(1),insert(1),insert(2) -> %s  expected=%s%n",
                Arrays.toString(got), Arrays.toString(expected));

        boolean[] gotAfterRemove = {collection.remove(1), collection.remove(3)};
        boolean[] expectedAfterRemove = {true, false};
        System.out.printf("ops=remove(1),remove(3) -> %s  expected=%s%n",
                Arrays.toString(gotAfterRemove), Arrays.toString(expectedAfterRemove));
    }
}