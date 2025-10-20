package designpattern.creational.factory;

/**
 * Driver - Client Class for Factory Pattern Demo
 *
 * This class demonstrates how clients use the Factory Pattern to create objects.
 * Instead of using 'new PC()' or 'new Server()' directly, the client calls the
 * factory method, which encapsulates the object creation logic.
 *
 * Benefits for Client:
 * - Doesn't need to know about concrete classes (PC, Server)
 * - Doesn't need to handle object creation logic
 * - Code is more maintainable and flexible
 * - Can easily work with new computer types without code changes
 *
 * @author Sandeep Chauhan
 */
public class Driver {

    /**
     * Main method demonstrating Factory Pattern usage.
     *
     * Notice how the client:
     * 1. Uses the factory method instead of 'new' operator
     * 2. Works with Computer interface, not concrete classes
     * 3. Doesn't need to know about PC or Server classes
     */
    public static void main(String[] args) {
        // Create a PC using the factory - client doesn't use 'new PC()'
        Computer pc = ComputerFactory.getComputer("pc","2 GB","500 GB","2.4 GHz");

        // Create a Server using the factory - client doesn't use 'new Server()'
        Computer server = ComputerFactory.getComputer("server","16 GB","1 TB","2.9 GHz");

        System.out.println("Factory PC Config::"+pc);
        System.out.println("Factory Server Config::"+server);
    }

}
