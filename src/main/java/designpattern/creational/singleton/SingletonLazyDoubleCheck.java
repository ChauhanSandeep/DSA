package designpattern.creational.singleton;

/**
 * Singleton pattern using lazy initialization with double-checked locking.
 *
 * Intent: ensure one shared instance and provide a global access point while
 * delaying creation until the first request. Use it for shared services where a
 * single process-wide object is intentional, such as configuration or logging.
 *
 * Participants: SingletonLazyDoubleCheck owns the static instance, the private
 * constructor blocks external construction, getInstance() is the access point,
 * and Driver is the client that requests the instance.
 */
public class SingletonLazyDoubleCheck {

    // volatile ensures visibility across threads and prevents instruction reordering
    private volatile static SingletonLazyDoubleCheck sc = null;

    /** Blocks clients from creating additional instances directly. */
    private SingletonLazyDoubleCheck() {

    }

    /** Returns the single lazily created instance. */
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
