package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a list of products. When a user starts typing a string, show most relevant 3 products from list.
 *
 * https://leetcode.com/problems/search-suggestions-system/
 */
public class SuggestedProducts {

    public static void main(String[] args) {
        String[] products = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        String searchWord = "mouse";
        List<List<String>> lists = new SuggestedProducts().suggestedProducts(products, searchWord);
        System.out.println(lists);
    }

    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        List<List<String>> result = new ArrayList<>();
        Arrays.sort(products);

        String prefix = "";
        char[] chars = searchWord.toCharArray();
        int start = 0;
        int len = products.length;

        for (char c : chars) {
            prefix += c;
            start = lowerBinarySearch(products, start, prefix);

            List<String> current = new ArrayList<>();
            for (int i = start; i < Math.min(start + 3, len); i++) {
                if (products[i].length() < prefix.length() || !products[i].startsWith(prefix)) {
                    break;
                }
                current.add(products[i]);
            }
            result.add(current);
        }
        return result;
    }

    public int lowerBinarySearch(String[] products, int start, String word) {
        int first = start;
        int last = products.length;
        int mid;

        while (first < last) {
            mid = (first + last) / 2;
            if (products[mid].compareTo(word) >= 0) {
                last = mid;
            } else {
                first = mid + 1;
            }
        }
        return first;
    }
}
