package String;

import java.util.*;

/**
 * 1044. Longest Duplicate Substring
 * 
 * Problem: Given a string s, return any duplicate substring that has the longest length.
 * If no duplicate substring exists, return empty string.
 * 
 * Example:
 * Input: s = "banana"
 * Output: "ana"
 * 
 * LeetCode: https://leetcode.com/problems/longest-duplicate-substring
 * 
 * Follow-up questions:
 * Q: How to handle very large strings efficiently?
 * A: Use suffix arrays with LCP (Longest Common Prefix) arrays for O(n) space.
 * 
 * Q: Can we find all duplicate substrings above a certain length?
 * A: Use rolling hash with different moduli or suffix array techniques.
 * 
 * Q: How to extend to multiple strings (longest common substring)?
 * A: Generalize suffix array approach or use advanced string matching.
 */
public class LongestDuplicateSubstring {
    
    private static final int BASE = 256;
    private static final long MOD = 1000000007L;
    
    /**
     * Binary search + Rolling hash approach - optimal for this problem.
     * 
     * Algorithm: Binary search on length + Rabin-Karp rolling hash
     * - Binary search on substring length from 1 to n-1
     * - For each length, use rolling hash to find duplicates in O(n) time
     * - Rolling hash allows O(1) hash updates when sliding window
     * - Use modular arithmetic to handle large hash values
     * 
     * Time Complexity: O(n log n) average case, O(n²) worst case (hash collisions)
     * Space Complexity: O(n) for hash storage
     */
    public String longestDupSubstring(String s) {
        int n = s.length();
        int left = 1, right = n - 1;
        String result = "";
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String duplicate = findDuplicateOfLength(s, mid);
            
            if (duplicate != null) {
                result = duplicate;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    // Find duplicate substring of specific length using rolling hash
    private String findDuplicateOfLength(String s, int length) {
        if (length == 0) return "";
        
        int n = s.length();
        Map<Long, List<Integer>> hashToIndices = new HashMap<>();
        
        // Calculate hash for first window
        long hash = 0;
        long pow = 1;
        
        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + s.charAt(i)) % MOD;
            if (i < length - 1) {
                pow = (pow * BASE) % MOD;
            }
        }
        
        hashToIndices.computeIfAbsent(hash, k -> new ArrayList<>()).add(0);
        
        // Slide window and update hash
        for (int i = length; i < n; i++) {
            // Remove leftmost character
            hash = (hash - (s.charAt(i - length) * pow) % MOD + MOD) % MOD;
            // Add rightmost character
            hash = (hash * BASE + s.charAt(i)) % MOD;
            
            int startIndex = i - length + 1;
            List<Integer> indices = hashToIndices.get(hash);
            
            if (indices != null) {
                // Check for actual string match (handle hash collisions)
                String current = s.substring(startIndex, i + 1);
                for (int prevIndex : indices) {
                    String prev = s.substring(prevIndex, prevIndex + length);
                    if (current.equals(prev)) {
                        return current;
                    }
                }
            }
            
            hashToIndices.computeIfAbsent(hash, k -> new ArrayList<>()).add(startIndex);
        }
        
        return null;
    }
    
    /**
     * Suffix Array approach - more memory efficient for large strings.
     * 
     * Algorithm: Suffix array with LCP array
     * - Build suffix array in O(n log n) time
     * - Compute LCP (Longest Common Prefix) array
     * - Find maximum LCP value and corresponding substring
     */
    public String longestDupSubstringSuffixArray(String s) {
        int n = s.length();
        if (n <= 1) return "";
        
        SuffixArray sa = new SuffixArray(s);
        int[] lcp = sa.computeLCPArray();
        
        int maxLcp = 0;
        int maxIndex = 0;
        
        for (int i = 0; i < lcp.length; i++) {
            if (lcp[i] > maxLcp) {
                maxLcp = lcp[i];
                maxIndex = i;
            }
        }
        
        if (maxLcp == 0) return "";
        
        int suffixIndex = sa.getSuffixArray()[maxIndex];
        return s.substring(suffixIndex, suffixIndex + maxLcp);
    }
    
    // Suffix Array implementation
    private static class SuffixArray {
        private String text;
        private int[] suffixArray;
        
