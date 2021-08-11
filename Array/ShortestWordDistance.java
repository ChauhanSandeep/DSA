package Array;

/**
 * Given an array of strings wordsDict and two different strings that already exist in the array word1 and word2,
 * return the shortest distance between these two words in the list.
 */
public class ShortestWordDistance {

    public static void main(String[] args) {
        String[] wordDict = {"practice", "makes", "perfect", "coding", "makes"};
        String word1 = "makes";
        String word2 = "coding";
        int result = new ShortestWordDistance().shortestDistance(wordDict, word1, word2);

        System.out.println(result);
    }
    public int shortestDistance(String[] wordsDict, String word1, String word2) {
        int word1Occ = -1;
        int word2Occ = -1;
        int result = Integer.MAX_VALUE;

        for(int i=0; i<wordsDict.length; i++) {
            String str = wordsDict[i];
            if(str.equals(word1)) {
                word1Occ = i;
            }
            if(str.equals(word2)) {
                word2Occ = i;
            }
            if(word1Occ != -1 && word2Occ != -1) {
                result = Math.min(result, Math.abs(word1Occ - word2Occ));
            }
        }
        return result;
    }
}
