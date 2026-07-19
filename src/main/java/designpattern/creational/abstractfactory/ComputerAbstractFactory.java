package designpattern.creational.abstractfactory;

/**
 * Abstract Factory pattern contract for this computer family example.
 *
 * Intent: define a creation interface that lets clients request related products
 * without naming the concrete product classes. Use it when a client should be
 * configured with a product family, such as PC hardware or server hardware, and
 * the concrete classes must stay behind factory abstractions.
 *
 * Participants: ComputerAbstractFactory is the abstract factory, PCFactory and
 * ServerFactory are concrete factories, Computer is the abstract product, PC and
 * Server are concrete products, and Driver is the client.
 */
public interface ComputerAbstractFactory {

    /** Creates one computer product for the concrete factory family. */
    public Computer createComputer();

}
