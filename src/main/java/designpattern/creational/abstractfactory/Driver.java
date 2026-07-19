package designpattern.creational.abstractfactory;

/**
 * Demo client for the Abstract Factory pattern.
 *
 * Intent: show that the client asks factories for Computer products instead of
 * constructing PC or Server directly. Use this pattern when callers should be
 * insulated from the concrete classes in a product family.
 *
 * Participants: Driver is the client, ComputerFactory is the helper that accepts
 * an abstract factory, PCFactory and ServerFactory are concrete factories, and
 * Computer is the product abstraction used by the client.
 */
public class Driver {
    public static void main(String[] args) {
        testAbstractFactory();
    }

    /** Runs the abstract factory demo using the original concrete factories. */
    private static void testAbstractFactory() {
        // Create PC using PCFactory
        // Note: Client creates factory but works through abstract interface
        Computer pc = ComputerFactory.getComputer(
            new PCFactory("2 GB","500 GB","2.4 GHz")
        );

        // Create Server using ServerFactory
        // Same client code pattern, different factory = different product
        Computer server = ComputerFactory.getComputer(
            new ServerFactory("16 GB","1 TB","2.9 GHz")
        );

        // Display products
        // Client treats both products uniformly through Computer interface
        System.out.println("AbstractFactory PC Config::" + pc);
        System.out.println("AbstractFactory Server Config::" + server);

        // Benefits demonstrated:
        System.out.println("\nPattern Benefits:");
        System.out.println("1. Client doesn't use 'new PC()' or 'new Server()' directly");
        System.out.println("2. Easy to add new computer types (e.g., Laptop, Tablet)");
        System.out.println("3. Client code doesn't change when adding new products");
        System.out.println("4. All products created through consistent interface");
    }
}
