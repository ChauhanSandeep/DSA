package designpattern.creational.factory;

/**
 * Computer - Abstract Product in Factory Pattern
 *
 * This abstract class defines the interface for all Computer products.
 * It serves as the common contract that all concrete products (PC, Server) must follow.
 *
 * Abstract Product Role:
 * - Declares the interface for objects the factory method creates
 * - Forces all subclasses to implement common behavior
 * - Allows client code to work with products without knowing their concrete types
 *
 * Design Benefits:
 * - Polymorphism: Client can work with Computer reference regardless of actual type
 * - Extensibility: New computer types can be added easily
 * - Type Safety: Compiler ensures all products implement required methods
 *
 * @author Sandeep Chauhan
 */
public abstract class Computer {

	/**
	 * Gets RAM specification of the computer.
	 * @return RAM specification as string (e.g., "8 GB")
	 */
	public abstract String getRAM();

	/**
	 * Gets hard disk specification of the computer.
	 * @return HDD specification as string (e.g., "500 GB")
	 */
	public abstract String getHDD();

	/**
	 * Gets CPU specification of the computer.
	 * @return CPU specification as string (e.g., "2.4 GHz")
	 */
	public abstract String getCPU();

	/**
	 * Returns string representation of computer configuration.
	 * Template method that uses abstract methods to build the string.
	 * @return Formatted string with RAM, HDD, and CPU specifications
	 */
	@Override
	public String toString(){
		return "RAM= "+this.getRAM()+", HDD="+this.getHDD()+", CPU="+this.getCPU();
	}
}

/**
 * PC - Concrete Product in Factory Pattern
 *
 * Represents a personal computer with specific hardware specifications.
 * This is one of the concrete implementations that the factory can create.
 *
 * In a real application, this might include additional PC-specific features like:
 * - Graphics card specifications
 * - Monitor type
 * - Keyboard and mouse configurations
 * - Operating system type
 */
class PC extends Computer {

	private String ram;
	private String hdd;
	private String cpu;

	/**
	 * Constructor for PC.
	 * @param ram RAM specification
	 * @param hdd Hard disk specification
	 * @param cpu CPU specification
	 */
	public PC(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
	}

	@Override
	public String getRAM() {
		return this.ram;
	}

	@Override
	public String getHDD() {
		return this.hdd;
	}

	@Override
	public String getCPU() {
		return this.cpu;
	}

}

/**
 * Server - Concrete Product in Factory Pattern
 *
 * Represents a server computer with specific hardware specifications.
 * This is another concrete implementation that the factory can create.
 *
 * In a real application, servers might include additional features like:
 * - RAID configuration
 * - Redundant power supplies
 * - Network interface specifications
 * - Server management interfaces
 * - Operating system and virtualization software
 */
class Server extends Computer {

	private String ram;
	private String hdd;
	private String cpu;

	/**
	 * Constructor for Server.
	 * @param ram RAM specification
	 * @param hdd Hard disk specification
	 * @param cpu CPU specification
	 */
	public Server(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
	}

	@Override
	public String getRAM() {
		return this.ram;
	}

	@Override
	public String getHDD() {
		return this.hdd;
	}

	@Override
	public String getCPU() {
		return this.cpu;
	}

}