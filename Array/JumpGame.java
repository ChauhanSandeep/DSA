package Array;

import java.security.SecureRandom;
import java.util.*;

/**
 * This class implements a data structure that supports O(1) operations for:
 * - Inserting a value.
 * - Removing a value.
 * - Getting a random value.
 *
 * Approach:
 * - Use an ArrayList (`values`) to store the elements.
 * - Use a HashMap (`valueToIndexMap`) to track the index of each value in the list.
 * - During removal, swap the element to be removed with the last element to maintain O(1) deletion.
 *
 * Time Complexity:
 * - Insert: O(1)
 * - Remove: O(1)
 * - Get Random: O(1)
 *
 * Space Complexity:
 * - O(N) where N is the number of elements stored.
 *
 * LeetCode Problem: https://leetcode.com/problems/insert-delete-getrandom-o1/
 */
public class InsertDeleteGetRandom {
    public static void main(String[] args) {
        RandomizedSet randomizedSet = new RandomizedSet();
        
        System.out.println(randomizedSet.insert(1));  // true (insert successful)
        System.out.println(randomizedSet.remove(2));  // false (2 not present)
        System.out.println(randomizedSet.insert(2));  // true (insert successful)
        System.out.println(randomizedSet.getRandom()); // Randomly returns 1 or 2
        System.out.println(randomizedSet.remove(1));  // true (1 removed)
        System.out.println(randomizedSet.insert(2));  // false (2 already exists)
        System.out.println(randomizedSet.getRandom()); // Always returns 2 (since only 2 is left)
    }
}

class RandomizedSet {
    private final List<Integer> values; // Stores elements
    private final Map<Integer, Integer> valueToIndexMap; // Maps value to index in list
    private final SecureRandom random; // SecureRandom for better randomness

    /** Initializes the data structure. */
    public RandomizedSet() {
        values = new ArrayList<>();
        valueToIndexMap = new HashMap<>();
        random = new SecureRandom();
    }

    /**
     * Inserts a value into the set.
     * @param val The value to insert.
     * @return True if insertion is successful (value was not already present).
     */
    public boolean insert(int val) {
        if (valueToIndexMap.containsKey(val)) {
            return false; // Value already exists
        }

        values.add(val);
        valueToIndexMap.put(val, values.size() - 1);
        return true;
    }

    /**
     * Removes a value from the set.
     * @param val The value to remove.
     * @return True if removal is successful, false if value does not exist.
     */
    public boolean remove(int val) {
        if (!valueToIndexMap.containsKey(val)) {
            return false; // Value not found
        }

        int index = valueToIndexMap.get(val);
        int lastElement = values.get(values.size() - 1);

        // Swap the value with the last element (if not already the last one)
        Collections.swap(values, index, values.size() - 1);

        // Update the moved element's index in the map
        valueToIndexMap.put(lastElement, index);
        
        // Remove the last element from the list and the map
        values.remove(values.size() - 1);
        valueToIndexMap.remove(val);

        return true;
    }

    /**
     * Gets a random value from the set.
     * @return A randomly selected value.
     */
    public int getRandom() {
        if (values.isEmpty()) {
            throw new NoSuchElementException("Cannot get random from an empty set.");
        }
        return values.get(random.nextInt(values.size()));
    }
}
