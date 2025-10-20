package designpattern.creational.factory;

/**
 * Factory Pattern - ComputerFactory
 *
 * The Factory Pattern is a creational design pattern that provides an interface for creating
 * objects without specifying their exact classes. It encapsulates object creation logic,
 * making the code more flexible and easier to maintain.
 *
 * Key Benefits:
 * 1. **Encapsulation**: Object creation logic is centralized in one place
 * 2. **Flexibility**: Easy to add new product types without changing client code
 * 3. **Loose Coupling**: Client code depends on interfaces, not concrete classes
 * 4. **Code Reusability**: Common creation logic can be shared
 *
 * Pattern Structure:
 * - Factory Class: ComputerFactory (this class)
 * - Product Interface: Computer (common interface)
 * - Concrete Products: PC, Server (specific implementations)
 * - Client: Calls factory method instead of using 'new' operator
 *
 * Use Cases:
 * - When object creation is complex and involves multiple steps
 * - When you don't know exact types of objects needed at compile time
 * - When you want to provide a library/framework where users can extend product types
 * - When you want to centralize object creation for consistency
 *
 * Example Real-World Applications:
 * - Database connection factories (MySQL, PostgreSQL, Oracle)
 * - Document generators (PDF, Word, Excel)
 * - Vehicle manufacturing (Car, Truck, Motorcycle)
 * - Logger factories (FileLogger, ConsoleLogger, DatabaseLogger)
 *
 * Design Principle: "Depend on abstractions, not concretions" (Dependency Inversion)
 *
 * Related Patterns:
 * - Abstract Factory: Family of related factories
 * - Builder: For complex object construction
 * - Prototype: For cloning existing objects
 *
 * @author Sandeep Chauhan
 */
public class ComputerFactory {

	/**
	 * Factory method to create Computer objects based on type.
	 *
	 * This is the core of the Factory Pattern - it decides which concrete class
	 * to instantiate based on the input parameter, hiding the creation logic
	 * from the client.
	 *
	 * Advantages of this approach:
	 * - Client doesn't need to know about PC or Server classes
	 * - Easy to add new computer types (just add new case)
	 * - Object creation is centralized and consistent
	 * - Can add validation, logging, or caching logic here
	 *
	 * @param type Type of computer ("PC" or "Server")
	 * @param ram RAM specification (e.g., "8 GB")
	 * @param hdd Hard disk specification (e.g., "500 GB")
	 * @param cpu CPU specification (e.g., "2.4 GHz")
	 * @return Computer instance (PC or Server) or null if type is invalid
	 */
	public static Computer getComputer(String type, String ram, String hdd, String cpu){
		if("PC".equalsIgnoreCase(type)) return new PC(ram, hdd, cpu);
		else if("Server".equalsIgnoreCase(type)) return new Server(ram, hdd, cpu);

		return null; // Could throw exception instead for invalid types
	}
}