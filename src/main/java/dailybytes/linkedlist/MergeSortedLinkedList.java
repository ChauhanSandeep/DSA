package dailybytes.linkedlist;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Arrays;

/**
 * Problem: Merge Two Sorted Lists
 *
 * Given two sorted linked lists of integers, merge their values into one sorted
 * linked list. The code walks both lists by index and repeatedly appends the
 * smaller current value.
 *
 * Leetcode: https://leetcode.com/problems/merge-two-sorted-lists/ (Easy)
 * Rating:   acceptance 68.6% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Linked list | Two pointers | Sorted merge
 *
 * Example:
 *   Input:  list1 = [2,4,6], list2 = [1,3,5,8,9]
 *   Output: [1,2,3,4,5,6,8,9]
 *   Why:    repeatedly taking the smaller current head preserves ascending order.
 *
 * Follow-ups:
 *   1. Merge by relinking nodes instead of copying values?
 *      Use a dummy node and advance through the original next pointers.
 *   2. Merge k sorted linked lists?
 *      Use a min-heap of current heads or divide and conquer pairwise merges.
 *   3. Merge descending lists?
 *      Reverse the comparison or reverse both lists before merging.
 *   4. Preserve stability when equal values appear?
 *      Take from list1 first on equality if list1's order should win.
 *
 * Related: Merge k Sorted Lists (23), Merge Sorted Array (88).
 */
public class MergeSortedLinkedList {

    public static void main(String[] args) {
        LinkedList<Integer> first = new LinkedList<>(Arrays.asList(2, 4, 6));
        LinkedList<Integer> second = new LinkedList<>(Arrays.asList(1, 3, 5, 8, 9));
        LinkedList<Integer> empty = new LinkedList<>();

        LinkedList<Integer>[] list1Inputs = new LinkedList[] { first, empty };
        LinkedList<Integer>[] list2Inputs = new LinkedList[] { second, new LinkedList<>(Arrays.asList(1, 2)) };
        String[] expected = { "[1, 2, 3, 4, 5, 6, 8, 9]", "[1, 2]" };

        for (int i = 0; i < list1Inputs.length; i++) {
            LinkedList<Integer> output = mergeSortedLinkedLists(list1Inputs[i], list2Inputs[i]);
            System.out.printf("lists=%s -> %s  expected=%s%n",
                Arrays.deepToString(new Object[] { list1Inputs[i], list2Inputs[i] }), output, expected[i]);
        }
    }

    /**
     * Intuition: because both inputs are already sorted, the smallest remaining
     * value must be at pointer1 or pointer2. Append the smaller value, advance
     * that pointer, and once one list is exhausted append the rest of the other.
     *
     * Algorithm:
     *   1. Start pointer1 and pointer2 at the front of list1 and list2.
     *   2. While both pointers are valid, append the smaller current value.
     *   3. Append any remaining values from list1.
     *   4. Append any remaining values from list2 and return resultList.
     *
     * Time:  O(n + m) - every value from both lists is appended once.
     * Space: O(n + m) - resultList stores all merged values.
     *
     * @param list1 first sorted linked list
     * @param list2 second sorted linked list
     * @return merged sorted linked list
     */
    public static LinkedList<Integer> mergeSortedLinkedLists(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> resultList = new LinkedList<>();

        int pointer1 = 0, pointer2 = 0;
        while (pointer1 < list1.size() && pointer2 < list2.size()) {
            if (list1.get(pointer1) < list2.get(pointer2)) {
                resultList.add(list1.get(pointer1));
                pointer1++;
            } else {
                resultList.add(list2.get(pointer2));
                pointer2++;
            }
        }

        // Add remaining elements from list1
        while (pointer1 < list1.size()) {
            resultList.add(list1.get(pointer1));
            pointer1++;
        }

        // Add remaining elements from list2
        while (pointer2 < list2.size()) {
            resultList.add(list2.get(pointer2));
            pointer2++;
        }

        return resultList;
    }
}
