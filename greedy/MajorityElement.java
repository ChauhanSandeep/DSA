package greedy;

import java.util.HashMap;
import java.util.Map;

public class MajorityElement {

    public static void main(String[] args) {
        int[] arr = {1, 2, 2, 1, 1};
        int res = new MajorityElement().majorityElement(arr);
        System.out.println(res);
    }

    /**
     * Using hashmap
     * Time complexity O(n), Space complexity O(n)
     */
    public int majorityElementWithMap(final int[] arr) {
        int len = arr.length;
        int target = len/2 + 1;

        Map<Integer, Integer> map = new HashMap<>();
        for(Integer element: arr) {
            map.put(element, map.getOrDefault(element, 0) + 1);
            if(map.get(element) >= target) return element;
        }

        return -1;
    }

    /**
     * Time complexity O(n), Space complexity O(n)
     */
    public int majorityElement(final int[] arr) {
        int resIndex = 0;   // tentative majority element index
        int count = 0;      // count of majority element

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == arr[resIndex]) count++;
            else count--;

            if (count == 0) {
                // Reject resIndex as tentative majority element.
                // consider current element as majority element.
                resIndex = i;
                count = 1;
            }
        }

        return arr[resIndex];
    }
}
