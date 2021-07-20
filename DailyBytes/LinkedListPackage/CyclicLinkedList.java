package DailyBytes.LinkedListPackage;
import java.util.*;

public class CyclicLinkedList {

    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.push(new MyNode(1));
        list.push(new MyNode(2));
        list.push(new MyNode(3));
        list.push(new MyNode(1));

        boolean isCycle = findCycle(list);
        System.out.println("Does linked list contains cycle ? " + isCycle);

    }

    /**
     * Given a linked list, containing unique numbers, return whether or not it has a cycle.
     * Note: a cycle is a circular arrangement (i.e. one node points back to a previous node)
     */
    public static boolean findCycle(MyLinkedList list) {
        HashSet<Integer> set = new HashSet<>();
        MyNode head = list.getHead();
        MyNode temp = head;

        while(temp != null) {
            if(set.contains(temp.getData())) return true;
            set.add(temp.getData());
            temp = temp.getNext();
        }
        return false;
    }
}
