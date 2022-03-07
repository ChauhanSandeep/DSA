package Hashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://www.interviewbit.com/problems/largest-continuous-sequence-zero-sum/
 */
public class ZeroSum {
    public static void main(String[] args) {
        ArrayList<Integer> list = (ArrayList<Integer>) Stream.of(1 ,2 ,-2 ,4 ,-4).collect(Collectors.toList());
        ArrayList<Integer> result = new ZeroSum().lszero(list);
        System.out.println(result);
    }

    public ArrayList<Integer> lszero(ArrayList<Integer> list) {
        int size = list.size();
        ArrayList<Integer> result = new ArrayList<>();

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);

        int start = -1;
        int end = -1;
        int maxLen = -1;
        int sum = 0;

        for(int i=0; i<size; i++) {
            sum += list.get(i);
            if(map.containsKey(sum)) {
                if(maxLen < i - map.get(sum)) {
                    start = map.get(sum) + 1;
                    end = i;
                    maxLen = end - start + 1;
                }
            }else{
                map.put(sum, i);
            }
        }

        if(maxLen != -1) {
            for(int i= start; i<=end; i++) {
                result.add(list.get(i));
            }
        }

        return result;
    }
}
