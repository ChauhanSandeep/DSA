package Multithreading.studentlibrary;

/**
 * Central configuration for the student-library simulation.
 *
 * The constants define how many Student tasks and Book resources are created,
 * plus how long each student keeps choosing books before leaving.
 */
public class Constants {

    // Private constructor to prevent instantiation
    private Constants() {}

    public static final int NUMBER_OF_STUDENTS = 5; // Total number of students in the library
    public static final int NUMBER_OF_BOOKS = 7;    // Total number of books available

    public static final int SIMULATION_DURATION_MS = 10_000; // Simulation running time in milliseconds
}
