package designpattern.creational.abstractfactory;

/**
 * Abstract Factory Pattern - ComputerFactory Main Class
 *
 * This is the main factory class that works with abstract factories.
 * It provides a static method that accepts any concrete factory and uses it
 * to create products, demonstrating the dependency inversion principle.
 *
 * Design Principles:
 * - Depends on abstraction (ComputerAbstractFactory) not concrete classes
 * - Open for extension: Add new factories without modifying this code
 * - Closed for modification: This code never needs to change
 *
 * @author Sandeep Chauhan
 */
public class ComputerFactory {

    /**
     * Creates a Computer using the provided factory.
     *
     * This method demonstrates the Abstract Factory Pattern's key benefit:
     * - Client code (this method) doesn't know which concrete factory is used
     * - Works with any factory implementing ComputerAbstractFactory
     * - New factory types can be added without changing this code
     *
     * Polymorphism in Action:
     * - Accepts ComputerAbstractFactory (abstract type)
     * - Receives PCFactory or ServerFactory (concrete types)
     * - Calls createComputer() which executes the concrete factory's implementation
     *
     * @param factory Any concrete factory implementing ComputerAbstractFactory
     * @return Computer instance created by the factory
     */
    public static Computer getComputer(ComputerAbstractFactory factory){
        return factory.createComputer();
    }
}

/**
 * PCFactory - Concrete Factory for Creating PC Objects
 *
 * Implements ComputerAbstractFactory to create PC instances with
 * specific hardware configurations.
 *
 * Encapsulation:
 * - Stores PC configuration parameters
 * - Passes them to PC constructor when creating instance
 * - Client doesn't need to know PC constructor details
 */
class PCFactory implements ComputerAbstractFactory {

    private String ram;  // RAM specification for PC
    private String hdd;  // Hard disk specification for PC
    private String cpu;  // CPU specification for PC

    /**
     * Constructor to configure the PC to be created.
     *
     * @param ram RAM specification
     * @param hdd Hard disk specification
     * @param cpu CPU specification
     */
    public PCFactory(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    /**
     * Creates and returns a PC instance with the configured specifications.
     *
     * @return New PC instance
     */
    @Override
    public Computer createComputer() {
        return new PC(ram, hdd, cpu);
    }

}

/**
 * ServerFactory - Concrete Factory for Creating Server Objects
 *
 * Implements ComputerAbstractFactory to create Server instances with
 * specific hardware configurations.
 *
 * Design Symmetry:
 * - Same structure as PCFactory
 * - Creates Server instead of PC
 * - Demonstrates how Abstract Factory supports multiple product families
 */
class ServerFactory implements ComputerAbstractFactory {

	private String ram;  // RAM specification for Server
	private String hdd;  // Hard disk specification for Server
	private String cpu;  // CPU specification for Server

	/**
	 * Constructor to configure the Server to be created.
	 *
	 * @param ram RAM specification
	 * @param hdd Hard disk specification
	 * @param cpu CPU specification
	 */
	public ServerFactory(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
	}

	/**
	 * Creates and returns a Server instance with the configured specifications.
	 *
	 * @return New Server instance
	 */
	@Override
	public Computer createComputer() {
		return new Server(ram,hdd,cpu);
	}

}

/**
 * Computer - Abstract Product
 *
 * Defines the interface for computer products created by the factories.
 * All concrete products (PC, Server) must extend this class.
 */
abstract class Computer {

    /**
     * Gets RAM specification.
     * @return RAM specification string
     */
    public abstract String getRAM();

    /**
     * Gets hard disk specification.
     * @return HDD specification string
     */
    public abstract String getHDD();

    /**
     * Gets CPU specification.
     * @return CPU specification string
     */
    public abstract String getCPU();

    /**
     * Returns string representation of computer configuration.
     * @return Formatted configuration string
     */
    @Override
    public String toString() {
        return "RAM= " + this.getRAM() + ", HDD=" + this.getHDD() + ", CPU=" + this.getCPU();
    }
}

/**
 * PC - Concrete Product
 *
 * Represents a personal computer with specific hardware specifications.
 */
class PC extends Computer {

    private String ram;
    private String hdd;
    private String cpu;

    public PC(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
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
 * Server - Concrete Product
 *
 * Represents a server computer with specific hardware specifications.
 */
class Server extends Computer {

    private String ram;
    private String hdd;
    private String cpu;

    public Server(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
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