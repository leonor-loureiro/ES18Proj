package pt.ulisboa.tecnico.softeng.car;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public class RentACar {
	private String _name;
	private String _code;
	private ArrayList<Car> _listCars;
	private ArrayList<Motorcycle> _listMotorcycles;
	
	public RentACar(String name) {
		this._name = name;
	}
	
	public Renting getRenting(String reference) {
		//TODO stub
		return null;
	}
	
	public List<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		//TODO stub
		return null;
	}
	
	public List<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		//TODO stub
		return null;
	}
	
	public RentingData getRentingData(String reference) {
		//TODO stub
		return null;
	}

}
	
