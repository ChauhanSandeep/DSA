package strings.greedy;

import java.util.*;

/**
 * Problem: Hand of Straights
 *
 * Given card values and a group size, decide whether every card can be
 * rearranged into groups of exactly groupSize consecutive values. Each card must
 * be used once.
 *
 * Leetcode: https://leetcode.com/problems/hand-of-straights/ (Medium)
 * Rating:   acceptance 57.1% (Medium), contest rating 1565
 * Pattern:  Greedy | Ordered frequency map | Consecutive grouping
 *
 * Example:
 *   Input:  hand = [1,2,3,6,2,3,4,7,8], groupSize = 3
 *   Output: true
 *   Why:    the cards can become [1,2,3], [2,3,4], and [6,7,8].
 *
 * Follow-ups:
 *   1. What if group sizes can vary?
 *      The fixed greedy no longer applies; this becomes search or DP over remaining cards.
 *   2. Return the actual groups instead of a boolean?
 *      Record each consecutive run while decrementing the same frequency map.
 *   3. Handle a massive value range with few cards?
 *      Keep the TreeMap approach because it stores only values that appear.
 *   4. What if cards arrive online?
 *      Buffer counts and greedily close groups only when smaller values can no longer arrive.
 *
 * Related: Divide Array in Sets of K Consecutive Numbers (1296), Split Array into Consecutive Subsequences (659).
 */
public class HandOfStraights {

    public static void main(String[] args) {
        HandOfStraights solver = new HandOfStraights();
        int[][] hands = {
            {1, 2, 3, 6, 2, 3, 4, 7, 8},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4}
        };
        int[] groupSizes = {3, 4, 1};
        boolean[] expected = {true, false, true};

        for (int i = 0; i < hands.length; i++) {
            boolean got = solver.isNStraightHand(hands[i], groupSizes[i]);
            System.out.printf("hand=%s groupSize=%d -> %s  expected=%s%n",
                java.util.Arrays.toString(hands[i]), groupSizes[i], got, expected[i]);
        }
    }


    /**
     * Intuition: the smallest remaining card has no smaller card available to sit
     * before it, so any valid arrangement must start one group at that value. If the
     * next groupSize consecutive values are not all available, that smallest card
     * cannot be placed anywhere and the hand is impossible.
     *
     * Algorithm:
     *   1. Reject hands whose size is not divisible by groupSize.
     *   2. Count card frequencies in a TreeMap so the smallest remaining card is easy to find.
     *   3. Repeatedly start from the smallest card and consume one copy of each needed consecutive value.
     *   4. Return false on the first missing value; otherwise all cards formed valid groups.
     *
     * Time:  O(n log n) - each card update touches the ordered map.
     * Space: O(n) - the frequency map can store every distinct card value.
     *
     * @param hands card values to partition into consecutive groups
     * @param groupSize required number of cards in every group
     * @return true if all cards can be partitioned into valid groups
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

