package designpattern.creational.singleton;

/**
 * Demo client for the Singleton pattern.
 *
 * Intent: show that repeated calls to getInstance() return the same object. Use
 * this pattern only when a single shared instance is part of the design, because
 * it creates global state.
 *
 * Participants: Driver is the client and SingletonLazyDoubleCheck is the
 * singleton that controls construction and access.
 */
public class Driver {

    /** Requests the singleton twice and prints whether both references match. */
    public static void main(String[] args) {
        // First call creates the singleton instance
        SingletonLazyDoubleCheck singleton1 = SingletonLazyDoubleCheck.getInstance();

        // Second call returns the same instance (no new creation)
        SingletonLazyDoubleCheck singleton2 = SingletonLazyDoubleCheck.getInstance();

        // Verify they're the same instance
        System.out.println("Are both instances same? " + (singleton1 == singleton2)); // Should print: true
    }
}
