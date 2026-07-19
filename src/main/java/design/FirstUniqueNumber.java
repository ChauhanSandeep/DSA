package design;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Arrays;

/**
 * Problem: First Unique Number
 *
 * Maintain a queue-like stream of integers and return the first value that has
 * appeared exactly once so far. Adding a duplicate must remove that value from the
 * ordered unique candidates without disturbing the remaining order.
 *
 * Leetcode: https://leetcode.com/problems/first-unique-number/ (Medium)
 * Rating:   not available (design problem)
 * Pattern:  Design | Hash set | LinkedHashSet insertion order
 *
 * Example:
 *   Input:  nums = [2,3,5], add(5), add(2), showFirstUnique()
 *   Output: 3
 *   Why:    5 and 2 have both become duplicates, leaving 3 as the earliest unique value.
 *
 * Follow-ups:
 *   1. How would you support remove(value)?
 *      Track counts and original positions, then reinsert count-one values by order.
 *   2. How would you make it thread-safe?
 *      Guard both sets with one lock so updates stay atomic.
 *   3. How would you support arbitrary object keys?
 *      Generalize Integer to a type parameter and keep the same two-set design.
 *
 * Related: First Unique Character in a String (387), LRU Cache (146).
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

    public static void main(String[] args) {
        int[] nums = {2, 3, 5};
        FirstUniqueNumber firstUnique = new FirstUniqueNumber(nums);
        int[] got = {firstUnique.showFirstUnique()};
        int[] expected = {2};
        System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(nums), Arrays.toString(got), Arrays.toString(expected));

        firstUnique.add(5);
        firstUnique.add(2);
        firstUnique.add(3);
        int[] gotAfterAdds = {firstUnique.showFirstUnique()};
        int[] expectedAfterAdds = {-1};
        System.out.printf("ops=add(5),add(2),add(3),showFirstUnique -> %s  expected=%s%n",
                Arrays.toString(gotAfterAdds), Arrays.toString(expectedAfterAdds));
    }

}
