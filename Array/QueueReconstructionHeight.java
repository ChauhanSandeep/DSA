package Array;

import java.util.*;

/**
 * https://leetcode.com/problems/queue-reconstruction-by-height/
 */
public class QueueReconstructionHeight {
    public static void main(String[] args) {
        int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
        int[][] sortedPeople = new QueueReconstructionHeight().reconstructQueue(people);
        System.out.println(Arrays.deepToString(sortedPeople));
    }

    public int[][] reconstructQueue(int[][] people) {
        int len = people.length;
        List<People> list = new ArrayList<>();
        for(int[] p: people) {
            list.add(new People(p[0], p[1]));
        }
//        Sort in ascending order of k if heights are same else sort in descending order  based on height
        Collections.sort(list, (a, b) -> (a.height == b.height) ? a.front - b.front : b.height - a.height);

        List<People> sortedList = new ArrayList<>();
        for (People person : list) {
//            insert at the index and shift the number if it's already present
//            like for (5,0) shift the (7,0) and then add 5 in 0 th position
            sortedList.add(person.front, person);
        }
        int[][] result = new int[len][2];
        for(int i=0; i<len; i++) {
            result[i] = sortedList.get(i).toArray();
        }
        return result;
    }

    class People {
        int height;
        int front;

        public People(int height, int front) {
            this.height = height;
            this.front = front;
        }

        public int[] toArray() {
            return new int[] {height, front};
        }
    }
}


