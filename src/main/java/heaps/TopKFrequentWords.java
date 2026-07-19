package heaps;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Problem: Top K Frequent Words
 *
 * Given words and k, return the k most frequent words sorted by decreasing
 * frequency. Words with the same frequency must appear in lexicographical order.
 *
 * Leetcode: https://leetcode.com/problems/top-k-frequent-words/ (Medium)
 * Rating:   acceptance 60.4% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Heap | Frequency map | Lexicographic tie-break
 *
 * Example:
 *   Input:  words = ["i","love","leetcode","i","love","coding"], k = 2
 *   Output: ["i","love"]
 *   Why:    both words appear twice, and "i" comes before "love" lexicographically.
 *
 * Follow-ups:
 *   1. How do you handle data too large for memory?
 *      Count per shard, merge partial counts, then run the top-k heap globally.
 *   2. What if queries are prefix-based?
 *      Store counts in a trie and run top-k within the prefix subtree.
 *   3. What if words arrive in a stream?
 *      Maintain counts plus a lazily refreshed heap or use approximate sketches.
 *   4. How do you make ties locale-aware?
 *      Replace String.compareTo with a Collator-based comparator.
 *
 * Related: Top K Frequent Elements (347), Sort Characters By Frequency (451).
 */

public class TopKFrequentWords {

