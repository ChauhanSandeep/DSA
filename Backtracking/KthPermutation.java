package Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Find kth increasing permutation of a String of length n
 * n == 3 then string is 123
 */
public class KthPermutation {

    public static void main(String[] args) {
        String result = new KthPermutation().getPermutation(4, 17);
        System.out.println(result);

    }

    public String getPermutation(int n, int k) {
        int fact = 1;
        List<Integer> numbers = new ArrayList<>();
        for(int i=1; i<n; i++) {
            fact = fact*i;
            numbers.add(i);
        }
        numbers.add(n);
        String ans = "";
        k = k-1;
        while(true) {
            ans = ans + numbers.get(k/fact);
            numbers.remove(k/fact);
            if(numbers.size() == 0) break;
            k = k%fact;
            fact = fact/numbers.size();
        }
        return ans;
    }
}
