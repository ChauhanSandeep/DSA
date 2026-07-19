package designpattern.creational.factory;

/**
 * Factory pattern creator for Computer products.
 *
 * Intent: centralize the decision of which Computer subclass to instantiate so
 * clients ask for a product by type instead of calling concrete constructors.
 * Use it when creation logic should be isolated from client code and future
 * product types should be added in one place.
 *
 * Participants: ComputerFactory is the creator, Computer is the product
 * abstraction, PC and Server are concrete products, and Driver is the client.
 */
public class ComputerFactory {

	/** Creates the requested computer type, or null when the type is unknown. */
	public static Computer getComputer(String type, String ram, String hdd, String cpu){
		if("PC".equalsIgnoreCase(type)) return new PC(ram, hdd, cpu);
		else if("Server".equalsIgnoreCase(type)) return new Server(ram, hdd, cpu);

		return null; // Could throw exception instead for invalid types
	}
}
