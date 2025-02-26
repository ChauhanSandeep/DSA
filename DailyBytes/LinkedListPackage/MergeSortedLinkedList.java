package DailyBytes.LinkedListPackage;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class merges two sorted linked lists into a single sorted linked list.
 * 
 * Algorithm:
 * - Use two pointers to traverse both lists and add the smaller element to the result list.
 * - Once one list is exhausted, add the remaining elements of the other list to the result list.
 * - Time Complexity: O(n + m) where n and m are the lengths of the two input lists.
 * - Space Complexity: O(n + m)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/merge-two-sorted-lists/
 */
public class MergeSortedLinkedList {

    public static void main(String[] args) {
        LinkedList<Integer> list1 = new LinkedList<>();
        list1.add(2);
        list1.add(4);
        list1.add(6);

        LinkedList<Integer> list2 = new LinkedList<>();
        list2.add(1);
        list2.add(3);
        list2.add(5);
        list2.add(8);
        list2.add(9);

        LinkedList<Integer> resultList = mergeSortedLinkedLists(list1, list2);
        Iterator<Integer> iterator = resultList.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     * Given two sorted linked lists, merge them together in ascending order and return a reference to the merged list.
     * @param list1 The first sorted linked list.
     * @param list2 The second sorted linked list.
     * @return The merged sorted linked list.
     */
    public static LinkedList<Integer> mergeSortedLinkedLists(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> resultList = new LinkedList<>();

        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) < list2.get(j)) {
                resultList.add(list1.get(i));
                i++;
            } else {
                resultList.add(list2.get(j));
                j++;
            }
        }

        // Add remaining elements from list1
        while (i < list1.size()) {
            resultList.add(list1.get(i));
            i++;
        }

        // Add remaining elements from list2
        while (j < list2.size()) {
            resultList.add(list2.get(j));
            j++;
        }

        return resultList;
    }
}
