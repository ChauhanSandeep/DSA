package Array;

public class BoundedRobot {

    public static void main(String[] args) {
        String instructions = "GGLLGG";
        System.out.println(isRobotBounded(instructions));
        instructions = "GG";
        System.out.println(isRobotBounded(instructions));
        instructions = "GL";
        System.out.println(isRobotBounded(instructions));

    }

    /**
     * On an infinite plane, a robot initially stands at (0, 0) and faces north. The robot can receive one of three instructions:
     * "G": go straight 1 unit;
     * "L": turn 90 degrees to the left;
     * "R": turn 90 degrees to the right.
     * The robot performs the instructions given in order, and repeats them forever.
     * Return true if and only if there exists a circle in the plane such that the robot never leaves the circle.
     */
    public static boolean isRobotBounded(String instructions) {
        int horizontal = 0;
        int vertical = 0;
        int direction = 0;

        for(Character c: instructions.toCharArray()) {
            switch(c){
                case 'L':
                    direction = (direction + 3) %4;
                    break;
                case 'R':
                    direction = (direction + 1)%4;
                    break;
                case 'G':
                    if (direction == 0) vertical ++;
                    else if(direction == 1) horizontal++;
                    else if(direction == 2) vertical--;
                    else if(direction == 3) horizontal--;
                    else throw new RuntimeException("Invalid direction");
            }
        }
        return (horizontal == 0 && vertical == 0)
                || (direction != 0);
        // (direction != 0) is required because we are considering robot coming back to
        // original position after running same instructions multiple times

    }
}
