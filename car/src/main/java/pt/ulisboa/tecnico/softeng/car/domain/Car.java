package pt.ulisboa.tecnico.softeng.car.domain;

public class Car extends Vehicle {
	public Car(String plate, int kilometers, int price, RentACar rentACar) { //NOVO
		super(plate, kilometers, price, rentACar);
	}
}
