package DynamicProgramming;

public class MinOperations {
    public static void main(String[] args) {
        int input = 3;
        int operations = minOperation(input);
        System.out.println(operations);
    }

    /**
     * Given a number N. Find the minimum number of operations required to reach N starting from 0. You have 2 operations available
     * 1. Double the number
     * 2. Add one to the number
     * @param input
     * @return
     */
    public static int minOperation(int input) {
        int[] arr = new int[input+1];
        arr[0] = 0;
        arr[1] = 1;
        for(int i=2; i<input+1; i++) {
            if(i%2 == 0)
                arr[i] = Math.min(arr[i/2] + 1, arr[i-1] + 1);
            else
                arr[i] = arr[i-1] + 1;
        }
        return arr[arr.length - 1];
    }
}
