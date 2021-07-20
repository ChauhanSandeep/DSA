package LinkedList;


import LinkedList.Util.LinkedList;
import LinkedList.Util.Node;

public class PalllindromeList {

    public static void main(String[] args) {
        Node head = new Node(1);
        LinkedList list = new LinkedList(head);
        list.add(new Node(2));
        list.add(new Node(3));
        list.add(new Node(2));
        list.add(new Node(1));
        System.out.println("Is linked list palindrome? " + isPalindrome(head));

    }

    /**
     * Find if the linked list is pallindrome.
     * Steps: 1. Find the middle element. 2. Reverse 2nd half of list. 3. Compare first hald and 2nd reversed half.
     * @param head
     * @return
     */
    public static boolean isPalindrome(Node head) {
        Node slow = head;
        Node fast = head;

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

    public static Node reverse(Node head) {
        Node prev = null;
        Node curr = head;
        Node next = head;

        while(curr != null) {
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    public static boolean compare(Node node1, Node node2) {
        while(node1 != null && node2 != null) {
            if(node1.data != node2.data) return false;
            node1 = node1.next;
            node2 = node2.next;
        }
        return true;
    }
}
