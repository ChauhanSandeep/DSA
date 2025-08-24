package designpattern.structural.decorator;

public interface Car {

	public void assemble();
}

/**
 * BASIC CAR
 */
class BasicCar implements Car {

	@Override
	public void assemble() {
		System.out.print("Assembling Basic Car.");
	}

}

/**
 * CAR DECORATOR
 */
class CarDecorator implements Car {

	protected Car car;

	public CarDecorator(Car c){
		this.car=c;
	}

	@Override
	public void assemble() {
		this.car.assemble();
	}

}

/**
 * SPORTS CAR
 */
class SportsCar extends CarDecorator {

	public SportsCar(Car c) {
		super(c);
		System.out.print("sports car features. ");
	}

	@Override
	public void assemble(){
		super.assemble();
		System.out.print(" assembling features of Sports Car. ");
	}
}

/**
 * LUXURY CAR
 */
class LuxuryCar extends CarDecorator {

	public LuxuryCar(Car c) {
		super(c);
		System.out.print("luxury car features. ");
	}

	@Override
	public void assemble(){
		super.assemble();
		System.out.print(" Adding features of Luxury Car. ");
	}
}