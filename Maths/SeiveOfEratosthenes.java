package Maths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeiveOfEratosthenes {

    public static void main(String[] args) {
        System.out.println("Total primes are " + new SeiveOfEratosthenes().countPrimes(10));
    }

    public List<Integer> countPrimes(int n) {
        if(n <= 2) return new ArrayList<>();

        boolean[] notPrime = new boolean[n];

        for(int i=2; i*i<n; i++) {
            if(!notPrime[i]) {
                for(int j=i*i; j<n; j+=i) {
                    notPrime[j] = true;
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        for(int i=2; i<n; i++) {
            if(!notPrime[i]) {
                result.add(i);
            }
        }
        return result;
    }
}
