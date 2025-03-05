package Array;

/**
 * There are n dominoes in a line, and we place each domino vertically upright. In the beginning, we push some dominoes either left ('L') or right ('R').
 * This function returns the final state of dominoes after all forces are applied.
 */
public class PushDominoes {

    public static void main(String[] args) {
        String dominoes = ".L.R...LR..L..";
        System.out.println("Optimized Output: " + pushDominoes(dominoes));
    }

    /**
     * 🔥 Two-pointer approach (Efficient In-Place Modification)
     */
    public static String pushDominoes(String dominoes) {
        char[] arr = dominoes.toCharArray();
        
        for (int i = 0, lastL = -1, lastR = -1; i <= arr.length; i++) {
            if (i == arr.length || arr[i] == 'R') {
                if (lastR > lastL) { // R.....R
                    while (lastR < i) arr[lastR++] = 'R';
                }
                lastR = i;
            } else if (arr[i] == 'L') {
                if (lastL > lastR || lastR == -1) { // L.....L
                    while (++lastL < i) arr[lastL] = 'L';
                } else { // R...L
                    lastL = i;
                    for (int lo = lastR + 1, hi = lastL - 1; lo < hi; ) {
                        arr[lo++] = 'R';
                        arr[hi--] = 'L';
                    }
                }
            }
        }
        return new String(arr);
    }

    /**
     * 🔥 Force-based approach (Uses an auxiliary forces array)
     * - Uses an integer array to track net force applied at each index.
     */
    public static String pushDominoes2(String dominoes) {
        char[] arr = dominoes.toCharArray();
        int len = arr.length;
        int[] forces = new int[len];

        int force = 0;

        // Pass 1: Compute forces from Right ('R' applies force)
        for (int i = 0; i < len; i++) {
            if (arr[i] == 'R') {
                force = len;
            } else if (arr[i] == 'L') {
                force = 0;
            } else {
                force = Math.max(force - 1, 0);
            }
            forces[i] += force;
        }

        // Pass 2: Compute forces from Left ('L' applies force)
        force = 0;
        for (int i = len - 1; i >= 0; i--) {
            if (arr[i] == 'L') {
                force = len;
            } else if (arr[i] == 'R') {
                force = 0;
            } else {
                force = Math.max(force - 1, 0);
            }
            forces[i] -= force;
        }

        // Construct result based on net forces
        StringBuilder result = new StringBuilder(len);
        for (int f : forces) {
            result.append(f > 0 ? 'R' : f < 0 ? 'L' : '.');
        }
        return result.toString();
    }
}
