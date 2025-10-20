package designpattern.creational.singleton;

/**
 * Singleton Pattern - Thread-Safe Lazy Initialization with Double-Checked Locking
 *
 * The Singleton pattern ensures a class has only one instance and provides a global
 * point of access to that instance. This implementation uses double-checked locking
 * to provide thread-safe lazy initialization with optimal performance.
 *
 * Key Components:
 * 1. **Private Constructor**: Prevents direct instantiation from outside the class
 * 2. **Static Instance Variable**: Holds the single instance of the class
 * 3. **Volatile Keyword**: Ensures visibility of changes across threads and prevents
 *    instruction reordering that could lead to partially constructed objects
 * 4. **Double-Checked Locking**: First check without synchronization for performance,
 *    then synchronized check to ensure thread safety during instance creation
 *
 * Why Double-Checked Locking?
 * - First null check (outside synchronized block): Avoids synchronization overhead
 *   once instance is created. Most calls will return immediately here.
 * - Synchronized block: Ensures only one thread can create the instance
 * - Second null check (inside synchronized block): Prevents multiple threads that
 *   passed first check from creating multiple instances
 *
 * Why Volatile?
 * - Prevents instruction reordering that could expose a partially constructed object
 * - Without volatile, another thread might see a non-null reference to an object
 *   that hasn't been fully initialized yet
 * - Ensures all threads see the most up-to-date value of the instance variable
 *
 * Use Cases:
 * - Database connection pools
 * - Logger instances
 * - Configuration managers
 * - Thread pools
 * - Cache managers
 *
 * Alternatives:
 * - Eager initialization: Instance created at class loading (simpler but wastes memory if unused)
 * - Enum singleton: Best approach in Java (prevents serialization/reflection issues)
 * - Bill Pugh Singleton: Uses inner static helper class (simpler, equally thread-safe)
 *
 * Thread Safety: Yes (with double-checked locking and volatile)
 * Lazy Initialization: Yes
 * Performance: Optimal (minimal synchronization overhead)
 *
 * @author Sandeep Chauhan
 */
public class SingletonLazyDoubleCheck {

    // volatile ensures visibility across threads and prevents instruction reordering
    private volatile static SingletonLazyDoubleCheck sc = null;

    /**
     * Private constructor prevents external instantiation.
     * Only the class itself can create an instance.
     */
    private SingletonLazyDoubleCheck() {

    }

    /**
     * Gets the single instance of this class.
     * Creates the instance on first call (lazy initialization).
     * Thread-safe using double-checked locking pattern.
     *
     * Flow:
     * 1. First check: if instance exists, return it immediately (no synchronization)
     * 2. If null, enter synchronized block
     * 3. Second check: another thread might have created instance while waiting for lock
     * 4. If still null, create the instance
     * 5. Return the instance
     *
     * @return The single instance of SingletonLazyDoubleCheck
     */
    public static SingletonLazyDoubleCheck getInstance() {

        if (sc == null) { // First check (no locking) - fast path for already created instance
            synchronized (SingletonLazyDoubleCheck.class) { // Acquire lock for instance creation
                if (sc == null) { // Second check (with locking) - ensure only one instance is created
                    System.out.println("Getting new instance");
                    sc = new SingletonLazyDoubleCheck();
                }
            }
        }

        return sc;
    }
}