package DailyBytes.ArrayPackage;

public class ValidAnagram {

    public static void main(String[] args) {
        System.out.println(validAnagrams("cat", "tac"));
        System.out.println(validAnagrams("listen", "silent"));
        System.out.println(validAnagrams("program", "function"));
    }

    public static boolean validAnagrams(String str1, String str2) {
        int[] alphabets = new int[26];
        for(char c: str1.toCharArray()) {
            alphabets[c - 'a'] ++;
        }

        for(char c: str2.toCharArray()) {
            alphabets[c - 'a'] --;
        }
        for(int i : alphabets) {
            if(i !=0) return false;
        }
        return true;
    }
}
