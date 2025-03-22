package Multithreading.studentlibrary;

import java.util.Random;

/**
 * Represents a student who randomly selects books to read from the library.
 * Each student continuously picks a random book and reads it while ensuring
 * proper concurrency control.
 * 
 * Problem: Concurrency in resource sharing (students competing for books).
 * Solution: Uses locking to ensure mutual exclusion when reading books.
 * 
 * @author [Your Name]
 */
public class Student implements Runnable {

    private final int studentId;
    private final Book[] books;
    private final Random random;

    /**
     * Initializes a student with a unique ID and access to all books.
     *
     * @param id    The unique identifier for the student.
     * @param books The array of books available in the library.
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

    /**
     * Returns the student's unique ID.
     */
    public int getId() {
        return studentId;
    }

    @Override
    public String toString() {
        return "Student#" + this.studentId;
    }
}
