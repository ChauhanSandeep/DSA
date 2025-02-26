package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a list of products, return the top 3 lexicographically smallest suggestions for each prefix
 * of the searchWord.
 * <p>
 * LeetCode: https://leetcode.com/problems/search-suggestions-system/
 */
public class SuggestedProducts {

    public static void main(String[] args) {
        String[] products = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
        String searchWord = "mouse";
        List<List<String>> suggestions = new SuggestedProducts().suggestedProducts(products, searchWord);
        System.out.println(suggestions);
    }

    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        List<List<String>> result = new ArrayList<>();
        Arrays.sort(products);  // Sort the products lexicographically

        StringBuilder prefix = new StringBuilder();
        int start = 0;
        int len = products.length;

        for (char c : searchWord.toCharArray()) {
            prefix.append(c);
            start = lowerBinarySearch(products, start, prefix.toString());

            List<String> currentSuggestions = new ArrayList<>();
            for (int i = start; i < Math.min(start + 3, len); i++) {
                if (!products[i].startsWith(prefix.toString())) break;
                currentSuggestions.add(products[i]);
            }
            result.add(currentSuggestions);
        }
        return result;
    }

    /**
     * Finds the first occurrence of a word that is lexicographically >= prefix
     * using binary search.
     *
     * @param products Sorted list of product names
     * @param start    Start index for searching
     * @param prefix   The current prefix of the search word
     * @return The index of the first word >= prefix
     */
    private int lowerBinarySearch(String[] products, int start, String prefix) {
        int left = start, right = products.length;

        while (left < right) {
            int mid = (left + right) / 2;
            if (products[mid].compareTo(prefix) < 0) {  // Move right if mid word < prefix
                left = mid + 1;
            } else {
                right = mid;  // Keep searching in the left half
            }
        }
        return left;
    }
}
