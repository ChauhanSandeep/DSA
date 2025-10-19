package designpattern.creational.abstractfactory;

/**
 * Driver - Client Class for Abstract Factory Pattern Demo
 *
 * This class demonstrates the Abstract Factory Pattern by showing how client code
 * can create different product families (PC, Server) without knowing their concrete
 * classes.
 *
 * Key Demonstration:
 * - Client works with abstract interfaces (ComputerAbstractFactory, Computer)
 * - Client doesn't use 'new PC()' or 'new Server()' directly
 * - Client doesn't know which concrete factory or product class is being used
 * - Easy to switch between product families by changing the factory
 *
 * Pattern Flow:
 * 1. Create concrete factory (PCFactory or ServerFactory)
 * 2. Pass factory to ComputerFactory.getComputer()
 * 3. Factory creates and returns the appropriate product
 * 4. Client uses product through abstract interface (Computer)
 *
 * @author Sandeep Chauhan
 */
public class Driver {
    public static void main(String[] args) {
        testAbstractFactory();
    }

    /**
     * Demonstrates Abstract Factory Pattern usage.
     *
     * Shows:
     * - Creating different product types using different factories
     * - Working with products through abstract interface
     * - Complete isolation from concrete implementations
     *
     * Pattern Benefits Shown:
     * - PCFactory creates PC instances
     * - ServerFactory creates Server instances
     * - ComputerFactory doesn't know or care which factory it receives
     * - Client code works with Computer interface, not concrete classes
     */
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
