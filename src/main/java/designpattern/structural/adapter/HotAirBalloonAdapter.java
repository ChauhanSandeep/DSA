package designpattern.structural.adapter;

/**
 * Adapter Pattern - HotAirBalloonAdapter
 *
 * The Adapter Pattern allows incompatible interfaces to work together by wrapping
 * an object with an adapter that translates calls from one interface to another.
 *
 * Pattern Structure:
 * - Target Interface: IAircraft (the interface clients expect)
 * - Adaptee: HotAirBalloon (existing class with incompatible interface)
 * - Adapter: HotAirBalloonAdapter (this class - makes Adaptee compatible with Target)
 *
 * How This Adapter Works:
 * 1. Implements IAircraft (Target interface) - so it can be used where IAircraft is expected
 * 2. Contains HotAirBalloon instance (Adaptee) - the incompatible class we're wrapping
 * 3. Translates IAircraft.fly() calls into HotAirBalloon.fly(gas) calls
 * 4. Retrieves necessary data (gas type) from the wrapped HotAirBalloon object
 *
 * The Translation Process:
 * - Client calls: adapter.fly()  (IAircraft interface)
 * - Adapter gets gas: hotAirBalloon.inflateWithGas()
 * - Adapter delegates: hotAirBalloon.fly(gas)
 *
 * Key Benefits:
 * - Single Responsibility: Separates interface conversion from business logic
 * - Open/Closed Principle: Can introduce new adapters without changing existing code
 * - Flexibility: Can work with multiple incompatible classes
 * - Reusability: Can reuse existing classes without modification
 *
 * Real-World Examples:
 * - Java's Arrays.asList() - adapts arrays to List interface
 * - InputStreamReader - adapts byte streams to character streams
 * - JDBC-ODBC bridge - adapts JDBC calls to ODBC database API
 * - Media player adapting different audio formats
 *
 * Related Patterns:
 * - Bridge: Similar structure but different intent (separates abstraction from implementation)
 * - Decorator: Adds new functionality, doesn't change interface
 * - Facade: Simplifies interface, doesn't make incompatible interfaces compatible
 *
 * Reference: https://www.educative.io/courses/software-design-patterns-best-practices/gx2rnY2QgK9
 *
 * @author Sandeep Chauhan
 */
public class HotAirBalloonAdapter implements IAircraft {

    HotAirBalloon hotAirBalloon; // The Adaptee - object being adapted

    /**
     * Constructor that accepts the Adaptee (HotAirBalloon).
     * The adapter wraps the incompatible object to make it compatible.
     *
     * @param hotAirBalloon The HotAirBalloon instance to adapt to IAircraft interface
     */
    public HotAirBalloonAdapter(HotAirBalloon hotAirBalloon) {
        this.hotAirBalloon = hotAirBalloon;
    }

    /**
     * Implements IAircraft.fly() by adapting to HotAirBalloon's interface.
     *
     * This method performs the adaptation:
     * 1. Gets the fuel/gas from the HotAirBalloon object
     * 2. Calls HotAirBalloon's fly(gas) method with the retrieved gas
     *
     * This allows HotAirBalloon to be used wherever IAircraft is expected,
     * even though their interfaces are incompatible.
     */
    @Override
    public void fly() {
        String fuelUsed = hotAirBalloon.inflateWithGas(); // Get required parameter
        hotAirBalloon.fly(fuelUsed); // Delegate to adaptee with proper parameter
    }
}