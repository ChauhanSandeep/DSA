package Graph;

public class FibonacciSum {
    public static void main(String[] args) {
        System.out.println(new FibonacciSum().fibsum(11));
    }
    // 1, 1, 2, 3, 5
    // S(n) = F(n+2) – 1
    public int fibsum(int num) {
        int fib[] = new int[60];
        fib[0] = 0;
        fib[1] = 1;
        int i = 2;

        while (fib[i - 1] + fib[i - 2] <= num) {
            fib[i] = fib[i - 1] + fib[i - 2];
            i++;
        }
        i--;
        int count = 0;
        while (num > 0) {
            while (fib[i] > num) {
                i--;
            }
            num = num - fib[i];
            count++;
        }
        return count;
    }
}