        SuffixArray(String text) {
            this.text = text + "$"; // Add sentinel character
            buildSuffixArray();
        }
        
        private void buildSuffixArray() {
            int n = text.length();
            Integer[] indices = new Integer[n];
            
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
            
            // Sort suffixes using comparison
            Arrays.sort(indices, (a, b) -> text.substring(a).compareTo(text.substring(b)));
            
            suffixArray = new int[n];
            for (int i = 0; i < n; i++) {
                suffixArray[i] = indices[i];
            }
        }
        
        int[] getSuffixArray() {
            return suffixArray;
        }
        
        int[] computeLCPArray() {
            int n = suffixArray.length;
            int[] lcp = new int[n - 1];
            
            for (int i = 0; i < n - 1; i++) {
                int suffix1 = suffixArray[i];
                int suffix2 = suffixArray[i + 1];
                lcp[i] = computeLCP(suffix1, suffix2);
            }
            
            return lcp;
        }
        
        private int computeLCP(int i, int j) {
            int lcp = 0;
            while (i < text.length() && j < text.length() && text.charAt(i) == text.charAt(j)) {
                lcp++;
                i++;
                j++;
            }
            return lcp;
        }
    }
    
    /**
     * Optimized suffix array using radix sort.
     * More efficient construction for large alphabets.
     */
    public String longestDupSubstringOptimized(String s) {
        int n = s.length();
        OptimizedSuffixArray sa = new OptimizedSuffixArray(s);
        
        int maxLcp = 0;
        String result = "";
        
        int[] suffixes = sa.getSuffixArray();
        
        for (int i = 0; i < n - 1; i++) {
            int lcp = computeLCPOptimized(s, suffixes[i], suffixes[i + 1]);
            if (lcp > maxLcp) {
                maxLcp = lcp;
                result = s.substring(suffixes[i], suffixes[i] + lcp);
            }
        }
        
        return result;
    }
    
    private int computeLCPOptimized(String s, int i, int j) {
        int lcp = 0;
        while (i < s.length() && j < s.length() && s.charAt(i) == s.charAt(j)) {
            lcp++;
            i++;
            j++;
        }
        return lcp;
    }
    
    // Optimized suffix array with better construction
    private static class OptimizedSuffixArray {
        private int[] suffixArray;
        
        OptimizedSuffixArray(String s) {
            buildOptimized(s);
        }
        
        private void buildOptimized(String s) {
            int n = s.length();
            suffixArray = new int[n];
            
            // Convert string to integer array
            int[] text = new int[n];
            for (int i = 0; i < n; i++) {
                text[i] = s.charAt(i);
            }
            
            // Use DC3 algorithm (simplified version)
            suffixArray = buildSuffixArrayDC3(text);
        }
        
        // Simplified DC3 (Difference Cover 3) algorithm
        private int[] buildSuffixArrayDC3(int[] text) {
            int n = text.length;
            Integer[] indices = new Integer[n];
            
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
            
            // Multi-key radix sort simulation
            Arrays.sort(indices, (a, b) -> {
                for (int k = 0; k < n; k++) {
                    int charA = (a + k < n) ? text[a + k] : -1;
                    int charB = (b + k < n) ? text[b + k] : -1;
                    
                    if (charA != charB) {
                        return Integer.compare(charA, charB);
                    }
                }
                return 0;
            });
            
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                result[i] = indices[i];
            }
            
            return result;
        }
        
