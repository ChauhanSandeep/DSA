package AmazonOa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LargeLeftSum {
    public static void main(String[] args) {
        System.out.println(new LargeLeftSum().splitIntoTwo(Stream.of(10, 4, -8, 7)
                .collect(Collectors.toList())));
    }

    public int splitIntoTwo(List<Integer> dataset) {
        List<Integer> prefixSum = new ArrayList<>();
        int sum = 0;
        for(Integer num: dataset) {
            sum += num;
            prefixSum.add(sum);
        }
        int count = 0;
        for(Integer leftSum: prefixSum) {
            if(leftSum > sum - leftSum) count++;
        }
        return count;
    }
}