    public static void main(String[] args) {
        TopKFrequentWords solver = new TopKFrequentWords();
        String[][] inputs = { {"i", "love", "leetcode", "i", "love", "coding"}, {"solo"} };
        int[] kValues = {2, 1};
        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("i", "love"));
        expected.add(Arrays.asList("solo"));

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = solver.topKFrequent(inputs[i], kValues[i]);
            System.out.printf("words=%s k=%d -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), kValues[i], got, expected.get(i));
        }
    }

        /**
     * Intuition: keep the best k words, but make the heap root the weakest kept
     * word. Lower frequency is weaker; for equal frequency, lexicographically
     * larger words are weaker because the final answer wants smaller words first.
     *
     * Algorithm:
     *   1. Return an empty list for null, empty, or non-positive k input.
     *   2. Count every word's frequency in a HashMap.
     *   3. Offer unique words into a min heap using frequency and reverse lexicographic tie-break.
     *   4. Poll when heap size exceeds k, then add polled words to the front of result.
     *
     * Time:  O(n log k) - each unique word may update a heap of size k.
     * Space: O(n) - the frequency map and result storage scale with unique words.
     *
     * @param words input words
     * @param k number of words to return
     * @return k most frequent words in required order
     */

    public List<String> topKFrequent(String[] words, int k) {
        if (words == null || words.length == 0 || k <= 0) {
            return new ArrayList<>();
        }

        // Count frequency of each word
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        // Min heap with custom comparator
        // Priority: frequency (ascending), then lexicographical (descending for heap property)
        PriorityQueue<String> minHeap = new PriorityQueue<>((w1, w2) -> {
            int freq1 = frequencyMap.get(w1);
            int freq2 = frequencyMap.get(w2);

            if (freq1 != freq2) {
                return Integer.compare(freq1, freq2); // Lower frequency first
            }
            return w2.compareTo(w1); // Reverse lexicographical for min heap
        });

        // Add words to heap, maintaining only k elements
        for (String word : frequencyMap.keySet()) {
            minHeap.offer(word);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Extract results and reverse to get correct order
        List<String> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(0, minHeap.poll()); // Add to beginning to reverse order
        }

        return result;
    }

    /**
     * Alternative approach using max heap for more intuitive ordering.
     *
     * Time Complexity: O(n log n) where n is number of unique words. Because of sorting
     * Space Complexity: O(n)
     */
    public List<String> topKFrequentMaxHeap(String[] words, int k) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        // Max heap with correct ordering
        PriorityQueue<String> maxHeap = new PriorityQueue<>((w1, w2) -> {
            int freq1 = frequencyMap.get(w1);
            int freq2 = frequencyMap.get(w2);

            if (freq1 != freq2) {
                return Integer.compare(freq2, freq1); // Higher frequency first
            }
            return w1.compareTo(w2); // Lexicographical order for same frequency
        });

        maxHeap.addAll(frequencyMap.keySet());

        List<String> result = new ArrayList<>();
        for (int i = 0; i < k && !maxHeap.isEmpty(); i++) {
            result.add(maxHeap.poll());
        }

        return result;
    }

    /**
     * Bucket sort approach for O(n) time complexity when k is small.
     *
     * Time Complexity: Average case O(n), worst case O(n log n) because of sorting
     * Space Complexity: O(n)
     */
    public List<String> topKFrequentBucketSort(String[] words, int k) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        // Create buckets for each possible frequency
        List<String>[] buckets = new ArrayList[words.length + 1];

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String key = entry.getKey();
            int frequency = entry.getValue();
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(key);
        }

        // Sort words within each bucket lexicographically
        for (List<String> bucket : buckets) {
            if (bucket != null) {
                bucket.sort(String::compareTo);
            }
        }

        // Collect results from highest frequency buckets
        List<String> result = new ArrayList<>();
        for (int freq = buckets.length - 1; freq >= 0 && result.size() < k; freq--) {
            if (buckets[freq] != null) {
                for (String word : buckets[freq]) {
                    if (result.size() < k) {
                        result.add(word);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Trie-based approach for prefix-aware frequency counting.
     * Useful when we need to support prefix queries as well.
     */
    public List<String> topKFrequentTrie(String[] words, int k) {
        TrieNode root = new TrieNode();
        Map<String, Integer> frequencyMap = new HashMap<>();

        // Build trie and count frequencies
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            insertWord(root, word);
        }

        // Use heap to get top k
        PriorityQueue<String> minHeap = new PriorityQueue<>((w1, w2) -> {
            int freq1 = frequencyMap.get(w1);
            int freq2 = frequencyMap.get(w2);

            if (freq1 != freq2) {
                return Integer.compare(freq1, freq2);
            }
            return w2.compareTo(w1);
        });

        // Collect all words from trie
        List<String> allWords = new ArrayList<>();
        collectWords(root, new StringBuilder(), allWords);

        for (String word : allWords) {
            if (frequencyMap.containsKey(word)) {
                minHeap.offer(word);
                if (minHeap.size() > k) {
                    minHeap.poll();
                }
            }
        }

        List<String> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(0, minHeap.poll());
        }

        return result;
    }

    /**
     * Stream processing approach for real-time data.
     * Maintains top-k in a sliding window fashion.
     */
    public static class TopKFrequentWordsStream {
        private final int k;
        private final Map<String, Integer> frequencyMap;
        private final PriorityQueue<String> minHeap;

        public TopKFrequentWordsStream(int k) {
            this.k = k;
            this.frequencyMap = new HashMap<>();
            this.minHeap = new PriorityQueue<>((w1, w2) -> {
                int freq1 = frequencyMap.get(w1);
                int freq2 = frequencyMap.get(w2);

                if (freq1 != freq2) {
                    return Integer.compare(freq1, freq2);
                }
                return w2.compareTo(w1);
            });
        }

        public void addWord(String word) {
            // Update frequency
            int oldFreq = frequencyMap.getOrDefault(word, 0);
            frequencyMap.put(word, oldFreq + 1);

            // If word already in heap, remove and re-add with new frequency
            if (minHeap.contains(word)) {
                minHeap.remove(word);
            }

            minHeap.offer(word);

            // Maintain heap size
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        public List<String> getTopK() {
            List<String> result = new ArrayList<>(minHeap);
            result.sort((w1, w2) -> {
                int freq1 = frequencyMap.get(w1);
                int freq2 = frequencyMap.get(w2);

                if (freq1 != freq2) {
                    return Integer.compare(freq2, freq1); // Higher frequency first
                }
                return w1.compareTo(w2); // Lexicographical order
            });

            return result;
        }
    }

    /** Trie node used by the prefix-aware alternative. */
    static class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;

        TrieNode() {
            children = new TrieNode[26];
            isEndOfWord = false;
        }
    }

    /** Inserts one lowercase word into the trie. */
    private void insertWord(TrieNode root, String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }

    /** Collects complete words from the trie into words. */
    private void collectWords(TrieNode node, StringBuilder prefix, List<String> words) {
        if (node.isEndOfWord) {
            words.add(prefix.toString());
        }

        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                prefix.append((char) ('a' + i));
                collectWords(node.children[i], prefix, words);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    /**
     * Validates the result against problem constraints.
     *
     * @param words Original words array
     * @param result Computed result
     * @param k Expected number of results
     * @return true if result is valid
     */
    public boolean validateResult(String[] words, List<String> result, int k) {
        if (result.size() != Math.min(k, getUniqueWordCount(words))) {
            return false;
        }

        // Check if results are in correct order
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }

        for (int i = 0; i < result.size() - 1; i++) {
            String current = result.get(i);
            String next = result.get(i + 1);

            int freqCurrent = frequencyMap.get(current);
            int freqNext = frequencyMap.get(next);

            if (freqCurrent < freqNext) {
                return false; // Frequency should be decreasing
            }

            if (freqCurrent == freqNext && current.compareTo(next) > 0) {
                return false; // Lexicographical order for same frequency
            }
        }

        return true;
    }

    /** Counts distinct words in the input array. */
    private int getUniqueWordCount(String[] words) {
        return (int) java.util.Arrays.stream(words).distinct().count();
    }
}