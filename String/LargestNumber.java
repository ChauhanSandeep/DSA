package String;

import java.util.Arrays;

/**
 * Create largest number from the list of integers. Return number as string.
 */
public class LargestNumber {
    public static void main(String[] args) {
        int[] nums = {3,30,34,5,9};
        String result = new LargestNumber().largestNumber(nums);
        System.out.println("Largest number is " + result);
    }
    public String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for(int i=0; i<nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }

        Arrays.sort(strs, (a, b) -> (b+a).compareTo(a+b));
        StringBuilder builder = new StringBuilder();
        for(String str: strs) {
            builder.append(str);
        }
        if(builder.charAt(0) == '0') return "0";
        return builder.toString();
    }
}
