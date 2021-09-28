package slidingwindow;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Find maximum of all subarray of size k
 * https://leetcode.com/problems/sliding-window-maximum/
 * https://www.youtube.com/watch?v=xFJXtB5vSmM
 */
public class MaxSlidingWindow {

    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        int[] result = new MaxSlidingWindow().maxSlidingWindow(nums, 3);
        System.out.println(Arrays.toString(result));
    }

    static int[] maxSlidingWindow(int[] nums, int k) {
        int len = nums.length;
        Deque<Integer> queue = new LinkedList<>();
        int[] result = new int[len - k + 1];
        int index = 0;


        int last;
        for (last = 0; last < k; ++last) {
            // elements in queue lesser than current element can never be max of any sliding window.
            // so removing those elements
            while (!queue.isEmpty() && nums[last] >= nums[queue.peekLast()]) {
                queue.removeLast();
            }

            queue.addLast(last);
        }

        for (; last < len; ++last) {
            result[index++] = nums[queue.peek()];

            // removing indexes lesser that window start
            while ((!queue.isEmpty()) && queue.peek() <= last - k) {
                queue.removeFirst();
            }

            // elements in queue lesser than current element can never be max of any sliding window.
            // so removing those elements
            while ((!queue.isEmpty()) && nums[last] >= nums[queue.peekLast()]) {
                queue.removeLast();
            }

            queue.addLast(last);
        }

        result[index] = nums[queue.peek()];
        return result;
    }
}
