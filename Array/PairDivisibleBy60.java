package Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Find all the pairs of song divisible by 60
 */
public class PairDivisibleBy60 {

    public static void main(String[] args) {
        int[] times = {30,20,150,100,40};
        int i = new PairDivisibleBy60().numPairsDivisibleBy60(times);
        System.out.println(i);
    }

    public int numPairsDivisibleBy60(int[] time) {
        Map<Integer, Integer> map = new HashMap<>();
        int pairs = 0;

        for(int t: time) {
            int effTime = t%60;
            int other = (effTime == 0) ? 0 : 60 - effTime;
            pairs += map.getOrDefault(other, 0);
            map.put(effTime, map.getOrDefault(effTime, 0) + 1);
        }

        return pairs;
    }

    public int numPairsDivisibleBy60UsingArray(int[] time) {
        int reminders[] = new int[60];
        int pairs = 0;

        for(int t: time) {
            int effTime = t%60;
            if(effTime == 0) {
                pairs += reminders[0];
            }else{
                pairs += reminders[60 - effTime];
            }
            reminders[effTime]++;
        }
        return pairs;
    }
}
