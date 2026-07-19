package Multithreading.philosopherproblem;

import java.util.concurrent.TimeUnit;

/**
 * Central configuration for the Dining Philosophers simulation.
 *
 * The values define the number of philosopher tasks, the number of shared
 * chopstick locks, and how long the App main method lets the simulation run.
 */
public final class Constants {

    // Private constructor to prevent instantiation of this utility class.
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated.");
    }

    /** Number of philosophers participating in the simulation */
    public static final int NUMBER_OF_PHILOSOPHERS = 5;

    /** Number of chopsticks available (equal to the number of philosophers) */
    public static final int NUMBER_OF_CHOPSTICKS = 5;

    /** Total time for which the simulation will run (in milliseconds) */
    public static final int SIMULATION_RUNNING_TIME = (int) TimeUnit.SECONDS.toMillis(5);
}
