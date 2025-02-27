package Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Find the smallest multiple of a number using only 0s and 1s.
 * Uses BFS and remainder tracking to construct the smallest valid number.
 * https://www.youtube.com/watch?v=Om47LiGTy8o
 */
public class SmallestMultiple {

    public static void main(String[] args) {
        int num = 7;
        String multiple = new SmallestMultiple().multiple(num);
        System.out.println("Smallest multiple of " + num + " with only 0s and 1s: " + multiple);
    }

    /**
     * Optimized BFS approach to find the smallest multiple containing only 0s and 1s.
     * @param num The number whose multiple we need to find.
     * @return Smallest multiple in string format.
     */
    public String multiple(int num) {
        if (num == 1) return "1";

        Queue<Integer> queue = new LinkedList<>();
        char[] charArr = new char[num]; // Stores '0' or '1' for each remainder
        int[] parentArr = new int[num]; // Stores the parent remainder for backtracking

        Arrays.fill(charArr, '\0'); // Uninitialized state
        Arrays.fill(parentArr, -1);

        charArr[1] = '1'; // Starting remainder
        queue.offer(1);

        while (!queue.isEmpty()) {
            int r = queue.poll();

            int r0 = (r * 10) % num;
            int r1 = (r * 10 + 1) % num;

            if (charArr[r0] == '\0') {
                charArr[r0] = '0';
                parentArr[r0] = r;
                queue.offer(r0);
            }
            if (charArr[r1] == '\0') {
                charArr[r1] = '1';
                parentArr[r1] = r;
                queue.offer(r1);
            }
        }

        // Backtrack to reconstruct the number
        StringBuilder builder = new StringBuilder();
        int rem = 0;  // Start backtracking from remainder 0
        while (rem != -1) {
            builder.insert(0, charArr[rem]);
            rem = parentArr[rem];
        }

        return builder.toString();
    }
}
