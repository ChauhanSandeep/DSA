package Array;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LongestConsecutiveSequence {
    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        int result = new LongestConsecutiveSequence().longestConsecutive(nums);
        System.out.println(result);
    }

    public int longestConsecutive(int[] nums) {
        Set<Integer> set = Arrays.stream(nums).boxed().collect(Collectors.toSet());

        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                int currMax = 1;
                set.remove(nums[i]);
                currMax += lowerSide(nums[i] - 1, set);
                currMax += higherSide(nums[i] + 1, set);
                max = Math.max(max, currMax);
            }
        }
        return max;

    }

    public int lowerSide(int num, Set<Integer> set) {
        int count = 0;

        while (set.contains(num)) {
            count++;
            set.remove(num);
            num--;
        }
        return count;
    }

    public int higherSide(int num, Set<Integer> set) {
        int count = 0;
        while (set.contains(num)) {
            count++;
            set.remove(num);
            num++;
        }
        return count;
    }

}
