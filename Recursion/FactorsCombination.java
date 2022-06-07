package Recursion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Find all the combinations of factors of number n
 */
public class FactorsCombination {
    public static void main(String[] args) {
        List<List<Integer>> factors = new FactorsCombination().getFactors(12);
        System.out.println(factors);
    }

    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> result = new ArrayList<>();
        if(n <= 1) return result;
        result.add(Stream.of(n).collect(Collectors.toList()));
        int limit = (int)Math.sqrt(n);

        for(int i=2; i <= limit; i++) {
            if(n%i == 0) {
                List<List<Integer>> list = getFactors(n/i);
                for(List<Integer> innerList: list) {
                    innerList.add(i);
                    result.add(innerList);
                }
            }
        }
        return result;
    }
}
