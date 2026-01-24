package strings.greedy;

import java.util.*;

/**
 * HandOfStraights.java
 *
 * Problem Statement:
 * Alice has some cards, where each card has a value written on it. She wants to rearrange the 
 * cards into groups such that each group has exactly groupSize cards, and the values in each 
 * group are consecutive integers.
 * 
 * Given an integer array hand where hand[i] is the value on the ith card, and an integer 
 * groupSize, return true if she can rearrange the cards into valid groups, or false otherwise.
 *
 * Example 1:
 * Input: hand = [1,2,3,6,2,3,4,7,8], groupSize = 3
 * Output: true
 * Explanation: Alice's hand can be rearranged as [1,2,3], [2,3,4], [6,7,8].
 *
 * Example 2:
 * Input: hand = [1,2,3,4,5], groupSize = 4
 * Output: false
 * Explanation: Cannot form groups of 4 consecutive cards.
 *
 * LeetCode link: https://leetcode.com/problems/hand-of-straights/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - What if groupSize can vary for different groups?
 *    → Problem becomes NP-hard; need backtracking or DP.
 *  - Can you handle very large card values efficiently?
 *    → Use TreeMap (already does this) instead of sorting array.
 *  - What if you need to return the actual groups formed?
 *    → Track groups as you build them in the algorithm.
 *  - How would you optimize for very small groupSize (e.g., 2)?
 *    → Can use simpler greedy without TreeMap overhead.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 1296 (Divide Array in Sets of K Consecutive Numbers): https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/
 *  - LeetCode 659 (Split Array into Consecutive Subsequences): https://leetcode.com/problems/split-array-into-consecutive-subsequences/
 *  - LeetCode 1121 (Divide Array Into Increasing Sequences): https://leetcode.com/problems/divide-array-into-increasing-sequences/
 * LeetCode Contest Rating: 1565
 **/
public class HandOfStraights {

    /**
     * Main method: Greedy approach with TreeMap (Optimal).
     * Step-by-step:
     *  1. Check if total cards divisible by groupSize (early exit if not)
     *  2. Count frequency of each card value using TreeMap (keeps sorted order)
     *  3. While cards remain:
     *     a. Pick smallest available card (TreeMap gives this automatically)
     *     b. Try to form group starting from this card
     *     c. For each of the next groupSize consecutive values:
     *        - Check if available, if not return false
     *        - Decrement count, remove from map if count becomes 0
     *  4. If all groups formed successfully, return true
     *
     * Key Insight:
     * Greedy works! Always start with the smallest available card. If we can't
     * form a consecutive group from the smallest card, it's impossible because
     * that card must go somewhere and can only fit in a consecutive sequence.
     *
     * Algorithm: Greedy with TreeMap.
     * Time Complexity: O(n log n), TreeMap operations are O(log n), done n times.
     * Space Complexity: O(n) for TreeMap storing unique card values.
     */
    public boolean isNStraightHand(int[] hands, int groupSize) {
        if (hands.length % groupSize != 0) {
            return false;
        }

        // TreeMap keeps cards sorted by value
        TreeMap<Integer, Integer> freqMap = new TreeMap<>();
        for (int card : hands) {
            freqMap.put(card, freqMap.getOrDefault(card, 0) + 1);
        }

        while (!freqMap.isEmpty()) {
            // Get smallest card value available
            int firstCard = freqMap.firstKey();

            // Try to form a group starting from firstCard
            for (int i = 0; i < groupSize; i++) {
                int currentCard = firstCard + i;

                if (!freqMap.containsKey(currentCard)) {
                    // Missing consecutive card, impossible to form group
                    return false;
                }

                // Use one instance of this card
                int count = freqMap.get(currentCard);
                if (count == 1) {
                    freqMap.remove(currentCard);
                } else {
                    freqMap.put(currentCard, count - 1);
                }
            }
        }

        return true;
    }

    /**
     * Alternative method: Sorting with HashMap (More explicit).
     * Step-by-step:
     *  1. Sort the hand array
     *  2. Count frequencies using HashMap
     *  3. For each card in sorted order:
     *     a. If count is 0, skip (already used)
     *     b. Otherwise, try to form group starting from this card
     *     c. For next groupSize consecutive values, decrement counts
     *  4. Return true if all groups formed
     *
     * Key Insight:
     * Sorting ensures we process cards in ascending order, same effect as TreeMap.
     * Explicitly iterate through sorted array instead of using TreeMap's ordering.
     *
     * Algorithm: Sort + HashMap.
     * Time Complexity: O(n log n) for sorting + O(n) iteration.
     * Space Complexity: O(n) for HashMap.
     */
    public boolean isNStraightHandSort(int[] hands, int groupSize) {
        if (hands.length % groupSize != 0) {
            return false;
        }

        Arrays.sort(hands);
        Map<Integer, Integer> freqMap = new HashMap<>();
        
        for (int card : hands) {
            freqMap.put(card, freqMap.getOrDefault(card, 0) + 1);
        }

        for (int card : hands) {
            // Skip if this card already used
            if (freqMap.get(card) == 0) {
                continue;
            }

            // Try to form group starting from this card
            for (int i = 0; i < groupSize; i++) {
                int currentCard = card + i;
                int currentCardCount = freqMap.getOrDefault(currentCard, 0);

                if (currentCardCount == 0) {
                    // Missing consecutive card
                    return false;
                }

                freqMap.put(currentCard, currentCardCount - 1);
                if (freqMap.get(currentCard) == 0) {
                    freqMap.remove(currentCard);
                }
            }
        }

        return true;
    }
}

