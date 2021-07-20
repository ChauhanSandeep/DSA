package Recursion;

import java.util.ArrayList;
import java.util.List;

public class LetterCombination {
    public static void main(String[] args) {
        System.out.println("All string combinations for provided numbers are "+ letterCombinations("23"));
        System.out.println("All string combinations for provided numbers are "+ letterCombinations("222"));
    }

    /**
     * Given a string containing digits from 2-9 inclusive, return all
     * possible letter combinations that the number could represent in telephone
     * @param digits
     * @return
     */
    public static List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if(digits == null || digits.length() == 0) return result;

        String[] mapping = {
                "0",
                "1",
                "abc",
                "def",
                "ghi",
                "jkl",
                "mno",
                "pqrs",
                "tuv",
                "wxyz"
        };

        letterCombinationRec(digits, mapping, 0, "", result);
        return result;

    }

    public static void letterCombinationRec(String digits, String[] mapping, int index, String current, List<String> result) {
        if(index == digits.length()) {
            result.add(current);
            return;
        }

        String letters = mapping[digits.charAt(index) - '0'];
        for(Character c: letters.toCharArray()) {
            letterCombinationRec(digits, mapping, index+1, current + c, result);
        }
    }
}
