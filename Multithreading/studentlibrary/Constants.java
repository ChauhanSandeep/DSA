package Multithreading.studentlibrary;

/**
 * Defines global constants for the Student Library simulation.
 * 
 * These constants configure the number of students, books, and other parameters
 * used throughout the simulation.
 * 
 * @author [Your Name]
 */
public class Constants {

    // Private constructor to prevent instantiation
    private Constants() {}

    public static final int NUMBER_OF_STUDENTS = 5; // Total number of students in the library
    public static final int NUMBER_OF_BOOKS = 7;    // Total number of books available

    public static final int SIMULATION_DURATION_MS = 10_000; // Simulation running time in milliseconds
}
