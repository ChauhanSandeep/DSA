package AmazonOa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxEvents {
    public static void main(String[] args) {
        int[] arrivals = {1, 3, 3, 5, 7};
        int[] durations = {2, 2, 1, 2, 1};
        System.out.println(new MaxEvents().maxEvents(arrivals, durations));
    }

    public int maxEvents(int[] arrivals, int[] durations) {
        int size = arrivals.length;

        List<Interval> intervals = new ArrayList<>();
        for(int i=0; i<size; i++) {
            Interval interval = new Interval(arrivals[i], arrivals[i] + durations[i]);
            intervals.add(interval);
        }
        Collections.sort(intervals, (a, b) -> a.end - b.end);
        int droppedIntervals = 0;

        int lastEnd = 0;
        for(int i=0; i<size; i++) {
            Interval curr = intervals.get(i);
            if(curr.start < lastEnd) {
                droppedIntervals++;
            }else{
                lastEnd = curr.end;
            }
        }
        return size - droppedIntervals;
    }
}

class Interval {
    int start;
    int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }
}