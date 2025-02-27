package Array;

import java.util.*;

public class InsertDeleteGetRandom {
    public static void main(String[] args) {
        RandomizedSet randomizedSet = new RandomizedSet();
        System.out.println(randomizedSet.insert(1)); // true
        System.out.println(randomizedSet.remove(2)); // false
        System.out.println(randomizedSet.insert(2)); // true
        System.out.println(randomizedSet.getRandom()); // 1 or 2
        System.out.println(randomizedSet.remove(1)); // true
        System.out.println(randomizedSet.insert(2)); // false
        System.out.println(randomizedSet.getRandom()); // 2
    }
}

/**
 * Create a set that provides insert, delete, and getRandom functionality in O(1) time complexity.
 */
class RandomizedSet {
    private final List<Integer> elements;
    private final Map<Integer, Integer> indexMap;
    private final Random random;

    public RandomizedSet() {
        elements = new ArrayList<>();
        indexMap = new HashMap<>();
        random = new Random();
    }

    /** Inserts a value to the set. Returns true if the value was not already present. */
    public boolean insert(int val) {
        if (indexMap.containsKey(val)) {
            return false;
        }
        indexMap.put(val, elements.size());
        elements.add(val);
        return true;
    }

    /** Removes a value from the set. Returns true if the value was present. */
    public boolean remove(int val) {
        if (!indexMap.containsKey(val)) {
            return false;
        }
        int indexToRemove = indexMap.get(val);
        int lastElement = elements.get(elements.size() - 1);

        // Move the last element to the removed spot
        elements.set(indexToRemove, lastElement);
        indexMap.put(lastElement, indexToRemove);

        // Remove last element from list and map
        elements.remove(elements.size() - 1);
        indexMap.remove(val);
        return true;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        return elements.get(random.nextInt(elements.size()));
    }
}