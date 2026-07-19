package Multithreading.philosopherproblem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Runs the Dining Philosophers concurrency simulation.
 *
 * The demo creates one philosopher task per philosopher and one shared
 * ReentrantLock-backed Chopstick per table position. Philosophers run in a fixed
 * thread pool, repeatedly think, try to acquire two chopsticks, eat, and release
 * the locks until the simulation timer marks them full.
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = null;
        Philosopher[] philosophers = new Philosopher[Constants.NUMBER_OF_PHILOSOPHERS];
        Chopstick[] chopsticks = new Chopstick[Constants.NUMBER_OF_CHOPSTICKS];

        try {
            executorService = Executors.newFixedThreadPool(Constants.NUMBER_OF_PHILOSOPHERS);

            // Initialize chopsticks
            for (int i = 0; i < Constants.NUMBER_OF_CHOPSTICKS; i++) {
                chopsticks[i] = new Chopstick(i);
            }

            // Initialize philosophers and assign them chopsticks
            for (int i = 0; i < Constants.NUMBER_OF_PHILOSOPHERS; i++) {
                Chopstick leftChopstick = chopsticks[i];
                Chopstick rightChopstick = chopsticks[(i + 1) % Constants.NUMBER_OF_PHILOSOPHERS];

                philosophers[i] = new Philosopher(i, leftChopstick, rightChopstick);
                executorService.execute(philosophers[i]); // Start philosopher thread
            }

            // Let the simulation run for a fixed duration
            TimeUnit.MILLISECONDS.sleep(Constants.SIMULATION_RUNNING_TIME);

            // Signal philosophers to stop
            for (Philosopher philosopher : philosophers) {
                philosopher.setFull();
            }

        } finally {
            // Gracefully shutdown the executor service
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                TimeUnit.MILLISECONDS.sleep(1000); // Wait until all threads finish execution
            }

            // Print the number of times each philosopher has eaten
            for (Philosopher philosopher : philosophers) {
                System.out.println("Philosopher " + philosopher.getPhilosopherId() + " ate " + philosopher.getEatingCounter() + " times.");
            }
        }
    }
}