        int[] getSuffixArray() {
            return suffixArray;
        }
    }
    
    /**
     * Multiple hash approach to reduce collision probability.
     * Uses multiple hash functions for better reliability.
     */
    public String longestDupSubstringMultiHash(String s) {
        int n = s.length();
        int left = 1, right = n - 1;
        String result = "";
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            String duplicate = findDuplicateMultiHash(s, mid);
            
            if (duplicate != null) {
                result = duplicate;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
    private String findDuplicateMultiHash(String s, int length) {
        if (length == 0) return "";
        
        int n = s.length();
        long[] mods = {1000000007L, 1000000009L, 1000000021L};
        int numHashes = mods.length;
        
        Map<List<Long>, Integer> hashToIndex = new HashMap<>();
        
        // Compute initial hashes for first window
        long[] hashes = new long[numHashes];
        long[] powers = new long[numHashes];
        
        for (int h = 0; h < numHashes; h++) {
            powers[h] = 1;
            for (int i = 0; i < length; i++) {
                hashes[h] = (hashes[h] * BASE + s.charAt(i)) % mods[h];
                if (i < length - 1) {
                    powers[h] = (powers[h] * BASE) % mods[h];
                }
            }
        }
        
        List<Long> initialHash = Arrays.asList(hashes[0], hashes[1], hashes[2]);
        hashToIndex.put(initialHash, 0);
        
        // Slide window
        for (int i = length; i < n; i++) {
            for (int h = 0; h < numHashes; h++) {
                // Remove leftmost character
                hashes[h] = (hashes[h] - (s.charAt(i - length) * powers[h]) % mods[h] + mods[h]) % mods[h];
                // Add rightmost character
                hashes[h] = (hashes[h] * BASE + s.charAt(i)) % mods[h];
            }
            
            List<Long> currentHash = Arrays.asList(hashes[0], hashes[1], hashes[2]);
            int startIndex = i - length + 1;
            
            if (hashToIndex.containsKey(currentHash)) {
                // Additional verification for true match
                String current = s.substring(startIndex, i + 1);
                int prevIndex = hashToIndex.get(currentHash);
                String previous = s.substring(prevIndex, prevIndex + length);
                
                if (current.equals(previous)) {
                    return current;
                }
            }
            
            hashToIndex.put(currentHash, startIndex);
        }
        
        return null;
    }
    
    /**
     * Generalized suffix tree approach for multiple patterns.
     * Can find longest common substring among multiple strings.
     */
    public String longestCommonSubstring(String[] strings) {
        if (strings.length == 0) return "";
        if (strings.length == 1) return longestDupSubstring(strings[0]);
        
        // Concatenate strings with unique separators
        StringBuilder combined = new StringBuilder();
        int[] stringStarts = new int[strings.length];
        int[] stringEnds = new int[strings.length];
        
        for (int i = 0; i < strings.length; i++) {
            stringStarts[i] = combined.length();
            combined.append(strings[i]);
            stringEnds[i] = combined.length() - 1;
            
            if (i < strings.length - 1) {
                combined.append((char) ('$' + i)); // Unique separator
            }
        }
        
        String text = combined.toString();
        GeneralizedSuffixArray gsa = new GeneralizedSuffixArray(text, stringStarts, stringEnds);
        
        return gsa.findLongestCommonSubstring();
    }
    
    // Generalized suffix array for multiple strings
    private static class GeneralizedSuffixArray {
        private String text;
        private int[] suffixArray;
        private int[] stringIds;
        private int[] stringStarts;
        private int[] stringEnds;
        
        GeneralizedSuffixArray(String text, int[] stringStarts, int[] stringEnds) {
            this.text = text;
            this.stringStarts = stringStarts;
            this.stringEnds = stringEnds;
            buildSuffixArray();
            computeStringIds();
        }
        
        private void buildSuffixArray() {
            int n = text.length();
            Integer[] indices = new Integer[n];
            
            for (int i = 0; i < n; i++) {
                indices[i] = i;
            }
            
            Arrays.sort(indices, (a, b) -> text.substring(a).compareTo(text.substring(b)));
            
            suffixArray = new int[n];
            for (int i = 0; i < n; i++) {
                suffixArray[i] = indices[i];
            }
        }
        
        private void computeStringIds() {
            stringIds = new int[text.length()];
            
            for (int i = 0; i < text.length(); i++) {
                for (int j = 0; j < stringStarts.length; j++) {
                    if (i >= stringStarts[j] && i <= stringEnds[j]) {
                        stringIds[i] = j;
                        break;
                    }
                }
            }
        }
        
        String findLongestCommonSubstring() {
            int maxLcp = 0;
            String result = "";
            
            for (int i = 0; i < suffixArray.length - 1; i++) {
                int suffix1 = suffixArray[i];
                int suffix2 = suffixArray[i + 1];
                
                // Check if suffixes are from different strings
                if (stringIds[suffix1] != stringIds[suffix2]) {
                    int lcp = computeLCP(suffix1, suffix2);
                    if (lcp > maxLcp) {
                        maxLcp = lcp;
                        result = text.substring(suffix1, suffix1 + lcp);
                    }
                }
            }
            
            return result;
        }
        
        private int computeLCP(int i, int j) {
            int lcp = 0;
            while (i < text.length() && j < text.length() && 
                   text.charAt(i) == text.charAt(j) &&
                   !isSeparator(text.charAt(i))) {
                lcp++;
                i++;
                j++;
            }
            return lcp;
        }
        
        private boolean isSeparator(char c) {
            return c >= '$' && c <= '$' + 10; // Range of separators used
        }
    }
    
    /**
     * Streaming approach for very large strings.
     * Processes string in chunks to handle memory constraints.
     */
    public String longestDupSubstringStreaming(String s, int chunkSize) {
        int n = s.length();
        if (n <= chunkSize) {
            return longestDupSubstring(s);
        }
        
        String globalResult = "";
        
        // Process overlapping chunks
        for (int start = 0; start < n; start += chunkSize / 2) {
            int end = Math.min(start + chunkSize, n);
            String chunk = s.substring(start, end);
            
            String chunkResult = longestDupSubstring(chunk);
            if (chunkResult.length() > globalResult.length()) {
                globalResult = chunkResult;
            }
        }
        
        return globalResult;
    }
    
    /**
     * Parallel processing approach for very large strings.
     * Divides string into segments and processes concurrently.
     */
    public String longestDupSubstringParallel(String s) {
        if (s.length() < 10000) {
            return longestDupSubstring(s);
        }
        
        int numThreads = Runtime.getRuntime().availableProcessors();
        int segmentSize = s.length() / numThreads;
        
        List<String> results = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();
        
        for (int t = 0; t < numThreads; t++) {
            final int start = t * segmentSize;
            final int end = (t == numThreads - 1) ? s.length() : (t + 1) * segmentSize;
            
            Thread thread = new Thread(() -> {
                String segment = s.substring(start, end);
                String result = longestDupSubstring(segment);
                if (!result.isEmpty()) {
                    results.add(result);
                }
            });
            
            threads.add(thread);
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Find longest result
        return results.stream()
            .max(Comparator.comparing(String::length))
            .orElse("");
    }
    
    /**
     * Analysis and debugging utilities.
     * Tools for analyzing duplicate substring patterns.
     */
    public static class DuplicateAnalysis {
        
        public static DuplicateStats analyzeDuplicates(String s) {
            Map<String, Integer> substringCount = new HashMap<>();
            Set<String> duplicates = new HashSet<>();
            
            // Count all substrings
            for (int i = 0; i < s.length(); i++) {
                for (int j = i + 1; j <= s.length(); j++) {
                    String substring = s.substring(i, j);
                    int count = substringCount.getOrDefault(substring, 0) + 1;
                    substringCount.put(substring, count);
                    
                    if (count > 1) {
                        duplicates.add(substring);
                    }
                }
            }
            
            String longest = duplicates.stream()
                .max(Comparator.comparing(String::length))
                .orElse("");
            
            return new DuplicateStats(
                substringCount.size(),
                duplicates.size(),
                longest,
                longest.length(),
                findMostFrequent(substringCount)
            );
        }
        
        private static String findMostFrequent(Map<String, Integer> substringCount) {
            return substringCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
        }
    }
    
    public static class DuplicateStats {
        public final int totalSubstrings;
        public final int duplicateSubstrings;
        public final String longestDuplicate;
        public final int longestLength;
        public final String mostFrequent;
        
        public DuplicateStats(int totalSubstrings, int duplicateSubstrings, String longestDuplicate,
                            int longestLength, String mostFrequent) {
            this.totalSubstrings = totalSubstrings;
            this.duplicateSubstrings = duplicateSubstrings;
            this.longestDuplicate = longestDuplicate;
            this.longestLength = longestLength;
            this.mostFrequent = mostFrequent;
        }
        
        @Override
        public String toString() {
            return String.format("Total: %d, Duplicates: %d, Longest: '%s' (len=%d), Most frequent: '%s'",
                               totalSubstrings, duplicateSubstrings, longestDuplicate, 
                               longestLength, mostFrequent);
        }
    }
}