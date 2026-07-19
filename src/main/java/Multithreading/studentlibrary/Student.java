package Multithreading.studentlibrary;

import java.util.Random;

/**
 * Represents one student task in the library simulation.
 *
 * A student repeatedly chooses a random shared Book and calls its lock-protected
 * read method until the simulation duration expires. The Student itself owns no
 * lock; synchronization is delegated to the Book being read.
 */
public class Student implements Runnable {

    private final int studentId;
    private final Book[] books;
    private final Random random;

    /**
     * Creates a student with an id and access to the shared book array.
     *
     * @param id student display id
     * @param books shared books available in the library
     */
    public Student(int id, Book[] books) {
        this.studentId = id;
        this.books = books;
        this.random = new Random();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < Constants.SIMULATION_DURATION_MS) {
            int bookIndex = random.nextInt(Constants.NUMBER_OF_BOOKS);
            try {
                books[bookIndex].read(this);
                Thread.sleep(random.nextInt(500)); // Random short break before picking another book
            } catch (InterruptedException e) {
                System.err.println("Student " + studentId + " was interrupted while reading.");
                Thread.currentThread().interrupt(); // Restore the interrupt flag
                break;
            }
        }

        System.out.println("Student " + studentId + " has finished reading and is leaving the library.");
    }

    /** Returns this student's display id. */
    public int getId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "Student#" + this.studentId;
    }
}
