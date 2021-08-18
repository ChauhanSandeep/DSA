package LinkedList;


import LinkedList.Util.LinkedList;
import LinkedList.Util.ListNode;

public class PalllindromeList {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        LinkedList list = new LinkedList(head);
        list.add(new ListNode(2));
        list.add(new ListNode(3));
        list.add(new ListNode(2));
        list.add(new ListNode(1));
        System.out.println("Is linked list palindrome? " + isPalindrome(head));

    }

    /**
     * Find if the linked list is pallindrome.
     * Steps: 1. Find the middle element. 2. Reverse 2nd half of list. 3. Compare first hald and 2nd reversed half.
     * @param head
     * @return
     */
    public static boolean isPalindrome(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        boolean isEven = false;
        if(fast == null) {
            isEven = true;
        }
        if(!isEven) {
            slow = slow.next;
        }
        fast = reverse(slow);
        return compare(head, fast);

    }

    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = head;

        while(curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static boolean compare(ListNode node1, ListNode node2) {
        while(node1 != null && node2 != null) {
            if(node1.val != node2.val) return false;
            node1 = node1.next;
            node2 = node2.next;
        }
        return true;
    }
}
