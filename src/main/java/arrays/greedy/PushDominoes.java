package arrays.greedy;

import java.util.Arrays;


/**
 * Problem: Push Dominoes
 *
 * Dominoes begin upright, pushed left, or pushed right. All pushes propagate
 * simultaneously, and equal opposing forces leave a domino upright. Return the
 * final string once every force has settled.
 *
 * Leetcode: https://leetcode.com/problems/push-dominoes/ (Medium)
 * Rating:   acceptance 58.3% (Medium) - contest rating 1638
 * Pattern:  Greedy | Two pointers | Force simulation
 *
 * Example:
 *   Input:  dominoes = ".L.R...LR..L.."
 *   Output: "LL.RR.LLRRLL.."
 *   Why:    left-only and right-only stretches fall one way, while the R...L
 *           stretch fills inward and can leave the middle upright.
 *
 * Follow-ups:
 *   1. What if pushes have different strengths?
 *      Store force magnitudes and decay them as they propagate.
 *   2. What if pushes happen at different times?
 *      Use BFS with timestamps and resolve equal-time collisions.
 *   3. What if dominoes are in a grid?
 *      Propagate forces in four directions with multi-source BFS.
 *
 * Related: Shortest Distance from All Buildings (317), Rotting Oranges (994).
 */
public class PushDominoes {

    public static void main(String[] args) {
        String[] inputs = {".L.R...LR..L..", "RR.L", "..."};
        String[] expected = {"LL.RR.LLRRLL..", "RR.L", "..."};

        for (int i = 0; i < inputs.length; i++) {
            String got = pushDominoes(inputs[i]);
            System.out.printf("dominoes=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }



    /**
     * Intuition: settled stretches are determined only by their nearest pushed
     * boundaries. L...L and R...R fall in one direction, R...L fills inward from
     * both sides, and L...R leaves the middle upright.
     *
     * Algorithm:
     *   1. Walk the char array while tracking the last seen L and R positions.
     *   2. When a new boundary is found, fill the stretch according to the boundary pair.
     *   3. Use the extra loop step at the end to flush a trailing R stretch.
     *
     * Time:  O(n) - each domino is visited and assigned a constant number of times.
     * Space: O(n) - the immutable string is copied into a char array.
     *
     * @param dominoes initial domino states using '.', 'L', and 'R'
     * @return final domino state after all pushes settle
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
