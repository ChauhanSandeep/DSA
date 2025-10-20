package designpattern.structural.adapter;

/**
 * IAircraft - Target Interface in Adapter Pattern
 *
 * This interface represents the expected interface that clients want to use.
 * In the Adapter Pattern, this is called the "Target" interface.
 *
 * Purpose:
 * - Defines the domain-specific interface that Client uses
 * - Establishes the contract that all aircraft (including adapted ones) must follow
 * - Allows polymorphic treatment of different aircraft types
 *
 * Adapter Pattern Context:
 * - Target: IAircraft (this interface)
 * - Adaptee: HotAirBalloon (incompatible class we want to adapt)
 * - Adapter: HotAirBalloonAdapter (makes HotAirBalloon compatible with IAircraft)
 * - Client: Code that uses IAircraft interface
 *
 * @author Sandeep Chauhan
 */
public interface IAircraft {
    /**
     * Makes the aircraft fly.
     * All aircraft implementations must provide this method.
     */
    void fly();
}