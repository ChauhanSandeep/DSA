package arrays.hashmap;

import java.util.*;


/**
 * Problem: Insert Delete GetRandom O(1)
 * LeetCode: https://leetcode.com/problems/insert-delete-getrandom-o1/
 *
 * Design a set data structure that supports:
 * - insert(val): Inserts an item val to the set if not already present. If present, return false.
 * - remove(val): Removes an item val from the set if present.
 * - getRandom(): Returns a random element from the set.
 *
 * ✅ All operations must run in average O(1) time.
 *
 * 📌 Example:
 * Input:
 * RandomizedSet randomizedSet = new RandomizedSet();
 * randomizedSet.insert(1); // true
 * randomizedSet.remove(2); // false
 * randomizedSet.insert(2); // true
 * randomizedSet.getRandom(); // 1 or 2
 * randomizedSet.remove(1); // true
 * randomizedSet.insert(2); // false
 * randomizedSet.getRandom(); // 2
 *
 * 🧠 Key Insight:
 * - Use ArrayList to store elements for O(1) random access
 * - Use HashMap to track element index for O(1) insert/remove
 * - On remove, swap with last element and delete in O(1)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
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
 * RandomizedSet supports O(1) insertion, deletion, and random access.
 */
class RandomizedSet {
  private final List<Integer> elements;         // Stores values for random access
  private final Map<Integer, Integer> indexMap; // Maps value -> index in elements
  private final Random random;                  // Random number generator

  /** Constructor to initialize data structures. */
  public RandomizedSet() {
    this.elements = new ArrayList<>();
    this.indexMap = new HashMap<>();
    this.random = new Random();
  }

  /**
   * Inserts a value into the set.
   * @param val Value to insert.
   * @return True if insertion is successful (i.e., not already present), false otherwise.
   *
   * Time: O(1)
   * Space: O(1)
   */
  public boolean insert(int val) {
    if (indexMap.containsKey(val)) {
      return false;
    }
    indexMap.put(val, elements.size());
    elements.add(val);
    return true;
  }

  /**
   * Removes a value from the set.
   * @param val Value to remove.
   * @return True if removal is successful, false if value was not present.
   *
   * Approach:
   * - Swap `val` with the last element.
   * - Update map and remove last element.
   *
   * Time: O(1)
   * Space: O(1)
   */
  public boolean remove(int val) {
    if (!indexMap.containsKey(val)) {
      return false;
    }

    int indexToRemove = indexMap.get(val);
    int lastElement = elements.get(elements.size() - 1);

    // Move last element into the position of the element to be removed
    elements.set(indexToRemove, lastElement);
    indexMap.put(lastElement, indexToRemove);

    // Remove last element and clean up map
    elements.remove(elements.size() - 1);
    indexMap.remove(val);

    return true;
  }

  /**
   * Returns a random element from the set.
   * @return A random element uniformly picked.
   *
   * Time: O(1)
   * Space: O(1)
   */
  public int getRandom() {
    int randomIndex = random.nextInt(elements.size());
    return elements.get(randomIndex);
  }
}