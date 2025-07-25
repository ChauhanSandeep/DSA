package LinkedList;

import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

/**
 * ✅ Problem: Check if a linked list is a palindrome.
 * 🔗 LeetCode: https://leetcode.com/problems/palindrome-linked-list/
 *
 * 🔍 **Approach:**
 * 1️⃣ Find the middle of the linked list using slow & fast pointers.
 * 2️⃣ Reverse the second half of the list.
 * 3️⃣ Compare the first half with the reversed second half.
 *      - If they are the same, the list is a palindrome.
 *      - If they differ, the list is not a palindrome.
 * 4️⃣ Restore the list to its original state (optional, useful in real-world scenarios).
 *
 * 📊 **Time Complexity:** O(N) - Traverses the list twice.
 * 🛠 **Space Complexity:** O(1) - Uses constant extra space.
 */
public class PalindromeList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(2));
        list.add(new ListNode(1));

        System.out.println("Is linked list a palindrome? " + isPalindrome(head));
    }

    /**
     * Determines if a linked list is a palindrome.
     * @param head Head of the linked list.
     * @return True if the list is a palindrome, else False.
     */
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true; // Edge case: Empty or single-node list

        // Step 1: Find the middle of the linked list
        ListNode middleNode = findMiddle(head);

        // Step 2: Reverse the second half of the list
        ListNode reversedSecondHalf = reverseList(middleNode);

        // Step 3: Compare both halves
        return isEqual(head, reversedSecondHalf);
    }

    /**
     * Uses the slow and fast pointer technique to find the middle of the list.
     * @param head Head of the list.
     * @return The starting node of the second half of the list.
     */
    private static ListNode findMiddle(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow; // Middle of the list (or second half start in even-length lists)
    }

    /**
     * Reverses a linked list.
     * @param head Head of the list to reverse.
     * @return New head of the reversed list.
     */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head, next;

        while (curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }

    /**
     * Compares two linked lists for equality.
     * @param first First list's head.
     * @param second Second list's head.
     * @return True if both lists are identical, otherwise False.
     */
    private static boolean isEqual(ListNode first, ListNode second) {
        while (second != null) { // Only check second half (first half is always longer or equal)
            if (first.val != second.val) return false;
            first = first.next;
            second = second.next;
        }
        return true;
    }
}
