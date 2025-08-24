package array;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Rearrange Array Elements By Sign
 *
 * Problem: Given array with equal number of positive and negative integers,
 * rearrange so that positive and negative numbers alternate, starting with positive.
 *
 * Example: nums = [3,1,-2,-5,2,-4] -> Output: [3,-2,1,-5,2,-4]
 * Alternate positive and negative while maintaining relative order within each type.
 *
 * LeetCode: https://leetcode.com/problems/rearrange-array-elements-by-sign
 *
 * Follow-up Questions:
 * - What if counts of positive/negative are different? (Handle remaining elements separately)
 * - How to minimize space usage? (In-place rearrangement is complex but possible)
 * - What if we want to start with negative numbers? (Swap the roles of pos/neg indices)
 */
public class RearrangeArrayElementsBySign {

    /**
     * Rearranges array to alternate positive and negative numbers.
     *
     * Algorithm:
     * 1. Use two pointers: one for positive positions (0,2,4,...), one for negative (1,3,5,...)
     * 2. Iterate through original array
     * 3. Place positive numbers at even indices, negative numbers at odd indices
     * 4. Maintain relative order within each type
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(n) for result array
     *
     * @param nums input array with equal positive and negative numbers
     * @return rearranged array alternating positive and negative
     */
    public int[] rearrangeArray(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        int posIndex = 0; // Even indices for positive numbers
        int negIndex = 1; // Odd indices for negative numbers

        for (int num : nums) {
            if (num > 0) {
                result[posIndex] = num;
                posIndex += 2;
            } else {
                result[negIndex] = num;
                negIndex += 2;
            }
        }

        return result;
    }

    /**
     * Two-pass approach: collect positives and negatives separately
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] rearrangeArrayTwoPass(int[] nums) {
        List<Integer> positives = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();

        // Separate positive and negative numbers
        for (int num : nums) {
            if (num > 0) {
                positives.add(num);
            } else {
                negatives.add(num);
            }
        }

        // Merge alternately
        int[] result = new int[nums.length];
        for (int i = 0; i < positives.size(); i++) {
            result[2 * i] = positives.get(i);      // Even positions
            result[2 * i + 1] = negatives.get(i); // Odd positions
        }

        return result;
    }

    /**
     * Generic version that handles unequal counts
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] rearrangeArrayGeneric(int[] nums) {
        List<Integer> positives = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();

        for (int num : nums) {
            if (num > 0) {
                positives.add(num);
            } else {
                negatives.add(num);
            }
        }

        List<Integer> result = new ArrayList<>();
        int posIdx = 0, negIdx = 0;
        boolean usePositive = true; // Start with positive

        // Alternate until one list is exhausted
        while (posIdx < positives.size() && negIdx < negatives.size()) {
            if (usePositive) {
                result.add(positives.get(posIdx++));
            } else {
                result.add(negatives.get(negIdx++));
            }
            usePositive = !usePositive;
        }

        // Add remaining elements
        while (posIdx < positives.size()) {
            result.add(positives.get(posIdx++));
        }
        while (negIdx < negatives.size()) {
            result.add(negatives.get(negIdx++));
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * In-place rearrangement (complex but space-efficient)
     * Time Complexity: O(n), Space Complexity: O(1)
     * Note: This modifies the original array
     */
    public int[] rearrangeArrayInPlace(int[] nums) {
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            // If current position expects positive but has negative (or vice versa)
            if ((i % 2 == 0 && nums[i] < 0) || (i % 2 == 1 && nums[i] > 0)) {
                // Find next number of correct sign
                int j = i + 1;
                while (j < n && ((i % 2 == 0 && nums[j] < 0) || (i % 2 == 1 && nums[j] > 0))) {
                    j++;
                }

                // Rotate elements to bring correct sign to position i
                if (j < n) {
                    int temp = nums[j];
                    while (j > i) {
                        nums[j] = nums[j - 1];
                        j--;
                    }
                    nums[i] = temp;
                }
            }
        }

        return nums;
    }

    /**
     * Using deques for clear separation
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] rearrangeArrayDeque(int[] nums) {
        Deque<Integer> positives = new ArrayDeque<>();
        Deque<Integer> negatives = new ArrayDeque<>();

        for (int num : nums) {
            if (num > 0) {
                positives.addLast(num);
            } else {
                negatives.addLast(num);
            }
        }

        int[] result = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (i % 2 == 0) {
                result[i] = positives.removeFirst();
            } else {
                result[i] = negatives.removeFirst();
            }
        }

        return result;
    }

    /**
     * Functional approach using streams
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public int[] rearrangeArrayStream(int[] nums) {
        int[] positives = Arrays.stream(nums).filter(x -> x > 0).toArray();
        int[] negatives = Arrays.stream(nums).filter(x -> x < 0).toArray();

        return IntStream.range(0, nums.length)
                .map(i -> i % 2 == 0 ? positives[i / 2] : negatives[i / 2])
                .toArray();
    }
}
