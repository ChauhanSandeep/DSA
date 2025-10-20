package designpattern.creational.abstractfactory;

/**
 * Abstract Factory Pattern - ComputerAbstractFactory Interface
 *
 * The Abstract Factory Pattern provides an interface for creating families of related
 * or dependent objects without specifying their concrete classes. It's a factory of factories.
 *
 * Pattern Structure:
 * 1. **AbstractFactory** (ComputerAbstractFactory): Declares creation methods
 * 2. **ConcreteFactory** (PCFactory, ServerFactory): Implements creation methods
 * 3. **AbstractProduct** (Computer): Interface for product types
 * 4. **ConcreteProduct** (PC, Server): Specific product implementations
 * 5. **Client** (Driver): Uses abstract interfaces, never concrete classes
 *
 * Difference from Factory Pattern:
 * - Factory Pattern: Creates objects of one type (single factory method)
 * - Abstract Factory: Creates families of related objects (multiple factory methods)
 * - Factory: Uses inheritance
 * - Abstract Factory: Uses composition
 *
 * Key Benefits:
 * - Isolates concrete classes: Client uses only abstract interfaces
 * - Easy to exchange product families: Change factory, change all products
 * - Promotes consistency: All products from one factory are compatible
 * - Follows Open/Closed Principle: Add new factories without changing existing code
 *
 * Example Scenario:
 * GUI toolkit that needs Windows vs Mac components:
 * - WindowsFactory creates WindowsButton, WindowsCheckbox, WindowsMenu
 * - MacFactory creates MacButton, MacCheckbox, MacMenu
 * - Client code uses AbstractFactory, doesn't know if it's Windows or Mac
 *
 * Real-World Examples:
 * - Database drivers: Different factories for MySQL, PostgreSQL, Oracle
 * - UI themes: Light theme vs Dark theme components
 * - Document formats: PDF creator vs Word creator vs Excel creator
 * - Game platforms: PC vs Console vs Mobile game components
 *
 * When to Use:
 * - System should be independent of how its products are created
 * - System configured with one of multiple families of products
 * - Related products designed to work together, constraint must be enforced
 * - Providing a library of products without exposing implementations
 *
 * @author Sandeep Chauhan
 */
public interface ComputerAbstractFactory {

    /**
     * Factory method to create a Computer object.
     *
     * This is the core of the Abstract Factory Pattern:
     * - Abstract method that each concrete factory implements
     * - Returns abstract product type (Computer)
     * - Concrete factories return their specific products (PC or Server)
     * - Client doesn't know which concrete class is created
     *
     * @return Computer instance (could be PC, Server, or other Computer subclass)
     */
    public Computer createComputer();

}
