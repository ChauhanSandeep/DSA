package StackQueue;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * https://leetcode.com/problems/lfu-cache/
 */
public class LfuCacheTest {
    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);
        System.out.println(cache.get(2));
        System.out.println(cache.get(3));
        cache.put(4, 4);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));

    }
}

class LFUCache {
    public Map<Integer, Integer> keyValueMap;
    public Map<Integer, Integer> keyFreqMap;
    public Map<Integer, LinkedHashSet<Integer>> freqKeysMap;
    int capacity;
    int minFreq = -1;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        keyValueMap = new HashMap<>();
        keyFreqMap = new HashMap<>();
        freqKeysMap = new HashMap<>();
        freqKeysMap.put(1, new LinkedHashSet<>());
    }

    public int get(int key) {
        if(!keyValueMap.containsKey(key)) return -1;

        int freq = keyFreqMap.get(key);
        // update keyFreqMap
        keyFreqMap.put(key, freq+1);

        // update freqKeysMap
        freqKeysMap.get(freq).remove(key);
        if(!freqKeysMap.containsKey(freq+1)) {
            freqKeysMap.put(freq+1, new LinkedHashSet<>());
        }
        freqKeysMap.get(freq+1).add(key);

        // update min Freq
        if(freq == minFreq && freqKeysMap.get(freq).size() == 0) {
            minFreq++; // minFreq can never jump by more than 1, since updating an item only increments its count by 1
        }
        return keyValueMap.get(key);
    }

    public void put(int key, int value) {
        if(capacity <= 0)return;

        if(keyValueMap.containsKey(key)) {
            // key already exists
            keyValueMap.put(key, value);
            get(key);
        }else{
            // adding key for first time
            if(keyValueMap.size() >= capacity) {
                int deleteKey = freqKeysMap.get(minFreq).iterator().next();
                freqKeysMap.get(minFreq).remove(deleteKey);
                keyValueMap.remove(deleteKey);
                keyFreqMap.remove(deleteKey);
            }

            keyValueMap.put(key,value);
            keyFreqMap.put(key,1);
            freqKeysMap.get(1).add(key);
            minFreq = 1;
        }
    }
}