package arrays.hashmap;

import java.util.*;


/**
 * Problem: Insert Delete GetRandom O(1)
 *
 * Design a set-like data structure that supports insert, remove, and getRandom.
 * insert and remove should report whether they changed the set, and getRandom
 * should return one stored value uniformly at random. All three operations must
 * run in average constant time.
 *
 * Leetcode: https://leetcode.com/problems/insert-delete-getrandom-o1/
 * Rating:   acceptance 55.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Design | Hash map | Array list with swap-delete
 *
 * Example:
 *   Input:  insert(1), remove(2), insert(2), remove(1), insert(2), getRandom()
 *   Output: true, false, true, true, false, 2
 *   Why:    after removing 1, the set contains only 2, so reinserting 2 fails and
 *           the only possible random value is 2.
 *
 * Follow-ups:
 *   1. How would you allow duplicate values?
 *      Map each value to a set of indices and update one index during swap-delete.
 *   2. How would you make getRandom weighted by value frequency?
 *      Store repeated values in the list or maintain prefix weights with updates.
 *   3. How would you make the structure thread-safe?
 *      Guard all list and map mutations with one lock, or use a read-write strategy.
 *
 * Related: Insert Delete GetRandom O(1) - Duplicates allowed (381).
 */
public class InsertDeleteGetRandom {
    public static void main(String[] args) {
        RandomizedSet randomizedSet = new RandomizedSet();
        boolean[] got = {
            randomizedSet.insert(1),
            randomizedSet.remove(2),
            randomizedSet.insert(2),
            randomizedSet.remove(1),
            randomizedSet.insert(2)
        };
        int randomValue = randomizedSet.getRandom();

        System.out.printf("ops=[insert1, remove2, insert2, remove1, insert2] -> %s  expected=[true, false, true, true, false]%n",
            java.util.Arrays.toString(got));
        System.out.printf("set=[2] getRandom -> %d  expected=2%n", randomValue);
    }
}

/**
 * RandomizedSet supports O(1) insertion, deletion, and random access.
 */
class RandomizedSet {
  private final List<Integer> elements;         // Stores values for random access
  private final Map<Integer, Integer> valueToIndexMap; // value -> index in elements
  private final Random random;                  // Random number generator

  /** Constructor to initialize data structures. */
  public RandomizedSet() {
    this.elements = new ArrayList<>();
    this.valueToIndexMap = new HashMap<>();
    this.random = new Random();
  }

  /**
     * Intuition: the map answers membership in constant time and also tells us
     * where a value lives in the list. Inserting a new value is just appending it
     * at the end of the list and storing that index. If the map already has the
     * value, the set would not change, so the method returns false. Because append
     * and hash-map put are both average constant time, insertion meets the target.
     *
     * Time:  O(1) - one average hash lookup, one hash put, and one list append.
     * Space: O(1) - one inserted value adds one list slot and one map entry.
     *
     * @param val value to insert
     * @return true if the value was absent and inserted, otherwise false
     */
  public boolean insert(int val) {
    if (valueToIndexMap.containsKey(val)) {
      return false;
    }
    valueToIndexMap.put(val, elements.size());
    elements.add(val);
    return true;
  }

  /**
     * Intuition: removing from the middle of an ArrayList would be O(n), so we
     * avoid shifting. The list order does not matter for a set, which lets us move
     * the last value into the deleted value's slot, update that moved value's map
     * index, and then pop the last slot. That keeps the list dense for getRandom
     * while preserving average constant-time removal.
     *
     * Time:  O(1) - all work is a constant number of hash-map and list operations.
     * Space: O(1) - removal only changes existing storage.
     *
     * @param val value to remove
     * @return true if the value was present and removed, otherwise false
     */
  public boolean remove(int val) {
    if (!valueToIndexMap.containsKey(val)) {
      return false;
    }

    int indexToRemove = valueToIndexMap.get(val);
    int lastElement = elements.get(elements.size() - 1);

    // Move last element into the position of the element to be removed
    elements.set(indexToRemove, lastElement);
    valueToIndexMap.put(lastElement, indexToRemove);

    // Remove last element and clean up map
    elements.remove(elements.size() - 1);
    valueToIndexMap.remove(val);

    return true;
  }

  /**
     * Intuition: getRandom needs uniform access over the current set. The ArrayList
     * is dense because removal always fills holes with the last element, so every
     * valid index from 0 to size - 1 holds exactly one set value. Choosing a random
     * index in that range therefore chooses every stored value with the same
     * probability, and list indexing is constant time.
     *
     * Time:  O(1) - random index generation and list access are constant time.
     * Space: O(1) - the operation allocates no data proportional to set size.
     *
     * @return a uniformly random stored value
     */
  public int getRandom() {
    int randomIndex = random.nextInt(elements.size());
    return elements.get(randomIndex);
  }
}