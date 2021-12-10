package DynamicProgramming;

public class ShortestCommonSuperstring {

    public static void main(String[] args) {
        String[] arr = {"abcd", "cdef", "fgh", "de"};
        System.out.println(new ShortestCommonSuperstring().solve(arr));
    }

    public int solve(String[] arr) {
        if (arr.length == 1) {
            System.out.println(arr[0]);
            return arr[0].length();
        }

        int overlap = -1;
        int x = -1;
        int y = -1;
        int maxLen = -1;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (j == i) {
                    continue;
                }
                int currOverlap = findOverlap(arr[i], arr[j]);
                if (currOverlap > overlap || (currOverlap == overlap && maxLen < arr[i].length() + arr[j].length() - currOverlap)) {
                    overlap = currOverlap;
                    x = i;
                    y = j;
                    maxLen = arr[i].length() + arr[j].length() - currOverlap;
                }
            }
        }

        if (overlap == -1) {
            int len = 0;
            for (String str : arr)
                len += str.length();
            return len;
        }

        String merged = mergeStr(arr[x], arr[y], overlap);
        String[] next = new String[arr.length - 1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != x && i != y) {
                next[index++] = arr[i];
            }
        }
        next[index] = merged;
        return solve(next);
    }

    private int findOverlap(String str1, String str2) {
        for (int i = 0; i < str1.length(); i++) {
            if (str2.startsWith(str1.substring(i))) {
                return str1.length() - i;
            }
        }
        return -1;
    }

    private String mergeStr(String str1, String str2, int overlap) {
        StringBuilder builder = new StringBuilder();
        int index = str1.length() - overlap;
        builder.append(str1, 0, index);
        builder.append(str2);
        return builder.toString();
    }
}
