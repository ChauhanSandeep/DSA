package Array;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * https://leetcode.com/problems/minimum-cost-to-connect-sticks/
 * Connect all sticks with min cost. The cost of connecting two sticks of length a and b is (a+b)
 */
public class ConnectedStick {

    public static void main(String[] args) {
        int[] sticks = {1,8,3,5};
        int result = new ConnectedStick().connectSticks(sticks);
        System.out.println(result);
    }

    //The approach is to use smallest stick first.
    // But this approach fails because adding two small stick might become larger than some other stick.
    // Therefore simple sorting does not work
    public int connectSticksWrong(int[] sticks) {
        Arrays.sort(sticks);
        /*
        1, 3, 5, 8

        1 + 3 = 4
        4 + 5 = 9
        9 + 8 = 17

        1 + 3 + (1 + 3 + 5) + (1 + 3 + 5 + 8) = 30.
         */
        int result = 0;
        int sum = sticks[0];
        for(int i=1; i<sticks.length; i++) {
            result += sticks[i] + sum;
            sum += sticks[i];
        }
        return result;
    }

    // The correct approach is to use priority queue
    public int connectSticks(int[] sticks) {
        int result = 0;

        PriorityQueue<Integer> queue = new PriorityQueue<>();

        for(int stick: sticks) {
            queue.offer(stick);
        }

        while(queue.size() > 1) {
            int stick1 = queue.poll();
            int stick2 = queue.poll();

            int joinedStick = stick1 + stick2;
            result += joinedStick;
            queue.offer(joinedStick);
        }
        return result;
    }


}
