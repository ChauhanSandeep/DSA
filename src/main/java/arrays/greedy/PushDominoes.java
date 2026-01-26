package arrays.greedy;

import java.util.Arrays;


/**
 * Push Dominoes - LeetCode Problem 838
 *
 * Problem Statement:
 * There are n dominoes in a line, placed vertically upright. Initially, some dominoes
 * are pushed either to the left or right simultaneously. Each second, falling dominoes
 * push adjacent dominoes in their direction. When a domino receives equal forces from
 * both sides, it remains upright. Find the final state after all dominoes stop moving.
 *
 * Example:
 * Input: dominoes = ".L.R...LR..L.."
 * Output: "LL.RR.LLRRLL.."
 * Explanation: The 'L' at position 1 causes dominoes to its left to fall left.
 * The 'R' at position 3 causes dominoes to its right to fall right until they
 * meet the force from 'L' at position 7. Forces balance at position 5, keeping it upright.
 *
 * LeetCode Link: https://leetcode.com/problems/push-dominoes/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you handle if dominoes could be pushed with different strengths?
 *    Answer: Modify force calculation to account for varying initial force magnitudes.
 *
 * 2. What if dominoes could be pushed at different times (not simultaneously)?
 *    Answer: Use BFS/simulation with timestamps to process forces in chronological order.
 *    Related: LeetCode 1136 - Parallel Courses (topological ordering with time)
 *
 * 3. How to extend this to 2D grid of dominoes?
 *    Answer: Similar force propagation but in 4 directions, using BFS from all initial sources.
 *
 * 4. What if we need to minimize the number of initial pushes to achieve a target state?
 *    Answer: Dynamic programming or backtracking to find optimal initial configuration.
 *    Related: LeetCode 1284 - Minimum Number of Flips to Convert Binary Matrix
 * LeetCode Contest Rating: 1638
 */
public class PushDominoes {

    public static void main(String[] args) {
        String dominoes = ".L.R...LR..L..";
        System.out.println("Optimized Output: " + pushDominoesForceBased(dominoes));
    }

    /**
     * Simulates the final state of dominoes after being pushed using an efficient two-pointer approach.
     *
     * Approach:
     * 1. Convert the string to a character array for in-place modification.
     * 2. Traverse the array using a single loop and track the most recent 'L' and 'R' positions.
     * 3. Handle 3 cases:
     *    - R...R → fill with 'R'
     *    - L...L → fill with 'L'
     *    - R...L → fill from both ends inward (symmetrically)
     * 4. Return the modified char array as a string.
     *
     * Time Complexity: O(n), where n = length of dominoes
     * Space Complexity: O(n) to store the char array (input is immutable)
     *
     * @param dominoes A string representing the initial state of dominoes with '.', 'L', and 'R'
     * @return A string representing the final state after all pushes have resolved
     */
    public static String pushDominoes(String dominoes) {
        char[] arr = dominoes.toCharArray();
        int lastL = -1, lastR = -1;

        // Traverse the dominoes array with one extra step to handle trailing Rs
        for (int i = 0; i <= arr.length; i++) {
            // Treat i == arr.length as a virtual 'R' boundary to flush any trailing 'R...'
            if (i == arr.length || arr[i] == 'R') {
                if (lastR > lastL) {
                    // Case: R...R → fill entire stretch with 'R'
                    while (lastR < i) {
                        arr[lastR++] = 'R';
                    }
                }
                lastR = i; // Update last seen R
            } else if (arr[i] == 'L') {
                if (lastL > lastR || lastR == -1) {
                    // Case: L...L → fill entire stretch with 'L'
                    lastL++; // start from next to previous L
                    while (lastL < i) {
                        arr[lastL++] = 'L';
                    }
                } else {
                    // Case: R...L → push from both ends inward
                    lastL = i;
                    int lo = lastR + 1;
                    int hi = lastL - 1;
                    while (lo < hi) {
                        arr[lo++] = 'R';
                        arr[hi--] = 'L';
                    }
                    // Middle domino (if any) remains '.'
                }
            }
        }

        return new String(arr);
    }

    /**
     * Simulates the final state of dominoes using a force-based approach.
     *
     * Approach:
     * 1. Use an array to simulate the net force on each domino.
     *    - Force from 'R' is positive.
     *    - Force from 'L' is negative.
     * 2. Do two linear passes:
     *    - Left to right: accumulate rightward forces.
     *    - Right to left: accumulate leftward forces.
     * 3. Compute the net force at each index to determine final state.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(n) (due to the `forces` array)
     *
     * @param dominoes A string of initial domino states ('.', 'L', 'R')
     * @return Final domino state after all forces have settled
     */
    public static String pushDominoesForceBased(String dominoes) {
        char[] arr = dominoes.toCharArray();
        int len = arr.length;
        int[] forces = new int[len];
        int force = 0;

        // 👉 Pass 1: Compute rightward forces from 'R'
        for (int i = 0; i < len; i++) {
            if (arr[i] == 'R') {
                force = len; // max force
            } else if (arr[i] == 'L') {
                force = 0;   // force blocked by 'L'
            } else {
                force = Math.max(force - 1, 0); // decay over distance
            }
            forces[i] += force;
        }
        System.out.println("After rightward pass: " + Arrays.toString(forces));

        // 👉 Pass 2: Compute leftward forces from 'L'
        force = 0;
        for (int i = len - 1; i >= 0; i--) {
            if (arr[i] == 'L') {
                force = len;
            } else if (arr[i] == 'R') {
                force = 0;
            } else {
                force = Math.max(force - 1, 0);
            }
            forces[i] -= force; // subtract because leftward is negative
        }
        System.out.println("After leftward pass: " + Arrays.toString(forces));

        // Pass 3: Build the final result based on net forces
        StringBuilder result = new StringBuilder(len);
        for (int f : forces) {
            result.append((f > 0) ? 'R' : ((f < 0) ? 'L' : '.'));
        }

        return result.toString();
    }
}
