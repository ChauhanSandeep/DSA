package design;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 1429. First Unique Number
 *
 * Problem: You have a queue of integers, you need to retrieve the first unique
 * integer in the queue. Implement the FirstUnique class:
 *  - FirstUnique(int[] nums) Initializes the object with the numbers in the queue.
 *  - int showFirstUnique() returns the value of the first unique integer of the
 *    queue, and returns -1 if there is no such integer.
 *  - void add(int value) inserts value to the queue.
 *
 * LeetCode: https://leetcode.com/problems/first-unique-number
 *
 * Follow-up questions:
 * Q: How would you make this thread-safe for concurrent producers/consumers?
 * A: Guard the map and set with a single lock, or use ConcurrentHashMap together
 *    with a synchronized LinkedHashSet, or a custom doubly-linked list of unique
 *    nodes protected by a ReentrantLock.
 *
 * Q: Can we support remove(value) efficiently as well?
 * A: Yes - decrement the count; if it drops to 1, re-insert into the order
 *    structure at its original position. To preserve the original insertion
 *    order on re-insertion, track each value's first-seen index and use a
 *    TreeMap<Index, Value> instead of LinkedHashSet.
 *
 * Q: What if values can be very large or arbitrary objects?
 * A: Replace Integer with a generic type K; both HashMap and LinkedHashSet
 *    already support generics with O(1) average operations.
 *
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
class FirstUniqueNumber {

    private final Set<Integer> isSeen;
    private final LinkedHashSet<Integer> orderedSet;

    /**
     * Initializes the queue with the given numbers.
     *
     * Algorithm:
     *  - isSeen tracks every value ever added (so we can detect duplicates in O(1)).
     *  - orderedSet (LinkedHashSet) holds the values that are currently unique,
     *    in their original insertion order.
     *  - For each number we delegate to {@link #add(int)} so the bookkeeping
     *    stays in a single place.
     *
     * Time Complexity:  O(N) where N = nums.length.
     * Space Complexity: O(N).
     */
    public FirstUniqueNumber(int[] nums) {
        isSeen = new HashSet<>();
        orderedSet = new LinkedHashSet<>();
        for (int num : nums) {
            add(num);
        }
    }

    /**
     * Returns the value of the first unique integer in the queue, or -1 if none.
     *
     * Time Complexity:  O(1) - peek the first element of the LinkedHashSet.
     * Space Complexity: O(1).
     */
    public int showFirstUnique() {
        if (orderedSet.isEmpty()) {
            return -1;
        }
        return orderedSet.iterator().hasNext() ? orderedSet.iterator().next() : -1;
    }

    /**
     * Inserts value into the queue and updates the unique-tracking structures:
     *  - If value is being seen for the first time, mark it seen and add it to
     *    orderedSet (it is currently unique).
     *  - Otherwise it has occurred before, so remove it from orderedSet (it is
     *    no longer unique). The remove is a no-op if it was already removed by
     *    a previous duplicate.
     *
     * Time Complexity:  O(1) average.
     * Space Complexity: O(1) per call (amortized O(N) overall).
     */
    public void add(int value) {
        if (!isSeen.contains(value)) {
            isSeen.add(value);
            orderedSet.add(value);
        } else {
            orderedSet.remove(value);
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(new int[]{2, 3, 5});
        System.out.println(firstUnique.showFirstUnique()); // 2
        firstUnique.add(5);
        System.out.println(firstUnique.showFirstUnique()); // 2
        firstUnique.add(2);
        System.out.println(firstUnique.showFirstUnique()); // 3
        firstUnique.add(3);
        System.out.println(firstUnique.showFirstUnique()); // -1
    }
}
