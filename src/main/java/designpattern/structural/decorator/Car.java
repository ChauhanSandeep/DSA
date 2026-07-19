package designpattern.structural.decorator;

/**
 * Decorator pattern component interface.
 *
 * Intent: let clients add responsibilities by wrapping a Car with other Car
 * implementations instead of creating subclasses for every feature combination.
 * Use it when features such as sports or luxury options should be stacked at
 * runtime while preserving the same interface.
 *
 * Participants: Car is the component, BasicCar is the concrete component,
 * CarDecorator is the base decorator, and SportsCar and LuxuryCar are concrete
 * decorators.
 */
public interface Car {

	/** Performs this car layer's assembly behavior. */
	public void assemble();
}

/** Concrete component that provides the base car behavior. */
class BasicCar implements Car {

	/** Prints the base car assembly step. */
	@Override
	public void assemble() {
		System.out.print("Assembling Basic Car.");
	}

}

/** Base decorator that forwards calls to the wrapped Car. */
class CarDecorator implements Car {

	protected Car car;

	/** Stores the component being decorated. */
	public CarDecorator(Car c){
		this.car=c;
	}

	/** Delegates assembly to the wrapped component. */
	@Override
	public void assemble() {
		this.car.assemble();
	}

}

/** Concrete decorator that adds sports-car behavior. */
class SportsCar extends CarDecorator {

	/** Wraps a car with sports features. */
	public SportsCar(Car c) {
		super(c);
		System.out.print("sports car features. ");
	}

	/** Delegates assembly, then adds sports features. */
	@Override
	public void assemble(){
		super.assemble();
		System.out.print(" assembling features of Sports Car. ");
	}
}

/** Concrete decorator that adds luxury-car behavior. */
class LuxuryCar extends CarDecorator {

	/** Wraps a car with luxury features. */
	public LuxuryCar(Car c) {
		super(c);
		System.out.print("luxury car features. ");
	}

	/** Delegates assembly, then adds luxury features. */
	@Override
	public void assemble(){
		super.assemble();
		System.out.print(" Adding features of Luxury Car. ");
	}
}
