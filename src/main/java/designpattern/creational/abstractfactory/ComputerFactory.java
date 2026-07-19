package designpattern.creational.abstractfactory;

/**
 * Client-facing entry point for the Abstract Factory pattern example.
 *
 * Intent: depend on ComputerAbstractFactory instead of concrete factories so the
 * caller can swap product families without changing this class. Use this shape
 * when object creation should vary by family while the client code stays stable.
 *
 * Participants: this class is the factory client helper, ComputerAbstractFactory
 * is the abstract factory, PCFactory and ServerFactory provide concrete product
 * creation, and Computer is the product abstraction returned to callers.
 */
public class ComputerFactory {

    /** Delegates computer creation to the supplied abstract factory. */
    public static Computer getComputer(ComputerAbstractFactory factory){
        return factory.createComputer();
    }
}

/** Concrete factory that creates PC products with the configured specifications. */
class PCFactory implements ComputerAbstractFactory {

    private String ram;  // RAM specification for PC
    private String hdd;  // Hard disk specification for PC
    private String cpu;  // CPU specification for PC

    /** Stores the specifications used when this factory creates a PC. */
    public PCFactory(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    /** Creates a PC product for this factory family. */
    @Override
    public Computer createComputer() {
        return new PC(ram, hdd, cpu);
    }

}

/** Concrete factory that creates Server products with the configured specifications. */
class ServerFactory implements ComputerAbstractFactory {

	private String ram;  // RAM specification for Server
	private String hdd;  // Hard disk specification for Server
	private String cpu;  // CPU specification for Server

	/** Stores the specifications used when this factory creates a Server. */
	public ServerFactory(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
	}

	/** Creates a Server product for this factory family. */
	@Override
	public Computer createComputer() {
		return new Server(ram,hdd,cpu);
	}

}

/** Product abstraction shared by every computer created by the factories. */
abstract class Computer {

    /** Returns the RAM specification. */
    public abstract String getRAM();

    /** Returns the hard disk specification. */
    public abstract String getHDD();

    /** Returns the CPU specification. */
    public abstract String getCPU();

    /** Formats the computer configuration for demos and logs. */
    @Override
    public String toString() {
        return "RAM= " + this.getRAM() + ", HDD=" + this.getHDD() + ", CPU=" + this.getCPU();
    }
}

/** Concrete product representing a personal computer. */
class PC extends Computer {

    private String ram;
    private String hdd;
    private String cpu;

    /** Stores this PC's hardware specifications. */
    public PC(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    /** Returns this PC's RAM specification. */
    @Override
    public String getRAM() {
        return this.ram;
    }

    /** Returns this PC's hard disk specification. */
    @Override
    public String getHDD() {
        return this.hdd;
    }

    /** Returns this PC's CPU specification. */
    @Override
    public String getCPU() {
        return this.cpu;
    }

}

/** Concrete product representing a server computer. */
class Server extends Computer {

    private String ram;
    private String hdd;
    private String cpu;

    /** Stores this server's hardware specifications. */
    public Server(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    /** Returns this server's RAM specification. */
    @Override
    public String getRAM() {
        return this.ram;
    }

    /** Returns this server's hard disk specification. */
    @Override
    public String getHDD() {
        return this.hdd;
    }

    /** Returns this server's CPU specification. */
    @Override
    public String getCPU() {
        return this.cpu;
    }

}
