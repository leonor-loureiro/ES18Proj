package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobject.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar {
	public static Set<RentACar> rentACars = new HashSet<>();
	
	private static int counter = 0;
	private String name;
	private String code;
	private ArrayList<Car> listCars;
	private ArrayList<Motorcycle> listMotorcycles;
	
	public RentACar(String name) {
		checkArguments(name);
		this.name = name;
		this.code = "" + Integer.toString(++counter);
	}
	
	private void checkDates(LocalDate begin, LocalDate end) {
		if (begin == null || end == null ||begin.isAfter(end))
			throw new CarException("RentACar Exception: Invalid dates");
	}
	
	private void checkArguments(String name) {
		if (name == null || !name.matches("^[a-zA-Z]"))
			throw new CarException("RentACar Exception: Invalid Name");
	}
	
	public Renting getRenting(String reference) {
		Stream<Renting> carRentings   = this.listCars.stream().flatMap(o -> o.getRentings().stream());
		Stream<Renting> motorCRenting = this.listMotorcycles.stream().flatMap(o -> o.getRentings().stream());
		Stream<Renting> listRentings  = Stream.concat(carRentings, motorCRenting);
		
		return listRentings.filter(rent -> rent.getReference().equals(reference)).findFirst().orElse(null);
	}
	
	public static Renting getRentingByReference(String reference) {
		return RentACar.rentACars.stream().map(rac -> rac.getRenting(reference)).findFirst().orElse(null);
	}
	
	public List<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		return this.listCars.stream().filter(car -> car.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public List<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		checkDates(begin, end);
		return this.listMotorcycles.stream().filter(motorC -> motorC.getRentings().stream().allMatch(rent -> !rent.conflict(begin, end))).collect(Collectors.toList());
	}
	
	public static RentingData getRentingData(String reference) {
		Renting renting = getRentingByReference(reference);
		if (renting != null)
			return new RentingData(renting);
		throw new CarException("RentACar Exception: No renting found");
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
}
	
