package designpattern.creational.factory;

/**
 * Demo client for the Factory pattern.
 *
 * Intent: show that client code requests computers from ComputerFactory and uses
 * the shared Computer abstraction. Use this pattern when the caller should not
 * know which concrete product class is created.
 *
 * Participants: Driver is the client, ComputerFactory is the creator, Computer
 * is the product abstraction, and PC and Server are concrete products.
 */
public class Driver {

    /** Creates two product types through the factory and prints their configs. */
    public static void main(String[] args) {
        // Create a PC using the factory - client doesn't use 'new PC()'
        Computer pc = ComputerFactory.getComputer("pc","2 GB","500 GB","2.4 GHz");

        // Create a Server using the factory - client doesn't use 'new Server()'
        Computer server = ComputerFactory.getComputer("server","16 GB","1 TB","2.9 GHz");

        System.out.println("Factory PC Config::"+pc);
        System.out.println("Factory Server Config::"+server);
    }

}
