package DailyBytes.LinkedListPackage;

import java.util.Iterator;
import java.util.LinkedList;

public class MergeSortedLinkedList {

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(2);
        list.add(4);
        list.add(6);

        LinkedList<Integer> list1 = new LinkedList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);
        list1.add(8);
        list1.add(9);

        LinkedList<Integer> resultList = mergeSortedLinkedList(list, list1);
        Iterator<Integer> iterator = resultList.iterator();

        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    /**
     *Given two sorted linked lists, merge them together in ascending order and return a reference to the merged list
     */
    public static LinkedList<Integer> mergeSortedLinkedList(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> resultList = new LinkedList<Integer>();

        int i=0, j=0;
        while(i < list1.size() && j < list2.size()) {
            if(list1.get(i) < list2.get(j)) {
                resultList.add(list1.get(i));
                i++;
            }else{
                resultList.add(list2.get(j));
                j++;
            }
        }

        while(i < list1.size()) {
            resultList.add(list1.get(i));
            i++;
        }
        while(j < list2.size()) {
            resultList.add(list2.get(j));
            j++;
        }
        return resultList;
    }
}
