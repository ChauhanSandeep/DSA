package designpattern.creational.factory;

/**
 * Product abstraction for the Factory pattern example.
 *
 * Intent: give clients one Computer type while the factory chooses the concrete
 * product class. Use this when object creation should be centralized and callers
 * should not depend on classes such as PC or Server.
 *
 * Participants: Computer is the product abstraction, PC and Server are concrete
 * products, ComputerFactory is the creator, and Driver is the client.
 */
public abstract class Computer {

	/** Returns the RAM specification. */
	public abstract String getRAM();

	/** Returns the hard disk specification. */
	public abstract String getHDD();

	/** Returns the CPU specification. */
	public abstract String getCPU();

	/** Formats the computer configuration for demo output. */
	@Override
	public String toString(){
		return "RAM= "+this.getRAM()+", HDD="+this.getHDD()+", CPU="+this.getCPU();
	}
}

/** Concrete product representing a personal computer. */
class PC extends Computer {

	private String ram;
	private String hdd;
	private String cpu;

	/** Stores this PC's hardware specifications. */
	public PC(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
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
	public Server(String ram, String hdd, String cpu){
		this.ram=ram;
		this.hdd=hdd;
		this.cpu=cpu;
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
