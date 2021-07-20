package Graph;

import java.util.*;

/**
 * Convert source word to target word changing one character at a time.
 * All the iterations should be present in words dictionary.
 */
class FindIterations {
    public static void main(String[] args) {
        String[] words = {"but","put","big","pot","pog","pig","dog","lot"}; // bit -> but -> put -> pot -> pog
        String source = "bit", target = "pog";

        Map<String, List<String>> map = new HashMap<>();
        for(int i=0; i<words.length - 1; i++){


            for(int j= i+1; j<words.length; j++){
                if(isNeighbour(words[i], words[j])) {

                    if(map.containsKey(words[i])) {
                        List<String> tempList = map.get(words[i]);
                        tempList.add(words[j]);
                    }else{
                        List<String> tempList = new ArrayList<>();
                        tempList.add(words[j]);
                        map.put(words[i], tempList);
                    }
                }
            }

            if(isNeighbour(words[i], target)) {
                if(map.containsKey(words[i])) {
                    List<String> tempList = map.get(words[i]);
                    tempList.add(target);
                }else{
                    map.put(words[i], Arrays.asList(target));
                }
            }

        }

        System.out.println("Created the graph " + map);
        boolean isBFSPossible = false;

        for(Map.Entry<String, List<String>> entry: map.entrySet()) {
            if(isNeighbour(entry.getKey(), source)) {
                isBFSPossible = true;
                doBFS(map, entry.getKey(), source, target, 1, 0);
            }
        }
        if(!isBFSPossible) System.out.println("-1");
    }

    public static void doBFS(Map<String, List<String>> map, String key, String source, String destination, int result, int visitedNodes) {
        if(map.get(key) != null && map.get(key).contains(destination)) {
            System.out.println(result + 1);
            return;
        }
        if(visitedNodes >= map.size()) {
            System.out.println("-1");
            return;
        }

        List<String> values = map.get(key);
        if(values != null) {
            for(String str: values) {
                doBFS(map, str, source, destination, result+1, visitedNodes + 1);
            }
        }

    }

    public static boolean isNeighbour(String a, String b) {
        if(a == null || b == null || a.length() != b.length()) return false;

        int diff = 0;
        for(int i=0; i<a.length(); i++) {
            if(a.charAt(i) != b.charAt(i)) diff ++;
        }
        return diff == 1;
    }
}