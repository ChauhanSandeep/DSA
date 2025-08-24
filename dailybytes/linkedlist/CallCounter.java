package dailybytes.linkedlist;
import java.util.*;

public class CallCounter {
    public static void main(String[] args) {
        System.out.println(ping(1));
        System.out.println(ping(300));
        System.out.println(ping(3000));
        System.out.println(ping(3002));
        System.out.println(ping(7000));
    }
    static TreeSet<Integer> set = new TreeSet<>();

    /**
     * Create a class CallCounter that tracks the number of calls a client has made within the last 3 seconds.
     * Your class should contain one method, ping(int t) that receives the current timestamp (in milliseconds) of
     * a new call being made and returns the number of calls made within the last 3 seconds.
     * Note: you may assume that the time associated with each subsequent call to ping is strictly increasing.
     */
    public static int ping(int time) {
        set.add(time);
        return set.tailSet(time - 3000).size();
    }
}
