package Graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Find smallest multiple of a number which only contains 0s and 1s
 * https://www.youtube.com/watch?v=Om47LiGTy8o
 */
public class SmallestMultiple {

    public static void main(String[] args) {
        String multiple = new SmallestMultiple().multiple(7);
        System.out.println(multiple);
    }

    /**
     * This is basic approach. Gives TLE for large inputs
     **/
    public String multipleBasic(int num) {
        Queue<String> queue = new LinkedList<>();
        queue.offer("1");

        while(!queue.isEmpty()) {
            String curr = queue.poll();
            if(isDivisible(curr, num)) return curr;
            queue.offer(curr+"0");
            queue.offer(curr+"1");
        }
        return "";
    }

    public boolean isDivisible(String numerator, int denominator) {
        int remainder = 0;
        for(int i=0; i<numerator.length(); i++) {
            remainder = remainder * 10 + (numerator.charAt(i) - '0');
            remainder = remainder % denominator;
        }
        return remainder == 0;
    }

    /**
     * This is most optimum approach
     * Add `0` and `1` to the current number and store remainder in queue
     * corresponding char to remainder is stored in `charArr`
     * parent to backtrack is stored in `parent`
     * @param num
     * @return
     */
    public String multiple(int num) {
        if(num == 1) return "1";

        Queue<Integer> queue = new LinkedList<>();
        char[] charArr = new char[num];
        int[] parentArr = new int[num];
        Arrays.fill(charArr, '2');
        Arrays.fill(parentArr, -1);

        charArr[1] = '1';
        queue.offer(1);

        while(!queue.isEmpty()) {
            int r = queue.poll();
            if(r == 0) break;
            int r0 = (r*10 + 0)%num;
            int r1 = (r*10 + 1)%num;

            if(charArr[r0] == '2') {
                charArr[r0] = '0';
                parentArr[r0] = r;
                queue.offer(r0);
            }
            if(charArr[r1] == '2'){
                charArr[r1] = '1';
                parentArr[r1] = r;
                queue.offer(r1);
            }
        }
        StringBuilder builder = new StringBuilder();
        int rem = 0;
        while(rem != 1) {
            builder.insert(0, charArr[rem]);
            rem = parentArr[rem];
        }
        builder.insert(0, '1');
        return builder.toString();
    }
}
