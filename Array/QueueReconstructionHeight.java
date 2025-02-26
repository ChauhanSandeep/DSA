package Array;

import java.util.*;

/**
 * 🔗 LeetCode: https://leetcode.com/problems/queue-reconstruction-by-height/
 * ✅ Greedy + Sorting + Insertion
 */
public class QueueReconstructionHeight {
    public static void main(String[] args) {
        int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
        int[][] sortedPeople = reconstructQueue(people);
        System.out.println(Arrays.deepToString(sortedPeople));
    }

    public static int[][] reconstructQueue(int[][] people) {
        // Step 1: Sort array
        Arrays.sort(people, (a, b) -> (a[0] == b[0]) ? a[1] - b[1] : b[0] - a[0]);

        // Step 2: Insert elements at their correct positions
        List<int[]> sortedList = new LinkedList<>();
        for (int[] person : people) {
            sortedList.add(person[1], person);
        }

        // Step 3: Convert List back to array
        return sortedList.toArray(new int[people.length][2]);
    }
}
