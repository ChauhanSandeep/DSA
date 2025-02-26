package Array;

public class ShortestWordDistance {

    public static void main(String[] args) {
        String[] wordsDict = {"practice", "makes", "perfect", "coding", "makes"};
        String word1 = "makes";
        String word2 = "coding";
        int result = new ShortestWordDistance().shortestDistance(wordsDict, word1, word2);
        System.out.println(result);
    }

    public int shortestDistance(String[] wordsDict, String word1, String word2) {
        int lastIndexWord1 = -1;
        int lastIndexWord2 = -1;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < wordsDict.length; i++) {
            if (wordsDict[i].equals(word1)) {
                lastIndexWord1 = i;
            } 
            if (wordsDict[i].equals(word2)) {
                lastIndexWord2 = i;
            }
            // Only calculate distance if both words have appeared at least once
            if (lastIndexWord1 != -1 && lastIndexWord2 != -1) {
                minDistance = Math.min(minDistance, Math.abs(lastIndexWord1 - lastIndexWord2));
            }
        }
        return minDistance;
    }
}
