package String;

import java.util.HashSet;
import java.util.Set;

/**
 * Given a string, find all the permutations
 * https://www.youtube.com/watch?v=TnZHaH9i6-0
 */
public class Permutation {

    public static void main(String[] args) {
        String str = "ABC";
        System.out.println(permute(str));
    }

    private static Set<String> permute(String str) {
        if(str.length() == 1) System.out.println(str);
        Set<String> result = new HashSet<>();
        permute(str, 0, str.length() - 1, result);
        return result;
    }

    private static void permute(String str, int left, int right, Set<String> result) {
        if (left == right) {
            result.add(str);
        } else {
            for (int i = left; i <= right; i++) {
                str = swap(str, left, i);
                permute(str, left + 1, right, result);
                str = swap(str, left, i);
            }
        }
    }

    public static String swap(String a, int i, int j) {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }


    // This approach also works for non duplicate characters
    public static void permutation(String str) {
        permutationRec("", str);
    }
    private static void permutationRec(String prefix, String str) {
        int n = str.length();
        if (n == 0) System.out.println(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutationRec(prefix + str.charAt(i), str.substring(0, i) + str.substring(i + 1, n));
        }
    }
}
