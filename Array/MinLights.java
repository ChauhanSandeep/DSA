package Array;

/**
 * Min lights required to activate to light complete corridor
 * https://www.interviewbit.com/problems/minimum-lights-to-activate/
 */
public class MinLights {
    public static void main(String[] args) {
        int[] lights = {0, 0, 1, 1, 1, 0, 0, 1};
        int solve = new MinLights().solve(lights, 3);
        System.out.println(solve);
    }

    public int solve(int[] lights, int range) {
        int size = lights.length;
        int count = 0;
        int max = 0;
        while (max < size) {
            boolean found = false;
            int left = Math.max(0, max - range + 1);
            int right = Math.min(size - 1, max + range - 1);

            for (int j = right; j >= left; j--) {
                if (lights[j] == 1) {
                    max = j + range;
                    found = true;
                    count++;
                    break;
                }
            }
            if (!found) return -1;
        }
        return count;

    }


}
