package DynamicProgramming.MatrixChainMul;

public class ScrambleString {

    public int isScramble(final String s1, final String s2) {
        if (s1.equals(s2)) return 1;
        if (!anagram(s1, s2)) return 0;

        for (int i = 1; i < s1.length(); i++) {
            if (isScramble(s1.substring(0, i), s2.substring(0, i)) == 1
                    && isScramble(s1.substring(i), s2.substring(i)) == 1) return 1;

            if (isScramble(s1.substring(0, i), s2.substring(s2.length() - i)) == 1
                    && isScramble(s1.substring(i), s2.substring(0, s2.length() - i)) == 1) return 1;
        }
        return 0;
    }

    private boolean anagram(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int len = s1.length();

        int[] chars = new int[256];
        for (int i = 0; i < len; i++) {
            chars[s1.charAt(i) - '0']++;
            chars[s2.charAt(i) - '0']--;
        }
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != 0) return false;
        }
        return true;
    }
}
