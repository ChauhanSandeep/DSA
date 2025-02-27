package DynamicProgramming.MatrixChainMul;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string str, partition str such that every substring of the partition is a palindrome.
 * Return all possible palindrome partitioning of str.
 */
public class PalindromePartitioning {

    public static void main(String[] args) {
        List<List<String>> result = new PalindromePartitioning().partition("aab");
        System.out.println(result);
    }

    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        List<String> path = new ArrayList<>();
        helper(0, s, path, result);
        return result;
    }

    public void helper(int index, String str, List<String> path, List<List<String>> result) {
        if (index == str.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = index; i < str.length(); i++) {
            if (isPalindrome(str, index, i)) {
                path.add(str.substring(index, i + 1));
                helper(i + 1, str, path, result);
                path.remove(path.size() - 1);
            }
        }
    }

    public boolean isPalindrome(String str, int start, int end) {
        while (start <= end) {
            if (str.charAt(start++) != str.charAt(end--)) return false;
        }
        return true;
    }

}
