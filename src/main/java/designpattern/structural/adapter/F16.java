package designpattern.structural.adapter;

/**
 * F16 - Concrete Implementation of IAircraft
 *
 * F16 is a modern fighter jet that natively implements the IAircraft interface.
 * Unlike HotAirBalloon, it doesn't need an adapter because it already follows
 * the expected interface.
 *
 * Role in Adapter Pattern:
 * This class demonstrates that some classes may already implement the target
 * interface and work seamlessly without adaptation, while others (like
 * HotAirBalloon) require an adapter.
 *
 * Comparison:
 * - F16: Directly implements IAircraft (no adapter needed)
 * - HotAirBalloon: Incompatible interface (needs HotAirBalloonAdapter)
 *
 * @author Sandeep Chauhan
 */
public class F16 implements IAircraft{
    /**
     * Makes the F16 fighter jet fly.
     * This directly implements the IAircraft interface method.
     */
    @Override
    public void fly() {
        System.out.println("F16 is flying");
    }
}
