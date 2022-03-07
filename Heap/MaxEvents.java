package Heap;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/
 */
public class MaxEvents {
    public static void main(String[] args) {
        int[][] events = {
                {1, 4},
                {4, 4},
                {2, 2},
                {3, 4},
                {1, 1}
        };
        System.out.println(new MaxEvents().maxEvents(events));
    }

    /**
     * sort the events array by startTime and poll from queue sorting in endTime
     * @param events
     * @return
     */
    public int maxEvents(int[][] events) {
        if(events == null || events.length == 0) return 0;

        Arrays.sort(events, (a, b) -> {
            if(a[0] == b[0]) return a[1] - b[1];
            return a[0] - b[0];
        });

        // Queue returns event ending first
        PriorityQueue<Integer> attendableEvents = new PriorityQueue<>();

        int index = 0;
        int result = 0;
        int len = events.length;
        int today = events[0][0];

        while(!attendableEvents.isEmpty() || index<len) {
            while(!attendableEvents.isEmpty() && attendableEvents.peek() < today){
                // events ended before today. cannot attend
                attendableEvents.poll();
            }

            while(index < len && events[index][0] == today) {
                // events starting today
                attendableEvents.offer(events[index][1]);
                index++;
            }

            if(!attendableEvents.isEmpty()) {
                // attend event
                result++;
                attendableEvents.poll();
            }
            today++;
        }
        return result;
    }
}
