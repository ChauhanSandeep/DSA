package codingassessment.doubletsolution;
// Given a list of words (ie a vocabulary), a start word, and an end word, determine the minimum number of valid transformations applied to the start word to arrive at the end word.
// A transformation is valid if it changes one letter from a word, and the resulting word is included in the vocabulary.
// Eg Start = APE, end = MAN, vocabulary = APE, TAP, OAT, OAR, MAN, OPT, APT, ATE, MAT
// APE -> MAN  : APE -> APT -> OPT -> OAT -> MAT -> MAN


// class Doublets {
//     public int minSteps(String beginWord, String endWord, Set<String> dictionary) {
//         // Implement this
//     }
// }

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


class DoubletsSolution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> set = new HashSet<>(wordList);
      if (!set.contains(endWord)) {
        return 0;
      }

        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);

        Set<String> visited = new HashSet<>();
        queue.add(beginWord);

        int changes = 1;

        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0; i < size; i++){
                String word = queue.poll();
              if (word.equals(endWord)) {
                return changes;
              }

                for(int j = 0; j < word.length(); j++){
                    for(int k = 'a'; k <= 'z'; k++){
                        char arr[] = word.toCharArray();
                        arr[j] = (char) k;

                        String str = new String(arr);
                        if(set.contains(str) && !visited.contains(str)){
                            queue.add(str);
                            visited.add(str);
                        }
                    }
                }
            }
            ++changes;
        }
        return -1;
    }
}