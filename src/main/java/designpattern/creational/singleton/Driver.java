package designpattern.creational.singleton;

/**
 * Driver - Client Class for Singleton Pattern Demo
 *
 * This class demonstrates the Singleton pattern by showing that multiple calls
 * to getInstance() return the same instance.
 *
 * Expected Output:
 * "Getting new instance" is printed only once, proving that only one instance
 * is created despite multiple getInstance() calls.
 *
 * Key Demonstration:
 * - singleton1 and singleton2 reference the same object
 * - The instance is created lazily (only when first requested)
 * - Thread-safe creation is ensured by double-checked locking
 *
 * @author Sandeep Chauhan
 */
public class Driver {

    /**
     * Main method demonstrating Singleton pattern behavior.
     *
     * Key Observation:
     * - First getInstance() creates the instance (prints "Getting new instance")
     * - Second getInstance() returns existing instance (no print statement)
     * - Both variables reference the exact same object in memory
     */
    public static void main(String[] args) {
        // First call creates the singleton instance
        SingletonLazyDoubleCheck singleton1 = SingletonLazyDoubleCheck.getInstance();

        // Second call returns the same instance (no new creation)
        SingletonLazyDoubleCheck singleton2 = SingletonLazyDoubleCheck.getInstance();

        // Verify they're the same instance
        System.out.println("Are both instances same? " + (singleton1 == singleton2)); // Should print: true
    }
}
