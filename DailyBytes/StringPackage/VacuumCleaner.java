package DailyBytes.StringPackage;

public class VacuumCleaner {

    public static void main(String[] args) {
        boolean isOriginalPosition = isOriginalPosition("LR");
        System.out.println("Vacuum cleaner returns to original postions ? " + isOriginalPosition);

        isOriginalPosition = isOriginalPosition("URURD");
        System.out.println("Vacuum cleaner returns to original postions ? " + isOriginalPosition);

        isOriginalPosition = isOriginalPosition("RUULLDRD");
        System.out.println("Vacuum cleaner returns to original postions ? " + isOriginalPosition);
    }

    /**
     * Given string with vacuum cleaner movements, find if cleaner returns to its original position
     * @param route Series of movements
     * @return does vacuum cleaner returns to original position
     */
    public static boolean isOriginalPosition(String route) {
        int horizontal = 0;
        int vertical = 0;

        for(Character c : route.toCharArray()) {
            switch(c) {
                case 'L':
                    horizontal--;
                    break;
                case 'R':
                    horizontal ++;
                    break;
                case 'U':
                    vertical ++;
                    break;
                case 'D':
                    vertical --;
                    break;
                default:
                    break;
            }
        }
        return horizontal == 0 && vertical == 0;
    }
}
