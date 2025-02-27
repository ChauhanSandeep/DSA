package Graph;

import java.util.ArrayList;
import java.util.List;

public class FibonacciSum {
    public static void main(String[] args) {
        System.out.println(new FibonacciSum().fibsum(11)); // Output: 2 (8 + 3)
        System.out.println(new FibonacciSum().fibsum(17)); // Output: 3 (13 + 3 + 1)
        System.out.println(new FibonacciSum().fibsum(19)); // Output: 3 (13 + 5 + 1)
    }

    public int fibsum(int num) {
        // Generate Fibonacci numbers up to num
        List<Integer> fibList = new ArrayList<>();
        fibList.add(1);
        fibList.add(1);

        while (fibList.get(fibList.size() - 1) + fibList.get(fibList.size() - 2) <= num) {
            fibList.add(fibList.get(fibList.size() - 1) + fibList.get(fibList.size() - 2));
        }

        int count = 0;
        int fibIndex = fibList.size() - 1;

        // Find the minimum number of Fibonacci numbers that sum to num
        while (num > 0) {
            while (fibList.get(fibIndex) > num) {
                fibIndex--;
            }
            num -= fibList.get(fibIndex);
            count++;
        }
        return count;
    }
}
